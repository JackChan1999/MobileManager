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

public class UnLockFragment extends Fragment {
	@Bind(R.id.tv_unlock)
	TextView tv_unlock;

	@Bind(R.id.list_view)
	ListView list_view;

	private UnLockAdapter adapter;

	private List<AppInfo> list;
	private List<AppInfo> unlockapp;
	private AppLockDao dao;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.item_unlock_fragment, null);
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
		list = AppInfoParser.getAppInfos(getActivity());
		dao = new AppLockDao(getActivity());
		unlockapp = new ArrayList<AppInfo>();
		for (AppInfo appinfo : list) {

			if (TextUtils.isEmpty(dao.findLock(appinfo.packagename))) {
				if (appinfo.packagename.equals(getActivity().getPackageName())) {
					continue;
				}
				unlockapp.add(appinfo);
			}

		}

		adapter = new UnLockAdapter(getActivity(), R.layout.item_unlock, unlockapp);

		list_view.setAdapter(adapter);

	}

	private class UnLockAdapter extends CommonAdapter<AppInfo> {

		public UnLockAdapter(Context context, int layoutId, List<AppInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public int getCount() {
			tv_unlock.setText("未加锁程序：" + unlockapp.size() + "个");
			return super.getCount();
		}

		@Override
		public void convert(final ViewHolder holder, final AppInfo appInfo) {

			holder.setText(R.id.tv_name, appInfo.name);
			holder.setImageDrawable(R.id.iv_icon, appInfo.icon);
			holder.setOnClickListener(R.id.iv_unlock, new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
							0);
					// 设置动画时间
					translateAnimation.setDuration(200);
					// 开始动画
					holder.getConvertView().startAnimation(translateAnimation);
					ThreadManager.instance.createShortPool().execute(new Runnable() {
						@Override
						public void run() {
							SystemClock.sleep(200);
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									dao.add(appInfo.packagename);
									unlockapp.remove(holder.getPos());
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
