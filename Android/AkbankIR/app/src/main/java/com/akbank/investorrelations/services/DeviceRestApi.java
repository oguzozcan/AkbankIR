package com.akbank.investorrelations.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by oguzemreozcan on 20/04/16.
 */
public interface DeviceRestApi {
    @FormUrlEncoded
    @POST("iosDevice/")
    Call<String> registerDevice(@Field("registration_id") String regId, @Field("active") boolean isActive, @Field("lang") String lang);
}
