package com.mallardduckapps.akbankir;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.mallardduckapps.akbankir.adapters.RatingsGridViewAdapter;
import com.mallardduckapps.akbankir.busevents.EventAboutTurkeyRequest;
import com.mallardduckapps.akbankir.busevents.EventAboutTurkeyResponse;
import com.mallardduckapps.akbankir.busevents.EventDashboardRequest;
import com.mallardduckapps.akbankir.busevents.EventDashboardResponse;
import com.mallardduckapps.akbankir.busevents.EventFileDownloadRequest;
import com.mallardduckapps.akbankir.busevents.EventFileDownloadResponse;
import com.mallardduckapps.akbankir.busevents.EventMainGraphRequest;
import com.mallardduckapps.akbankir.busevents.EventMainGraphResponse;
import com.mallardduckapps.akbankir.busevents.EventRatingRequest;
import com.mallardduckapps.akbankir.busevents.EventRatingResponse;
import com.mallardduckapps.akbankir.objects.AboutTurkeyObject;
import com.mallardduckapps.akbankir.objects.AnnualReportObject;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.CalendarEvent;
import com.mallardduckapps.akbankir.objects.DashboardContainerObject;
import com.mallardduckapps.akbankir.objects.MainGraphDot;
import com.mallardduckapps.akbankir.objects.NewsObject;
import com.mallardduckapps.akbankir.objects.Rating;
import com.mallardduckapps.akbankir.objects.WebcastObject;
import com.mallardduckapps.akbankir.services.GraphHelper;
import com.mallardduckapps.akbankir.utils.TimeUtil;
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends BaseActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ViewPager mPager;
    private GraphHelper helper;
    private LinearLayout calendarView;
    private RelativeLayout annualReportView;
    private RelativeLayout webcastView;
    private RelativeLayout aboutTurkeyView;
    private RecyclerView ratingsGridView;
    private LinearLayout progressBarLayout;
    boolean stopSliding = false;
    private Runnable animateViewPager;
    private Handler handler;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "DashboardActivity";
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_item_list, null, false);
        mContent.addView(contentView, 0);
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        ratingsGridView = (RecyclerView) contentView.findViewById(R.id.ratingsGridView);
        ratingsGridView.setLayoutManager(lm);
        ratingsGridView.setNestedScrollingEnabled(true);
        //mDrawer.addView(contentView, 0);
        //setContentView(R.layout.activity_item_list);
        calendarView = (LinearLayout) contentView.findViewById(R.id.calendarLayout);
        annualReportView = (RelativeLayout) contentView.findViewById(R.id.annualReportLayout);
        webcastView = (RelativeLayout) contentView.findViewById(R.id.webcastLayout);
        aboutTurkeyView = (RelativeLayout) contentView.findViewById(R.id.aboutTurkeyLayout);
        progressBarLayout = (LinearLayout) contentView.findViewById(R.id.progressBarLayout);

        setNavigationFromTitles(contentView);

        mPager = (ViewPager) contentView.findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.akbank_action_bar_logo);

        //toolbar.setTitle(getTitle());
        GraphView graphView = (GraphView) contentView.findViewById(R.id.graph);
        helper = new GraphHelper(this, graphView, null);
        //   View recyclerView = findViewById(R.id.item_list);
        //   assert recyclerView != null;
        //   setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void setTag() {
        TAG = "ItemListActivity";
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        //TODO these dates need to be fixed
        drawMainGraph(app, TimeUtil.getDateBeforeOrAfterToday(-1, true, false), TimeUtil.getDateBeforeOrAfterToday(0, true, false));
        app.getBus().post(new EventDashboardRequest());
        app.getBus().post(new EventRatingRequest());
        app.getBus().post(new EventAboutTurkeyRequest());
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
        unregisterReceiver(receiver);
    }

    @Subscribe
    public void onLoadDashboardItems(final EventDashboardResponse event) {
        Response<DashboardContainerObject> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            DashboardContainerObject dashboardContainerObject = response.body();
            //News layout
            final ArrayList<NewsObject> newsObjects = dashboardContainerObject.getNewsObjectArrayList();
            mPager.setAdapter(new CustomPagerAdapter(getApplicationContext(), newsObjects));
            mPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_CANCEL:
                            break;

                        case MotionEvent.ACTION_UP:
                            // calls when touch release on ViewPager
                            if (newsObjects != null && newsObjects.size() != 0) {
                                stopSliding = false;
                                runnable(newsObjects.size());
                                handler.postDelayed(animateViewPager,
                                        ANIM_VIEWPAGER_DELAY_USER_VIEW);
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:
                            // calls when ViewPager touch
                            if (handler != null && !stopSliding) {
                                stopSliding = true;
                                handler.removeCallbacks(animateViewPager);
                            }
                            break;
                    }
                    return false;
                }
            });
            if (newsObjects != null) {
                runnable(newsObjects.size());
                handler.postDelayed(animateViewPager,
                        ANIM_VIEWPAGER_DELAY_USER_VIEW);
            }

            //CalendarLayout
            setCalendarLayout(dashboardContainerObject);
            //AnnualReport Layout
            setAnnualReportLayout(dashboardContainerObject);
            //Webcasts Layout
            setWebcastsLayout(dashboardContainerObject);

        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }


    @Subscribe
    public void onLoadRatings(final EventRatingResponse event) {
        Response<ArrayList<Rating>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE ratings- responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE ratings: " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            ArrayList<Rating> ratings = response.body();
            Log.d(TAG, "RATINGs size: " + ratings.size());
            ArrayList<String> ratingsArray = new ArrayList();
            ratingsArray.add("");
            ratingsArray.add("Moody's");
            ratingsArray.add("Fitch");
            for (int i = 0; i < ratings.size(); i++) {
                //Log.d(TAG, "RATING: " + ratings.get(i).getTitle());
                ratingsArray.add(ratings.get(i).getTitle());
                ratingsArray.add(ratings.get(i).getMoody());
                ratingsArray.add(ratings.get(i).getFitch());
            }
            //StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
            GridLayoutManager lm = new GridLayoutManager(this, 3);
            lm.setSpanCount(3);
            ratingsGridView.setLayoutManager(lm);
            ratingsGridView.setAdapter(new RatingsGridViewAdapter(this, ratingsArray));

        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }

    @Subscribe
    public void onLoadAboutTurkey(final EventAboutTurkeyResponse event) {
        Response<AboutTurkeyObject> response = event.getAboutTurkeyObject();
        Log.d(TAG, "ON RESPONSE aboutTurkey- responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE aboutTurkey: " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            AboutTurkeyObject aboutTurkeyObject = response.body();
            setAboutTurkeyLayout(aboutTurkeyObject);

        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }

    @Subscribe
    public void onLoadMainGraphData(final EventMainGraphResponse event) {
        Response<ArrayList<MainGraphDot>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            //ArrayList<MainGraphDot> graphDots = response.body();
            helper.init(response.body());
        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }

    @Subscribe
    public void onFileDownloaded(final EventFileDownloadResponse event) {
        Response<ResponseBody> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE file download");
        if (response.isSuccessful()) {
            try {
                Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw() + "bytes: " + response.body().bytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }

    private void setEventDate(TextView tv, String date, boolean newLine) {
        String eventDate = TimeUtil.getDateTime(date, TimeUtil.dtfApiFormat, TimeUtil.dtfOutWOTime);
        String weekDay = TimeUtil.getDateTime(date, TimeUtil.dtfApiFormat, TimeUtil.dtfOutWeekday);
        if (newLine) {
            tv.setText(eventDate + "\n" + weekDay);
        } else {
            tv.setText(eventDate + " " + weekDay);
        }

    }

    private void drawMainGraph(AkbankApp app, String startDate, String endDate) {
        if (helper != null) {
            helper.cleanGraph();
        }
        //loadingLayout.setVisibility(View.VISIBLE);
        Log.d(TAG, "START DATE: " + startDate + " - endDate: " + endDate);
        app.getBus().post(new EventMainGraphRequest(TimeUtil.getDateTxtForForex(startDate), TimeUtil.getDateTxtForForex(endDate), 60));
        //mainGraphRestApi.getMainGraphData(period, startDate, endDate).enqueue(ItemDetailFragment.this);
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mPager.getCurrentItem() == size - 1) {
                        mPager.setCurrentItem(0);
                    } else {
                        mPager.setCurrentItem(
                                mPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    @Override
    public void onPause() {
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
        super.onPause();
    }

/*
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
//                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
//                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }*/

    private void setNavigationFromTitles(View contentView) {
        RelativeLayout newsTitleLayout = (RelativeLayout) contentView.findViewById(R.id.newsTitleLayout);
        RelativeLayout stocksTitleLayout = (RelativeLayout) contentView.findViewById(R.id.stocksTitleLayout);
        RelativeLayout calendarTitleLayout = (RelativeLayout) contentView.findViewById(R.id.calendarTitleLayout);
        RelativeLayout annualReportsTitleLayout = (RelativeLayout) contentView.findViewById(R.id.annualReportsTitleLayout);
        RelativeLayout webCastsTitleLayout = (RelativeLayout) contentView.findViewById(R.id.webcastsTitleLayout);
        RelativeLayout aboutTurkeyTitleLayout = (RelativeLayout) contentView.findViewById(R.id.aboutTurkeyTitleLayout);
        RelativeLayout ratingsTitleLayout = (RelativeLayout) contentView.findViewById(R.id.ratingsTitleLayout);
        newsTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        stocksTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCalendar = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                ItemListActivity.this.startActivity(intentCalendar);
            }
        });
        calendarTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCalendar = new Intent(ItemListActivity.this, CalendarActivity.class);
                ItemListActivity.this.startActivity(intentCalendar);

            }
        });
        annualReportsTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        webCastsTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        aboutTurkeyTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ratingsTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setCalendarLayout(DashboardContainerObject dashboardContainerObject) {
        ArrayList<CalendarEvent> calendarEvents = dashboardContainerObject.getCalendarEvents();
        final TextView descriptionTextView = (TextView) calendarView.findViewById(R.id.eventDescription);
        final TextView eventDateTextView = (TextView) calendarView.findViewById(R.id.eventDetailDate);

        for (int i = 0; i < calendarEvents.size(); i++) {
            final CalendarEvent ce = calendarEvents.get(i);
            Log.d(TAG, "CALENDAR EVENT: " + ce.getEventDate() + " - ce.type: " + ce.getType() + "-desc: " + ce.getDescription());
            TextView dateTextView = null;
            RelativeLayout calendarItem = null;
            switch (i) {
                case 0:
                    calendarItem = (RelativeLayout) calendarView.findViewById(R.id.calendarItem1);
                    calendarItem.setBackgroundResource(R.drawable.calendar_ircalendar_background);
                    dateTextView = (TextView) calendarItem.findViewById(R.id.dateTextView1);
                    descriptionTextView.setText(ce.getDescription());
                    setEventDate(eventDateTextView, ce.getEventDate(), false);
                    break;
                case 1:
                    calendarItem = (RelativeLayout) calendarView.findViewById(R.id.calendarItem2);
                    dateTextView = (TextView) calendarItem.findViewById(R.id.dateTextView2);
                    break;
                case 2:
                    calendarItem = (RelativeLayout) calendarView.findViewById(R.id.calendarItem3);
                    dateTextView = (TextView) calendarItem.findViewById(R.id.dateTextView3);
                    break;
            }
            if (dateTextView != null) {
                setEventDate(dateTextView, ce.getEventDate(), true);
                final LinearLayout itemsLayout = (LinearLayout) calendarView.findViewById(R.id.calendarItemsLayout);
                calendarItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        descriptionTextView.setText(ce.getDescription());
                        setEventDate(eventDateTextView, ce.getEventDate(), false);
                        for (int i = 0; i < itemsLayout.getChildCount(); i++) {
                            View v = itemsLayout.getChildAt(i);
                            if (v instanceof RelativeLayout) {
                                v.setBackgroundResource(R.drawable.calendar_major_events_background);
                            }
                        }
                        view.setBackgroundResource(R.drawable.calendar_ircalendar_background);
                    }
                });
            }
        }
    }

    private void setAnnualReportLayout(final DashboardContainerObject dashboardContainerObject) {
        final AnnualReportObject annualReportObject = dashboardContainerObject.getAnnualReportObject();
        TextView annualObjectName = (TextView) annualReportView.findViewById(R.id.annualReportDescription);
        TextView annualObjectDescription = (TextView) annualReportView.findViewById(R.id.descriptionSubtitle);
        annualObjectName.setText(annualReportObject.getTitle());
        annualObjectDescription.setText(annualReportObject.getDescription());
        annualObjectDescription.setVisibility(View.VISIBLE);
        if (annualReportObject.getDescription().equals("")) {
            annualObjectDescription.setVisibility(View.GONE);
        }

        TextView saveButton = (TextView) annualReportView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app.getBus().post(new EventFileDownloadRequest(ItemListActivity.this, AkbankApp.ROOT_URL_1, annualReportObject.getPdfUrl()));
                Log.d(TAG, "Download STARTED annualReport");
                startFileDownload(annualReportObject.getPdfUrl(), annualReportObject.getTitle(), false);
            }
        });
        TextView viewButton = (TextView) annualReportView.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ItemListActivity.this, WebActivity.class);
