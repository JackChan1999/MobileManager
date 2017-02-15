package com.google.mobilesafe.ui.activity;

import android.content.Intent;
import android.view.View;

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
public class Setup1Activity extends BaseSetupActivity {
	
	@Override
	public void initView() {
		super.initView();
		setContentView(R.layout.activity_setup1);
	}	
	
	public void next(View view){
		showNextPage();
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(Setup1Activity.this,Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}

	@Override
	public void showPreviousPage() {
		
	}
}
