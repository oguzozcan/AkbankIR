package com.mallardduckapps.akbankir.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.busevents.EventDeviceRegisterRequest;
import com.mallardduckapps.akbankir.busevents.EventDeviceRegisterResponse;
import com.mallardduckapps.akbankir.busevents.EventPagesRequest;
import com.mallardduckapps.akbankir.busevents.EventPagesResponse;
import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.objects.PagesObject;
import com.mallardduckapps.akbankir.utils.Constants;
import com.mallardduckapps.akbankir.utils.DataSaver;
import com.mallardduckapps.akbankir.utils.Utils;
import com.squareup.otto.Subscribe;

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
    private int pageIndex;
    private TextView textView;
//    private Button confirmButton;
    private RelativeLayout loadingBar;

//    lang” : “langen”
//    “lang” : “ langtr”

    @Override
    protected void setTag() {
        try{
            TAG = "";//getString(R.string.Terms_of_Services);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public TermsFragment() {
    }

    public static TermsFragment newInstance(int pageIndex) {
        TermsFragment fragment = new TermsFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_PARAM, pageIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(PAGE_PARAM, 1);
        }
        //setTag();
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
        final String android_id = Utils.getDeviceId(getActivity());
        Log.d(TAG, "DEVICE ID : " + android_id);
        confirmButton.setText(pageIndex == 1 ? "Onayla" : "Confirm");
        if(mListener != null){
            mListener.onTitleTextChange(pageIndex == 1 ? "Kullanım Koşulları" : "Terms of Services");
        }
        if(pageIndex == 1){
            Spanned htmlAsSpanned = Html.fromHtml(Constants.HTML_TERMS_OF_SERVICE_TR);
            textView.setText(htmlAsSpanned);
        }
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //app.getBus().post(new EventDeviceRegisterRequest(android_id,true, pageIndex == 1 ? "tr": "en"));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });
            }
        });


        DataSaver ds = app.getDataSaver();
        notFirstEntrance = ds.getBoolean("N_FIRST_ENTRANCE");
        if(notFirstEntrance){
            textView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            app.getBus().post(new EventPagesRequest(pageIndex));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Subscribe
    public void onRegisterDevice(EventDeviceRegisterResponse event){
        Response<String> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: device registered " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        if (response.isSuccessful()){
            Log.d(TAG, "SUCCESS");
        }else{
            app.getBus().post(new ApiErrorEvent(response.code(), response.message(),true));
        }
    }

    @Subscribe
    public void onLoadPages(EventPagesResponse event){
        Response<PagesObject> response = event.getResponse();
        Log.d(TAG, "ON RESPONSE - responsecode: " + response.code() + " - response:" + response.raw());
        Log.d(TAG, "RESPONSE : " + response.body().toString());
        loadingBar.setVisibility(View.GONE);
        //loadingLayout.setVisibility(View.GONE);
        if (response.isSuccessful() && pageIndex == 2) {
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