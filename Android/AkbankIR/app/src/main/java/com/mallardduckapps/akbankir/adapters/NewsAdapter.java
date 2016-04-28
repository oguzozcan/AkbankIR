package com.mallardduckapps.akbankir.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallardduckapps.akbankir.CalendarActivity;
import com.mallardduckapps.akbankir.NewsDetailActivity;
import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.objects.NewsObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<NewsObject> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Webcasts_ADAPTER";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView titleTv;
//        final TextView dateTv;
//        final TextView listenButton;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleTv = (TextView) itemView.findViewById(R.id.newsTitle);
//            dateTv = (TextView) itemView.findViewById(R.id.dateText);
//            listenButton = (TextView) itemView.findViewById(R.id.listenButton);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_news_item, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public NewsAdapter(Activity act, ArrayList<NewsObject> events) {
        this.activity = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            //events.remove(0);
            data = events;
        }
    }

    public void add(NewsObject data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<NewsObject> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final NewsObject newsObject = getItem(position);
        holder.titleTv.setText(newsObject.getTitle());
        holder.titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NewsDetailActivity.class);
                intent.putExtra("title", newsObject.getTitle());
                intent.putExtra("description", newsObject.getText());
                activity.startActivity(intent);
            }
        });
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

    public NewsObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
