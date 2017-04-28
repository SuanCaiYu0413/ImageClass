package com.android.scy.pictureclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/27.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register,container,false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.btnRight.setText("");
        mainActivity.title.setText("注册");
        mainActivity.btnRight.setOnClickListener(null);
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                Toast.makeText(mainActivity, "注册按钮被单击", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
