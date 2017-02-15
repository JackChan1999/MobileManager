package com.google.mobilesafe.ui.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.CommonAdapter;
import com.google.mobilesafe.domain.CacheInfo;
import com.google.mobilesafe.R;
import com.google.mobilesafe.utils.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
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
public class CacheCleanActivity extends BaseActivity implements OnClickListener {

	@Bind(R.id.list_view) ListView listView;

	@Bind(R.id.pb) ProgressBar pb;

	@Bind(R.id.tv_appname) TextView tv_appname;

	@Bind(R.id.tv_cachesize) TextView tv_cachesize;

	@Bind(R.id.tv_cache_count) TextView tv_result;

	@Bind(R.id.iv_icon) ImageView iv_icon;

	@Bind(R.id.scan_line) ImageView iv_scan_line;

	@Bind(R.id.btn_cleanAll) Button btn_cleanAll;

	@Bind(R.id.bt_scan) Button btn_scan;

	@Bind(R.id.rl_content) RelativeLayout rl_content;

	@Bind(R.id.rl_result) RelativeLayout rl_result;

	private PackageManager packageManager;
	private List<CacheInfo> cacheList;
	private ScanAsyncTask task;
	private final String action = "android.settings.APPLICATION_DETAILS_SETTINGS";
	private final String category = "android.intent.category.DEFAULT";
	private int cache_count = 0;
	private int tv_cache_size = 0;

	/**获取某个包名对应的应用程序的缓存大小*/
	private IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

			try {
				CacheInfo info = new CacheInfo();
				long cacheSize = pStats.cacheSize;
				String packageName = pStats.packageName;
				PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);

				info.icon = packageInfo.applicationInfo.loadIcon(packageManager);
				info.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
				info.appPackageName = packageName;

				if (cacheSize > 0) {
					cache_count++;
					tv_cache_size += cacheSize;
					info.cacheSize = cacheSize;
					cacheList.add(0, info);
				} else {
					cacheList.add(info);
				}
				task.updateCacheInfo(info);

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	};

	/**一键清理*/
	private IPackageDataObserver.Stub mDataObserver = new IPackageDataObserver.Stub() {
		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
			toast("清理完毕");
		}
	};

	public void initView() {
		setContentView(R.layout.activity_clean_cache);
		ButterKnife.bind(this);
	}

	public void initData() {
		packageManager = getPackageManager();
		task = new ScanAsyncTask();
		task.execute();
	}
	@Override
	protected void initListener() {
		btn_scan.setOnClickListener(this);
		btn_cleanAll.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (task != null) {
			task.stop();
			task = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_scan:
			initData();
			rl_result.setVisibility(View.INVISIBLE);
			rl_content.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_cleanAll:
			Method method;
			try {
				method = PackageManager.class.getDeclaredMethod("freeStorageAndNotify", long.class,
						IPackageDataObserver.class);
				method.setAccessible(true);
				method.invoke(packageManager, Long.MAX_VALUE, mDataObserver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}


	private class CacheAdapter extends CommonAdapter<CacheInfo> {

		public CacheAdapter(Context context, int layoutId, List<CacheInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public void convert(ViewHolder holder, final CacheInfo cacheInfo) {
			holder.setText(R.id.tv_name, cacheInfo.appName);
			holder.setText(R.id.tv_cache_size,
					"缓存大小:" + Formatter.formatFileSize(CacheCleanActivity.this, cacheInfo.cacheSize));
			holder.setImageDrawable(R.id.iv_icon, cacheInfo.icon);
			holder.setVisible(R.id.ib_delete, cacheInfo.cacheSize > 0 ? true : false);

			holder.setOnClickListener(R.id.ib_delete, new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(action);
					intent.addCategory(category);
					intent.setData(Uri.parse("package:" + cacheInfo.appPackageName));
					startActivity(intent);
				}
			});
		}

	}

	private class ScanAsyncTask extends AsyncTask<Void, CacheInfo, Void> {
		/**是否扫描完成*/
		boolean isFinish = false;
		private CacheAdapter adapter;
		private int progress = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//执行扫描动画
			TranslateAnimation translateAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 0f,
					Animation.RELATIVE_TO_PARENT, 0.8f);
			translateAnimation.setDuration(800);
			translateAnimation.setRepeatCount(Animation.INFINITE);
			translateAnimation.setRepeatMode(Animation.REVERSE);
			iv_scan_line.startAnimation(translateAnimation);
		}

		@Override
		protected Void doInBackground(Void... params) {

			List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
			cacheList = new ArrayList<CacheInfo>();
			pb.setMax(installedPackages.size());

			for (PackageInfo packageInfo : installedPackages) {
				progress++;

				try {//获取某个包名对应的应用程序的缓存大小
					Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class,
							IPackageStatsObserver.class);
					method.setAccessible(true);
					method.invoke(packageManager, packageInfo.packageName, mStatsObserver);
					SystemClock.sleep(200);
					if (isFinish) {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public void updateCacheInfo(CacheInfo info) {
			publishProgress(info);
		}

		public void stop() {
			isFinish = true;
		}

		@Override
		protected void onProgressUpdate(CacheInfo... values) {
			super.onProgressUpdate(values);

			if (isFinish) {
				return;
			}

			CacheInfo info = values[0];
			iv_icon.setImageDrawable(info.icon);
			tv_appname.setText(info.appName);
			tv_cachesize.setText("缓存大小：" + FileUtils.formatFileSize(info.cacheSize));
			pb.setProgress(progress);

			if (adapter == null) {
				adapter = new CacheAdapter(mContext, R.layout.item_clean_cache, cacheList);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			listView.smoothScrollToPosition(adapter.getCount());
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (isFinish) {
				return;
			}
			Collections.sort(cacheList);
			listView.smoothScrollToPosition(0);
			rl_result.setVisibility(View.VISIBLE);
			rl_content.setVisibility(View.INVISIBLE);
			tv_result.setText("总共有" + cache_count + "个缓存文件,缓存大小"
					+ FileUtils.formatFileSize(tv_cache_size));
		}
	}

}
