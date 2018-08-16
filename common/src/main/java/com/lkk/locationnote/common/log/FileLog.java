package com.lkk.locationnote.common.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class FileLog implements ILog {

    private HandlerThread mLogThread;
    private Handler mHandler;

    @Override
    public void init() {
        mLogThread = new HandlerThread(this.getClass().getSimpleName());
        mLogThread.start();
        mHandler = new Handler(mLogThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };
    }

    @Override
    public void v(String tag, String msg) {

    }

    @Override
    public void d(String tag, String msg) {

    }

    @Override
    public void i(String tag, String msg) {

    }

    @Override
    public void w(String tag, String msg) {

    }

    @Override
    public void e(String tag, String msg) {

    }
}
