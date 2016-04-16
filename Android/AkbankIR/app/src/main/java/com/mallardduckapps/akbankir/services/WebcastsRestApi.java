package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.WebcastObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 13/04/16.
 */
public interface WebcastsRestApi {
    @GET("webcasts/")
    Call<ArrayList<WebcastObject>> getWebcasts();
}
