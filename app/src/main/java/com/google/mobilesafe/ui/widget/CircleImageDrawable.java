package com.google.mobilesafe.ui.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
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
public class CircleImageDrawable extends Drawable
{

	private Paint mPaint;
	private int mWidth;
	private Bitmap mBitmap ; 

	public CircleImageDrawable(Bitmap bitmap)
	{
		mBitmap = bitmap ; 
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
	}

	@Override
	public int getIntrinsicWidth()
	{
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight()
	{
		return mWidth;
	}

	@Override
	public void setAlpha(int alpha)
	{
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity()
	{
		return PixelFormat.TRANSLUCENT;
	}

}
