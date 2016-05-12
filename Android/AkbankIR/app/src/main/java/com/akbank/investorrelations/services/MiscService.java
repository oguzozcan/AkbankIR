package com.akbank.investorrelations.services;

import android.util.Log;

import com.akbank.investorrelations.busevents.EventAnalystCovarageRequest;
import com.akbank.investorrelations.busevents.EventAnalystCovarageResponse;
import com.akbank.investorrelations.busevents.EventAnnualReportsRequest;
import com.akbank.investorrelations.busevents.EventAnnualReportsResponse;
import com.akbank.investorrelations.busevents.EventDeviceRegisterRequest;
import com.akbank.investorrelations.busevents.EventDeviceRegisterResponse;
import com.akbank.investorrelations.busevents.EventEarningPresentationsRequest;
import com.akbank.investorrelations.busevents.EventEarningPresentationsResponse;
import com.akbank.investorrelations.busevents.EventIRTeamRequest;
import com.akbank.investorrelations.busevents.EventIRTeamResponse;
import com.akbank.investorrelations.busevents.EventInvestorDaysRequest;
import com.akbank.investorrelations.busevents.EventInvestorDaysResponse;
import com.akbank.investorrelations.busevents.EventInvestorPresentationRequest;
import com.akbank.investorrelations.busevents.EventInvestorPresentationsResponse;
import com.akbank.investorrelations.busevents.EventNewsRequest;
import com.akbank.investorrelations.busevents.EventNewsResponse;
import com.akbank.investorrelations.busevents.EventPagesRequest;
import com.akbank.investorrelations.busevents.EventPagesResponse;
import com.akbank.investorrelations.busevents.EventSustainabilityReportsRequest;
import com.akbank.investorrelations.busevents.EventSustainabilityReportsResponse;
import com.akbank.investorrelations.objects.AnalystCovarageObject;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.InvestorDaysObject;
import com.akbank.investorrelations.objects.NewsObject;
import com.akbank.investorrelations.objects.PagesObject;
import com.akbank.investorrelations.objects.Person;
import com.akbank.investorrelations.objects.ReportObject;
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
    private final EarningPresentationsRestApi eRestApi;
    private final InvestorDaysRestApi daysRestApi;
    private final PagesRestApi pagesRestApi;
    private final DeviceRestApi deviceRestApi;
    private final String TAG = "MiscService";

    public MiscService(IrTeamRestApi irTeamApi, NewsRestApi newsRestApi, AnalystCovarageRestApi analystRestApi,
                       SustainabilityReportRestApi sRestApi, AnnualReportRestApi aRestApi,InvestorPresentationRestApi iRestApi,
                       EarningPresentationsRestApi eRestApi, InvestorDaysRestApi daysRestApi,PagesRestApi pagesRestApi, DeviceRestApi deviceRestApi, Bus bus){
        this.irTeamApi = irTeamApi;
        this.newsRestApi = newsRestApi;
        this.analystRestApi = analystRestApi;
        this.sRestApi= sRestApi;
        this.aRestApi = aRestApi;
        this.iRestApi = iRestApi;
        this.eRestApi = eRestApi;
        this.daysRestApi = daysRestApi;
        this.pagesRestApi = pagesRestApi;
        this.deviceRestApi = deviceRestApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadIRTeam(final EventIRTeamRequest event) {
        irTeamApi.getIrTeam(event.getLangHeader()).enqueue(new Callback<ArrayList<Person>>() {
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
        newsRestApi.getNewsAndAnnouncements(event.getLangHeader()).enqueue(new Callback<ArrayList<NewsObject>>() {
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
        analystRestApi.getAnalystCovarageList(event.getLangHeader()).enqueue(new Callback<ArrayList<AnalystCovarageObject>>() {
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
    public void onDeviceRegistered(final EventDeviceRegisterRequest event) {
        deviceRestApi.registerDevice(event.getRegistrationId(), event.isActive(), event.getLang()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventDeviceRegisterResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadInvestorPresentations(final EventInvestorPresentationRequest event) {
        iRestApi.getInvestorPresentations(event.getLangHeader()).enqueue(new Callback<ArrayList<ReportObject>>() {
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
    public void onLoadInvestorDays(final EventInvestorDaysRequest event) {
        daysRestApi.getInvestorDays(event.getLangHeader()).enqueue(new Callback<ArrayList<InvestorDaysObject>>() {
            @Override
            public void onResponse(Call<ArrayList<InvestorDaysObject>> call, Response<ArrayList<InvestorDaysObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventInvestorDaysResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<InvestorDaysObject>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

    @Subscribe
    public void onLoadSustainabilityReports(final EventSustainabilityReportsRequest event) {
        sRestApi.getSustainabilityReports(event.getLangHeader()).enqueue(new Callback<ArrayList<ReportObject>>() {
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
        aRestApi.getAnnualReports(event.getLangHeader()).enqueue(new Callback<ArrayList<ReportObject>>() {
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

    @Subscribe
    public void onLoadEarningPresentations(final EventEarningPresentationsRequest event) {
        eRestApi.getEarningPresentations(event.getLangHeader()).enqueue(new Callback<ArrayList<ReportObject>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportObject>> call, Response<ArrayList<ReportObject>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventEarningPresentationsResponse(response));
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
    public void onLoadPages(final EventPagesRequest event) {
        pagesRestApi.getPage(event.getLangHeader(),event.getPageNumber()).enqueue(new Callback<PagesObject>() {
            @Override
            public void onResponse(Call<PagesObject> call, Response<PagesObject> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventPagesResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<PagesObject> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }

}
