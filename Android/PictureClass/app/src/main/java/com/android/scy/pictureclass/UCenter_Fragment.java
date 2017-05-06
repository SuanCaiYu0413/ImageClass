package com.android.scy.pictureclass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Adapter.UCenterAdapter;
import Model.ResultItem;

/**
 * Created by Administrator on 2017/5/3.
 */

public class UCenter_Fragment extends Fragment implements AdapterView.OnItemClickListener {
    private MainActivity mainActivity;
    private List<ResultItem> itemList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ucenter_fragment,container,false);

        Log.d("xxx","asdasd");
        initFragment(view);
        return view;
    }

    private void initFragment(View v) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportFragmentManager();
        mainActivity.toolbar.setNavigationIcon(R.drawable.skatemenu);
        mainActivity.title.setText("个人中心");
        mainActivity.btnRight.setText("");
        mainActivity.btnRight.setOnClickListener(null);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        itemList = new ArrayList<ResultItem>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);;
        String userName = sharedPreferences.getString("userName",null);
        Log.d("xxxxa",userName);
        ResultItem header;
        if(userName != null){
            header = new ResultItem(0,userName,R.drawable.header);
        }else {
            header = new ResultItem(0,"来到南航我们吃什么",R.drawable.header);
        }
        ResultItem placeholder1 = new ResultItem(2,"",0);
        ResultItem modify = new ResultItem(1,"信息修改",R.drawable.modify);
        ResultItem cache = new ResultItem(1,"清理缓存",R.drawable.cache);
        ResultItem placeholder2 = new ResultItem(2,"",0);
        ResultItem exit = new ResultItem(1,"退出登录",R.drawable.exit);
        itemList.add(0,header);
        itemList.add(1,placeholder1);
        itemList.add(2,modify);
        itemList.add(3,cache);
        itemList.add(4,placeholder2);
        itemList.add(5,exit);
        ListView lv = (ListView) v.findViewById(R.id.ucenter_list);
        Log.d("debug",lv + "");
        UCenterAdapter adapter = new UCenterAdapter(getContext(),itemList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ResultItem item = itemList.get(i);
        switch (item.getTitle()){
            case "清理缓存":
                Intent intent = new Intent(mainActivity,CleanCacheDiglog.class);
                startActivity(intent);
                break;
            case "退出登录":
                SharedPreferences sharedPreferences =  mainActivity.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("sid",null);
                edit.commit();
                new ReplaceFragment(mainActivity,new WelcomeFragment(),1).load();
                break;
        }
    }
}
