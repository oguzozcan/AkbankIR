package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public class Rating {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("moody")
    private String moody;
    @SerializedName("fitch")
    private String fitch;

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

    public String getMoody() {
        return moody;
    }

    public void setMoody(String moody) {
        this.moody = moody;
    }

    public String getFitch() {
        return fitch;
    }

    public void setFitch(String fitch) {
        this.fitch = fitch;
    }
}
