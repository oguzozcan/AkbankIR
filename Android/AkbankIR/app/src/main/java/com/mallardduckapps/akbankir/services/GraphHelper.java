package com.mallardduckapps.akbankir.services;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.percent.PercentLayoutHelper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.objects.ComparableStockData;
import com.mallardduckapps.akbankir.objects.GraphDot;
import com.mallardduckapps.akbankir.objects.MainGraphDot;
import com.mallardduckapps.akbankir.utils.TimeUtil;
import com.mallardduckapps.akbankir.utils.Utils;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by oguzemreozcan on 31/01/16.
 */
public class GraphHelper {

    private final GraphView graphView;
    private final GraphView barGraph;
    private final Activity activity;
    private LinearLayout popupPanel;
    private final String TAG = "GraphHelper";

    public GraphHelper(final Activity activity, final GraphView mainGrapView, final GraphView barGraph){
        this.activity = activity;
        graphView = mainGrapView;
        this.barGraph = barGraph;
    }

    public void init(final ArrayList<MainGraphDot> graphDots){
        setMainGraph(graphDots);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphView.getGridLabelRenderer().setGridColor(ContextCompat.getColor(activity, R.color.warm_grey));
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        //graphView.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);
        graphView.getGridLabelRenderer().setVerticalLabelsSecondScaleAlign(Paint.Align.CENTER);
        graphView.getViewport().setScalable(false);
        graphView.getViewport().setXAxisBoundsManual(true);
  /*      graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return TimeUtil.getDateTxtAccordingToMillis((long) value, null);//super.formatLabel(value, isValueX);
                } else {
                    // show normal y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });*/
        if(barGraph != null){
            barGraph.getGridLabelRenderer().setGridColor(ContextCompat.getColor(activity, R.color.warm_grey));
            barGraph.getViewport().setScalable(false);
            //barGraph.getViewport().setXAxisBoundsManual(true);
            barGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
            //barGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(activity));

        }

        /*{
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    Log.d(TAG, "VALUE X: " + value);
                    return dateTimeFormatter.format(new Date((long) value));//TimeUtil.getDateTxtAccordingToMillis(new DateTime(value));
                        //return TimeUtil.getDateTxtAccordingToMillis((long)value, null);//super.formatLabel(value, isValueX);
                } else {
                    // show normal y values
                    Log.d(TAG, "VALUE: " + new BigDecimal(value).longValue());
                    return super.formatLabel(new BigDecimal(value).longValue(), isValueX);
                }
            }
        });*/
    }

    public void setPopupPanel(LinearLayout popupPanel){
        this.popupPanel = popupPanel;
    }

    private void setMainGraph(final ArrayList<MainGraphDot> graphDots){
        if(graphDots != null){
            final int dotsSize = graphDots.size();
            DataPoint[] dataPoints = new DataPoint[dotsSize];
            DataPoint[] dataPoints2 = new DataPoint[dotsSize];
            DataPoint[] volumeDataPoints = new DataPoint[dotsSize];
            //dateDataPoints = new DataPoint[graphDots.size()];
            String[] dates = new String[graphDots.size()];
            //dateArray = new long[graphDots.size()];
            int i = 0 ;
            double max = 0;
            double min = 0;

            for(MainGraphDot gd :  graphDots){
                double y = gd.getCloseValue();
                dataPoints[i] = new DataPoint(i, gd.getCloseValue());
                dataPoints2[i] = new DataPoint(i,1);
                //dateDataPoints[i] = new DataPoint(i, gd.getDate());
                dates[i] = TimeUtil.getDateTxtAccordingToMillis(gd.getDate(), TimeUtil.dtfBarGraph);
                //dateArray[i] = gd.getDate();
                volumeDataPoints[i] = new DataPoint(i, new BigDecimal(gd.getV()).longValue());
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
            final AkbankApp app = (AkbankApp)activity.getApplication();
            LineGraphSeries<DataPoint> series = setLineGraphSerie(dataPoints, Color.RED);
            final int screenWidth = app.getScreenSize(activity)[0];
            final double avarage = max - min;
            final double graphMin = min;

            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    //Toast.makeText(activity, "Series1: On Data Point clicked: x:" + dataPoint.getX(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "GRAPH DOT size: " + dotsSize + "- x: " + dataPoint.getX() + "- screenwidth: " + screenWidth + " - graphView height: " + graphView.getHeight());
                    if (popupPanel != null) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 80);
                        int marginLeft = dataPoint.getX() > 0 ? ((int) (screenWidth / dotsSize * (dataPoint.getX() - 1))) : 0;
                        int marginTop = graphView.getHeight() - popupPanel.getHeight()- 20;;
                        //avarage = graphView.getHeight
                        double differenceWithMin = (dataPoint.getY() - graphMin);
                        if(differenceWithMin > 0){
                            marginTop -= (int)(graphView.getHeight() * (differenceWithMin) / avarage);
                            Log.d(TAG, "MARGIN TOP : " + marginTop );
                        }else {
                           //÷ marginTop =
                        }
                        params.topMargin = marginTop;
                        if(marginLeft + popupPanel.getWidth() >= screenWidth){
                            marginLeft = screenWidth - popupPanel.getWidth() - 45;
                        }
                        params.leftMargin = marginLeft;
                        params.addRule(RelativeLayout.BELOW, R.id.topPanel);
                        Log.d(TAG, "LEFT MARGIN : " + marginLeft + " - screenwidth: " + screenWidth + " - popup panelwidth: " + popupPanel.getWidth() + "- topMargin : " + dataPoint.getY());
                        if (dataPoint.getX() > dotsSize / 2) {
                            //setPivot
                            popupPanel.setPivotX(popupPanel.getWidth());
                        } else {
                            popupPanel.setPivotX(0);
                        }
                        popupPanel.setVisibility(View.VISIBLE);
                        popupPanel.setLayoutParams(params);
                    }

                }
            });

