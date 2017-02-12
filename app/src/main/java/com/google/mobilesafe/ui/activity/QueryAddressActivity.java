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
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-15 下午11:21:12
 * 
 * 描 述 ：
 * 
 * 
 * 修订历史 ：
 * 
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
