package com.mallardduckapps.akbankir.services;

import android.util.Log;

import com.mallardduckapps.akbankir.busevents.EventAnalystCovarageRequest;
import com.mallardduckapps.akbankir.busevents.EventAnalystCovarageResponse;
import com.mallardduckapps.akbankir.busevents.EventAnnualReportsRequest;
import com.mallardduckapps.akbankir.busevents.EventAnnualReportsResponse;
import com.mallardduckapps.akbankir.busevents.EventIRTeamRequest;
import com.mallardduckapps.akbankir.busevents.EventIRTeamResponse;
import com.mallardduckapps.akbankir.busevents.EventInvestorPresentationRequest;
import com.mallardduckapps.akbankir.busevents.EventInvestorPresentationsResponse;
import com.mallardduckapps.akbankir.busevents.EventNewsRequest;
import com.mallardduckapps.akbankir.busevents.EventNewsResponse;
import com.mallardduckapps.akbankir.busevents.EventSustainabilityReportsRequest;
import com.mallardduckapps.akbankir.busevents.EventSustainabilityReportsResponse;
import com.mallardduckapps.akbankir.objects.AnalystCovarageObject;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.NewsObject;
import com.mallardduckapps.akbankir.objects.Person;
import com.mallardduckapps.akbankir.objects.ReportObject;
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
    private final NewsRestApi newsRestApi;
    private final AnalystCovarageRestApi analystRestApi;
    private final SustainabilityReportRestApi sRestApi;
    private final AnnualReportRestApi aRestApi;
    private final InvestorPresentationRestApi iRestApi;
    private final String TAG = "MiscService";

    public MiscService(IrTeamRestApi irTeamApi, NewsRestApi newsRestApi, AnalystCovarageRestApi analystRestApi,
                       SustainabilityReportRestApi sRestApi, AnnualReportRestApi aRestApi,InvestorPresentationRestApi iRestApi, Bus bus){
        this.irTeamApi = irTeamApi;
        this.newsRestApi = newsRestApi;
        this.analystRestApi = analystRestApi;
        this.sRestApi= sRestApi;
        this.aRestApi = aRestApi;
        this.iRestApi = iRestApi;
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

    @Subscribe
    public void onLoadNewsAndAnnouncements(final EventNewsRequest event) {
        newsRestApi.getNewsAndAnnouncements().enqueue(new Callback<ArrayList<NewsObject>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsObject>> call, Response<ArrayList<NewsObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventNewsResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<NewsObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadAnalystCovarage(final EventAnalystCovarageRequest event) {
        analystRestApi.getAnalystCovarageList().enqueue(new Callback<ArrayList<AnalystCovarageObject>>() {
            @Override
            public void onResponse(Call<ArrayList<AnalystCovarageObject>> call, Response<ArrayList<AnalystCovarageObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventAnalystCovarageResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AnalystCovarageObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadInvestorPresentations(final EventInvestorPresentationRequest event) {
        iRestApi.getInvestorPresentations().enqueue(new Callback<ArrayList<ReportObject>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportObject>> call, Response<ArrayList<ReportObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventInvestorPresentationsResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReportObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadSustainabilityReports(final EventSustainabilityReportsRequest event) {
        sRestApi.getSustainabilityReports().enqueue(new Callback<ArrayList<ReportObject>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportObject>> call, Response<ArrayList<ReportObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventSustainabilityReportsResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReportObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadAnnualReports(final EventAnnualReportsRequest event) {
        aRestApi.getAnnualReports().enqueue(new Callback<ArrayList<ReportObject>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportObject>> call, Response<ArrayList<ReportObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventAnnualReportsResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReportObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

}
