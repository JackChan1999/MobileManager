package com.google.mobilesafe.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.fragment.SettingsFragment;
import com.google.mobilesafe.ui.widget.items.SettingsItem;


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
