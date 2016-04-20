package com.mallardduckapps.akbankir.busevents;

/**
 * Created by oguzemreozcan on 19/04/16.
 */
public class EventPagesRequest {
    int pageNumber;

    public EventPagesRequest(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
