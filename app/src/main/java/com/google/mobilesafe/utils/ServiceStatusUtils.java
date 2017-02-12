package com.google.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

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
