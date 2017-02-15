package com.google.mobilesafe.ui.activity;

import java.util.List;

import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.db.dao.BlackNumberDao;
import com.google.mobilesafe.domain.BlackNumberInfo;
import com.google.mobilesafe.manager.ThreadManager;
import com.google.mobilesafe.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
public class CallSmsStateActivity extends BaseActivity {
	@Bind(R.id.lv_blackNumber)
	ListView listView;

	@Bind(R.id.ll_addNumber)
	LinearLayout llAddNumber;

	@Bind(R.id.ll_loading)
	LinearLayout llLoading;

	private int startIndex = 0;
	private int maxCount = 20;
	private int totalCount = 0;

	private List<BlackNumberInfo> info;

	private BlackNumberDao dao;

	private CallSmsSafeAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			llLoading.setVisibility(View.INVISIBLE);
			if (info.size() == 0) {
				llAddNumber.setVisibility(View.VISIBLE);
			} else {
				if (adapter == null) {
					adapter = new CallSmsSafeAdapter();
					listView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillData();
	}

	public void initView() {
		setContentView(R.layout.activity_firewall);
		ButterKnife.bind(this);
	}
	
	@Override
	protected void initListener() {
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:

					int lastposition = listView.getLastVisiblePosition();
					if (lastposition == info.size() - 1) { // //20条数据
						// 加载更多的数据。 更改加载数据的开始位置
						startIndex += maxCount;
						if (startIndex >= totalCount) {
							Toast.makeText(getApplicationContext(), "没有更多的数据了。", 0).show();
							return;
						}
						fillData();
					}
					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
	}

	private void fillData() {
		dao = new BlackNumberDao(this);
		totalCount = dao.getCount();
		// 数据库的总条目个数 / 每个页面最多显示多少条数据
		// 耗时的操作 逻辑应该放在子线程里面执行。
		llLoading.setVisibility(View.VISIBLE);

		ThreadManager.instance.createShortPool().execute(new Runnable() {

			@Override
			public void run() {
				if (info == null) {
					info = dao.findPart2(startIndex, maxCount);
				} else {
					// 集合里面原来有数据,新的数据应该放在旧的集合的后面。
					info.addAll(dao.findPart2(startIndex, maxCount));
				}
				handler.sendEmptyMessage(0);
			}
		});
	}

	private class CallSmsSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return info.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(CallSmsStateActivity.this, R.layout.item_callsms, null);
				holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
				holder.tvMode = (TextView) convertView.findViewById(R.id.tv_item_mode);
				holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_blackNumber);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final BlackNumberInfo info1 = info.get(position);

			holder.ivDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String number = info1.number;
					boolean result = dao.delete(number);
					if (result) {
						info.remove(info1);
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), "删除失败", 0).show();
					}
				}
			});
			holder.tvNumber.setText(info1.number);

			String mode = info1.mode;
			if ("1".equals(mode)) {
				holder.tvMode.setText("全部拦截");
			} else if ("2".equals(mode)) {
				holder.tvMode.setText("短信拦截");
			} else if ("3".equals(mode)) {
				holder.tvMode.setText("电话拦截");
			}

			return convertView;
		}
	}

	static class ViewHolder {
		private TextView tvNumber;
		private TextView tvMode;
		private ImageView ivDelete;
	}

	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View dialog_view = View.inflate(this, R.layout.dialog_add_blacknumber, null);

		final AlertDialog dialog = builder.create();

		final EditText et_blacknumber = (EditText) dialog_view.findViewById(R.id.et_blacknumber);
		final CheckBox cbPhone = (CheckBox) dialog_view.findViewById(R.id.cb_phone);
		final CheckBox cbSms = (CheckBox) dialog_view.findViewById(R.id.cb_sms);

		Button btCancel = (Button) dialog_view.findViewById(R.id.bt_cancel2);
		Button btOk = (Button) dialog_view.findViewById(R.id.bt_ok2);
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString().trim();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "号码不能为空", 1).show();
					return;
				} else {

					String mode = "0";

					if (cbPhone.isChecked() && cbSms.isChecked()) {
						mode = "1";
					} else if (cbSms.isChecked()) {
						mode = "3";
					} else if (cbPhone.isChecked()) {
						mode = "2";
					} else {
						Toast.makeText(getApplicationContext(), "请选择拦截模式", 1).show();
						return;
					}
					boolean result = dao.add(blacknumber, mode);
					if (result) {
						BlackNumberInfo numberInfo = new BlackNumberInfo();
						numberInfo.mode = mode;
						numberInfo.number = blacknumber;
						info.add(0, numberInfo);
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						} else {
							adapter = new CallSmsSafeAdapter();
							listView.setAdapter(adapter);
						}
					}
					dialog.dismiss();
				}

			}
		});

		dialog.setView(dialog_view, 0, 0, 0, 0);
		dialog.show();

	}

}
