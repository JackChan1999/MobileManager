package com.google.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
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
public class SystemInfoUtils {

	public static boolean isServiceRunning(Context context, String classname) {

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = manager.getRunningServices(200);
		for (RunningServiceInfo info : infos) {
			String servicename = info.service.getClassName();
			if (classname.equals(servicename)) {
				return true;
			}
		}
		return false;
	}

	public static int getProcessCount(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningApp = manager
				.getRunningServices(Integer.MAX_VALUE);
		return runningApp.size();

	}

	/**
	 * 获取总共有多少个进程
	 */
	public static int getRunningTotalCount(Context context) {
		// 获取总共有多少个进程
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager
				.getInstalledPackages(0);
		// 初始化hashset
		// 记录一共有多少个进程
		int count = 0;
		for (PackageInfo packageInfo : installedPackages) {
			HashSet<String> set = new HashSet<String>();
			// 获取到进程的名字
			String processName = packageInfo.applicationInfo.processName;
			set.add(processName);
			ActivityInfo[] activities = packageInfo.activities;
			if (null != activities) {
				for (ActivityInfo activityInfo : activities) {
					processName = activityInfo.processName;
					set.add(processName);
				}
			}
			ServiceInfo[] services = packageInfo.services;
			if (null != services) {
				for (ServiceInfo serviceInfo : services) {
					processName = serviceInfo.processName;
					set.add(processName);
				}
			}
			ActivityInfo[] receivers = packageInfo.receivers;
			if (null != receivers) {
				for (ActivityInfo activityInfo : receivers) {
					processName = activityInfo.processName;
					set.add(processName);
				}
			}
			ProviderInfo[] providers = packageInfo.providers;
			if (null != providers) {
				for (ProviderInfo providerInfo : providers) {
					processName = providerInfo.processName;
					set.add(processName);
				}
			}
			count += set.size();
		}
		return count;
	}

	public static long getAvailMem(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		manager.getMemoryInfo(info);
		return info.availMem;
	}

	public static long getTotalMem(Context context) {

		FileInputStream fis;
		try {
			fis = new FileInputStream("/proc/meminfo");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String totalmem = br.readLine();
			StringBuffer sb = new StringBuffer();
			for (char c : totalmem.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
