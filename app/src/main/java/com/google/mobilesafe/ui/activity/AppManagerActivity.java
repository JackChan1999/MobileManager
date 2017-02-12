package com.google.mobilesafe.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.MultiItemCommonAdapter;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.MultiItemTypeSupport;
import com.google.mobilesafe.domain.AppInfo;
import com.google.mobilesafe.engine.AppInfoParser;
import com.google.mobilesafe.manager.ThreadManager;
import com.google.mobilesafe.ui.widget.ProgressDesView;
import com.google.mobilesafe.ui.widget.jumpingbeans.JumpingBeans;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AppManagerActivity extends BaseActivity implements OnClickListener {
	private List<AppInfo> infos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;

	@Bind(R.id.lv_apps)
	ListView listview;

	@Bind(R.id.appsize)
	TextView appsize;

	@Bind(R.id.tv_loading)
	TextView tv_loading;

	@Bind(R.id.ll_loading)
	LinearLayout ll_loading;

	@Bind(R.id.iv_loading)
	ImageView ivLoading;

	private JumpingBeans jumpingBeans;

	private PopupWindow popupWindow;

	@Bind(R.id.pdv_rom)
	ProgressDesView pdv_rom;

	@Bind(R.id.pdv_sd)
	ProgressDesView pdv_sd;

	private AppInfo appinfo;

	private UninstallReceiver receiver;

	// private LoadingProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.layout_title);
	}

	@Override
	protected void onResume() {
		super.onResume();
		jumpingBeans = JumpingBeans.with(tv_loading).appendJumpingDots().build();
	}

	@Override
	public void initListener() {
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				dismissPopupWindow();
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem >= userAppInfos.size() + 1) {
						appsize.setText("系统程序：" + systemAppInfos.size() + "个");
					} else {
						appsize.setText("用户程序：" + userAppInfos.size() + "个");
					}
				}
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object obj = listview.getItemAtPosition(position);

				if (obj != null && obj instanceof AppInfo) {
					appinfo = (AppInfo) obj;

					showshowPopupWindow(view, parent);
				}

			}
		});
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_app_manager);
		ButterKnife.bind(this);
		receiver = new UninstallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);

		// 剩余SD卡
		// long avail_sd =
		// Environment.getExternalStorageDirectory().getFreeSpace();
		// 剩余手机内存
		// long avail_rom = Environment.getDataDirectory().getFreeSpace();

		// 获取到剩余内存
		long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
		// 获取到总共有多少内存
		long romTotalSpace = Environment.getDataDirectory().getTotalSpace();
		// 获取到已经使用了的内存
		long userSpace = romTotalSpace - romFreeSpace;

		// 进度条进度
		int progress = (int) (userSpace * 100f / romTotalSpace + 0.5f);

		// String str_avail_sd = Formatter.formatFileSize(this, avail_sd);
		// String str_avail_rom = Formatter.formatFileSize(this, avail_rom);
		// tv_avail_rom.setText("剩余手机内存：" + str_avail_rom);
		// tv_avail_sd.setText("剩余SD卡：" + str_avail_sd);

		// 设置内存的数据
		pdv_rom.setTitle("内存");
		// 已经使用内存
		pdv_rom.setTvLeft(Formatter.formatFileSize(this, userSpace));
		// 可以使用内存
		pdv_rom.setTvRight(Formatter.formatFileSize(this, romFreeSpace));
		// 设置进度条
		pdv_rom.setProgress(progress);
		// 获取到sd 卡的剩余目录
		long sdFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
		// 获取到sd 卡总的目录
		long sdTotalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
		// 获取到sd 卡的使用目录
		long sdUserSpace = sdTotalSpace - sdFreeSpace;
		// 获取到sd 卡进度条进度
		int sdProgress = (int) (sdUserSpace * 100f / sdTotalSpace + 0.5f);
		// 设置sd 卡的数据
		pdv_sd.setTitle("SD卡");
		// 已经使用内存
		pdv_sd.setTvLeft(Formatter.formatFileSize(this, sdUserSpace));
		// 可以使用内存
		pdv_sd.setTvRight(Formatter.formatFileSize(this, sdFreeSpace));
		// 设置进度条
		pdv_sd.setProgress(sdProgress);
	}

	private class AppManagerAdapter extends MultiItemCommonAdapter<AppInfo> {

		public AppManagerAdapter(Context context, List<AppInfo> datas) {
			super(context, datas, new MultiItemTypeSupport<AppInfo>() {

				@Override
				public int getLayoutId(int position, AppInfo t) {
					if (position == 0 || position == userAppInfos.size() + 1) {
						return R.layout.item_headview;// 特殊条目
					} else {
						return R.layout.item_app_manager;// 普通条目
					}
				}

				@Override
				public int getViewTypeCount() {
					return 2;
				}

				@Override
				public int getItemViewType(int position, AppInfo t) {

					if (position == 0 || position == userAppInfos.size() + 1) {
						return 0;// 特殊条目
					} else {
						return 1;// 普通条目
					}
				}
			});
		}

		@Override
		public AppInfo getItem(int position) {
			if (position == 0 || position == userAppInfos.size() + 1) {
				return null;
			}

			AppInfo appInfo;

			if (position < (userAppInfos.size() + 1)) {
				appInfo = userAppInfos.get(position - 1);
			} else {
				int location = position - 1 - userAppInfos.size() - 1;
				appInfo = systemAppInfos.get(location);
			}
			return appInfo;
		}

		@Override
		public void convert(ViewHolder holder, AppInfo t) {

			switch (holder.getLayoutId()) {
			case R.layout.item_headview:
				if (holder.getPosition() == 0) {
					holder.setText(R.id.tv_head_view_title, "用户程序：" + userAppInfos.size() + "个");
				} else {
					holder.setText(R.id.tv_head_view_title, "系统程序：" + systemAppInfos.size() + "个");
				}
				break;

			case R.layout.item_app_manager:
				holder.setImageDrawable(R.id.app_icon, t.icon);
				holder.setText(R.id.app_title, t.name);
				holder.setText(R.id.appsize, Formatter.formatFileSize(getApplicationContext(), t.appSize));
				if (t.inRom) {
					holder.setText(R.id.app_location, "手机内存");
				} else {
					holder.setText(R.id.app_location, "外部存储");
				}
				break;
			}
		}

	}

	@Override
	public void initData() {
		Drawable loading1 = getResources().getDrawable(R.mipmap.progress_loading_image_01);
		Drawable loading2 = getResources().getDrawable(R.mipmap.progress_loading_image_02);
		final AnimationDrawable frameAnimation = new AnimationDrawable();
		frameAnimation.addFrame(loading1,150);
		frameAnimation.addFrame(loading2,150);
		frameAnimation.setOneShot(false);
		ivLoading.setBackgroundDrawable(frameAnimation);
		frameAnimation.start();

		/*ivLoading.setBackgroundResource(R.drawable.loading_animation1);
		final AnimationDrawable animDrawable = (AnimationDrawable) ivLoading.getBackground();
		animDrawable.start();*/

		/*
		 * dialog = new LoadingProgressDialog(this,
		 * "正在加载中..",R.drawable.loading_animation1); dialog.show();
		 */

		ThreadManager.instance.createShortPool().execute(new Runnable() {

			@Override
			public void run() {
				infos = AppInfoParser.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appinfo : infos) {
					if (appinfo.userApp) {
						userAppInfos.add(appinfo);
					} else {
						systemAppInfos.add(appinfo);
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						frameAnimation.stop();
						ll_loading.setVisibility(View.INVISIBLE);
						listview.setVisibility(View.VISIBLE);
						appsize.setVisibility(View.VISIBLE);
						jumpingBeans.stopJumping();
						listview.setAdapter(new AppManagerAdapter(AppManagerActivity.this, infos));
					}
				});
			}
		});
	}

	private void showshowPopupWindow(View view, AdapterView<?> parent) {
		View contentView = View.inflate(AppManagerActivity.this, R.layout.popup_item, null);
		
		TextView start = ButterKnife.findById(contentView, R.id.start);
		TextView share= ButterKnife.findById(contentView, R.id.share);
		TextView setting= ButterKnife.findById(contentView, R.id.setting);
		TextView uninstall= ButterKnife.findById(contentView,R.id.uninstall);
		
		start.setOnClickListener(AppManagerActivity.this);
		share.setOnClickListener(AppManagerActivity.this);
		uninstall.setOnClickListener(AppManagerActivity.this);
		setting.setOnClickListener(AppManagerActivity.this);

		dismissPopupWindow();
		popupWindow = new PopupWindow(contentView, -2, -2);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// int[] location = new int[2];
		// view.getLocationInWindow(location);

		// popupWindow.showAtLocation(parent, Gravity.LEFT+
		// Gravity.TOP, 170, location[1]);
		popupWindow.showAsDropDown(view, 150, -view.getHeight());

		ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(200);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
		alphaAnimation.setDuration(200);

		AnimationSet set = new AnimationSet(false);
		set.addAnimation(animation);
		set.addAnimation(alphaAnimation);

		contentView.startAnimation(set);
	}

	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.share:
			shareApplication();
			break;
		case R.id.setting:
			viewAppDetail();
			break;
		case R.id.uninstall:
			uninstallApplication();
			break;
		case R.id.start:
			startApplication();
			break;
		}
		dismissPopupWindow();

	}

	@Override
	protected void onDestroy() {

		dismissPopupWindow();
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private class UninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String info = intent.getData().toString();
			initData();
		}

	}

	private void shareApplication() {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "向你推荐一款好玩好用的软件：" + appinfo.name
				+ ",下载地址：https://play.google.com/store/apps/details?id=" + appinfo.packagename);
		startActivity(intent);

		// intent.putExtra("android.intent.extra.SUBJECT", "分享");
		// intent.putExtra("android.intent.extra.TEXT", "推荐您使用软件：" +
		// appinfo.getName());
		// startActivity(Intent.createChooser(intent, "分享"));
		// intent.putExtra(Intent.EXTRA_TEXT, appinfo.getName()
		// + "下载路径：https://play.google.com/store/apps/details?id="
		// + appinfo.getPackagename());
	}

	private void startApplication() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(appinfo.packagename);
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void uninstallApplication() {
		if (appinfo.userApp) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + appinfo.packagename));
			startActivity(intent);

		} else {
			try {
				if (!RootTools.isAccessGiven()) {
					return;
				}

				RootTools.sendShell("mount -o remount ,rw /system", 3000);
				RootTools.sendShell("rm -r " + appinfo.apkpath, 30000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void viewAppDetail() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
				Uri.parse("package:" + appinfo.packagename));
		// intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		// intent.addCategory(Intent.CATEGORY_DEFAULT);
		// intent.setData(Uri.parse("package:" + appinfo.getPackagename()));
		startActivity(intent);

	}
}
