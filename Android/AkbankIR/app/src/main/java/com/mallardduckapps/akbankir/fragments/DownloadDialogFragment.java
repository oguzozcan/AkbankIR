package com.mallardduckapps.akbankir.fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.akbankir.AkbankApp;
import com.mallardduckapps.akbankir.BaseActivity;
import com.mallardduckapps.akbankir.R;
import com.mallardduckapps.akbankir.WebActivity;
import com.mallardduckapps.akbankir.objects.SavedDocumentObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by oguzemreozcan on 18/04/16.
 */
public class DownloadDialogFragment extends DialogFragment {

    private CancelDownloadCallback callback;
    private Activity activity;
    private final String TAG = "DownloadDialog";
    String title;
    String url;
    boolean shouldShownAfterDownload;
    private ProgressBar progressBar;
    TextView fileNameTv;
    TextView statusTextTv;
    long enqueue;
    boolean downloadActive;


    public DownloadDialogFragment() {
    }

    public static DownloadDialogFragment newInstance() {
        return new DownloadDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //callback = (CancelDownloadCallback) getParentFragment();//getTargetFragment();
        } catch (ClassCastException e) {
            Log.d("DownloadDialog", "CLASS CAST EXCEPTION");
            e.printStackTrace();
//            throw new ClassCastException("Calling fragment must implement LogoutCallback interface");
        }

        title = getArguments().getString("title", "");
        url = getArguments().getString("url", "");
        shouldShownAfterDownload = getArguments().getBoolean("shouldShowAfterDownload");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        activity.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (activity != null) {
            activity.unregisterReceiver(receiver);
        }
    }

    private boolean isFilePresent(String title) {
        String path = Environment.getExternalStorageDirectory().toString() + SavedDocumentObject.FOLDER_PATH;//"/akbank_files";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: " + file.length);
        for (int i = 0; i < file.length; i++) {
            Log.d("Files", "FileName:" + file[i].getName());
            if (title.equals(file[i].getName()) ){
                Log.d("Files", "FilePresent filename is" + file[i].getName());
                return true;
            }
        }
        return false;
    }

    private void startFileDownload(String fileName, String title, boolean shouldShownAfterDownload) {
        //progressBarLayout.setVisibility(View.VISIBLE);
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/akbank_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        //listFiles();
        boolean isFilePresent = isFilePresent(title);
        fileNameTv.setText(title);
        String status = shouldShownAfterDownload ? getString(R.string.Opening) : getString(R.string.Downloading);
        statusTextTv.setText(status);

        if(isFilePresent){
            if(shouldShownAfterDownload){
                String path = Environment.getExternalStorageDirectory().toString() + SavedDocumentObject.FOLDER_PATH + "/" + title;
                Intent intentPdf = new Intent(activity, WebActivity.class);
                intentPdf.putExtra("uri", path);
                intentPdf.putExtra("title", title);
                intentPdf.putExtra("type", "pdf");
                activity.startActivity(intentPdf);
            }
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 400);
        } else{
            final DownloadManager dm = (DownloadManager) activity.getSystemService(BaseActivity.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse((AkbankApp.ROOT_URL_1 + fileName)));
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(title)
                    .setDescription(shouldShownAfterDownload ? "view" : "download")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir("/akbank_files", title)
                    .allowScanningByMediaScanner();
            enqueue = dm.enqueue(request);
            downloadActive = true;
            final Timer myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (downloadActive) {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(enqueue);
                            Cursor cursor = dm.query(q);
                            cursor.moveToFirst();

                            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            cursor.close();
                            final int dl_progress = (bytes_downloaded * 100 / bytes_total);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "DOWNLOAD PROGRESS " + dl_progress);
                                    progressBar.setProgress(dl_progress);
                                }
                            });
                        } else {
                            myTimer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10, 500);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loading_pdf_layout, container);
        getDialog().setCancelable(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getActivity().setFinishOnTouchOutside(false);
        fileNameTv = (TextView) rootView.findViewById(R.id.fileName);
        statusTextTv = (TextView) rootView.findViewById(R.id.statusText);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Query query = new DownloadManager.Query();
                DownloadManager dm = ((DownloadManager) activity.getSystemService(BaseActivity.DOWNLOAD_SERVICE));
                Cursor c = dm.query(query);
                dm.remove(enqueue);
                downloadActive = false;
                //String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_ID));
                dismiss();
            }
        });
        startFileDownload(url, title, shouldShownAfterDownload);

        return rootView;
    }

    public interface CancelDownloadCallback {
        void cancelDownload(boolean cancel);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.d(TAG, "Download Completed");

                //progressBarLayout.setVisibility(View.GONE);
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                Log.d(TAG, "Download Completed id: " + downloadId);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = ((DownloadManager) activity.getSystemService(BaseActivity.DOWNLOAD_SERVICE)).query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        String description = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                        String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        //Log.d(TAG, "DOWNLOAD COMPLETED: " + uriString);
                        Log.d(TAG, "DOWNLOAD COMPLETED: " + Uri.parse(uriString));
                        Log.d(TAG, "DOWNLOAD DESCRIPTION: " + description);
                        downloadActive = false;
                        dismiss();

                        if (description.equals("view")) {
                            Intent intentPdf = new Intent(activity, WebActivity.class);
                            intentPdf.putExtra("uri", uriString);
                            intentPdf.putExtra("title", title);
                            intentPdf.putExtra("type", "pdf");
                            activity.startActivity(intentPdf);
                        } else {
                            Log.d(TAG, "ONLY DOWNLOAD: " + description);
                        }

                    }
                    int columnDownloadedBytes = c
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    Log.d(TAG, "DOWNLOADED BYTES: " + c.getInt(columnDownloadedBytes));

                }
            }
        }
    };
}
