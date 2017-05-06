package com.android.scy.pictureclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
 * Created by Administrator on 2017/5/5.
 */

public class Forget_Password_Fragment extends Fragment {
    private MainActivity mainActivity;
    private TextInputLayout phone;
    private TextInputLayout code;
    private Button btnOk;
    private List chackList;
    private TextInputLayout newpassword;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.forget_password_fragment,container,false);
        chackList = new ArrayList<Boolean>();
        chackList.add(false);
        chackList.add(false);
        chackList.add(false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setNavigationIcon(R.drawable.back);
        mainActivity.title.setText("找回密码");
        btnOk = (Button) v.findViewById(R.id.forget_password_btn_ok);
        btnOk.setOnClickListener(new btnClick());
        phone = (TextInputLayout) v.findViewById(R.id.forget_password_phone);
        code = (TextInputLayout) v.findViewById(R.id.forget_password_phone_code);
        newpassword = (TextInputLayout) v.findViewById(R.id.forget_password_new_password);
        phone.getEditText().addTextChangedListener(new editPhone(R.id.forget_password_phone));
        code.getEditText().addTextChangedListener(new editPhone(R.id.forget_password_phone_code));
        newpassword.getEditText().addTextChangedListener(new editPhone(R.id.forget_password_new_password));
        return v;
    }


    private class editPhone implements TextWatcher {
        private int id;
        private editPhone(int fu){
            this.id = fu;
        }
        @Override
        public void afterTextChanged(Editable editable) {
            switch (id){
                case R.id.forget_password_phone:
                    //Toast.makeText(mainActivity,"string length:"+editable.toString(),Toast.LENGTH_SHORT).show();
                    switch (Input_Text_Chack.chackPhoneNumber(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            phone.setError("值不能为空");
                            chackList.set(0,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            phone.setError("手机号码值不能少于11位");
                            chackList.set(0,false);
                            break;
                        case Input_Text_Chack.STR_FORMAT:
                            phone.setError("手机号码格式不正确");
                            chackList.set(0,false);
                            break;
                        default:
                            phone.setErrorEnabled(false);
                            chackList.set(0,true);
                            break;
                    }
                    break;
                case R.id.forget_password_phone_code:
                    switch (Input_Text_Chack.chackCode(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            code.setError("值不能为空");
                            chackList.set(1,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            code.setError("验证码位数不能少于5位");
                            chackList.set(1,false);
                            break;
                        case Input_Text_Chack.STR_HIGH:
                            code.setError("验证码位数不能超过5位");
                            chackList.set(1,false);
                            break;
                        default:
                            code.setErrorEnabled(false);
                            chackList.set(1,true);
                            break;
                    }
                    break;
                case R.id.forget_password_new_password:
                    switch (Input_Text_Chack.chackPassWord(editable.toString())){
                        case Input_Text_Chack.STR_NULL:
                            newpassword.setError("值不能为空");
                            chackList.set(2,false);
                            break;
                        case Input_Text_Chack.STR_LOW:
                            newpassword.setError("密码位数不能少于6位");
                            chackList.set(2,false);
                            break;
                        case Input_Text_Chack.STR_HIGH:
                            newpassword.setError("密码位数不能超过16位");
                            chackList.set(2,false);
                            break;
                        default:
                            newpassword.setErrorEnabled(false);
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

    class btnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.forget_password_btn_ok:
                    //确认按钮被单击
                    int count = 0;
                    for (int i=0;i<chackList.size();i++) {
                        if(!(boolean)chackList.get(i)){
                            count++;
                        }
                    }
                    switch (count){
                        case 0:
                            Toast.makeText(mainActivity,"更改成功",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mainActivity,"请检查是否输入值或有错误提示文本框",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case R.id.forget_password_btn_sendcode:
                    //发送验证码按钮被单击
                    if(!(boolean)chackList.get(0)){

                    }else{
                        Toast.makeText(mainActivity,"手机号码输入有问题",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
