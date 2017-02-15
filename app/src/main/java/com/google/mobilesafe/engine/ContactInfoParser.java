package com.google.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.mobilesafe.domain.ContactInfo1;
import com.google.mobilesafe.domain.ContactInfo2;
import com.google.mobilesafe.utils.PinyinUtils;

import java.io.InputStream;
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
public class ContactInfoParser {
	private static final String RAW_CONTACTS = "content://com.android.contacts/raw_contacts";
	private static final String DATA_URI = "content://com.android.contacts/data";

	/**获取联系人头像*/
	public static Bitmap getBitmap(Context context, long contactId) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId + "");
		InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(
				contentResolver, uri);
		return BitmapFactory.decodeStream(is);
	}

	/**获取联系人信息*/
	public static List<ContactInfo1> findAll(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 用户名
				ContactsContract.CommonDataKinds.Phone.NUMBER,// 电话号码
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 联系人id
		};
		Cursor cursor = contentResolver.query(uri, projection, null, null, null);
		List<ContactInfo1> list = new ArrayList<ContactInfo1>();
		while (cursor.moveToNext()) {
			ContactInfo1 info = new ContactInfo1();
			info.name = cursor.getString(0);
			info.pinyin = PinyinUtils.getPinyin(cursor.getString(0));
			info.phone = cursor.getString(1);
			info.id = cursor.getInt(2);
			list.add(info);
		}
		cursor.close();
		return list;
	}

	/**获取联系人信息*/
	public static List<ContactInfo2> findContactsInfo(Context context){
		ContentResolver resolver = context.getContentResolver();
		// 1. 查询raw_contacts 表，把联系人的id 取出来
		Uri uri = Uri.parse(RAW_CONTACTS);
		Uri dataUri = Uri.parse(DATA_URI);
		List<ContactInfo2> list = new ArrayList<>();
		Cursor cursor = resolver.query(uri,new String[]{"contact_id"},null,null,null);

		while (cursor.moveToNext()){
			String id = cursor.getString(0);
			if (id != null){
				ContactInfo2 info = new ContactInfo2();
				info.id = id;
				// 2. 根据联系人的id，查询data 表，把这个id 的数据取出来
				Cursor dataCursor = resolver.query(dataUri,new String[]{"data1", "mimetype"},
						"raw_contact_id = ?",new String[]{id},null);
				while (dataCursor.moveToNext()){
					String data1 = dataCursor.getString(0);
					String mimeType = dataCursor.getString(1);
					if (mimeType.equals("vnd.android.cursor.item/name")){
						info.name = data1;
					}else if (mimeType.equals("vnd.android.cursor.item/email_v2")){
						info.email = data1;
					}else if (mimeType.equals("vnd.android.cursor.item/phone_v2")){
						info.phone = data1;
					}else if (mimeType.equals("vnd.android.cursor.item/im")){
						info.qq = data1;
					}
				}
				list.add(info);
				dataCursor.close();
			}
		}
		cursor.close();
		return list;
	}
}
