package com.akbank.investorrelations;

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

import com.akbank.investorrelations.busevents.EventAnalystCovarageRequest;
import com.akbank.investorrelations.busevents.EventAnalystCovarageResponse;
import com.akbank.investorrelations.fragments.AnalystCovarageFragment;
import com.akbank.investorrelations.objects.AnalystCovarageObject;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.utils.Constants;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Response;

public class AnalystCovarageActivity extends BaseActivity {

    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_analyst_covarage, null, false);
        mContent.addView(contentView, 0);
    }

    @Override
    protected void setTag() {
        TAG = "AnalystCovarageActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        app.getBus().post(new EventAnalystCovarageRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Menu_AnalystCoverage));
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
                if(aco.getType().equalsIgnoreCase("l")){
                    localList.add(aco);
                }else if(aco.getType().equalsIgnoreCase("i")){
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
