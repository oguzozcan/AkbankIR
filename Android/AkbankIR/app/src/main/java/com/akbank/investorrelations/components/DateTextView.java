package com.akbank.investorrelations.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akbank.investorrelations.utils.TimeUtil;
import com.android.datetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.Calendar;

import com.akbank.investorrelations.R;

/**
 * Created by oguzemreozcan on 01/02/16.
 */
public class DateTextView extends LinearLayout implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private DateTime dateTime;
    private TextView textView;
    private Activity activity;
    private String tag;
    private boolean isStartDate;

    public DateTextView(Context context) {
        super(context);
        tag = "DateTextView";
        initTextView();
        init();
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyledAttributes(context, attrs);
        initTextView();
        init();
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyledAttributes(context, attrs);
        initTextView();
        init();
    }

    public void initTextView(){
        textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        //textView.setTypeface(ArmutUtils.loadFont(getContext().getAssets(), getContext().getString(R.string.font_pera_bold)));
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int drawablePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        params.bottomMargin = margin;
        params.topMargin = margin;
        //setTextSize(fontSize);
        textView.setGravity(Gravity.LEFT);
        textView.setLayoutParams(params);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.calendar_dashbard_mini_icon), null);
        textView.setCompoundDrawablePadding(drawablePadding);
        addView(textView);
    }

    private void initStyledAttributes(Context context, AttributeSet attrs){
        tag = "DateTextView";
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DateTextView,
                0, 0);
        try {
            isStartDate = a.getBoolean(R.styleable.DateTextView_isStartDate, true);
            tag = a.getString(R.styleable.CompareButton_tag);
        } finally {
            a.recycle();
        }
        this.setClickable(true);
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
    }

    private void init(){
        String dateText = "";
        if(textView.getText().toString().equals("")){
            if(isStartDate ){
                //TODO control it was -1
                dateText = TimeUtil.getDateBeforeOrAfterToday(0, true, false);
            }else{
                dateText = TimeUtil.getTodayJoda(true, false);
            }
        }else{
            dateText = textView.getText().toString();
        }
        Log.d("DATETEXTVIEW", "DATE TEXT init: " + dateText);
        dateTime = TimeUtil.getDateTime(dateText, true, false);
        textView.setText(dateText);
        this.setOnClickListener(this);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public String getDateText(){
        return textView.getText().toString();
    }

    public void setDateText(String text){
        textView.setText(text);
        dateTime = TimeUtil.getDateTime(text, true, false);
    }

    public TextView getTextView(){
        return textView;
    }

    public String getFormatConvertedDateText(){
        String date = textView.getText().toString();
        return TimeUtil.getDateTxtForForex(date);
    }

    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Log.d("DATETEXTVIEW", "DATE TEXT: " + textView.getText().toString());
        dateTime = TimeUtil.getDateTime(textView.getText().toString(), true, false);
        DatePickerDialog datePicker = DatePickerDialog.newInstance(DateTextView.this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth() ); // + 2
        datePicker.setMaxDate(calendar);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.YEAR, -1);
        datePicker.setMinDate(calendar1);
        datePicker.show(activity.getFragmentManager(), "datePicker");
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateText = "" + dayOfMonth + "." + (monthOfYear + 1) + "." + year;
        String dateTextFormatted = TimeUtil.convertSimpleDateToReadableForm(dateText, false);
        textView.setText(dateTextFormatted);
        dateTime = TimeUtil.getDateTime(dateTextFormatted, true, false);
    }
}
