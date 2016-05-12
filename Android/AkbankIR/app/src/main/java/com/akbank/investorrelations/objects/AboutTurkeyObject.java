package com.akbank.investorrelations.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public class AboutTurkeyObject {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("created")
    private String created;
    @SerializedName("date")
    private String date;
    @SerializedName("pdf_title")
    private String pdfTitle;
    @SerializedName("pdf_title2")
    private String pdfTitle2;
    @SerializedName("pdf")
    private String pdf;
    @SerializedName("pdf2")
    private String pdf2;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getCreated() {
        return created;
    }

    public String getDate() {
        return date;
    }

    public String getPdfTitle() {
        return pdfTitle;
    }

    public String getPdfTitle2() {
        return pdfTitle2;
    }

    public String getPdf() {
        return pdf;
    }

    public String getPdf2() {
        return pdf2;
    }
}
