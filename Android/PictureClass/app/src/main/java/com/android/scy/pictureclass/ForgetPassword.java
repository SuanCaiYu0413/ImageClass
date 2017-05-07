package com.android.scy.pictureclass;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassword extends AppCompatActivity {

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
    }

    @OnClick({R.id.forget_password_btn_sendcode, R.id.forget_password_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_password_btn_sendcode:
                break;
            case R.id.forget_password_btn_ok:
                break;
        }
    }
}
