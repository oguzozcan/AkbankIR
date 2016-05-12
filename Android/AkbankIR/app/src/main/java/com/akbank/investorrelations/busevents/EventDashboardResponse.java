package com.akbank.investorrelations.busevents;

import com.akbank.investorrelations.objects.DashboardContainerObject;

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
