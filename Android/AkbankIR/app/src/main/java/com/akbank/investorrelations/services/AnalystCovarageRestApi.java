package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.AnalystCovarageObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public interface AnalystCovarageRestApi {

    @GET("coverage/")
    Call<ArrayList<AnalystCovarageObject>> getAnalystCovarageList(@Header("Accept-Language") String language);

}
