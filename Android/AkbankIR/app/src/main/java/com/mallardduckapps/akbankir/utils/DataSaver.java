package com.mallardduckapps.akbankir.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by oguzemreozcan on 20/04/16.
 */

public class DataSaver {

    private final SharedPreferences saveArea;
    private final SharedPreferences.Editor saveAreaEditor;
    //final Context context;
//	private static final DataSaver instance = new DataSaver();

    public DataSaver(final Context context, final String saveAreaName, final boolean clearOnLoad) {
        saveArea = context.getSharedPreferences(saveAreaName, Context.MODE_PRIVATE);
        saveAreaEditor = saveArea.edit();
        //this.context = context;
        if (clearOnLoad) {
            clear();
            save();
        }
    }

//	public static DataSaver getInstance() {
//		    return instance;
//	}

    public Map<String, ?> getAll() {
        return saveArea.getAll();
    }

    public int getInt(final String key) {
        return saveArea.getInt(key, 0);
    }

    public String getLangString(final String key) {
        return saveArea.getString(key, "en");
    }

    public String getString(final String key) {
        return saveArea.getString(key, "");
    }

    public float getFloat(final String key) {
        return saveArea.getFloat(key, 0f);
    }

    public long getLong(final String key) {
        return saveArea.getLong(key, 0);
    }

    public boolean getBoolean(final String key) {
        return saveArea.getBoolean(key, false);
    }

    public void putInt(final String key, final int value) {
        saveAreaEditor.putInt(key, value);
    }

    public void putString(final String key, final String value) {
        saveAreaEditor.putString(key, value);
    }

    public void putFloat(final String key, final float value) {
        saveAreaEditor.putFloat(key, value);
    }

    public void putLong(final String key, final long value) {
        saveAreaEditor.putLong(key, value);
    }

    public void putBoolean(final String key, final boolean value) {
        saveAreaEditor.putBoolean(key, value);
    }

    public void remove(final String key) {
        saveAreaEditor.remove(key);
    }

    private void clear() {
        saveAreaEditor.clear();
    }

    public boolean save() {
        return saveAreaEditor.commit();
    }

}

