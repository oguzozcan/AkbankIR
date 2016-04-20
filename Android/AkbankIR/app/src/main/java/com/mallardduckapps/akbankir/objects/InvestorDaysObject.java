package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class InvestorDaysObject {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private String video;
    @SerializedName("pdf")
    private String pdf;
    @SerializedName("date")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
