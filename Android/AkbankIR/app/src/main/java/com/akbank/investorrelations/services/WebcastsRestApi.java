package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.WebcastObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by oguzemreozcan on 13/04/16.
 */
public interface WebcastsRestApi {
    @GET("webcasts/")
    Call<ArrayList<WebcastObject>> getWebcasts(@Header("Accept-Language") String language);
}
