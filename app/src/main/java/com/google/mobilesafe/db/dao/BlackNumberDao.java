package com.google.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.mobilesafe.db.BlackNumberOpenHelper;
import com.google.mobilesafe.domain.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumberDao {

	private BlackNumberOpenHelper openhelper;

	public BlackNumberDao(Context context) {
		openhelper = new BlackNumberOpenHelper(context);
	}

	public boolean add(String number, String mode) {

		SQLiteDatabase db = openhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		long rawid = db.insert("blacknumber", null, values);
		if (rawid == -1) {
			return false;
		} else {
			return true;
		}
	}

	public boolean delete(String number) {
		SQLiteDatabase db = openhelper.getWritableDatabase();
		int rownumber = db.delete("blacknumber", "number = ?",
				new String[] { number });
		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean changeBlackMode(String number, String newmode) {
		SQLiteDatabase db = openhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		int rownumber = db.update("blacknumber", values, "number = ?",
				new String[] { number });

		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	public String findBlackMode(String number) {
		String mode = "0";
		SQLiteDatabase db = openhelper.getWritableDatabase();

		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"number = ?", new String[] { number }, null, null, null);

		while (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}

		cursor.close();
		db.close();
		return mode;
	}

	
	/**
	 * 查询所有黑名单
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db
				.query("blacknumber", new String[] { "number", "mode" }, null,
						null, null, null, null);
		List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();

		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			info.number = cursor.getString(0);
			info.mode = cursor.getString(1);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 分页加载
	 * 
	 * @param pagenumber
	 *            第几页
	 * @param pagesize
	 *            每一页有多少条数据
	 * @return
	 */
	public List<BlackNumberInfo> findPart(int pagenumber, int pagesize) {
		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from blacknumber limit ? offset?",
				new String[] { String.valueOf(pagesize),
						String.valueOf(pagesize * pagenumber) });
		List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			info.number = cursor.getString(0);
			info.mode = cursor.getString(1);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * 分批加载
	 * @param startIndex
	 * @param maxCount
	 * @return
	 */
	public List<BlackNumberInfo> findPart2(int startIndex, int maxCount) {

		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from blacknumber order by _id desc limit ? offset ?",
				new String[] { String.valueOf(maxCount),
						String.valueOf(startIndex) });
		List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			info.number = cursor.getString(0);
			info.mode = cursor.getString(1);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list ;

	}
	
	public int getCount(){
		SQLiteDatabase db = openhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int count  = cursor.getInt(0);
		cursor.close();
		db.close();
		return count ;
	}

}
