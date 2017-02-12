package com.google.mobilesafe.engine;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.google.mobilesafe.R;
import com.google.mobilesafe.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

public class TaskInfoParser {

	public static List<TaskInfo> getTaskInfos(Context context) {
		PackageManager manager = context.getPackageManager();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<TaskInfo> list = new ArrayList<TaskInfo>();
		if (android.os.Build.VERSION.SDK_INT < 21) {
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();

			for (RunningAppProcessInfo runningAppInfo : appProcesses) {

				TaskInfo info = new TaskInfo();

				String processName = runningAppInfo.processName;
				// if (processName.equals(context.getPackageName())) {
				// continue;
				// }

				info.packagename = processName;

				try {
					MemoryInfo[] memoryinfo = activityManager
							.getProcessMemoryInfo(new int[] { runningAppInfo.pid });

					int totalDirty = memoryinfo[0].getTotalPrivateDirty() * 1024;
					info.memorysize = totalDirty;

					PackageInfo packinfo = manager.getPackageInfo(processName,
							0);

					Drawable icon = packinfo.applicationInfo.loadIcon(manager);
					info.icon = icon;

					String appname = packinfo.applicationInfo
							.loadLabel(manager).toString();
					info.appname = appname;

					int flags = packinfo.applicationInfo.flags;
					if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
						info.isUserApp = false;
					} else {
						info.isUserApp = true;
					}

				} catch (Exception e) {
					e.printStackTrace();
					info.appname = processName;
					info.icon = context.getResources().getDrawable(
							R.mipmap.ic_launcher);
				}

				list.add(info);

			}
		} else {
			List<RunningServiceInfo> runningServices = activityManager
					.getRunningServices(Integer.MAX_VALUE);

			for (RunningServiceInfo runningServiceInfo : runningServices) {

				TaskInfo info = new TaskInfo();

				String processName = runningServiceInfo.process;
				// if (processName.equals(context.getPackageName())) {
				// continue;
				// }

				info.packagename = processName;

				try {
					MemoryInfo[] memoryinfo = activityManager
							.getProcessMemoryInfo(new int[] { runningServiceInfo.pid });

					int totalDirty = memoryinfo[0].getTotalPrivateDirty() * 1024;
					info.memorysize = totalDirty;

					PackageInfo packinfo = manager.getPackageInfo(processName,
							0);

					Drawable icon = packinfo.applicationInfo.loadIcon(manager);
					info.icon = icon;

					String appname = packinfo.applicationInfo
							.loadLabel(manager).toString();
					info.appname = appname;

					int flags = packinfo.applicationInfo.flags;
					if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
						info.isUserApp = false;
					} else {
						info.isUserApp = true;
					}

				} catch (Exception e) {
					e.printStackTrace();
					info.appname = processName;
					info.icon = context.getResources().getDrawable(
							R.mipmap.ic_launcher);
				}

				list.add(info);

			}
		}

		return list;
	}
}
