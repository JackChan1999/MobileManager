package com.google.mobilesafe.ui.activity;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.widget.LockPatternView;

import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/7/6 18:51
 * 描 述 ：
 * 修订历史 ：
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
