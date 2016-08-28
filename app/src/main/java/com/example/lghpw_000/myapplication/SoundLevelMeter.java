package com.example.lghpw_000.myapplication;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by lghpw_000 on 2016/08/28.
 */
public class SoundLevelMeter implements Runnable {
    private static final int SAMPLE_RATE = 8000;

    private int bufferSize;
    private AudioRecord audioRecord;
    private boolean isRecording;
    private boolean isPausing;
    private double baseValue;

    public double test;
    public double tempo;
    public double avgdb;
    private short avg;

    public interface SoundLevelMeterListener {
        void onMeasure(double db,double db2);
    }

    private SoundLevelMeterListener listener;

    public SoundLevelMeter() {
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        Log.d("aaaa","aaabbbbb");
        Log.d("aaaa",String.valueOf(bufferSize));
        Log.d("aaaa",String.valueOf(audioRecord));
        listener = null;
        isRecording = true;
        baseValue = 12.0;
        test = 0;
        tempo = 0;
        pause();
    }
    public void setListener(SoundLevelMeterListener l) {
        listener = l;
    }

    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        audioRecord.startRecording();
        short[] buffer = new short[bufferSize];
        while (isRecording){
            int read = audioRecord.read(buffer, 0, bufferSize);
            Log.d("vvvvvvvvvvvvvvvvv",String.valueOf(read));
            if (read < 0) {
                Log.d("bbbbbbbbbbbbb",String.valueOf(read));
            }

            int maxValue = 0;
            long sum = 0;
            for (int i = 0; i < read; i++) {
                maxValue = Math.max(maxValue, buffer[i]);
                sum += Math.abs(buffer[i]);

            }

            avg = (short) (sum / bufferSize);

            test = 20.0 * Math.log10(maxValue / baseValue);
            avgdb = 20.0 * Math.log10(avg / baseValue);
            tempo=tempo+1;

            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            MainActivity.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onMeasure(test,avgdb);
                }
            });


        }
        audioRecord.stop();
        audioRecord.release();
    }


    public void stop() {
        isRecording = false;
    }

    public void pause() {
        if (!isPausing)
            audioRecord.stop();
        isPausing = true;
    }
}
