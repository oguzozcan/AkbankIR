package com.akbank.investorrelations.services;

import com.akbank.investorrelations.objects.SnapshotData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Created by oguzemreozcan on 01/02/16.
 */
public interface SnapshotRestApi {
   // https://cloud.foreks.com/snapshot-service/multisnapshot/delay/15?c=AKBNK.E.BIST&f=last&f=dailyLowest&f=dailyHighest&f=dailyVolume&f=dailyChangePercentageDirection&f=time&f=dailyChange&f=dailyChangePercentage
        @Headers({
                "Authorization: YWtiYW5rLXlhdGlyaW06YTI0Y0JzIXpY"
        })
        @GET("snapshot-service/multisnapshot/delay/15?c=AKBNK.E.BIST&f=last&f=dailyLowest&f=dailyHighest&f=dailyVolume&f=dailyChangePercentageDirection&f=time&f=dailyChange&f=dailyChangePercentage&f=marketCapital")
        Call<ArrayList<SnapshotData>> getSnapShotData(@Header("Accept-Language") String language);
}
