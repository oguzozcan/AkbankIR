package com.akbank.investorrelations.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public class AnalystCovarageObject implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("analysts")
    private String analysts;
    @SerializedName("type")
    private String type;
    @SerializedName("pdf_url")
    private String pdfUrl;
    @SerializedName("pdf2")
    private String pdf2;
    @SerializedName("pdf3")
    private String pdf3;
    @SerializedName("image")
    private String image;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdf2() {
        return pdf2;
    }

    public void setPdf2(String pdf2) {
        this.pdf2 = pdf2;
    }

    public String getPdf3() {
        return pdf3;
    }

    public void setPdf3(String pdf3) {
        this.pdf3 = pdf3;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getAnalysts() {
        return analysts;
    }

    public void setAnalysts(String analysts) {
        this.analysts = analysts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.analysts);
        dest.writeString(this.type);
        dest.writeString(this.pdfUrl);
        dest.writeString(this.pdf2);
        dest.writeString(this.pdf3);
        dest.writeString(this.image);
        dest.writeString(this.createdDate);
        dest.writeString(this.date);
    }

    public AnalystCovarageObject() {
    }

    protected AnalystCovarageObject(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.analysts = in.readString();
        this.type = in.readString();
        this.pdfUrl = in.readString();
        this.pdf2 = in.readString();
        this.pdf3 = in.readString();
        this.image = in.readString();
        this.createdDate = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<AnalystCovarageObject> CREATOR = new Parcelable.Creator<AnalystCovarageObject>() {
        @Override
        public AnalystCovarageObject createFromParcel(Parcel source) {
            return new AnalystCovarageObject(source);
        }

        @Override
        public AnalystCovarageObject[] newArray(int size) {
            return new AnalystCovarageObject[size];
        }
    };
}
