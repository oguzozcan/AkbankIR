package com.mallardduckapps.akbankir.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.adapters.AnalystCovarageAdapter;
import com.mallardduckapps.akbankir.objects.AnalystCovarageObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 14/04/16.
 */
public class AnalystCovarageFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
//    private RecyclerView covarageRecyclerView;

    public static AnalystCovarageFragment newInstance(int sectionNumber, ArrayList<AnalystCovarageObject> covarageList) {
        AnalystCovarageFragment fragment = new AnalystCovarageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelableArrayList("COVARAGE_LIST", covarageList);
        fragment.setArguments(args);
        return fragment;
    }

    public AnalystCovarageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analyst_covarage, container, false);
        RecyclerView covarageRecyclerView = (RecyclerView) rootView.findViewById(R.id.analystRecylerView);
        //switcher = (ViewSwitcher) rootView.findViewById(R.id.viewSwitcher);
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        ArrayList<AnalystCovarageObject> covarageList = getArguments().getParcelableArrayList("COVARAGE_LIST");
        if(covarageList == null){
            Log.d("COVARAGE_LIST", "ARRAYLIST IS NULL - Load new data");
        }else{
            //fillData(reviewArrayList);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            covarageRecyclerView.setLayoutManager(mLayoutManager);
            covarageRecyclerView.setAdapter(new AnalystCovarageAdapter(this.getActivity(), covarageList));
        }
        //ReviewsAdapter adapter = new ReviewsAdapter(getActivity(),prepareTestData());
        //commentsListView.setAdapter(adapter);
        return rootView;
    }
}
