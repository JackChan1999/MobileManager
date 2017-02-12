package com.google.mobilesafe.ui.widget.items;

import android.view.View;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.SettingsFragment;

public class DividerItem extends SettingsItem {

	public DividerItem(SettingsFragment fragment) {
		super(fragment,"divider");
	}

	@Override
	public int getViewResource() {
		return R.layout.item_divider;
	}

	@Override
	public void setupView(View v) {

	}
}
