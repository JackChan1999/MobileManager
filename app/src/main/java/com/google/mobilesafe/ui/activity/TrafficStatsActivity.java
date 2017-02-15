package com.google.mobilesafe.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.CommonAdapter;
import com.google.mobilesafe.domain.TrafficInfo;
import com.google.mobilesafe.ui.adapter.recyclerview.SlideInBottomAnimatorAdapter;
import com.google.mobilesafe.ui.widget.jumpingbeans.JumpingBeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class TrafficStatsActivity extends BaseActivity {

	private List<TrafficInfo> list;

	@Bind(R.id.ll_loading)
	LinearLayout ll_loading;

	@Bind(R.id.iv_loading)
	ImageView ivLoading;

	@Bind(R.id.tv_loading)
	TextView tv_loading;

	private JumpingBeans mJumpingBeans;

	private PackageManager mPm;

	private ScanTask task;

	@Bind(R.id.recyclerview)
	RecyclerView recyclerView;

	@Override
	protected void onResume() {
		super.onResume();
		mJumpingBeans = JumpingBeans.with(tv_loading).appendJumpingDots().build();
	}

	public void initView() {
		setContentView(R.layout.activity_traffic);
		ButterKnife.bind(this);
	}

	public void initData() {
		mPm = getPackageManager();
		task = new ScanTask();
		task.execute();
	}

	private class ScanTask extends AsyncTask<Void, Void, Void> {
		// private boolean isFinish = false;
		// private ListAdapter adapter;
		// private LoadingProgressDialog dialog;
		private AnimationDrawable animDrawable;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * dialog = new LoadingProgressDialog(TrafficStatsActivity.this,
			 * "拼命加载中...", R.drawable.loading_animation2); dialog.show();
			 */
			ivLoading.setBackgroundResource(R.drawable.loading_animation2);
			animDrawable = (AnimationDrawable) ivLoading.getBackground();
			animDrawable.start();
		}

		@Override
		protected Void doInBackground(Void... params) {

			list = new ArrayList<TrafficInfo>();
			List<PackageInfo> installedPackages = mPm.getInstalledPackages(0);

			for (PackageInfo packageInfo : installedPackages) {
				TrafficInfo info = new TrafficInfo();

				String appname = packageInfo.applicationInfo.loadLabel(mPm).toString();
				Drawable icon = packageInfo.applicationInfo.loadIcon(mPm);
				int uid = packageInfo.applicationInfo.uid;

				long snd = TrafficStats.getUidRxBytes(uid);
				long rcv = TrafficStats.getUidTxBytes(uid);

				if (snd != 0 && rcv != 0) {
					info.icon = icon;
					info.appname = appname;
					info.rcv = rcv;
					info.snd = snd;

					list.add(info);
				}

			}
			Collections.sort(list);
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			// if (adapter == null) {
			// adapter = new ListAdapter(TrafficStatsActivity.this, list);
			// recyclerView.setAdapter(adapter);
			// }else {
			// adapter.notifyDataSetChanged();
			// }

		}

		protected void onPostExecute(Void result) {
			// dialog.dismiss();
			ll_loading.setVisibility(View.INVISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
			mJumpingBeans.stopJumping();
			// list_view.setVisibility(View.VISIBLE);
			// TrafficManagerAdapter adapter = new TrafficManagerAdapter();
			// list_view.setAdapter(adapter);
			setAdapter();

		};

		// private void stop() {
		// isFinish = true;
		// }

		// private void updateProgress() {
		//
		// }

	}

	public void setAdapter() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(TrafficStatsActivity.this);
		recyclerView.setLayoutManager(layoutManager);
		ListAdapter adapter = new ListAdapter(TrafficStatsActivity.this, R.layout.item_traffic, list);
		SlideInBottomAnimatorAdapter slideInBottomAnimAdapter = new SlideInBottomAnimatorAdapter<>(adapter,
				recyclerView);
		recyclerView.setAdapter(slideInBottomAnimAdapter);
	}

	private class ListAdapter extends CommonAdapter<TrafficInfo> {
		private Context mContext;

		public ListAdapter(Context context, int layoutId, List<TrafficInfo> datas) {
			super(context, layoutId, datas);
			mContext = context;
		}

		@Override
		public void convert(com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder holder, TrafficInfo trafficInfo) {
			holder.setText(R.id.tv_load, "下载：" + Formatter.formatFileSize(mContext, trafficInfo.snd));
			holder.setText(R.id.tv_up, "上传：" + Formatter.formatFileSize(mContext, trafficInfo.rcv));
			holder.setText(R.id.tv_sum, Formatter.formatFileSize(mContext, trafficInfo.snd + trafficInfo.rcv));
			holder.setText(R.id.tv_appname, trafficInfo.appname);
			holder.setImageDrawable(R.id.iv_icon, trafficInfo.icon);
		}

	}

}
