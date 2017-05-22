package com.android.scy.pictureclass;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import InterFace.HttpCallbackListener;
import Public_Class.CheckInput;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {
    private int taskCount = 59;
    @BindView(R.id.register_title)
    TextView registerTitle;
    @BindView(R.id.register_btnRight)
    Button registerBtnRight;
    @BindView(R.id.register_toolbar)
    Toolbar registerToolbar;
    @BindView(R.id.reg_phone)
    TextInputLayout regPhone;
    @BindView(R.id.register_btn_sendcode)
    Button registerBtnSendcode;
    @BindView(R.id.reg_code)
    TextInputLayout regCode;
    @BindView(R.id.reg_username)
    TextInputLayout regUsername;
    @BindView(R.id.reg_password)
    TextInputLayout regPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    btnRegister.setText("注册");
                    btnRegister.setEnabled(true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initActivity();
    }

    private void initActivity() {
        registerTitle.setText("用户注册");
        registerToolbar.setNavigationIcon(R.drawable.back);
        registerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //文本实时处理
        regCode.getEditText().addTextChangedListener(new TextCheck(R.id.reg_code));
        regPassword.getEditText().addTextChangedListener(new TextCheck(R.id.reg_password));
        regPhone.getEditText().addTextChangedListener(new TextCheck(R.id.reg_phone));
        regUsername.getEditText().addTextChangedListener(new TextCheck(R.id.reg_username));
    }

    @OnClick({R.id.register_btnRight, R.id.register_btn_sendcode, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_btnRight:
                break;
            case R.id.register_btn_sendcode:
                if(regPhone.getError() == null){
                    sendCode();
                }else{
                    Toast.makeText(this,"请检查电话号码是否正确",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                if(errorCheck()){
                    register();
                }else {
                    Toast.makeText(this,"请检查是否输入值或有错误提示文本框",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void sendCode() {
        regPhone.setEnabled(false);
        registerBtnSendcode.setEnabled(false);
        cdt.start();
        String phoneNumber = regPhone.getEditText().getText().toString().trim();
        String timeStamp = String.valueOf(new Date().getTime()).toString().substring(0,10).trim();
        String postData = "timeStamp="+timeStamp+"&phoneNumber="+phoneNumber.trim();
        HttpUtil.sendHttpRequest(getApplicationContext(), postData, HttpUtil.POST, "GetPhoneCode", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Toast.makeText(getApplicationContext(),"验证码已发送",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(),"获取验证码失败，未知错误",Toast.LENGTH_SHORT).show();
            }


        });
    }


    private void register() {
        String phoneNumber = regPhone.getEditText().getText().toString().trim();
        String phoneCode = regCode.getEditText().getText().toString().trim();
        String userName = regUsername.getEditText().getText().toString().trim();
        btnRegister.setEnabled(false);
        btnRegister.setText("正在注册...");
        try {
            userName = URLEncoder.encode(userName,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String passWord = regPassword.getEditText().getText().toString().trim();
        String timeStamp = String.valueOf(new Date().getTime()).toString().substring(0,10).trim();
        String postData = "timeStamp="+timeStamp+"&userName="+userName+"&passWord="+passWord+"&phoneNumber="+phoneNumber+"&phoneCode="+phoneCode;
        Log.d("pads",postData);
        HttpUtil.sendHttpRequest(getApplicationContext(), postData, HttpUtil.POST, "Registered", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                parseJSONWithJSONObject(response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(),"注册失败,未知错误",Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(0);
            }


        });
    }

    private void parseJSONWithJSONObject(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String statusCode = jsonObject.getString("statusCode");
            switch (statusCode){
                case "200":
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    StatusCodeDealWith.showDealWith(statusCode,getApplicationContext());
                    mHandler.sendEmptyMessage(0);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class TextCheck implements TextWatcher{
        private int id;
        public TextCheck(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (id){
                case R.id.reg_username:
                    CheckInput.checkStr(regUsername,CheckInput.USERNAME);
                    break;
                case R.id.reg_code:
                    CheckInput.checkStr(regCode,CheckInput.CODE);
                    break;
                case R.id.reg_password:
                    CheckInput.checkStr(regPassword,CheckInput.PASSWORD);
                    break;
                case R.id.reg_phone:
                    CheckInput.checkStr(regPhone,CheckInput.PHONENUMBER);
                    break;
            }
        }
    }
    private boolean errorCheck(){
        List<TextInputLayout> list = new ArrayList<TextInputLayout>();
        list.add(regCode);
        list.add(regPassword);
        list.add(regPhone);
        list.add(regUsername);
        return CheckInput.isPass(list);
    }

    CountDownTimer cdt = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            taskCount --;
            if(taskCount>0){
                registerBtnSendcode.setText(taskCount+"秒后可以重发");
            }else{
                taskCount = 60;
                registerBtnSendcode.setText("发送验证码");
                registerBtnSendcode.setEnabled(true);
                regPhone.setEnabled(true);
            }

        }
        @Override
        public void onFinish() {

        }
    };

}
