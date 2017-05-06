package com.android.scy.pictureclass;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    private Context context;
    NavigationView mNavigationView;
    Toolbar toolbar;
    Button btnRight;
    UCenter_Fragment uCenter_fragment;
    WelcomeFragment welcomeFragment;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    Forget_Password_Fragment forget_password_fragment;
    TextView title;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initActivty();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        String sid = sharedPreferences.getString("sid",null);
        if(sid != null){
            new ReplaceFragment(MainActivity.this,uCenter_fragment==null?uCenter_fragment=new UCenter_Fragment():uCenter_fragment,1).load();
        }else{
            new ReplaceFragment(this,welcomeFragment==null?welcomeFragment=new WelcomeFragment():welcomeFragment,1).load();
        }

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
                        new ReplaceFragment(MainActivity.this,uCenter_fragment==null?uCenter_fragment=new UCenter_Fragment():uCenter_fragment,0).load();
                        break;
                    case R.id.biaoqian:
                        Intent intent = new Intent(context,SetLabel.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                        break;
                    case R.id.yiimg:
                        Intent intent_history = new Intent(context,HistoryLabelActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent_history,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        }
                        break;
                    case R.id.LableTree:
                        Toast.makeText(context,"标签树",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mDrawerLayout != null){
                mDrawerLayout.closeDrawers();
            }
            int num = getSupportFragmentManager().getBackStackEntryCount();
            if(num > 0){
                getSupportFragmentManager().popBackStack();
            }else {
                finish();
            }

            imm.hideSoftInputFromWindow(mDrawerLayout.getWindowToken(), 0);
        }
        return false;
    }

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
