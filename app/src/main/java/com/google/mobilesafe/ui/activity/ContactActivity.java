package com.google.mobilesafe.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.mobilesafe.R;
import com.google.mobilesafe.base.BaseActivity;
import com.google.mobilesafe.domain.ContactInfo1;
import com.google.mobilesafe.engine.ContactInfoParser;
import com.google.mobilesafe.ui.adapter.commonadapter.ViewHolder;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.CommonAdapter;
import com.google.mobilesafe.ui.adapter.commonadapter.recyclerview.OnItemClickListener;
import com.google.mobilesafe.ui.widget.QuickIndexBar;
import com.google.mobilesafe.ui.widget.QuickIndexBar.OnLetterUpdateListener;
import com.google.mobilesafe.ui.widget.SearchEditText;
import com.google.mobilesafe.ui.widget.TextDrawable;
import com.google.mobilesafe.utils.ColorGenerator;
import com.google.mobilesafe.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactActivity extends BaseActivity {

    /* @Bind(R.id.lv_contact)
     ListView lv_contact;*/
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_center)
    TextView tv_center;
    @Bind(R.id.bar)
    QuickIndexBar bar;
    @Bind(R.id.et_search)
    SearchEditText et_search;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private Handler mHandler = new Handler();
    private ContactAdapter adapter;
    private List<ContactInfo1> list;
    private ColorGenerator mGenerator = ColorGenerator.DEFAULT;
    private SparseArray<TextDrawable> array;

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void initData() {
        list = ContactInfoParser.findAll(this);
        Collections.sort(list);
        adapter = new ContactAdapter(ContactActivity.this, R.layout.item_contact, list);
        //lv_contact.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        array = new SparseArray<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i).name.substring(0, 1);
            array.put(i, TextDrawable.builder().buildRound(text, mGenerator.getColor(text)));
        }
    }

    @Override
    public void initListener() {
        bar.setListener(new OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                showLetter(letter);
                for (int i = 0; i < list.size(); i++) {
                    ContactInfo1 info = list.get(i);
                    String initial = info.pinyin.charAt(0) + "";
                    if (TextUtils.equals(letter, initial)) {
                        //lv_contact.setSelection(i);
                        mRecyclerView.getLayoutManager().scrollToPosition(i);
                        break;
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener<ContactInfo1>(){

            @Override
            public void onItemClick(ViewGroup parent, View view, ContactInfo1 contactInfo, int position) {
                Intent intent = new Intent();
                intent.putExtra("phone", list.get(position).phone);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, ContactInfo1 contactInfo1, int position) {
                return false;
            }
        });

        // 根据输入框输入值的改变来过滤搜索
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 显示字母
     *
     * @param letter
     */
    protected void showLetter(String letter) {
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText(letter);
        bar.setBackgroundColor(Color.rgb(191, 191, 191));

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_center.setVisibility(View.GONE);
                bar.setBackgroundColor(Color.TRANSPARENT);
            }
        }, 250);

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactInfo1> filterDateList = new ArrayList<ContactInfo1>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = list;
        } else {
            filterDateList.clear();
            for (ContactInfo1 contactinfo : list) {
                String name = contactinfo.name;
                if (name.indexOf(filterStr.toString()) != -1
                        || PinyinUtils.getPinyin(name).startsWith(filterStr.toString())) {
                    filterDateList.add(contactinfo);
                }
            }
        }

        Collections.sort(filterDateList);
        adapter.updateListView(filterDateList);
    }

    private class ContactAdapter extends CommonAdapter<ContactInfo1> {

        public ContactAdapter(Context context, int layoutId, List<ContactInfo1> datas) {
            super(context, layoutId, datas);
        }

        public void updateListView(List<ContactInfo1> list) {
            mDatas = list;
            notifyDataSetChanged();
        }

        @Override
        public void convert(ViewHolder holder, ContactInfo1 contactInfo) {
            int position = getPosition(holder);
            String str = null;
            String currentLetter = contactInfo.pinyin.charAt(0) + "";
            if (position == 0) {
                str = currentLetter;
            } else {
                String preLetter = mDatas.get(position - 1).pinyin.charAt(0) + "";
                if (!TextUtils.equals(preLetter, currentLetter)) {
                    str = currentLetter;
                }
            }

            if (position < mDatas.size() - 1){
                String nextLetter = mDatas.get(position + 1).pinyin.charAt(0) + "";
                holder.setVisible(R.id.listDivider, TextUtils.equals(nextLetter, currentLetter));
            }
            holder.setVisible(R.id.tv_index, str != null );
            holder.setText(R.id.tv_index, currentLetter);
            holder.setImageDrawable(R.id.icon, array.get(position));
            holder.setText(R.id.tv_name, contactInfo.name);
            holder.setText(R.id.tv_number, contactInfo.phone);
        }
    }

   /* private class ContactAdapter extends CommonAdapter<ContactInfo1> {

        public ContactAdapter(Context context, int layoutId, List<ContactInfo1> datas) {
            super(context, layoutId, datas);
        }

        public void updateListView(List<ContactInfo1> list) {
            mDatas = list;
            notifyDataSetChanged();
        }

        @Override
        public void convert(ViewHolder holder, ContactInfo1 contactInfo) {
            int position = holder.getPos();
            String str = null;
            String currentLetter = contactInfo.pinyin.charAt(0) + "";
            if (position == 0) {
                str = currentLetter;
            } else {
                String preLetter = mDatas.get(position - 1).pinyin.charAt(0) + "";
                if (!TextUtils.equals(preLetter, currentLetter)) {
                    str = currentLetter;
                }
            }

            holder.setVisible(R.id.tv_index, str == null ? false : true);
            holder.setText(R.id.tv_index, currentLetter);
            holder.setImageDrawable(R.id.icon, array.get(position));
            holder.setText(R.id.tv_name, contactInfo.name);
            holder.setText(R.id.tv_number, contactInfo.phone);
        }
    }*/

}
