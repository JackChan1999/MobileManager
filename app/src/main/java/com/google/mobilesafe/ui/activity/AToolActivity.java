package com.google.mobilesafe.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.CommonAdapter;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.OnItemClickListener;
import com.google.mobilesafe.domain.ToolsInfo;
import com.google.mobilesafe.manager.ThreadManager;
import com.google.mobilesafe.utils.PackageUtils;
import com.google.mobilesafe.utils.SmsUtils;
import com.google.mobilesafe.utils.SmsUtils.BackUpCallbackSms;
import com.google.mobilesafe.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AToolActivity extends BaseActivity {

	private List<ToolsInfo> list;

	private ProgressDialog pd;

	ProgressBar progressBar;

	private GridAdapter adapter;
	@Bind(R.id.gridview)
	RecyclerView recyclerView;

	private String[] toolsName;
	private int[] iconId;

	@Override
	public void initView() {
		setContentView(R.layout.activity_atools);
		ButterKnife.bind(this);
	}

	@Override
	public void initData() {
		toolsName = UIUtils.getStringArray(R.array.tool_name);
		iconId = new int[] { R.mipmap.home_depot, R.mipmap.sms, R.mipmap.contact, R.mipmap.ic_launcher,
				R.mipmap.ic_launcher };

		list = new ArrayList<ToolsInfo>();

		for (int i = 0; i < iconId.length; i++) {
			ToolsInfo info = new ToolsInfo();
			info.icon = iconId[i];
			info.toolName = toolsName[i];
			list.add(info);
		}

		GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
		recyclerView.setLayoutManager(layoutManager);
		adapter = new GridAdapter(this, R.layout.item_tools, list);
		recyclerView.setAdapter(adapter);

	}

	@Override
	protected void initListener() {
		adapter.setOnItemClickListener(new OnItemClickListener<ToolsInfo>() {

			@Override
			public void onItemClick(ViewGroup parent, View view, ToolsInfo t, int position) {
				switch (position) {
					case 0:
						PackageUtils.startActivity(AToolActivity.this, AppLockActivity.class);
						break;
					case 1:
						PackageUtils.startActivity(AToolActivity.this, QueryAddressActivity.class);
						break;
					case 2:
						PackageUtils.startActivity(AToolActivity.this, LockPatternActivity.class);
						break;
					case 3:
						//backUpsms();
						break;
				}
			}

			@Override
			public boolean onItemLongClick(ViewGroup parent, View view, ToolsInfo t, int position) {
				return false;
			}
		});
	}

	public void addressQuery(View view) {
		startActivity(new Intent(this, QueryAddressActivity.class));
	}

	public void appLock(View view) {
		startActivity(new Intent(this, AppLockActivity.class));
	}

	private void backUpsms() {

		pd = new ProgressDialog(AToolActivity.this);
		pd.setTitle("提示");
		pd.setMessage("稍安勿躁。正在备份。你等着吧。。");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		ThreadManager.instance.createShortPool().execute(new Runnable() {

			@Override
			public void run() {
				boolean result = SmsUtils.backUp(AToolActivity.this, new BackUpCallbackSms() {

					@Override
					public void onBackUpSms(int progress) {
						pd.setProgress(progress);
						progressBar.setProgress(progress);
					}

					@Override
					public void before(int count) {
						pd.setMax(count);
						progressBar.setMax(count);

					}
				});
				pd.dismiss();
			}
		});

	}

	private class GridAdapter extends CommonAdapter<ToolsInfo> {

		public GridAdapter(Context context, int layoutId, List<ToolsInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public void convert(ViewHolder holder, ToolsInfo t) {
			holder.setImageResource(R.id.iv_tools, t.icon);
			holder.setText(R.id.tv_toolName, t.toolName);
		}

	}
}
