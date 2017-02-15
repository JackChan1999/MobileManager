package com.google.mobilesafe.utils;

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
import android.content.SharedPreferences;

public class SPUtils {
	private static final String SP_NAME = "config";
	private static SharedPreferences sp;

	public static boolean getBoolean(String key, boolean defValue) {
		if (sp == null) {
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		}

		return sp.getBoolean(key, defValue);
	}

	public static int getInt(String key, int defValue) {
		if (sp == null) {
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		}

		return sp.getInt(key, defValue);
	}

	public static void putInt(String key, int value) {

		if (sp == null) {
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		}

		sp.edit().putInt(key, value).commit();

	}

	public static void saveBoolean(String key, boolean value) {

		if (sp == null) {
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		}

		sp.edit().putBoolean(key, value).commit();

	}

	// 保存string 类型的数据
	public static void saveString(String key, String value) {
		if (sp == null)
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
	}

	// 获取string 类型的数据
	public static String getString(String key, String defValue) {
		if (sp == null)
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		return sp.getString(key, defValue);
	}

	//删除数据
	public static boolean remove(String key) {
		if (sp == null)
			sp = UIUtils.getContext().getSharedPreferences(SP_NAME, 0);
		return sp.edit().remove(key).commit();
	}
}
