package com.mallardduckapps.akbankir.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Button;

import com.mallardduckapps.akbankir.R;

/**
 * Created by oguzemreozcan on 05/02/16.
 */
public class IntervalButton extends Button {

    public String tag;
    private int color;
    private boolean buttonSelected;
    private int period;
    private int interval;

    public IntervalButton(Context context) {
        super(context);
    }

    public IntervalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public IntervalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);

    }

    private void initAttributes(AttributeSet attrs){
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.IntervalButton,
                0, 0);

        try {
            tag = a.getString(R.styleable.CompareButton_tag);
            buttonSelected = a.getBoolean(R.styleable.IntervalButton_buttonSelected, false);
            period = a.getInt(R.styleable.IntervalButton_period, 1440);
            interval = a.getInt(R.styleable.IntervalButton_interval, 1);
        } finally {
            a.recycle();
        }
        if(buttonSelected){
            setButtonSelected(buttonSelected);
        }
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }

    public boolean isButtonSelected() {
        return buttonSelected;
    }

    public void setButtonSelected(boolean buttonSelected) {
        this.buttonSelected = buttonSelected;
        if(buttonSelected){
            setTextColor(ContextCompat.getColor(getContext(),R.color.coral_two));
        }else{
            setTextColor(ContextCompat.getColor(getContext(),R.color.warm_grey));
        }
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
