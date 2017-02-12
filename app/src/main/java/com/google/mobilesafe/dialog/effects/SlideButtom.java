package com.google.mobilesafe.dialog.effects;

import com.google.mobilesafe.dialog.BaseEffects;

import android.animation.ObjectAnimator;
import android.view.View;


public class SlideButtom extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
    	
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -300, -500).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(mDuration*3/2)

        );
    }
}