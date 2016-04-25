package com.mallardduckapps.akbankir.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.jjoe64.graphview.GraphView;
import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.ItemDetailActivity;
import com.mallardduckapps.akbankir.ItemListActivity;
import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.adapters.SnapshotGridAdapter;
import com.mallardduckapps.akbankir.busevents.EventComparableGraphRequest;
import com.mallardduckapps.akbankir.busevents.EventComparableGraphResponse;
import com.mallardduckapps.akbankir.busevents.EventMainGraphRequest;
import com.mallardduckapps.akbankir.busevents.EventMainGraphResponse;
import com.mallardduckapps.akbankir.busevents.EventSnapshotRequest;
import com.mallardduckapps.akbankir.busevents.EventSnapshotResponse;
import com.mallardduckapps.akbankir.components.CompareButton;
import com.mallardduckapps.akbankir.components.DateTextView;
import com.mallardduckapps.akbankir.components.IntervalButton;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.ComparableStockData;
import com.mallardduckapps.akbankir.objects.MainGraphDot;
import com.mallardduckapps.akbankir.objects.SnapshotData;
import com.mallardduckapps.akbankir.services.GraphHelper;
import com.mallardduckapps.akbankir.utils.Constants;
import com.mallardduckapps.akbankir.utils.TimeUtil;
import com.squareup.otto.Subscribe;
import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Response;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends BaseFragment {

    //public static final String ARG_ITEM_ID = "item_id";
    GraphHelper helper;
    RelativeLayout loadingLayout;
    int period = 60;
    ScrollView sc;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
//            }
//        }
    }

    @Override
    protected void setTag() {
        TAG = "Stock";
    }


    private void initIntervalLayout(View rootView, final DateTextView sDateView, final DateTextView eDateView) {
        final ArrayList<IntervalButton> buttons = new ArrayList<>(5);
        final IntervalButton buttonInterday = (IntervalButton) rootView.findViewById(R.id.interdayButton);
        final IntervalButton buttonMonth = (IntervalButton) rootView.findViewById(R.id.oneMonthButton);
        final IntervalButton buttonThreeMonths = (IntervalButton) rootView.findViewById(R.id.threeMonthButton);
        final IntervalButton buttonSixMonths = (IntervalButton) rootView.findViewById(R.id.sixMonthButton);
        final IntervalButton buttonYear = (IntervalButton) rootView.findViewById(R.id.oneYearButton);
        View.OnClickListener intervalClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof IntervalButton) {
                    IntervalButton button = (IntervalButton) view;
                    for (IntervalButton b : buttons) {
                        if (button.equals(b)) {
                            b.setButtonSelected(true);
                            period = b.getPeriod();
                            String dateBefore;
                            switch (b.getInterval()) {
                                case 2:
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-30, true, false);
                                    break;
                                case 3:
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-90, true, false);
                                    break;
                                case 4:
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-180, true, false);
                                    break;
                                case 5:
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-365, true, false);
                                    break;
                                case 1:
                                default:
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-1, true, false);
                                    break;
                            }
                            sDateView.getTextView().setText(dateBefore);
                        } else {
                            b.setButtonSelected(false);
                        }
                    }
                }
            }
        };
        buttons.add(buttonInterday);
        buttons.add(buttonMonth);
        buttons.add(buttonThreeMonths);
        buttons.add(buttonSixMonths);
        buttons.add(buttonYear);
        for (IntervalButton button : buttons) {
            button.setOnClickListener(intervalClickListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stock_market_layout, container, false);
        GraphView graphView = (GraphView) rootView.findViewById(R.id.graph);
        GraphView barGraphView = (GraphView) rootView.findViewById(R.id.barGraph);
        loadingLayout = (RelativeLayout) rootView.findViewById(R.id.loadingLayout);
        final ArrayList<String> comparables = new ArrayList<>();
        comparables.add(Constants.AKBANK);
        final AkbankApp app = (AkbankApp) (getActivity().getApplication());

        helper = new GraphHelper(getActivity(), graphView, barGraphView);
        helper.setPopupPanel((LinearLayout) rootView.findViewById(R.id.popupPanel));
        //send request imm. to draw main graph
        final GridView snapshotGridView = (GridView) rootView.findViewById(R.id.snapshotGridView);
        CompareButton bist30Button = (CompareButton) rootView.findViewById(R.id.bist30Button);
        CompareButton bistBankaButton = (CompareButton) rootView.findViewById(R.id.bistBankaButton);
        CompareButton usdButton = (CompareButton) rootView.findViewById(R.id.usdButton);
        CompareButton eurButton = (CompareButton) rootView.findViewById(R.id.eurButton);
        final DateTextView sDateView = (DateTextView) rootView.findViewById(R.id.startingDateView);
        final DateTextView eDateView = (DateTextView) rootView.findViewById(R.id.endingDateView);
        sDateView.setActivity(getActivity());
        eDateView.setActivity(getActivity());
        eDateView.getTextView().addTextChangedListener(new DateViewTextWatcher(app, sDateView, eDateView, comparables));
        sDateView.getTextView().addTextChangedListener(new DateViewTextWatcher(app, sDateView, eDateView, comparables));
        reAdjustDate(sDateView.getDateText(), sDateView, eDateView);
        initIntervalLayout(rootView, sDateView, eDateView);
        drawMainGraph(app, sDateView.getFormatConvertedDateText(), eDateView.getFormatConvertedDateText(),snapshotGridView, period);
        //getSnapShotData(app, snapshotGridView);
        sc = (ScrollView) rootView.findViewById(R.id.scrollView);
        sc.scrollTo(0,0);
        View.OnClickListener compareButtonListener = new CompareButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof CompareButton) {
                    CompareButton button = (CompareButton) view;
                    button.reverseSelection();
                    if (button.isButtonSelected()) {
                        comparables.add(button.tag);
                    } else {
                        comparables.remove(button.tag);
                    }
                    if(comparables.size() <= 1){
                        drawMainGraph(app, sDateView.getFormatConvertedDateText(), eDateView.getFormatConvertedDateText(),snapshotGridView, period);
                    }else{
                        drawComparableGraph(app, comparables, sDateView.getFormatConvertedDateText(), eDateView.getFormatConvertedDateText(), period);
                    }
                }
            }
        };

        bist30Button.setOnClickListener(compareButtonListener);
        bistBankaButton.setOnClickListener(compareButtonListener);
        usdButton.setOnClickListener(compareButtonListener);
        eurButton.setOnClickListener(compareButtonListener);
        //useLoader();
        if(mListener != null)
            mListener.onTitleTextChange(getString(R.string.Menu_Stocks));
        return rootView;
    }

    private void drawMainGraph(AkbankApp app, String startDate, String endDate, GridView gridView, int period) {
        helper.cleanGraph();
        helper.cleanBarGraph();
        loadingLayout.setVisibility(View.VISIBLE);

        Log.d(TAG, "START DATE: " + startDate + " - endDate: " + endDate + " - period: " + period);
        if(gridView != null){
            app.getBus().post(new EventSnapshotRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY),gridView));
        }
        app.getBus().post(new EventMainGraphRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY),startDate, endDate, period));
        //mainGraphRestApi.getMainGraphData(period, startDate, endDate).enqueue(ItemDetailFragment.this);
    }

    private void drawComparableGraph(AkbankApp app, ArrayList<String> comparables, String startDate, String endDate, int period) {
        app.getBus().post(new EventComparableGraphRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY),comparables,startDate, endDate, period));
    }

    @Subscribe
    public void onLoadMainGraphData(final EventMainGraphResponse event) {
        Response<ArrayList<MainGraphDot>> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            //ArrayList<MainGraphDot> graphDots = response.body();
            helper.init(response.body());
            sc.scrollTo(0, 0);
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    @Subscribe
    public void onLoadComparableGraphData(final EventComparableGraphResponse event){
        Response<ComparableStockData> response = event.getResponse();
        if (response.isSuccessful()) {
            helper.cleanGraph();
            ComparableStockData stockData = response.body();
            helper.setComparableGraph(stockData);
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    @Subscribe
    public void onLoadSnapshotData(final EventSnapshotResponse event) {
        Response<ArrayList <SnapshotData>> response = event.getResponse();
        if(response.isSuccessful()){
            ArrayList<SnapshotData> snapshotDataList = response.body();
            SnapshotData snapshotData = snapshotDataList.get(0);
            //NumberFormat nf = NumberFormat.getCurrencyInstance(TimeUtil.localeTr);
            DecimalFormat volumeFormatter = new DecimalFormat("#,###,###");
            DecimalFormat capitalFormatter = new DecimalFormat("#,###");
            ArrayList<SnapshotGridAdapter.SnapShotItem> items = new ArrayList<>();
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_StockName), snapshotData.getName()));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_Last), Double.toString(snapshotData.getLast()) + " TL"));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_Change), (Double.toString(snapshotData.getDailyChangePercentage())+ " %")));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_Volume), volumeFormatter.format(snapshotData.getDailyVolume().longValue() / 1000) + " MiO TL"));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_HighestLowest), snapshotData.getDailyHighest() + " - " + snapshotData.getDailyLowest()));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_MarketCapital), capitalFormatter.format(snapshotData.getMarketCapital().longValue() / 100000) + " MiO TL"));
            SnapshotGridAdapter adapter = new SnapshotGridAdapter(getContext(), items);
            event.getSnapShotGridView().setAdapter(adapter);
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    public void reAdjustDate(String dateText, DateTextView startDate, DateTextView endDate) {
        int dateBetween = 0;
        DateTime dateTime = TimeUtil.getDateTime(dateText, true, false);
        if (dateText.equals(startDate.getDateText())) {
            dateBetween = TimeUtil.getDaysInBetween(dateTime, endDate.getDateTime());
            Log.d(TAG, "DAYS IN BETWEEN start:" + dateBetween);
            if (dateBetween == 1) {
                if (TimeUtil.isItWeekend(dateText) && TimeUtil.isItWeekend(endDate.getDateText())) {
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -1, true, false));
                }
            } else if (dateBetween == 0) {
                if (TimeUtil.getDayOfWeek(dateText) == 6) {
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -1, true, false));
                } else if (TimeUtil.getDayOfWeek(dateText) == 7) {
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -2, true, false));
                } else {
                    endDate.setDateText(TimeUtil.getDateBeforeOrAfter(endDate.getDateTime(), 1, true, false));
                }
            }

        } else if (dateText.equals(endDate.getDateText())) {
            //DateTime startDateTime = TimeUtil.getDateTime(endDate.getDateText(), true, false);
            dateBetween = TimeUtil.getDaysInBetween(startDate.getDateTime(), dateTime);
            Log.d(TAG, "DAYS IN BETWEEN end:" + dateBetween);
            if (dateBetween == 1) {
                if (TimeUtil.isItWeekend(startDate.getDateText()) && TimeUtil.isItWeekend(dateText)) {
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -1, true, false));
                }
            } else if (dateBetween == 0) {
                if (TimeUtil.getDayOfWeek(dateText) == 6) {
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -1, true, false));
                } else if (TimeUtil.getDayOfWeek(dateText) == 7) {
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -2, true, false));
                } else {
                    endDate.setDateText(TimeUtil.getDateBeforeOrAfter(endDate.getDateTime(), 1, true, false));
                }
            }
        }
    }

    public class DateViewTextWatcher implements TextWatcher {
        private final AkbankApp app;
        private final DateTextView startDate;
        private final DateTextView endDate;
        private final ArrayList<String> comparables;

        public DateViewTextWatcher(AkbankApp app, DateTextView startDate, DateTextView endDate, ArrayList<String> comparables) {
            this.app = app;
            this.startDate = startDate;
            this.endDate = endDate;
            this.comparables = comparables;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String dateText = editable.toString();
            Log.d(TAG, "TEXT: " + dateText);
            reAdjustDate(dateText, startDate, endDate);
            if (comparables.size() <= 1) {
                Log.d(TAG, "AFTER TEXT CHANGED: refresh main graph: true : period: " + period);
                //helper.cleanGraph();
                drawMainGraph(app, startDate.getFormatConvertedDateText(), endDate.getFormatConvertedDateText(), null, period);
            } else {
                Log.d(TAG, "AFTER TEXT CHANGED: refresh main graph: false: period: " + period);
                drawComparableGraph(app, comparables, startDate.getFormatConvertedDateText(), endDate.getFormatConvertedDateText(), period);
            }
        }
    }
}
