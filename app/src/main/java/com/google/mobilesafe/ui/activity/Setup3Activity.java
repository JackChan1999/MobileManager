package com.google.mobilesafe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.mobilesafe.R;
import com.google.mobilesafe.utils.SPUtils;
import com.google.mobilesafe.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Setup3Activity extends BaseSetupActivity {

	@Bind(R.id.et_phone)
	 EditText et_phone;

	@Override
	public void initView() {
		super.initView();
		setContentView(R.layout.activity_setup3);
		ButterKnife.bind(this);
	}

	public void next(View view) {
		showNextPage();
	}

	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public void showNextPage() {
		String number = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			ToastUtils.showSafeToast(this, "安全号码不能为空");
			return;

		}
		SPUtils.saveString("safephone", number);
		startActivity(new Intent(this, Setup4Activity.class));
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);

	}

	public void selectContact(View view) {
		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replace("-", "").replace(" ", "");
			et_phone.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
