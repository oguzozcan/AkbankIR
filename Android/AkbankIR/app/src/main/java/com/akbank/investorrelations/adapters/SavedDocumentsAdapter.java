package com.akbank.investorrelations.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akbank.investorrelations.WebActivity;
import com.akbank.investorrelations.objects.SavedDocumentObject;

import java.io.File;
import java.util.ArrayList;

import com.akbank.investorrelations.R;

/**
 * Created by oguzemreozcan on 18/04/16.
 */
public class SavedDocumentsAdapter extends RecyclerView.Adapter<SavedDocumentsAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<SavedDocumentObject> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Reports_ADAPTER";

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView titleTv;
        final TextView deleteTv;
        final TextView viewTv;
        final ImageView pdfIcon;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            pdfIcon = (ImageView) itemView.findViewById(R.id.pdfIcon);
            titleTv = (TextView) itemView.findViewById(R.id.reportDescription);
            deleteTv = (TextView) itemView.findViewById(R.id.deleteButton);
            viewTv = (TextView) itemView.findViewById(R.id.viewButton);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_file_item, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public SavedDocumentsAdapter(Activity act, ArrayList<SavedDocumentObject> events) {
        this.activity = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            events.remove(0);
            data = new ArrayList<>(events);
        }
    }

    public void add(SavedDocumentObject data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void remove(int position, SavedDocumentObject report) {
        if (data != null) {
            if(!data.isEmpty()){
                data.remove(report);
                notifyItemRemoved(position);
            }
        }
    }

    public void addData(ArrayList<SavedDocumentObject> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        final SavedDocumentObject report = getItem(position);
        holder.titleTv.setText(report.getFileName());
        //Picasso.with(activity).load(AkbankApp.ROOT_URL + report.getImage()).placeholder(R.drawable.group_copy_4).into(holder.pdfIcon);
        //holder.saveTv.setText(webcast.getCreatedDate());
        //String date = TimeUtil.getDateTime(webcast.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort);
        //holder.dateTv.setText(date);
        holder.viewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPdf = new Intent(activity, WebActivity.class);
                String path = Environment.getExternalStorageDirectory().toString() + SavedDocumentObject.FOLDER_PATH;
                intentPdf.putExtra("uri", path + "/" + report.getFileName());
                intentPdf.putExtra("title", report.getFileName());
                intentPdf.putExtra("type", "pdf");
                activity.startActivity(intentPdf);
            }
        });

        holder.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = Environment.getExternalStorageDirectory().toString() + SavedDocumentObject.FOLDER_PATH +"/"+ report.getFileName();
                File file = new File(path);
                if(file.exists()){
                    Log.d(TAG, "FILE EXISTS");
                    remove(position, report);
                    file.delete();

                }else{
                    Log.d(TAG, "FILE NOT EXISTS");
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

    public SavedDocumentObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
