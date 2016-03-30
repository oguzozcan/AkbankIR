package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;
import com.mallardduckapps.akbankir.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 30/01/16.
 */
public class ComparableStockData {

    @SerializedName(Constants.XU30)
    ArrayList<GraphDot> xu30Data;
    @SerializedName(Constants.XU100)
    ArrayList<GraphDot> xu100Data;
    @SerializedName(Constants.XBANKS)
    ArrayList<GraphDot> xBanksData;
    @SerializedName(Constants.AKBANK)
    ArrayList<GraphDot> akbankData;
    @SerializedName(Constants.USD)
    ArrayList<GraphDot> usdData;
    @SerializedName(Constants.EUR)
    ArrayList<GraphDot> euroData;

    public ArrayList<GraphDot> getXu30Data() {
        return xu30Data;
    }

    public void setXu30Data(ArrayList<GraphDot> xu30Data) {
        this.xu30Data = xu30Data;
    }

    public ArrayList<GraphDot> getXu100Data() {
        return xu100Data;
    }

    public void setXu100Data(ArrayList<GraphDot> xu100Data) {
        this.xu100Data = xu100Data;
    }

    public ArrayList<GraphDot> getxBanksData() {
        return xBanksData;
    }

    public void setxBanksData(ArrayList<GraphDot> xBanksData) {
        this.xBanksData = xBanksData;
    }

    public ArrayList<GraphDot> getAkbankData() {
        return akbankData;
    }

    public void setAkbankData(ArrayList<GraphDot> akbankData) {
        this.akbankData = akbankData;
    }

    public ArrayList<GraphDot> getUsdData() {
        return usdData;
    }

    public void setUsdData(ArrayList<GraphDot> usdData) {
        this.usdData = usdData;
    }

    public ArrayList<GraphDot> getEuroData() {
        return euroData;
    }

    public void setEuroData(ArrayList<GraphDot> euroData) {
        this.euroData = euroData;
    }
}
