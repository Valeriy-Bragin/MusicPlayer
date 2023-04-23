package com.example.meriniguanplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ImageView playPauseIconImageView;
    ImageView volumeImageView;
    AudioManager audioManager;
    SeekBar volumeSeekBar;
    int currentSeekBarProgress;
    SeekBar progressSeekBar;
    TextView progressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.shakira_she_wolf);
        playPauseIconImageView = findViewById(R.id.playOrPauseImageView);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        volumeImageView = findViewById(R.id.volumeImageView);
        progressSeekBar = findViewById(R.id.progressSeekBar);
        progressTextView = findViewById(R.id.progressTextView);

        setVolumeSeekBar();
        setProgressSeekBar();
    }

    private void setVolumeSeekBar() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);


                if (progress == 0) {
                    volumeImageView.setImageResource(R.drawable.ic_baseline_volume_off_24);
                } else if (progress < maxVolume*0.2) {
                    volumeImageView.setImageResource((R.drawable.ic_baseline_volume_mute_24));
                } else if (progress < maxVolume*0.5) {
                    volumeImageView.setImageResource((R.drawable.ic_baseline_volume_down_24));
                } else {
                    volumeImageView.setImageResource(R.drawable.ic_baseline_volume_up_24);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void offOrReturnVolume(View view) {
        if (volumeSeekBar.getProgress() > 0) {
            currentSeekBarProgress = volumeSeekBar.getProgress();
            volumeSeekBar.setProgress(0);
        } else {
            volumeSeekBar.setProgress(currentSeekBarProgress);
        }
    }


    private void setProgressSeekBar() {
        progressSeekBar.setMax(mediaPlayer.getDuration());
        setTimerTaskForProgressSeekBar();
        progressSeekBar.setOnSeekBarChangeListener(createOnProgressSeekBarChangeListener());
    }

    private SeekBar.OnSeekBarChangeListener createOnProgressSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                progressTextView.setText(millsToMinsAndSecs(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private String millsToMinsAndSecs(int milliseconds) {
        int seconds = (int) ((milliseconds / 1000) % 60);
        int minutes = (int) ((milliseconds / 1000) / 60);
        return minutes + ":" + seconds;
    }

    private void setTimerTaskForProgressSeekBar() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                progressSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);
    }


    public void playOrPause(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            mediaPlayer.start();
            playPauseIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
        }
    }

    public void skipPrevious(View view) {
        mediaPlayer.seekTo(0);
        progressSeekBar.setProgress(0);
        mediaPlayer.pause();
        playPauseIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
    }

    public void skipNext(View view) {
        mediaPlayer.seekTo(mediaPlayer.getDuration());
        progressSeekBar.setProgress(mediaPlayer.getDuration());
        mediaPlayer.pause();
        playPauseIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
    }
}