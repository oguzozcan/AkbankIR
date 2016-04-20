package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.InvestorDaysObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class EventInvestorDaysResponse {
    private Response<ArrayList<InvestorDaysObject>> response;

    public EventInvestorDaysResponse(Response<ArrayList<InvestorDaysObject>> response){
        this.response = response;
    }

    public Response<ArrayList<InvestorDaysObject>> getResponse() {
        return response;
    }
}
