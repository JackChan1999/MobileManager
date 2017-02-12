package com.google.mobilesafe.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-29 下午6:35:45
 * 
 * 描 述 ：
 * 		fragment基类
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public abstract class BaseFragment extends Fragment {

	public Activity mActivity;

	// fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	// 处理fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initViews();
	}

	// 依附的activity创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}

	// 子类必须实现初始化布局的方法
	public abstract View initViews();

	// 初始化数据, 可以不实现
	public void initData() {

	}

}
