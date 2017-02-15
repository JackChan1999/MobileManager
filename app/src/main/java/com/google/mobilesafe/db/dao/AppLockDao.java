package com.google.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.mobilesafe.db.AppLockOpenhelper;

import java.util.ArrayList;
import java.util.List;

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
public class AppLockDao {

	private Context context;

	private AppLockOpenhelper openhelper;

	public AppLockDao(Context context) {
		this.context = context;
		openhelper = new AppLockOpenhelper(context);
	}

	/**
	 * 添加到程序锁里面
	 * 
	 * @param packagename
	 *            包名
	 */
	public void add(String packagename) {
		SQLiteDatabase db = openhelper.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("packagename", packagename);

		db.insert("lock", null, values);
		db.close();
		context.getContentResolver().notifyChange(
				Uri.parse("content://com.qq.mobilesafe.change"), null);
	}

	/**
	 * 从程序锁里面删除当前的包名
	 * 
	 * @param packagename
	 */
	public void delete(String packagename) {
		SQLiteDatabase db = openhelper.getWritableDatabase();

		db.delete("lock", "packagename = ? ", new String[] { packagename });
		db.close();

		context.getContentResolver().notifyChange(
				Uri.parse("content://com.qq.mobilesafe.change"), null);

	}

	/**
	 * 查询当前的包名是否在程序锁里面
	 * @param packagename
	 * @return
	 */
	public String findLock(String packagename) {

		String name = "";
		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db.query("lock", null, "packagename = ?",
				new String[] { packagename }, null, null, null);
		if (cursor.moveToNext()) {
			name = cursor.getString(0);
		}
		return name;
	}

	/**
	 * 查询所有的锁定的包名
	 * 
	 * @return
	 */
	public List<String> findAll() {

		List<String> list = new ArrayList<String>();

		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db.query("lock", new String[] { "packagename" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;

	}

}
