package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.Rating;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public class EventRatingResponse {

    private final Response<ArrayList<Rating>> response;

    public EventRatingResponse(Response<ArrayList<Rating>> response) {
        this.response = response;
    }

    public Response<ArrayList<Rating>> getResponse() {
        return response;
    }
}
