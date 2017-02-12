package com.google.mobilesafe.ui.activity;

import android.content.Intent;
import android.view.View;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.utils.SPUtils;

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
