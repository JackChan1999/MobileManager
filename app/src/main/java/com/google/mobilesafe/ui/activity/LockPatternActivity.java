package com.google.mobilesafe.ui.activity;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.widget.LockPatternView;

import java.util.List;

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
public class LockPatternActivity extends BaseActivity {

    protected LockPatternView mLockPatternView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_lockpattern);
        mLockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_view);
        mLockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {

            }

            @Override
            public void onPatternCleared() {

            }

            @Override
            public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

            }

            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                StringBuffer sb = new StringBuffer();
                for (LockPatternView.Cell cell : pattern) {
                    sb.append(cell.toPwd());
                }

                if ("12345".equals(sb.toString())) {
                    //密码正确
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                } else {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                }
            }
        });
    }

}
