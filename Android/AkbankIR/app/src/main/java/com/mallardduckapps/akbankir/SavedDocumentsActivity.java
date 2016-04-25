package com.mallardduckapps.akbankir;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.mallardduckapps.akbankir.adapters.ReportsAdapter;
import com.mallardduckapps.akbankir.adapters.SavedDocumentsAdapter;
import com.mallardduckapps.akbankir.objects.SavedDocumentObject;

import java.io.File;
import java.util.ArrayList;

public class SavedDocumentsActivity extends BaseActivity {

    private View contentView;
    private RecyclerView reportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_saved_documents, null, false);
        mContent.addView(contentView, 0);
        reportsList = (RecyclerView) findViewById(R.id.reportsList);
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
