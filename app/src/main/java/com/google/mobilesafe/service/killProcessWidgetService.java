package com.google.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.google.mobilesafe.R;
import com.google.mobilesafe.receiver.MyAppWidget;
import com.google.mobilesafe.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;
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
public class killProcessWidgetService extends Service {

	private AppWidgetManager widgetManager;

	private Timer timer;
	private TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		widgetManager = AppWidgetManager.getInstance(this);

		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				/**
				 * 需要注意。这个里面findingviewyid这个方法
				 * 设置当前文本里面一共有多少个进程
				 */
				int processCount = SystemInfoUtils.getProcessCount(getApplicationContext());
				System.out.println(processCount);
				//设置文本
				views.setTextViewText(R.id.process_count,"正在运行的软件:" + String.valueOf(processCount));
				//获取到当前手机上面的可用内存
				long availMem = SystemInfoUtils.getAvailMem(getApplicationContext());
				System.out.println(availMem);
				
				views.setTextViewText(R.id.process_memory, "可用内存:" +Formatter.formatFileSize(getApplicationContext(), availMem));
				
				
				Intent intent = new Intent();
				
				//发送一个隐式意图
				intent.setAction("com.qq.mobilesafe");
				
				
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				//设置点击事件
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				
				
				//第一个参数表示上下文
				//第二个参数表示当前有哪一个广播进行去处理当前的桌面小控件
				ComponentName provider = new ComponentName(getApplicationContext(), MyAppWidget.class);
				
				
				
				
				//更新桌面
				widgetManager.updateAppWidget(provider, views);

			}
		};

		timer.schedule(task, 0, 5000);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null || task != null) {
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;
		}
	}

}
