package com.google.mobilesafe.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.google.mobilesafe.R;
import com.google.mobilesafe.service.AddressService;
import com.google.mobilesafe.service.WatchDogService;
import com.google.mobilesafe.ui.widget.items.CheckboxItem;
import com.google.mobilesafe.ui.widget.items.DividerItem;
import com.google.mobilesafe.ui.widget.items.StyleItem;
import com.google.mobilesafe.ui.widget.items.SwitcherItem;
import com.google.mobilesafe.utils.SPUtils;
import com.google.mobilesafe.utils.ServiceStatusUtils;
import com.google.mobilesafe.utils.SystemInfoUtils;
import com.google.mobilesafe.utils.UIUtils;

public class SettingActivity extends BaseSettingsActivity {

	private DividerItem mDividerItem;
	private CheckboxItem mUpdateItem;
	private CheckboxItem mWatchDogItem;
	private SwitcherItem mAddressItem;
	private StyleItem mKuaidialStyleItem;
	private StyleItem mDialogItem;

	private Intent watchDogIntent;
	private Context mContext;
	private final String watchDogService = "com.google.mobilesafe.service.WatchDogService";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initItems();
		addItem(mUpdateItem);
		addItem(mDividerItem);
		addItem(mWatchDogItem);
		addItem(mDividerItem);
		addItem(mAddressItem);
		addItem(mDividerItem);
		addItem(mKuaidialStyleItem);
		addItem(mDividerItem);
		addItem(mDialogItem);
		addItem(mDividerItem);
		initUpdateView();
		initWatchDog();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
	}

	@Override
	protected void onStart() {
		// 判断服务的运行状态
		boolean running = SystemInfoUtils.isServiceRunning(this, watchDogService);
		if (running) {
			mWatchDogItem.updateChecked(true);
		} else {
			mWatchDogItem.updateChecked(false);
		}
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_setting,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_settings:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initWatchDog() {
		watchDogIntent = new Intent(this, WatchDogService.class);
		mWatchDogItem.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
			@Override
			public void onCheckedChange(CheckboxItem item, boolean isChecked) {
				if (isChecked) {
					stopService(watchDogIntent);// 停止拦截服务
					mWatchDogItem.updateSubTitle("看萌狗已开启");
				} else {
					startService(watchDogIntent);// 开启拦截服务
					mWatchDogItem.updateSubTitle("看萌狗已关闭");
				}
			}
		});
	}

	private void initUpdateView() {
		boolean autoUpdate = SPUtils.getBoolean("autoUpdate", true);
		if (autoUpdate) {
			mUpdateItem.updateChecked(true);
		} else {
			mUpdateItem.updateChecked(false);
		}

		mUpdateItem.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
			@Override
			public void onCheckedChange(CheckboxItem item, boolean isChecked) {
				SPUtils.saveBoolean("autoUpdate",isChecked);
				if (isChecked){
					mUpdateItem.updateSubTitle("自动更新已开启");
				}else {
					mUpdateItem.updateSubTitle("自动更新已关闭");
				}
			}
		});
	}

	public void initAddressView() {
		boolean runningservice = ServiceStatusUtils.isServiceRunning(this, AddressService.class);
		if (runningservice) {
			mAddressItem.updateChecked(true);
		} else {
			mAddressItem.updateChecked(false);
		}

		mAddressItem.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
			@Override
			public void onCheckedChange(CheckboxItem item, boolean isChecked) {
				if (isChecked) {
					stopService(new Intent(mContext, AddressService.class));
					mAddressItem.updateSubTitle("电话归属地显示已开启");
				} else {
					startService(new Intent(mContext, AddressService.class));
					mAddressItem.updateSubTitle("电话归属地显示已关闭");
				}
			}
		});
	}

	final String[] style = UIUtils.getStringArray(R.array.style);

	private void initAddressStyle() {
		int index = SPUtils.getInt("address_style", 0);
		mKuaidialStyleItem.updateSubTitle(style[index]);
		mKuaidialStyleItem.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
			@Override
			public void onCheckedChange(CheckboxItem item, boolean isChecked) {
				showSingleChooseDailog();
			}
		});
	}

	protected void showSingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("归属地提示框风格");
		int index = SPUtils.getInt("address_style", 0);

		builder.setSingleChoiceItems(style, index, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SPUtils.putInt("address_style", which);
				dialog.dismiss();
				mKuaidialStyleItem.updateSubTitle(style[which]);
			}
		});
		builder.setNegativeButton(R.string.cancel, null);
		builder.show();
	}

	public void initAddressLocation() {
		mDialogItem.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
			@Override
			public void onCheckedChange(CheckboxItem item, boolean isChecked) {
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
	}

	private void initItems() {
		mDividerItem = new DividerItem(getFragment());
		mUpdateItem = new CheckboxItem(getFragment(),"update").setTitle("自动更新设置").setSubtitle("自动更新已关闭");
		mWatchDogItem = new CheckboxItem(getFragment(),"watchDog").setTitle("看萌狗设置").setSubtitle("看萌狗已关闭");
		mAddressItem = new SwitcherItem(getFragment(),"address");
		mAddressItem.setTitle("电话归属地显示设置").setSubtitle("电话归属地显示已关闭");

		mKuaidialStyleItem = new StyleItem(getFragment(),"kuaidialStyle");
		mKuaidialStyleItem.setTitle("归属地风格设置").setSubtitle("半透明");

		mDialogItem = new StyleItem(getFragment(),"dialog");
		mDialogItem.setTitle("归属地显示位置").setSubtitle("设置归属地的显示位置");
	}
}
