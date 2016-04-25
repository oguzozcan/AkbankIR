package com.mallardduckapps.akbankir;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.mallardduckapps.akbankir.utils.Constants;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;

public class WebActivity extends BaseActivity {

    @Override
    protected void setTag() {
        TAG = "WebActivity";
    }

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_web, null, false);
        mContent.addView(contentView, 0);
        WebView webView = (WebView) contentView.findViewById(R.id.webView);
        TextView textView = (TextView) contentView.findViewById(R.id.htmlTitle);
        PDFView pdfView = (PDFView) contentView.findViewById(R.id.pdfview);
        //pdfView.
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(124);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        String fileName = getIntent().getStringExtra("file_name");
        String uri = getIntent().getStringExtra("uri");
        title = getIntent().getStringExtra("title");
        String type = getIntent().getStringExtra("type");
        int position = getIntent().getIntExtra("position", 0);
        if(title != null){
            TAG = title;
        }

        if(type.equals("web")){
            //webView.loadUrl("file:///android_asset/html/"+fileName);
            //StringEscapeUtils.unescapeJson(comment.getComment()
            boolean isLocalHtml = false;
            String html = "";
            switch (position){
                case 0:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_CORPORATE_GOVERNANCE);
                    Log.d(TAG, "HTML: " + html);
                    isLocalHtml = true;
                    break;
                case 1:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ARTICLES_OF_ASSOCIATION);
                    isLocalHtml = true;
                    break;
                case 3:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ANNUAL_GENERAL_MEETING_DOCUMENTS);
                    isLocalHtml = true;
                    break;
                case 4:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ABOUT_SUZAN_SABANCI);
                    isLocalHtml = true;
                    break;
                case 5:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_CAPITAL_TRADE_REGISTRY_INFORMATION);
                    isLocalHtml = true;
                    break;
                case 6:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ETHICAL_PRINCIPLES);
                    isLocalHtml = true;
                    break;
                case 7:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_COMPLIANCE);
                    isLocalHtml = true;
                    break;
                case 8:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_COMPANSATION);
                    isLocalHtml = true;
                    break;
                case 9:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_DONATION_CONTRUBUTION);
                    isLocalHtml = true;
                    break;
                case 10:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_DISCLOSURE_POLICY);
                    isLocalHtml = true;
                    break;
                case 11:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_DIVIDENT_POLICY);
                    isLocalHtml = true;
                    break;
            }
            //webView.loadData(html,"text/html", "UTF-8");
            if(isLocalHtml){
                Spanned htmlAsSpanned = Html.fromHtml(html);
                textView.setText(htmlAsSpanned);
                webView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                //webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            }else{
                textView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=http://akbank.steelkiwi.com" + fileName);
                Log.d(TAG, "URL: " + "http://akbank.steelkiwi.com" + fileName);
            }

            pdfView.setVisibility(View.GONE);

        }else if(type.equals("pdf")){
            webView.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            //pdfView.setCameraDistance(20f);

            //webView.loadUrl("http://akbank.steelkiwi.com/media/reports/anti-bribery-anti-corruption-policy.pdf");
            if(fileName != null){
                pdfView.zoomTo(2f);
                pdfView.toCurrentScale(1.4f);
                pdfView.fromAsset(fileName)
                        //.pages(0, 2, 1, 3, 3, 3)
                        .defaultPage(1)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .swipeVertical(true)
                                //.onDraw(onDrawListener)
                                //.onLoad(onLoadCompleteListener)
                                //.onPageChange(onPageChangeListener)
                        .load();
            }else{
//                File direct = new File(Environment.getExternalStorageDirectory()
//                        + "/akbank_files");
                pdfView.toCurrentScale(1.4f);
                pdfView.zoomTo(2f);
                pdfView.fromFile(new File(Uri.parse(uri).getPath()))
                        //.pages(0, 2, 1, 3, 3, 3)
                        .defaultPage(1)
                        .showMinimap(false)
                        .enableSwipe(true)
                        .swipeVertical(true)
                                //.onDraw(onDrawListener)
                                //.onLoad(onLoadCompleteListener)
                                //.onPageChange(onPageChangeListener)
                        .load();
            }

        }

//        String mime = "text/html";
//        String encoding = "utf-8";
        //webView.loadDataWithBaseURL(null, Constants.HTML_CORPORATE_GOVERNANCE, mime, encoding, null);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded
                // Return the app name after finish loading
                if (progress == 100)
                    setTitle(R.string.app_name);
            }
        });

        //webView.getSettings().setPluginsEnabled(true);

        //String url = getIntent().getStringExtra("URL");
/*        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(ArmutUtils.loadFont(getAssets(), getString(R.string.font_pera_bold)));
        tvName.setText(title);*/
        //webView.setWebViewClient(new HelloWebViewClient());
        //webView.loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTitle.setText(title);
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

//    private class HelloWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            Toast.makeText(WebActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//        }
//    }

  //  @Override
   // public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
   //     return true;
   // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        finish();
        //MainActivity.setBackwardsTranslateAnimation(this);
    }
}
