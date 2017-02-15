package com.google.mobilesafe.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：MobileSafe
 * Package_Name：com.google.mobilesafe
 * Version：1.0
 * time：2016/2/15 22:32
 * des ：手机卫士
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
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
