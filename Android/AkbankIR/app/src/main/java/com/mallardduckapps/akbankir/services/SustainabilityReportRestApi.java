package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.ReportObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public interface SustainabilityReportRestApi {

    @GET("sustainabilityReports/")
    Call<ArrayList<ReportObject>> getSustainabilityReports();
}
