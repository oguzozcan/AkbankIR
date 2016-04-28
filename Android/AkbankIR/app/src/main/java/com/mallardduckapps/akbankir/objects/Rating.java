package com.mallardduckapps.akbankir.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public class Rating implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.moody);
        dest.writeString(this.fitch);
    }

    public Rating() {
    }

    protected Rating(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.moody = in.readString();
        this.fitch = in.readString();
    }

    public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel source) {
            return new Rating(source);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };
}
