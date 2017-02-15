package com.google.mobilesafe.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

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
public class LogManager {
    private static final String TAG = "LogManager";
    private static SimpleDateFormat a = null;
    public static boolean debug = false;
    public static boolean writeFileLog = false;

    private static synchronized SimpleDateFormat a() {
        SimpleDateFormat simpleDateFormat;
        synchronized (LogManager.class) {
            if (a == null) {
                a = new SimpleDateFormat("yyyy.MM.dd");
            }
            simpleDateFormat = a;
        }
        return simpleDateFormat;
    }

    public static void d(String str, String str2) {
        if (debug) {
            Log.d(str, str2);
        }
        writeLogToFile(str, str2);
    }

    public static void d(String str, String str2, Throwable th) {
        if (debug) {
            Log.d(str, str2, th);
        }
        writeLogToFile(str, str2);
    }

    public static void e(String str, String str2) {
        if (debug) {
            Log.e(str, str2);
        }
        writeLogToFile(str, str2);
    }

    /*public static void e(String str, String str2, Throwable th) {
        while (true) {
            if (debug) {
                Log.e(str, str2, th);
            }
            writeLogToFile(str, str2);
            try {
                DuoquUtils.getSdkDoAction().logError(str, str2, th);
                break;
            } catch (Throwable th2) {
                th = th2;
                str = "XIAOYUAN";
                str2 = "e: " + th.getMessage();
            }
        }
    }*/

    public static void i(String str, String str2) {
        if (debug) {
            Log.i(str, str2);
        }
        writeLogToFile(str, str2);
    }

    public static void i(String str, String str2, Throwable th) {
        if (debug) {
            Log.i(str, str2, th);
        }
        writeLogToFile(str, str2);
    }

    public static void ll(String str, String str2) {
        if (debug) {
            Log.d(str, str2);
        }
        writeLogToFile(str, str2);
    }

    public static void w(String str, String str2) {
        if (debug) {
            Log.w(str, str2);
        }
        writeLogToFile(str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        if (debug) {
            Log.w(str, str2, th);
        }
        writeLogToFile(str, str2);
    }

    public static void writeLogToFile(String str, String str2) {
        if (writeFileLog) {
            try {
                SimpleDateFormat a = a();
                synchronized (a) {
                    String format = a.format(Long.valueOf(System.currentTimeMillis()));
                    String str3 = getFilePath() + File.separator + "logs";
                    File file = new File(str3);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    PrintStream printStream = new PrintStream(new FileOutputStream(new StringBuilder(String.valueOf(str3)).append(File.separator).append(format).append(".ll.log").toString(), true));
                    printStream.println(new StringBuilder(String.valueOf(str)).append("  ").append(str2).append(" time=").append(System.currentTimeMillis()).toString());
                    printStream.close();
                }
            } catch (Throwable th) {
                Log.e(TAG,th.getMessage());
            }
        }
    }


    public static String getFilePath() {
        return new StringBuilder(String.valueOf(UIUtils.getContext().getFilesDir().getPath())).append(File.separator).toString();
    }


    public static void writeLogToFile(String str, String str2, String str3) {
        if (writeFileLog) {
            try {
                SimpleDateFormat a = a();
                synchronized (a) {
                    String format = a.format(Long.valueOf(System.currentTimeMillis()));
                    String str4 = getFilePath() + File.separator + "logs";
                    File file = new File(str4);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    PrintStream printStream = new PrintStream(new FileOutputStream(new StringBuilder(String.valueOf(str4)).append(File.separator).append(format).append(str3).toString(), true));
                    printStream.println(new StringBuilder(String.valueOf(str)).append("  ").append(str2).append(" time=").append(System.currentTimeMillis()).toString());
                    printStream.close();
                }
            } catch (Throwable th) {
                Log.e(TAG, th.getMessage());
            }
        }
    }
}
