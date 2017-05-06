package com.android.scy.pictureclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import InterFace.HttpCallbackListener;
import Public_Class.HttpUtil;
import Public_Class.Input_Text_Chack;

/**
 * Created by Administrator on 2017/4/27.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;
    private TextInputLayout username_til;
    private List chackList;
    private TextInputLayout password_til;
    private String method = "Login";
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginfragment,container,false);
        mainActivity = (MainActivity) getActivity();
        username_til = (TextInputLayout) view.findViewById(R.id.login_username);
        password_til = (TextInputLayout) view.findViewById(R.id.login_password);
        chackList = new ArrayList();
        sharedPreferences = mainActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        chackList.add(false);
        chackList.add(false);
        mainActivity.toolbar.setNavigationIcon(R.drawable.back);
        mainActivity.title.setText("登录");
        mainActivity.btnRight.setText("注册");
        mainActivity.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplaceFragment(mainActivity,new RegisterFragment(),0).load();
            }
        });
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().popBackStack();
                mainActivity.imm.hideSoftInputFromWindow(mainActivity.mDrawerLayout.getWindowToken(), 0);
            }
        });
        String phoneText = sharedPreferences.getString("phone",null);
        if(phoneText != null){
            username_til.getEditText().setText(phoneText);
            chackList.set(0,true);
        }
        username_til.getEditText().addTextChangedListener(new TextChang(R.id.login_username));
        password_til.getEditText().addTextChangedListener(new TextChang(R.id.login_password));
        Button btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        Button btn_forget = (Button) view.findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forget:
                new ReplaceFragment(mainActivity,new Forget_Password_Fragment(),0).load();
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
                    HttpUtil.sendHttpRequest(getContext(),"Phonenumber=" + uname + "&Password=" + pwd, HttpUtil.POST, "Login", new HttpCallbackListener() {
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
            String StatusCode = jsonObject.getString("StatusCode");
            String phonenumber = jsonObject.getString("phone");
            String TimeStamp = jsonObject.getString("TimeStamp");
            Log.d("DEBUG",StatusCode);
            Log.d("DEBUG",phonenumber);
            Log.d("DEBUG",TimeStamp);
            Toast.makeText(mainActivity,"登录成功："+phonenumber,Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phone",username_til.getEditText().getText().toString().trim());
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
