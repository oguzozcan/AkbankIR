package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.Rating;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public interface RatingsRestApi {
    @GET("ratings/")
    Call<ArrayList<Rating>> getRatings();
}
