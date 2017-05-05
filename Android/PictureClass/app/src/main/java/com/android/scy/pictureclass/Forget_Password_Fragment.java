package com.android.scy.pictureclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/5/5.
 */

public class Forget_Password_Fragment extends Fragment {
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.forget_password_fragment,container,false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setNavigationIcon(R.drawable.back);
        mainActivity.title.setText("找回密码");
        return v;
    }
}
