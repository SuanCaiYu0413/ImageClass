package com.android.scy.pictureclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Diglog extends AppCompatActivity {

    @BindView(R.id.diglog_msg)
    TextView diglogMsg;
    @BindView(R.id.diglog_btn_ok)
    Button diglogBtnOk;
    @BindView(R.id.diglog_btn_cancel)
    Button diglogBtnCancel;
    @BindView(R.id.activity_diglog)
    LinearLayout activityDiglog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diglog);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("key");
        diglogMsg.setText(b.getString("msg"));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick({R.id.diglog_btn_ok, R.id.diglog_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.diglog_btn_ok:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.diglog_btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
