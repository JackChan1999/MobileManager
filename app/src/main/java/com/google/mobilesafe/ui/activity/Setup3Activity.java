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
