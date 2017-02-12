package com.google.mobilesafe.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Setup2Activity extends BaseSetupActivity {

	@Bind(R.id.iv_lock)
	ImageView iv_lock;

	@Bind(R.id.bind_sim)
	Button btn_bind_sim;

	private TelephonyManager tm;
	private final String SIM = "sim";

	@Override
	public  void initView() {
		super.initView();
		setContentView(R.layout.activity_setup2);
		ButterKnife.bind(this);
		String sim = SPUtils.getString(SIM, "");
		if (!TextUtils.isEmpty(sim)) {
			iv_lock.setImageResource(R.mipmap.ic_lock_white_24dp);
		}
	}
	@Override
	protected void initListener() {
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		btn_bind_sim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sim = SPUtils.getString(SIM, "");
				if (TextUtils.isEmpty(sim)){
					iv_lock.setImageResource(R.mipmap.ic_lock_white_24dp);
					SPUtils.saveString(SIM,tm.getSimSerialNumber());
				}else {
					iv_lock.setImageResource(R.mipmap.ic_lock_open_white_24dp);
					SPUtils.saveString(SIM,"");
				}
			}
		});
	}

	public void showNextPage() {
		if (TextUtils.isEmpty(SPUtils.getString(SIM,""))){
			toast("请绑定SIM卡");
			return;
		}
		startActivity(new Intent(this, Setup3Activity.class));
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
	}

}
