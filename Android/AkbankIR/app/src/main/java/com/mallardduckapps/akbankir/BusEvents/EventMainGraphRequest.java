package com.mallardduckapps.akbankir.busevents;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventMainGraphRequest {

//    final private ArrayList<MainGraphDot> mainGraphList;
    final private String startDate;
    final private String endDate;
    final private int period;


    public EventMainGraphRequest(String startDate, String endDate, int period){
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
}
