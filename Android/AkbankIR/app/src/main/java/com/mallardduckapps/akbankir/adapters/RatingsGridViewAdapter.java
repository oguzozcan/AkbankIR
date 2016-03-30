package com.mallardduckapps.akbankir.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallardduckapps.akbankir.R;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 22/03/16.
 */
public class RatingsGridViewAdapter extends RecyclerView.Adapter<RatingsGridViewAdapter.DataObjectHolder> {

    private final ArrayList<String> ratings;
    private final Activity context;
    private final LayoutInflater inflater;

    public RatingsGridViewAdapter(final Activity context, ArrayList<String> ratings) {
        this.context = context;
        this.ratings = ratings;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ratings_row, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        String item = getItem(position);
        int viewType = getItemViewType(position);
        if (item != null) {
            holder.title.setText(item);
            Log.d("adapter", "GETITEM: " + item);
            if (viewType == 1) {
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.greyish_brown));
            } else if (viewType == 2) {
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.tomato));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (ratings == null) {
            return 0;
        }
        return ratings.size();
    }

    public String getItem(int position) {
        return ratings.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 3 == 0 || position < 3) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        View rowView = convertView;
//        //final Rating item = ;
//        if (rowView == null) {
//            rowView = inflater.inflate(R.layout.ratings_row, parent, false);
//            holder = new ViewHolder();
//            holder.title = (TextView) rowView.findViewById(R.id.title);
//            rowView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) rowView.getTag();
//        }
//        holder.title.setText(getItem(position));
//        Log.d("adapter", "GETITEM: " + getItem(position));
//        if(position % 3 == 0 || position < 3)
//            holder.title.setTextColor(ContextCompat.getColor(context, R.color.greyish_brown));
//        else{
//            holder.title.setTextColor(ContextCompat.getColor(context, R.color.tomato));
//        }
//        return rowView;
//    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView title;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }

    }
}