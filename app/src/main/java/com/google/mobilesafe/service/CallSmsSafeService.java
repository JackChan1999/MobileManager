package com.google.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.google.mobilesafe.db.dao.BlackNumberDao;

import java.lang.reflect.Method;

public class CallSmsSafeService extends Service {
    private BlackNumberDao dao;
    private TelephonyManager tm;

    private INnerSmsReceiver receiver;

    private MyPhoneListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dao = new BlackNumberDao(this);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        receiver = new INnerSmsReceiver();
        IntentFilter filter = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(receiver, filter);
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
        super.onDestroy();
    }

    private class MyPhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = dao.findBlackMode(incomingNumber);
                    if ("1".equals(mode) || "2".equals(mode)) {
                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri, true,
                                new CallLogObserver(new Handler(), incomingNumber));
                        endCall();

                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;

                default:
                    break;
            }
        }

    }

    /**
     * 挂断电话
     */
    private void endCall() {
        try {

            Method method1 = TelephonyManager.class.getDeclaredMethod("endCall");
            TelephonyManager manager = (TelephonyManager) getSystemService(Service
                    .TELEPHONY_SERVICE);
            method1.invoke(manager);

            //低版本解决方法
           /* Class clazz = getClassLoader().loadClass(
                    "android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            // invoke()调用method方法
            IBinder binder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;

        public CallLogObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {

            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber);
            super.onChange(selfChange);
        }
    }

    public void deleteCallLog(String incomingNumber) {
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        resolver.delete(uri, "number = ?", new String[]{incomingNumber});
    }

    private class INnerSmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] object = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : object) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                String sender = sms.getOriginatingAddress();
                String mode = dao.findBlackMode(sender);
                if ("1".equals(mode) || "2".equals(mode)) {
                    abortBroadcast();
                }
                String body = sms.getMessageBody();
                if (body.contains("发票")) {
                    abortBroadcast();
                }
            }
        }

    }

}
