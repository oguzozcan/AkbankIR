package com.akbank.investorrelations.busevents;

import com.akbank.investorrelations.objects.ReportObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class EventAnnualReportsResponse {
    private Response<ArrayList<ReportObject>> response;

    public EventAnnualReportsResponse(Response<ArrayList<ReportObject>> response){
        this.response = response;
    }

    public Response<ArrayList<ReportObject>> getResponse() {
        return response;
    }
}
