package com.mallardduckapps.akbankir.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.R;

/**
 * Created by oguzemreozcan on 30/01/16.
 */
public class CompareButton extends LinearLayout {

    public String tag;
    public String text;
    public float textSize;
    private int color;
    private boolean buttonSelected;

    public CompareButton(Context context) {
        super(context);
    }

    public CompareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CompareButton,
                0, 0);

        try {
            color = a.getColor(R.styleable.CompareButton_underline_color, Color.WHITE);
            tag = a.getString(R.styleable.CompareButton_tag);
            text = a.getString(R.styleable.CompareButton_text);
            textSize = a.getDimension(R.styleable.CompareButton_textSize,12);
        } finally {
            a.recycle();
        }
        init();
    }

    public CompareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CompareButton,
                0, 0);

        try {
            color = a.getColor(R.styleable.CompareButton_underline_color, Color.WHITE);
            tag = a.getString(R.styleable.CompareButton_tag);
            text = a.getString(R.styleable.CompareButton_text);
            textSize = a.getDimension(R.styleable.CompareButton_textSize, 12);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init(){
        setOrientation(VERTICAL);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(textSize / getResources().getDisplayMetrics().density);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textView);
        View view = new View(getContext());
        view.setBackgroundColor(color);
        view.setVisibility(INVISIBLE);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        LayoutParams  layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, size);
        view.setLayoutParams(layoutParams1);
        addView(view);
    }

    public boolean isButtonSelected() {
        return buttonSelected;
    }

    public void reverseSelection(){
        if (!buttonSelected){
            buttonSelected = true;
            getChildAt(1).setVisibility(View.VISIBLE);
            //setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else{
            buttonSelected = false;
            getChildAt(1).setVisibility(View.INVISIBLE);
            //setPaintFlags(Paint.LINEAR_TEXT_FLAG);
        }
    }

    public int getColor() {
        return color;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
