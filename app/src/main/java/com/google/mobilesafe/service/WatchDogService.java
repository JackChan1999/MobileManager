package com.google.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.google.mobilesafe.db.dao.AppLockDao;
import com.google.mobilesafe.manager.ThreadManager;
import com.google.mobilesafe.ui.activity.EnterPwdActivity;

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
public class WatchDogService extends Service {

	private AppLockDao appLockDao;
	private ActivityManager activityManager;
	private List<String> appInfos;
	private WatchDogReceiver dogReceiver;

	// 标记当前的看萌狗是否停下来
	private boolean flag = false;

	// 临时停止保护的包名
	private String tempStopProtectPackageName;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class WatchDogReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.qq.mobilesafe.stopprotect")) {
				// 获取到停止保护的对象
				tempStopProtectPackageName = intent
						.getStringExtra("packageName");
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				if (flag = false) {
					startDog();
				}

			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				tempStopProtectPackageName = null;
				// 让狗休息
				flag = false;
			}

		}

	}

	private class AppLockContentObserver extends ContentObserver {

		public AppLockContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			appInfos = appLockDao.findAll();
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		getContentResolver().registerContentObserver(
				Uri.parse("content://com.qq.mobilesafe.change"), true,
				new AppLockContentObserver(new Handler()));
		appLockDao = new AppLockDao(this);
		appInfos = appLockDao.findAll();

		dogReceiver = new WatchDogReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.qq.mobilesafe.stopprotect");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(dogReceiver, filter);

		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		startDog();
	}

	private void startDog() {

		ThreadManager.instance.createLongPool().execute(new Runnable() {

			@Override
			public void run() {
				flag = true;
				while (flag) {
					// String packageName = null;
					// if (android.os.Build.VERSION.SDK_INT <= 21) {
					// List<RunningTaskInfo> runningTasks =
					// activityManager.getRunningTasks(1);
					// RunningTaskInfo taskInfo = runningTasks.get(0);
					// packageName = taskInfo.topActivity.getPackageName();
					// }else {
					// List<RunningAppProcessInfo> tasks =
					// activityManager.getRunningAppProcesses();
					// packageName = tasks.get(0).processName;
					// }
					List<RunningAppProcessInfo> tasks = activityManager
							.getRunningAppProcesses();
					String packageName = tasks.get(0).processName;
					SystemClock.sleep(30);
					if (appInfos.contains(packageName)) {
						if (packageName.equals("tempStopProtectPackageName")) {

						} else {
							Intent intent = new Intent(WatchDogService.this,
									EnterPwdActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("packageName", packageName);
							startActivity(intent);
						}
					}
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		flag = false;
		unregisterReceiver(dogReceiver);
		dogReceiver = null;
	}

}
