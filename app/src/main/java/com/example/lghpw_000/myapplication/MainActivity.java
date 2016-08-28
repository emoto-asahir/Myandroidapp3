package com.example.lghpw_000.myapplication;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SoundLevelMeter.SoundLevelMeterListener{
    private AudioManager audio;
    private int musicMaxVol;
    private int musicVol;
    private TextView curMusicVolTex;
    private TextView textView3;
    private SoundLevelMeter soundLevelMeter;

    private double testh;

    static Handler mainHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundLevelMeter = new SoundLevelMeter();
        TextView textView = (TextView) findViewById(R.id.textView2);
        curMusicVolTex = (TextView) findViewById(R.id.curMusicVolTex);
        textView3 = (TextView) findViewById(R.id.aaa);


        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        musicMaxVol = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        musicVol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        textView.setText(String.valueOf(musicMaxVol));
        curMusicVolTex.setText(String.valueOf(musicVol));
        textView3.setText("cccccccccvvvvvvv");

        mainHandler = new Handler();

        soundLevelMeter.setListener(this);
        (new Thread(soundLevelMeter)).start();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicVol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                curMusicVolTex.setText(String.valueOf(musicVol));
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audio.setStreamVolume(AudioManager.STREAM_MUSIC,6,0);
                musicVol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                curMusicVolTex.setText(String.valueOf(musicVol));

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundLevelMeter.stop();
    }

    public void onMeasure(double db,double db2){
        Log.d("onmeasure",String.valueOf(db));
        Log.d("avvvvvddd",String.valueOf(db2));
        textView3.setText(String.valueOf(db));

        if(db2 > 18.0){
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,2,0);
            musicVol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            curMusicVolTex.setText(String.valueOf(musicVol));
        }else {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,7,0);
            musicVol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            curMusicVolTex.setText(String.valueOf(musicVol));
        }

        testh = db;

    }
}
