package com.google.mobilesafe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.widget.items.SettingsItem;

import java.util.HashMap;
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
public class SettingsFragment extends Fragment {

	private LinearLayout material_settings_content;
	private Toolbar toolbar;
	private HashMap<String, SettingsItem> items;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		items = new HashMap<>();
		View root = inflater.inflate(R.layout.fragment_settings, container, false);
		material_settings_content = (LinearLayout) root.findViewById(R.id.material_settings_content);
		return root;
	}

	public void addItem(SettingsItem item) {
		View newView = item.getView(material_settings_content);
		if (newView != null) {
			material_settings_content.addView(newView);
			items.put(item.getName(), item);
		}
	}

	public SettingsItem getItem(String keyName) {
		return items.get(keyName);
	}
}
