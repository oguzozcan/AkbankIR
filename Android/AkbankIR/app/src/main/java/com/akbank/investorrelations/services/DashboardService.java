package com.akbank.investorrelations.services;

import android.util.Log;

import com.akbank.investorrelations.AkbankApp;
import com.akbank.investorrelations.busevents.EventAboutTurkeyRequest;
import com.akbank.investorrelations.busevents.EventAboutTurkeyResponse;
import com.akbank.investorrelations.busevents.EventDashboardRequest;
import com.akbank.investorrelations.busevents.EventDashboardResponse;
import com.akbank.investorrelations.busevents.EventFileDownloadRequest;
import com.akbank.investorrelations.busevents.EventRatingRequest;
import com.akbank.investorrelations.busevents.EventRatingResponse;
import com.akbank.investorrelations.objects.AboutTurkeyObject;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.DashboardContainerObject;
import com.akbank.investorrelations.objects.Rating;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oguzemreozcan on 20/03/16.
 */
public class DashboardService {
    private final Bus mBus;
    private final DashboardRestApi dashboardRest;
    private final RatingsRestApi ratingsRestApi;
    private final AboutTurkeyRestApi aboutTurkeyRestApi;
    //private final DownloadFileApi downloadFileApi;
    private final String TAG = "DashboardService";

    public DashboardService(DashboardRestApi dashboardRestApi, RatingsRestApi ratingsRestApi, AboutTurkeyRestApi aboutTurkeyRestApi, DownloadFileApi downloadFileApi, Bus bus){
        this.dashboardRest = dashboardRestApi;
        this.ratingsRestApi = ratingsRestApi;
        //this.downloadFileApi = downloadFileApi;
        this.aboutTurkeyRestApi = aboutTurkeyRestApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadDashboardData(final EventDashboardRequest event) {
        dashboardRest.getDashboardItems(event.getLangHeader()).enqueue(new Callback<DashboardContainerObject>() {
            @Override
            public void onResponse(Call<DashboardContainerObject> call, Response<DashboardContainerObject> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventDashboardResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<DashboardContainerObject> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadRatingsData(final EventRatingRequest event) {
        ratingsRestApi.getRatings(event.getLangHeader()).enqueue(new Callback<ArrayList<Rating>>() {
            @Override
            public void onResponse(Call<ArrayList<Rating>> call, Response<ArrayList<Rating>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventRatingResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Rating>> call,Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadAboutTurkey(final EventAboutTurkeyRequest event) {
        aboutTurkeyRestApi.getAboutTurkey(event.getLangHeader()).enqueue(new Callback<AboutTurkeyObject>() {
            @Override
            public void onResponse(Call<AboutTurkeyObject> call, Response<AboutTurkeyObject> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventAboutTurkeyResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<AboutTurkeyObject> call,Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadFile(final EventFileDownloadRequest event) {
        //private long enqueue;
        //private  dm;
        Log.d(TAG, "EVENT POSTFIX: " + (AkbankApp.ROOT_URL_1 + event.getPostFix()));

//        downloadFileApi.getFile(event.getPostFix()).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.d(TAG, "ON RESPONSE on load file: " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message() );
//                    Log.d(TAG, "REsponceBody: bytes: " + response.body().bytes());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (response.isSuccessful()) {
//                    mBus.post(new EventFileDownloadResponse(response));
//                } else {
//                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "ON FAilure: url" + call.request().url() + " - call body: " + call.request().body());
//                t.printStackTrace();
//                mBus.post(new ApiErrorEvent());
//            }
//        });
    }

}
