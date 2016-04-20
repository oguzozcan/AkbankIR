package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 19/04/16.
 */
public class PagesObject {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("pdf_title")
    private String pdfTitle;
    @SerializedName("pdf")
    private String pdf;
    @SerializedName("pdf2")
    private String pdf2;
    @SerializedName("pdf_title2")
    private String pdfTitle2;
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

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getPdf2() {
        return pdf2;
    }

    public void setPdf2(String pdf2) {
        this.pdf2 = pdf2;
    }

    public String getPdfTitle2() {
        return pdfTitle2;
    }

    public void setPdfTitle2(String pdfTitle2) {
        this.pdfTitle2 = pdfTitle2;
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
