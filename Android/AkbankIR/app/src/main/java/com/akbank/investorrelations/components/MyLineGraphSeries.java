package com.akbank.investorrelations.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPointInterface;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by oguzemreozcan on 09/05/16.
 */
public class MyLineGraphSeries <E extends DataPointInterface> extends BaseSeries<E> {
    /**
     * wrapped styles regarding the line
     */
    private final class Styles {
        /**
         * the thickness of the line.
         * This option will be ignored if you are
         * using a custom paint via {@link #setCustomPaint(android.graphics.Paint)}
         */
        private int thickness = 5;

        /**
         * flag whether the area under the line to the bottom
         * of the viewport will be filled with a
         * specific background color.
         *
         * @see #backgroundColor
         */
        private boolean drawBackground = false;

        /**
         * flag whether the data points are highlighted as
         * a visible point.
         *
         * @see #dataPointsRadius
         */
        private boolean drawDataPoints = false;

        /**
         * the radius for the data points.
         *
         * @see #drawDataPoints
         */
        private float dataPointsRadius = 10f;

        /**
         * the background color for the filling under
         * the line.
         *
         * @see #drawBackground
         */
        private int backgroundColor = Color.argb(100, 172, 218, 255);
    }

    /**
     * wrapped styles
     */
    private Styles mStyles;

    /**
     * internal paint object
     */
    private Paint mPaint;

    /**
     * paint for the background
     */
    private Paint mPaintBackground;

    private Paint testPaint;

    /**
     * path for the background filling
     */
    private Path mPathBackground;

    /**
     * path to the line
     */
    private Path mPath;

    /**
     * custom paint that can be used.
     * this will ignore the thickness and color styles.
     */
    private Paint mCustomPaint;

    /**
     * creates a series without data
     */
    public MyLineGraphSeries() {
        init();
    }

    /**
     * creates a series with data
     *
     * @param data data points
     */
    public MyLineGraphSeries(E[] data) {
        super(data);
        init();
    }

    ArrayList<DataPointPosition> dataPointPositionArrayList;
    /**
     * do the initialization
     * creates internal objects
     */
    protected void init() {
        mStyles = new Styles();
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaintBackground = new Paint();
//        testPaint = new Paint();
//        testPaint.setColor(Color.CYAN);
//        testPaint.setStrokeCap(Paint.Cap.ROUND);
//        testPaint.setStyle(Paint.Style.STROKE);

        mPathBackground = new Path();
        mPath = new Path();

        dataPointPositionArrayList = new ArrayList<>();
    }

    public ArrayList<DataPointPosition> getDataPointPositionArrayList(){
        return dataPointPositionArrayList;
    }

    /**
     * plots the series
     * draws the line and the background
     *
     * @param graphView graphview
     * @param canvas canvas
     * @param isSecondScale flag if it is the second scale
     */
    @Override
    public void draw(GraphView graphView, Canvas canvas, boolean isSecondScale) {
        resetDataPoints();

        // get data
        double maxX = graphView.getViewport().getMaxX(false);
        double minX = graphView.getViewport().getMinX(false);

        double maxY;
        double minY;
        if (isSecondScale) {
            maxY = graphView.getSecondScale().getMaxY();
            minY = graphView.getSecondScale().getMinY();
        } else {
            maxY = graphView.getViewport().getMaxY(false);
            minY = graphView.getViewport().getMinY(false);
        }

        Iterator<E> values = getValues(minX, maxX);

        // draw background
        double lastEndY = 0;
        double lastEndX = 0;

        // draw data
        mPaint.setStrokeWidth(mStyles.thickness);
        mPaint.setColor(getColor());
        mPaintBackground.setColor(mStyles.backgroundColor);

        Paint paint;
        if (mCustomPaint != null) {
            paint = mCustomPaint;
        } else {
            paint = mPaint;
        }

        if (mStyles.drawBackground) {
            mPathBackground.reset();
        }

        double diffY = maxY - minY;
        double diffX = maxX - minX;

        float graphHeight = graphView.getGraphContentHeight();
        float graphWidth = graphView.getGraphContentWidth();
        float graphLeft = graphView.getGraphContentLeft();
        float graphTop = graphView.getGraphContentTop();

        lastEndY = 0;
        lastEndX = 0;
        double lastUsedEndX = 0;
        float firstX = 0;
        int i=0;
        while (values.hasNext()) {
            E value = values.next();

            double valY = value.getY() - minY;
            double ratY = valY / diffY;
            double y = graphHeight * ratY;

            double valX = value.getX() - minX;
            double ratX = valX / diffX;
            double x = graphWidth * ratX;

            double orgX = x;
            double orgY = y;

            if (i > 0) {
                // overdraw
                if (x > graphWidth) { // end right
                    double b = ((graphWidth - lastEndX) * (y - lastEndY)/(x - lastEndX));
                    y = lastEndY+b;
                    x = graphWidth;
                }
                if (y < 0) { // end bottom
                    double b = ((0 - lastEndY) * (x - lastEndX)/(y - lastEndY));
                    x = lastEndX+b;
                    y = 0;
                }
                if (y > graphHeight) { // end top
                    double b = ((graphHeight - lastEndY) * (x - lastEndX)/(y - lastEndY));
                    x = lastEndX+b;
                    y = graphHeight;
                }
                if (lastEndY < 0) { // start bottom
                    double b = ((0 - y) * (x - lastEndX)/(lastEndY - y));
                    lastEndX = x-b;
                    lastEndY = 0;
                }
                if (lastEndX < 0) { // start left
                    double b = ((0 - x) * (y - lastEndY)/(lastEndX - x));
                    lastEndY = y-b;
                    lastEndX = 0;
                }
                if (lastEndY > graphHeight) { // start top
                    double b = ((graphHeight - y) * (x - lastEndX)/(lastEndY - y));
                    lastEndX = x-b;
                    lastEndY = graphHeight;
                }

                float startX = (float) lastEndX + (graphLeft + 1);
                float startY = (float) (graphTop - lastEndY) + graphHeight;
                float endX = (float) x + (graphLeft + 1);
                float endY = (float) (graphTop - y) + graphHeight;

                // draw data point
                if (mStyles.drawDataPoints) {
                    //fix: last value was not drawn. Draw here now the end values
                    canvas.drawCircle(endX, endY, mStyles.dataPointsRadius, mPaint);
                }
                dataPointPositionArrayList.add(new DataPointPosition(startX,startY, endX, endY));

//                if(i == 5){
//                    Log.d("MYLINE GRAPH SERIES", "POINT " + i + ": endX: " + endX + " - endY: " + endY);
//
//                }
//                Log.d("MYLINE GRAPH SERIES", "POINT " + i + ": endX: " + endX + " - endY: " + endY);
//                canvas.drawRect(endX, endY, 180, 100, testPaint);
                registerDataPoint(endX, endY, value);

                mPath.reset();
                mPath.moveTo(startX, startY);
                mPath.lineTo(endX, endY);
                canvas.drawPath(mPath, paint);
                if (mStyles.drawBackground) {
                    if (i==1) {
                        firstX = startX;
                        mPathBackground.moveTo(startX, startY);
                    }
                    mPathBackground.lineTo(endX, endY);
                }
                lastUsedEndX = endX;
            } else if (mStyles.drawDataPoints) {
                //fix: last value not drawn as datapoint. Draw first point here, and then on every step the end values (above)
                float first_X = (float) x + (graphLeft + 1);
                float first_Y = (float) (graphTop - y) + graphHeight;
                //TODO canvas.drawCircle(first_X, first_Y, dataPointsRadius, mPaint);
            }
            lastEndY = orgY;
            lastEndX = orgX;
            i++;
        }

        if (mStyles.drawBackground) {
            // end / close path
            mPathBackground.lineTo((float) lastUsedEndX, graphHeight + graphTop);
            mPathBackground.lineTo(firstX, graphHeight + graphTop);
            mPathBackground.close();
            canvas.drawPath(mPathBackground, mPaintBackground);
        }

    }



