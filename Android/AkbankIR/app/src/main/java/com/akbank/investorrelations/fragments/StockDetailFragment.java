package com.akbank.investorrelations.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.akbank.investorrelations.AkbankApp;
import com.akbank.investorrelations.ItemDetailActivity;
import com.akbank.investorrelations.ItemListActivity;
import com.akbank.investorrelations.R;
import com.akbank.investorrelations.adapters.SnapshotGridAdapter;
import com.akbank.investorrelations.busevents.EventComparableGraphRequest;
import com.akbank.investorrelations.busevents.EventComparableGraphResponse;
import com.akbank.investorrelations.busevents.EventMainGraphRequest;
import com.akbank.investorrelations.busevents.EventMainGraphResponse;
import com.akbank.investorrelations.busevents.EventSnapshotRequest;
import com.akbank.investorrelations.busevents.EventSnapshotResponse;
import com.akbank.investorrelations.components.CompareButton;
import com.akbank.investorrelations.components.DateTextView;
import com.akbank.investorrelations.components.IntervalButton;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.ComparableStockData;
import com.akbank.investorrelations.objects.MainGraphDot;
import com.akbank.investorrelations.objects.SnapshotData;
import com.akbank.investorrelations.services.GraphHelper;
import com.akbank.investorrelations.utils.Constants;
import com.akbank.investorrelations.utils.TimeUtil;
import com.jjoe64.graphview.GraphView;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class StockDetailFragment extends BaseFragment {

    //public static final String ARG_ITEM_ID = "item_id";
    private GraphHelper helper;
    private RelativeLayout loadingLayout;
    private int period = 1;
    private ScrollView sc;
    private TextView percentIndicator;
//    private boolean daysAdjusted;
//    private String tmpStartDate;
//    private String tmpEndDate;

    public StockDetailFragment() {
    }

    @Override
    protected void setTag() {
        TAG = "Stock";
    }

    public int daysBetween = 0;
    DateTextView sDateView;
//    public boolean isIntervalButtonSelected;

    private void initIntervalLayout(View rootView, final DateTextView sDateView, final DateTextView eDateView, final CompareButton usdButton, final CompareButton eurButton) {
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
                            period = b.getPeriod();
                            String dateBefore;
                            usdButton.setVisibility(View.VISIBLE);
                            eurButton.setVisibility(View.VISIBLE);
                            switch (b.getInterval()) {
                                case 2:
                                    daysBetween = 30;
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-30, true, false);
                                    break;
                                case 3:
                                    daysBetween = 90;
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-90, true, false);
                                    break;
                                case 4:
                                    daysBetween = 180;
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-180, true, false);
                                    break;
                                case 5:
                                    daysBetween = 365;
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(-365, true, false);
                                    break;
                                case 1:
                                default:
                                    daysBetween = 1;
                                    dateBefore = TimeUtil.getDateBeforeOrAfterToday(0, true, false);
                                    usdButton.setVisibility(View.INVISIBLE);
                                    eurButton.setVisibility(View.INVISIBLE);
                                    break;
                            }
                            //isIntervalButtonSelected = true;
                            if(!b.isButtonSelected()){
                                b.setButtonSelected(true);
                                sDateView.getTextView().setText(dateBefore);
                            }
                            //dontReadjustDate = true;
                            //Log.d(TAG, "DONT READJUST DATE TRUE");
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
        final GraphView graphView = (GraphView) rootView.findViewById(R.id.graph);
        GraphView barGraphView = (GraphView) rootView.findViewById(R.id.barGraph);
        loadingLayout = (RelativeLayout) rootView.findViewById(R.id.loadingLayout);
        percentIndicator = (TextView) rootView.findViewById(R.id.percentIndicator);
        percentIndicator.setVisibility(View.INVISIBLE);
        final ArrayList<String> comparables = new ArrayList<>();
        comparables.add(Constants.AKBANK);
        final AkbankApp app = (AkbankApp) (getActivity().getApplication());
        helper = new GraphHelper(getActivity(), graphView, barGraphView);

/*
        final ViewTreeObserver vto = graphView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] pos = new int[2];
                graphView.getLocationOnScreen(pos);
                Log.d(TAG, "GRAPH POS: x: " + pos[0] + "posy: " + pos[1]);
            }
        });
*/

        helper.setPopupPanel((RelativeLayout) rootView.findViewById(R.id.popupPanel));
        helper.setPopupDot((ImageView) rootView.findViewById(R.id.dot));
        //send request imm. to draw main graph
        final GridView snapshotGridView = (GridView) rootView.findViewById(R.id.snapshotGridView);
        CompareButton bist30Button = (CompareButton) rootView.findViewById(R.id.bist30Button);
        CompareButton bistBankaButton = (CompareButton) rootView.findViewById(R.id.bistBankaButton);
        CompareButton usdButton = (CompareButton) rootView.findViewById(R.id.usdButton);
        CompareButton eurButton = (CompareButton) rootView.findViewById(R.id.eurButton);
        sDateView = (DateTextView) rootView.findViewById(R.id.startingDateView);
        final DateTextView eDateView = (DateTextView) rootView.findViewById(R.id.endingDateView);
        sDateView.setActivity(getActivity());
        eDateView.setActivity(getActivity());

        reAdjustDate(sDateView.getDateText(), sDateView, eDateView, true);