//                intent.putExtra("file_name", annualReportObject.getPdfUrl());
//                intent.putExtra("title", annualReportObject.getTitle());
//                intent.putExtra("type", "web");
//                ItemListActivity.this.startActivity(intent);
                startFileDownload(annualReportObject.getPdfUrl(), annualReportObject.getTitle(), true);
            }
        });
    }

    private void startFileDownload(String fileName, String title, boolean shouldShownAfterDownload) {
        progressBarLayout.setVisibility(View.VISIBLE);
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/akbank_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        DownloadManager dm = (DownloadManager) getSystemService(BaseActivity.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse((AkbankApp.ROOT_URL_1 + fileName))); //"http://www.vogella.de/img/lars/LarsVogelArticle7.png"
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(title)
                .setDescription(shouldShownAfterDownload ? "view" : "download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir("/akbank_files", title)
                .allowScanningByMediaScanner();
        long enqueue = dm.enqueue(request);
    }

    private void setAboutTurkeyLayout(final AboutTurkeyObject aboutTurkeyObject) {
//         annualReportObject = dashboardContainerObject.get;
        TextView aboutTurkeyPdfName = (TextView) aboutTurkeyView.findViewById(R.id.aboutTurkeyDescription);
        aboutTurkeyPdfName.setText(aboutTurkeyObject.getPdfTitle());

        TextView saveButton = (TextView) aboutTurkeyView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app.getBus().post(new EventFileDownloadRequest(ItemListActivity.this,AkbankApp.ROOT_URL_1, aboutTurkeyObject.getPdf()));
                Log.d(TAG, "Download STARTED about Turkey");
                startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), false);


            }
        });
        TextView viewButton = (TextView) aboutTurkeyView.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ItemListActivity.this, WebActivity.class);