    /**
     * the thickness of the line.
     * This option will be ignored if you are
     * using a custom paint via {@link #setCustomPaint(android.graphics.Paint)}
     *
     * @return the thickness of the line
     */
    public int getThickness() {
        return mStyles.thickness;
    }

    /**
     * the thickness of the line.
     * This option will be ignored if you are
     * using a custom paint via {@link #setCustomPaint(android.graphics.Paint)}
     *
     * @param thickness thickness of the line
     */
    public void setThickness(int thickness) {
        mStyles.thickness = thickness;
    }

    /**
     * flag whether the area under the line to the bottom
     * of the viewport will be filled with a
     * specific background color.
     *
     * @return whether the background will be drawn
     * @see #getBackgroundColor()
     */
    public boolean isDrawBackground() {
        return mStyles.drawBackground;
    }

    /**
     * flag whether the area under the line to the bottom
     * of the viewport will be filled with a
     * specific background color.
     *
     * @param drawBackground whether the background will be drawn
     * @see #setBackgroundColor(int)
     */
    public void setDrawBackground(boolean drawBackground) {
        mStyles.drawBackground = drawBackground;
    }

    /**
     * flag whether the data points are highlighted as
     * a visible point.
     *
     * @return flag whether the data points are highlighted
     * @see #setDataPointsRadius(float)
     */
    public boolean isDrawDataPoints() {
        return mStyles.drawDataPoints;
    }

    /**
     * flag whether the data points are highlighted as
     * a visible point.
     *
     * @param drawDataPoints flag whether the data points are highlighted
     * @see #setDataPointsRadius(float)
     */
    public void setDrawDataPoints(boolean drawDataPoints) {
        mStyles.drawDataPoints = drawDataPoints;
    }

    /**
     * @return the radius for the data points.
     * @see #setDrawDataPoints(boolean)
     */
    public float getDataPointsRadius() {
        return mStyles.dataPointsRadius;
    }

    /**
     * @param dataPointsRadius the radius for the data points.
     * @see #setDrawDataPoints(boolean)
     */
    public void setDataPointsRadius(float dataPointsRadius) {
        mStyles.dataPointsRadius = dataPointsRadius;
    }

    /**
     * @return  the background color for the filling under
     *          the line.
     * @see #setDrawBackground(boolean)
     */
    public int getBackgroundColor() {
        return mStyles.backgroundColor;
    }

    /**
     * @param backgroundColor  the background color for the filling under
     *                          the line.
     * @see #setDrawBackground(boolean)
     */
    public void setBackgroundColor(int backgroundColor) {
        mStyles.backgroundColor = backgroundColor;
    }

    /**
     * custom paint that can be used.
     * this will ignore the thickness and color styles.
     *
     * @param customPaint the custom paint to be used for rendering the line
     */
    public void setCustomPaint(Paint customPaint) {
        this.mCustomPaint = customPaint;
    }

    public class DataPointPosition{
        float startX;
        float startY;
        float endX;
        float endY;

        public DataPointPosition(float startX, float startY, float endX, float endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        public float getEndX() {
            return endX;
        }

        public float getEndY() {
            return endY;
        }
    }
}
