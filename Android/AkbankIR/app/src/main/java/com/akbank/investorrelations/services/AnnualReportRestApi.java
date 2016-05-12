package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.ReportObject;

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
