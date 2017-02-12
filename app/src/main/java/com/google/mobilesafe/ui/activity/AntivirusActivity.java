package com.google.mobilesafe.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.CommonAdapter;
import com.google.mobilesafe.db.dao.AntivirusDao;
import com.google.mobilesafe.domain.AntivirusInfo;
import com.google.mobilesafe.utils.MD5Utils;
import com.google.mobilesafe.ui.widget.circleprogress.ArcProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AntivirusActivity extends BaseActivity {

	private List<AntivirusInfo> list;

	private PackageManager mPm;
	@Bind(R.id.list_view)
	ListView list_view;

	@Bind(R.id.rl_scan)
	RelativeLayout rl_scan;

	@Bind(R.id.ll_result_content)
	LinearLayout ll_result_content;

	@Bind(R.id.ll_anim_content)
	LinearLayout ll_anim_content;

	@Bind(R.id.tv_progress)
	ArcProgress mTvProgress;

	@Bind(R.id.tv_packageName)
	TextView tv_packageName;

	@Bind(R.id.tv_is_virus)
	TextView tv_is_virus;

	@Bind(R.id.iv_right)
	ImageView iv_right;

	@Bind(R.id.iv_left)
	ImageView iv_left;

	AntivirusTask task;

	@Bind(R.id.bt_scan)
	Button btn_scan;

	@Override
	protected void onPause() {
		super.onPause();
		if (task != null) {
			task.stop();
			task = null;
		}
	}

	@Override
	public void initData() {
		mPm = getPackageManager();
		if (task != null) {
			task = null;
		}
		task = new AntivirusTask();
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_antiviruse);
		ButterKnife.bind(this);
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 重新扫描不可以被点击
				btn_scan.setEnabled(false);
				// 关闭动画
				stopAnimation();

			}
		});
	}

	protected void stopAnimation() {
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator animatorTranslationLeft = ObjectAnimator.ofFloat(iv_left, "translationX", -iv_left.getWidth(),
				0);
		ObjectAnimator animatorTranslationRight = ObjectAnimator.ofFloat(iv_right, "translationX", iv_right.getWidth(),
				0);
		ObjectAnimator animatorAlphaLeft = ObjectAnimator.ofFloat(iv_left, "alpha", 0, 1.0f);
		ObjectAnimator animatorAlphaRight = ObjectAnimator.ofFloat(iv_right, "alpha", 0, 1.0f);
		set.playTogether(animatorTranslationLeft, animatorTranslationRight, animatorAlphaLeft, animatorAlphaRight);
		set.setDuration(2000);
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				initData();
			}
		});
		set.start();
	}

	private class AntivirusTask extends AsyncTask<Void, AntivirusInfo, Void> {

		private int progress = 0;
		private int max = 0;
		private VirusAdapter adapter;
		private boolean isFinish = false;
		private int virusCount = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			rl_scan.setVisibility(View.VISIBLE);
			ll_result_content.setVisibility(View.INVISIBLE);
			ll_anim_content.setVisibility(View.INVISIBLE);
			btn_scan.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<PackageInfo> installedPackages = mPm.getInstalledPackages(0);
			max = installedPackages.size();
			list = new ArrayList<AntivirusInfo>();
			for (PackageInfo packageInfo : installedPackages) {
				if (isFinish) {
					break;
				}
				progress++;
				AntivirusInfo info = new AntivirusInfo();
				String appName = packageInfo.applicationInfo.loadLabel(mPm).toString();
				Drawable icon = packageInfo.applicationInfo.loadIcon(mPm);
				String packageName = packageInfo.applicationInfo.packageName;

				String md5 = MD5Utils.md5(packageInfo.applicationInfo.sourceDir);
				boolean isVirus = AntivirusDao.isVirus(AntivirusActivity.this, md5);

				info.isVirus = isVirus;
				info.appName = appName;
				info.icon = icon;
				info.packageName = packageName;
				if (isVirus) {
					virusCount++;
					list.add(0, info);
				} else {
					list.add(info);
				}

				publishProgress(info);
				SystemClock.sleep(100);
			}

			return null;
		}

		public void stop() {
			isFinish = true;
		}

		@Override
		protected void onProgressUpdate(AntivirusInfo... values) {
			super.onProgressUpdate(values);
			if (isFinish) {
				return;
			}
			AntivirusInfo info = values[0];

			int mCurrentProgress = (int) (progress * 100f / max);
			mTvProgress.setProgress(mCurrentProgress);
			tv_packageName.setText(info.packageName);
			if (adapter == null) {
				adapter = new VirusAdapter(AntivirusActivity.this, R.layout.item_virus, list);
				list_view.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			list_view.smoothScrollToPosition(adapter.getCount());

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isFinish) {
				return;
			}
			if (virusCount > 0) {
				tv_is_virus.setText("手机很不安全");
			} else {
				tv_is_virus.setText("未发现危险，您的手机很安全");
			}

			list_view.smoothScrollToPosition(0);

			rl_scan.setVisibility(View.INVISIBLE);
			ll_result_content.setVisibility(View.INVISIBLE);

			ll_anim_content.setVisibility(View.VISIBLE);

			// 扫描完成之后。执行动画
			ll_anim_content.setVisibility(View.VISIBLE);

			// 需要把背景图片一分为二
			// 设置背景图片，设置为true 可以从当前View 对象画出图片
			rl_scan.setDrawingCacheEnabled(true);
			// 设置图片的质量
			// 设置一张高清的图片
			rl_scan.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
			// 首先获取到背景图片
			Bitmap drawingCache = rl_scan.getDrawingCache();
			// 设置左边的图片
			iv_left.setImageBitmap(getBitmapLeft(drawingCache));
			// 设置右边的图片
			iv_right.setImageBitmap(getBitmapRight(drawingCache));
			// 打开背景图片的动画
			showOpenAnim();

		}

	}

	/**
	 * 打开背景图片的动画
	 */
	public void showOpenAnim() {
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator animatorTranslationLeft = ObjectAnimator.ofFloat(iv_left, "translationX", 0,
				-iv_left.getWidth());
		ObjectAnimator animatorTranslationRight = ObjectAnimator.ofFloat(iv_right, "translationX", 0,
				iv_right.getWidth());
		ObjectAnimator animatorAlphaLeft = ObjectAnimator.ofFloat(iv_left, "alpha", 1.0f, 0);
		ObjectAnimator animatorAlphaRight = ObjectAnimator.ofFloat(iv_right, "alpha", 1.0f, 0);
		set.playTogether(animatorTranslationLeft, animatorTranslationRight, animatorAlphaLeft, animatorAlphaRight);
		set.setDuration(2000);
		// 动画监听，当动画结束之后，将扫描结果展示
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				ll_result_content.setVisibility(View.VISIBLE);
				btn_scan.setEnabled(true);
			}
		});
		set.start();
	}

	/**
	 * 设置右边的图片
	 * 
	 * @param drawingCache
	 * @return
	 */
	public Bitmap getBitmapRight(Bitmap drawingCache) {
		// 得到原图的一半
		int width = drawingCache.getWidth() / 2;
		int height = drawingCache.getHeight();
		// 获取到图片
		Bitmap createBitmap = Bitmap.createBitmap(width, height, drawingCache.getConfig());
		// 将白纸铺在画布上
		Canvas canvas = new Canvas(createBitmap);
		Paint paint = new Paint();
		Matrix matrix = new Matrix();
		matrix.setTranslate(-width, 0);
		// 绘制图片
		canvas.drawBitmap(drawingCache, matrix, paint);
		return createBitmap;
	}

	/**
	 * 获取到左边的图片
	 * 
	 * @param drawingCache
	 *            原图
	 * @return
	 */
	public Bitmap getBitmapLeft(Bitmap drawingCache) {
		// 得到原图的一半
		int width = drawingCache.getWidth() / 2;
		int height = drawingCache.getHeight();
		// 获取到图片
		Bitmap createBitmap = Bitmap.createBitmap(width, height, drawingCache.getConfig());
		Canvas canvas = new Canvas(createBitmap);
		Paint paint = new Paint();
		Matrix matrix = new Matrix();
		// 绘制图片
		canvas.drawBitmap(drawingCache, matrix, paint);
		return createBitmap;
	}

	private class VirusAdapter extends CommonAdapter<AntivirusInfo> {

		public VirusAdapter(Context context, int layoutId, List<AntivirusInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public void convert(ViewHolder holder, AntivirusInfo info) {
			holder.setText(R.id.tv_isVirus, info.isVirus ? "病毒" : "扫描安全");
			holder.setImageDrawable(R.id.iv_icon, info.icon);
			holder.setText(R.id.tv_appname, info.appName);
			if (info.isVirus) {
				holder.setTextColor(R.id.tv_isVirus, Color.RED);
			}
		}
	}
}
