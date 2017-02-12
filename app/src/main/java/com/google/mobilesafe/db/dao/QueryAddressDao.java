package com.google.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryAddressDao {
	// 注意,该路径必须是data/data目录的文件,否则数据库访问不到
	// private static final String PATH =
	// "data/data/com.qq.mobilesafe/files/address.db";

	public static String getAddress(Context context, String number) {
		String address = null;

		// 获取数据库对象
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				context.getFilesDir() + "/address.db", null,
				SQLiteDatabase.OPEN_READONLY);

		// 手机号码特点: 1 + (3,4,5,6,7,8) + (9位数字)
		// 正则表达式
		// ^1[3-8]\d{9}$
		// "^1[34578]\\d{9}$"
		// "^1[3-8]\\d{9}$"

		if (number.matches("^1[34578]\\d{9}$")) {// 匹配手机号码

			// String sub_phone = number.substring(0, 7);

			Cursor cursor = database.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });

			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}

			cursor.close();
		} else if (number.matches("^\\d+$")) {// 匹配数字
			switch (number.length()) {
			case 3:
				if ("110".equals(number)) {
					address = "匪警";
				} else if ("120".equals(number)) {
					address = "急救中心";
				} else if ("119".equals(number)) {
					address = "匪警";
				}
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				if ("10000".equals(number)) {
					address = "中国电信";
				} else if ("10086".equals(number)) {
					address = "中国移动";
				} else if ("10010".equals(number)) {
					address = "中国联通";
				}
				break;
			case 7:
			case 8:
				address = "本地电话";
				break;
			default:
				// 01088881234
				// 048388888888
				if (number.startsWith("0") && number.length() > 10) {// 有可能是长途电话
					// 有些区号是4位,有些区号是3位(包括0)

					// 先查询4位区号
					Cursor cursor = database.rawQuery(
							"select location from data2 where area =?",
							new String[] { number.substring(1, 4) });

					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {
						cursor.close();

						// 查询3位区号
						cursor = database.rawQuery(
								"select location from data2 where area =?",
								new String[] { number.substring(1, 3) });

						if (cursor.moveToNext()) {
							address = cursor.getString(0);
						}

						cursor.close();
					}
				}
				break;
			}
		}

		database.close();// 关闭数据库
		return address;
	}
}
