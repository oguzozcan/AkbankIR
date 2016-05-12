package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.MainGraphDot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by oguzemreozcan on 28/01/16.
 */
public interface MainGraphRestApi {
    //"https://cloud.foreks.com/historical-service/intraday/delay/15/code/AKBNK.E.BIST/period/1440/from/20121201000000/to/20130102000000?f=c"
    @Headers({
            "Authorization: YWtiYW5rLXlhdGlyaW06YTI0Y0JzIXpY"
    })
    @GET("historical-service/intraday/delay/15/code/AKBNK.E.BIST/period/{period}/from/{from}/to/{to}?f=c&f=v")
    Call<ArrayList<MainGraphDot>> getMainGraphData(@Header("Accept-Language") String language, @Path("period") int period, @Path("from") String from, @Path("to") String to); //    , Callback<ArrayList<GraphDot>> response
}
