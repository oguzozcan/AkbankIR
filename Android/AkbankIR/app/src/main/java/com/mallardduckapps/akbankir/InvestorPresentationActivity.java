package com.mallardduckapps.akbankir;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.adapters.ReportsAdapter;
import com.mallardduckapps.akbankir.busevents.EventInvestorPresentationRequest;
import com.mallardduckapps.akbankir.busevents.EventInvestorPresentationsResponse;
import com.mallardduckapps.akbankir.busevents.EventSustainabilityReportsRequest;
import com.mallardduckapps.akbankir.busevents.EventSustainabilityReportsResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.ReportObject;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Response;

public class InvestorPresentationActivity extends BaseActivity {

    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_investor_presentation, null, false);
        mContent.addView(contentView, 0);
        onTitleTextChange(getString(R.string.Sub_Menu_1_Investor));
    }

    @Override
    protected void setTag() {
        TAG = "InvestorPresentationActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventInvestorPresentationRequest());
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Subscribe
    public void onLoadInvestorPresentations(EventInvestorPresentationsResponse event){
        Response<ArrayList<ReportObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<ReportObject> reports = response.body();
            setReportObject(reports.get(0));
            //LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            //reportsList.setLayoutManager(mLayoutManager);
            //reportsList.setAdapter(new ReportsAdapter(this, reports));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    private void setReportObject(ReportObject reportObject ) {
        RelativeLayout webcastLayout = (RelativeLayout) contentView.findViewById(R.id.latestReport);
        TextView description = (TextView) webcastLayout.findViewById(R.id.reportDescription);
        TextView description2 = (TextView) webcastLayout.findViewById(R.id.reportDescription2);
        description.setText(reportObject.getTitle());
        if(reportObject.getDescription().isEmpty()){
            description2.setVisibility(View.INVISIBLE);
        }else{
            Spanned htmlAsSpanned = Html.fromHtml(reportObject.getDescription());
            description2.setText(htmlAsSpanned);
        }
        TextView saveButton = (TextView) webcastLayout.findViewById(R.id.saveButton);
        TextView viewButton = (TextView) webcastLayout.findViewById(R.id.viewButton);
        ImageView pdfIcon = (ImageView) webcastLayout.findViewById(R.id.pdfIcon);

        Picasso.with(this).load(AkbankApp.ROOT_URL + reportObject.getImage()).placeholder(R.drawable.group_copy_4).into(pdfIcon);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
