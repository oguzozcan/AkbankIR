package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.CalendarEvent;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 05/03/16.
 */
public interface CalendarRestApi {
    @GET("calendarEvents/")
    Call<ArrayList<CalendarEvent>> getCalendarEvents();
}