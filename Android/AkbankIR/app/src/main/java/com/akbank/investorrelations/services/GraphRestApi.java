package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.ComparableStockData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by oguzemreozcan on 30/01/16.
 */
public interface GraphRestApi {
//cloud.foreks.com/historical-service/compare/intraday/delay/15/period/1440/from/20150505000000/to/20150510000000?c=AKBNK.E.BIST&c=XU100.I.BIST&c=XU030.I.BIST&c=XBANK.I.BIST&c=SUSD&c=SEUR
    //"https://cloud.foreks.com/historical-service/intraday/delay/15/code/AKBNK.E.BIST/period/1440/from/20121201000000/to/20130102000000?f=c"
    @Headers({
            "Authorization: YWtiYW5rLXlhdGlyaW06YTI0Y0JzIXpY"
    })
    @GET("historical-service/compare/intraday/delay/15/period/{period}/from/{from}/to/{to}")
    Call<ComparableStockData> getComparableGraphData(@Header("Accept-Language") String language,@Path("period") int period, @Path("from") String from, @Path("to") String to, @Query("c") List<String> comparableIds);
}







