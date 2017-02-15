package com.google.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
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
public class AntivirusDao {

	public static boolean isVirus(Context context, String md5) {
		boolean result = false;
		String path = new File(context.getFilesDir(), "antivirus.db")
				.getAbsolutePath();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable  where md5 = ?", new String[]{md5});
		
		while (cursor.moveToNext()) {
			return true ;
		}

		return result ;
	}

	public static String checkFilevirus(String MD5) {
		String desc = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.qq.mobilesafe/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);

		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?",
				new String[] { MD5 });

		while (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		return desc;

	}

	public static void addVirus(String md5, String desc) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.itheima.mobileguard/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);

		ContentValues values = new ContentValues();
		values.put("md5", md5);
		values.put("desc", desc);
		values.put("type", 6);

		values.put("name", "Android.Troj.AirAD.a");

		db.insert("datable", null, values);

	}
}
