package com.google.mobilesafe.ui.fragment;

import java.util.List;

import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.abslistview.CommonAdapter;
import com.google.mobilesafe.db.dao.BlackNumberDao;
import com.google.mobilesafe.domain.BlackNumberInfo;
import com.google.mobilesafe.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
public class FirewallBlackFragment extends Fragment implements OnClickListener {

	private View view;

	@Bind(R.id.empty)
	 ImageView empty;

	@Bind(R.id.list_view)
	 ListView list_view;

	private BlackNumberDao dao;

	private List<BlackNumberInfo> list;

	private BlackAdapter adapter;

	@Bind(R.id.btn_addBlackNumber)
	 FloatingActionButton btn_add;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_firewall_black, null);
		ButterKnife.bind(this,view);
		btn_add.setOnClickListener(this);
		list_view.setEmptyView(empty);

		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		dao = new BlackNumberDao(getActivity());
		list = dao.findAll();
		adapter = new BlackAdapter(getActivity(),
				R.layout.item_list_blackcontact, list);
		list_view.setAdapter(adapter);
	}

	/**
	 * 去掉重影
	 */
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (getView() != null) {
			getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_addBlackNumber:
			addBlackNumber();
			break;
		}
	}

	private void addBlackNumber() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialog_view = View.inflate(getActivity(),
				R.layout.dialog_add_blacknumber, null);

		final AlertDialog dialog = builder.create();

		final EditText et_blacknumber = (EditText) dialog_view
				.findViewById(R.id.et_blacknumber);
		final CheckBox cbPhone = (CheckBox) dialog_view
				.findViewById(R.id.cb_phone);
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
					Toast.makeText(getActivity(), "号码不能为空", 1).show();
					return;
				} else {

					String mode = "";

					if (cbPhone.isChecked() && cbSms.isChecked()) {
						mode = "电话拦截+短信拦截";
					} else if (cbSms.isChecked()) {
						mode = "短信拦截";
					} else if (cbPhone.isChecked()) {
						mode = "电话拦截";
					} else {
						Toast.makeText(getActivity(), "请选择拦截模式", 1).show();
						return;
					}
					boolean result = dao.add(blacknumber, mode);
					if (result) {
						BlackNumberInfo numberInfo = new BlackNumberInfo();
						numberInfo.mode = mode;
						numberInfo.number = blacknumber;
						list.add(0, numberInfo);
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						} else {
							adapter = new BlackAdapter(getActivity(),
									R.layout.item_list_blackcontact, list);
							list_view.setAdapter(adapter);
						}
					}
					dialog.dismiss();
				}

			}
		});

		dialog.setView(dialog_view, 0, 0, 0, 0);
		dialog.show();

		// final NiftyDialogBuilder builder = NiftyDialogBuilder
		// .getInstance(getActivity());
		// View dialog_view = View.inflate(getActivity(),
		// R.layout.dialog_add_blacknumber, null);
		// final EditText et_blacknumber = (EditText) dialog_view
		// .findViewById(R.id.et_blacknumber);
		// final CheckBox cbPhone = (CheckBox) dialog_view
		// .findViewById(R.id.cb_phone);
		// final CheckBox cbSms = (CheckBox)
		// dialog_view.findViewById(R.id.cb_sms);
		//
		// builder.withTitle("添加黑名单号码")
		// // .withTitle(null) no title
		// .withTitleColor("#000000")
		// // def
		// .withDividerColor("#11000000")
		// // def
		// // .withMessage("确认退出手机管家吗？")
		// // .withMessage(null) no Msg
		// //.withMessageColor("#FFFFFF")
		// // def
		// // .withIcon(getResources().getDrawable(R.drawable.icon))
		// .isCancelableOnTouchOutside(true) // def | isCancelable(true)
		// .withDuration(700) // def
		// .withEffect(new SlideTop()) // def Effectstype.Slidetop
		// .withButton1Text("确定") // def gone
		// .withButton2Text("取消") // def gone
		// .setCustomView(dialog_view, getActivity()) // .setCustomView(View
		// // or
		// // ResId,context)
		// .setButton1Click(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// String blacknumber = et_blacknumber.getText()
		// .toString().trim();
		// if (TextUtils.isEmpty(blacknumber)) {
		// Toast.makeText(getActivity(), "号码不能为空", 1).show();
		// return;
		// } else {
		//
		// String mode = "";
		//
		// if (cbPhone.isChecked() && cbSms.isChecked()) {
		// mode = "电话拦截+短信拦截";
		// } else if (cbSms.isChecked()) {
		// mode = "短信拦截";
		// } else if (cbPhone.isChecked()) {
		// mode = "电话拦截";
		// } else {
		// Toast.makeText(getActivity(), "请选择拦截模式", 1)
		// .show();
		// return;
		// }
		// boolean result = dao.add(blacknumber, mode);
		// if (result) {
		// BlackNumberInfo numberInfo = new BlackNumberInfo();
		// numberInfo.setMode(mode);
		// numberInfo.setNumber(blacknumber);
		// list.add(0, numberInfo);
		// if (adapter != null) {
		// adapter.notifyDataSetChanged();
		// } else {
		// adapter = new BlackAdapter(list,
		// getActivity(), dao);
		// list_view.setAdapter(adapter);
		// }
		// }
		//
		// }
		// builder.cancel();
		// }
		// }).setButton2Click(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// builder.cancel();
		// }
		// }).show();

	}

	private class BlackAdapter extends CommonAdapter<BlackNumberInfo> {

		public BlackAdapter(Context context, int layoutId,
				List<BlackNumberInfo> datas) {
			super(context, layoutId, datas);
		}

		@Override
		public void convert(ViewHolder holder, final BlackNumberInfo t) {
			holder.setText(R.id.tv_phone, t.number);
			holder.setText(R.id.tv_mode, t.mode);
			holder.setOnClickListener(R.id.ib_delete, new OnClickListener() {

				@Override
				public void onClick(View v) {
					dao.delete(t.number);
					list.remove(t);
					notifyDataSetChanged();
				}
			});
		}

	}

}
