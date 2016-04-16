package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.AnalystCovarageObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public interface AnalystCovarageRestApi {

    @GET("coverage/")
    Call<ArrayList<AnalystCovarageObject>> getAnalystCovarageList();

}
