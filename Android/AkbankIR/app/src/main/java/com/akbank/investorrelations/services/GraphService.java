package com.akbank.investorrelations.services;

import android.util.Log;

import com.akbank.investorrelations.busevents.EventComparableGraphRequest;
import com.akbank.investorrelations.busevents.EventComparableGraphResponse;
import com.akbank.investorrelations.busevents.EventMainGraphRequest;
import com.akbank.investorrelations.busevents.EventMainGraphResponse;
import com.akbank.investorrelations.busevents.EventSnapshotRequest;
import com.akbank.investorrelations.busevents.EventSnapshotResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.ComparableStockData;
import com.akbank.investorrelations.objects.MainGraphDot;
import com.akbank.investorrelations.objects.SnapshotData;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oguzemreozcan on 03/03/16.
 */
public class GraphService {

    private final Bus mBus;
    private final MainGraphRestApi mainGraphApi;
    private final SnapshotRestApi snapshotApi;
    private final GraphRestApi graphRestApi;
    private final String TAG = "GraphService";

    public GraphService(MainGraphRestApi mainGraphApi,GraphRestApi graphRestApi, SnapshotRestApi snapshotApi, Bus bus){
        this.mainGraphApi = mainGraphApi;
        this.snapshotApi = snapshotApi;
        this.graphRestApi= graphRestApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadMainGraphData(final EventMainGraphRequest event) {
        mainGraphApi.getMainGraphData(event.getLangHeader(),event.getPeriod(), event.getStartDate(), event.getEndDate()).enqueue(new Callback<ArrayList<MainGraphDot>>() {
            @Override
            public void onResponse(Call<ArrayList<MainGraphDot>> call,Response<ArrayList<MainGraphDot>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventMainGraphResponse(response, event.getPeriod(), event.getDaysBetween()));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MainGraphDot>> call,Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadSnapshotData(final EventSnapshotRequest event){
        final Callback<ArrayList<SnapshotData>> snapShotCallback  = new Callback<ArrayList<SnapshotData>>() {
            @Override
            public void onResponse(Call<ArrayList<SnapshotData>>call, Response<ArrayList<SnapshotData>> response) {
                //Log.d(TAG, "SNAPSHOT RESPONSE: " + response.raw().toString());
                if(response.isSuccessful()) {
                    mBus.post(new EventSnapshotResponse(response, event.getGridView()));
                }else{
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SnapshotData>>call, Throwable t) {
                mBus.post(new ApiErrorEvent());
                Log.d(TAG, "SNAPSHOT ON FAILURE: ");
                t.printStackTrace();
                Log.d(TAG, t.getMessage());
            }
        };
        snapshotApi.getSnapShotData(event.getLangHeader()).enqueue(snapShotCallback);
    }

    @Subscribe
    public void onLoadComparableGraphData(EventComparableGraphRequest request){
        final Callback<ComparableStockData> comparableCallback = new Callback<ComparableStockData>() {
            @Override
            public void onResponse(Call<ComparableStockData>call,Response<ComparableStockData> response) {
                Log.d(TAG, "COMPARABLE RESPONSE: " + response.raw().toString());
                if(response.isSuccessful()) {
                    mBus.post(new EventComparableGraphResponse(response));
                }else{
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ComparableStockData>call,Throwable t) {
                Log.d(TAG, "ON FAILURE");
                Log.d(TAG, t.getMessage());
            }
        };
//        GraphRestApi graphRestApi = app.retrofitForex.create(GraphRestApi.class);
        graphRestApi.getComparableGraphData(request.getLangHeader(),request.getPeriod(), request.getStartDate(), request.getEndDate(), request.getComparables()).enqueue(comparableCallback);
    }
}
