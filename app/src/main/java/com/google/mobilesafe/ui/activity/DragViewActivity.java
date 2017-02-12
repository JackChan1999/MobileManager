package com.google.mobilesafe.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.mobilesafe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DragViewActivity extends Activity {

	private SharedPreferences mPref;

	@Bind(R.id.tv_top)
	TextView tvTop;

	@Bind(R.id.tv_buttom)
	TextView tvButtom;

	@Bind(R.id.iv_drag)
	ImageView ivDrag;

	private int startX;
	private int startY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		ButterKnife.bind(this);

		mPref = getSharedPreferences("config", MODE_PRIVATE);
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();

		if (lastY > height / 2) {
			tvTop.setVisibility(View.VISIBLE);
			tvButtom.setVisibility(View.INVISIBLE);
		} else {
			tvTop.setVisibility(View.INVISIBLE);
			tvButtom.setVisibility(View.VISIBLE);
		}

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
		layoutParams.leftMargin = lastX;
		layoutParams.topMargin = lastY;

		ivDrag.setLayoutParams(layoutParams);

		ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					int endx = (int) event.getRawX();
					int endy = (int) event.getRawY();

					int dx = endx - startX;
					int dy = endy - startY;

					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;

					ivDrag.layout(l, t, r, b);

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

					case MotionEvent.ACTION_UP:
						SharedPreferences.Editor edit = mPref.edit();
						edit.putInt("lastX", ivDrag.getLeft());
						edit.putInt("lastY", ivDrag.getTop());
						edit.commit();
						break;
				}
				return true;
			}
		});

	}

}
