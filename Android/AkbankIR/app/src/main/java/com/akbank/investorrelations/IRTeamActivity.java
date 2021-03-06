package com.akbank.investorrelations;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.akbank.investorrelations.adapters.IRTeamAdapter;
import com.akbank.investorrelations.busevents.EventIRTeamRequest;
import com.akbank.investorrelations.busevents.EventIRTeamResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.Person;
import com.akbank.investorrelations.utils.Constants;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Response;

public class IRTeamActivity extends BaseActivity {

    RecyclerView irTeamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_irteam, null, false);
        mContent.addView(contentView, 0);
        irTeamList = (RecyclerView) findViewById(R.id.irTeamList);
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventIRTeamRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Menu_IRTeam));
    }

    @Subscribe
    public void onLoadIrTeamList(EventIRTeamResponse event){
        Response<ArrayList<Person>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<Person> irTeam = response.body();
            //eventsListView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            irTeamList.setLayoutManager(mLayoutManager);
            irTeamList.setAdapter(new IRTeamAdapter(this,irTeam ));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    @Override
    protected void setTag() {
        TAG = "IRTeamActivity";
    }
}
