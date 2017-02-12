package com.google.mobilesafe.ui.activity;

import android.content.Intent;
import android.view.View;

import com.google.mobilesafe.R;

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
