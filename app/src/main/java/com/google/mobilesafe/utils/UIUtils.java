package com.google.mobilesafe.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.mobilesafe.base.BaseApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
public class UIUtils {
	/**
	 * 获取全局的上下文
	 * 
	 * @return
	 */
	public static Context getContext() {
		return BaseApplication.getContext();
	}

	/**
	 * 得到应用程序的包名
	 */
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**
	 * 获取全局的Handler
	 * 
	 * @return
	 */
	public static Handler getHandler() {
		return BaseApplication.getHandler();
	}

	/**
	 * 获取主线程的方法
	 * 
	 * @return
	 */
	public static Thread getThread() {
		return BaseApplication.getMainThread();
	}

	/**
	 * 获取主线程的id
	 * 
	 * @return
	 */
	public static int getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/**
	 * 
	 * @param layoutId
	 *            布局文件的资源id
	 * @return 布局文件资源id 对应的view
	 */
	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}

	/**
	 * 
	 * @return 获取资源文件夹对象
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/**
	 * 
	 * @param stringid
	 *            字符串对应的资源id
	 * @return string.xml 中字符串对应资源id 对应的值
	 */
	public static String getString(int stringid) {
		return getResources().getString(stringid);
	}

	/**
	 * 
	 * @param stringArrayid
	 *            字符串数组对应的资源id
	 * @return string.xml 中字符串数组对应资源id 对应的值
	 */
	public static String[] getStringArray(int stringArrayid) {
		return getResources().getStringArray(stringArrayid);
	}

	// dip2px 1:0.75 1:1 1:0.5 1:2 1:3
	public static int dip2px(int dip) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (density * dip + 0.5);
	}

	// px2dip
	public static int px2dip(int px) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5);
	}

	/**
	 * 
	 * @param runnable
	 *            保证方法在主线程中运行
	 */
	public static void runOnMainThread(Runnable runnable) {
		if (android.os.Process.myTid() == getMainThreadId()) {
			// 在主线程中的方法直接运行
			runnable.run();
		} else {
			// 不在主线程中放入主线程中运行
			getHandler().post(runnable);
		}
	}

	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getResources().getColorStateList(mTabTextColorResId);
	}

	public static Drawable getDrawable(int drawableId) {
		return getResources().getDrawable(drawableId);
	}

	public static int getStatusBarHeight(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static TextView getActionBarTextView(Toolbar toolbar) {
		TextView titleTextView = null;
		try {
			Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
			f.setAccessible(true);
			titleTextView = (TextView) f.get(toolbar);
		} catch (NoSuchFieldException e) {
		} catch (IllegalAccessException e) {
		}
		return titleTextView;
	}

	public static void makeTitleCenter(String title, Toolbar toolbar, ActionBar actionBar) {
		if (title != null && !TextUtils.isEmpty(title.trim())) {
			final String tag = " ";
			if (actionBar != null) {
				actionBar.setTitle(tag);
			}
			TextView titleTv = null;
			View leftBtn = null;
			for (int i = 0; i < toolbar.getChildCount(); i++) {
				View view = toolbar.getChildAt(i);
				CharSequence text = null;
				if (view instanceof TextView && (text = ((TextView) view).getText()) != null && text.equals(tag)) {
					titleTv = (TextView) view;
				} else if (view instanceof ImageButton) {
					leftBtn = view;
				}
			}
			if (titleTv != null) {
				final TextView fTitleTv = titleTv;
				final View fLeftBtn = leftBtn;
				fTitleTv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						fTitleTv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						int leftWidgetWidth = fLeftBtn != null ? fLeftBtn.getWidth() : 0;
						fTitleTv.setPadding(UIUtils.getContext().getResources().getDisplayMetrics().widthPixels / 2
								- leftWidgetWidth - fTitleTv.getWidth() / 2, 0, 0, 0);
						fTitleTv.requestLayout();
					}
				});
			}
		}
	}

	public static void centerToolbarTitle(@NonNull final Toolbar toolbar) {
		final CharSequence title = toolbar.getTitle();
		final ArrayList<View> outViews = new ArrayList<>(1);
		toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
		if (!outViews.isEmpty()) {
			final TextView titleView = (TextView) outViews.get(0);
			titleView.setGravity(Gravity.CENTER);
			final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			toolbar.requestLayout();
			//also you can use titleView for changing font: titleView.setTypeface(Typeface);
		}
	}
}
