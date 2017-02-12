package com.google.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;
/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-21 上午10:47:56
 * 
 * 描 述 ：
 * 		土司工具类
 * 
 * 修订历史 ：
 * 
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
