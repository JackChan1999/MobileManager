package com.google.mobilesafe.utils;

import java.util.Iterator;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;

public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	public static AppManager getAppManager() {
		if (instance == null)
			instance = new AppManager();
		return instance;
	}

	public void AppExit(Context paramContext) {
		try {
			finishAllActivity();
			((ActivityManager) paramContext.getSystemService("activity"))
					.restartPackage(paramContext.getPackageName());
			System.exit(0);
			Process.killProcess(Process.myPid());
			return;
		} catch (Exception localException) {
		}
	}

	public void addActivity(Activity paramActivity) {
		if (activityStack == null)
			activityStack = new Stack();
		activityStack.add(paramActivity);
	}

	public Activity currentActivity() {
		return (Activity) activityStack.lastElement();
	}

	public void finishActivity() {
		finishActivity((Activity) activityStack.lastElement());
	}

	public void finishActivity(Activity paramActivity) {
		if (paramActivity != null) {
			activityStack.remove(paramActivity);
			paramActivity.finish();
		}
	}

	public void finishActivity(Class<?> paramClass) {
		Iterator localIterator = activityStack.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			Activity localActivity = (Activity) localIterator.next();
			if (!localActivity.getClass().equals(paramClass))
				continue;
			finishActivity(localActivity);
		}
	}

	public void finishAllActivity() {
		int i = 0;
		int j = activityStack.size();
		while (true) {
			if (i >= j) {
				activityStack.clear();
				return;
			}
			if (activityStack.get(i) != null)
				((Activity) activityStack.get(i)).finish();
			i++;
		}
	}

	/**2.2以上版本*/
	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**2.3以上版本*/
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**3.0以上版本*/
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**3.1以上版本*/
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**4.1以上版本*/
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**4.4以上版本*/
	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}
}
