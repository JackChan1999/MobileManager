package com.google.mobilesafe.receiver;

import com.google.mobilesafe.service.killProcessWidgetService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyAppWidget extends AppWidgetProvider {
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
		Intent intent = new Intent(context,killProcessWidgetService.class);
		context.startService(intent);
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		

		Intent intent = new Intent(context,killProcessWidgetService.class);
		context.stopService(intent);
	}

}
