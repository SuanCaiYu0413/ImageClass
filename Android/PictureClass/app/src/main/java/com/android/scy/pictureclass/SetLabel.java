package com.android.scy.pictureclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import Model.PictureInfo;


public class SetLabel extends AppCompatActivity {
    PictureInfo pic;
    ImageView imgShow;
    RelativeLayout ly;
    DisplayMetrics dm;
    private Context context;
    private Point ontouch;
    private MyOnTouchListener labelLisener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_label);
        context = getApplicationContext();
        initActivity();
    }

    private void initActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(500));
            getWindow().setExitTransition(new Explode().setDuration(500));
        }
        imgShow = (ImageView) findViewById(R.id.img_show);
        ly = (RelativeLayout) findViewById(R.id.set_layout);
        ontouch = new Point(0,0);
        dm = getResources().getDisplayMetrics();
        labelLisener = new MyOnTouchListener();
        imgShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ontouch.x = (int) motionEvent.getRawX();
                        ontouch.y = (int) motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("MOVEx",motionEvent.getX()+"");
                        Log.d("MOVEy",motionEvent.getY()+"");
                        return true;
                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getRawX();
                        int y = (int) motionEvent.getRawY();
                        if(Math.abs(x-ontouch.x) < 15 && Math.abs(y-ontouch.y) < 15){
                            Intent intent = new Intent(context,AddLabelDiglog.class);
                            intent.putExtra("x",x);
                            intent.putExtra("y",y);
                            startActivityForResult(intent,1);
                        }
                        break;
                }
                return false;
            }
        });
        pic = new PictureInfo();
        pic.setName(R.drawable.hah);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnstr = data.getStringExtra("label");
                    int x = data.getIntExtra("x",0);
                    int y = data.getIntExtra("y",0);
                    if(returnstr.equals("")){
                        Toast.makeText(context,"标签不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    TextView label = new TextView(context);
                    label.setText("  # "+returnstr);
                    label.setTextColor(Color.WHITE);
                    label.setBackgroundResource(R.drawable.label);
                    label.setOnTouchListener(labelLisener);
                    int l = x;
                    int t = y;
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    param.setMargins(l,t,0,0);
                    label.setLayoutParams(param);
                    ly.addView(label);
                    Toast.makeText(context,"添加标签成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).load(pic.getName()).into(imgShow);
    }

    private class MyOnTouchListener implements View.OnTouchListener{

        private int lastX;
        private int lastY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                    lastY = (int) event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    int l = v.getLeft() + dx;
                    int b = v.getBottom() + dy;
                    int r = v.getRight() + dx;
                    int t = v.getTop() + dy;
                    // 下面判断移动是否超出屏幕
                    if (l < +imgShow.getLeft()) {
                        l = +imgShow.getLeft();
                        r = l + v.getWidth();
                    }
                    if (t < imgShow.getTop()) {
                        t = imgShow.getTop();
                        b = t + v.getHeight();
                    }
                    if (r > imgShow.getWidth()+imgShow.getLeft()) {
                        r = imgShow.getWidth()+imgShow.getLeft();
                        l = r - v.getWidth();
                    }
                    if (b > imgShow.getHeight()+imgShow.getTop()) {
                        b = imgShow.getHeight()+imgShow.getTop();
                        t = b - v.getHeight();
                    }
                    v.layout(l, t, r, b);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    v.postInvalidate();
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    param.setMargins(l,t,0,0);
                    v.setLayoutParams(param);
                    break;
                case MotionEvent.ACTION_UP:
                    return true;
            }
            return false;
        }
    }
}
