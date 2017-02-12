package com.google.mobilesafe.ui.widget.items;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.mobilesafe.R;

public class SettingClickView extends RelativeLayout {
	
	private TextView tvDesc;
	
	private TextView tvTile;

	public SettingClickView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	public SettingClickView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	
	/**
	 * 初始化布局
	 */
	private void initView(){
		 inflate(getContext(), R.layout.view_setting_click, this);
		
		 tvDesc = (TextView) findViewById(R.id.tv_desc2);
		 tvTile = (TextView) findViewById(R.id.tv_title2);
	}
	
	public void setTile(String text){
		tvTile.setText(text);
	}
	public void setDesc(String text){
		tvDesc.setText(text);
	}

}
