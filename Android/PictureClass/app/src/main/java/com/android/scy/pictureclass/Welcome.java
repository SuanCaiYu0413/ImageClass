package com.android.scy.pictureclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Welcome extends AppCompatActivity {

    @BindView(R.id.welcome_title)
    TextView welcomeTitle;
    @BindView(R.id.welcome_btnRight)
    Button welcomeBtnRight;
    @BindView(R.id.welcome_toolbar)
    Toolbar welcomeToolbar;
    @BindView(R.id.welcome_img)
    ImageView welcomeImg;
    @BindView(R.id.welcome_big_title)
    TextView welcomeBigTitle;
    @BindView(R.id.welcome_subtitle)
    TextView welcomeSubtitle;
    @BindView(R.id.welcome_LodinReg)
    Button welcomeLodinReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initActivity();
    }

    private void initActivity() {
        welcomeTitle.setText("加入我们吧");
    }

    @OnClick({R.id.welcome_btnRight, R.id.welcome_LodinReg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.welcome_btnRight:
                break;
            case R.id.welcome_LodinReg:
                Intent login = new Intent(this,Login.class);
                startActivityForResult(login,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            finish();
        }
    }
}
