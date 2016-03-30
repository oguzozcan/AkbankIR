package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by oguzemreozcan on 25/01/16.
 */
public class SnapshotData {

    @SerializedName("dailyVolume")
    private BigDecimal dailyVolume;
    @SerializedName("time")
    private long time;
    @SerializedName("dailyHighest")
    private double dailyHighest;
    @SerializedName("last")
    private double last;
    @SerializedName("c")
    private String name;
    @SerializedName("dailyLowest")
    private double dailyLowest;
    @SerializedName("dailyChangePercentage")
    private double dailyChangePercentage;
    @SerializedName("dailyChange")
    private double dailyChange;
    @SerializedName("dailyChangePercentageDirection")
    private int dailyChangePercentageDirection;
    @SerializedName("marketCapital")
    private BigDecimal marketCapital;

    public BigDecimal getDailyVolume() {
       // BigDecimal d = new BigDecimal(dailyVolume);
       // long value = d.longValue();
       // System.out.println("GET DAILY VOLUME: " +value);
        return dailyVolume;
    }

    public void setDailyVolume(BigDecimal dailyVolume) {
        this.dailyVolume = dailyVolume;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getDailyHighest() {
        return dailyHighest;
    }

    public void setDailyHighest(double dailyHighest) {
        this.dailyHighest = dailyHighest;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDailyLowest() {
        return dailyLowest;
    }

    public void setDailyLowest(double dailyLowest) {
        this.dailyLowest = dailyLowest;
    }

    public double getDailyChangePercentage() {
        return dailyChangePercentage;
    }

    public void setDailyChangePercentage(double dailyChangePercentage) {
        this.dailyChangePercentage = dailyChangePercentage;
    }

    public double getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(double dailyChange) {
        this.dailyChange = dailyChange;
    }

    public double getDailyChangePercentageDirection() {
        return dailyChangePercentageDirection;
    }

    public void setDailyChangePercentageDirection(int dailyChangePercentageDirection) {
        this.dailyChangePercentageDirection = dailyChangePercentageDirection;
    }

    public BigDecimal getMarketCapital() {
        return marketCapital;
    }

    public void setMarketCapital(BigDecimal marketCapital) {
        this.marketCapital = marketCapital;
    }
}