//        tmpStartDate = sDateView.getDateText();
//        tmpEndDate = eDateView.getDateText();
        eDateView.getTextView().addTextChangedListener( new DateViewTextWatcher(app, sDateView, eDateView, comparables, false));
        sDateView.getTextView().addTextChangedListener( new DateViewTextWatcher(app, sDateView, eDateView, comparables, true));

        initIntervalLayout(rootView, sDateView, eDateView, usdButton, eurButton);
        drawMainGraph(app, sDateView.getFormatConvertedDateText(), eDateView.getFormatConvertedDateText(),snapshotGridView, period, daysBetween);
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
                        percentIndicator.setVisibility(View.INVISIBLE);
                        drawMainGraph(app, sDateView.getFormatConvertedDateText(), eDateView.getFormatConvertedDateText(),snapshotGridView, period, daysBetween);
                    }else{
                        percentIndicator.setVisibility(View.VISIBLE);
                        drawComparableGraph(app, comparables, sDateView.getFormatConvertedDateText(), eDateView.getFormatConvertedDateText(), period);
                    }
                }
            }
        };

        bist30Button.setOnClickListener(compareButtonListener);
        bistBankaButton.setOnClickListener(compareButtonListener);
        usdButton.setOnClickListener(compareButtonListener);
        eurButton.setOnClickListener(compareButtonListener);
        usdButton.setVisibility(View.INVISIBLE);
        eurButton.setVisibility(View.INVISIBLE);
        //useLoader();
        if(mListener != null)
            mListener.onTitleTextChange(getString(R.string.Menu_Stocks));
        return rootView;
    }

    private void drawMainGraph(AkbankApp app, String startDate, String endDate, GridView gridView, int period, int daysBetween) {
        helper.cleanGraph();
        helper.cleanBarGraph();
        loadingLayout.setVisibility(View.VISIBLE);

        startDate = startDate.substring(0,8) + "090000";
        endDate = endDate.substring(0,8) + "173000";
        Log.d(TAG, "START DATE: " + startDate + " - endDate: " + endDate + " - period: " + period);
        if(gridView != null){
            app.getBus().post(new EventSnapshotRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY),gridView));
        }
        app.getBus().post(new EventMainGraphRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY),startDate, endDate, period, daysBetween));
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
            ArrayList<MainGraphDot> graphDots = response.body();
            if(graphDots.size() == 0){
                //sDateView.getTextView().setText(dateBefore);
                startDateChanged = true;
                DateTime dateTime = TimeUtil.getDateTime(sDateView.getDateText(), true, false);
                sDateView.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -1, true, false));
                //TODO this can be dangerous
                Log.d(TAG, "DATA NULL GO ONE DAY BEFORE");
                //return;
            }
            //1 is interday
            helper.init(graphDots, event.getPeriod() == 1, event.getDaysBetween());
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
            DecimalFormat capitalFormatter = new DecimalFormat("##,###");
            ArrayList<SnapshotGridAdapter.SnapShotItem> items = new ArrayList<>();
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_StockName), snapshotData.getName()));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_Last), Double.toString(GraphHelper.round(snapshotData.getLast(),3)) + " TL"));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_Change), (Double.toString(GraphHelper.round(snapshotData.getDailyChangePercentage(),3))+ " %")));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_Volume), volumeFormatter.format((snapshotData.getDailyVolume().longValue()) / 1000) + " MiO TL"));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_HighestLowest), snapshotData.getDailyHighest() + " - " + snapshotData.getDailyLowest()));
            items.add(new SnapshotGridAdapter.SnapShotItem(getString(R.string.Stock_MarketCapital), capitalFormatter.format(snapshotData.getMarketCapital().longValue() / 1000000) + " BiO TL"));
            SnapshotGridAdapter adapter = new SnapshotGridAdapter(getContext(), items);
            event.getSnapShotGridView().setAdapter(adapter);
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    boolean startDateChanged;

    public void reAdjustDate(String dateText, DateTextView startDate, DateTextView endDate, boolean isForStartDate) {
        int dateBetween = 0;
        //TODO
//        if(daysAdjusted){
//            return;
//        }
        DateTime dateTime = TimeUtil.getDateTime(dateText, true, false);
        if (dateText.equals(startDate.getDateText())) {
            dateBetween = TimeUtil.getDaysInBetween(dateTime, endDate.getDateTime());
            Log.d(TAG, "DAYS IN BETWEEN start:" + dateBetween);
            if (dateBetween == 1) {
                if (TimeUtil.isItWeekend(dateText) && TimeUtil.isItWeekend(endDate.getDateText())) {
                    startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -1, true, false));
//                    daysAdjusted = true;
                }
                //TODO günicine defalarca tiklamak bug
                else if(TimeUtil.isItWeekend(startDate.getDateText()) && TimeUtil.getDayOfWeek(endDate.getDateText()) == 1) { // dateText
                    startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -3, true, false));