//                intent.putExtra("file_name", aboutTurkeyObject.getPdf());
//                intent.putExtra("title", aboutTurkeyObject.getTitle());
//                intent.putExtra("type", "web");
//                ItemListActivity.this.startActivity(intent);
                startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), true);
            }
        });
    }


    private void setWebcastsLayout(DashboardContainerObject dashboardContainerObject) {
        WebcastObject webcastObject = dashboardContainerObject.getWebcastObject();
        TextView webcastTitle = (TextView) webcastView.findViewById(R.id.webcastsTitle);
        webcastTitle.setText(webcastObject.getTitle());
        TextView listenButton = (TextView) webcastView.findViewById(R.id.listenButton);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<NewsObject> newsObjectArrayList;

        public CustomPagerAdapter(Context context, ArrayList<NewsObject> newsObjectArrayList) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.newsObjectArrayList = newsObjectArrayList;
        }

        @Override
        public int getCount() {
            return newsObjectArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.news_pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            TextView textView = (TextView) itemView.findViewById(R.id.textView);
            //imageView.setImageResource();
            textView.setText(newsObjectArrayList.get(position).getTitle());
            Picasso.with(getApplicationContext()).load(AkbankApp.ROOT_URL + newsObjectArrayList.get(position).getImage()).placeholder(R.drawable.page_1).into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.d(TAG, "Download Completed");
                progressBarLayout.setVisibility(View.GONE);
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                Log.d(TAG, "Download Completed id: " + downloadId);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = ((DownloadManager) getSystemService(BaseActivity.DOWNLOAD_SERVICE)).query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        String description = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                        String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        //Log.d(TAG, "DOWNLOAD COMPLETED: " + uriString);
                        Log.d(TAG, "DOWNLOAD COMPLETED: " + Uri.parse(uriString));
                        Log.d(TAG, "DOWNLOAD DESCRIPTION: " + description);
                        if(description.equals("view")){
                            Intent intentPdf = new Intent(ItemListActivity.this, WebActivity.class);
                            intentPdf.putExtra("uri", uriString);
                            intentPdf.putExtra("title", title);
                            intentPdf.putExtra("type", "pdf");
                            ItemListActivity.this.startActivity(intentPdf);
                        }else{
                            Log.d(TAG, "ONLY DOWNLOAD: " + description);
                        }

                    }
                }
            }
        }
    };

}