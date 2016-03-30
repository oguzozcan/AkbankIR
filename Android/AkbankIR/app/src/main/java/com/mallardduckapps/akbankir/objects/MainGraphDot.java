package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by oguzemreozcan on 25/01/16.
 */
public class MainGraphDot {

    //close
    @SerializedName("c")
    private double c;
    //date
    @SerializedName("d")
    private long d;

    @SerializedName("v")
    private double v;

    public double getCloseValue() {
        return c;
    }

    public void setCloseValue(double c) {
        this.c = c;
    }

    public long getDate() {
        return d;
    }

    public void setDate(long d) {
        this.d = d;
    }

    public double getV() {
        //BigDecimal d = new BigDecimal(v);
        //long value = d.longValue();
        //System.out.println(value);
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
