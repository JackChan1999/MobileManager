package com.google.mobilesafe.dialog;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseDialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends BaseDialog {

	private String title;
	private String message;
	private TextView tv_dialog_title;
	private TextView tv_dialog_message;
	private Button bt_dialog_cancel;
	private Button bt_dialog_confirm;
	private OnConfirmListener onConfirmListener;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	protected ConfirmDialog(Context context) {
		super(context);
	}

	public static void showDialog(Context context, String title, String message, OnConfirmListener confirmListener){
		ConfirmDialog dialog = new ConfirmDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setConfirmListener(confirmListener);
		dialog.show();
	}
	
	@Override
	public void initView() {
		setContentView(R.layout.dialog_confirm);
		
		tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
		tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
		
		bt_dialog_cancel = (Button) findViewById(R.id.bt_dialog_cancel);
		bt_dialog_confirm = (Button) findViewById(R.id.bt_dialog_confirm);

	}

	@Override
	public void initListener() {
		bt_dialog_cancel.setOnClickListener(this);
		bt_dialog_confirm.setOnClickListener(this);

	}

	@Override
	public void initData() {
		tv_dialog_title.setText(title);
		tv_dialog_message.setText(message);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_dialog_cancel:
			if(onConfirmListener != null){
				onConfirmListener.onCancel();
			}
			break;
		case R.id.bt_dialog_confirm:
			if(onConfirmListener != null){
				onConfirmListener.onConfirm();
			}
			break;
			
		}
		dismiss();
	}
	
	
	
	public void setConfirmListener(OnConfirmListener confirmListener) {
		this.onConfirmListener = confirmListener;
	}



	public interface OnConfirmListener{
		void onCancel();
		void onConfirm();
	}
}
