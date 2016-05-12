package com.akbank.investorrelations.busevents;



import com.squareup.okhttp.ResponseBody;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 25/03/16.
 */
public class EventFileDownloadResponse {


    private Response<ResponseBody> response;

    public EventFileDownloadResponse(Response<ResponseBody> response){
        this.response = response;
    }

    public Response<ResponseBody> getResponse() {
        return response;
    }


}
