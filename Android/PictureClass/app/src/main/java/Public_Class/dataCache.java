package Public_Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/8.
 */

public class DataCache {
    public static String getString(String name, Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Log.d("ax",sharedPreferences.toString());
        return sharedPreferences.getString(name.trim(),null);
    }
    public  static boolean putString(String name,String value,Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name.trim(),value);
        return editor.commit();
    }
}
