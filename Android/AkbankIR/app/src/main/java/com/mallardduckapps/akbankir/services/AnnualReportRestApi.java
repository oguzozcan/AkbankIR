package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.ReportObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public interface AnnualReportRestApi {

    @GET("annualReports/")
    Call<ArrayList<ReportObject>> getAnnualReports(@Header("Accept-Language") String language);
}
