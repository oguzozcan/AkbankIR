package com.mallardduckapps.akbankir.busevents;

import android.widget.GridView;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventSnapshotRequest {

    private GridView gridView;

    public EventSnapshotRequest(GridView gridView) {
        this.gridView = gridView;
    }

    public GridView getGridView() {
        return gridView;
    }
}
