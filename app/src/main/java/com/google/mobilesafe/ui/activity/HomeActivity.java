package com.google.mobilesafe.ui.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.CommonAdapter;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.OnItemClickListener;
import com.google.mobilesafe.dialog.ConfirmDialog;
import com.google.mobilesafe.dialog.ConfirmDialog.OnConfirmListener;
import com.google.mobilesafe.domain.MainInfo;
import com.google.mobilesafe.ui.widget.BadgeView;
import com.google.mobilesafe.ui.widget.recyclerview.divider.HorizontalDividerItemDecoration;
import com.google.mobilesafe.ui.widget.recyclerview.divider.VerticalDividerItemDecoration;
import com.google.mobilesafe.ui.widget.shimmer.Shimmer;
import com.google.mobilesafe.ui.widget.shimmer.ShimmerTextView;
import com.google.mobilesafe.utils.MD5Utils;
import com.google.mobilesafe.utils.SPUtils;
import com.google.mobilesafe.utils.UIUtils;

import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.OffersManager;

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
public class HomeActivity extends BaseActivity {

	@Bind(R.id.tv_heimatitle)
	ShimmerTextView shimmerTextView;

	@Bind(R.id.icon)
	ImageView iv_logo;

	@Bind(R.id.iv_setting)
	ImageView mIvSetting;

	@Bind(R.id.rv_home)
	RecyclerView recyclerView;

