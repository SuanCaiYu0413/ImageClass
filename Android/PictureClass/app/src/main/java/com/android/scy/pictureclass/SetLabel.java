package com.android.scy.pictureclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import InterFace.HttpCallbackListener;
import Model.HistoryLabel;
import Model.LabelModel;
import Model.PictureInfo;
import Model.tagModel;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetLabel extends AppCompatActivity {
    private static final int ADDHISTORYLABEL = 98;
    PictureInfo pic;
    ImageView imgShow;
    RelativeLayout ly;
    DisplayMetrics dm;
    @BindView(R.id.set_toolbar)
    Toolbar setToolbar;
    @BindView(R.id.set_layout)
    RelativeLayout setLayout;
    @BindView(R.id.set_lable_back)
    Button setLableBack;
    private Context context;
    private Point ontouch;
    private long downTime;
    private MyOnTouchListener labelLisener;
    private boolean mIsLongPressed;
    private long LongTime = 1000;
    private final int DELETEFLAG = 100;
    Point xyDian = new Point(0, 0);
    File cachePic = null;
    private final int ADDLABEL = 99;
    PictureInfo currentPic = null;
    int currentViewId;
    private Map<String,LabelModel> labelMap = new HashMap<String,LabelModel>();
    List<TextView> Currentlables = new ArrayList<>();
    final List<PictureInfo> picList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b;
            switch (msg.what) {
                case 0:
                    Glide.with(getApplicationContext()).load(picList.get(1).getUrl()).into(imgShow);
                    currentPic = picList.get(1);
                    picList.remove(1);
                    break;
                case ADDLABEL:
                     b = msg.getData();
                    addLable(b.getInt("x"),b.getInt("y"),b.getString("name"), Integer.parseInt(b.getString("labelId")));
                    break;
                case DELETEFLAG:
                    ly.removeView(findViewById(currentViewId));
                    break;
                case ADDHISTORYLABEL:
                    b = msg.getData();
                    addHistoryLable(b.getInt("x"),b.getInt("y"),b.getString("name"), Integer.parseInt(b.getString("labelId")));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_label);
        ButterKnife.bind(this);
        context = getApplicationContext();
        initActivity();
        Intent intent = getIntent();
        if(intent.getIntExtra("flag",0) == 99){
            initData("1");
            HistoryLabel history = (HistoryLabel) intent.getSerializableExtra("pic");
            currentPic = new PictureInfo();
            currentPic.setId(history.getImgid());
            HttpUtil.sendHttpRequest(context, "pictureId=" + history.getImgid(), HttpUtil.POST, "PictureList/labelList", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString("statusCode").equals("200")){
                            JSONArray array = object.getJSONArray("labels");
                            for(int i=0;i<array.length();i++){
                                JSONObject labelJson = array.getJSONObject(i);
                                Message msg = new Message();
                                Bundle b = new Bundle();
                                b.putString("labelId",labelJson.getString("label_id"));
                                b.putString("name",labelJson.getString("name"));
                                b.putInt("x",Integer.parseInt(labelJson.getString("local_x")));
                                b.putInt("y",Integer.parseInt(labelJson.getString("local_y")));
                                msg.setData(b);
                                msg.what = ADDHISTORYLABEL;
                                mHandler.sendMessage(msg);
                            }
                        }else{
                            StatusCodeDealWith.showDealWith(object.getString("statusCode"),context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
            Glide.with(this).load(history.getUrl()).into(imgShow);
        }else{
            initData("2");
        }
    }

    private void initData(final String number) {
        String postData = "num=" + number + "&phoneNumber=" + DataCache.getString("phoneNumber", context);
        HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "GetPicture", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("tag", DataCache.getString("phoneNumber", context));
                try {
                    JSONObject getPic = new JSONObject(response);
                    DataCache.putString("userId", getPic.getString("userId"), context);
                    JSONArray pics = getPic.getJSONArray("pictures");
                    for (int i = 0; i < pics.length(); i++) {
                        JSONObject jsonPic = pics.getJSONObject(i);
                        PictureInfo pic = new PictureInfo();
                        String md5Url = jsonPic.getString("url");
                        pic.setUrl("http://119.29.194.163/tp/Public/Home/server/" + md5Url.substring(md5Url.length() - 5, md5Url.length()) + "/" + jsonPic.getString("name"));
                        pic.setHeight(Integer.parseInt(jsonPic.getString("height")));
                        pic.setWidth(Integer.parseInt(jsonPic.getString("width")));
                        pic.setType(jsonPic.getString("type"));
                        pic.setId(jsonPic.getString("picture_id"));
                        picList.add(pic);
                    }
                    try {
                        cachePic = Glide.with(getApplicationContext()).load(picList.get(0).getUrl()).downloadOnly(picList.get(0).getWidth(), picList.get(0).getHeight()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (number.equals("2")) {
                        mHandler.sendEmptyMessage(0);
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
            getWindow().setExitTransition(new Explode().setDuration(500));
        }
        setLableBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgShow = (ImageView) findViewById(R.id.img_show);
        ly = (RelativeLayout) findViewById(R.id.set_layout);
        ontouch = new Point(0, 0);
        setToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        setLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToolbar.setVisibility(View.VISIBLE);
            }
        });
        dm = getResources().getDisplayMetrics();
        labelLisener = new MyOnTouchListener();
        imgShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ontouch.x = (int) motionEvent.getRawX();
                        ontouch.y = (int) motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("MOVEx", motionEvent.getX() + "");
                        Log.d("MOVEy", motionEvent.getY() + "");
                        return true;
                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getRawX();
                        int y = (int) motionEvent.getRawY();
                        if (Math.abs(x - ontouch.x) < 15 && Math.abs(y - ontouch.y) < 15) {
                            Intent intent = new Intent(context, AddLabelDiglog.class);
                            intent.putExtra("x", x);
                            intent.putExtra("y", y);
                            startActivityForResult(intent, 1);
                        }
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    final String returnstr = data.getStringExtra("label");
                    final int x = data.getIntExtra("x", 0);
                    final int y = data.getIntExtra("y", 0);
                    if (returnstr.equals("")) {
                        Toast.makeText(context, "标签不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    long time = new Date().getTime();
                    String phoneNumber = DataCache.getString("phoneNumber",context);
                    String lName = null;
                    try {
                        lName = URLEncoder.encode(returnstr,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String postData = "name="+lName+"&local_x="+x+"&local_y="+y+"&perantId=0&pictureId="+currentPic.getId()+"&phoneNumber="+phoneNumber+"&createTime="+String.valueOf(time).toString().substring(0,10).trim()+"&isValid=1";
                    Log.d("canshu",postData);
                    HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "Label", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                            try {
                                JSONObject reJson = new JSONObject(response);
                                if(reJson.getString("statusCode").equals("200")){
                                    LabelModel label = new LabelModel();
                                    label.setLabelId(reJson.getString("labelId"));
                                    label.setName(returnstr);
                                    label.setUserId(reJson.getString("userId"));
                                    label.setPicId(reJson.getString("pictureId"));
                                    labelMap.put(reJson.getString("labelId"),label);
                                    Message msg = new Message();
                                    Bundle b = new Bundle();
                                    b.putString("labelId",reJson.getString("labelId"));
                                    b.putString("name",returnstr);
                                    b.putInt("x",x);
                                    b.putInt("y",y);
                                    msg.setData(b);
                                    msg.what = ADDLABEL;
                                    mHandler.sendMessage(msg);
                                }else {
                                    StatusCodeDealWith.showDealWith(reJson.getString("statusCode"),context);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                    Toast.makeText(context, "添加标签成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETEFLAG:
                if (resultCode == RESULT_OK) {
                    Log.d("tag", "确认返回");
                    LabelModel label = labelMap.get(""+currentViewId);
                    String phoneNumber = DataCache.getString("phoneNumber",context);
                    String postData = "pictureId="+label.getPicId()+"&labelId="+currentViewId+"&phoneNumber="+phoneNumber+"&isValid=0";
                    HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "Imagedit/reMoveLabel", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                            try {
                                JSONObject reJson = new JSONObject(response);
                                if(reJson.getString("statusCode").equals("200")){
                                    mHandler.sendEmptyMessage(DELETEFLAG);
                                }else{
                                    StatusCodeDealWith.showDealWith(reJson.getString("statusCode"),context);
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
    }

    private void addLable(int x,int y,String name,int id){
        TextView label = new TextView(context);
        label.setText("  # " + name);
        label.setId(id);
        label.setTextSize(16);
        label.setTextColor(Color.WHITE);
        label.setBackgroundResource(R.drawable.label);
//                    label.setOnClickListener(new MyOnClickListener());
        label.setOnTouchListener(labelLisener);

        int l = x;
        int t = y;
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(l, t, 0, 0);
        label.setLayoutParams(param);
        ly.addView(label);
        Currentlables.add(label);
    }
    private void addHistoryLable(int x,int y,String name,int id){
        TextView label = new TextView(context);
        label.setText("  # " + name);
        label.setId(id);
        label.setTag(R.id.tag_flag,0);
        label.setTag(R.id.tag_name,name);
        label.setTag(R.id.tag_x,x);
        label.setTag(R.id.tag_y,y);

        label.setTextSize(16);
        label.setTextColor(Color.WHITE);

        label.setBackgroundResource(R.drawable.labelgray);
        label.setOnClickListener(new MyOnClickListener());
////        label.setOnTouchListe9ner(labelLisener);
//        label.setOnClickListener();
        int l = x;
        int t = y;
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(l, t, 0, 0);
        label.setLayoutParams(param);
        ly.addView(label);
        Currentlables.add(label);
    }



    @OnClick(R.id.set_lable_next)
    public void onViewClicked() {
        Glide.with(getApplicationContext()).load(cachePic).into(imgShow);
        for (int i = 0; i < Currentlables.size(); i++) {
            ly.removeView(Currentlables.get(i));
        }
        currentPic = picList.get(0);
        Log.d("imgId",currentPic.getId());
        picList.remove(0);
        initData("1");
    }


    private class MyOnTouchListener implements View.OnTouchListener {

        private int lastX;
        private int lastY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    downTime = event.getDownTime();
                    xyDian.x = lastX;
                    xyDian.y = lastY;
                    LongTime = 500;

                    return true;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
//                    Log.d("ax",dx + ":" + lastX);
//                    Log.d("ay",dy + ":" + lastY);
                    int l = v.getLeft() + dx;
                    int b = v.getBottom() + dy;
                    int r = v.getRight() + dx;
                    int t = v.getTop() + dy;
                    if (l < imgShow.getLeft()) {
                        l = +imgShow.getLeft();
                        r = l + v.getWidth();
                    }
                    if (t < imgShow.getTop()) {
                        t = imgShow.getTop();
                        b = t + v.getHeight();
                    }
                    if (r > imgShow.getWidth() + imgShow.getLeft()) {
                        r = imgShow.getWidth() + imgShow.getLeft();
                        l = r - v.getWidth();
                    }
                    if (b > imgShow.getHeight() + imgShow.getTop()) {
                        b = imgShow.getHeight() + imgShow.getTop();
                        t = b - v.getHeight();
                    }
                    v.layout(l, t, r, b);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    v.postInvalidate();
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    param.setMargins(l, t, 0, 0);
                    v.setLayoutParams(param);
                    if (!mIsLongPressed) {
                        mIsLongPressed = isLongPressed(xyDian.x, xyDian.y, event.getRawX(), event.getRawY(), downTime, event.getEventTime(), LongTime);
                    }
                    Log.d("MOVEx", mIsLongPressed + "");
                    if (mIsLongPressed) {
                        Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show();
                        currentViewId = v.getId();
                        Intent i = new Intent(context, Diglog.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", "确定删除此标签吗?");
                        i.putExtra("key", bundle);
                        startActivityForResult(i, DELETEFLAG);
                        LongTime *= 100000;
                        mIsLongPressed = false;
                    } else {

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mIsLongPressed = false;
                    return true;
            }
            return true;
        }
    }

    static boolean isLongPressed(float lastX, float lastY, float thisX,
                                 float thisY, long lastDownTime, long thisEventTime,
                                 long longPressTime) {
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        Log.d("ad", lastDownTime + ":" + thisEventTime);
        Log.d("ax", thisX + ":" + lastX);
        Log.d("ay", thisY + ":" + lastY);
        if (offsetX <= 30 && offsetY <= 30 && intervalTime >= longPressTime) {
            return true;
        }
        return false;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView label = (TextView) v;
            if((int)label.getTag(R.id.tag_flag)==0){
                label.setTag(R.id.tag_flag,1);
                label.setBackgroundResource(R.drawable.label);
                long time = new Date().getTime();
                String phoneNumber = DataCache.getString("phoneNumber",context);
                String lName = null;
                try {
                    lName = URLEncoder.encode(label.getTag(R.id.tag_name).toString().trim(),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int x = (int) label.getTag(R.id.tag_x);
                int y = (int) label.getTag(R.id.tag_y);
                String postData = "name="+lName+"&local_x="+x+"&local_y="+y+"&perantId=0&pictureId="+currentPic.getId()+"&phoneNumber="+phoneNumber+"&createTime="+String.valueOf(time).toString().substring(0,10).trim()+"&isValid=1";
                Log.d("canshu",postData);
                HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "Label", new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {

                        try {
                            JSONObject reJson = new JSONObject(response);
                            if(reJson.getString("statusCode").equals("200")){
                                mHandler.sendEmptyMessage(97);
                            }else {
                                StatusCodeDealWith.showDealWith(reJson.getString("statusCode"),context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }else if((int)label.getTag(R.id.tag_flag)==1){
                label.setTag(R.id.tag_flag,0);
                label.setBackgroundResource(R.drawable.labelgray);
                String phoneNumber = DataCache.getString("phoneNumber",context);
                String postData = "pictureId="+currentPic.getId()+"&labelId="+label.getId()+"&phoneNumber="+phoneNumber+"&isValid=0";
                HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "Imagedit/reMoveLabel", new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {

                        try {
                            JSONObject reJson = new JSONObject(response);
                            if(reJson.getString("statusCode").equals("200")){

                            }else{
                                StatusCodeDealWith.showDealWith(reJson.getString("statusCode"),context);
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
    }
}
