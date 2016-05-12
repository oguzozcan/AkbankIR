package com.akbank.investorrelations;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akbank.investorrelations.fragments.DownloadDialogFragment;
import com.akbank.investorrelations.objects.InvestorDaysObject;
import com.akbank.investorrelations.utils.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class InvestorDaysDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener  {

    //private View contentView;
    private String postFix;

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
        InvestorDaysObject investorDaysObject = getIntent().getParcelableExtra("investor_days");
        setReportObject(investorDaysObject);
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

    private void setReportObject(final InvestorDaysObject daysObject ) {

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.detailItem);
        //RelativeLayout webcastLayout = (RelativeLayout) layout.findViewById(R.id.reportLayout);
        TextView description = (TextView) layout.findViewById(R.id.reportDescription);
        if(daysObject == null){
            layout.setVisibility(View.GONE);
            return;
        }
        description.setText(daysObject.getTitle());
        TextView viewButton = (TextView) layout.findViewById(R.id.viewButton);
        TextView saveButton = (TextView) layout.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", daysObject.getTitle());
                b.putString("url", daysObject.getPdf());
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Downloading));
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", daysObject.getTitle());
                b.putString("url", daysObject.getPdf());
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Opening));
            }
        });
    }

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
            Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
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
