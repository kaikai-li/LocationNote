package com.lkk.locationnote.common.utils;

import android.content.Context;
import android.text.format.DateUtils;

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

    public static String getFormatTime(Context context, long time) {
        if (DateUtils.isToday(time)) {
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_TIME);
        }

        return DateUtils.formatDateTime(context, time,
                DateUtils.FORMAT_SHOW_YEAR
                    | DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_NUMERIC_DATE);
    }
}
