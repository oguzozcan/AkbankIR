package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.Rating;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public interface RatingsRestApi {
    @GET("ratings/")
    Call<ArrayList<Rating>> getRatings(@Header("Accept-Language") String language);
}
