package com.google.mobilesafe.db.dao;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
