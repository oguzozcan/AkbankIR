package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 20/03/16.
 */
public class DashboardContainerObject {

    @SerializedName("sustainabilityReport")
    private ReportObject reportObject;
    @SerializedName("investorPresentation")
    private InvestorPresentationObject investorPresentationObject;
    @SerializedName("webcast")
    private WebcastObject webcastObject;
    @SerializedName("news")
    private ArrayList<NewsObject> newsObjectArrayList;
    @SerializedName("calendar")
    private ArrayList<CalendarEvent> calendarEvents;
    @SerializedName("annualReport")
    private AnnualReportObject annualReportObject;

    public ReportObject getReportObject() {
        return reportObject;
    }

    public void setReportObject(ReportObject reportObject) {
        this.reportObject = reportObject;
    }

    public InvestorPresentationObject getInvestorPresentationObject() {
        return investorPresentationObject;
    }

    public void setInvestorPresentationObject(InvestorPresentationObject investorPresentationObject) {
        this.investorPresentationObject = investorPresentationObject;
    }

    public WebcastObject getWebcastObject() {
        return webcastObject;
    }

    public void setWebcastObject(WebcastObject webcastObject) {
        this.webcastObject = webcastObject;
    }

    public ArrayList<NewsObject> getNewsObjectArrayList() {
        return newsObjectArrayList;
    }

    public void setNewsObjectArrayList(ArrayList<NewsObject> newsObjectArrayList) {
        this.newsObjectArrayList = newsObjectArrayList;
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(ArrayList<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public AnnualReportObject getAnnualReportObject() {
        return annualReportObject;
    }

    public void setAnnualReportObject(AnnualReportObject annualReportObject) {
        this.annualReportObject = annualReportObject;
    }
}
