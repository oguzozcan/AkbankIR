package com.mallardduckapps.akbankir.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.objects.AnalystCovarageObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public class AnalystCovarageAdapter extends RecyclerView.Adapter<AnalystCovarageAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<AnalystCovarageObject> data;
    private LayoutInflater inflater = null;
    private final String TAG = "AnalystCovarageAdapter";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView companyTitle;
        final TextView analystTitle;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
            companyTitle = (TextView) itemView.findViewById(R.id.companyTitle);
            analystTitle = (TextView) itemView.findViewById(R.id.analystTitle);
//            dateTv = (TextView) itemView.findViewById(R.id.dateText);
//            listenButton = (TextView) itemView.findViewById(R.id.listenButton);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_analyst_item, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public AnalystCovarageAdapter(Activity act, ArrayList<AnalystCovarageObject> events) {
        this.activity = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            events.remove(0);
            data = new ArrayList<>(events);
        }
    }

    public void add(AnalystCovarageObject data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<AnalystCovarageObject> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        AnalystCovarageObject covarageObject = getItem(position);
        holder.companyTitle.setText(covarageObject.getName());
        holder.analystTitle.setText(covarageObject.getAnalysts());
//        holder.dateTv.setText(newsObject.getCreatedDate());
//        String date = TimeUtil.getDateTime(newsObject.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort);
//        holder.dateTv.setText(date);
//        holder.listenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public AnalystCovarageObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

