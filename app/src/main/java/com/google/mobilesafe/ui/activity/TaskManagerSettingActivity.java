package com.google.mobilesafe.ui.activity;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.service.killProcessService;
import com.google.mobilesafe.utils.SPUtils;
import com.google.mobilesafe.utils.SystemInfoUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskManagerSettingActivity extends BaseActivity {
	@Bind(R.id.cb_progress_status)
	CheckBox cb_status;

	@Bind(R.id.cb_killprogress)
	CheckBox cb_killprogress;

	@Override
	protected void onStart() {
		super.onStart();
		if (SystemInfoUtils.isServiceRunning(this, "com.qq.mobilesafe.services.KillProcessService")) {
			cb_killprogress.setChecked(true);
		} else {
			cb_killprogress.setChecked(false);
		}
	}

	public void initView() {
		setContentView(R.layout.activity_task_manager_setting);
		ButterKnife.bind(this);
		
		cb_status.setChecked(
				SPUtils.getBoolean("is_system_show", false));
		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SPUtils.saveBoolean("is_system_show", isChecked);
			}
		});

		final Intent intent = new Intent(this, killProcessService.class);
		cb_killprogress.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					startService(intent);
				} else {
					stopService(intent);
				}
			}
		});

	}
}
