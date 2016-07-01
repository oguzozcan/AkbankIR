package com.akbank.investorrelations;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.adapters.RatingsGridViewAdapter;
import com.akbank.investorrelations.busevents.EventAboutTurkeyRequest;
import com.akbank.investorrelations.busevents.EventAboutTurkeyResponse;
import com.akbank.investorrelations.busevents.EventDashboardRequest;
import com.akbank.investorrelations.busevents.EventDashboardResponse;
import com.akbank.investorrelations.busevents.EventMainGraphRequest;
import com.akbank.investorrelations.busevents.EventMainGraphResponse;
import com.akbank.investorrelations.busevents.EventRatingRequest;
import com.akbank.investorrelations.busevents.EventRatingResponse;
import com.akbank.investorrelations.fragments.DownloadDialogFragment;
import com.akbank.investorrelations.objects.AboutTurkeyObject;
import com.akbank.investorrelations.objects.AnnualReportObject;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.CalendarEvent;
import com.akbank.investorrelations.objects.DashboardContainerObject;
import com.akbank.investorrelations.objects.InvestorPresentationObject;
import com.akbank.investorrelations.objects.MainGraphDot;
import com.akbank.investorrelations.objects.NewsObject;
import com.akbank.investorrelations.objects.Rating;
import com.akbank.investorrelations.objects.WebcastObject;
import com.akbank.investorrelations.services.GraphHelper;
import com.akbank.investorrelations.utils.Constants;
import com.akbank.investorrelations.utils.DataSaver;
import com.akbank.investorrelations.utils.TimeUtil;
import com.jjoe64.graphview.GraphView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Response;

//import android.support.v4.app.FragmentManager;

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
    //private boolean mTwoPane;
    private ViewPager mPager;
    private GraphHelper helper;
    //private LinearLayout calendarView;
    private RelativeLayout annualReportView;
    private RelativeLayout investorPresentationView;
    private RelativeLayout webcastView;
    private RelativeLayout aboutTurkeyView;
    private RecyclerView ratingsGridView;
    //private LinearLayout progressBarLayout;
    boolean stopSliding = false;
    private Runnable animateViewPager;
    private Handler handler;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    //View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_item_list, null, false);
        mContent.addView(contentView, 0);
        TimeUtil.changeLocale(getResources().getConfiguration().locale);
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        ratingsGridView = (RecyclerView) contentView.findViewById(R.id.ratingsGridView);
        ratingsGridView.setLayoutManager(lm);
        ratingsGridView.setNestedScrollingEnabled(true);
        //mDrawer.addView(contentView, 0);
        //setContentView(R.layout.activity_item_list);
        //
        annualReportView = (RelativeLayout) contentView.findViewById(R.id.annualReportLayout);
        webcastView = (RelativeLayout) contentView.findViewById(R.id.webcastLayout);
        aboutTurkeyView = (RelativeLayout) contentView.findViewById(R.id.aboutTurkeyLayout);
        investorPresentationView = (RelativeLayout) contentView.findViewById(R.id.latestPresentation);

        //progressBarLayout = (LinearLayout) contentView.findViewById(R.id.progressBarLayout);

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
        helper.setPopupPanel((RelativeLayout) findViewById(R.id.popupPanel));
        helper.setPopupDot((ImageView) findViewById(R.id.dot));

        //   View recyclerView = findViewById(R.id.item_list);
        //   assert recyclerView != null;
        //   setupRecyclerView((RecyclerView) recyclerView);
        TAG = "DashboardActivity";
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.

            // mTwoPane = true;
        }

        final DataSaver ds = app.getDataSaver();
        if (!ds.getBoolean("N_FIRST_ENTRANCE")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent termsIntent = new Intent(ItemListActivity.this, TermsActivity.class);
                    termsIntent.putExtra("automatic", true);
                    ItemListActivity.this.startActivity(termsIntent);
                    ds.putBoolean(Constants.FIRST_ENTRANCE_KEY, true);
                    ds.save();
                }
            }, 300);
        }
    }


    @Override
    protected void setTag() {
        TAG = "ItemListActivity";
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange("");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "ON NEW INTENT: loc : " + getResources().getConfiguration().locale.toString());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ItemListActivity.this.recreate();
            }
        }, 5);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "ON CONF CHANGED: loc" + getResources().getConfiguration().locale.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        app.getBus().register(this);
        Locale current = getResources().getConfiguration().locale;
        String lng = current.toString();
        Log.d(TAG, "CURRENT LOCALE terms fragment: " + lng);
        if (isLocaleTurkish(lng)) {
            ds.putString(Constants.SELECTED_LANGUAGE_KEY, Constants.TURKISH);
            ds.save();
        }
        drawMainGraph(app, TimeUtil.getDateBeforeOrAfterToday(-30, true, false), TimeUtil.getDateBeforeOrAfterToday(0, true, false), 1440, 30);
        app.getBus().post(new EventDashboardRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
        app.getBus().post(new EventRatingRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
        app.getBus().post(new EventAboutTurkeyRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
//        registerReceiver(receiver, new IntentFilter(
//                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static boolean isLocaleTurkish(String lng) {
        return lng.equalsIgnoreCase("tr_TR") || lng.equalsIgnoreCase(Constants.TURKISH_LOCALE_CODE) || lng.equalsIgnoreCase("tr");
    }

    @Override
    public void onStop() {
        super.onStop();
        app.getBus().unregister(this);
        //unregisterReceiver(receiver);
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
            //InvestorPresentationLayout
            setInvestorPresentationLayout(dashboardContainerObject);

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
            final ArrayList<Rating> ratings = response.body();
            Log.d(TAG, "RATINGs size: " + ratings.size());
            final ArrayList<String> ratingsArray = new ArrayList();
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

            RelativeLayout ratingsTitleLayout = (RelativeLayout) findViewById(R.id.ratingsTitleLayout); //contentView.
            ratingsTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ratings = new Intent(ItemListActivity.this, RatingsActivity.class);
                    ratings.putExtra("ratings", ratingsArray);
                    ItemListActivity.this.startActivity(ratings);
                }
            });


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
            helper.init(response.body(), event.getPeriod() == 1, event.getDaysBetween());
        } else {
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
        }
    }

    //    @Subscribe
