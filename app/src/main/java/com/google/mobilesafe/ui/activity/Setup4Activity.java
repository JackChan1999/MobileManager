package com.google.mobilesafe.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.google.mobilesafe.R;
import com.google.mobilesafe.utils.SPUtils;

public class Setup4Activity extends BaseSetupActivity {
	
	private SharedPreferences sp ;

	@Override
	public void initView() {
		super.initView();
		setContentView(R.layout.activity_setup4);
	}

	public void next(View view) {
		showNextPage();
	}

	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		SPUtils.saveBoolean("configed", true);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
		
	}
}
