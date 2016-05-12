package com.akbank.investorrelations.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class InvestorDaysObject implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.video);
        dest.writeString(this.pdf);
        dest.writeString(this.date);
    }

    public InvestorDaysObject() {
    }

    protected InvestorDaysObject(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.video = in.readString();
        this.pdf = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<InvestorDaysObject> CREATOR = new Parcelable.Creator<InvestorDaysObject>() {
        @Override
        public InvestorDaysObject createFromParcel(Parcel source) {
            return new InvestorDaysObject(source);
        }

        @Override
        public InvestorDaysObject[] newArray(int size) {
            return new InvestorDaysObject[size];
        }
    };
}
