package com.google.mobilesafe.ui.widget.viewpager;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerTransformer extends ViewPager {

	private float mTranslationX = 0;
	private static float MIN_SCALE = 0.75f;
	private float mScaleX = 0;
	// 记录左右2 个图片
	private View left;
	private View right;
	private Map<Integer, View> mHashMaps = new HashMap<Integer, View>();

	public ViewPagerTransformer(Context context) {
		super(context);
	}

	public ViewPagerTransformer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * position :表示位置
	 * offset : 表示比例
	 * offsetPixels : 表示像素
	 */
	@Override
	protected void onPageScrolled(int position, float offset, int offsetPixels) {
//		System.out.println("position=========" + position);
//		System.out.println("offset=========" + offset);
//		System.out.println("offsetPixels=========" + offsetPixels);
		// 表示左边和右边的值
		left = mHashMaps.get(position);
		right = mHashMaps.get(position + 1);
		// 开始动画
		startAnimation(left, right, position, offset, offsetPixels);
		super.onPageScrolled(position, offset, offsetPixels);
	}

	/**
	 * 开始动画
	 * 
	 * @param left
	 *            左边的页面
	 * @param right
	 *            右边的页面
	 * @param position
	 *            当前的位置
	 * @param offset
	 *            当前的比例
	 * @param offsetPixels
	 *            当前的像素
	 */
	private void startAnimation(View left, View right, int position,
			float offset, int offsetPixels) {
		// 如果右边的图片不等于null 说明有值
		if (null != right) {
			// -320 + 0;
			mTranslationX = -getWidth() + offsetPixels;
			// 设置位移动画
			right.setTranslationX(mTranslationX);
			// 0 - 1
			// 0.25 * 0 + 0.75
			mScaleX = (1 - MIN_SCALE) * offset + MIN_SCALE;
			right.setScaleX(mScaleX);
			right.setScaleY(mScaleX);
			//right.setAlpha(1-offset);
		}
		if (null != left) {
			// 左边永远在上面
			left.bringToFront();
			//left.setAlpha(1-offset);
		}
	}

	/**
	 * 添加一个子view
	 * 
	 * @param imageView
	 *            子view
	 * @param position
	 *            位置
	 */
	public void addViewChild(View view, int position) {
		mHashMaps.put(position, view);
	}

	/**
	 * 删除一个孩子
	 * 
	 * @param position
	 */
	public void removeViewChild(int position) {
		mHashMaps.remove(position);
	}
}
