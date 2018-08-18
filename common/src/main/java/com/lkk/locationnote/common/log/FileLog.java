package com.lkk.locationnote.common.log;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.lkk.locationnote.common.utils.PreferenceUtil;
import com.lkk.locationnote.common.utils.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLog implements ILog {

    private static final int LOG_FILE_SIZE = 5 * 1024 * 1024;
    private static final int MSG_WHAT_INIT = 0x300;
    private static final int MSG_WHAT_LOG = 0x301;
    private static final String LOG_MASSAGE = "LOG_MASSAGE";

    private HandlerThread mLogThread;
    private Handler mHandler;
    private Context mContext;
    // TODO writer is not closed
    private BufferedWriter mWriter;
    private String mProcessName;

    public FileLog(Application application) {
        mContext = application.getApplicationContext();
        mProcessName = mContext.getPackageName();
        mLogThread = new HandlerThread(this.getClass().getSimpleName());
        mLogThread.start();
        mHandler = new Handler(mLogThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_WHAT_INIT:
                        init();
                        break;
                    case MSG_WHAT_LOG:
                        try {
                            mWriter.write(msg.getData().getString(LOG_MASSAGE));
                            mWriter.newLine();
                            mWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        mHandler.sendEmptyMessage(MSG_WHAT_INIT);
    }

    private void init() {
        String logsDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/logs";
        File logsDirFile = new File(logsDir);
        if (!logsDirFile.exists()) {
            logsDirFile.mkdirs();
        }
        String logName = PreferenceUtil.getInstance().getLogName();
        if (TextUtils.isEmpty(logName)) {
            logName = Util.getLatestLogName();
            PreferenceUtil.getInstance().saveLogName(logName);
        }
        File logFile = new File(logsDir, logName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (logFile.length() >= LOG_FILE_SIZE) {
            String newLogName = Util.getLatestLogName();
            PreferenceUtil.getInstance().saveLogName(newLogName);
            logFile = new File(logsDir, newLogName);
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            mWriter = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile(),
                    true), 2048);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void v(String tag, String msg) {
        logMessage(Level.VERBOSE, tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        logMessage(Level.DEBUG, tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        logMessage(Level.INFO, tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        logMessage(Level.WARN, tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        logMessage(Level.ERROR, tag, msg);
    }

    private void logMessage(Level level, String tag, String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Util.formatTime(System.currentTimeMillis()));
        stringBuffer.append("|" + mProcessName);
        switch (level) {
            case VERBOSE:
                stringBuffer.append("|[V]|");
                break;
            case DEBUG:
                stringBuffer.append("|[D]|");
                break;
            case INFO:
                stringBuffer.append("|[I]|");
                break;
            case WARN:
                stringBuffer.append("|[W]|");
                break;
            case ERROR:
                stringBuffer.append("|[E]|");
                break;
        }
        stringBuffer.append(tag + "|");
        stringBuffer.append(msg);
        Message message = Message.obtain();
        message.what = MSG_WHAT_LOG;
        Bundle bundle = new Bundle(1);
        bundle.putString(LOG_MASSAGE, stringBuffer.toString());
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private enum Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}
