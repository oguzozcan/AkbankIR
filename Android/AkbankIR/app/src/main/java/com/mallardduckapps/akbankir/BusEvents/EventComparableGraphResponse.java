package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.ComparableStockData;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventComparableGraphResponse {

    private final Response<ComparableStockData> response;

    public EventComparableGraphResponse(Response<ComparableStockData> response) {
        this.response = response;
    }

    public Response<ComparableStockData> getResponse() {
        return response;
    }
}
