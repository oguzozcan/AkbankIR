package com.akbank.investorrelations.busevents;

/**
 * Created by oguzemreozcan on 19/04/16.
 */
public class EventPagesRequest extends EventRequestParent {
    int pageNumber;

    public EventPagesRequest(String langHeader, int pageNumber){
        super(langHeader);
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
