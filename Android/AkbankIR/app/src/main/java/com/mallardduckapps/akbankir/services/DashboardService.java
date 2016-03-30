package com.mallardduckapps.akbankir.services;

import android.app.DownloadManager;
import android.net.Uri;
import android.util.Log;

import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.BaseActivity;
import com.mallardduckapps.akbankir.busevents.EventAboutTurkeyRequest;
import com.mallardduckapps.akbankir.busevents.EventAboutTurkeyResponse;
import com.mallardduckapps.akbankir.busevents.EventDashboardRequest;
import com.mallardduckapps.akbankir.busevents.EventDashboardResponse;
import com.mallardduckapps.akbankir.busevents.EventFileDownloadRequest;
import com.mallardduckapps.akbankir.busevents.EventFileDownloadResponse;
import com.mallardduckapps.akbankir.busevents.EventRatingRequest;
import com.mallardduckapps.akbankir.busevents.EventRatingResponse;
import com.mallardduckapps.akbankir.objects.AboutTurkeyObject;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.CalendarEvent;
import com.mallardduckapps.akbankir.objects.DashboardContainerObject;
import com.mallardduckapps.akbankir.objects.Rating;
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
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
    private final DownloadFileApi downloadFileApi;
    private final String TAG = "DashboardService";

    public DashboardService(DashboardRestApi dashboardRestApi, RatingsRestApi ratingsRestApi, AboutTurkeyRestApi aboutTurkeyRestApi, DownloadFileApi downloadFileApi, Bus bus){
        this.dashboardRest = dashboardRestApi;
        this.ratingsRestApi = ratingsRestApi;
        this.downloadFileApi = downloadFileApi;
        this.aboutTurkeyRestApi = aboutTurkeyRestApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadDashboardData(final EventDashboardRequest event) {
        dashboardRest.getDashboardItems().enqueue(new Callback<DashboardContainerObject>() {
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
        ratingsRestApi.getRatings().enqueue(new Callback<ArrayList<Rating>>() {
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
        aboutTurkeyRestApi.getAboutTurkey().enqueue(new Callback<AboutTurkeyObject>() {
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
