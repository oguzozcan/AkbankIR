package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.DashboardContainerObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 20/03/16.
 */
public interface DashboardRestApi {
    @GET("dashboard/")
    Call<DashboardContainerObject> getDashboardItems();
}

