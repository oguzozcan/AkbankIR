package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.AnalystCovarageObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public class EventAnalystCovarageResponse {
    private Response<ArrayList<AnalystCovarageObject>> response;

    public EventAnalystCovarageResponse(Response<ArrayList<AnalystCovarageObject>> response){
        this.response = response;
    }

    public Response<ArrayList<AnalystCovarageObject>> getResponse() {
        return response;
    }
}