//                    daysAdjusted = true;
                }
            } else if (dateBetween == 0) {
                if (TimeUtil.getDayOfWeek(dateText) == 6) {
                    startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -1, true, false));
//                    daysAdjusted = true;
                } else if (TimeUtil.getDayOfWeek(dateText) == 7) {
                    startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -2, true, false));
//                    daysAdjusted = true;
                }
                else if(TimeUtil.getDayOfWeek(dateText) == 1 ){
                    DateTime dt = DateTime.now();
                    int hour = dt.getHourOfDay();
                    int minute = dt.getMinuteOfHour();
                    //TODO TEST THIS
                    Log.d(TAG, "HOUR: " + hour + " : " + minute);
                    if(hour < 10 && minute < 31){
                        startDateChanged = true;
                        startDate.setDateText(TimeUtil.getDateBeforeOrAfter(dateTime, -3, true, false));
                    }
                }

//                else {

                    //startDateChanged = false;
                    //endDate.setDateText(TimeUtil.getDateBeforeOrAfter(endDate.getDateTime(), 1, true, false));
//                    daysAdjusted = true;
//                }
            }

        } else if (dateText.equals(endDate.getDateText())) {
            //DateTime startDateTime = TimeUtil.getDateTime(endDate.getDateText(), true, false);
            dateBetween = TimeUtil.getDaysInBetween(startDate.getDateTime(), dateTime);
            Log.d(TAG, "DAYS IN BETWEEN end:" + dateBetween);
            if (dateBetween == 1) {
                if (TimeUtil.isItWeekend(startDate.getDateText()) && TimeUtil.isItWeekend(dateText)) {
                    startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -1, true, false));
//                    daysAdjusted = true;
                }
                //TODO
//                else if(TimeUtil.isItWeekend(startDate.getDateText()) && TimeUtil.getDayOfWeek(dateText) == 1) {
//                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -3, true, false));
//                }
            } else if (dateBetween == 0) {
                //TODO
                 if (TimeUtil.getDayOfWeek(dateText) == 6) {
                     startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -1, true, false));
//                     daysAdjusted = true;
                } else if (TimeUtil.getDayOfWeek(dateText) == 7) {
                     startDateChanged = true;
                    startDate.setDateText(TimeUtil.getDateBeforeOrAfter(startDate.getDateTime(), -2, true, false));
//                     daysAdjusted = true;
                }
                 else {
                     //TODO
                     startDateChanged = false;
                     endDate.setDateText(TimeUtil.getDateBeforeOrAfter(endDate.getDateTime(), 1, true, false));
//                     daysAdjusted = true;
                }
            }
        }
    }

    public class DateViewTextWatcher implements TextWatcher {
        private final AkbankApp app;
        private final DateTextView startDate;
        private final DateTextView endDate;
        private final ArrayList<String> comparables;
        private final boolean isForStartDate;

        public DateViewTextWatcher(AkbankApp app, DateTextView startDate, DateTextView endDate, ArrayList<String> comparables, boolean isForStartDate) {
            this.app = app;
            this.startDate = startDate;
            this.endDate = endDate;
            this.comparables = comparables;
            this.isForStartDate = isForStartDate;
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
            Log.d(TAG, "TEXT: " + dateText + "isForStartDate: " + isForStartDate + "- startDateChanged: " + startDateChanged);

//            if(isForStartDate && !startDateChanged) {
//                return;
//            }
//            else if(!isForStartDate && startDateChanged){
//                    return;
//            }
//            if(dontReadjustDate){
//                dontReadjustDate = false;
//                return;
//            }
            reAdjustDate(dateText, startDate, endDate, isForStartDate);
            if (comparables.size() <= 1) {
                Log.d(TAG, "AFTER TEXT CHANGED: refresh main graph: true : period: " + period);
                //helper.cleanGraph();
                drawMainGraph(app, startDate.getFormatConvertedDateText(), endDate.getFormatConvertedDateText(), null, period, daysBetween);
            } else {
                Log.d(TAG, "AFTER TEXT CHANGED: refresh main graph: false: period: " + period);
                drawComparableGraph(app, comparables, startDate.getFormatConvertedDateText(), endDate.getFormatConvertedDateText(), period);
            }
        }
    }
}
