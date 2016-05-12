package com.akbank.investorrelations.services;

import android.util.Log;

import com.akbank.investorrelations.busevents.EventCalendarRequest;
import com.akbank.investorrelations.busevents.EventCalendarResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.CalendarEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oguzemreozcan on 05/03/16.
 */
public class CalendarService {

    private final Bus mBus;
    private final CalendarRestApi calendarRestApi;
    private final String TAG = "GraphService";

    public CalendarService(CalendarRestApi calendarRestApi, Bus bus){
        this.calendarRestApi = calendarRestApi;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadCalendarData(final EventCalendarRequest event) {
        calendarRestApi.getCalendarEvents(event.getLangHeader()).enqueue(new Callback<ArrayList<CalendarEvent>>() {
            @Override
            public void onResponse(Call<ArrayList<CalendarEvent>> call, Response<ArrayList<CalendarEvent>> response) {
                Log.d(TAG, "ON RESPONSE : " + response.isSuccessful() + " - responsecode: " + response.code() + " - response:" + response.message());
                if (response.isSuccessful()) {
                    mBus.post(new EventCalendarResponse(response));
                } else {
                    mBus.post(new ApiErrorEvent(response.code(), response.message(), false));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CalendarEvent>> call, Throwable t) {
                Log.d(TAG, "ON FAilure");
                mBus.post(new ApiErrorEvent());
            }
        });
    }
}
