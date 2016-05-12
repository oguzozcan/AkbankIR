package com.akbank.investorrelations.services;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.R;
import com.akbank.investorrelations.components.MyLineGraphSeries;
import com.akbank.investorrelations.objects.ComparableStockData;
import com.akbank.investorrelations.objects.GraphDot;
import com.akbank.investorrelations.objects.MainGraphDot;
import com.akbank.investorrelations.utils.TimeUtil;
import com.akbank.investorrelations.utils.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 31/01/16.
 */
public class GraphHelper {

    private final GraphView graphView;
    private final GraphView barGraph;
    private final Activity activity;
    private RelativeLayout popupPanel;
    private ImageView dot;
    private boolean isInterday;
    private int daysBetween;
    private final String TAG = "GraphHelper";
    private ArrayList<MainGraphDot> mainGraphDots;

    public GraphHelper(final Activity activity, final GraphView mainGrapView, final GraphView barGraph){
        this.activity = activity;
        graphView = mainGrapView;
        this.barGraph = barGraph;
    }

    public void init(final ArrayList<MainGraphDot> graphDots, final boolean isInterday, int daysBetween){
        this.isInterday = isInterday;
        this.daysBetween = daysBetween;
        setMainGraph(graphDots);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphView.getGridLabelRenderer().setGridColor(ContextCompat.getColor(activity, R.color.warm_grey));
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setVerticalLabelsSecondScaleAlign(Paint.Align.CENTER);
        graphView.getViewport().setScalable(false);
        graphView.getViewport().setXAxisBoundsManual(true);
        if(barGraph != null){
            barGraph.getGridLabelRenderer().setGridColor(ContextCompat.getColor(activity, R.color.warm_grey));
            barGraph.getViewport().setScalable(false);
            barGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        }
    }

    public void setPopupPanel(RelativeLayout popupPanel){
        this.popupPanel = popupPanel;
    }

    public void setPopupDot(ImageView dot){
        this.dot = dot;
    }

