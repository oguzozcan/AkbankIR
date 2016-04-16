package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.ReportObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class EventSustainabilityReportsResponse {

    private Response<ArrayList<ReportObject>> response;

    public EventSustainabilityReportsResponse(Response<ArrayList<ReportObject>> response){
        this.response = response;
    }

    public Response<ArrayList<ReportObject>> getResponse() {
        return response;
    }
}