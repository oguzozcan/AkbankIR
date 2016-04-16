package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.NewsObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public class EventNewsResponse {

    private Response<ArrayList<NewsObject>> response;

    public EventNewsResponse(Response<ArrayList<NewsObject>> response){
        this.response = response;
    }

    public Response<ArrayList<NewsObject>> getResponse() {
        return response;
    }
}