//            graphView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    return false;
//                }
//            });
            //To remove popup panel when user
            if(popupPanel != null){
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
                                }
                                return false;

                        }
                        return false;
                    }
                });
            }
            LineGraphSeries<DataPoint> series2 = setLineGraphSerie(dataPoints2, Color.TRANSPARENT);
            graphView.getSecondScale().addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(2);
            graphView.addSeries(series2);
            if(barGraph != null){
                BarGraphSeries<DataPoint> barGraphSeries = setBarGraphSerie(volumeDataPoints, Color.RED);
                barGraph.addSeries(barGraphSeries);
                //barGraph.getViewport().setMinX(0);
                //barGraph.getViewport().setMaxX(graphDots.size());
                barGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
                if(dates.length > 0){
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(barGraph);
                    staticLabelsFormatter.setHorizontalLabels(dates);
                    barGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    //TODO
                    //staticLabelsFormatter.setVerticalLabels(new String[]{"low", "middle", "high"});
                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, activity.getResources().getDisplayMetrics());
                    barGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(size);
                    barGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(size);
                    barGraph.getGridLabelRenderer().setTextSize(size);
                    //barGraph.setTitleTextSize(size);
                    barGraph.getGridLabelRenderer().reloadStyles();
                }
            }
            //barGraph.getGridLabelRenderer().setHo
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

    public void setComparableGraph(ComparableStockData comparableData){
        //graphView.removeAllSeries();
        if(comparableData != null){
            ArrayList<GraphDot> akbankData = comparableData.getAkbankData();
            ArrayList<GraphDot> banksData = comparableData.getxBanksData();
            //ArrayList<GraphDot> xu100Data = comparableData.getXu100Data();
            ArrayList<GraphDot> xu30Data = comparableData.getXu30Data();
            ArrayList<GraphDot> usdData = comparableData.getUsdData();
            ArrayList<GraphDot> euroData = comparableData.getEuroData();

            ArrayList<GraphDot> totalData = new ArrayList<>();
            DataPoint[] akbankDataPoints = getDataPointArray(akbankData);
            DataPoint[] xBanksDataPoints,xu30DataPoints,usdDataPoints,euroDataPoints; //xu100DataPoints,
            LineGraphSeries<DataPoint> xBanksSeries,xu30Series,usdSeries,euroSeries; //xu100Series,
            LineGraphSeries<DataPoint> akbankSeries = setLineGraphSerie(akbankDataPoints, Color.RED);
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

    public LineGraphSeries<DataPoint> setLineGraphSerie(DataPoint[] dataPoints, int color){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        series.setThickness(3);
        series.setDrawDataPoints(false);
        series.setDataPointsRadius(3);
        series.setColor(color);
        return series;
    }

    public BarGraphSeries<DataPoint> setBarGraphSerie(DataPoint[] dataPoints, int color){
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        //series.setThickness(3);
//        series.setDrawDataPoints(false);
//        series.setDataPointsRadius(10);
        series.setSpacing(1);
        series.setColor(color);
        return series;
    }
}
