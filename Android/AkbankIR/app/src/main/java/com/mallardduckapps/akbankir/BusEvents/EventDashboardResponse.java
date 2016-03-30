package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.DashboardContainerObject;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 20/03/16.
 */
public class EventDashboardResponse {
    private Response<DashboardContainerObject> response;

    public EventDashboardResponse(Response<DashboardContainerObject> response){
        this.response = response;
    }

    public Response<DashboardContainerObject> getResponse() {
        return response;
    }
}
