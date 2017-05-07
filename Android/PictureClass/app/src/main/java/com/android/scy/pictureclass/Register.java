package com.android.scy.pictureclass;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

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
    }

    @OnClick({R.id.register_btnRight, R.id.register_btn_sendcode, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_btnRight:
                break;
            case R.id.register_btn_sendcode:
                break;
            case R.id.btn_register:
                break;
        }
    }
}
