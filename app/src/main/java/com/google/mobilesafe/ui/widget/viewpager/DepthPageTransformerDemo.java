package com.google.mobilesafe.ui.widget.viewpager;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class DepthPageTransformerDemo implements PageTransformer {
	private static float MIN_SCALE = 0.75f;

	/**
	 * view：当前页面和下一个页面 position:当前页面的位置和下一个页面的位置
	 */
	public void transformPage(View view, float position) {
		// System.out.println("view======" + view);
		// System.out.println("position=====" + position);
		// 屏幕的宽度
		//int pageWidth = view.getWidth();
		// System.out.println("当前页面的宽度=======" + pageWidth);
		// 最左边的页面，设置完全不可见
		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);
		} else if (position <= 0) { // [-1,0]
			// A 页面当前页面(0 ,-1)，设置完全可见，X 轴不平移，XY 轴不缩放
			// Use the default slide transition when moving to the left page
			view.setAlpha(1);
			//view.setTranslationX(0);
			//view.setScaleX(1);
			//view.setScaleY(1);
		} else if (position <= 1) { // (0,1]
			// B 页面下页面(1, 0 )根据当前页面的位置position，设置透明度，X 轴平移，XY 轴缩放值
			// Fade the page out.
			view.setAlpha(1 - position);
			// Counteract the default slide transition
			// 320 * -1
			//view.setTranslationX(pageWidth * -position);
			// scaleX = (1 - MIN_SCALE) * offset + MIN_SCALE;
			// mTranslationX = -getWidth() + offsetPixels;
			// Scale the page down (between MIN_SCALE and 1)
			// private static float MIN_SCALE = 0.75f;
			// 0.75f + 0.25 * 1
			//float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)* (1 - Math.abs(position));
			//view.setScaleX(scaleFactor);
			//view.setScaleY(scaleFactor);
		} else { // (1,+Infinity]
			// 表示最右边的页面，设置完全不可见
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}
