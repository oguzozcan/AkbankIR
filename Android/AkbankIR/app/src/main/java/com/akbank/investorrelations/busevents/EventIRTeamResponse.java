package com.akbank.investorrelations.busevents;

import com.akbank.investorrelations.objects.Person;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 07/03/16.
 */
public class EventIRTeamResponse {
    private Response<ArrayList<Person>> response;

    public EventIRTeamResponse(Response<ArrayList<Person>> response){
        this.response = response;
    }

    public Response<ArrayList<Person>> getResponse() {
        return response;
    }
}
