package com.android.scy.pictureclass;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    private Context context;
    NavigationView mNavigationView;
    Toolbar toolbar;
    Button btnRight;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivty();
        new ReplaceFragment(this,new WelcomeFragment(),1).load();
    }

    private void initActivty() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.app_title);
        btnRight = (Button) findViewById(R.id.btnRight);
        context = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setCheckedItem(R.id.me);
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.me:
                        Toast.makeText(context,"个人中心",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.yiimg:
                        Toast.makeText(context,"已打图片",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,SetLabel.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                        break;
                    case R.id.LableTree:
                        Toast.makeText(context,"标签树",Toast.LENGTH_SHORT).show();
                        Intent intent_history = new Intent(context,HistoryLabelActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent_history,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                        break;
                    case R.id.About:
                        Toast.makeText(context,"关于",Toast.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.skatemenu);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(mDrawerLayout != null){
//                mDrawerLayout.closeDrawers();
//            }
//        }
//        return false;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mNavigationView.setCheckedItem(R.id.me);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                break;
        }
        return true;
    }
}
