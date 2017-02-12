package com.google.mobilesafe.ui.activity;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.mobilesafe.base.BaseActivity;

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
