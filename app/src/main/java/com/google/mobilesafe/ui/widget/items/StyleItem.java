package com.google.mobilesafe.ui.widget.items;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.SettingsFragment;

public class StyleItem extends CheckboxItem {

	public StyleItem(SettingsFragment fragment, String name) {
		super(fragment, name);
	}

	@Override
	public int getViewResource() {
		return R.layout.item_style;
	}

}
