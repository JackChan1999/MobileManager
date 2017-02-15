package com.google.mobilesafe.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.FirewallBlackFragment;
import com.google.mobilesafe.ui.fragment.FirewallPhoneFragment;
import com.google.mobilesafe.ui.fragment.FirewallSmsFragment;
import com.google.mobilesafe.ui.widget.StatusBarCompat;

import butterknife.Bind;
import butterknife.ButterKnife;
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
public class FirewallActivity extends FragmentActivity {
	@Bind(R.id.view_pager)
	ViewPager mViewPager;

	@Bind(R.id.tab_layout)
	TabLayout mTabLayout;

	@Override
	protected void onCreate(@Nullable Bundle arg) {
		super.onCreate(arg);
		StatusBarCompat.compat(this);
		setContentView(R.layout.activity_firewall);
		ButterKnife.bind(this);

		mViewPager.setAdapter(new FirewallFragmentPagerAdapter(getSupportFragmentManager()));
		mTabLayout.setupWithViewPager(mViewPager);
	}

	class FirewallFragmentPagerAdapter extends FragmentStatePagerAdapter {

		public FirewallFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private Fragment fragment;
		private String[] title = { "短信拦截", "电话拦截", "黑名单管理" };

		@Override
		public int getCount() {
			return title.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				fragment = new FirewallSmsFragment();
				break;
			case 1:
				fragment = new FirewallPhoneFragment();
				break;
			case 2:
				fragment = new FirewallBlackFragment();
				break;
			}
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return title[position];
		}
	}
}
