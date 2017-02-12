package com.google.mobilesafe.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {

	public static String getVersionName(Context context) {

		// 获取到包的管理者
		PackageManager packageManager = context.getPackageManager();
		try {

			// 获取到包的基本信息
			// 参数1：包名
			// 参数2：标记，0表示获取所有信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			// 获取版本号
			String versionName = packageInfo.versionName;

			return versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return "";

	}

	public static int getVersionCode(Context context) {

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);

			int versionCode = packageInfo.versionCode;

			return versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}
	/**
	 * 跳转
	 */
	public static void startActivity(Context context, Class<?> clazz) {
		Intent intent = new Intent(context, clazz);
		// 添加一个标记
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
