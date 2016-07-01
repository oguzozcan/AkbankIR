package com.akbank.investorrelations.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.AkbankApp;
import com.akbank.investorrelations.WebcastPlayerActivity;
import com.akbank.investorrelations.objects.WebcastObject;
import com.akbank.investorrelations.utils.TimeUtil;

import java.util.ArrayList;

import com.akbank.investorrelations.R;

/**
 * Created by oguzemreozcan on 13/04/16.
 */
public class WebcastsAdapter extends RecyclerView.Adapter<WebcastsAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<WebcastObject> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Webcasts_ADAPTER";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView titleTv;
        final TextView dateTv;
        final TextView listenButton;
        final Activity activity;
        final RelativeLayout parent;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleTv = (TextView) itemView.findViewById(R.id.webcastsTitle);
            dateTv = (TextView) itemView.findViewById(R.id.dateText);
            listenButton = (TextView) itemView.findViewById(R.id.listenButton);
            parent = (RelativeLayout) itemView.findViewById(R.id.webcastLayout);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_webcast_summary, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public WebcastsAdapter(Activity act, ArrayList<WebcastObject> events) {
        this.activity = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            //events.remove(0);
            data = new ArrayList<>(events);
        }
    }

    public void add(WebcastObject data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<WebcastObject> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final WebcastObject webcast = getItem(position);
        holder.titleTv.setText(webcast.getTitle());
        holder.dateTv.setText(webcast.getCreatedDate());
        String date = TimeUtil.getDateTime(webcast.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort, AkbankApp.localeTr);
        holder.dateTv.setText(date);
        holder.listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, WebcastPlayerActivity.class);
                intent.putExtra("name", webcast.getTitle());
                intent.putExtra("date", webcast.getDate());
                intent.putExtra("postfix", webcast.getAudioUrl());
                activity.startActivity(intent);
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, WebcastPlayerActivity.class);
                intent.putExtra("name", webcast.getTitle());
                intent.putExtra("date", webcast.getDate());
                intent.putExtra("postfix", webcast.getAudioUrl());
                activity.startActivity(intent);
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

    public WebcastObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
