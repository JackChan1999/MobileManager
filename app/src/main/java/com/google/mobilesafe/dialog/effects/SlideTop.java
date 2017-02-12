package com.google.mobilesafe.dialog.effects;

import com.google.mobilesafe.dialog.BaseEffects;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-23 下午9:11:44
 * 
 * 描 述 ：
 * 
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class SlideTop extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)

        );
    }
}
