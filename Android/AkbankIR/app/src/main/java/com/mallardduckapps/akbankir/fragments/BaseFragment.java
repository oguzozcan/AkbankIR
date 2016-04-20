package com.mallardduckapps.akbankir.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mallardduckapps.akbankir.AkbankApp;

/**
 * Created by oguzemreozcan on 03/03/16.
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG = "BaseFragment";
    public AkbankApp app;
    public boolean attached;
    public OnFragmentInteractionListener mListener;


    public BaseFragment() {
        setTag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (AkbankApp) getActivity().getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        app.getBus().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attached = true;
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if(mListener != null){
            mListener.onTitleTextChange(TAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        app.getBus().unregister(this);
    }

    @Override
    public void onDetach() {
        attached = false;
        super.onDetach();
    }

    public void showMessage(final String message, final int length) {
        if (attached) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), message, length).show();
                }
            });
        }
    }

//    @Subscribe
//    public void onLoadingError(final EventBusLoadingError eventBusLoadingError) {
//        if(attached){
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    int statusCode = eventBusLoadingError.getStatusCode();
//                    switch (statusCode) {
//                        case 200:
//                        case 201:
//                        case 204:
//                        case 304:
//                            return;
//                        case 306:
//                            Log.d(TAG, "UNSUPPORTED API");
//                            DialogNoSupport dialogNoSupport = new DialogNoSupport();
//                            //dialogNoSupport.setTargetFragment(BasicFragment.this, Constants.NO_SUPPORT);
//                            dialogNoSupport.show(getChildFragmentManager(), "noSupportDialog");
//                            break;
//                        default:
//                            Log.d(TAG, "BF - ON LOADING ERROR " + statusCode);
//                            BasicActivity.showMessage(getActivity(), statusCode);
//                            break;
//                    }
//                }
//            });
//        }
//    }
//
    protected abstract void setTag();

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String name, int pathId, int id, int color);

        void onTitleTextChange(String text);

//        void displayBottomBar(boolean display);
//
//        void displayTopBar(boolean display);
//
//        void onToolbarColorChange(int color);
//
//        void onTitleColorChange(int color);
//
//        void onToolbarIconEnabled(int imageResource);
//
//        void onToolbarHomeButtonChange(int imageResource);
//
//        void changeTab(int tabIndex);
//
//        void initNotificationView(boolean visible, int badgeCount);
    }
}
