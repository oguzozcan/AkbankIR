package com.mallardduckapps.akbankir.utils;

import android.util.Log;

import com.mallardduckapps.akbankir.AkbankApp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by oguzemreozcan on 26/01/16.
 */
public class TimeUtil {

    public static Locale localeTr = new Locale("tr");
    //public static Locale localeEn = new Locale("en");
    public static DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd MMMM yyyy HH:mm").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public static DateTimeFormatter dtfOutWeekday = DateTimeFormat.forPattern("EEEE").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public final static DateTimeFormatter dtfOutWeekdayShort = DateTimeFormat.forPattern("EEE").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public static DateTimeFormatter dtfOutWOTimeShort = DateTimeFormat.forPattern("dd MMM yyyy").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public static DateTimeFormatter dtfOutWOTime = DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public final static DateTimeFormatter updateDateFormat = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public final static DateTimeFormatter updateDateFormatWOTime = DateTimeFormat.forPattern("dd.MM.yyyy").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    private final static DateTimeFormatter dtfSimple = DateTimeFormat.forPattern("dd.MM.yyyy HH.mm").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    private final static DateTimeFormatter dtfSimpleWOTime = DateTimeFormat.forPattern("dd.MM.yyyy").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    private final static DateTimeFormatter dtfForex = DateTimeFormat.forPattern("yyyyMMddHHmmss").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public static final DateTimeFormatter dfISO = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withChronology(ISOChronology.getInstanceUTC());//.withLocale(TimeUtil.localeTr)
    public final static DateTimeFormatter dtfBarGraph = DateTimeFormat.forPattern("dd.MM").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    public static DateTimeFormatter dtfApiFormat = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
    //"20121201000000"
    private static final String TAG = "TimeUtil";

    public static String getTodayJoda(boolean humanReadable, boolean isTimeIncluded) {
        DateTime dt = DateTime.now();
        if (humanReadable) {
            if(isTimeIncluded){
                return dtfOut.print(dt);
            }else{
                return dtfOutWOTime.print(dt);
            }
        }
        if(isTimeIncluded){
            return updateDateFormat.print(dt);
        }else{
            return updateDateFormatWOTime.print(dt);
        }
    }

    public static void changeLocale(Locale locale){
        localeTr = locale;
        AkbankApp.localeTr = locale;
        //dtfOut.withLocale(locale);
        //dtfOutWeekday.withLocale(locale);
        dtfOut = DateTimeFormat.forPattern("dd MMMM yyyy HH:mm").withLocale(locale).withZone(DateTimeZone.forID("Europe/Istanbul"));
        dtfOutWeekday = DateTimeFormat.forPattern("EEEE").withLocale(locale).withZone(DateTimeZone.forID("Europe/Istanbul"));
        dtfOutWOTimeShort = DateTimeFormat.forPattern("dd MMM yyyy").withLocale(locale).withZone(DateTimeZone.forID("Europe/Istanbul"));
        dtfOutWOTime = DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(locale).withZone(DateTimeZone.forID("Europe/Istanbul"));
        dtfApiFormat = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(locale).withZone(DateTimeZone.forID("Europe/Istanbul"));
        //dtfOutWOTimeShort.withLocale(locale);
        //dtfOutWOTime.withLocale(locale);


//        updateDateFormat.withLocale(locale);
//        updateDateFormatWOTime.withLocale(locale);
//        dtfSimple.withLocale(locale);
//        dtfSimpleWOTime.withLocale(locale);
//        dtfBarGraph.withLocale(locale);
//        dfISO.withLocale(locale);

    }

    public static String getTodayJoda( DateTimeFormatter format) {
        DateTime dt = DateTime.now();
        return format.print(dt);
    }

    public static DateTime getDateAccordingToMillis(long millis) {
        return new DateTime(millis).withZone(DateTimeZone.forID("Europe/Istanbul"));
    }

    public static boolean isItWeekend(String date){
       // dtfOutWOTime
        DateTime dt = dtfOutWOTime.parseDateTime(date).toDateTime();
        Log.d(TAG, "DAY OF WEEK: " + dt.dayOfWeek().get());//dt.dayOfWeek();
        return dt.getDayOfWeek() > 5 ? true : false;
    }

