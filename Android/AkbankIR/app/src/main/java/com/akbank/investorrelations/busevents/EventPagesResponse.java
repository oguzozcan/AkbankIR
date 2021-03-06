package com.akbank.investorrelations.busevents;

import com.akbank.investorrelations.objects.PagesObject;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 19/04/16.
 */
public class EventPagesResponse {

    private final Response<PagesObject> response;

    public EventPagesResponse(Response<PagesObject> response) {
        this.response = response;
    }

    public Response<PagesObject> getResponse() {
        return response;
    }
}
