package com.yaswanth.audioplayer;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class second extends AppCompatActivity {

Button play,pause;
SeekBar seekBar;
Handler handler;
Runnable runnable;
MediaPlayer mediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        handler = new Handler();
        seekBar = findViewById(R.id.seekBar);

        mediaplayer = MediaPlayer.create(second.this, R.raw.bensound);
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaplayer.getDuration());
                playCycle();
                mediaplayer.start();

            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input) {

                    mediaplayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addNotification();
                mediaplayer.start();
                playCycle();

            }


        });

        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mediaplayer.pause();
                playCycle();
            }


        });
    }

    private void addNotification(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"1");
        mBuilder.setSmallIcon(R.drawable.music);
        mBuilder.setContentTitle("Bensound Ukulele");
        mBuilder.setContentText("Playing Now");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }


        public void playCycle(){

            seekBar.setProgress(mediaplayer.getCurrentPosition());

            if(mediaplayer.isPlaying()){

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        playCycle();

                    }
                };
                handler.postDelayed(runnable,1000);
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        mediaplayer.start();
        playCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaplayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.release();
        handler.removeCallbacks(runnable);
    }
}
