package com.akbank.investorrelations;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akbank.investorrelations.utils.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class IntroActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    //YouTubePlayerView youTubeView;
    LinearLayout splashScreen;
//    static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
//            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        //YouTubePlayerView youTubeView = getYouTubePlayerProvider();// (YouTubePlayerView) findViewById(R.id.youtubeView);
        splashScreen = (LinearLayout) findViewById(R.id.splashScreen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Initializing video player with developer key
        getYouTubePlayerProvider().initialize(Constants.DEVELOPER_KEY, IntroActivity.this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        final YouTubePlayer player, boolean wasRestored) {
        Log.d("INTRO", "onInitializationSuccess " + wasRestored);
        if (!wasRestored) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
           // int controlFlags = player.getFullscreenControlFlags();
            //setRequestedOrientation(PORTRAIT_ORIENTATION);
            //controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
           // player.setFullscreenControlFlags(controlFlags);
            //player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

            player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {
                    //Toast.makeText(IntroActivity.this, "ON PLAYING", Toast.LENGTH_SHORT).show();

                    //getYouTubePlayerProvider().setVisibility(View.VISIBLE);
                    if(splashScreen.getVisibility() != View.GONE){
                        Log.d("INTRO", "PLAYING VIDEO");
                        splashScreen.setVisibility(View.GONE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player.pause();
                                Intent start = new Intent(IntroActivity.this, ItemListActivity.class);
                                IntroActivity.this.startActivity(start);
                                IntroActivity.this.finish();
                            }
                        }, 4200);
                    }
                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onStopped() {

                }

                @Override
                public void onBuffering(boolean b) {

                }

                @Override
                public void onSeekTo(int i) {

                }
            });

            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    Log.d("INTRO", "ON LOADING");
                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    Log.d("INTRO", "ON VIDEO STARTED");

                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {
                    Log.d("INTRO", errorReason.toString());
                    Intent start = new Intent(IntroActivity.this, ItemListActivity.class);
                    IntroActivity.this.startActivity(start);
                }
            });


            if(!player.isPlaying()){
                Log.d("INTRO", "LOAD VIDEO");
                player.loadVideo("tTmU84qQPNY");
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            }

//            Handler handler1 = new Handler();
//            handler1.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 800);
        }else{
            Intent start = new Intent(IntroActivity.this, ItemListActivity.class);
            IntroActivity.this.startActivity(start);
            IntroActivity.this.finish();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            // errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
//            String errorMessage = String.format(
//                    "Error", errorReason.toString());
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
        Intent start = new Intent(IntroActivity.this, ItemListActivity.class);
        IntroActivity.this.startActivity(start);
        IntroActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.DEVELOPER_KEY, IntroActivity.this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeView);
    }
}
