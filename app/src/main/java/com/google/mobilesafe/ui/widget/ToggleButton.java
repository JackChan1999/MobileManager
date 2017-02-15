package com.google.mobilesafe.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.mobilesafe.R;
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
public class ToggleButton extends View {

    private Bitmap switchBitmap;
    private Bitmap slidBitmap;
    private Bitmap offBitmap;
    private boolean currentState;
    private int currentX;
    private boolean isTouching = false;
    private OnSwitchChangeListener mListener;

    private int switchBtnBackgroudId;
    private int slidBtnBackgroudId;
    private int offBtnBackgroudId;

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.ToggleButton);
        for (int i=0; i<typedArray.getIndexCount(); i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.ToggleButton_CurrentState:
                    currentState = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.ToggleButton_SwitchBtnBackgroud:
                    switchBtnBackgroudId = typedArray.getInt(attr, -1);
                    slidBtnBackgroudId = typedArray.getInt(attr, -1);
                    offBtnBackgroudId = typedArray.getInt(attr, -1);
                    break;
                case R.styleable.ToggleButton_SlidBtnBackgroud:
                    break;
            }
        }

        setSwitchBtnBackgroudResource(switchBtnBackgroudId);
        setSlidBtnBackgroudResource(slidBtnBackgroudId);
        setBtnBackgroudResource(offBtnBackgroudId);
    }

    public ToggleButton(Context context) {
        super(context);
    }

    public void setSwitchBtnBackgroudResource(int resId) {
        switchBitmap = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setBtnBackgroudResource(int resId) {
        offBitmap = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setSlidBtnBackgroudResource(int resId) {
        slidBitmap = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setCurrentState(boolean state) {
        currentState = state;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchBitmap.getWidth(), switchBitmap.getHeight());
    }

    // 2、绘制，画出我们的滑动开关
    // canvas:画布，将图形绘制在canvas，才能显示到屏幕上
    @Override
    protected void onDraw(Canvas canvas) {

        if (currentState) {
            // 绘制滑动开关的背景图片
            canvas.drawBitmap(switchBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(offBitmap, 0, 0, null);
        }

        // 绘制滑动块的背景图片
        if (isTouching) {// 手指触摸的时候，根据currentx 的值来绘制滑动块
            // 根据手指的X 值，来绘制滑动块图片
            int left = currentX - slidBitmap.getWidth() / 2;
            if (left < 0) {
                left = 0;
            } else if (left > (switchBitmap.getWidth() - slidBitmap.getWidth())) {// 设置右边界

                left = switchBitmap.getWidth() - slidBitmap.getWidth();
            }
            canvas.drawBitmap(slidBitmap, left, 0, null);
        } else { // 手指离开控件的时候，根据状态来绘制滑动块
            // 根据状态值，来绘制滑动块
            if (currentState) { // 当前为true，开关打开，滑动块显示在最右边
                canvas.drawBitmap(slidBitmap, switchBitmap.getWidth()
                        - slidBitmap.getWidth(), 0, null);
            } else {// 当前为false，开关关闭，滑动块显示在最左边
                canvas.drawBitmap(slidBitmap, 0, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 当滑动块中心点大于滑动开关背景图片的中心线时，显示到右边，当前状态为true
        int center = switchBitmap.getWidth() / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                currentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isTouching = true;
                currentX = (int) event.getX();
                currentState = currentX > center;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                currentX = (int) event.getX();
                boolean state = currentState;
                currentState = currentX > center;
                invalidate();
                if (mListener != null && state != currentState) {
                    mListener.onSwitchChange(currentState);
                }
                break;
        }

        return true;
    }

    public interface OnSwitchChangeListener {
        void onSwitchChange(boolean currentState);
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener listener) {
        mListener = listener;
    }

}
