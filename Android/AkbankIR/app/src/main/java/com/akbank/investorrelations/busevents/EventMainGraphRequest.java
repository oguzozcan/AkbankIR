package com.akbank.investorrelations.busevents;

/**
 * Created by oguzemreozcan on 04/03/16.
 */
public class EventMainGraphRequest extends EventRequestParent{

    final private String startDate;
    final private String endDate;
    final private int period;
    final private int daysBetween;

    public EventMainGraphRequest(String langHeader,String startDate, String endDate, int period, int daysBetween){
        super(langHeader);
        this.startDate = startDate;
        this.daysBetween = daysBetween;
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

    public int getDaysBetween() {
        return daysBetween;
    }
}
