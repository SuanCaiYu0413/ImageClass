package com.android.scy.pictureclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Public_Class.Input_Text_Chack;

/**
 * Created by Administrator on 2017/4/27.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    MainActivity mainActivity;
    private TextInputLayout phone_til;
    private TextInputLayout code_til;
    private TextInputLayout username_til;
    private TextInputLayout password_til;
    private List chackList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register,container,false);
        chackList = new ArrayList<Boolean>();
        chackList.add(false);chackList.add(false);chackList.add(false);chackList.add(false);
        phone_til = (TextInputLayout) view.findViewById(R.id.reg_phone);
        code_til = (TextInputLayout) view.findViewById(R.id.reg_code);
        username_til = (TextInputLayout) view.findViewById(R.id.reg_username);
        password_til = (TextInputLayout) view.findViewById(R.id.reg_password);
        mainActivity = (MainActivity) getActivity();
        mainActivity.btnRight.setText("");
        mainActivity.title.setText("注册");
        mainActivity.btnRight.setOnClickListener(null);
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        phone_til.getEditText().addTextChangedListener(new TextCheng(R.id.reg_phone));
        code_til.getEditText().addTextChangedListener(new TextCheng(R.id.reg_code));
        username_til.getEditText().addTextChangedListener(new TextCheng(R.id.reg_username));
        password_til.getEditText().addTextChangedListener(new TextCheng(R.id.reg_password));
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                int count = 0;
                for (int i=0;i<chackList.size();i++) {
                    if(!(boolean)chackList.get(i)){
                        count++;
                    }
                }
                if(count==0){
                    Toast.makeText(mainActivity,"提交注册",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(mainActivity,"请检查是否输入值或有错误提示文本框",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private class TextCheng implements TextWatcher {
        private int id;
        private TextCheng(int fu){
            this.id = fu;
        }
        @Override
        public void afterTextChanged(Editable editable) {
            switch (id){
                case R.id.reg_phone:
                    //Toast.makeText(mainActivity,"string length:"+editable.toString(),Toast.LENGTH_SHORT).show();
                    switch (Input_Text_Chack.chackPhoneNumber(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            phone_til.setError("值不能为空");
                            chackList.set(0,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            phone_til.setError("手机号码值不能少于11位");
                            chackList.set(0,false);
                            break;
                        case Input_Text_Chack.STR_FORMAT:
                            phone_til.setError("手机号码格式不正确");
                            chackList.set(0,false);
                            break;
                        default:
                            phone_til.setErrorEnabled(false);
                            chackList.set(0,true);
                            break;
                    }
                    break;
                case R.id.reg_code:
                    switch (Input_Text_Chack.chackCode(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            code_til.setError("值不能为空");
                            chackList.set(1,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            code_til.setError("验证码位数不能少于5位");
                            chackList.set(1,false);
                            break;
                        case Input_Text_Chack.STR_HIGH:
                            code_til.setError("验证码位数不能超过5位");
                            chackList.set(1,false);
                            break;
                        default:
                            code_til.setErrorEnabled(false);
                            chackList.set(1,true);
                            break;
                    }
                    break;
                case R.id.reg_password:
                    switch (Input_Text_Chack.chackPassWord(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            password_til.setError("值不能为空");
                            chackList.set(2,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            password_til.setError("密码位数不能少于6位");
                            chackList.set(2,false);
                            break;
                        case Input_Text_Chack.STR_HIGH:
                            password_til.setError("密码位数不能超过16位");
                            chackList.set(2,false);
                            break;
                        default:
                            password_til.setErrorEnabled(false);
                            chackList.set(2,true);
                            break;
                    }
                    break;
                case R.id.reg_username:
                    switch (Input_Text_Chack.chackUserName(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            username_til.setError("值不能为空");
                            chackList.set(2,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            username_til.setError("用户名不能少于5位");
                            chackList.set(2,false);
                            break;
                        case Input_Text_Chack.STR_HIGH:
                            username_til.setError("用户名不能超过12位");
                            chackList.set(2,false);
                            break;
                        default:
                            username_til.setErrorEnabled(false);
                            chackList.set(2,true);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    }
}
