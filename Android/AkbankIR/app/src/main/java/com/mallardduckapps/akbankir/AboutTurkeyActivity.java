package com.mallardduckapps.akbankir;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.busevents.EventAboutTurkeyRequest;
import com.mallardduckapps.akbankir.busevents.EventAboutTurkeyResponse;
import com.mallardduckapps.akbankir.objects.AboutTurkeyObject;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
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
        onTitleTextChange(getString(R.string.Menu_AboutTurkey));
        aboutTurkeyView = (RelativeLayout) contentView.findViewById(R.id.aboutTurkeyLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventAboutTurkeyRequest());
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

    private void setPdfContentView(View view, final AboutTurkeyObject aboutTurkeyObject, int pdfIndex){

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
            }
        });
        TextView viewButton = (TextView) view.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), true);
            }
        });
    }

    @Override
    protected void setTag() {
        TAG = "AboutTurkeyActivity";
    }
}
