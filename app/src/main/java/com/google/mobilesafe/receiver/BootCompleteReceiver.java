package com.google.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELECOM_SERVICE);
				String currentSim = tm.getSimSerialNumber();
				if (sim.equals(currentSim)) {
					System.out.println("手机安全");
				}else {
					String phone = sp.getString("safe_phone", "");
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null, "sim card changed", null, null);
					
				}
			}
		}
		
	}

}
