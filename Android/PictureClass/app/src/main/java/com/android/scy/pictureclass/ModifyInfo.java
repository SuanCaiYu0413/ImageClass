package com.android.scy.pictureclass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import InterFace.HttpCallbackListener;
import Model.HobbyLabel;
import Model.ResultItem;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import Public_Class.UploadUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyInfo extends AppCompatActivity {
    private static final int CHOOSENO = 2;
    @BindView(R.id.header_pic_ucenter)
    CircleImageView headerPicUcenter;
    @BindView(R.id.uname_ucenter)
    TextView unameUcenter;
    @BindView(R.id.item_modify_title)
    TextView itemModifyTitle;
    @BindView(R.id.item_modify_edit)
    EditText itemModifyEdit;
    @BindView(R.id.item_modify_career_title)
    TextView itemModifyCareerTitle;
    @BindView(R.id.item_modify_career_edit)
    EditText itemModifyCareerEdit;
    @BindView(R.id.activity_modify_info)
    RelativeLayout activityModifyInfo;
    @BindView(R.id.item_modify_hobby_title)
    TextView itemModifyHobbyTitle;
    @BindView(R.id.item_modify_hobby)
    LinearLayout itemModifyHobby;
    @BindView(R.id.modify_cleancache)
    LinearLayout modifyCleancache;
    @BindView(R.id.header_xuanze)
    LinearLayout headerXuanze;
    @BindView(R.id.modify_repwd)
    LinearLayout modifyRepwd;
    @BindView(R.id.modify_exit)
    LinearLayout modifyExit;
    @BindView(R.id.modify_hobby_list)
    LinearLayout modifyHobbyList;
    private List<ResultItem> menuList = new ArrayList<>();
    @BindView(R.id.modify_title)
    TextView modifyTitle;
    @BindView(R.id.modify_btnRight)
    Button modifyBtnRight;
    @BindView(R.id.modify_toolbar)
    Toolbar modifyToolbar;
    //    @BindView(R.id.modify_list)
//    ListView modifyList;
    private final int CHOOSEOK = 1;
    private final int GETHOBBY = 3;
    String hobbyLIst = null;
    List<HobbyLabel> hobbyList = new ArrayList<>();
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHOOSEOK:
                    Bundle b = msg.getData();
                    String imgPath = b.getString("imgPath");
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    headerPicUcenter.setImageBitmap(bitmap);
                    Toast.makeText(getApplicationContext(), "头像修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case CHOOSENO:
                    Toast.makeText(getApplicationContext(), "头像修改失败", Toast.LENGTH_SHORT).show();
                    break;
                case GETHOBBY:
                    for (int i=0;i<hobbyList.size();i++){
                        TextView hobbyText = new TextView(getApplicationContext());
                        hobbyText.setText(hobbyList.get(i).getName().toString());
                        hobbyText.setBackgroundColor(Color.rgb(236, 236, 236));
                        hobbyText.setTextColor(Color.rgb(102, 102, 102));
                        hobbyText.setTextSize(18);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        param.setMargins(50,0,0,0);
                        hobbyText.setLayoutParams(param);
                        modifyHobbyList.addView(hobbyText);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        ButterKnife.bind(this);
        initActivity();
    }


    private void initActivity() {
        modifyToolbar.setNavigationIcon(R.drawable.back);
        modifyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        modifyBtnRight.setText("保存");
        modifyTitle.setText("我的信息");
        unameUcenter.setText("修改头像");
        unameUcenter.setTextColor(Color.rgb(102, 102, 102));
        String name = DataCache.getString("userName", getApplicationContext());
        itemModifyEdit.setText(name == null ? "" : name);
        String profession = DataCache.getString("profession", getApplicationContext());
        itemModifyCareerEdit.setText(profession == null ? "" : profession);
        String phoneNumber = DataCache.getString("phoneNumber", getApplicationContext());
        if (phoneNumber != null) {
            final File file = new File(getExternalCacheDir(), phoneNumber.trim() + ".jpg");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            headerPicUcenter.setImageBitmap(bitmap);
        }
        getHobbyList();
    }

    @OnClick({R.id.item_modify_hobby, R.id.modify_cleancache,R.id.modify_btnRight, R.id.modify_exit, R.id.modify_repwd, R.id.header_xuanze})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_modify_hobby:
                Intent hobby = new Intent(this, hobbyActivity.class);
                Bundle b = new Bundle();
                b.putString("hobbyList",hobbyLIst);
                hobby.putExtra("value",b);
                startActivityForResult(hobby, 0);
                break;
            case R.id.modify_cleancache:
                Intent intent = new Intent(this, CleanCacheDiglog.class);
                startActivity(intent);
                break;
            case R.id.modify_exit:
                DataCache.putString("userName", null, getApplicationContext());
                DataCache.putString("phoneNumber", null, getApplicationContext());
                DataCache.putString("sid", null, getApplicationContext());
                Intent in = new Intent(this, Welcome.class);
                startActivity(in);
                finish();
                break;
            case R.id.modify_repwd:
                Intent rePwd = new Intent(this, ForgetPassword.class);
                startActivityForResult(rePwd, 1);
                break;
            case R.id.header_xuanze:
                Intent avatar = new Intent(this, AvatarDiglog.class);
                startActivityForResult(avatar, 10);
                break;
            case R.id.modify_btnRight:
                String phoneNumber =DataCache.getString("phoneNumber", getApplicationContext());
                final String profession = itemModifyCareerEdit.getText().toString().trim();
                final String uname = itemModifyEdit.getText().toString().trim();
                String userName=null;
                String profe = null;
                try {
                    userName = URLEncoder.encode(uname,"utf-8");
                    profe = URLEncoder.encode(profession,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String sid=DataCache.getString("sid", getApplicationContext());
                if(uname.equals(DataCache.getString("userName", getApplicationContext())) && profession.equals(DataCache.getString("profession", getApplicationContext()))){
                    return;
                }
                String parma = "phoneNumber="+phoneNumber+"&profession="+profe+"&userName="+userName+"&sid="+sid;
                HttpUtil.sendHttpRequest(getApplicationContext(), parma, HttpUtil.POST, "UserInfo", new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!profession.equals(DataCache.getString("profession", getApplicationContext()))){
                                JSONObject professionObject = jsonObject.getJSONObject("setProfession");
                                if(professionObject.getString("statusCode").trim().equals("200")){
                                    Toast.makeText(getApplicationContext(),"职业更新成功",Toast.LENGTH_SHORT).show();
                                    DataCache.putString("profession",profession,getApplicationContext());
                                }else {
                                    StatusCodeDealWith.showDealWith(professionObject.getString("statusCode").trim(),getApplicationContext());
                                }
                            }
                            if(uname.equals(DataCache.getString("userName", getApplicationContext()))){
                                return;
                            }
                            JSONObject userNameObject = jsonObject.getJSONObject("setUserName");
                            if(userNameObject.getString("statusCode").trim().equals("200")){
                                Toast.makeText(getApplicationContext(),"姓名更新成功",Toast.LENGTH_SHORT).show();
                                DataCache.putString("userName",uname,getApplicationContext());
                            }else {
                                StatusCodeDealWith.showDealWith(userNameObject.getString("statusCode").trim(),getApplicationContext());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(),"信息修改失败，未知错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                finish();
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    String str = data.getStringExtra("tag");
                    Log.d("tag", str);
                    if (str.equals("camera")) {
                        Uri imgUri = Uri.parse(data.getStringExtra("header"));
                        UpLoadImg(imgUri.getPath());
                    } else if (str.equals("album")) {
                        String imgPath = data.getStringExtra("imgPath");
                        UpLoadImg(imgPath);
                    }
                }
                break;
        }
    }

    private void UpLoadImg(final String imgPath) {
        final File file = new File(getExternalCacheDir(), DataCache.getString("phoneNumber", getApplicationContext()).trim() + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            UploadUtil.compressAndGenImage(imgPath, file, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UploadUtil.uploadFile(file, "http://119.29.194.163/tp/UserInfo/upload", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("imgPath", imgPath);
                msg.setData(b);
                msg.what = CHOOSEOK;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                myHandler.sendEmptyMessage(CHOOSENO);
            }

        });
    }

    public void getHobbyList() {
        HttpUtil.sendHttpRequest(getApplicationContext(), "", HttpUtil.GET, "UserInfo/getHobby", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject hobby = new JSONObject(response);
                    if(hobby.getString("statusCode").equals("200")){
                        hobbyLIst = hobby.getString("hobby");
                        String localHobby = DataCache.getString("hobbyList",getApplicationContext());
                        String[] a = localHobby.split("--");
                        Log.e("e",localHobby);
                        Log.e("a",String.valueOf(a.length));
                        for(int i=0;i<a.length;i++){
                            Log.d("asd",a[i]);
                            JSONArray ja = new JSONArray(hobbyLIst);
                            HobbyLabel h = null;
                            for(int j=0;j<ja.length();j++){
                                JSONObject hobbyJo = ja.getJSONObject(j);
                                if(hobbyJo.getString("hobby_id").trim().equals(a[i])){
                                    h = new HobbyLabel(hobbyJo.getString("hobby").trim(),Integer.parseInt(a[i]));
                                }
                            }
                            hobbyList.add(h);
                        }
                        myHandler.sendEmptyMessage(GETHOBBY);
                    }else {
                        StatusCodeDealWith.showDealWith(hobby.getString("statusCode"),getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        return ;
    }
}
