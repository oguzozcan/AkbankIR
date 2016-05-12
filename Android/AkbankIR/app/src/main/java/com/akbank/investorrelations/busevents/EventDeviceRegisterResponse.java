package com.akbank.investorrelations.busevents;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 20/04/16.
 */
public class EventDeviceRegisterResponse {
    private final Response<String> response;

    public EventDeviceRegisterResponse(Response<String> response) {
        this.response = response;
    }

    public Response<String> getResponse() {
        return response;
    }
}
