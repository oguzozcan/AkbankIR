package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.ReportObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public interface EarningPresentationsRestApi {
    @GET("financialPresentations/")
    Call<ArrayList<ReportObject>> getEarningPresentations(@Header("Accept-Language") String language);
}
