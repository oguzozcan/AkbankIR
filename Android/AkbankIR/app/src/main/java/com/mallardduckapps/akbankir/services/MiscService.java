package com.mallardduckapps.akbankir.services;

import android.util.Log;

import com.mallardduckapps.akbankir.busevents.EventIRTeamRequest;
import com.mallardduckapps.akbankir.busevents.EventIRTeamResponse;
import com.mallardduckapps.akbankir.busevents.EventMainGraphRequest;
import com.mallardduckapps.akbankir.busevents.EventMainGraphResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.MainGraphDot;
import com.mallardduckapps.akbankir.objects.Person;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oguzemreozcan on 07/03/16.
 */
public class MiscService {
    private final Bus mBus;
    private final IrTeamRestApi irTeamApi;
    private final String TAG = "MiscService";

    public MiscService(IrTeamRestApi irTeamApi, Bus bus){
        this.irTeamApi = irTeamApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadIRTeam(final EventIRTeamRequest event) {
        irTeamApi.getIrTeam().enqueue(new Callback<ArrayList<Person>>() {
            @Override
            public void onResponse(Call<ArrayList<Person>> call, Response<ArrayList<Person>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventIRTeamResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Person>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

}
