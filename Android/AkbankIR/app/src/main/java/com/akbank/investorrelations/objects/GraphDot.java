package com.akbank.investorrelations.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 30/01/16.
 */
public class GraphDot {
    @SerializedName("c")
    private double changePercentage;
    @SerializedName("d")
    private long date;
    @SerializedName("cl")
    private double closeValue;

    public GraphDot(double changePercentage, long date, double closeValue) {
        this.changePercentage = changePercentage;
        this.date = date;
        this.closeValue = closeValue;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(double closeValue) {
        this.closeValue = closeValue;
    }
}
