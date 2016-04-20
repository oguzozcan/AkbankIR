package com.mallardduckapps.akbankir;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.mallardduckapps.akbankir.fragments.TermsFragment;

public class TermsActivity extends BaseActivity {
    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_terms, null, false);
        mContent.addView(contentView, 0);

        final ViewPager mViewPager = (ViewPager) contentView.findViewById(R.id.pager);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
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
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if(i == 0){
                        tabLayout.getTabAt(i).setIcon(R.drawable.turkey_flag);
                    }else{
                        tabLayout.getTabAt(i).setIcon(R.drawable.us_flag);
                    }

                }
            }
        });
        mViewPager.setOffscreenPageLimit(1);

        onTitleTextChange(getString(R.string.Terms_of_Services));
    }

    @Override
    protected void setTag() {
        TAG = "AnalystCovarageActivity";
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        app.getBus().register(this);
//        app.getBus().post(new EventPagesRequest(1));
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        app.getBus().unregister(this);
//    }
//
//    @Subscribe
//    public void onLoadPages(EventPagesResponse event){
//        Response<PagesObject> response = event.getResponse();
//        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
//        Log.d(TAG, "RESPONSE : " + response.body().toString());
//        //loadingLayout.setVisibility(View.GONE);
//        if (response.isSuccessful()) {
//            PagesObject pagesObject = response.body();
//            //setWebcastsLayout(webcasts.get(0));
//            //eventsListView.setHasFixedSize(true);
//
//            final ViewPager mViewPager = (ViewPager) contentView.findViewById(R.id.pager);
//            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//            mViewPager.setAdapter(mSectionsPagerAdapter);
//
//            final TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.tabLayout);
//            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
//            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tomato));
//            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.greyish_brown), ContextCompat.getColor(this, R.color.tomato));
//            tabLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    tabLayout.setupWithViewPager(mViewPager);
//                }
//            });
//            mViewPager.setOffscreenPageLimit(1);
//        }else{
//            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return TermsFragment.newInstance(position+1);
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
                    return "TÜRKÇE";//.toUpperCase(l);
                case 1:
                    return "ENGLISH";//.toUpperCase(l);
            }
            return null;
        }
    }
}