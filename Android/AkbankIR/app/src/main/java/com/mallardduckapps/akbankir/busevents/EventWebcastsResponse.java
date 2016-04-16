package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.WebcastObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 13/04/16.
 */
public class EventWebcastsResponse {
    private Response<ArrayList<WebcastObject>> response;

    public EventWebcastsResponse(Response<ArrayList<WebcastObject>> response){
        this.response = response;
    }

    public Response<ArrayList<WebcastObject>> getResponse() {
        return response;
    }
}
