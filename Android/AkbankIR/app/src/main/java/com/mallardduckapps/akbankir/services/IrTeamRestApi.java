package com.mallardduckapps.akbankir.services;

import com.mallardduckapps.akbankir.objects.Person;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by oguzemreozcan on 06/03/16.
 */
public interface IrTeamRestApi {
        @GET("team/")
        Call<ArrayList<Person>> getIrTeam();
}
