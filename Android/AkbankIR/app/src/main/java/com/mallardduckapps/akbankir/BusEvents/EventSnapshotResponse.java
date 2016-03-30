package com.mallardduckapps.akbankir.busevents;

import android.widget.GridView;

import com.mallardduckapps.akbankir.objects.SnapshotData;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventSnapshotResponse {

    private final Response<ArrayList<SnapshotData>> response;
    private final GridView snapShotGridView;

    public EventSnapshotResponse(Response<ArrayList<SnapshotData>> response, GridView snapShotGridView) {
        this.response = response;
        this.snapShotGridView = snapShotGridView;
    }

    public Response<ArrayList<SnapshotData>> getResponse() {
        return response;
    }

    public GridView getSnapShotGridView() {
        return snapShotGridView;
    }
}
