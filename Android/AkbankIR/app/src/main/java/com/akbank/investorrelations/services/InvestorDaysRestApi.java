package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.InvestorDaysObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public interface InvestorDaysRestApi {
    @GET("days/")
    Call<ArrayList<InvestorDaysObject>> getInvestorDays(@Header("Accept-Language") String language);
}
