package com.google.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
public class SmsUtils {

	public interface BackUpCallbackSms {
		public void before(int count);

		public void onBackUpSms(int progress);

	}

	public static boolean backUp(Context context, BackUpCallbackSms callback) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms");
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"date", "type", "body" }, null, null, null);
			int count = cursor.getCount();

			callback.before(count);
			int progress = 0;

			try {
				File file = new File(Environment.getExternalStorageDirectory(),
						"backup.xml");
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				XmlSerializer serializer = Xml.newSerializer();
				serializer.setOutput(bos, "utf-8");
				serializer.startDocument("utf-8", true);// 独立文件
				serializer.startTag(null, "smss");
				serializer.attribute(null, "size", String.valueOf(count));

				while (cursor.moveToNext()) {
					serializer.startTag(null, "sms");

					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");

					serializer.startTag(null, "date");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "date");

					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");

					serializer.startTag(null, "body");
					serializer.text(Crypto.encrypt("123", cursor.getString(3)));// 加密
					serializer.endTag(null, "body");

					serializer.endTag(null, "sms");
					callback.onBackUpSms(progress);

				}
				cursor.close();
				serializer.endTag(null, "smsss");
				serializer.endDocument();
				bos.flush();
				bos.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return false;
	}
}
