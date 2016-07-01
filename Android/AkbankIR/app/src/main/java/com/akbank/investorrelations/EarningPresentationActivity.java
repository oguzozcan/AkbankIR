package com.akbank.investorrelations;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.adapters.ReportsAdapter;
import com.akbank.investorrelations.busevents.EventEarningPresentationsRequest;
import com.akbank.investorrelations.busevents.EventEarningPresentationsResponse;
import com.akbank.investorrelations.fragments.DownloadDialogFragment;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.ReportObject;
import com.akbank.investorrelations.utils.Constants;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Response;

//import android.support.v4.app.FragmentManager;

public class EarningPresentationActivity extends BaseActivity {

    //private View contentView;
    //private RecyclerView reportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_earning_presentation, null, false);
        mContent.addView(contentView, 0);
    }

    @Override
    protected void setTag() {
        TAG = "SustainabilityReportActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventEarningPresentationsRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
        Log.d(TAG, "EARNING PRESENTATION: language key: " + ds.getLangString(Constants.SELECTED_LANGUAGE_KEY) );
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Sub_Menu_1_Financial));
    }

    @Subscribe
    public void onLoadAnnualReports(EventEarningPresentationsResponse event){
        Response<ArrayList<ReportObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<ReportObject> reports = response.body();
            reports = filterOutEnglishTitles(reports);
            setReportObject(reports.get(0));
            RecyclerView reportsList = (RecyclerView) findViewById(R.id.reportsList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            reportsList.setLayoutManager(mLayoutManager);
            reportsList.setAdapter(new ReportsAdapter(this, reports));

        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    private ArrayList<ReportObject> filterOutEnglishTitles(ArrayList<ReportObject> reports){
        ArrayList<ReportObject> newReports = new ArrayList<>();
        for(ReportObject report : reports){
            String title = report.getTitle();
            if(!title.toLowerCase().contains("quarter results")){
                newReports.add(report);
            }
        }
        return newReports;
    }

    private void setReportObject(final ReportObject reportObject ) {
        RelativeLayout webcastLayout = (RelativeLayout) findViewById(R.id.latestReport);
        TextView description = (TextView) webcastLayout.findViewById(R.id.reportDescription);
        description.setText(reportObject.getTitle());
        TextView saveButton = (TextView) webcastLayout.findViewById(R.id.saveButton);
        TextView viewButton = (TextView) webcastLayout.findViewById(R.id.viewButton);
        ImageView pdfIcon = (ImageView) webcastLayout.findViewById(R.id.pdfIcon);

        Picasso.with(this).load(AkbankApp.ROOT_URL + reportObject.getImage()).placeholder(R.drawable.group_copy_4).into(pdfIcon);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", reportObject.getTitle());
                b.putString("url", reportObject.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Downloading));
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", reportObject.getTitle());
                b.putString("url", reportObject.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Opening));
            }
        });
    }
}

