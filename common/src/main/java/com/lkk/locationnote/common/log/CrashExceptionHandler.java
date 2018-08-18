package com.lkk.locationnote.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static CrashExceptionHandler mInstance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashExceptionHandler() {}

    public static CrashExceptionHandler getInstance() {
        if (mInstance == null) {
            mInstance = new CrashExceptionHandler();
        }

        return mInstance;
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        StringBuffer err = new StringBuffer();
        err.append(throwable.getClass().getName());
        err.append(": ");
        err.append(throwable.getMessage());
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        err.append("\n");
        err.append(writer.toString());
        Log.e(getClass().getSimpleName(), err.toString());
        mDefaultHandler.uncaughtException(thread, throwable);
    }
}
