package com.google.mobilesafe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.mobilesafe.R;
import com.google.mobilesafe.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EnterPwdActivity extends Activity  {
    @Bind(R.id.et_pwd)
    EditText et_pwd;

    @Bind(R.id.btn_0)
    Button btn_0;
    @Bind(R.id.btn_1)
    Button btn_1;
    @Bind(R.id.btn_2)
    Button btn_2;
    @Bind(R.id.btn_3)
    Button btn_3;
    @Bind(R.id.btn_4)
    Button btn_4;
    @Bind(R.id.btn_5)
    Button btn_5;
    @Bind(R.id.btn_6)
    Button btn_6;
    @Bind(R.id.btn_7)
    Button btn_7;
    @Bind(R.id.btn_8)
    Button btn_8;
    @Bind(R.id.btn_9)
    Button btn_9;
    @Bind(R.id.btn_clear)
    Button btn_clear;
    @Bind(R.id.btn_delete)
    Button btn_delete;
    @Bind(R.id.btn_ok)
    Button btn_ok;

    private String packname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_set_pwd);
        ButterKnife.bind(this);
       Intent intent = getIntent();
        if (intent != null) {
            packname = intent.getStringExtra("packagename");
        }

        et_pwd.setInputType(InputType.TYPE_NULL);
        /*btn_ok.setOnClickListener(this);
        btn_clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_pwd.setText("");
            }
        });

        btn_delete.setOnClickListener(new OnClickListener() {

            private String str;

            @Override
            public void onClick(View v) {
                str = et_pwd.getText().toString();
                if (str.length() == 0) {
                    return;
                }
                et_pwd.setText(str.substring(0, (str.length() - 1)));
            }
        });*/



      /*  btn_0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_0.getText().toString());
            }
        });
        btn_1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_1.getText().toString());
            }
        });
        btn_2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_2.getText().toString());
            }
        });
        btn_3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_3.getText().toString());
            }
        });
        btn_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_4.getText().toString());
            }
        });
        btn_5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_5.getText().toString());
            }
        });
        btn_6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_6.getText().toString());
            }
        });
        btn_7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_7.getText().toString());
            }
        });
        btn_8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_8.getText().toString());
            }
        });
        btn_9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = et_pwd.getText().toString();
                et_pwd.setText(str + btn_9.getText().toString());
            }
        });*/
    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:

                String result = et_pwd.getText().toString();
               if ("123".equals(result)) {
                    Intent intent = new Intent();
                    intent.setAction("com.qq.mobileguard.stopprotect");
                    intent.putExtra("packagename", packname);

                    sendBroadcast(intent);
                    finish();
                } else {
                    ToastUtils.showSafeToast(this,"密码错误");
                }

                break;
        }
    }*/

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        // 当用户输入后退健 的时候。我们进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

    public void btnClick(View v){
        String str = et_pwd.getText().toString();
        switch (v.getId()){
            case R.id.btn_0:
                et_pwd.setText(str + btn_0.getText().toString());
                break;
            case R.id.btn_1:
                et_pwd.setText(str + btn_1.getText().toString());
                break;
            case R.id.btn_2:
                et_pwd.setText(str + btn_2.getText().toString());
                break;
            case R.id.btn_3:
                et_pwd.setText(str + btn_3.getText().toString());
                break;
            case R.id.btn_4:
                et_pwd.setText(str + btn_4.getText().toString());
                break;
            case R.id.btn_5:
                et_pwd.setText(str + btn_5.getText().toString());
                break;
            case R.id.btn_6:
                et_pwd.setText(str + btn_6.getText().toString());
                break;
            case R.id.btn_7:
                et_pwd.setText(str + btn_7.getText().toString());
                break;
            case R.id.btn_8:
                et_pwd.setText(str + btn_8.getText().toString());
                break;
            case R.id.btn_9:
                et_pwd.setText(str + btn_9.getText().toString());
                break;
            case R.id.btn_ok:
                if ("123".equals(str)) {
                    Intent intent = new Intent();
                    intent.setAction("com.qq.mobileguard.stopprotect");
                    intent.putExtra("packagename", packname);

                    sendBroadcast(intent);
                    finish();
                } else {
                    ToastUtils.showSafeToast(this,"密码错误");
                }
                break;
            case R.id.btn_delete:
                str = et_pwd.getText().toString();
                if (str.length() == 0) {
                    return;
                }
                et_pwd.setText(str.substring(0, (str.length() - 1)));
                break;
            case R.id.btn_clear:
                et_pwd.setText("");
                break;
        }

    }

}
