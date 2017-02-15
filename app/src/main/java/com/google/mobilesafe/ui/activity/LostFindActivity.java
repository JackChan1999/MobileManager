package com.google.mobilesafe.ui.activity;

import android.content.Intent;
import android.view.View;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.utils.SPUtils;
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
public class LostFindActivity extends BaseActivity {
	
	@Override
	public void initView() {
		boolean configed = SPUtils.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			
		}else {
			startActivity(new Intent(this,Setup1Activity.class));
			finish();
		}
	}
	
	public void reEnter(View view){
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
	}

}
