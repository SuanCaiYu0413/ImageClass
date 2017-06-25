package com.android.scy.pictureclass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import InterFace.HttpCallbackListener;
import Model.HobbyLabel;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class hobbyActivity extends AppCompatActivity {

    @BindView(R.id.hobby_title)
    TextView hobbyTitle;
    @BindView(R.id.hobby_btnRight)
    Button hobbyBtnRight;
    @BindView(R.id.hobby_toolbar)
    Toolbar hobbyToolbar;
    @BindView(R.id.activity_hobby)
    FlexboxLayout activityHobby;
    List<HobbyLabel> hobby = new ArrayList<>();
    List<Integer> Change = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);
        ButterKnife.bind(this);
        initData();
        initActivity();
    }

    private void initActivity() {
        hobbyToolbar.setNavigationIcon(R.drawable.back);
        hobbyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        hobbyBtnRight.setText("保存");
        hobbyTitle.setText("选择几个喜欢的吧");
        for (int i = 0; i < hobby.size(); i++) {
            FlexboxLayout.LayoutParams param = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.MATCH_PARENT);
            TextView btn = new TextView(getApplicationContext());
            btn.setText(hobby.get(i).getName().toString().trim());
            btn.setId(hobby.get(i).getId());
//            btn.setAllCaps(false);
            param.setMargins(18, 18, 18, 18);
            if(hobby.get(i).isChoose()){
                btn.setTag(1);
                btn.setBackgroundColor(Color.rgb(94, 207, 186));
            }else {
                btn.setTag(0);
                btn.setBackgroundColor(Color.rgb(236, 236, 236));
            }
            btn.setTextSize(16);
            btn.setPadding(15, 20, 15, 20);
            btn.setTextColor(Color.rgb(102, 102, 102));
            btn.setOnClickListener(new HobbyOnClickListener());
            btn.setLayoutParams(param);
            activityHobby.addView(btn);
        }
    }

    private void initData() {
        Intent hobbyIntent = getIntent();
        String[] localHobby = DataCache.getString("hobbyList",getApplicationContext()).trim().split("--");
        List<String> localHobbyList = Arrays.asList(localHobby);
        if(hobbyIntent!=null){
            Bundle b = hobbyIntent.getBundleExtra("value");
            try {
                JSONArray json = new JSONArray(b.getString("hobbyList"));
                for(int i=0;i<json.length();i++){
                    JSONObject jo = json.getJSONObject(i);
                    HobbyLabel h = new HobbyLabel(jo.getString("hobby"),Integer.parseInt(jo.getString("hobby_id")));
                    if(localHobbyList.contains(jo.getString("hobby_id").trim())){
                        h.setChoose(true);
                        Change.add(Integer.parseInt(jo.getString("hobby_id")));
                    }
                    hobby.add(h);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.hobby_btnRight)
    public void onViewClicked() {
        if(Change.size() < 1) return;
        JSONArray hobby = new JSONArray();
        String changeList = "";
        for (int i= 0;i<Change.size();i++){
            changeList += i==Change.size()-1?Change.get(i)+"":Change.get(i)+"--";
            JSONObject hobbyItem = new JSONObject();
            try {
                hobbyItem.put("hobbyId",Change.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hobby.put(hobbyItem);
        }
        Log.d("save",changeList);
        DataCache.putString("hobbyList",changeList,getApplicationContext());
        String parma = "phoneNumber="+ DataCache.getString("phoneNumber",getApplicationContext()).trim()+"&hobby="+String.valueOf(hobby);
        Log.d("tag",parma);
        HttpUtil.sendHttpRequest(getApplicationContext(), parma, HttpUtil.POST, "UserInfo/setUserHobby", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                JSONObject reJson = null;
                try {
                    reJson = new JSONObject(response);
                    if(reJson.getString("statusCode").equals("200")){
                        Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                        finish();
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

    private class HobbyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TextView btn = (TextView) view;
            if ((int) btn.getTag() == 0) {
                btn.setTag(1);
                Change.add(btn.getId());
                btn.setBackgroundColor(Color.rgb(94, 207, 186));
            } else {
                btn.setTag(0);
                for (int i = 0; i < Change.size(); i++) {
                    if (Change.get(i) == btn.getId()) {
                        Change.remove(i);
                        break;
                    }
                }
                btn.setBackgroundColor(Color.rgb(236, 236, 236));
            }
            Log.d("List", Change.toString());
        }
    }

}
