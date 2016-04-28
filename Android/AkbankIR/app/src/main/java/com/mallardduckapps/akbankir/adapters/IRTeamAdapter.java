package com.mallardduckapps.akbankir.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.objects.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by oguzemreozcan on 07/03/16.
 */
public class IRTeamAdapter extends RecyclerView.Adapter<IRTeamAdapter.DataObjectHolder> {
    private final Activity activity;
    private ArrayList<Person> data;
    private LayoutInflater inflater = null;
    private final String TAG = "IR_TEAM_ADAPTER";
    // private final AssetManager manager;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        final TextView nameTv;
        final TextView positionTv;
        final TextView emailTv;
        final TextView phoneTv;
        final CircleImageView portraitImage;
        final Activity activity;

        public DataObjectHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            phoneTv = (TextView) itemView.findViewById(R.id.phoneTv);
            emailTv = (TextView) itemView.findViewById(R.id.emailTv);
            portraitImage = (CircleImageView) itemView.findViewById(R.id.portraitImage);
            positionTv = (TextView) itemView.findViewById(R.id.positionTv);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_ir_team, parent, false);
        return new DataObjectHolder(view, activity);
    }

    public IRTeamAdapter(Activity act, ArrayList<Person> events) {
        this.activity = act;
        //manager = act.getAssets();
        //font = activity.getString(R.string.font_pera_regular);
        //AkbankApp app = (AkbankApp) act.getApplication();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (events != null) {
            data = events;
        }
    }

    public void add(Person data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        int position = this.data.size() - 1;
        notifyItemInserted(position);
    }

    public void addData(ArrayList<Person> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void remove(Person item) {
        int position = data.indexOf(item);
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Person person = getItem(position);
        // String eventDate = TimeUtil.getDateTime(event.getEventDate(), TimeUtil.dtfApiFormat, TimeUtil.dtfOutWOTime);
        //String weekDay = TimeUtil.getDateTime(event.getEventDate(), TimeUtil.dtfApiFormat, TimeUtil.dtfOutWeekday);
        Log.d(TAG, "IMAGE PATH : " + AkbankApp.ROOT_URL + person.getPortait());
        Picasso.with(activity).load(AkbankApp.ROOT_URL + person.getPortait()).placeholder(R.drawable.avatar).into(holder.portraitImage);
        holder.nameTv.setText(person.getName());
        holder.positionTv.setText(person.getPosition());
        holder.emailTv.setText(person.getEmail());
        holder.phoneTv.setText(person.getPhone());
        Log.d(TAG, "Person: " + person.getName());
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public Person getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

