package com.google.mobilesafe.ui.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.utils.DensityUtils;
import com.google.mobilesafe.utils.PackageUtils;
import com.google.mobilesafe.utils.SPUtils;
import com.google.mobilesafe.ui.widget.StatusBarCompat;
import com.google.mobilesafe.ui.widget.viewpager.StackTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-26 下午7:42:08
 * 
 * 描 述 ： 新手引导页
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class GuideActivity extends BaseActivity {

	@Bind(R.id.view_pager)
	ViewPager mViewPager;

	@Bind(R.id.ll_point_group)
	LinearLayout llPointGroup;

	@Bind(R.id.view_red_point)
	View redPoint;

	@Bind(R.id.btn_start)
	Button btn_start;

	private List<View> list;

	private int[] bg_rl;
	private int[] bg_iv;

	private int mPoint;

	@Override
	public void initView() {
		StatusBarCompat.compat(this);
		setContentView(R.layout.activity_guide);
		ButterKnife.bind(this);
	}
	
	@Override
	public void initData() {

		bg_rl = new int[] { R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3 };
		bg_iv = new int[] { R.mipmap.bg4, R.mipmap.bg5, R.mipmap.bg6 };

		list = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater().from(this);

		for (int i = 0; i < bg_iv.length; i++) {
			View view = inflater.inflate(R.layout.item_viewpager, null);
			RelativeLayout rl_guide = (RelativeLayout) view.findViewById(R.id.rl_guide);
			ImageView iv_guide = (ImageView) view.findViewById(R.id.iv_guide);
			rl_guide.setBackgroundResource(bg_rl[i]);
			iv_guide.setBackgroundResource(bg_iv[i]);
			list.add(view);
		}

		for (int i = 0; i < bg_iv.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_darkergray);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, 10),
					DensityUtils.dp2px(this, 10));
			if (i > 0) {
				params.leftMargin = DensityUtils.dp2px(this, 10);
			}
			point.setLayoutParams(params);
			llPointGroup.addView(point);
		}
		GuideAdapter adapter = new GuideAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setPageTransformer(true, new StackTransformer());
	}
	
	@Override
	protected void initListener() {
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mPoint = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
			}
		});
		
		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SPUtils.saveBoolean("is_userGuide_showed", true);
				PackageUtils.startActivity(GuideActivity.this, HomeActivity.class);
				finish();
			}
		});
		mViewPager.addOnPageChangeListener(new GuidePagerListener());
	}

	private class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = list.get(position);
			container.addView(view);
			return view;
		}
	}

	class GuidePagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			int len = (int) (mPoint * positionOffset + position * mPoint);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
			params.leftMargin = len;
			redPoint.setLayoutParams(params);
		}

		@Override
		public void onPageSelected(int position) {
			if (position == list.size() - 1) {
				btn_start.setVisibility(View.VISIBLE);
			} else {
				btn_start.setVisibility(View.INVISIBLE);
			}
		}

	}

}