    private void initGraph(){
        if(popupPanel != null){
            popupPanel.setVisibility(View.GONE);
            //To remove popup panel when user
            graphView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "MOTION EVENT DOWN");
                            if(popupPanel.getVisibility() == View.VISIBLE){
                                return false;
                            }else{
                                return false;
                            }

                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "MOTION EVENT UP");
                            if(popupPanel.getVisibility() == View.VISIBLE){
                                popupPanel.setVisibility(View.GONE);
                                dot.setVisibility(View.GONE);
                            }
                            return false;

                    }
                    return false;
                }
            });
        }
        if(dot != null){
            dot.setVisibility(View.GONE);
        }
    }

    private void setMainGraph(final ArrayList<MainGraphDot> graphDots){
        if(graphDots != null){
            mainGraphDots = graphDots;
            final int dotsSize = graphDots.size();
            DataPoint[] dataPoints = new DataPoint[dotsSize];
            DataPoint[] dataPoints2 = new DataPoint[dotsSize];
            DataPoint[] volumeDataPoints = new DataPoint[dotsSize];
            initGraph();
            //dateDataPoints = new DataPoint[graphDots.size()];
            String[] dates = new String[graphDots.size()];
            //dateArray = new long[graphDots.size()];
            int i = 0 ;
            double max = 0;
            double min = 0;

            for(MainGraphDot gd :  graphDots){
                double y = gd.getCloseValue();
                dataPoints[i] = new DataPoint(i, y);
                dataPoints2[i] = new DataPoint(i,1);
                //dateDataPoints[i] = new DataPoint(i, gd.getDate());
                dates[i] = TimeUtil.getDateTxtAccordingToMillis(gd.getDate(), isInterday ? TimeUtil.dtfBarGraphTime : TimeUtil.dtfBarGraph);
                Log.d(TAG, "DATES: " + dates[i] + "- date : "  + gd.getDate());
                //dateArray[i] = gd.getDate();
                volumeDataPoints[i] = new DataPoint(i, new BigDecimal(gd.getV()).longValue());
                Log.d(TAG, "VOLUME DATA: "+ i + "VALUE: " + new BigDecimal(gd.getV()).longValue());
                //Log.d("TAg", "DATE: " + TimeUtil.getDateTxtAccordingToMillis(gd.getDate(), null) + " - date: " + TimeUtil.getDate(gd.getDate()).toString());
                //Log.d(TAG, "DATE AS date format: " +dates[i]);
                Log.d("TAg", "Y: " + y );
                Log.d("TAg", "VOLUME: " + new BigDecimal(gd.getV()) + " - rounded: " + round(gd.getV(),2) );
                if(i == 0){
                    max = y;
                    min = y;
                }
                if(y > max){
                    max = y;
                }
                if(y < min){
                    min = y;
                }
                i ++;
            }

            final MyLineGraphSeries<DataPoint> series = setLineGraphSerie(dataPoints, Color.RED);
//            final double avarage = max - min;
//            final double graphMin = min;
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    //Toast.makeText(activity, "Series1: On Data Point clicked: x:" + dataPoint.getX(), Toast.LENGTH_SHORT).show();
                    graphDotOnClick(dataPoint, (MyLineGraphSeries) series, graphDots, null);
                }
            });

            MyLineGraphSeries<DataPoint> series2 = setLineGraphSerie(dataPoints2, Color.TRANSPARENT);
            graphView.getSecondScale().addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(1);
            graphView.addSeries(series2);
            if(barGraph != null){
                BarGraphSeries<DataPoint> barGraphSeries = setBarGraphSerie(volumeDataPoints, Color.RED);
                barGraph.addSeries(barGraphSeries);
                //barGraph.getViewport().setMinX(0);
                //barGraph.getViewport().setMaxX(graphDots.size());
                barGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
                if(dates.length > 0){
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(barGraph);
                    staticLabelsFormatter.setHorizontalLabels(manipulateDates(dates, isInterday, daysBetween));
                    barGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    barGraph.getViewport().setXAxisBoundsManual(true);
                    barGraph.getViewport().setMinX(barGraphSeries.getLowestValueX());
                    barGraph.getViewport().setMaxX(barGraphSeries.getHighestValueX());
                    Log.d(TAG, "MIN: " + barGraphSeries.getLowestValueX());
                    Log.d(TAG, "MAX "+ barGraphSeries.getHighestValueX());
                    Log.d(TAG, "DATE MIN: " + dates[0]);
                    Log.d(TAG, "DATE MAX "+ dates[dates.length -1]);
                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, activity.getResources().getDisplayMetrics());
                    barGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(size);
                    barGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(size);
                    barGraph.getGridLabelRenderer().setTextSize(size);
                    //barGraph.getViewport().setScrollable(true);
                    //barGraph.setTitleTextSize(size);
                    barGraph.getGridLabelRenderer().reloadStyles();
                }
            }
           // barGraph.getSecondScale().addSeries(barGraphDateSeries);
            //barGraph.getGridLabelRenderer().setNumHorizontalLabels(graphDots.size());
            //graphView.addSeries(series3);
            int graphTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, activity.getResources().getDisplayMetrics());
            graphView.getGridLabelRenderer().setTextSize(graphTextSize);
            graphView.getGridLabelRenderer().setVerticalAxisTitleTextSize(graphTextSize);
            graphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(graphTextSize);
            //graphView.setTitleTextSize(graphTextSize);
            graphView.getSecondScale().setMinY(min);
            graphView.getSecondScale().setMaxY(max);
            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(graphDots.size());
            graphView.getGridLabelRenderer().reloadStyles();
        }
    }

    private void graphDotOnClick(DataPointInterface dataPoint, final MyLineGraphSeries series, final ArrayList<MainGraphDot> mainGraphDots, final ComparableStockData comparableData ){
        //final AkbankApp app = (AkbankApp)activity.getApplication();
        //final int screenWidth = app.getScreenSize(activity)[0];
        final int dotsSize = mainGraphDots.size();
       // Log.d(TAG, "GRAPH DOT size: " + dotsSize + "- x: " + dataPoint.getX() + "- screenwidth: " + screenWidth + " - graphView height: " + graphView.getHeight() + "- isInterday:  " + isInterday);
        if (popupPanel != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int dotSize = (int)Utils.convertDpToPixel(3, activity);
            RelativeLayout.LayoutParams paramsDot = new RelativeLayout.LayoutParams(dotSize, dotSize);
            if(series instanceof MyLineGraphSeries){
                ArrayList<MyLineGraphSeries.DataPointPosition> posArray = ((MyLineGraphSeries) series).getDataPointPositionArrayList();
                int i = (int)dataPoint.getX();
                int marginLeft = (int)posArray.get(i).getStartX();
                int marginTop = (int)posArray.get(i).getStartY();
                TextView timeTv = (TextView) popupPanel.findViewById(R.id.timeTv);
                TextView volumeTv = (TextView) popupPanel.findViewById(R.id.volumeTv);
                TextView akbankTv = (TextView) popupPanel.findViewById(R.id.akbankTv);
                TextView bist30Tv = (TextView) popupPanel.findViewById(R.id.bist30Tv);
                TextView bistBankTv = (TextView) popupPanel.findViewById(R.id.bistBankTv);
                TextView usdTv = (TextView) popupPanel.findViewById(R.id.usdTv);
                TextView eurTv = (TextView) popupPanel.findViewById(R.id.eurTv);
                bist30Tv.setVisibility(View.GONE);
                bistBankTv.setVisibility(View.GONE);
                usdTv.setVisibility(View.GONE);
                eurTv.setVisibility(View.GONE);

                MainGraphDot gd = mainGraphDots.get((int)dataPoint.getX());
                timeTv.setText("TIME : " + TimeUtil.getDateTxtAccordingToMillis(gd.getDate(), isInterday ? TimeUtil.dtfBarGraphTime : TimeUtil.updateDateFormatWOTime));
                akbankTv.setText("AKBANK: " + dataPoint.getY());
                DecimalFormat volumeFormatter = new DecimalFormat("#,###,###");
                volumeTv.setText("VOLUME : " +  volumeFormatter.format(new BigDecimal(gd.getV()).longValue()));

                if(comparableData != null){
                    //ArrayList<GraphDot> akbankData = comparableData.getAkbankData();
                    ArrayList<GraphDot> banksData = comparableData.getxBanksData();
                    //ArrayList<GraphDot> xu100Data = comparableData.getXu100Data();
                    ArrayList<GraphDot> xu30Data = comparableData.getXu30Data();
                    ArrayList<GraphDot> usdData = comparableData.getUsdData();
                    ArrayList<GraphDot> euroData = comparableData.getEuroData();

                    if(xu30Data != null){
                        bist30Tv.setText("BIST 30 : " + xu30Data.get(i).getCloseValue());
                        bist30Tv.setVisibility(View.VISIBLE);
                    }

                    if(usdData != null){
                        usdTv.setText("USD : " + usdData.get(i).getCloseValue());
                        usdTv.setVisibility(View.VISIBLE);
                    }

                    if(euroData != null){
                        eurTv.setText("EUR : " + euroData.get(i).getCloseValue());
                        eurTv.setVisibility(View.VISIBLE);
                    }

                    if(banksData != null){
                        bistBankTv.setText("BIST BANK : " + banksData.get(i).getCloseValue());
                        bistBankTv.setVisibility(View.VISIBLE);
                    }
                }

                params.topMargin = marginTop;
                paramsDot.topMargin = marginTop;
                if (dataPoint.getX() > dotsSize/2 + 1) {
                    //setPivot
                    Log.d(TAG," GET WIDTH: " + params.width);
                    params.leftMargin = marginLeft;// - params.width;
                    popupPanel.setPivotX(popupPanel.getWidth());
                    //paramsDot.leftMargin = marginLeft - popupPanel.getWidth();
                } else {
                    params.leftMargin = marginLeft;
                    popupPanel.setPivotX(0);
                }
                paramsDot.leftMargin = marginLeft;
                popupPanel.setVisibility(View.VISIBLE);
                dot.setVisibility(View.VISIBLE);
                popupPanel.setLayoutParams(params);
                dot.setLayoutParams(paramsDot);
            }
        }
    }

    private String[] manipulateDates(String[] dates, boolean isInterday, int daysBetween){
        int size = dates.length;
        //String[] datesClon = Arrays.copyOf(dates, dates.length);//new String[dates.length];
        if(isInterday){
//            String[] newDates = new String[9];
//            newDates[0] = dates[0];
//            int period = size / 9;
//            Log.d(TAG, "PERIOD: " + period + "size: " + size);
//            int lastIndex = period;
//            for(int i = 1 ; i < 8; i++){
//                newDates[i] = dates[lastIndex];
//                Log.d(TAG, "DATES: " + dates[i]);
//                lastIndex+=period;
//            }
//            newDates[8] = dates[dates.length -1];
            return new String[] {"09.30", "10.30", "11.30", "12.30", "13.30", "14.30", "15.30", "16.30", "17.39"};
        }else{
            String[] newDates;
            int period;
            int lastIndex;
            switch (daysBetween){
                case 30:
                    newDates = new String[4];
                    newDates[0] = dates[0];
                    period = size / 4;
                    Log.d(TAG, "PERIOD: " + period + "size: " + size);
                    lastIndex = period;
                    for(int i = 1 ; i < 3; i++){
                        newDates[i] = dates[lastIndex];
                        lastIndex+=period;
                    }
                    newDates[3] = dates[size -1];
                    return newDates;
                case 90:
                    newDates = new String[6];
                    newDates[0] = dates[0];
                    period = size / 6;
                    Log.d(TAG, "PERIOD: " + period + "size: " + size);
                    lastIndex = period;
                    for(int i = 1 ; i < 5; i++){
                        newDates[i] = dates[lastIndex];
                        lastIndex+=period;
                    }
                    newDates[5] = dates[size -1];
                    return newDates;
                case 180:
                    newDates = new String[3];
                    newDates[0] = dates[0];
                    period = size / 3;
                    Log.d(TAG, "PERIOD: " + period + "size: " + size);
                    lastIndex = period;
                    for(int i = 1 ; i < 2; i++){
                        newDates[i] = dates[lastIndex];
                        lastIndex+=period;
                    }
                    newDates[2] = dates[size -1];
                    return newDates;
                case 365:
                    newDates = new String[6];
                    newDates[0] = dates[0];
                    period = size / 6;
                    Log.d(TAG, "PERIOD: " + period + "size: " + size);
                    lastIndex = period;
                    for(int i = 1 ; i < 5; i++){
                        newDates[i] = dates[lastIndex];
                        lastIndex+=period;
                    }
                    newDates[5] = dates[size -1];
                    return newDates;

            }

            return dates;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void cleanGraph(){
        graphView.removeAllSeries();
        graphView.getSecondScale().getSeries().clear();
    }

    public void cleanBarGraph(){
        barGraph.removeAllSeries();
    }

    public void setComparableGraph(final ComparableStockData comparableData){
        //graphView.removeAllSeries();
        if(comparableData != null){
            ArrayList<GraphDot> akbankData = comparableData.getAkbankData();
            ArrayList<GraphDot> banksData = comparableData.getxBanksData();
            //ArrayList<GraphDot> xu100Data = comparableData.getXu100Data();
            ArrayList<GraphDot> xu30Data = comparableData.getXu30Data();
            ArrayList<GraphDot> usdData = comparableData.getUsdData();
            ArrayList<GraphDot> euroData = comparableData.getEuroData();

            final ArrayList<GraphDot> totalData = new ArrayList<>();
            DataPoint[] akbankDataPoints = getDataPointArray(akbankData);
            DataPoint[] xBanksDataPoints,xu30DataPoints,usdDataPoints,euroDataPoints; //xu100DataPoints,
            MyLineGraphSeries<DataPoint> xBanksSeries,xu30Series,usdSeries,euroSeries; //xu100Series,
            MyLineGraphSeries<DataPoint> akbankSeries = setLineGraphSerie(akbankDataPoints, Color.RED);
            totalData.addAll(akbankData);
/*            if(xu100Data != null){
                if(xu100Data.size() > 0){
                    totalData.addAll(xu100Data);
                    xu100DataPoints = getDataPointArray(xu100Data);
                    xu100Series = setLineGraphSerie(xu100DataPoints, ContextCompat.getColor(activity, R.color.bist30Color));
                    graphView.addSeries(xu100Series);
                }
            }*/
            if(banksData != null){
                if(banksData.size() > 0){
                    totalData.addAll(banksData);
                    xBanksDataPoints = getDataPointArray(banksData);
                    xBanksSeries = setLineGraphSerie(xBanksDataPoints, ContextCompat.getColor(activity, R.color.bistBankColor));
                    graphView.addSeries(xBanksSeries);
                }
            }

            if(xu30Data != null){
                if(xu30Data.size() > 0){
                    totalData.addAll(xu30Data);
                    xu30DataPoints = getDataPointArray(xu30Data);
                    xu30Series = setLineGraphSerie(xu30DataPoints, ContextCompat.getColor(activity, R.color.bist30Color));
                    graphView.addSeries(xu30Series);
                }
            }

            if(usdData != null){
                if(usdData.size() > 0){
                    totalData.addAll(usdData);
                    usdDataPoints = getDataPointArray(usdData);
                    usdSeries = setLineGraphSerie(usdDataPoints, ContextCompat.getColor(activity, R.color.dolarColor));
                    graphView.addSeries(usdSeries);
                }
            }

            if(euroData != null){
                if(euroData.size() > 0){
                    totalData.addAll(euroData);
                    euroDataPoints = getDataPointArray(euroData);
                    euroSeries = setLineGraphSerie(euroDataPoints, ContextCompat.getColor(activity, R.color.euroColor));
                    graphView.addSeries(euroSeries);
                }
            }
            double[] values = getMaxAndMinOfSerie(totalData);
            graphView.getSecondScale().setMinY(values[1]);
            graphView.getSecondScale().setMaxY(values[0]);
            graphView.getSecondScale().addSeries(akbankSeries);
            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(akbankData.size());
            //TODO graph popup
            initGraph();
            akbankSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    //Toast.makeText(activity, "Series1: On Data Point clicked: x:" + dataPoint.getX(), Toast.LENGTH_SHORT).show();
                    if(mainGraphDots != null)
                        graphDotOnClick(dataPoint, (MyLineGraphSeries) series,mainGraphDots, comparableData);
                }
            });

        }else{
            Log.d(TAG, "DATA IS NULL");
        }
    }

    private double[] getMaxAndMinOfSerie(ArrayList<GraphDot> totalData){
        double[] values = new double[2];
        double max = 0;
        double min = 0;
        for(int i = 0; i < totalData.size(); i ++){
            double y = totalData.get(i).getChangePercentage();
            if(i == 0){
                max = y;
                min = y;
            }
            if(y > max){
                max = y;
            }
            if(y < min){
                min = y;
            }
        }
        values[0] = max;
        values[1] = min;
        return values;
    }

    private DataPoint[] getDataPointArray(ArrayList<GraphDot> graphDots){
        DataPoint[] dataPoints = new DataPoint[graphDots.size()];
        Log.d(TAG, "DATA POINTS SIZE: " + graphDots.size());
        int i = 0;
        for(GraphDot gd :  graphDots){
            dataPoints[i] = new DataPoint(i, gd.getChangePercentage());
            i ++;
        }
        return dataPoints;
    }

    public MyLineGraphSeries<DataPoint> setLineGraphSerie(DataPoint[] dataPoints, int color){
        MyLineGraphSeries<DataPoint> series = new MyLineGraphSeries<DataPoint>(dataPoints);
        series.setThickness(3);
        series.setDrawDataPoints(false);
        series.setDataPointsRadius(3);
        series.setColor(color);
        return series;
    }

    public BarGraphSeries<DataPoint> setBarGraphSerie(DataPoint[] dataPoints, int color){
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        Log.d(TAG, "BAR GRAPH SERIES: " + dataPoints.length);
        //series.setThickness(3);
//        series.setDrawDataPoints(false);
//        series.setDataPointsRadius(10);
        series.setSpacing(1);
        series.setColor(color);
        return series;
    }
}
