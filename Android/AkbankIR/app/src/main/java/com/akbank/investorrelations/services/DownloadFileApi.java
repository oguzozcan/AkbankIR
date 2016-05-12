package com.akbank.investorrelations.services;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Created by oguzemreozcan on 24/03/16.
 */
public interface DownloadFileApi {

    //@GET("{url}")
    //@Streaming
    @GET("{url}")
    @Streaming
     Call<ResponseBody> getFile(@Path("url") String url);


}
