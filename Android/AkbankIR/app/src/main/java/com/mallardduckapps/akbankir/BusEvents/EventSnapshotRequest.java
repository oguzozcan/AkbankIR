package com.mallardduckapps.akbankir.busevents;

import android.widget.GridView;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventSnapshotRequest extends EventRequestParent {

    private GridView gridView;

    public EventSnapshotRequest(String langHeader, GridView gridView) {
        super(langHeader);
        this.gridView = gridView;
    }

    public GridView getGridView() {
        return gridView;
    }
}
