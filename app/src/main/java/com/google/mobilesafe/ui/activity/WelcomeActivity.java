package com.google.mobilesafe.ui.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.db.dao.AntivirusDao;
import com.google.mobilesafe.domain.ApkInfo;
import com.google.mobilesafe.domain.Virus;
import com.google.mobilesafe.manager.ThreadManager;
import com.google.mobilesafe.net.Constants;
import com.google.mobilesafe.utils.GsonTools;
import com.google.mobilesafe.net.okhttputils.OkHttpUtils;
import com.google.mobilesafe.net.okhttputils.callback.FileCallBack;
import com.google.mobilesafe.net.okhttputils.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
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
public class WelcomeActivity extends Activity {


	/*@Bind(R.id.rl_splash)
    RelativeLayout rl_splash;

	@Bind(R.id.tv_progress)
	TextView tv_progress;*/

    private ImageView mImageView;

    private String versionName;
    private int versionCode;

    private String Desc;
    private String DownloadUrl;
    private AntivirusDao dao;

    /**
     * 三个切换的动画
     */
    private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
       // initAnim();
       // intListener();
    }

    private void intListener() {
        mFadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                //startActivity(WelcomeActivity.this);
                //finish();
                // mImageView.startAnimation(mFadeOut);
            }
        });
        mFadeOut.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                // startActivity(MainActivity.class);
            }
        });
    }

    private void initAnim() {
        mFadeIn = new AlphaAnimation(0, 1);
        mFadeIn.setDuration(500);

        mFadeInScale = new ScaleAnimation(0.9f, 1.1f, 0.9f, 1.1f, Animation
                .RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
        mFadeInScale.setDuration(2000);
        mFadeInScale.setFillAfter(false);
        mFadeInScale.setInterpolator(new DecelerateInterpolator());

        mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
        mFadeOut.setDuration(500);

        AnimationSet animSet = new AnimationSet(false);
        animSet.addAnimation(mFadeIn);
        animSet.addAnimation(mFadeInScale);
        animSet.addAnimation(mFadeOut);
        mImageView.startAnimation(mFadeIn);
        mImageView.startAnimation(animSet);

        ViewPropertyAnimator animator = mImageView.animate();

        AnimationDrawable drawableAnim = new AnimationDrawable();
        for (int i=0; i<7; i++){
            int resId = getResources().getIdentifier("img"+i,"mipmap",getPackageName());


        }
        Drawable drawable = getResources().getDrawable(R.mipmap.gif_loading1);
        drawableAnim.addFrame(drawable , 5000);
        animator.alpha(0).alphaBy(1).setDuration(1000).scaleX(0.9f).scaleXBy(1.1f).scaleY(0.9f)
				.scaleYBy(1.1f);

    }


    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
				.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //ButterKnife.bind(this);
        mImageView = (ImageView) findViewById(R.id.image);
		/*// 渐变动画效果
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(2000);
		rl_splash.startAnimation(animation);

		copyDB("address.db");
		copyDB("antivirus.db");
		// tv_version.setText("版本号：" + PackageUtils.getVersionName(this));

		// 有米广告初始化
		AdManager.getInstance(this).init("4b2db6d8c67b8eec", "5775c5ae80f21417", true);
		OffersManager.getInstance(this).onAppExit();

		versionCode = PackageUtils.getVersionCode(this);*/
		checkVersion();
    }

    // 检查版本号
    private void checkVersion() {

        OkHttpUtils.get().url(Constants.URLS.ApkVersionUrl).build().connTimeOut(2000).execute(new
																									  StringCallback() {

            @Override
            public void onResponse(String result) {
                processData(result);
            }

            @Override
            public void onError(Call arg0, Exception arg1) {
                SystemClock.sleep(2000);
                enterHome();
            }
        });
    }

    // 解析json
    protected void processData(String result) {
        ApkInfo apkInfo = GsonTools.changeGsonToBean(result, ApkInfo.class);
        DownloadUrl = apkInfo.downloadurl;
        Desc = apkInfo.desc;
        int netVersion = apkInfo.version;
        if (netVersion == versionCode) {
            enterHome();
        } else {
            showDialog();
        }
    }

    // 显示对话框
    private void showDialog() {
        Builder builder = new Builder(this);
        builder.setTitle("提示");
        builder.setMessage(Desc);

        // 返回键，取消的监听
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                enterHome();
            }
        });

        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                enterHome();
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                downLoadApk(DownloadUrl);
                dialog.dismiss();

            }
        });
        builder.show();
    }

    // 下载更新包
    protected void downLoadApk(String url) {

        OkHttpUtils.get().url(url).build().execute(new FileCallBack("/mnt/sdcard", "temp.apk") {

            @Override
            public void onResponse(File file) {
                installApk("/mnt/sdcard/temp.apk");
            }

            @Override
            public void onError(Call arg0, Exception exception) {
                exception.printStackTrace();
            }

            @Override
            public void inProgress(float arg0, long arg1) {

            }
        });
    }

    // 安装软件
    protected void installApk(String url) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.View");
        intent.addCategory("android.intent.category.DEFAULT");

        intent.setDataAndType(Uri.fromFile(new File(url)), "application/vnd.android" +
				".package-archive");
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 进入主界面
    private void enterHome() {
        startActivity(new Intent(this, GuideActivity.class));
        finish();
    }

    private void copyDB(final String dbName) {
        // File filesDir = getFilesDir();
        // System.out.println("路径:" + filesDir.getAbsolutePath());
        ThreadManager.instance.createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                File destFile = new File(getFilesDir(), dbName);// 要拷贝的目标地址

                if (destFile.exists()) {
                    System.out.println("数据库" + dbName + "已存在!");
                    return;
                }

                FileOutputStream out = null;
                InputStream in = null;

                try {
                    in = getAssets().open(dbName);
                    out = new FileOutputStream(destFile);

                    int len = 0;
                    byte[] buffer = new byte[1024];

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateVirus() {

        dao = new AntivirusDao();
        OkHttpUtils.get().url(Constants.URLS.AntivirusUrl).build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                Virus virus = GsonTools.changeGsonToBean(result, Virus.class);
                dao.addVirus(virus.md5, virus.desc);
            }

            @Override
            public void onError(Call arg0, Exception arg1) {

            }
        });

    }
}
