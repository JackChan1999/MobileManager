package com.google.mobilesafe.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final long ONE_DAY_TIME = 86400000;

    public static String addDays(String time, String pattern, int days) {
        return getTimeString(pattern, getTime(time, pattern) + (86400000 * ((long) days)));
    }

    public static boolean compareDateString(String date1, String date2, String pattern) {
        return getTime(date1, pattern) > getTime(date2, pattern);
    }

    public static String getCurrentTimeString(String pattern) {
        String currentTime = "";
        try {
            currentTime = new SimpleDateFormat(pattern).format(new Date(System.currentTimeMillis()));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return currentTime;
    }

    public static long getTime(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(time).getTime();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getTimeString(String pattern, long milliseconds) {
        String time = "";
        try {
            time = new SimpleDateFormat(pattern).format(new Date(milliseconds));
        } catch (Throwable th) {
           th.printStackTrace();
        }
        return time;
    }
}
