package com.google.mobilesafe.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.MultiItemCommonAdapter;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.MultiItemTypeSupport;
import com.google.mobilesafe.domain.TaskInfo;
import com.google.mobilesafe.engine.TaskInfoParser;
import com.google.mobilesafe.manager.ThreadManager;
import com.google.mobilesafe.utils.SPUtils;
import com.google.mobilesafe.utils.SystemInfoUtils;
import com.google.mobilesafe.ui.widget.ProgressDesView;
import com.google.mobilesafe.ui.widget.jumpingbeans.JumpingBeans;
import com.google.mobilesafe.R;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
public class TaskManagerActivity extends BaseActivity {
	private int progressCount;
	private long availMem;
	private long totalMem;

	@Bind(R.id.applist)
	ListView listview;

	/*@Bind(R.id.tv_task_process_count)
	TextView tv_processcount;*/

	@Bind(R.id.pdv_task_count)
	ProgressDesView pdv_task_count;

	@Bind(R.id.pdv_ram)
	ProgressDesView pdv_ram;

	@Bind(R.id.tv_task_memory)
	TextView tv_memory;

	@Bind(R.id.tv_head_view_title)
	TextView tv_head_view;

	@Bind(R.id.tv_loading)
	TextView tvLoading;

	@Bind(R.id.ll_loading)
	LinearLayout loading;

	@Bind(R.id.iv_loading)
	ImageView iv_loading;

	private List<TaskInfo> taskinfo;
	private List<TaskInfo> userinfo;
	private List<TaskInfo> systeminfo;

	private TaskManagerAdapter adapter;

	private JumpingBeans jumpingBeans;

