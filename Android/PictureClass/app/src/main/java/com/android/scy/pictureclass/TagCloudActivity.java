package com.android.scy.pictureclass;

import InterFace.HttpCallbackListener;
import Model.LabelModel;
import Model.Tag;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Model.TagCloudView;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.ButterKnife;

public class TagCloudActivity extends AppCompatActivity  {
    private TagCloudView mTagCloudView;
    private final List<Tag> tempList = new ArrayList<Tag>();
    private List<String> tagList = new ArrayList<String>();
    private List<String> labels = new ArrayList<>();
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();
                    int height = display.getHeight();

                    mTagCloudView = new TagCloudView(getApplicationContext(), width, height,tempList, labels ); //通过当前上下文
                    setContentView(mTagCloudView);
                    mTagCloudView.requestFocus();
                    mTagCloudView.onTagClickListener = new OnTagClick();
                    mTagCloudView.setFocusableInTouchMode(true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }
    private  void initData(){

        HttpUtil.sendHttpRequest(getApplicationContext(), "", HttpUtil.POST, "CloudTag", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                JSONArray tagArr = null;
                try {
                    tagArr = new JSONArray(response);
                    for(int i=0;i<tagArr.length();i++){
                        JSONObject tag = tagArr.getJSONObject(i);
                        tempList.add(new Tag(tag.getString("name"), Integer.parseInt(tag.getString("value")), tag.getString("value")));
                    }
                    getMeHobby();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getMeHobby(){
        String phoneNumber = DataCache.getString("phoneNumber",getApplicationContext());
        HttpUtil.sendHttpRequest(getApplicationContext(), "phoneNumber=" + phoneNumber, HttpUtil.POST, "LabelManage", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    if(result.getString("statusCode").equals("200")){
                        JSONArray arr = result.getJSONArray("tags");
                        for (int i = 0 ; i < arr.length() ; i++ ){
                            labels.add(arr.getJSONObject(i).getString("name"));
                        }
                        Log.d("tags",labels.toString());
                    }else{
                        StatusCodeDealWith.showDealWith(result.getString("statusCode"),getApplicationContext());
                    }
                    mHandler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    class OnTagClick implements TagCloudView.OnCloudTagClickListener {
        @Override
        public void onTagClick(View view) {
            TextView mTextView =  ((TextView)view);
            if(mTextView.getTag() == null ||  ((int)mTextView.getTag()) == 11){
                mTextView.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.tag_border));
                mTextView.setTag(1);
                tagList.add(mTextView.getText().toString().trim());
                writeTag(mTextView.getText().toString().trim(),1);
            }else{
                mTextView.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.tag_no_border));
                mTextView.setTag(11);
                for(int i = 0 ; i < tagList.size() ; i++){
                    if(mTextView.getText().toString().trim() == tagList.get(i)){
                        tagList.remove(i);
                    }
                }
                writeTag(mTextView.getText().toString().trim(),0);
            }
            Log.d("tags",tagList.toString());
        }
    }



    private void writeTag(String name,final int flag){
        String phoneNumber = DataCache.getString("phoneNumber",getApplicationContext());
        String lName = null;
        try {
            lName = URLEncoder.encode(name,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String postData = "phoneNumber="+phoneNumber+"&labelName="+lName;
        HttpUtil.sendHttpRequest(getApplicationContext(), postData, HttpUtil.POST,flag==1?"LabelManage/addTag":"LabelManage/reTag", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    if(result.getString("statusCode").equals("200")){

                    }else{
                        StatusCodeDealWith.showDealWith(result.getString("statusCode"),getApplicationContext());
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
}
