package com.android.scy.pictureclass;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/27.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginfragment,container,false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setNavigationIcon(R.drawable.back);
        mainActivity.title.setText("登录");
        mainActivity.btnRight.setText("注册");
        mainActivity.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplaceFragment(mainActivity,new RegisterFragment(),0).load();
            }
        });
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().popBackStack();
                mainActivity.imm.hideSoftInputFromWindow(mainActivity.mDrawerLayout.getWindowToken(), 0);
            }
        });
        Button btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        Button btn_forget = (Button) view.findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forget:
                new ReplaceFragment(mainActivity,new Forget_Password_Fragment(),0).load();
                break;
            case R.id.btn_login:
                Toast.makeText(mainActivity, "登录按钮被单击", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}
