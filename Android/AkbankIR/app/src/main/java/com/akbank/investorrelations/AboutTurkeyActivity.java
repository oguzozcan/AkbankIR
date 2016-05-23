package com.akbank.investorrelations;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.busevents.EventAboutTurkeyRequest;
import com.akbank.investorrelations.busevents.EventAboutTurkeyResponse;
import com.akbank.investorrelations.fragments.DownloadDialogFragment;
import com.akbank.investorrelations.objects.AboutTurkeyObject;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.utils.Constants;
import com.squareup.otto.Subscribe;

import retrofit2.Response;

public class AboutTurkeyActivity extends BaseActivity {

    RelativeLayout aboutTurkeyView;
    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_about_turkey, null, false);
        mContent.addView(contentView, 0);
        //irTeamList = (RecyclerView) findViewById(R.id.irTeamList);
        aboutTurkeyView = (RelativeLayout) contentView.findViewById(R.id.aboutTurkeyLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Menu_AboutTurkey));
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventAboutTurkeyRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Subscribe
    public void onLoadAboutTurkey(final EventAboutTurkeyResponse event) {
        Response<AboutTurkeyObject> response = event.getAboutTurkeyObject();
        Log.d(TAG, "ON RESPONSE aboutTurkey- responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE aboutTurkey: " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            AboutTurkeyObject aboutTurkeyObject = response.body();
            setAboutTurkeyLayout(aboutTurkeyObject);

        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }

    private void setAboutTurkeyLayout(final AboutTurkeyObject aboutTurkeyObject) {
        TextView text = (TextView) contentView.findViewById(R.id.aboutTurkeyText);
        Spanned htmlAsSpanned = Html.fromHtml(aboutTurkeyObject.getText());
        text.setText(htmlAsSpanned);

        RelativeLayout pdf1Layout = (RelativeLayout) contentView.findViewById(R.id.pdf1);
        RelativeLayout pdf2Layout = (RelativeLayout) contentView.findViewById(R.id.pdf2);
        setPdfContentView(pdf1Layout, aboutTurkeyObject, 1);
        setPdfContentView(pdf2Layout, aboutTurkeyObject, 2);
    }

    private void setPdfContentView(View view, final AboutTurkeyObject aboutTurkeyObject, final int pdfIndex){

        TextView aboutTurkeyPdfName = (TextView) view.findViewById(R.id.aboutTurkeyDescription);
        if(pdfIndex == 1){
            aboutTurkeyPdfName.setText(aboutTurkeyObject.getPdfTitle());
        }else if(pdfIndex == 2){
            aboutTurkeyPdfName.setText(aboutTurkeyObject.getPdfTitle2());
        }

        TextView saveButton = (TextView) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app.getBus().post(new EventFileDownloadRequest(ItemListActivity.this,AkbankApp.ROOT_URL_1, aboutTurkeyObject.getPdf()));
                Log.d(TAG, "Download STARTED about Turkey");
                //startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), false);
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                if(pdfIndex == 1){
                    b.putString("url", aboutTurkeyObject.getPdf());
                    b.putString("title", aboutTurkeyObject.getPdfTitle());
                }else{
                    b.putString("url", aboutTurkeyObject.getPdf2());
                    b.putString("title", aboutTurkeyObject.getPdfTitle2());
                }
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Downloading));

            }
        });
        TextView viewButton = (TextView) view.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), true);
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                if(pdfIndex == 1){
                    b.putString("url", aboutTurkeyObject.getPdf());
                    b.putString("title", aboutTurkeyObject.getPdfTitle());
                }else{
                    b.putString("url", aboutTurkeyObject.getPdf2());
                    b.putString("title", aboutTurkeyObject.getPdfTitle2());
                }
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Opening));
            }
        });
    }

    @Override
    protected void setTag() {
        TAG = "AboutTurkeyActivity";
    }
}
