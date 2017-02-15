package com.google.mobilesafe.ui.activity;

import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.db.dao.QueryAddressDao;

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
public class QueryAddressActivity extends BaseActivity {

	@Bind(R.id.tv_result)
	TextView tv_result;

	@Bind(R.id.et_number)
	EditText et_number;

	@Override
	public void initView() {
		setContentView(R.layout.activity_address);
		ButterKnife.bind(this);
	}
	
	@Override
	protected void initListener() {
		et_number.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String address = QueryAddressDao.getAddress(QueryAddressActivity.this, s.toString());
				tv_result.setText(address);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}


	public void query(View view) {
		String number = et_number.getText().toString().trim();
		if (!TextUtils.isEmpty(number)) {
			String address = QueryAddressDao.getAddress(this, number);
			tv_result.setText("归属地：" + address);
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
			vibrate();
		}
	}

	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);
	}

}
