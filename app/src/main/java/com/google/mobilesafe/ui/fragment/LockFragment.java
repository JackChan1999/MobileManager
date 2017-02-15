package com.google.mobilesafe.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.CommonAdapter;
import com.google.mobilesafe.db.dao.AppLockDao;
import com.google.mobilesafe.domain.AppInfo;
import com.google.mobilesafe.engine.AppInfoParser;
import com.google.mobilesafe.manager.ThreadManager;

import java.util.ArrayList;
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
public class LockFragment extends Fragment {
	@Bind(R.id.tv_lock)
	TextView tv_lock;

	@Bind(R.id.list_view)
	ListView list_view;

	private List<AppInfo> list;
	private LockAdapter adapter;
	private AppLockDao dao;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_lock_fragment, null);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	@Override
	public void onStart() {
		super.onStart();

		List<AppInfo> appInfos = AppInfoParser.getAppInfos(getActivity());

		list = new ArrayList<AppInfo>();
		dao = new AppLockDao(getActivity());
		for (AppInfo appInfo : appInfos) {
			if (TextUtils.isEmpty(dao.findLock(appInfo.packagename))) {
				continue;
			} else {
				list.add(appInfo);
			}
			adapter = new LockAdapter(getActivity(), R.layout.item_lock, list);
			list_view.setAdapter(adapter);

		}

	}

	private class LockAdapter extends CommonAdapter<AppInfo> {

		public LockAdapter(Context context, int layoutId, List<AppInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public int getCount() {
			tv_lock.setText("已加锁程序：" + list.size() + "个");
			return super.getCount();
		}

		@Override
		public void convert(final ViewHolder holder, final AppInfo appinfo) {
			holder.setImageDrawable(R.id.iv_icon, appinfo.icon);
			holder.setText(R.id.tv_name, appinfo.name);
			holder.setOnClickListener(R.id.iv_lock, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					animation.setDuration(200);

					holder.getConvertView().startAnimation(animation);
					ThreadManager.instance.createShortPool().execute(new Runnable() {

						@Override
						public void run() {
							SystemClock.sleep(200);

							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									dao.delete(appinfo.packagename);
									list.remove(holder.getPos());
									adapter.notifyDataSetChanged();
								}
							});
						}
					});
				}
			});

		}

	}

}