package com.google.mobilesafe.ui.widget.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.mobilesafe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingItemView extends RelativeLayout {

	@Bind(R.id.tv_title)
	TextView tvTitle;

	@Bind(R.id.tv_desc)
	TextView tvDesc;

	@Bind(R.id.cb_status)
	CheckBox cbSttatus;

	private String title;
	private String DescOn;
	private String DescOff;

	public SettingItemView(Context context) {
		this(context, null);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		inflate(getContext(), R.layout.view_setting_item, this);
		ButterKnife.bind(this);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
		title = a.getString(R.styleable.SettingItemView_mtitle);
		DescOn = a.getString(R.styleable.SettingItemView_desc_on);
		DescOff = a.getString(R.styleable.SettingItemView_desc_off);
		a.recycle();
		setTitle(title);

	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}

	/**
	 * 设置checkbox的勾选状态
	 * 
	 * @param check
	 */
	public void setChecked(boolean check) {

		cbSttatus.setChecked(check);
		if (check) {
			setDesc(DescOn);
		} else {
			setDesc(DescOff);
		}

	}

	/**
	 * 返回checkbox的勾选状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cbSttatus.isChecked();

	}

}