//    public void onFileDownloaded(final EventFileDownloadResponse event) {
//        Response<ResponseBody> response = event.getResponse();
//        Log.d(TAG, "ON RESPONSE file download");
//        if (response.isSuccessful()) {
//            try {
//                Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw() + "bytes: " + response.body().bytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            app.getBus().post(new ApiErrorEvent(response.code(), response.message(), true));
//        }
//    }
    private void setEventDate(TextView tv, String date, boolean newLine) {

        DateTimeFormatter dtfApiFormat = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(AkbankApp.localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
        DateTimeFormatter dtfOutWOTime = DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(AkbankApp.localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));
        DateTimeFormatter dtfOutWeekday = DateTimeFormat.forPattern("EEEE").withLocale(AkbankApp.localeTr).withZone(DateTimeZone.forID("Europe/Istanbul"));

        Log.d(TAG, "SEt EVENT DATE LANG: " + AkbankApp.localeTr.toString());
        Locale locale = getResources().getConfiguration().locale;
        Log.d(TAG, "SEt EVENT DATE LANG locale: " + locale);
        String eventDate = TimeUtil.getDateTime(date, dtfApiFormat, dtfOutWOTime, locale);
        String weekDay = TimeUtil.getDateTime(date, dtfApiFormat, dtfOutWeekday, locale);
        Log.d(TAG, "EVENT DATE: " + eventDate + "- weekDay: " + weekDay);

        if (newLine) {
            tv.setText(new StringBuilder().append(eventDate).append("\n").append(weekDay).toString());
        } else {
            tv.setText(new StringBuilder().append(eventDate).append(" ").append(weekDay).toString());
        }

        tv.invalidate();
    }

    private void drawMainGraph(AkbankApp app, String startDate, String endDate, int period, int daysBetween) {
        if (helper != null) {
            helper.cleanGraph();
        }
        //loadingLayout.setVisibility(View.VISIBLE);
        Log.d(TAG, "START DATE: " + startDate + " - endDate: " + endDate);
        app.getBus().post(new EventMainGraphRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY), TimeUtil.getDateTxtForForex(startDate), TimeUtil.getDateTxtForForex(endDate), period, daysBetween));
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
        RelativeLayout investorPresentationLayout = (RelativeLayout) contentView.findViewById(R.id.investorPresentationTitleLayout);

        newsTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsIntent = new Intent(ItemListActivity.this, AnnouncementAndNewsActivity.class);
                ItemListActivity.this.startActivity(newsIntent);
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
                Intent annualReports = new Intent(ItemListActivity.this, AnnualReportsActivity.class);
                ItemListActivity.this.startActivity(annualReports);
            }
        });
        webCastsTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webcastsIntent = new Intent(ItemListActivity.this, WebcastsActivity.class);
                ItemListActivity.this.startActivity(webcastsIntent);
            }
        });
        aboutTurkeyTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAboutTurkey = new Intent(ItemListActivity.this, AboutTurkeyActivity.class);
                ItemListActivity.this.startActivity(intentAboutTurkey);
            }
        });
        investorPresentationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPresentation = new Intent(ItemListActivity.this, InvestorPresentationActivity.class);
                ItemListActivity.this.startActivity(intentPresentation);
            }
        });
        //TODO
        Locale current = getResources().getConfiguration().locale;
        String lng = current.toString();
        Log.d(TAG, "CURRENT LOCALE terms fragment: " + lng);
        if (isLocaleTurkish(lng)) {
            investorPresentationLayout.setVisibility(View.GONE);
            investorPresentationView.setVisibility(View.GONE);
        }
    }

    private void setCalendarLayout(DashboardContainerObject dashboardContainerObject) {
        ArrayList<CalendarEvent> calendarEvents = dashboardContainerObject.getCalendarEvents();
        LinearLayout calendarView = (LinearLayout) findViewById(R.id.calendarLayout); //contentView.

        final TextView descriptionTextView = (TextView) calendarView.findViewById(R.id.eventDescription);
        final TextView eventDateTextView = (TextView) calendarView.findViewById(R.id.eventDetailDate);
        eventDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCalendar = new Intent(ItemListActivity.this, CalendarActivity.class);
                ItemListActivity.this.startActivity(intentCalendar);
            }
        });
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

                descriptionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentCalendar = new Intent(ItemListActivity.this, CalendarActivity.class);
                        ItemListActivity.this.startActivity(intentCalendar);
                    }
                });

