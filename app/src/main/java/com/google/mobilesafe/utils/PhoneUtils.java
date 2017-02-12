package com.google.mobilesafe.utils;

import android.content.Context;
import android.os.Build;
import android.os.UserManager;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/7/12 10:44
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class PhoneUtils {
    private static final String CLASS_NAME_BUILD_EXT = "android.os.BuildExt";
    private static final String CLASS_NAME_CONTEXT_EXT = "android.content.ContextExt";
    public static final String TAG = "PhoneUtils";
    private static Boolean sIsFlymeRom;
    private static Boolean sIsGuestMode;
    private static Boolean sIsPhoneRooted;
    private static Boolean sIsProductInternational;
    private static String sPhoneIMEI;
    private static String sPhoneModel;
    private static String sPhoneSn;

    private PhoneUtils() {
        throw new AssertionError();
    }

    public static String checkAndNormalizePhoneNum(Context context, String str) {
        if (str != null && str.contains(StringUtils.MPLUG86)) {
            str = str.replace(StringUtils.MPLUG86, "");
        }
        if (TextUtils.isEmpty(str) || TextUtils.isDigitsOnly(str)) {
            return str;
        }
        Toast.makeText(context, "手机号码不符合规范", 0).show();
        return "";
    }

    public static synchronized String getPhoneModel() {
        String str;
        synchronized (PhoneUtils.class) {
            if (TextUtils.isEmpty(sPhoneModel)) {
                if (isFlymeRom()) {
                    sPhoneModel = Build.MODEL;
                } else {
                    try {
                        sPhoneModel = (String) ReflectHelper.getStaticField(CLASS_NAME_BUILD_EXT, "MZ_MODEL");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (TextUtils.isEmpty(sPhoneModel) || sPhoneModel.toLowerCase().equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
                    Log.e(TAG, "get Mz Phone Model returns null or UNKNOWN");
                    sPhoneModel = Build.MODEL;
                }
            }
            str = sPhoneModel;
        }
        return str;
    }

    public static synchronized String getPhoneImei(Context context) {
        String str;
        synchronized (PhoneUtils.class) {
            if (TextUtils.isEmpty(sPhoneIMEI)) {
                try {
                    str = "android.telephony.MzTelephonyManager";
                    str = "getDeviceId";
                    sPhoneIMEI = (String) ReflectHelper.invokeStatic("android.telephony.MzTelephonyManager", "getDeviceId", null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(sPhoneIMEI)) {
                    sPhoneIMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                }
            }
            Log.e(TAG, "Get Mz Phone IMEI " + sPhoneIMEI);
            if (sPhoneIMEI == null) {
                sPhoneIMEI = "";
            }
            str = sPhoneIMEI;
        }
        return str;
    }

    public static synchronized boolean isFlymeRom() {
        boolean booleanValue;
        synchronized (PhoneUtils.class) {
            if (sIsFlymeRom != null) {
                booleanValue = sIsFlymeRom.booleanValue();
            } else {
                try {
                    sIsFlymeRom = ((Boolean) ReflectHelper.invokeStatic(CLASS_NAME_BUILD_EXT, "isFlymeRom", null)).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
                    booleanValue = sIsFlymeRom.booleanValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    booleanValue = false;
                }
            }
        }
        return booleanValue;
    }

    public static synchronized boolean isProductInternational() {
        boolean booleanValue;
        synchronized (PhoneUtils.class) {
            if (sIsProductInternational != null) {
                booleanValue = sIsProductInternational.booleanValue();
            } else {
                try {
                    sIsProductInternational = ((Boolean) ReflectHelper.invokeStatic(CLASS_NAME_BUILD_EXT, "isProductInternational", null)).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
                    booleanValue = sIsProductInternational.booleanValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    booleanValue = false;
                }
            }
        }
        return booleanValue;
    }

   /* public static synchronized boolean isPhoneRooted(Context context) {
        boolean booleanValue;
        synchronized (PhoneUtils.class) {
            if (sIsPhoneRooted != null) {
                booleanValue = sIsPhoneRooted.booleanValue();
            } else {
                Object obj;
                String str = "";
                try {
                    obj = (String) ReflectHelper.getStaticField(CLASS_NAME_CONTEXT_EXT, "DEVICE_STATE_SERVICE");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    String str2 = str;
                }
                if (TextUtils.isEmpty(obj)) {
                    booleanValue = false;
                } else {
                    obj = context.getSystemService(obj);
                    if (obj != null) {
                        try {
                            sIsPhoneRooted = ((Integer) ReflectHelper.invoke(obj, "doCheckState", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(1)})).intValue() == 1 ? Boolean.TRUE : Boolean.FALSE;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    booleanValue = sIsPhoneRooted.booleanValue();
                }
            }
        }
        return booleanValue;
    }*/

    public static synchronized boolean isGuestModeEnabled(Context context) {
        boolean booleanValue;
        synchronized (PhoneUtils.class) {
            if (sIsGuestMode != null) {
                booleanValue = sIsGuestMode.booleanValue();
            } else {
                try {
                    sIsGuestMode = ((Boolean) ReflectHelper.invoke((UserManager) context.getSystemService("user"), "isGuestUser", null)).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
                    booleanValue = sIsGuestMode.booleanValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    booleanValue = false;
                }
            }
        }
        return booleanValue;
    }
}