	private String[] itemname;
	private String[] detail;
	private int[] picture;
	private List<MainInfo> infos;
	private HomeAdapter adapter;
	private Shimmer shimmer;

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		rotateAnim();
		initBadgeView();
		initShimmer();
	}

	private void rotateAnim() {
		ObjectAnimator animator = ObjectAnimator.ofFloat(iv_logo, "rotationY", 0, 90, 270, 360);
		animator.setDuration(3000);
		animator.setRepeatCount(animator.INFINITE);
		animator.start();
	}

	private void initBadgeView(){
		BadgeView badgeView = new BadgeView(this);
		badgeView.setTargetView(mIvSetting);
		badgeView.setBadgeCount(39);
	}

	private void initShimmer(){
		if (shimmer != null && shimmer.isAnimating()) {
			shimmer.cancel();
		} else {
			shimmer = new Shimmer();
			shimmer.start(shimmerTextView);
		}
	}

	private void setAdapter() {
		adapter = new HomeAdapter(this, R.layout.item_main, infos);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
		recyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
				.color(Color.rgb(224, 224, 224)).build());
		recyclerView.setAdapter(adapter);
	}
	
	@Override
	public void initData() {
		itemname = UIUtils.getStringArray(R.array.itemname);
		detail = UIUtils.getStringArray(R.array.detail);
		picture = new int[] { R.mipmap.home_safe, R.mipmap.home_callmsgsafe, R.mipmap.home_apps,
				R.mipmap.home_taskmanager, R.mipmap.home_netmanager, R.mipmap.home_trojan,
				R.mipmap.home_sysoptimize, R.mipmap.home_tools, R.mipmap.home_settings, R.mipmap.app_store };

		infos = new ArrayList<MainInfo>();
		for (int i = 0; i < itemname.length; i++) {
			MainInfo maininfo = new MainInfo();
			maininfo.icon = picture[i];
			maininfo.title = itemname[i];
			maininfo.detail = detail[i];
			infos.add(maininfo);
		}
		setAdapter();
	}

	@Override
	public void initListener() {
		adapter.setOnItemClickListener(new OnItemClickListener<MainInfo>() {

			@Override
			public void onItemClick(ViewGroup parent, View view, MainInfo t, int position) {
				processItemClick(position);
			}

			@Override
			public boolean onItemLongClick(ViewGroup parent, View view, MainInfo t, int position) {
				return false;
			}
		});

	}

	private void processItemClick(int position){
		switch (position) {
			case 0:// 手机防盗
				showPasswordDialog();
				break;
			case 1:
				startActivity(FirewallActivity.class);
				break;
			case 2:// 软件管家
				startActivity(AppManagerActivity.class);
				break;
			case 3:// 进程管理
				startActivity(TaskManagerActivity.class);
				break;
			case 4:// 流量统计
				startActivity(TrafficStatsActivity.class);
				break;
			case 5:// 手机杀毒
				startActivity(AntivirusActivity.class);
				break;
			case 6:// 缓存清理
				startActivity(CacheCleanActivity.class);
				break;
			case 7:// 高级工具
				startActivity(AToolActivity.class);
				break;
			case 8:// 设置中心
				startActivity(SettingActivity.class);
				break;
			case 9:// 应用商店
				// 调用方式一：直接打开全屏积分墙
				// OffersManager.getInstance(HomeActivity.this).showOffersWall();
				// 调用方式二：直接打开全屏积分墙，并且监听积分墙退出的事件onDestory
				OffersManager.getInstance(HomeActivity.this).showOffersWall(new Interface_ActivityListener() {

					@Override
					public void onActivityDestroy(Context context) {
						OffersManager.getInstance(HomeActivity.this).onAppExit();
					}
				});
				break;
		}
	}

	private void startActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

	/**
	 * 显示弹窗密码
	 */
	protected void showPasswordDialog() {
		String password = SPUtils.getString("password", null);
		if (!TextUtils.isEmpty(password)) {
			showPasswordInputDialog();
		} else {
			showPasswordSetDialog();
		}

	}

	private void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_input_password, null);
		dialog.setView(view, 0, 0, 0, 0);

		final EditText et_password = (EditText) view.findViewById(R.id.et_password);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String password = et_password.getText().toString().trim();
				if (!TextUtils.isEmpty(password)) {
					String savedpass = SPUtils.getString("password", null);
					if (MD5Utils.md5(password).equals(savedpass)) {
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					} else {
						toast("密码错误");
					}

				} else {
					toast("输入内容不能为空");
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	/**
	 * 设置密码弹窗
	 */
	private void showPasswordSetDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_set_password, null);
		dialog.setView(view, 0, 0, 0, 0);

		final EditText etpass = (EditText) view.findViewById(R.id.etpassword);
		final EditText etpass2 = (EditText) view.findViewById(R.id.etpassword2);

		Button btok = (Button) view.findViewById(R.id.btok);
		Button btcancel = (Button) view.findViewById(R.id.btcancel);

		btok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String pass = etpass.getText().toString().trim();
				String pass2 = etpass2.getText().toString().trim();
				if (!TextUtils.isEmpty(pass) && !pass2.isEmpty()) {
					if (pass.equals(pass2)) {
						SPUtils.saveString("password", MD5Utils.md5(pass));
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					} else {
						toast("两次密码不一致");
					}
				} else {
					toast("密码不能为空");
				}
			}
		});

		btcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	@Override
	protected void onStart() {
		super.onStart();
		startAnimation();
	}

	private void startAnimation() {
		LayoutAnimationController animationController = new LayoutAnimationController(
				AnimationUtils.loadAnimation(this, R.anim.main_item));
		animationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
		recyclerView.setLayoutAnimation(animationController);
		recyclerView.startLayoutAnimation();
	}

	// 监听返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConfirmDialog.showDialog(this, "提示", "你确定要退出手机管家吗？", new OnConfirmListener() {

				@Override
				public void onConfirm() {
					// 有米广告
					OffersManager.getInstance(HomeActivity.this).onAppExit();
					killAll();
				}

				@Override
				public void onCancel() {

				}
			});

		}
		return true;
	}

	private class HomeAdapter extends CommonAdapter<MainInfo> {

		public HomeAdapter(Context context, int layoutId, List<MainInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public void convert(ViewHolder holder, MainInfo t) {
			holder.setText(R.id.tv_detail, t.detail);
			holder.setText(R.id.tv_title, t.title);
			holder.setImageResource(R.id.iv_icon, t.icon);
		}

	}
}