//                dateTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intentCalendar = new Intent(ItemListActivity.this, CalendarActivity.class);
//                        ItemListActivity.this.startActivity(intentCalendar);
//                    }
//                });
            }
        }
        calendarView.invalidate();
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
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", annualReportObject.getTitle());
                b.putString("url", annualReportObject.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Opening));
                //startFileDownload(annualReportObject.getPdfUrl(), annualReportObject.getTitle(), false);
            }
        });
        TextView viewButton = (TextView) annualReportView.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", annualReportObject.getTitle());
                b.putString("url", annualReportObject.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Downloading));
                //startFileDownload(annualReportObject.getPdfUrl(), annualReportObject.getTitle(), true);
            }
        });
    }


    private void setInvestorPresentationLayout(final DashboardContainerObject dashboardContainerObject) {
        final InvestorPresentationObject investorPresentationObject = dashboardContainerObject.getInvestorPresentationObject();
        TextView annualObjectName = (TextView) investorPresentationView.findViewById(R.id.reportDescription);
        TextView annualObjectDescription = (TextView) investorPresentationView.findViewById(R.id.reportDescription2);
        ImageView pdfIcon = (ImageView) investorPresentationView.findViewById(R.id.pdfIcon);
        annualObjectName.setText(investorPresentationObject.getTitle());
        annualObjectDescription.setText(investorPresentationObject.getDescription());
        annualObjectDescription.setVisibility(View.VISIBLE);
        if (investorPresentationObject.getDescription().equals("")) {
            annualObjectDescription.setVisibility(View.INVISIBLE);
        } else {
            Spanned htmlAsSpanned = Html.fromHtml(investorPresentationObject.getDescription());
            annualObjectDescription.setText(htmlAsSpanned);
        }
        Picasso.with(this).load(AkbankApp.ROOT_URL + investorPresentationObject.getImage()).placeholder(R.drawable.group_copy_4).into(pdfIcon);
        TextView saveButton = (TextView) investorPresentationView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app.getBus().post(new EventFileDownloadRequest(ItemListActivity.this, AkbankApp.ROOT_URL_1, annualReportObject.getPdfUrl()));
                Log.d(TAG, "Download STARTED annualReport");
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", investorPresentationObject.getTitle());
                b.putString("url", investorPresentationObject.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Opening));
                //startFileDownload(annualReportObject.getPdfUrl(), annualReportObject.getTitle(), false);
            }
        });
        TextView viewButton = (TextView) investorPresentationView.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", investorPresentationObject.getTitle());
                b.putString("url", investorPresentationObject.getPdfUrl());
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Downloading));
                //startFileDownload(annualReportObject.getPdfUrl(), annualReportObject.getTitle(), true);
            }
        });
    }

    //    private void setReportObject(final ReportObject reportObject ) {
