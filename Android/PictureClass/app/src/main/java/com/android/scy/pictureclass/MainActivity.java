package com.android.scy.pictureclass;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import InterFace.HttpCallbackListener;
import InterFace.HttpDownLoadListener;
import Public_Class.DataCache;
import Public_Class.HttpUtil;
import Public_Class.initApp;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    final private int DAY_IMG = 0;
    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.day_img)
    ImageView dayImg;
    @BindView(R.id.main_desc)
    TextView mainDesc;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Context context;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.app_title)
    TextView title;
    InputMethodManager imm;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DAY_IMG:
                    Bundle b = msg.getData();
                    Glide.with(context).load("http://cn.bing.com" + b.get("url").toString()).into(dayImg);
                    mainDesc.setText(b.getString("desc"));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initApp.initApplication(getApplicationContext());
        initActivty();
        dayImg();
    }

    private void dayImg() {
        String timeStamp = String.valueOf(new Date().getTime());
        HttpUtil.sendHttpRequest(context, "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&nc=" + timeStamp + "&pid=hp&video=1", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    jsonObject = jsonArray.getJSONObject(0);
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("url", jsonObject.getString("url"));
                    b.putString("desc", "    "+jsonObject.getString("copyright"));
                    msg.setData(b);
                    msg.what = DAY_IMG;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String phoneNumber = DataCache.getString("phoneNumber", context);
        String userName = DataCache.getString("userName", context);
        String sid = DataCache.getString("sid", context);
        if(userName == null || sid == null || phoneNumber == null){
            Intent welcome = new Intent(this,Welcome.class);
            startActivity(welcome);
            finish();
        }
        View headerLayout = mNavigationView.getHeaderView(0);
        CircleImageView headerPic = (CircleImageView) headerLayout.findViewById(R.id.header_pic);
        TextView headerUsername = (TextView) headerLayout.findViewById(R.id.header_username);
        final File file = new File(getExternalCacheDir(),DataCache.getString("phoneNumber",getApplicationContext()).trim()+".jpg");
        headerPic.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        headerUsername.setText(DataCache.getString("userName",getApplicationContext()));
    }

    private void initActivty() {
        context = getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
        title.setText("每日一图");
        toolbar.setNavigationIcon(R.drawable.skatemenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.me:
                        Toast.makeText(context, "个人中心", Toast.LENGTH_SHORT).show();
                        Intent ucenter = new Intent(getApplicationContext(), ModifyInfo.class);
                        startActivity(ucenter);
                        break;
                    case R.id.biaoqian:
                        Intent intent = new Intent(context, SetLabel.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                        break;
                    case R.id.yiimg:
                        Intent intent_history = new Intent(context, HistoryLabelActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent_history, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                        break;
                    case R.id.LableTree:
                        Toast.makeText(context, "标签树", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.About:
                        Toast.makeText(context, "关于", Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.skatemenu);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawers();
            }
            int num = getSupportFragmentManager().getBackStackEntryCount();
            if (num > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }

            imm.hideSoftInputFromWindow(mDrawerLayout.getWindowToken(), 0);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }
        return true;
    }
}
