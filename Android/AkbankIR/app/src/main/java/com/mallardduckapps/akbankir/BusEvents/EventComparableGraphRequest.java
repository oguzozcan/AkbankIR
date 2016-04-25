package com.mallardduckapps.akbankir.busevents;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventComparableGraphRequest extends EventRequestParent {

    final private ArrayList<String> comparables;
    final private String startDate;
    final private String endDate;
    final private int period;

    public EventComparableGraphRequest(String header, ArrayList<String> comparables, String startDate, String endDate, int period){
        super(header);
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
