package com.mallardduckapps.akbankir;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mallardduckapps.akbankir.utils.Constants;

public class InvestorDaysDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener  {

    //private View contentView;

    String postFix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        contentView = inflater.inflate(R.layout.activity_investor_days_detail, null, false);
//        mContent.addView(contentView, 0);
//        onTitleTextChange(getString(R.string.Sub_Menu_1_Akbank_Analyst));

        setContentView(R.layout.activity_investor_days_detail);

        postFix = getIntent().getStringExtra("postfix");
  /*      WebView webView = (WebView) findViewById(R.id.webView);
        String urlPostFix = getIntent().getStringExtra("postfix");
        // set webview properties
        WebSettings ws = webView.getSettings();
        ws.getPluginState();
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
       // ws.setUserAgent(0);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.loadUrl("https://www.youtube.com/watch?v=" + urlPostFix); //68AqHwgk2s8
        */

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        // Initializing video player with developer key
        youTubeView.initialize(Constants.DEVELOPER_KEY, this);

    }

//    @Override
//    protected void setTag() {
//        TAG = "InvestorDasyDetailActivity";
//    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(postFix);

            // Hiding player controls CHROMELESS
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
           // errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "Error", errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeView);
    }
}
