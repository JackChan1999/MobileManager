package com.google.mobilesafe.ui.widget.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.mobilesafe.ui.fragment.SettingsFragment;

public abstract class SettingsItem {

	protected SettingsFragment mContext;
	protected String name;

	public SettingsItem(SettingsFragment frg, String name) {
		this.mContext = frg;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public View initView(ViewGroup parent, int res) {
		return LayoutInflater.from(mContext.getActivity()).inflate(res, parent, false);
	}

	public View getView(ViewGroup parent) {
		if (getViewResource() > 0) {
			View v = initView(parent, getViewResource());
			setupView(v);
			return v;
		} else
			return null;
	}

	public abstract int getViewResource();
	public abstract void setupView(View v);

}
