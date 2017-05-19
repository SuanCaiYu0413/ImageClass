package com.android.scy.pictureclass;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import InterFace.HttpCallbackListener;
import Public_Class.CheckInput;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassword extends AppCompatActivity {
    private int taskCount = 59;
    @BindView(R.id.forget_password_title)
    TextView forgetPasswordTitle;
    @BindView(R.id.forget_password_toolbar)
    Toolbar forgetPasswordToolbar;
    @BindView(R.id.forget_password_phone)
    TextInputLayout forgetPasswordPhone;
    @BindView(R.id.forget_password_btn_sendcode)
    Button forgetPasswordBtnSendcode;
    @BindView(R.id.forget_password_phone_code)
    TextInputLayout forgetPasswordPhoneCode;
    @BindView(R.id.forget_password_new_password)
    TextInputLayout forgetPasswordNewPassword;
    @BindView(R.id.forget_password_btn_ok)
    Button forgetPasswordBtnOk;
    @BindView(R.id.forget_password_fragment_id)
    LinearLayout forgetPasswordFragmentId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    forgetPasswordBtnOk.setText("确认修改");
                    forgetPasswordBtnOk.setEnabled(true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initActivity();
    }

    private void initActivity() {
        forgetPasswordToolbar.setNavigationIcon(R.drawable.back);
        forgetPasswordToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        forgetPasswordTitle.setText("找回密码");

        //文本框实时验证
        forgetPasswordPhone.getEditText().addTextChangedListener(new TextCheck(R.id.forget_password_phone));
        forgetPasswordNewPassword.getEditText().addTextChangedListener(new TextCheck(R.id.forget_password_new_password));
        forgetPasswordPhoneCode.getEditText().addTextChangedListener(new TextCheck(R.id.forget_password_phone_code));
    }

    @OnClick({R.id.forget_password_btn_sendcode, R.id.forget_password_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_password_btn_sendcode:
                sendCode();
                break;
            case R.id.forget_password_btn_ok:
                if(errorCheck()){
                    rePassword();
                }else {
                    Toast.makeText(this,"请检查是否输入值或有错误提示文本框",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void rePassword() {
        forgetPasswordBtnOk.setText("提交中...");
        forgetPasswordBtnOk.setEnabled(false);
        String phoneNumber = forgetPasswordPhone.getEditText().getText().toString().trim();
        String phoneCode = forgetPasswordPhoneCode.getEditText().getText().toString().trim();
        String newPassword = forgetPasswordNewPassword.getEditText().getText().toString().trim();
        String timeStamp = String.valueOf(new Date().getTime()).toString().substring(0,10).trim();
        String postData = "timeStamp="+timeStamp+"&phoneNumber="+phoneNumber+"&newPassword="+newPassword+"&phoneCode="+phoneCode;
        HttpUtil.sendHttpRequest(getApplicationContext(), postData, HttpUtil.POST, "RePassWord", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                parseJSONWithJSONObject(response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(),"重置密码失败,未知错误",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(),"重置密码成功",Toast.LENGTH_SHORT).show();
                    DataCache.putString("userName",null,getApplicationContext());
                    DataCache.putString("phoneNumber",null,getApplicationContext());
                    DataCache.putString("sid",null,getApplicationContext());
                    setResult(5);
                    Intent login = new Intent(this,Login.class);
                    startActivity(login);
                    setResult(1);
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


    private void sendCode() {
        forgetPasswordPhone.setEnabled(false);
        forgetPasswordBtnSendcode.setEnabled(false);
        cdt.start();
        String phoneNumber = forgetPasswordPhone.getEditText().getText().toString().trim();
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

    CountDownTimer cdt = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            taskCount --;
            if(taskCount>0){
                forgetPasswordBtnSendcode.setText(taskCount+"秒后可以重发");
            }else{
                taskCount = 60;
                forgetPasswordBtnSendcode.setText("发送验证码");
                forgetPasswordBtnSendcode.setEnabled(true);
                forgetPasswordPhone.setEnabled(true);
            }

        }
        @Override
        public void onFinish() {

        }
    };

    private class TextCheck implements TextWatcher {
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
                case R.id.forget_password_new_password:
                    CheckInput.checkStr(forgetPasswordNewPassword,CheckInput.PASSWORD);
                    break;
                case R.id.forget_password_phone:
                    CheckInput.checkStr(forgetPasswordPhone,CheckInput.PHONENUMBER);
                    break;
                case R.id.forget_password_phone_code:
                    CheckInput.checkStr(forgetPasswordPhoneCode,CheckInput.CODE);
                    break;
            }
        }
    }
    private boolean errorCheck(){
        List<TextInputLayout> list = new ArrayList<TextInputLayout>();
        list.add(forgetPasswordNewPassword);
        list.add(forgetPasswordPhone);
        list.add(forgetPasswordPhoneCode);
        return CheckInput.isPass(list);
    }
}
