package com.google.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;

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
public class ServiceStatusUtils {

	public static boolean isServiceRunning(Context context, Class<? extends Service> clazz) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningServiceInfo> runningService = activityManager
				.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningService) {
			
			//ComponentName service = runningServiceInfo.service ;
			String classname = runningServiceInfo.service.getClassName();
			if (classname.equals(clazz.getClass())) {
				return true;
			}
		}
		return false;
	}
}
