package com.akbank.investorrelations.busevents;

/**
 * Created by oguzemreozcan on 21/04/16.
 */
public class EventRequestParent {

    private final String langHeader;

    public EventRequestParent(String langHeader) {
        this.langHeader = langHeader;
    }

    public String getLangHeader() {
        return langHeader;
    }
}
