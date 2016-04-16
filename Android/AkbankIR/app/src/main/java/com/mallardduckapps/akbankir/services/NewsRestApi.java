package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.NewsObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public interface NewsRestApi {
    @GET("news/")
    Call<ArrayList<NewsObject>> getNewsAndAnnouncements();
}
