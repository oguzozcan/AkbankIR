package com.akbank.investorrelations.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akbank.investorrelations.AkbankApp;
import com.akbank.investorrelations.fragments.DownloadDialogFragment;
import com.akbank.investorrelations.objects.ReportObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.akbank.investorrelations.R;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<ReportObject> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Reports_ADAPTER";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView titleTv;
        final TextView saveTv;
        final TextView viewTv;
        final ImageView pdfIcon;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            pdfIcon = (ImageView) itemView.findViewById(R.id.pdfIcon);
            titleTv = (TextView) itemView.findViewById(R.id.reportDescription);
            saveTv = (TextView) itemView.findViewById(R.id.saveButton);
            viewTv = (TextView) itemView.findViewById(R.id.viewButton);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_report_item, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public ReportsAdapter(Activity act, ArrayList<ReportObject> events) {
        this.activity = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            //events.remove(0);
            data = new ArrayList<>(events);
            data.remove(0);
        }
    }

    public void add(ReportObject data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<ReportObject> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final ReportObject report = getItem(position);
        holder.titleTv.setText(report.getTitle());
        Picasso.with(activity).load(AkbankApp.ROOT_URL + report.getImage()).placeholder(R.drawable.group_copy_4).into(holder.pdfIcon);

        //holder.saveTv.setText(webcast.getCreatedDate());
        //String date = TimeUtil.getDateTime(webcast.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort);
        //holder.dateTv.setText(date);
        holder.saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = activity.getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", report.getTitle());
                b.putString("url", report.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, activity.getString(R.string.Downloading));
            }
        });

        holder.viewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = activity.getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", report.getTitle());
                b.putString("url", report.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, activity.getString(R.string.Opening));
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

    public ReportObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

