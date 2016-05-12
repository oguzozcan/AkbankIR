package com.akbank.investorrelations.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbank.investorrelations.ItemListActivity;
import com.akbank.investorrelations.R;
import com.akbank.investorrelations.busevents.EventDeviceRegisterResponse;
import com.akbank.investorrelations.busevents.EventPagesRequest;
import com.akbank.investorrelations.busevents.EventPagesResponse;
import com.akbank.investorrelations.objects.ApiErrorEvent;
import com.akbank.investorrelations.objects.PagesObject;
import com.akbank.investorrelations.utils.Constants;
import com.akbank.investorrelations.utils.TimeUtil;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TermsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermsFragment extends BaseFragment {
    private static final String PAGE_PARAM = "PAGE";
    private static final String AUTO_OPEN_PARAM = "automatic";
    private int pageIndex;
    private boolean automatic;
    private TextView textView;
//    private Button confirmButton;
    private RelativeLayout loadingBar;

//    lang” : “langen”
//    “lang” : “ langtr”

    @Override
    protected void setTag() {
        try{
            TAG = "TERMS FRAGMENT";//getString(R.string.Terms_of_Services);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public TermsFragment() {
    }

    public static TermsFragment newInstance(int pageIndex, boolean automatic) {
        TermsFragment fragment = new TermsFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_PARAM, pageIndex);
        args.putBoolean(AUTO_OPEN_PARAM, automatic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(PAGE_PARAM, 1);
            automatic = getArguments().getBoolean(AUTO_OPEN_PARAM, false);
        }
        //setTag();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Log.d(TAG, "SET LOCALE: " + lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        TimeUtil.changeLocale(myLocale);

//        Intent intent = getActivity().getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        getActivity().startActivity(intent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent refresh = new Intent(getActivity(), ItemListActivity.class);
                refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(refresh);
                getActivity().finish();
            }
        }, 150);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        });
    }

    boolean notFirstEntrance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        textView = (TextView) view.findViewById(R.id.termsTv);
        Button confirmButton = (Button) view.findViewById(R.id.confirmButton);
        loadingBar = (RelativeLayout) view.findViewById(R.id.loadingLayout);
        loadingBar.setVisibility(View.VISIBLE);
//        final String android_id = Settings.Secure.getString(getContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//        final String android_id = Utils.getDeviceId(getActivity());
//        Log.d(TAG, "DEVICE ID : " + android_id);
        confirmButton.setText(pageIndex == 1 ? "Onayla" : "Confirm");
//        if(mListener != null){
//            mListener.onTitleTextChange(pageIndex == 1 ? "Kullanım Koşulları" : "Terms of Services");
//        }
//        if(pageIndex == 1){
//            Spanned htmlAsSpanned = Html.fromHtml(Constants.HTML_TERMS_OF_SERVICE_TR);
//            textView.setText(htmlAsSpanned);
//        }

        Locale current = getResources().getConfiguration().locale;
        Log.d(TAG, "CURRENT LOCALE terms fragment: " + current.toString());
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "CONFIRM BUTTTON CLICKED");
                //app.getBus().post(new EventDeviceRegisterRequest(android_id,true, pageIndex == 1 ? "tr": "en"));
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        ds.putString(Constants.SELECTED_LANGUAGE_KEY, pageIndex == 1 ? Constants.TURKISH : Constants.ENGLISH);
                        ds.save();
                        Log.d(TAG, "CALL SET LOCALE: " + pageIndex);
                        setLocale(pageIndex == 1 ? Constants.TURKISH_LOCALE_CODE : Constants.ENGLISH_LOCALE_CODE);
                        //getActivity().finish();
//                    }
//                });
            }
        });
        notFirstEntrance = ds.getBoolean(Constants.FIRST_ENTRANCE_KEY);
        if(!automatic){ // notFirstEntrance
            textView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            app.getBus().post(new EventPagesRequest(pageIndex == 1 ? Constants.TURKISH : Constants.ENGLISH, pageIndex));
        }
        return view;
    }

    @Subscribe
    public void onRegisterDevice(EventDeviceRegisterResponse event){
        Response<String> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: device registered " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body());
        if (response.isSuccessful()){
            Log.d(TAG, "SUCCESS");
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    @Subscribe
    public void onLoadPages(EventPagesResponse event){
        Response<PagesObject> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw() + " - response header: " + response.headers().get("Accept-Language"));
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        loadingBar.setVisibility(View.GONE);
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            PagesObject pagesObject = response.body();
            Spanned htmlAsSpanned = Html.fromHtml(pagesObject.getText());
            textView.setText(htmlAsSpanned);
            //setWebcastsLayout(webcasts.get(0));
            //eventsListView.setHasFixedSize(true);
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }
}
