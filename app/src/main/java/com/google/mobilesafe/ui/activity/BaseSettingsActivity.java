package com.google.mobilesafe.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.SettingsFragment;
import com.google.mobilesafe.ui.widget.items.SettingsItem;
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
public abstract class BaseSettingsActivity extends AppCompatActivity {

	private SettingsFragment fragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material_settings_base);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		fragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.material_settings_fragment);
	}


	public void addItem(SettingsItem item) {
		if (fragment != null)
			fragment.addItem(item);
	}

	public SettingsItem getItem(String keyName) {
		if (fragment != null) {
			return fragment.getItem(keyName);
		} else {
			return null;
		}
	}

	public SettingsFragment getFragment() {
		return fragment;
	}

}
