package com.google.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class killProcessService extends Service {
	
	private LockScreenReceiver receiver ;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		receiver = new LockScreenReceiver();
		IntentFilter filter  = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null ;
	}

	private class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager manager = (ActivityManager) context
					.getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = manager
					.getRunningAppProcesses();
			
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				manager.killBackgroundProcesses(runningAppProcessInfo.processName);
			}
		}

	}
}