//        RelativeLayout webcastLayout = (RelativeLayout) contentView.findViewById(R.id.latestReport);
//        TextView description = (TextView) webcastLayout.findViewById(R.id.reportDescription);
//        TextView description2 = (TextView) webcastLayout.findViewById(R.id.reportDescription2);
//        description.setText(reportObject.getTitle());
//        if(reportObject.getDescription().isEmpty()){
//            description2.setVisibility(View.INVISIBLE);
//        }else{
//            Spanned htmlAsSpanned = Html.fromHtml(reportObject.getDescription());
//            description2.setText(htmlAsSpanned);
//        }
//        TextView saveButton = (TextView) webcastLayout.findViewById(R.id.saveButton);
//        TextView viewButton = (TextView) webcastLayout.findViewById(R.id.viewButton);
//        ImageView pdfIcon = (ImageView) webcastLayout.findViewById(R.id.pdfIcon);
//
//        Picasso.with(this).load(AkbankApp.ROOT_URL + reportObject.getImage()).placeholder(R.drawable.group_copy_4).into(pdfIcon);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
//                DownloadDialogFragment newFragment = new DownloadDialogFragment();
//                Bundle b = new Bundle();
//                b.putString("title", reportObject.getTitle());
//                b.putString("url", reportObject.getPdfUrl());
//                b.putBoolean("shouldShowAfterDownload", false);
//                newFragment.setArguments(b);
//                newFragment.show(fm, getString(R.string.Downloading));
//            }
//        });
//
//        viewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
//                DownloadDialogFragment newFragment = new DownloadDialogFragment();
//                Bundle b = new Bundle();
//                b.putString("title", reportObject.getTitle());
//                b.putString("url", reportObject.getPdfUrl());
//                b.putBoolean("shouldShowAfterDownload", true);
//                newFragment.setArguments(b);
//                newFragment.show(fm, getString(R.string.Opening));
//            }
//        });
//    }
    private void setAboutTurkeyLayout(final AboutTurkeyObject aboutTurkeyObject) {
        TextView aboutTurkeyPdfName = (TextView) aboutTurkeyView.findViewById(R.id.aboutTurkeyDescription);
        aboutTurkeyPdfName.setText(aboutTurkeyObject.getPdfTitle());
        TextView saveButton = (TextView) aboutTurkeyView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app.getBus().post(new EventFileDownloadRequest(ItemListActivity.this,AkbankApp.ROOT_URL_1, aboutTurkeyObject.getPdf()));
                Log.d(TAG, "Download STARTED about Turkey");
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", aboutTurkeyObject.getTitle());
                b.putString("url", aboutTurkeyObject.getPdf());
                b.putBoolean("shouldShowAfterDownload", false);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Downloading));
                //startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), false);
            }
        });
        TextView viewButton = (TextView) aboutTurkeyView.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Download STARTED about Turkey");
                FragmentManager fm = getFragmentManager();//getSupportFragmentManager();
                DownloadDialogFragment newFragment = new DownloadDialogFragment();
                Bundle b = new Bundle();
                b.putString("title", aboutTurkeyObject.getTitle());
                b.putString("url", aboutTurkeyObject.getPdf());
                b.putBoolean("shouldShowAfterDownload", true);
                newFragment.setArguments(b);
                newFragment.show(fm, getString(R.string.Opening));
                //startFileDownload(aboutTurkeyObject.getPdf(), aboutTurkeyObject.getTitle(), true);
            }
        });
    }


    private void setWebcastsLayout(DashboardContainerObject dashboardContainerObject) {
        final WebcastObject webcastObject = dashboardContainerObject.getWebcastObject();
        TextView webcastTitle = (TextView) webcastView.findViewById(R.id.webcastsTitle);
        webcastTitle.setText(webcastObject.getTitle());
        TextView listenButton = (TextView) webcastView.findViewById(R.id.listenButton);
        TextView dateText = (TextView) webcastView.findViewById(R.id.dateText);
        String date = TimeUtil.getDateTime(webcastObject.getDate(), TimeUtil.dfISO, TimeUtil.dtfOutWOTimeShort,AkbankApp.localeTr);
        //String date = TimeUtil.getDateTime(webcastObject.getDate(), TimeUtil.dtfForex, TimeUtil.dtfOutWOTimeShort);
        dateText.setText(date);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemListActivity.this, WebcastPlayerActivity.class);
                intent.putExtra("name", webcastObject.getTitle());
                intent.putExtra("date", webcastObject.getDate());
                intent.putExtra("postfix", webcastObject.getAudioUrl());
                ItemListActivity.this.startActivity(intent);
//                Intent webcasts = new Intent(ItemListActivity.this, WebcastsActivity.class);
//                ItemListActivity.this.startActivity(webcasts);
            }
        });
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<NewsObject> newsObjectArrayList;

        public CustomPagerAdapter(Context context, ArrayList<NewsObject> newsObjectArrayList) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
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
            final NewsObject newsObject = newsObjectArrayList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            TextView textView = (TextView) itemView.findViewById(R.id.textView);
            //imageView.setImageResource();
            textView.setText(newsObject.getTitle());
            Picasso.with(getApplicationContext()).load(AkbankApp.ROOT_URL + newsObjectArrayList.get(position).getImage()).placeholder(R.drawable.page_1).into(imageView);

            container.addView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ItemListActivity.this, NewsDetailActivity.class);
                    intent.putExtra("title", newsObject.getTitle());
                    intent.putExtra("description", newsObject.getText());
                    ItemListActivity.this.startActivity(intent);
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
