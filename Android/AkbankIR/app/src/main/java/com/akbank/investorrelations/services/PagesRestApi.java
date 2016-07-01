package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.PagesObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by oguzemreozcan on 19/04/16.
 */
public interface PagesRestApi {
    @GET("/governance/{pages}")
    Call<PagesObject> getPage(@Header("Accept-Language") String language,@Path("pages") int pages);
}
