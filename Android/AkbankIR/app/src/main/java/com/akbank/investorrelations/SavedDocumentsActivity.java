package com.akbank.investorrelations;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.akbank.investorrelations.adapters.SavedDocumentsAdapter;
import com.akbank.investorrelations.objects.SavedDocumentObject;

import java.io.File;
import java.util.ArrayList;

public class SavedDocumentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_saved_documents, null, false);
        mContent.addView(contentView, 0);
        RecyclerView reportsList = (RecyclerView) findViewById(R.id.reportsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        reportsList.setLayoutManager(mLayoutManager);
        reportsList.setAdapter(new SavedDocumentsAdapter(this, listFiles()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTitleTextChange(getString(R.string.Menu_SavedDocuments));
    }

    private ArrayList<SavedDocumentObject> listFiles(){
        String path = Environment.getExternalStorageDirectory().toString() + SavedDocumentObject.FOLDER_PATH;
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        ArrayList<SavedDocumentObject> savedDocuments = new ArrayList<>();
        for (int i=0; i < file.length; i++) {
            Log.d("Files", "FileName:" + file[i].getName());
            savedDocuments.add(new SavedDocumentObject(file[i].getName()));
        }
        return savedDocuments;
    }

    @Override
    protected void setTag() {
        TAG = "SavedDocumentsActivity";
    }
}
