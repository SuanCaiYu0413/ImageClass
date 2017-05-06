package com.android.scy.pictureclass;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import com.android.scy.pictureclass.MainActivity;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Administrator on 2017/4/27.
 */

public class WelcomeFragment extends Fragment implements View.OnClickListener {

    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome,container,false);
        mainActivity= (MainActivity) getActivity();
        mainActivity.getSupportFragmentManager();
        mainActivity.toolbar.setNavigationIcon(R.drawable.skatemenu);
        mainActivity.title.setText("加入我们吧");
        mainActivity.btnRight.setText("");
        mainActivity.btnRight.setOnClickListener(null);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        Button regLogin = (Button) view.findViewById(R.id.LodinReg);
        regLogin.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LodinReg:
                new ReplaceFragment(mainActivity,new LoginFragment(),ReplaceFragment.END).load();
                break;
            default:
                break;
        }
    }
}
