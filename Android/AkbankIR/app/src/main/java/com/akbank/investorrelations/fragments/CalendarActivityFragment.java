package com.akbank.investorrelations.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akbank.investorrelations.AkbankApp;
import com.akbank.investorrelations.adapters.EventsCalendarAdapter;
import com.akbank.investorrelations.busevents.EventCalendarRequest;
import com.akbank.investorrelations.busevents.EventCalendarResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.CalendarEvent;
import com.akbank.investorrelations.utils.Constants;
import com.akbank.investorrelations.utils.DataSaver;
import com.akbank.investorrelations.utils.EventDecorator;
import com.akbank.investorrelations.utils.TimeUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.akbank.investorrelations.R;
import retrofit2.Response;

public class CalendarActivityFragment extends BaseFragment {

    private RecyclerView eventsListView;
    private DateTime fromDate;
    private ArrayList<CalendarEvent> calendarEvents;
    private MaterialCalendarView calendarView;
    private boolean filterEnabled = false;
    private boolean filterMajorElseIrEvents = false;

    public CalendarActivityFragment() {
    }

    @Override
    protected void setTag() {
        if(AkbankApp.localeTr.toString().equalsIgnoreCase("en")){
            TAG = "Calendar";
        }else{
            TAG = "Takvim";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        eventsListView = (RecyclerView) view.findViewById(R.id.eventsListView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                Log.d(TAG, date.getDate().toString());
                String dateSelected = new SimpleDateFormat("yyyy-MM-dd", TimeUtil.localeTr).format(date.getDate());
                fromDate = TimeUtil.getDateTime(dateSelected,TimeUtil.dtfApiFormat);
                Log.d(TAG, "Date formatted : " + dateSelected);
                if(calendarEvents != null){
                    eventsListView.setAdapter(new EventsCalendarAdapter(getActivity(), sortCalendarEventsAccordingToDate(calendarEvents)));
                }
            }
        });

        filterEnabled = false;
        filterMajorElseIrEvents = false;
        TextView majorFilterButton = (TextView) view.findViewById(R.id.majorButton);
        TextView irFilterButton = (TextView) view.findViewById(R.id.irButton);
        majorFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calendarEvents != null){
                    if (!filterMajorElseIrEvents) {
                        filterEnabled = true;
                    } else {
                        filterEnabled = false;
                    }
                    filterMajorElseIrEvents = true;
                    eventsListView.setAdapter(new EventsCalendarAdapter(getActivity(), sortCalendarEventsAccordingToDate(calendarEvents)));
                }
            }
        });
        irFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calendarEvents != null){
                    //filterEnabled = !filterEnabled;
                    if (filterMajorElseIrEvents) {
                        filterEnabled = true;
                    } else {
                        filterEnabled = false;
                    }
                    filterMajorElseIrEvents = false;
                    eventsListView.setAdapter(new EventsCalendarAdapter(getActivity(), sortCalendarEventsAccordingToDate(calendarEvents)));
                }
            }
        });
        fromDate = DateTime.now();//TimeUtil.getTodayJoda(TimeUtil.dtfApiFormat);
        calendarView.setDateSelected(new Date(System.currentTimeMillis()), true);
        DataSaver ds = app.getDataSaver();
        app.getBus().post(new EventCalendarRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
        return view;
    }

    @Subscribe
    public void onLoadCalendarEvents(final EventCalendarResponse event) {
        Response<ArrayList<CalendarEvent>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            calendarEvents = response.body();
            //eventsListView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            eventsListView.setLayoutManager(mLayoutManager);
            eventsListView.setAdapter(new EventsCalendarAdapter(getActivity(), sortCalendarEventsAccordingToDate(calendarEvents)));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    private ArrayList<CalendarEvent> sortCalendarEventsAccordingToDate(ArrayList<CalendarEvent> calendarEvents){
        ArrayList<CalendarEvent> calendarEventsFromDate = new ArrayList<>();
        List<CalendarDay> majorList = new ArrayList<CalendarDay>();
        List<CalendarDay> irList = new ArrayList<CalendarDay>();
        Calendar calendar = Calendar.getInstance(AkbankApp.localeTr);
        for(CalendarEvent ce : calendarEvents){
            if(TimeUtil.getDaysInBetween(fromDate, TimeUtil.getDateTime(ce.getEventDate(), TimeUtil.dtfApiFormat)) >= 0){
                Date date;
                try {
                    //TODO locale
                    date = new SimpleDateFormat("yyyy-MM-dd", AkbankApp.localeTr).parse(ce.getEventDate()); //TimeUtil.localeTr)
                    calendar.setTime(date);
                    CalendarDay calendarDay = CalendarDay.from(calendar);
                    if(ce.getType().equals(EventsCalendarAdapter.MAJOR)){
                        majorList.add(calendarDay);
                    }else{
                        irList.add(calendarDay);
                    }
                    Log.d(TAG, "IR DAYS: " + ce.getEventDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!filterEnabled){
                    calendarEventsFromDate.add(ce);
                }else{
                    if((filterMajorElseIrEvents && ce.getType().equals(EventsCalendarAdapter.MAJOR)) || (!filterMajorElseIrEvents && ce.getType().equals(EventsCalendarAdapter.IR))){
                        calendarEventsFromDate.add(ce);
                    }
                }
                Log.d(TAG, "EVENT: " + ce.getEventDate() + "");
            }
        }
        Collections.sort(calendarEventsFromDate, new Comparator<CalendarEvent>() {
            @Override
            public int compare(CalendarEvent lhs, CalendarEvent rhs) {
                return lhs.getEventDate().compareTo(rhs.getEventDate());
            }
        });
        calendarView.addDecorators(new EventDecorator(Color.BLACK, majorList));
        calendarView.addDecorators(new EventDecorator(ContextCompat.getColor(getContext(), R.color.tomato), majorList));
        calendarView.invalidateDecorators();
        return calendarEventsFromDate;
    }
}
