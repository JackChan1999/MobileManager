package com.google.mobilesafe.ui.widget.items;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.SettingsFragment;

public class SwitcherItem extends CheckboxItem {

	public SwitcherItem(SettingsFragment fragment, String name) {
		super(fragment, name);
	}

	@Override
	public int getViewResource() {
		return R.layout.item_switcher;
	}

}
