package com.akbank.investorrelations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.akbank.investorrelations.busevents.EventPagesRequest;
import com.akbank.investorrelations.busevents.EventPagesResponse;
import com.akbank.investorrelations.objects.PagesObject;
import com.akbank.investorrelations.utils.Constants;
import com.joanzapata.pdfview.PDFView;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Response;

public class WebActivity extends BaseActivity implements Html.ImageGetter {

    @Override
    protected void setTag() {
        TAG = "WebActivity";
    }

    String title;
    TextView textView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_web, null, false);
        mContent.addView(contentView, 0);
        webView = (WebView) contentView.findViewById(R.id.webView);
        textView = (TextView) contentView.findViewById(R.id.htmlTitle);

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        PDFView pdfView = (PDFView) contentView.findViewById(R.id.pdfview);
        //pdfView.
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(124);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);
        String fileName = getIntent().getStringExtra("file_name");
        String uri = getIntent().getStringExtra("uri");
        title = getIntent().getStringExtra("title");
        String type = getIntent().getStringExtra("type");
        int position = getIntent().getIntExtra("position", 0);
        if(title != null){
            TAG = title;
        }

        app.getBus().register(this);

        if(type.equals("web")){
            //webView.loadUrl("file:///android_asset/html/"+fileName);
            //StringEscapeUtils.unescapeJson(comment.getComment()
            boolean isLocalHtml = false;
            String html = "";
            int id = 0;
            switch (position){
                case 0:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_CORPORATE_GOVERNANCE);
                    id = Constants.PAGE_ID_Corporate_Governance;
                    isLocalHtml = true;
                    break;
                case 1:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ARTICLES_OF_ASSOCIATION);
                    id = Constants.PAGE_ID_Articles_of_Association;
                    isLocalHtml = true;
                    break;
                case 3:
//                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ANNUAL_GENERAL_MEETING_DOCUMENTS);
                    id = Constants.PAGE_ID_Annual_General_Meeting_Documents;
                    isLocalHtml = true;
                    break;
                case 4:
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ABOUT_SUZAN_SABANCI);
                    id = Constants.PAGE_ID_About_Suzan_Sabancı_Dincer;
                    isLocalHtml = true;
                    break;
                case 5:
                    id = Constants.PAGE_ID_Capital_Trade_Registry_Information;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_CAPITAL_TRADE_REGISTRY_INFORMATION);
                    isLocalHtml = true;
                    break;
                case 6:
                    id = Constants.PAGE_ID_Ethical_Principles;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_ETHICAL_PRINCIPLES);
                    isLocalHtml = true;
                    break;
                case 7:
                    id = Constants.PAGE_ID_Compliance;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_COMPLIANCE);
                    isLocalHtml = true;
                    break;
                case 8:
                    id = Constants.PAGE_ID_Compensation_Policy;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_COMPANSATION);
                    isLocalHtml = true;
                    break;
                case 9:
                    id = Constants.PAGE_ID_Donation_Contribution_Policy;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_DONATION_CONTRUBUTION);
                    isLocalHtml = true;
                    break;
                case 10:
                    id = Constants.PAGE_ID_Disclosure_Policy;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_DISCLOSURE_POLICY);
                    isLocalHtml = true;
                    break;
                case 12:
                    id = Constants.PAGE_ID_Dividend_Policy;
                    html = StringEscapeUtils.unescapeJava(Constants.HTML_DIVIDENT_POLICY);
                    isLocalHtml = true;
                    break;
            }
            //webView.loadData(html,"text/html", "UTF-8");
            if(isLocalHtml){

                //TODO
                requestPage(id);
//                Spanned htmlAsSpanned = Html.fromHtml(html, this, null);
//                textView.setText(htmlAsSpanned);
//                webView.setVisibility(View.GONE);
//                textView.setVisibility(View.VISIBLE);
                //TODO
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

    @Subscribe
    public void onLoadPages(EventPagesResponse eventPagesResponse){
        Response<PagesObject> response = eventPagesResponse.getResponse();
//        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
//        Log.d(TAG, "RESPONSE : " + response.body().toString());
//        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            PagesObject page = response.body();
            Spanned htmlAsSpanned = Html.fromHtml(page.getText(), this, null);
            textView.setText(htmlAsSpanned);
            webView = (WebView)findViewById(R.id.webView);
            webView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }


        //                Spanned htmlAsSpanned = Html.fromHtml(html, this, null);
//                textView.setText(htmlAsSpanned);
//                webView.setVisibility(View.GONE);
//                textView.setVisibility(View.VISIBLE);

    }

    public void requestPage(int id) {
        app.getBus().post(new EventPagesRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY),id));
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

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = ContextCompat.getDrawable(this, R.drawable.avatar);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new LoadImage().execute(source, d);
        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = textView.getText();
                textView.setText(t);
            }
        }
    }

}
