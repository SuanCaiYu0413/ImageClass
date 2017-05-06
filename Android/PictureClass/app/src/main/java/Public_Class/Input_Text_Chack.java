package Public_Class;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/5.
 */

public class Input_Text_Chack {
    final public static int STR_NULL = 0;
    final public static int STR_LOW = 1;
    final public static int STR_TRUE = 2;
    final public static int STR_FORMAT = 3;
    final public static int STR_HIGH = 4;
    public static int chackPhoneNumber(String Number){
        if(Number.trim().length() <= 0){
            return STR_NULL;
        }

        Pattern p = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|17[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher m = p.matcher(Number.trim());
        if(m.matches()){
            return STR_TRUE;
        }else{
            return STR_FORMAT;
        }
    }
    public static int chackUserName(String str){
        if(str.trim().length() <= 0){
            return STR_NULL;
        }
        if(str.trim().length() < 5){
            return STR_LOW;
        }
        if(str.trim().length() > 12){
            return STR_HIGH;
        }
        return STR_TRUE;
    }
    public static int chackCode(String str){
        if(str.trim().length() <= 0){
            return STR_NULL;
        }
        if(str.trim().length() < 5){
            return STR_LOW;
        }
        if(str.trim().length() > 5){
            return STR_HIGH;
        }
        return STR_TRUE;
    }
    public static int chackPassWord(String str){
        if(str.trim().length() <= 0){
            return STR_NULL;
        }
        if(str.trim().length() < 6){
            return STR_LOW;
        }
        if(str.trim().length() > 16){
            return STR_HIGH;
        }
        return STR_TRUE;
    }
}
