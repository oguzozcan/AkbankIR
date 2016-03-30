package com.mallardduckapps.akbankir.objects;

/**
 * Created by oguzemreozcan on 03/03/16.
 */
public class ApiErrorEvent {

    private int statusCode;
    private String errorMessage;
    private boolean retryRequest;

    public ApiErrorEvent(int statusCode, String errorMessage, boolean retryRequest) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.retryRequest = retryRequest;
    }

    public ApiErrorEvent(){

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isRetryRequest() {
        return retryRequest;
    }

    public void setRetryRequest(boolean retryRequest) {
        this.retryRequest = retryRequest;
    }
}
