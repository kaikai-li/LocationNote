package com.lkk.locationnote.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static String formatTime(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static String getLatestLogName() {
        return "log-" + formatTime(System.currentTimeMillis()) + ".txt";
    }
}
