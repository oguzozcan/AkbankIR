package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.MainGraphDot;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventMainGraphResponse {

    private Response<ArrayList<MainGraphDot>> response;

    public EventMainGraphResponse(Response<ArrayList<MainGraphDot>> response){
        this.response = response;
    }

    public Response<ArrayList<MainGraphDot>> getResponse() {
        return response;
    }
}
