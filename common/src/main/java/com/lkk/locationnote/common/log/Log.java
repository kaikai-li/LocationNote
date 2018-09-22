package com.lkk.locationnote.common.log;

import android.app.Application;

import com.lkk.locationnote.common.BuildConfig;

public class Log {

    private static final boolean DEBUGGABLE = BuildConfig.DEBUG;

    private static ILog mFileLog;
    private static ILog mLocatLog;

    public static void init(Application application) {
        mFileLog = new FileLog(application);
        mLocatLog = new LogcatLog();
    }

    public static void v(String tag, String msg) {
        mLocatLog.v(tag, msg);
        if (DEBUGGABLE) {
            mFileLog.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        mLocatLog.d(tag, msg);
        if (DEBUGGABLE) {
            mFileLog.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        mLocatLog.i(tag, msg);
        if (DEBUGGABLE) {
            mFileLog.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        mLocatLog.w(tag, msg);
        if (DEBUGGABLE) {
            mFileLog.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        mLocatLog.e(tag, msg);
        if (DEBUGGABLE) {
            mFileLog.e(tag, msg);
        }
    }
}
