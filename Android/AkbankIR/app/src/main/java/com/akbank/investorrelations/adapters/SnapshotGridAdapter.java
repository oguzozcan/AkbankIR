package com.akbank.investorrelations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.akbank.investorrelations.R;

/**
 * Created by oguzemreozcan on 05/02/16.
 */
public class SnapshotGridAdapter extends BaseAdapter {

    private final ArrayList<SnapShotItem> snapshotData;
    private final LayoutInflater inflater;

    public SnapshotGridAdapter(final Context context, final ArrayList<SnapShotItem> snapshotData){
        Context context1 = context;
        this.snapshotData = snapshotData;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return snapshotData.size();
    }

    @Override
    public SnapShotItem getItem(int i) {
        return snapshotData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        final SnapShotItem item = getItem(position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.snapshot_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.subTitle = (TextView) rowView.findViewById(R.id.subTitle);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.title.setText(item.title);
        holder.subTitle.setText(item.subTitle);
        return rowView;
    }

    public static class ViewHolder {
        TextView title;
        TextView subTitle;
    }

    public static class SnapShotItem {

        final String title;
        final String subTitle;

        public SnapShotItem(final String title, final String subTitle){
            this.title = title;
            this.subTitle = subTitle;
        }
    }
}
