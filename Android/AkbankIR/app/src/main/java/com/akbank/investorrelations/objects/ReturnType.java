package com.akbank.investorrelations.objects;

/**
 * Created by oguzemreozcan on 25/01/16.
 */
public class ReturnType{
    private final int statusCode;
    private final String response;

    public ReturnType(final int statusCode, final String response){
        this.statusCode = statusCode;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }
}
