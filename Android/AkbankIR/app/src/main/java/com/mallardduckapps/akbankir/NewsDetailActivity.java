package com.mallardduckapps.akbankir;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class NewsDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_news_detail, null, false);
        mContent.addView(contentView, 0);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        TextView titleTv = (TextView) contentView.findViewById(R.id.newsDetailTitle);
        TextView subtitleTv = (TextView) contentView.findViewById(R.id.newsDetailSubtitle);
        Spanned htmlAsSpanned = Html.fromHtml(description);
        titleTv.setText(title);
        subtitleTv.setText(htmlAsSpanned);
        onTitleTextChange(title);
    }

    @Override
    protected void setTag() {
        TAG = "NewsDetailActivity";
    }
}