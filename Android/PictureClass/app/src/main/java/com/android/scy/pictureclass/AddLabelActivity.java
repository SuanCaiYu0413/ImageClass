package com.android.scy.pictureclass;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import InterFace.HttpCallbackListener;
import Model.HistoryLabel;
import Model.LabelModel;
import Model.PictureInfo;
import Model.TagCloudViewMe;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddLabelActivity extends AppCompatActivity implements TagCloudViewMe.OnTagClickListener,TagCloudViewMe.OnLongTagClickListener {

    @BindView(R.id.add_img_show)
    ImageView addImgShow;
    @BindView(R.id.tag_cloud_view)
    TagCloudViewMe tagCloudView;
    @BindView(R.id.show_img)
    LinearLayout showImg;
    @BindView(R.id.set_lable_back)
    Button setLableBack;
    @BindView(R.id.app_title)
    TextView appTitle;
    @BindView(R.id.set_lable_next)
    Button setLableNext;
    @BindView(R.id.add_toolbar)
    Toolbar addToolbar;
    @BindView(R.id.activity_add_label)
    RelativeLayout activityAddLabel;
    @BindView(R.id.me_cloud_view)
    TagCloudViewMe meCloudView;

    private Context context;
    private List<PictureInfo> picList = new ArrayList<>();
    private File cachePic = null;
    private PictureInfo currentPic = null;
    private View waitDelView;
    private LabelModel waitDelLabel;

    private final int WINDOW_ADD = 1;
    private final int LABLE_ADD = 2;
    private final int SHOW_IMG = 3;
    private final int ADDHISTORYLABEL = 4;
    private final int DEL_LABEL = 5;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LABLE_ADD:
                    Bundle b = msg.getData();
                    LabelModel labelModel = new LabelModel();
                    labelModel.setLabelId(b.getString("labelId"));
                    labelModel.setName(b.getString("name"));
                    meCloudView.addTag(labelModel);
                    break;
                case SHOW_IMG:
                    Glide.with(getApplicationContext()).load(picList.get(1).getUrl()).into(addImgShow);
                    currentPic = picList.get(1);
                    getPicLabels();
                    picList.remove(1);
                    break;
                case ADDHISTORYLABEL:
                    Bundle bundle = msg.getData();
                    List<LabelModel> list_me = (ArrayList<LabelModel>) bundle.getSerializable("labels_me");
                    List<LabelModel> list_other = (ArrayList<LabelModel>) bundle.getSerializable("labels_other");
                    meCloudView.setTags(list_me);
                    tagCloudView.setTags(list_other);
                    Log.d("tag",list_me.toString());
                    Log.d("tag",list_other.toString());
                    break;
                case DEL_LABEL:
                    meCloudView.reView(waitDelView);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label);
        ButterKnife.bind(this);
        context = getApplicationContext();
        List<LabelModel> tags = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(500));
            getWindow().setExitTransition(new Explode().setDuration(500));
        }
        for (int i = 0; i < 10; i++) {
            LabelModel la = new LabelModel();
            la.setName("标签"+i);
            tags.add(la);
        }

        tagCloudView.setOnTagClickListener(this);
        meCloudView.setOnLongTagClickListener(this);
        historyLabel();
    }

    private void historyLabel() {
        Intent intent = getIntent();
        if(intent.getIntExtra("flag",0) == 99){
            initData("1");
            HistoryLabel history = (HistoryLabel) intent.getSerializableExtra("pic");
            Glide.with(this).load(history.getUrl()).into(addImgShow);
            currentPic = new PictureInfo();
            currentPic.setId(history.getImgid());
            getPicLabels();
        }else{
            initData("2");
        }
    }

    private void initData(final String num) {
        String postData = "num=" + num + "&phoneNumber=" + DataCache.getString("phoneNumber", context);
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

                    if (num.equals("2")) {
                        mHandler.sendEmptyMessage(SHOW_IMG);
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

    private void getPicLabels(){
        meCloudView.clearAll();
        tagCloudView.clearAll();
        String phoneNumber = DataCache.getString("phoneNumber", context);
        HttpUtil.sendHttpRequest(context, "pictureId=" + currentPic.getId()+"&phoneNumber="+phoneNumber, HttpUtil.POST, "PictureList/labelList", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    List<LabelModel> labelListOther = new ArrayList<LabelModel>();
                    if(object.getString("statusCode").equals("200")){
                        JSONArray array_me = object.getJSONArray("labels_me");
                        JSONArray array_other = object.getJSONArray("labels_other");
                        List<LabelModel> labelListMe = new ArrayList<LabelModel>();
                        for(int i=0;i<array_me.length();i++){
                            JSONObject labelJson = array_me.getJSONObject(i);
                            LabelModel labelModel = new LabelModel();
                            labelModel.setName(labelJson.getString("lable"));
                            labelModel.setLabelId(labelJson.getString("label_id"));
                            labelListMe.add(labelModel);
                        }

                        for(int i=0;i<array_other.length();i++){
                            JSONObject labelJson = array_other.getJSONObject(i);
                            LabelModel labelModel = new LabelModel();
                            labelModel.setName(labelJson.getString("lable"));
                            labelModel.setLabelId(labelJson.getString("label_id"));

                            int j=0;
                            for(;j<array_me.length();j++){
                                JSONObject labelJsons = array_me.getJSONObject(j);
                                if(labelJson.getString("label_id").trim().equals(labelJsons.getString("label_id").trim())){
                                    break;
                                }
                            }
                            if(j == array_me.length()){
                                labelListOther.add(labelModel);
                            }
                        }
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putSerializable("labels_me", (Serializable) labelListMe);
                        b.putSerializable("labels_other", (Serializable) labelListOther);
                        msg.setData(b);
                        msg.what = ADDHISTORYLABEL;
                        mHandler.sendMessage(msg);
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
    }


    @Override
    public void onTagClick(LabelModel position, View view) {
        if ((int) view.getTag() == 1) {
            ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.labelsapply));
            ((TextView) view).setPadding(25, 20, 25, 20);
            view.setTag(0);
            addTag(position);
        } else {
            ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.labelsadd));
            ((TextView) view).setPadding(25, 20, 25, 20);
            view.setTag(1);
            reTag(position);
        }
    }

    private void addTag(LabelModel labelModel){
        long time = new Date().getTime();
        String phoneNumber = DataCache.getString("phoneNumber",context);
        String lName = null;
        try {
            lName = URLEncoder.encode(labelModel.getName(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String postData = "name="+lName+"&local_x=0&local_y=0&perantId=0&pictureId="+currentPic.getId()+"&phoneNumber="+phoneNumber+"&createTime="+String.valueOf(time).toString().substring(0,10).trim()+"&isValid=1";
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
    }

    private void reTag(LabelModel labelModel){
        String phoneNumber = DataCache.getString("phoneNumber",context);
        String postData = "pictureId="+currentPic.getId()+"&labelId="+labelModel.getLabelId()+"&phoneNumber="+phoneNumber+"&isValid=0";
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

    @OnClick({R.id.set_lable_back, R.id.set_lable_next, R.id.add_img_show,R.id.add_toolbar,R.id.activity_add_label})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_lable_back:
                finish();
                break;
            case R.id.set_lable_next:
                Glide.with(getApplicationContext()).load(cachePic).into(addImgShow);
                currentPic = picList.get(0);
                picList.remove(0);
                initData("1");
                getPicLabels();
                break;
            case R.id.add_img_show:
                Intent addWindow = new Intent(this, AddLabelDiglog.class);
                startActivityForResult(addWindow, WINDOW_ADD);
                break;
            case R.id.add_toolbar:
                addToolbar.setVisibility(View.GONE);
                break;
            case R.id.activity_add_label:
                addToolbar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WINDOW_ADD:
                if (resultCode == RESULT_OK) {
                    final String returnstr = data.getStringExtra("label");
                    if (returnstr.equals("")) {
                        Toast.makeText(context, "标签不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    long time = new Date().getTime();
                    String phoneNumber = DataCache.getString("phoneNumber", context);
                    String lName = null;
                    try {
                        lName = URLEncoder.encode(returnstr, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String postData = "name="+lName+"&local_x=0&local_y=0&perantId=0&pictureId="+currentPic.getId()+"&phoneNumber="+phoneNumber+"&createTime="+String.valueOf(time).toString().substring(0,10).trim()+"&isValid=1";
                    Log.d("canshu",postData);
                    HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "Label", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                            try {
                                JSONObject reJson = new JSONObject(response);
                                if(reJson.getString("statusCode").equals("200")){
                                    Message msg = new Message();
                                    Bundle b = new Bundle();
                                    b.putString("labelId",reJson.getString("labelId"));
                                    b.putString("name",returnstr);
                                    msg.setData(b);
                                    msg.what = LABLE_ADD;
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
                }
                break;
            case DEL_LABEL:
                if(resultCode == RESULT_OK){
                    Log.d("waitDelView",waitDelView.toString());
                    String phoneNumber = DataCache.getString("phoneNumber",context);
                    String postData = "pictureId="+currentPic.getId()+"&labelId="+waitDelLabel.getLabelId()+"&phoneNumber="+phoneNumber+"&isValid=0";
                    HttpUtil.sendHttpRequest(context, postData, HttpUtil.POST, "Imagedit/reMoveLabel", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                            try {
                                JSONObject reJson = new JSONObject(response);
                                if(reJson.getString("statusCode").equals("200")){
                                    mHandler.sendEmptyMessage(DEL_LABEL);
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
                break;
        }
    }

    @Override
    public void onLongTagClick(LabelModel label, View view) {
        Intent i = new Intent(context, Diglog.class);
        Bundle bundle = new Bundle();
        bundle.putString("msg", "确定删除此标签吗?");
        i.putExtra("key", bundle);
        waitDelView = view;
        waitDelLabel = label;
        startActivityForResult(i,DEL_LABEL);
    }
}
