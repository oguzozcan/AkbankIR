package com.akbank.investorrelations.busevents;

import com.akbank.investorrelations.objects.MainGraphDot;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventMainGraphResponse {

    private Response<ArrayList<MainGraphDot>> response;
    private int period;
    private int daysBetween;

    public EventMainGraphResponse(Response<ArrayList<MainGraphDot>> response, int period, int daysBetween){
        this.response = response;
        this.period = period;
        this.daysBetween = daysBetween;
    }

    public Response<ArrayList<MainGraphDot>> getResponse() {
        return response;
    }

    public int getPeriod() {
        return period;
    }

    public int getDaysBetween() {
        return daysBetween;
    }
}
