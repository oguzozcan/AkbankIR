package com.mallardduckapps.akbankir;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.mallardduckapps.akbankir.adapters.NewsAdapter;
import com.mallardduckapps.akbankir.busevents.EventNewsRequest;
import com.mallardduckapps.akbankir.busevents.EventNewsResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.NewsObject;
import com.mallardduckapps.akbankir.utils.Constants;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Response;

public class AnnouncementAndNewsActivity extends BaseActivity {

    View contentView;
    private RecyclerView newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_announcement_and_news, null, false);
        mContent.addView(contentView, 0);
        newsList = (RecyclerView) findViewById(R.id.newsRecyclerView);
    }

    @Override
    protected void setTag() {
        TAG = "AnnoucementsAndNews";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventNewsRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Menu_AnnouncementsAndNews));
    }

    @Subscribe
    public void onLoadNews(EventNewsResponse event){
        Response<ArrayList<NewsObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<NewsObject> news = response.body();
            //setWebcastsLayout(webcasts.get(0));
            //eventsListView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            newsList.setLayoutManager(mLayoutManager);
            newsList.setAdapter(new NewsAdapter(this, news));
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }


}
