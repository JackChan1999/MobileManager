package com.google.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.google.mobilesafe.domain.AppInfo;

import java.io.File;
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
public class AppInfoParser {

	public static List<AppInfo> getAppInfos(Context context) {

		PackageManager pm = context.getPackageManager();

		List<PackageInfo> appinfos = pm.getInstalledPackages(0);

		List<AppInfo> list = new ArrayList<AppInfo>();
		for (PackageInfo packInfo : appinfos) {
			AppInfo appInfo = new AppInfo();
			String packname = packInfo.packageName;
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			String apkname = packInfo.applicationInfo.loadLabel(pm).toString();
			String apkpath = packInfo.applicationInfo.sourceDir;
			int flag = packInfo.applicationInfo.flags;

			appInfo.apkpath = apkpath;
			appInfo.appSize = new File(apkpath).length();
			appInfo.icon = icon;
			appInfo.name = apkname;
			appInfo.packagename = packname;

			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flag) != 0) {
				appInfo.inRom = false;
			} else {
				appInfo.inRom = true;
			}

			if ((ApplicationInfo.FLAG_SYSTEM & flag) != 0) {
				appInfo.userApp = false;
			} else {
				appInfo.userApp = true;
			}

			list.add(appInfo);
			appInfo = null;
		}

		return list;

	}

}
