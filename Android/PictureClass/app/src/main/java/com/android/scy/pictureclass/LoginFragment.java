package com.android.scy.pictureclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import InterFace.HttpCallbackListener;
import Public_Class.HttpUtil;
import Public_Class.Input_Text_Chack;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/27.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;
    @BindView(R.id.login_username) TextInputLayout username_til;
    @BindView(R.id.login_password) TextInputLayout password_til;
    @BindView(R.id.btn_login) Button btn_login;
    @BindView(R.id.btn_forget) Button btn_forget;
    private List chackList;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginfragment,container,false);
        ButterKnife.bind(this,view);
        mainActivity = (MainActivity) getActivity();
        chackList = new ArrayList();
        chackList.add(false);
        chackList.add(false);
        sharedPreferences = mainActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        mainActivity.toolbar.setNavigationIcon(R.drawable.back);
        mainActivity.title.setText("登录");
        mainActivity.btnRight.setText("注册");
        mainActivity.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplaceFragment(mainActivity,new RegisterFragment(),1).load();
            }
        });
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().popBackStack();
                mainActivity.imm.hideSoftInputFromWindow(mainActivity.mDrawerLayout.getWindowToken(), 0);
            }
        });
        String phoneText = sharedPreferences.getString("phoneNumber",null);
        String sid = sharedPreferences.getString("sid",null);
        if(phoneText != null){
            username_til.getEditText().setText(phoneText);
            chackList.set(0,true);
        }
        username_til.getEditText().addTextChangedListener(new TextChang(R.id.login_username));
        password_til.getEditText().addTextChangedListener(new TextChang(R.id.login_password));
        btn_login.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forget:
                new ReplaceFragment(mainActivity,new Forget_Password_Fragment(),ReplaceFragment.END).load();
                break;
            case R.id.btn_login:
                int count = 0;
                for(int i=0;i<chackList.size();i++){
                    if(!(boolean)chackList.get(i)){
                        count++;
                    }
                }
                if(count==0){
                    String uname = username_til.getEditText().getText().toString().trim();
                    String pwd = password_til.getEditText().getText().toString().trim();
                    long timeStamp = new Date().getTime();
                    Log.d("Timestamp",String.valueOf(timeStamp).toString().substring(0,10));
                    HttpUtil.sendHttpRequest(getContext(),"timeStamp="+String.valueOf(timeStamp).toString().substring(0,10)+"&phoneNumber=" + uname + "&passWord=" + pwd, HttpUtil.POST, "Login", new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            parseJSONWithJSONObject(response);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(mainActivity,"登录失败，未知错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Log.d("username",username_til.getEditText().getText().toString().trim());
                    Toast.makeText(mainActivity,"请检查是否输入值或有错误提示文本框",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    private void parseJSONWithJSONObject(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String statusCode = jsonObject.getString("statusCode");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (statusCode){
                case "200":
                    editor.putString("userName",jsonObject.getString("userName"));
                    editor.putString("phoneNumber",jsonObject.getString("phone"));
                    editor.putString("sid",jsonObject.getString("sid"));
                    Toast.makeText(mainActivity,"登录成功：",Toast.LENGTH_SHORT).show();
                    new ReplaceFragment(mainActivity,new UCenter_Fragment(),ReplaceFragment.DELLOGIN).load();
                    break;
                default:
                    StatusCodeDealWith.showDealWith(statusCode,getContext());
                    break;
            }
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class TextChang implements TextWatcher{
        private int id;
        private TextChang(int fu){
            this.id = fu;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (this.id){
                case R.id.login_username:
                    switch (Input_Text_Chack.chackPhoneNumber(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            username_til.setError("手机号不能为空");
                            chackList.set(0,false);
                            break;
                        case Input_Text_Chack.STR_FORMAT:
                            username_til.setError("手机号格式错误");
                            chackList.set(0,false);
                            break;
                        case Input_Text_Chack.STR_TRUE:
                            username_til.setErrorEnabled(false);
                            chackList.set(0,true);
                            break;
                    }
                    break;
                case R.id.login_password:
                    switch (Input_Text_Chack.chackPassWord(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            password_til.setError("密码不能为空");
                            chackList.set(1,false);
                            break;
                        default:
                            password_til.setErrorEnabled(false);
                            chackList.set(1,true);
                            break;
                    }
                    break;
            }
        }
    }
}
