package com.mallardduckapps.akbankir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.adapters.WebcastsAdapter;
import com.mallardduckapps.akbankir.busevents.EventWebcastsRequest;
import com.mallardduckapps.akbankir.busevents.EventWebcastsResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.WebcastObject;
import com.mallardduckapps.akbankir.utils.Constants;
import com.mallardduckapps.akbankir.utils.TimeUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Response;

public class WebcastsActivity extends BaseActivity {

    private View contentView;
    private RecyclerView webcastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_webcasts, null, false);
        mContent.addView(contentView, 0);
        webcastList = (RecyclerView) findViewById(R.id.webcastList);
    }

    @Override
    protected void setTag() {
        TAG = "WebcastsActivity";
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Sub_Menu_1_Webcasts));
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventWebcastsRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Subscribe
    public void onLoadWebcasts(EventWebcastsResponse event){
        Response<ArrayList<WebcastObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<WebcastObject> webcasts = response.body();
            setWebcastsLayout(webcasts.get(0));
            //eventsListView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            webcastList.setLayoutManager(mLayoutManager);
            webcastList.setAdapter(new WebcastsAdapter(this, webcasts));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    private void setWebcastsLayout(final WebcastObject webcastObject ) {
        RelativeLayout webcastLayout = (RelativeLayout) contentView.findViewById(R.id.latestReport);
        TextView webcastTitle = (TextView) webcastLayout.findViewById(R.id.webcastsTitle);
        webcastTitle.setText(webcastObject.getTitle());
        TextView listenButton = (TextView) webcastLayout.findViewById(R.id.listenButton);
        TextView dateText = (TextView ) webcastLayout.findViewById(R.id.dateText);
        String date = TimeUtil.getDateTime(webcastObject.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort);
        dateText.setText(date);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WebcastPlayerActivity
                Intent intent = new Intent(WebcastsActivity.this, WebcastPlayerActivity.class);
                intent.putExtra("name", webcastObject.getTitle());
                intent.putExtra("date", webcastObject.getDate());
                intent.putExtra("postfix", webcastObject.getAudioUrl());
                WebcastsActivity.this.startActivity(intent);
            }
        });

        webcastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebcastsActivity.this, WebcastPlayerActivity.class);
                intent.putExtra("name", webcastObject.getTitle());
                intent.putExtra("date", webcastObject.getDate());
                intent.putExtra("postfix", webcastObject.getAudioUrl());
                WebcastsActivity.this.startActivity(intent);
            }
        });
    }

}
