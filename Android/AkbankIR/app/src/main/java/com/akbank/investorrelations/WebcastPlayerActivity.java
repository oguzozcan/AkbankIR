package com.akbank.investorrelations;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.akbank.investorrelations.utils.TimeUtil;

public class WebcastPlayerActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnErrorListener {

    //String filePostFix;
    String fileName;
    MediaPlayer mediaPlayer;
    boolean isPrepared = false;
    boolean isPlaying = false;
    boolean isPaused = false;
    SeekBar songProgressBar;
    ProgressBar progressBar;
    private Handler mHandler = new Handler();
    private int seekForwardTime = 10000; // 10000 milliseconds
    private int seekBackwardTime = 10000; // 10000 milliseconds
    ImageView pausePlayButton;
    int pauseTime;
    boolean isMuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_webcast_player, null, false);
        mContent.addView(contentView, 0);
        fileName = getIntent().getStringExtra("name");
        String date = getIntent().getStringExtra("date");
        final String filePostFix = getIntent().getStringExtra("postfix");
        TextView titleTv = (TextView) findViewById(R.id.title);
        TextView dateTv = (TextView) findViewById(R.id.date);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        assert songProgressBar != null;
        songProgressBar.setOnSeekBarChangeListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        if (fileName != null) {
            assert titleTv != null;
            titleTv.setText(fileName);
        }
        String eventDate = TimeUtil.getDateTime(date, TimeUtil.dfISO, TimeUtil.dtfOutWOTime, AkbankApp.localeTr);
        if (eventDate != null) {
            assert dateTv != null;
            dateTv.setText(eventDate);
        }
        //new MediaPlayer();
//        try{
//            //mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor.getLength());
//            //mediaPlayer.prepare();
//           // mediaPlayer.setOnCompletionListener(this);
//        } catch(Exception ex){
//            throw new RuntimeException("Couldn't load music, uh oh!");
//        }
        pausePlayButton = (ImageView) findViewById(R.id.pausePlayButton);
        final ImageView muteButton = (ImageView) findViewById(R.id.muteIcon);
        ImageView rewindButton = (ImageView) findViewById(R.id.rewindButton);
        ImageView forwardButton = (ImageView) findViewById(R.id.forwardButton);

        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying && !isPaused) {
                    //stopPlaying();
                    playMp3(filePostFix);
                } else if (isPlaying && !isPaused) {
                    if (mediaPlayer != null) {
                        isPaused = true;
                        mediaPlayer.pause();
                        pauseTime = mediaPlayer.getCurrentPosition();
                    }
                } else if (isPaused && !isPlaying) {
                    isPaused = false;
                    mediaPlayer.seekTo(pauseTime);
                    mediaPlayer.start();
                }
                isPlaying = !isPlaying;
                pausePlayButton.setImageResource(isPlaying ? R.drawable.pause_icon : R.drawable.play_button);

            }
        });

        assert muteButton != null;
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    if(isMuted){
                        unmute();
                        assert muteButton != null;
                        muteButton.setImageResource(R.drawable.mute_icon);
                    }else{
                        mute();
                        assert muteButton != null;
                        muteButton.setImageResource(R.drawable.unmute_icon);
                    }
                    isMuted = !isMuted;
                }
            }
        });

        assert forwardButton != null;
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                } else {
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
            }
        });

        assert rewindButton != null;
        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (currentPosition - seekBackwardTime >= 0) {
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                } else {
                    mediaPlayer.seekTo(0);
                }
            }
        });
    }

    public void mute() {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
        } else {
            am.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

       // am.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void unmute() {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            am.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        //am.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    @Override
    protected void setTag() {
        TAG = "WebcastPlayerActivity";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fileName != null)
            onTitleTextChange(fileName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlaying();
    }

    public void playMp3(String link) {
        if(isPlaying){
            return;
        }

        if (mediaPlayer != null) {
            if (mUpdateTimeTask != null)
                mHandler.removeCallbacks(mUpdateTimeTask);
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }


        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            //mediaPlayer = MediaPlayer.create(WebcastPlayerActivity.this, Uri.parse(AkbankApp.ROOT_URL_1 + link));
            mediaPlayer.setDataSource(this.getApplicationContext(), Uri.parse(AkbankApp.ROOT_URL_1 + link)); //TODO
            mediaPlayer.setOnPreparedListener(WebcastPlayerActivity.this);
            mediaPlayer.setOnCompletionListener(WebcastPlayerActivity.this);
            mediaPlayer.setOnBufferingUpdateListener(WebcastPlayerActivity.this);
            mediaPlayer.setOnErrorListener(this);
            //Progressbar.setVisibility(View.VISIBLE);
            //mediaPlayer.prepare();
//            isPrepared = true;
            // might take long! (for buffering, etc)   //@@
            //mediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            // mediaPlayer.prepare();
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        if(isPlaying)
            Log.d(TAG, "BUFFERING : " + percent);
//        songProgressBar.setSecondaryProgress(percent);
        if (progressBar != null) {
            if (percent == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            } else if (percent > songProgressBar.getProgress()) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        songProgressBar.setProgress(0);
        songProgressBar.setSecondaryProgress(0);
        isPlaying = false;
        pausePlayButton.setImageResource(R.drawable.play_button);
        mediaPlayer.stop();
        mediaPlayer.reset();
//        play.setVisibility(View.VISIBLE);
//        stop.setVisibility(View.GONE);
//        link = "http://server11.mp3quran.net/hawashi/002.mp3";
//        playMp3(link);
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "IS PREPERED STARTTTT : " + mediaPlayer.isPlaying());
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
//            Progressbar.setVisibility(View.INVISIBLE);
//            play.setVisibility(View.GONE);
//            stop.setVisibility(View.VISIBLE);

            isPrepared = true;
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
            updateProgressBar();
        }

    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 500);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(!isPlaying){
                return;
            }
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            //songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            //songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);
            Log.d(TAG, "UPDATE PROGRESS: " + progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(TAG, "ON ERROR");
        return false;
    }
}
