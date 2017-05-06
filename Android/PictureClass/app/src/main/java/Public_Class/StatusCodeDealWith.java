package Public_Class;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/6.
 */

public class StatusCodeDealWith {
    public static  void showDealWith(String msg, Context context){
        switch (msg.trim()){
            case "201":
                Toast.makeText(context,"参数错误",Toast.LENGTH_SHORT).show();
                break;
            case "202":
                Toast.makeText(context,"用户不存在",Toast.LENGTH_SHORT).show();
                break;
            case "203":
                Toast.makeText(context,"验证码错误",Toast.LENGTH_SHORT).show();
                break;
            case "204":
                Toast.makeText(context,"时间戳不对",Toast.LENGTH_SHORT).show();
                break;
            case "205":
                Toast.makeText(context,"手机号为空或不符合要求",Toast.LENGTH_SHORT).show();
                break;
            case "206":
                Toast.makeText(context,"密码为空或不符合要求",Toast.LENGTH_SHORT).show();
                break;
            case "207":
                Toast.makeText(context,"Sid不匹配或不存在",Toast.LENGTH_SHORT).show();
                break;
            case "208":
                Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show();
                break;
            case "209":
                Toast.makeText(context,"写入数据库失败",Toast.LENGTH_SHORT).show();
                break;
            case "210":
                Toast.makeText(context,"注册手机号与接受验证码手机号不符合",Toast.LENGTH_SHORT).show();
                break;
            case "211":
                Toast.makeText(context,"Sid写入失败",Toast.LENGTH_SHORT).show();
                break;
            case "212":
                Toast.makeText(context,"Sid不存在",Toast.LENGTH_SHORT).show();
                break;
            case "213":
                Toast.makeText(context,"Sid存在 可以跳过登录",Toast.LENGTH_SHORT).show();
                break;
            case "214":
                Toast.makeText(context,"Sid不匹配",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
