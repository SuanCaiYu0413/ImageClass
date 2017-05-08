package Public_Class;

import android.support.design.widget.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/7.
 */

public class CheckInput {
    final public static int USERNAME = 0;
    final public static int PHONENUMBER = 1;
    final public static int PASSWORD = 2;
    final public static int CODE = 3;
    private List<Boolean> checkList = new ArrayList<Boolean>();
    public static void checkStr(TextInputLayout inputLayout,int type){
        String str = inputLayout.getEditText().getText().toString().trim();
        switch (type)
        {
            case USERNAME:
                switch (Input_Text_Chack.chackUserName(str)){
                    case Input_Text_Chack.STR_NULL:
                        inputLayout.setError("值不能为空");
                        break;
                    case Input_Text_Chack.STR_LOW:
                        inputLayout.setError("用户名不能少于5位");
                        break;
                    case Input_Text_Chack.STR_HIGH:
                        inputLayout.setError("用户名不能超过12位");
                        break;
                    default:
                        inputLayout.setError("");
                        inputLayout.setErrorEnabled(false);
                        break;
                }
                break;
            case PHONENUMBER:
                switch (Input_Text_Chack.chackPhoneNumber(str)){
                    case Input_Text_Chack.STR_NULL:
                        inputLayout.setError("值不能为空");
                        break;
                    case Input_Text_Chack.STR_LOW:
                        inputLayout.setError("手机号码值不能少于11位");
                        break;
                    case Input_Text_Chack.STR_FORMAT:
                        inputLayout.setError("手机号码格式不正确");
                        break;
                    case Input_Text_Chack.STR_HIGH:
                        inputLayout.setError("手机号码值不能超过11位");
                        break;
                    default:
                        inputLayout.setError("");
                        inputLayout.setErrorEnabled(false);
                        break;
                }
                break;
            case PASSWORD:
                switch (Input_Text_Chack.chackPassWord(str)){
                    case Input_Text_Chack.STR_NULL:
                        inputLayout.setError("值不能为空");
                        break;
                    case Input_Text_Chack.STR_LOW:
                        inputLayout.setError("密码位数不能少于6位");
                        break;
                    case Input_Text_Chack.STR_HIGH:
                        inputLayout.setError("密码位数不能超过16位");
                        break;
                    default:
                        inputLayout.setError("");
                        inputLayout.setErrorEnabled(false);
                        break;
                }
                break;
            case CODE:
                switch (Input_Text_Chack.chackCode(str)){
                    case Input_Text_Chack.STR_NULL:
                        inputLayout.setError("值不能为空");
                        break;
                    case Input_Text_Chack.STR_LOW:
                        inputLayout.setError("验证码位数不能少于5位");
                        break;
                    case Input_Text_Chack.STR_HIGH:
                        inputLayout.setError("验证码位数不能超过5位");
                        break;
                    default:
                        inputLayout.setError("");
                        inputLayout.setErrorEnabled(false);;
                        break;
                }
                break;
        }
    }

    public static boolean isPass(List<TextInputLayout> list){
        for(int i=0;i<list.size();i++){
            if(!(list.get(i).getError() == null)){
                return false;
            }
        }
        return true;
    }
}
