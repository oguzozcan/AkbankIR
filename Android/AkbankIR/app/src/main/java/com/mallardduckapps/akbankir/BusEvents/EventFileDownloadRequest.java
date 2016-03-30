package com.mallardduckapps.akbankir.busevents;

import android.content.Context;

/**
 * Created by oguzemreozcan on 25/03/16.
 */
public class EventFileDownloadRequest {

    private final String url;
    private final String postFix;
    private final Context context;

    public EventFileDownloadRequest(Context context, String url, String postFix) {
        this.context = context;
        this.url = url;
        this.postFix = postFix;
    }

    public String getUrl() {
        return url;
    }

    public String getPostFix() {
        return postFix;
    }

    public Context getContext() {
        return context;
    }
}
