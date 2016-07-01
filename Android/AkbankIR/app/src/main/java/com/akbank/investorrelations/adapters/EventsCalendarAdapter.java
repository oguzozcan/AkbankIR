package com.akbank.investorrelations.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akbank.investorrelations.AkbankApp;
import com.akbank.investorrelations.R;
import com.akbank.investorrelations.objects.CalendarEvent;
import com.akbank.investorrelations.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        final CalendarEvent event = getItem(position);
        String eventDate = TimeUtil.getDateTime(event.getEventDate(), TimeUtil.dtfApiFormat, TimeUtil.dtfOutWOTimeShort, AkbankApp.localeTr);
        String weekDay = TimeUtil.getDateTime(event.getEventDate(), TimeUtil.dtfApiFormat, TimeUtil.dtfOutWeekday, AkbankApp.localeTr);
        holder.dateTv.setText(new StringBuilder().append(eventDate).append("\n").append(weekDay).toString());
        holder.eventNameTv.setText(event.getTitle());
        Log.d(TAG, "EVENT TYPE: " + event.getType() + " - " + eventDate);
        if(event.getType().equals(MAJOR)){
            holder.imageView.setBackgroundResource(R.drawable.calendar_major_events_background);
        }else{
            holder.imageView.setBackgroundResource(R.drawable.calendar_ircalendar_background);
        }

        holder.addToCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date d;
                try {
                    d = formatter.parse(event.getEventDate());
                    long timestamp = d.getTime();
                    intent.putExtra("beginTime", timestamp);
                    intent.putExtra("allDay", true);
                    intent.putExtra("rrule", "FREQ=YEARLY");
                    intent.putExtra("endTime", timestamp);//TimeUtil.getDateTime(event.getEventDate(), false, false).getMillis()+60*60*1000);
                    intent.putExtra("title", event.getTitle());
                    activity.startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
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


