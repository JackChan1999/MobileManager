package com.google.mobilesafe.ui.activity;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.mobilesafe.base.BaseActivity;
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
public abstract class BaseSetupActivity extends BaseActivity {

	private GestureDetector mDetector;

	@Override
	public  void initView() {
		// 手势识别器
		mDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
										   float velocityX, float velocityY) {

						// if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
						// Toast.makeText(BaseSetupActivity.this, "不能这样划哦", 0)
						// .show();
						// return true;
						// }
						// if (Math.abs(velocityX) < 100) {
						// Toast.makeText(BaseSetupActivity.this, "滑动的太慢了", 0);
						// return true;
						// }

						// 右滑
						if (e1.getRawX() < 50+e2.getRawX()) {
							showPreviousPage();
							return true;
						}
						// 从右边往左边滑
						if (e1.getRawX() > e2.getRawX() + 50) {
							showNextPage();
							return true;
						}

						return super.onFling(e1, e2, velocityX, velocityY);
					}

				});
	}

	public abstract void showNextPage();

	public abstract void showPreviousPage();

	public void next(View view) {
		showNextPage();
	}

	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
