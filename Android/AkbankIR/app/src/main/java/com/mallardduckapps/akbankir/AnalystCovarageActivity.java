package com.mallardduckapps.akbankir;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.mallardduckapps.akbankir.busevents.EventAnalystCovarageRequest;
import com.mallardduckapps.akbankir.busevents.EventAnalystCovarageResponse;
import com.mallardduckapps.akbankir.fragments.AnalystCovarageFragment;
import com.mallardduckapps.akbankir.objects.AnalystCovarageObject;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Response;

public class AnalystCovarageActivity extends BaseActivity {
    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_analyst_covarage, null, false);
        mContent.addView(contentView, 0);
        onTitleTextChange(getString(R.string.Menu_AnalystCoverage));
    }

    @Override
    protected void setTag() {
        TAG = "AnalystCovarageActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventAnalystCovarageRequest());
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Subscribe
    public void onLoadAnalystCovarage(EventAnalystCovarageResponse event){
        Response<ArrayList<AnalystCovarageObject>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<AnalystCovarageObject> covarageList = response.body();
            //setWebcastsLayout(webcasts.get(0));
            //eventsListView.setHasFixedSize(true);

            final ViewPager mViewPager = (ViewPager) contentView.findViewById(R.id.pager);
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), covarageList);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            final TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.tabLayout);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tomato));
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.greyish_brown), ContextCompat.getColor(this, R.color.tomato));
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(mViewPager);
                }
            });
            mViewPager.setOffscreenPageLimit(1);
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final ArrayList<AnalystCovarageObject> localList;
        final ArrayList<AnalystCovarageObject> internationalList;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<AnalystCovarageObject> covarageList) {
            super(fm);
            localList = new ArrayList<>();
            internationalList = new ArrayList<>();
            for(AnalystCovarageObject aco : covarageList){
                if(aco.getType().equals("l")){
                    localList.add(aco);
                }else if(aco.getType().equals("i")){
                    internationalList.add(aco);
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return AnalystCovarageFragment.newInstance(position + 1, position == 0 ? internationalList : localList);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.International);//.toUpperCase(l);
                case 1:
                    return getString(R.string.Local);//.toUpperCase(l);
            }
            return null;
        }
    }
}
