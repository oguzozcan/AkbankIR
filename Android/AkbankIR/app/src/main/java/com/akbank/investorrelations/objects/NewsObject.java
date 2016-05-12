package com.akbank.investorrelations.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 20/03/16.
 */
public class NewsObject {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("image")
    private String image;
    @SerializedName("video")
    private String video;
    @SerializedName("created")
    private String createdDate;
    @SerializedName("date")
    private String date;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
