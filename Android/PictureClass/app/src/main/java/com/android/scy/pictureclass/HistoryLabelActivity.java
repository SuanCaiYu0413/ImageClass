package com.android.scy.pictureclass;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.idisfkj.mylibrary.EnhanceRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.HistoryAdapter;
import InterFace.HttpCallbackListener;
import Model.HistoryLabel;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryLabelActivity extends AppCompatActivity {
    List<HistoryLabel> pics = new ArrayList<>();
    @BindView(R.id.history_toolbar) Toolbar toolbar;
    @BindView(R.id.history_recyclerview)
    EnhanceRecyclerView mrv;
    private final int INIT = 0;
    private final int LOADEVENT = 1;
    private  int limit = 1;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT:
                    initActivity();
                    break;
                case LOADEVENT:
                    mrv.setRefreshComplete();
                    mrv.setLoadMoreComplete();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_label);
        ButterKnife.bind(this);
        initPics();
//        initActivity();
    }

    private void initPics() {
        String phoneNumber = DataCache.getString("phoneNumber",getApplicationContext());
        String postData = "phoneNumber="+phoneNumber+"&limit="+limit;
        HttpUtil.sendHttpRequest(getApplicationContext(), postData, HttpUtil.POST, "PictureList", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject reJson = new JSONObject(response);
                    if(reJson.getString("statusCode").equals("200")){
                        JSONArray pictures = reJson.getJSONArray("pics");
                        for(int i=0;i<pictures.length();i++){
                            JSONObject pic = pictures.getJSONObject(i);
                            HistoryLabel history = new HistoryLabel();
                            history.setImgid(pic.getString("picture_id"));
                            String url = pic.getString("url");
                            String[] file = url.split("\\.");
                            Log.d("file",file[0]);
                            history.setUrl("http://119.29.194.163/tp/Public/Home/server/"+file[0].substring(file[0].length()-5,file[0].length())+"/"+file[0].substring(0,file[0].length()-5)+"."+file[1]);
                            List<String> labels = new ArrayList<String>();
                            JSONArray labelsJson = pic.getJSONArray("lable");
                            for(int k=0;k<labelsJson.length();k++){
                                JSONObject label = labelsJson.getJSONObject(k);
                                labels.add(label.getString("name"));
                            }
                            history.setLabel((String[]) labels.toArray(new String[0]));
                            pics.add(history);
                        }
                        if(limit==1){
                            myHandler.sendEmptyMessage(INIT);
                        }else{
                            myHandler.sendEmptyMessage(LOADEVENT);
                        }
                    }else{
//                        StatusCodeDealWith.showDealWith(reJson.getString("statusCode"),getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(500));
            getWindow().setReturnTransition(new Explode().setDuration(500));
        }
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrv.setLayoutManager(layoutManager);
        Log.d("length",pics.size()+"");
        HistoryAdapter adapter = new HistoryAdapter(pics);
        mrv.setAdapter(adapter);
        mrv.initView();
        mrv.setLoadMoreListener(new EnhanceRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
        mrv.setPullToRefreshListener(new EnhanceRecyclerView.PullToRefreshListener() {
            @Override
            public void onRefreshing() {
//                Log.d("sad","onRefreshing");
                myHandler.sendEmptyMessageDelayed(LOADEVENT,3000);
            }
        });

        adapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
                Log.d("debug",pics.get(position).getUrl());
                Intent intent = new Intent(HistoryLabelActivity.this,AddLabelActivity.class);
                intent.putExtra("pic",pics.get(position));
                intent.putExtra("flag",99);
                startActivity(intent);
            }
        });
    }

    private void loadMoreData() {
        limit++;
        initPics();
        myHandler.sendEmptyMessage(LOADEVENT);
//        mrv.setLoadMoreComplete();
    }
}
