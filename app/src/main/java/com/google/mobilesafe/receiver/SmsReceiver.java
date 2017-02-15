package com.google.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.google.mobilesafe.service.LocationService;
import com.google.mobilesafe.R;
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
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		 Object[] objects = (Object[]) intent.getExtras().get("pdus");
		 
		 for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			String address = message.getOriginatingAddress();
			String body = message.getMessageBody();
			if ("#*alarm*#".equals(body)) {
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				abortBroadcast();
			}else if ("#*location*#".equals(body)) {
				
				
				context.startService(new Intent(context,LocationService.class));
				
				SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
				String location = sp.getString("location", "获取位置中...");
				abortBroadcast();
				
			}else if ("#*wipedata*#".equals(body)) {
				
				//远程清除数据
				abortBroadcast();
				
			}
			else if ("#*lockscreen*#".equals(body)) {
				
				//远程锁屏
				abortBroadcast();
			}
		}
	}
	
	
	

}
