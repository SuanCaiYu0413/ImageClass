package com.android.scy.pictureclass;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CleanCacheDiglog extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.cache_clean_text) TextView show;
    @BindView(R.id.clean_cache_ok) Button btnOk;
    @BindView(R.id.clean_cache_cancel) Button btnCancel;
    private Cache ch;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache_diglog);
        ButterKnife.bind(this);
        String cacheSize = null;
        context = getApplicationContext();
        ch = new Cache();
        try {
            cacheSize = ch.getCacheSize(context.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Debug",cacheSize);
        if(cacheSize != null){
            show.setText("共有: "+cacheSize + " 缓存");
        }else{
            show.setText("好像除了点问题啊!");
        }
        initActivity();
    }

    private void initActivity() {
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clean_cache_ok:
                ch.cleanInternalCache(context);
                Toast.makeText(context,"清除成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.clean_cache_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
