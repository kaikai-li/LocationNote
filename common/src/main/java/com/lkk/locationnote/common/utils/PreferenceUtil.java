package com.lkk.locationnote.common.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    private static final String PREFERENCE_NAME = "LocationNote";
    private static final String LOG_NAME = "LOG_NAME";

    private static volatile PreferenceUtil mInstance;
    private SharedPreferences mPreferences;

    private PreferenceUtil() {}

    public static PreferenceUtil getInstance() {
        if (mInstance == null) {
            synchronized (PreferenceUtil.class) {
                if (mInstance == null) {
                    mInstance = new PreferenceUtil();
                }
            }
        }

        return mInstance;
    }

    public void init(Application application) {
        mPreferences = application.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveLogName(String logName) {
        mPreferences.edit().putString(LOG_NAME, logName).commit();
    }

    public String getLogName() {
        return mPreferences.getString(LOG_NAME, null);
    }
}
