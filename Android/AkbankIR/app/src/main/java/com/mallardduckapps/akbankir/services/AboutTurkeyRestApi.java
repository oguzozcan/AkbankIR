package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.AboutTurkeyObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public interface AboutTurkeyRestApi {

    @GET("pages/3/")
    Call<AboutTurkeyObject> getAboutTurkey();

}
