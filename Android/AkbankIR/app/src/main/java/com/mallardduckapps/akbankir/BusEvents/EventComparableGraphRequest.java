package com.mallardduckapps.akbankir.busevents;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventComparableGraphRequest {

    final private ArrayList<String> comparables;
    final private String startDate;
    final private String endDate;
    final private int period;

    public EventComparableGraphRequest(ArrayList<String> comparables, String startDate, String endDate, int period){
        this.comparables = comparables;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getPeriod() {
        return period;
    }

    public ArrayList<String> getComparables() {
        return comparables;
    }
}
