package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.CalendarEvent;
import com.mallardduckapps.akbankir.objects.MainGraphDot;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 05/03/16.
 */
public class EventCalendarResponse {

    private Response<ArrayList<CalendarEvent>> response;

    public EventCalendarResponse(Response<ArrayList<CalendarEvent>> response){
        this.response = response;
    }

    public Response<ArrayList<CalendarEvent>> getResponse() {
        return response;
    }
}
