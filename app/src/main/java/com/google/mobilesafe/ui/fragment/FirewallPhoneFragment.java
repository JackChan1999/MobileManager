package com.google.mobilesafe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirewallPhoneFragment extends Fragment {

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity());
		textView.setText("电话拦截");

		return textView;

	}

	/**
	 * 去掉重影
	 */
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if (getView() != null) {
			getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.INVISIBLE);
		}
	}

}