    public static int getDayOfWeek(String date){
        DateTime dt = dtfOutWOTime.parseDateTime(date).toDateTime();
        Log.d(TAG, "DAY OF WEEK: " + dt.dayOfWeek().get());//dt.dayOfWeek();
        return dt.getDayOfWeek();
    }

    public static int getDaysInBetween(DateTime firstTime, DateTime secondTime){
        return Days.daysBetween(firstTime.toLocalDate(),
                secondTime.toLocalDate()).getDays();
    }

    public static Date getDate(long millis){
        Calendar calendar = Calendar.getInstance();
        //return new Date(millis);
       // calendar.setTimeInMillis(millis);
        //return calendar.getTime();
        Date d1 = calendar.getTime();
        calendar.setTimeInMillis(millis);
        calendar.add(Calendar.DATE, 1);
        return d1;
    }

    public static String getDateTxtAccordingToMillis(long millis, DateTimeFormatter formatter) {
        if(formatter == null){
            return dtfOut.print(new DateTime(millis).withZone(DateTimeZone.forID("Europe/Istanbul")));
        }else{
            return formatter.print(new DateTime(millis).withZone(DateTimeZone.forID("Europe/Istanbul")));
        }
    }

    public static String getDateTxtForForex(String dateTime){
       return convertDateTimeFormat(dtfOutWOTime, dtfForex, dateTime);
    }

    public static DateTime getDateTime(String dateTimeText, DateTimeFormatter dtf) {
        return dtf.parseDateTime(dateTimeText);
    }

    public static DateTime getDateTime(String dateTimeText, boolean humanReadable, boolean isTimeIncluded) {
        DateTime dt;
        if (humanReadable) {
            if(isTimeIncluded){
                dt = dtfOut.parseDateTime(dateTimeText).toDateTime();
            }else{
                dt = dtfOutWOTime.parseDateTime(dateTimeText).toDateTime();
            }
        } else {
            if(isTimeIncluded){
                dt = dtfSimple.parseDateTime(dateTimeText).toDateTime();
            }else{
                dt = dtfSimpleWOTime.parseDateTime(dateTimeText).toDateTime();
            }
        }
        return dt;
    }


    public static String getDateTime(String dateTimeText, DateTimeFormatter fromFormat, DateTimeFormatter toFormat) {
        //DateTime dt= fromFormat.parseDateTime(dateTimeText).toDateTime();
        return toFormat.print(fromFormat.parseDateTime(dateTimeText));
    }

    public static String convertSimpleDateToReadableForm(String date, boolean isTimeIncluded) {
        return isTimeIncluded ? dtfOut.print(dtfSimple.parseDateTime(date)) :
                dtfOutWOTime.print(dtfSimpleWOTime.parseDateTime(date));
    }

    public static String getDateBeforeOrAfterToday(int days, boolean humanReadable, boolean isTimeIncluded) {
        DateTime dateTime = DateTime.now();
        return getDateBeforeOrAfter(dateTime, days, humanReadable, isTimeIncluded);
    }

    public static String getDateBeforeOrAfter(DateTime dateTime, int days, boolean humanReadable, boolean isTimeIncluded) {
        if (days >= 0) {
            dateTime = dateTime.plusDays(days);
        } else {
            dateTime = dateTime.minusDays(-1 * days);
        }
        if (humanReadable) {
            if(isTimeIncluded){
                return dtfOut.print(dateTime);
            }else{
                return dtfOutWOTime.print(dateTime);
            }
        }
        if(isTimeIncluded){
            return updateDateFormat.print(dateTime);
        }else{
            return updateDateFormatWOTime.print(dateTime);
        }
    }

    public static String convertDateTimeFormat(DateTimeFormatter fromFormat, DateTimeFormatter toFormat, String date) {
        DateTime dt = fromFormat.parseDateTime(date).toDateTime();
        try {
            return dt.toString(toFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }
}