	@Override
	protected void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		jumpingBeans = JumpingBeans.with(tvLoading).appendJumpingDots().build();
	}

	public void initView() {
		setContentView(R.layout.activity_task_manager);
		ButterKnife.bind(this);
	}
	
	@Override
	protected void initListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object obj = listview.getItemAtPosition(position);
				if (obj != null && obj instanceof TaskInfo) {
					TaskInfo taskInfo = (TaskInfo) obj;
					ViewHolder holder = (ViewHolder) view.getTag();
					if (taskInfo.packagename.equals(getPackageName())) {
						return;
					}

					if (taskInfo.isChecked) {
						taskInfo.isChecked = false;
						holder.appstatus.setChecked(false);
					} else {
						taskInfo.isChecked = true;
						holder.appstatus.setChecked(true);
					}
				}
			}

		});
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (userinfo != null && systeminfo != null) {
					if (firstVisibleItem >= userinfo.size() + 1) {
						tv_head_view.setText("系统程序：" + systeminfo.size() + "个");
					} else {
						tv_head_view.setText("用户程序：" + userinfo.size() + "个");
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}
		});
	}

	public void initData() {
		initTaskCount();// 初始化进程个数
		initMem(); // 初始化内存
		
		loading.setVisibility(View.VISIBLE);
		iv_loading.setBackgroundResource(R.drawable.loading_animation);
		final AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading.getBackground();
		animationDrawable.start();

		ThreadManager.instance.createShortPool().execute(new Runnable() {

			@Override
			public void run() {
				taskinfo = TaskInfoParser.getTaskInfos(TaskManagerActivity.this);
				userinfo = new ArrayList<TaskInfo>();
				systeminfo = new ArrayList<TaskInfo>();
				for (TaskInfo info : taskinfo) {
					if (info.isUserApp) {
						userinfo.add(info);
					} else {
						systeminfo.add(info);
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						loading.setVisibility(View.INVISIBLE);
						animationDrawable.stop();
						jumpingBeans.stopJumping();
						//adapter = new TaskManagerAdapter();
						//listview.setAdapter(adapter);
					}
				});
			}
		});

	}
	
	private void initTaskCount() {
		// 总共的进程个数
		progressCount = SystemInfoUtils.getRunningTotalCount(TaskManagerActivity.this);
		// 获取正在运行的进程个数
		int runningProcessCount = SystemInfoUtils.getProcessCount(TaskManagerActivity.this);
		pdv_task_count.setTitle("进程");
		// 设置进程个数
		pdv_task_count.setTvLeft("正在运行" + runningProcessCount + "个");
		// 设置总共有多少个进程
		pdv_task_count.setTvRight("总共进程" + progressCount + "个");
		// 设置进度
		pdv_task_count.setProgress((int) (runningProcessCount * 100f / progressCount));
	}

	private void initMem() {
		// 获取到剩余内存
		availMem = SystemInfoUtils.getAvailMem(this);
		// 获取到总内存
		totalMem = SystemInfoUtils.getTotalMem(this);

		// 使用的内存
		long userMem = totalMem - availMem;
		pdv_ram.setTitle("内存");
		// 设置占用的内存
		pdv_ram.setTvLeft("占用内存" + Formatter.formatFileSize(this, userMem));
		// 设置可用内存
		pdv_ram.setTvRight("可用内存" + Formatter.formatFileSize(this, availMem));
		// 设置进度条
		pdv_ram.setProgress((int) (userMem * 100f / totalMem));
	}

	class TaskManagerAdapter extends MultiItemCommonAdapter<TaskInfo> {

		public TaskManagerAdapter(Context context, List<TaskInfo> datas) {
			super(context, datas, new MultiItemTypeSupport<TaskInfo>() {

				@Override
				public int getLayoutId(int position, TaskInfo t) {
					if (position == 0 || position == userinfo.size() + 1) {
						return R.layout.item_headview;
					} else {
						return R.layout.item_task_manager;
					}

				}

				@Override
				public int getViewTypeCount() {
					return 2;
				}

				@Override
				public int getItemViewType(int position, TaskInfo t) {
					if (position == 0 || position == userinfo.size() + 1) {
						return 0;
					} else {
						return 1;
					}
				}
			});

		}

		@Override
		public TaskInfo getItem(int position) {
			if (position == 0) {
				return null;
			} else if (position == (userinfo.size() + 1)) {
				return null;
			}

			TaskInfo info;
			if (position < (userinfo.size() + 1)) {
				info = userinfo.get(position - 1);
			} else {
				int location = position - userinfo.size() - 2;
				info = systeminfo.get(location);
			}

			return info;
		}

		@Override
		public int getCount() {
			boolean result = SPUtils.getBoolean("is_system_show", false);
			if (result) {
				return systeminfo.size() + 1 + userinfo.size() + 1;
			} else {
				return userinfo.size() + 1;
			}
		}

		@Override
		public void convert(com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder holder, TaskInfo taskinfo) {
			int position = holder.getPos();
			switch (holder.getItemViewType()) {
			case 0:
				if (position == 0) {
					holder.setText(R.id.tv_head_view_title, "用户程序：" + userinfo.size() + "个");
				} else {
					holder.setText(R.id.tv_head_view_title, "系统程序：" + systeminfo.size() + "个");
				}
				break;
			case 1:

				holder.setText(R.id.tv_appname, taskinfo.appname);
				holder.setText(R.id.tv_memorysize,
						"内存占用：" + Formatter.formatFileSize(TaskManagerActivity.this, taskinfo.memorysize));
				holder.setImageDrawable(R.id.iv_appicon, taskinfo.icon);
				if (taskinfo.isChecked) {
					holder.setChecked(R.id.tv_app_status, true);
				} else {
					holder.setChecked(R.id.tv_app_status, false);
				}

				if (taskinfo.packagename.equals(getPackageName())) {
					holder.setVisible(R.id.tv_app_status, false);
				} else {
					holder.setVisible(R.id.tv_app_status, true);
				}

				break;

			}
		}

	}

	class TaskManagerAdapter1 extends BaseAdapter {

		@Override
		public int getCount() {

			boolean result = SPUtils.getBoolean("is_system_show", false);
			if (result) {
				return systeminfo.size() + 1 + userinfo.size() + 1;
			} else {
				return userinfo.size() + 1;
			}
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return null;
			} else if (position == (userinfo.size() + 1)) {
				return null;
			}

			TaskInfo info;
			if (position < (userinfo.size() + 1)) {
				info = userinfo.get(position - 1);
			} else {
				int location = position - userinfo.size() - 2;
				info = systeminfo.get(location);
			}

			return info;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {

			if (position == 0 || position == userinfo.size() + 1) {
				return 0;
			} else {
				return 1;
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			HeadViewHolder headViewHolder;
			int type = getItemViewType(position);
			switch (type) {
			case 0:
				if (position == 0 || position == userinfo.size() + 1) {
					if (convertView == null) {
						headViewHolder = new HeadViewHolder();
						convertView = View.inflate(getApplicationContext(), R.layout.item_headview, null);
						headViewHolder.tv_head = (TextView) convertView.findViewById(R.id.tv_head_view_title);
						convertView.setTag(headViewHolder);
					} else {
						headViewHolder = (HeadViewHolder) convertView.getTag();
					}
					if (position == 0) {
						headViewHolder.tv_head.setText("用户程序：" + userinfo.size() + "个");
					} else {
						headViewHolder.tv_head.setText("系统程序：" + systeminfo.size() + "个");
					}

				}
				break;
			case 1:
				if (convertView != null && convertView instanceof LinearLayout) {

					holder = (ViewHolder) convertView.getTag();
				} else {

					holder = new ViewHolder();
					convertView = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
					holder.icon = (ImageView) convertView.findViewById(R.id.iv_appicon);
					holder.appname = (TextView) convertView.findViewById(R.id.tv_appname);
					holder.memorysize = (TextView) convertView.findViewById(R.id.tv_memorysize);
					holder.appstatus = (CheckBox) convertView.findViewById(R.id.tv_app_status);
					convertView.setTag(holder);
				}

				TaskInfo taskinfo;
				if (position < (userinfo.size() + 1)) {
					taskinfo = userinfo.get(position - 1);
				} else {
					int location = position - 1 - userinfo.size() - 1;
					taskinfo = systeminfo.get(location);
				}

				holder.icon.setImageDrawable(taskinfo.icon);
				holder.appname.setText(taskinfo.appname);
				holder.memorysize
						.setText("内存占用：" + Formatter.formatFileSize(TaskManagerActivity.this, taskinfo.memorysize));
				if (taskinfo.isChecked) {
					holder.appstatus.setChecked(true);
				} else {
					holder.appstatus.setChecked(false);
				}

				if (taskinfo.packagename.equals(getPackageName())) {
					holder.appstatus.setVisibility(View.INVISIBLE);

				} else {
					holder.appstatus.setVisibility(View.VISIBLE);
				}

				break;
			}
			return convertView;
		}

	}

	static class HeadViewHolder {
		TextView tv_head;
	}

	static class ViewHolder {
		private ImageView icon;
		private TextView appname;
		private TextView memorysize;
		private CheckBox appstatus;

	}

	public void selectAll(View view) {
		for (TaskInfo taskinfo : userinfo) {
			if (taskinfo.packagename.equals(getPackageName())) {
				continue;
			}
			taskinfo.isChecked = true;
		}
		for (TaskInfo taskinfo : systeminfo) {
			taskinfo.isChecked = true;
		}

		adapter.notifyDataSetChanged();
	}

	public void selectOppsite(View view) {

		for (TaskInfo taskinfo : userinfo) {
			if (taskinfo.packagename.equals(getPackageName())) {
				continue;
			}
			taskinfo.isChecked = !taskinfo.isChecked;
		}
		for (TaskInfo taskinfo : systeminfo) {
			taskinfo.isChecked = !taskinfo.isChecked;
		}

		adapter.notifyDataSetChanged();
	}

	public void killProcess(View view) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		List<TaskInfo> killList = new ArrayList<TaskInfo>();

		int killcount = 0;
		int killmem = 0;

		for (TaskInfo taskInfo : userinfo) {
			if (taskInfo.isChecked) {
				killList.add(taskInfo);
				killcount++;
				killmem += taskInfo.memorysize;
			}
		}
		for (TaskInfo taskInfo : systeminfo) {
			if (taskInfo.isChecked) {
				killList.add(taskInfo);
				killcount++;
				killmem += taskInfo.memorysize;
			}
			// manager.killBackgroundProcesses(taskInfo.packagename);
		}

		for (TaskInfo taskInfo : killList) {
			if (taskInfo.isUserApp) {
				userinfo.remove(taskInfo);
				// manager.killBackgroundProcesses(taskInfo.packagename);
			} else {
				systeminfo.remove(taskInfo);
				// manager.killBackgroundProcesses(taskInfo.packagename);
			}
			manager.killBackgroundProcesses(taskInfo.packagename);
		}
		Toast.makeText(this, "共清理" + killcount + "个进程,释放" + Formatter.formatFileSize(this, killmem) + "内存", 0).show();
		progressCount -= killcount;

		tv_memory.setText("剩余/总内存:" + Formatter.formatFileSize(TaskManagerActivity.this, availMem + killmem) + "/"
				+ Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

		adapter.notifyDataSetChanged();

	}

	public void openSetting(View view) {
		startActivity(new Intent(TaskManagerActivity.this, TaskManagerSettingActivity.class));
	}
}