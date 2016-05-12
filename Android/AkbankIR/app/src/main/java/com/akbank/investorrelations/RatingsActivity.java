package com.akbank.investorrelations;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.akbank.investorrelations.adapters.RatingsGridViewAdapter;

import java.util.ArrayList;

public class RatingsActivity extends BaseActivity {

    View contentView;
    RecyclerView ratingsGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_ratings, null, false);
        mContent.addView(contentView, 0);
        ratingsGridView = (RecyclerView) findViewById(R.id.ratingsGridView);

        ArrayList<String> ratingsArray = getIntent().getStringArrayListExtra("ratings");
//        ArrayList<Rating> ratings = new ArrayList<>();
//        try{
//            ratings = getIntent().getParcelableArrayListExtra("ratings");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        ArrayList<String> ratingsArray = new ArrayList();
//        ratingsArray.add("");
//        ratingsArray.add("Moody's");
//        ratingsArray.add("Fitch");
//        for (int i = 0; i < ratings.size(); i++) {
//            //Log.d(TAG, "RATING: " + ratings.get(i).getTitle());
//            ratingsArray.add(ratings.get(i).getTitle());
//            ratingsArray.add(ratings.get(i).getMoody());
//            ratingsArray.add(ratings.get(i).getFitch());
//        }
        //StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        GridLayoutManager lm = new GridLayoutManager(this, 3);
        lm.setSpanCount(3);
        ratingsGridView.setLayoutManager(lm);
        ratingsGridView.setAdapter(new RatingsGridViewAdapter(this, ratingsArray));

    }

    @Override
    protected void setTag() {
        TAG = "RatingsActivity";
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        app.getBus().register(this);
//        app.getBus().post(new EventSustainabilityReportsRequest(ds.getLangString(Constants.SELECTED_LANGUAGE_KEY)));
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        app.getBus().unregister(this);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.DashboardHeader_Ratings));
    }
}
