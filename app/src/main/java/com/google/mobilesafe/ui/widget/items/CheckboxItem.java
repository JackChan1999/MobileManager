package com.google.mobilesafe.ui.widget.items;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.SettingsFragment;
import com.google.mobilesafe.ui.widget.CheckableLinearLayout;


public class CheckboxItem extends SettingsItem {

	public static interface OnCheckedChangeListener {
		public void onCheckedChange(CheckboxItem item, boolean isChecked);
	}

	private String title, subtitle;
	private boolean checked, defaultValue = false;
	private TextView titleView, subtitleView;
	private CheckableLinearLayout mCheckableLinearLayout;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	public CheckboxItem(SettingsFragment fragment, String name) {
		super(fragment, name);
	}

	@Override
	public int getViewResource() {
		return R.layout.item_checkbox;
	}

	@Override
	public void setupView(View v) {

		mCheckableLinearLayout = (CheckableLinearLayout) v;
		titleView = (TextView) v.findViewById(R.id.material_dialog_item_title);
		subtitleView = (TextView) v.findViewById(R.id.material_dialog_item_subtitle);

		updateChecked(checked);
		updateTitle(title);
		updateSubTitle(subtitle);

		mCheckableLinearLayout.setChecked(checked);
		mCheckableLinearLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (getOnCheckedChangeListener() != null)
					getOnCheckedChangeListener().onCheckedChange(CheckboxItem.this, isChecked);
			}
		});
	}

	public CheckboxItem updateTitle(CharSequence newTitle) {
		if (titleView != null)
			titleView.setText(newTitle);
		return this;
	}

	public CheckboxItem updateSubTitle(CharSequence newSubTitle) {
		if (subtitleView != null) {
			subtitleView.setText(newSubTitle);
			subtitleView.setVisibility(subtitle != null && subtitle.trim().length() > 0 ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public String getTitle() {
		return title;
	}

	public CheckboxItem setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public CheckboxItem setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public boolean isChecked() {
		return mCheckableLinearLayout.isChecked();
	}

	public CheckboxItem updateChecked(boolean val) {
		mCheckableLinearLayout.setChecked(val);
		return this;
	}

	public OnCheckedChangeListener getOnCheckedChangeListener() {
		return mOnCheckedChangeListener;
	}

	public CheckboxItem setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
		this.mOnCheckedChangeListener = mOnCheckedChangeListener;
		return this;
	}

	public boolean isDefaultValue() {
		return defaultValue;
	}

	public CheckboxItem setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

}
