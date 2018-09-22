package com.lkk.locationnote;

import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lkk.locationnote.common.log.CrashExceptionHandler;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.utils.PreferenceUtil;

public class LocationNoteApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        PreferenceUtil.getInstance().init(this);
        Log.init(this);
        CrashExceptionHandler.getInstance().init();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
