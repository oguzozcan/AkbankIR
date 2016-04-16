package com.mallardduckapps.akbankir;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.adapters.ReportsAdapter;
import com.mallardduckapps.akbankir.busevents.EventSustainabilityReportsRequest;
import com.mallardduckapps.akbankir.busevents.EventSustainabilityReportsResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.ReportObject;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Response;

public class SustainabilityReportActivity extends BaseActivity {

    View contentView;
    private RecyclerView reportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_sustainability_report, null, false);
        mContent.addView(contentView, 0);
        reportsList = (RecyclerView) findViewById(R.id.reportsList);
        onTitleTextChange(getString(R.string.Sub_Menu_1_Sustainability));
    }

    @Override
    protected void setTag() {
        TAG = "SustainabilityReportActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventSustainabilityReportsRequest());
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Subscribe
    public void onLoadSustainabilityReports(EventSustainabilityReportsResponse event){
        Response<ArrayList<ReportObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<ReportObject> reports = response.body();
            setReportObject(reports.get(0));
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            reportsList.setLayoutManager(mLayoutManager);
            reportsList.setAdapter(new ReportsAdapter(this, reports));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    private void setReportObject(ReportObject reportObject ) {
        RelativeLayout webcastLayout = (RelativeLayout) contentView.findViewById(R.id.latestReport);
        TextView description = (TextView) webcastLayout.findViewById(R.id.reportDescription);
        description.setText(reportObject.getTitle());
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
