package com.google.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.mobilesafe.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

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
