package com.android.scy.pictureclass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import InterFace.HttpCallbackListener;
import Public_Class.HttpUtil;
import Public_Class.StatusCodeDealWith;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {
    private Context context;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.login_title)
    TextView loginTitle;
    @BindView(R.id.login_btnRight)
    Button loginBtnRight;
    @BindView(R.id.login_toolbar)
    Toolbar loginToolbar;
    @BindView(R.id.login_username)
    TextInputLayout loginUsername;
    @BindView(R.id.login_password)
    TextInputLayout loginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_forget)
    Button btnForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initActivity();
    }

    private void initActivity() {
        loginTitle.setText("用户登录");
        loginBtnRight.setText("注册");
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        loginToolbar.setNavigationIcon(R.drawable.back);
        loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.login_btnRight, R.id.btn_login, R.id.btn_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btnRight:
                Intent registerActivityIntent = new Intent(this,Register.class);
                startActivity(registerActivityIntent);
                break;
            case R.id.btn_login:
                userLogin();
                break;
            case R.id.btn_forget:
                Intent forgetActivityIntent = new Intent(this,ForgetPassword.class);
                startActivity(forgetActivityIntent);
                break;
        }
    }

    private void userLogin() {
        String uname = loginUsername.getEditText().getText().toString().trim();
        String pwd = loginPassword.getEditText().getText().toString().trim();
        long timeStamp = new Date().getTime();
        HttpUtil.sendHttpRequest(context,"timeStamp="+String.valueOf(timeStamp).toString().substring(0,10)+"&phoneNumber=" + uname + "&passWord=" + pwd, HttpUtil.POST, "Login", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                parseJSONWithJSONObject(response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context,"登录失败，未知错误",Toast.LENGTH_SHORT).show();
            }
        });
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
                    Toast.makeText(context,"登录成功：",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    StatusCodeDealWith.showDealWith(statusCode,context);
                    break;
            }
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
