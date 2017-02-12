package com.google.mobilesafe.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/7/12 10:49
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class LogManager {
    private final String TAG = "LogManager";
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
