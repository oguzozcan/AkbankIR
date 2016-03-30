package com.mallardduckapps.akbankir.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.objects.CalendarEvent;
import com.mallardduckapps.akbankir.utils.TimeUtil;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 06/03/16.
 */
public class EventsCalendarAdapter extends RecyclerView.Adapter<EventsCalendarAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<CalendarEvent> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Calendar_ADAPTER";
//    private final AssetManager manager;
    public static final String MAJOR = "MAJOR";
    public static final String IR = "IR";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView eventNameTv;
        final TextView dateTv;
        final LinearLayout imageView;
        final ImageView addToCalendarView;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            imageView = (LinearLayout) itemView.findViewById(R.id.imageView);
            eventNameTv = (TextView) itemView.findViewById(R.id.eventNameTextView);
            addToCalendarView = (ImageView) itemView.findViewById(R.id.addToCalendarIcon);
            dateTv = (TextView) itemView.findViewById(R.id.dateTextView);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.calendar_event_row, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public EventsCalendarAdapter(Activity act, ArrayList<CalendarEvent> events) {
        this.activity = act;
//        manager = act.getAssets();
//        AkbankApp app = (AkbankApp) act.getApplication();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            data = events;
        }
    }

    public void add(CalendarEvent data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<CalendarEvent> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void remove(CalendarEvent item) {
        int position = data.indexOf(item);
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        CalendarEvent event = getItem(position);
        String eventDate = TimeUtil.getDateTime(event.getEventDate(), TimeUtil.dtfApiFormat, TimeUtil.dtfOutWOTimeShort);
        String weekDay = TimeUtil.getDateTime(event.getEventDate(), TimeUtil.dtfApiFormat, TimeUtil.dtfOutWeekday);
        holder.dateTv.setText(eventDate + "\n" + weekDay);
        holder.eventNameTv.setText(event.getTitle());
        Log.d(TAG, "EVENT TYPE: " + event.getType() + " - " + eventDate);
        if(event.getType().equals(MAJOR)){
            holder.imageView.setBackgroundResource(R.drawable.calendar_major_events_background);
        }else{
            holder.imageView.setBackgroundResource(R.drawable.calendar_ircalendar_background);
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public CalendarEvent getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}


