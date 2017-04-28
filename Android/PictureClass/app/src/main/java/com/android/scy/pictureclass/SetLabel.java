package com.android.scy.pictureclass;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class SetLabel extends AppCompatActivity {
    PictureInfo pic;
    ImageView imgShow;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_label);
        context = getApplicationContext();
        initActivity();
    }

    private void initActivity() {
        imgShow = (ImageView) findViewById(R.id.img_show);
        imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pic = new PictureInfo();
        pic.setName(R.drawable.jiedao);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).load(pic.getName()).into(imgShow);
    }
}
