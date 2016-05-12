package com.akbank.investorrelations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.adapters.InvestorDaysAdapter;
import com.akbank.investorrelations.busevents.EventInvestorDaysRequest;
import com.akbank.investorrelations.busevents.EventInvestorDaysResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.InvestorDaysObject;
import com.akbank.investorrelations.utils.Constants;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Response;

public class InvestorDaysActivity extends BaseActivity {

    private View contentView;
    private RecyclerView reportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_investor_days, null, false);
        mContent.addView(contentView, 0);
        reportsList = (RecyclerView) findViewById(R.id.reportsList);
    }

    @Override
    protected void setTag() {
        TAG = "InvestorPresentationActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventInvestorDaysRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Sub_Menu_1_Akbank_Analyst));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Subscribe
    public void onLoadInvestorDays(EventInvestorDaysResponse event){
        Response<ArrayList<InvestorDaysObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<InvestorDaysObject> reports = response.body();
            setReportObject(reports.get(0));
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            reportsList.setLayoutManager(mLayoutManager);
            reportsList.setAdapter(new InvestorDaysAdapter(this, reports));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    private void setReportObject(final InvestorDaysObject daysObject ) {
        RelativeLayout webcastLayout = (RelativeLayout) contentView.findViewById(R.id.latestReport);
        TextView description = (TextView) webcastLayout.findViewById(R.id.reportDescription);
        description.setText(daysObject.getTitle());
        TextView viewDetailsButton = (TextView) webcastLayout.findViewById(R.id.viewButton);
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(InvestorDaysActivity.this, InvestorDaysDetailActivity.class);
                detail.putExtra("postfix", daysObject.getVideo());
                detail.putExtra("investor_days", daysObject);
                InvestorDaysActivity.this.startActivity(detail);
            }
        });
    }
}