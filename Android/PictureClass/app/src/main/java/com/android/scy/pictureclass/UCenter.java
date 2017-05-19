package com.android.scy.pictureclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapter.UCenterAdapter;
import Model.ResultItem;
import Public_Class.DataCache;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UCenter extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Context context;
    @BindView(R.id.ucenter_title)
    TextView ucenterTitle;
    @BindView(R.id.ucenter_toolbar)
    Toolbar ucenterToolbar;
    @BindView(R.id.ucenter_list)
    ListView ucenterList;
    @BindView(R.id.fragment)
    LinearLayout fragment;
    @BindView(R.id.activity_ucenter)
    RelativeLayout activityUcenter;
    private List<ResultItem> itemList;

    @Override
    protected void onStart() {
        super.onStart();
        String userName = DataCache.getString("userName", context);
        String sid = DataCache.getString("sid", context);
        String phoneNumber = DataCache.getString("phoneNumber", context);
        if(!(userName != null && sid != null && phoneNumber != null)){
            Intent welcome = new Intent(this,Welcome.class);
            startActivity(welcome);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucenter);
        ButterKnife.bind(this);
        context = getApplicationContext();
        initActivity();
        itemInit();
    }

    private void itemInit() {
        itemList = new ArrayList<ResultItem>();
        String userName = DataCache.getString("userName",getApplicationContext());
        ResultItem header;
        if(userName != null){
            header = new ResultItem(0,userName,R.drawable.header);
        }else {
            header = new ResultItem(0,"未登录",R.drawable.header);
        }
        ResultItem placeholder1 = new ResultItem(2,"",0);
        ResultItem modify = new ResultItem(1,"信息修改",R.drawable.modify);
        ResultItem cache = new ResultItem(1,"清理缓存",R.drawable.cache);
        ResultItem placeholder2 = new ResultItem(2,"",0);
        ResultItem rePwd = new ResultItem(1,"修改密码",R.drawable.repwd);
        ResultItem exit = new ResultItem(1,"退出登录",R.drawable.exit);
        itemList.add(0,header);
        itemList.add(1,placeholder1);
        itemList.add(2,modify);
        itemList.add(3,cache);
        itemList.add(4,placeholder2);
        itemList.add(5,rePwd);
        itemList.add(6,exit);
        UCenterAdapter adapter = new UCenterAdapter(getApplicationContext(),itemList);
        ucenterList.setAdapter(adapter);
        ucenterList.setOnItemClickListener(this);
    }

    private void initActivity() {
        ucenterTitle.setText("个人中心");
        ucenterToolbar.setNavigationIcon(R.drawable.back);
        ucenterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ResultItem item = itemList.get(i);
        switch (item.getTitle()){
            case "清理缓存":
                Intent intent = new Intent(this,CleanCacheDiglog.class);
                startActivity(intent);
                break;
            case "退出登录":
                DataCache.putString("userName",null,getApplicationContext());
                DataCache.putString("phoneNumber",null,getApplicationContext());
                DataCache.putString("sid",null,getApplicationContext());
                Intent in = new Intent(this,Welcome.class);
                startActivity(in);
                finish();
                break;
            case "修改密码":
                Intent rePwd = new Intent(this,ForgetPassword.class);
                startActivityForResult(rePwd,1);
                break;
            case "信息修改":
                Intent modify = new Intent(this,ModifyInfo.class);
                startActivity(modify);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                finish();
                break;
        }
    }
}
