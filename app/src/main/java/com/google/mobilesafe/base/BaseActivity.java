package com.google.mobilesafe.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.LinkedList;
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
 * des ：抽取BaseActivity 管理所有activity 方便退出
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/

public abstract class BaseActivity extends AppCompatActivity {
	// 管理运行的所有的activity
	public final static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
	protected Context mContext;

	// private KillAllReceiver receiver;
	// private class KillAllReceiver extends BroadcastReceiver{
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// finish();
	// }
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// receiver=new KillAllReceiver();
		// IntentFilter filter=new IntentFilter("com.itheima.google.killall");
		// registerReceiver(receiver, filter);
		mContext = this;
		synchronized (mActivities) {
			mActivities.add(this);
		}
		initView();
		initData();
		initListener();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		synchronized (mActivities) {
			mActivities.remove(this);
		}
		// if(receiver!=null){
		// unregisterReceiver(receiver);
		// receiver=null;
		// }
	}

	public void killAll() {
		// 复制了一份mActivities 集合
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new LinkedList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
		// 杀死当前的进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	protected abstract void initView();

	protected void initData() {
	}
	
	protected void initListener() {
	}

	public <T> void doAsync(IDataCallBack<T> callBack){
		AsyncTaskUtils.doAsync(callBack);
	}

	public void toast(String content){
		BaseApplication.ToastMgr.builder.display(content, Toast.LENGTH_SHORT);
	}

	public void toast(Activity activity, String content, int duration){
		BaseApplication.ToastMgr.builder.display(content,duration);
	}
}
