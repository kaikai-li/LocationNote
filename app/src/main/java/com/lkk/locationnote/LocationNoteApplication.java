package com.lkk.locationnote;

import android.support.multidex.MultiDexApplication;

import com.lkk.locationnote.common.log.CrashExceptionHandler;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.utils.PreferenceUtil;

public class LocationNoteApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.getInstance().init(this);
        Log.init(this);
        CrashExceptionHandler.getInstance().init();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
