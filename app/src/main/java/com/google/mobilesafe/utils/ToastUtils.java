package com.google.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;
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
public class ToastUtils {
	/**
	 * 展示一个安全的土司
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void showSafeToast(final Activity activity, final String msg) {
		// 当在主线程时，土司直接弹出
		if (Thread.currentThread().getName().equals("main")) {
			Toast.makeText(activity, msg, 0).show();
		} else {
			// 在子线程时，让土司在主线程中弹出
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					Toast.makeText(activity, msg, 0).show();
				}
			});
		}
	}
}
