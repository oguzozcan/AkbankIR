package com.mallardduckapps.akbankir.services;

import android.util.Log;

import com.mallardduckapps.akbankir.busevents.EventCalendarRequest;
import com.mallardduckapps.akbankir.busevents.EventCalendarResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.CalendarEvent;
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
        calendarRestApi.getCalendarEvents().enqueue(new Callback<ArrayList<CalendarEvent>>() {
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
