package com.google.mobilesafe.base;

import com.google.mobilesafe.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class BaseDialog extends AlertDialog implements View.OnClickListener{

	protected BaseDialog(Context context) {
		super(context, R.style.BaseDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}
	
	public abstract void initView();
	public abstract void initListener();
	public abstract void initData();
	public abstract void processClick(View v);
	
	@Override
	public void onClick(View v) {
		processClick(v);
		
	}
}
