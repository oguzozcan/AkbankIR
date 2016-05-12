package com.akbank.investorrelations.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akbank.investorrelations.InvestorDaysDetailActivity;
import com.akbank.investorrelations.R;
import com.akbank.investorrelations.objects.InvestorDaysObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 16/04/16.
 */
public class InvestorDaysAdapter extends RecyclerView.Adapter<InvestorDaysAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<InvestorDaysObject> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Reports_ADAPTER";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView titleTv;
        final TextView viewDetailsTv;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            titleTv = (TextView) itemView.findViewById(R.id.reportDescription);
            viewDetailsTv = (TextView) itemView.findViewById(R.id.viewButton);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_investor_days_item, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public InvestorDaysAdapter(Activity act, ArrayList<InvestorDaysObject> events) {
        this.activity = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            events.remove(0);
            data = new ArrayList<>(events);
        }
    }

    public void add(InvestorDaysObject data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<InvestorDaysObject> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final InvestorDaysObject report = getItem(position);
        holder.titleTv.setText(report.getTitle());
        //holder.saveTv.setText(webcast.getCreatedDate());
        //String date = TimeUtil.getDateTime(webcast.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort);
        //holder.dateTv.setText(date);
        holder.viewDetailsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(activity, InvestorDaysDetailActivity.class);
                detail.putExtra("postfix", report.getVideo());
                activity.startActivity(detail);
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

    public InvestorDaysObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}


