package com.akbank.investorrelations.services;

import android.util.Log;

import com.akbank.investorrelations.busevents.EventWebcastsRequest;
import com.akbank.investorrelations.busevents.EventWebcastsResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.WebcastObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oguzemreozcan on 13/04/16.
 */
public class WebcastsService {
    private final Bus mBus;
    private final WebcastsRestApi webcastsRestApi;
    private final String TAG = "WebcastsService";

    public WebcastsService(WebcastsRestApi webcastsRestApi, Bus bus){
        this.webcastsRestApi = webcastsRestApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadWebcasts(final EventWebcastsRequest event) {
        webcastsRestApi.getWebcasts(event.getLangHeader()).enqueue(new Callback<ArrayList<WebcastObject>>() {
            @Override
            public void onResponse(Call<ArrayList<WebcastObject>> call, Response<ArrayList<WebcastObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventWebcastsResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<WebcastObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure: " + call.request().url() + " - " + t.getMessage());
                mBus.post(new ApiErrorEvent());
            }
        });
    }
}
