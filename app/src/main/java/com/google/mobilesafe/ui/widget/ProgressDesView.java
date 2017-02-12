package com.google.mobilesafe.ui.widget;

import com.google.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDesView extends LinearLayout {

	private TextView tv_left;
	private TextView tv_right;
	private TextView tv_title_des;
	private ProgressBar pb_des;

	public ProgressDesView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public ProgressDesView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProgressDesView(Context context) {
		this(context, null);
	}

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.progress_des_view, this);
		tv_title_des = (TextView) findViewById(R.id.tv_title_des);
		// 进度条
		pb_des = (ProgressBar) findViewById(R.id.pb_des);
		// 左边的文本
		tv_left = (TextView) findViewById(R.id.tv_left);
		// 右边的文本
		tv_right = (TextView) findViewById(R.id.tv_right);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tv_title_des.setText(title);
	}

	/**
	 * 设置进度条进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		pb_des.setProgress(progress);
	}

	/**
	 * 设置已经使用的内存
	 * 
	 * @param left
	 */
	public void setTvLeft(String left_text) {
		tv_left.setText(left_text);
	}

	/**
	 * 设置可以使用内存
	 * 
	 * @param right_text
	 */
	public void setTvRight(String right_text) {
		tv_right.setText(right_text);
	}
}
