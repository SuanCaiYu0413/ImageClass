package Public_Class;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.scy.pictureclass.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import InterFace.HttpCallbackListener;
import InterFace.HttpDownLoadListener;

/**
 * Created by Administrator on 2017/5/14.
 */

public class initApp {
    public static void initApplication(final Context context){
        final String phoneNumber = DataCache.getString("phoneNumber", context);
        if(phoneNumber != null){
            HttpUtil.sendHttpRequest(context, "phoneNumber="+phoneNumber, HttpUtil.POST,"showInfo" , new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    try {
                        JSONObject info = new JSONObject(response);
                        String photo = info.getString("photo");
                        String userName = info.getString("userName");
                        String profession = info.getString("profession");
                        String hobby = info.getString("hobby");
                        String sid = info.getString("sid");
                        if(!photo.equals("")){
                            photo(photo,context,phoneNumber);
                        }
                        if(!userName.equals("")){
                            userName(userName,context);
                        }
                        if(!profession.equals("")){
                            profession(profession,context);
                        }
                        sid(sid,context);
                        if(!hobby.equals("")){
                            hobby(hobby,context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {

                }

            });
        }
    }

    private static void photo(String photo, final Context context,final String phoneNumber) throws JSONException {
        JSONObject jsonPhoto = new JSONObject(photo);
        HttpUtil.GetImageInputStream(context.getResources().getString(R.string.api_url) + jsonPhoto.getString("rootPath") + "/" + jsonPhoto.getString("savePath"), new HttpDownLoadListener() {
            @Override
            public void onDownLoad(Bitmap bitmap) {
                File file = null;
                if(bitmap != null){
                    file = new File(context.getExternalCacheDir(),phoneNumber+".jpg");
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        fos.write(os.toByteArray());
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static void sid(String sid, Context context){
        DataCache.putString("sid",sid,context);
    }

    private static void userName(String userName, Context context){
        DataCache.putString("userName",userName,context);
    }

    private static void profession(String profession, Context context){
        DataCache.putString("profession",profession,context);
    }

    private static void hobby(String hobby, Context context) throws JSONException {
        JSONArray hobbyArray= new JSONArray(hobby);
        String hobbyList = "";
        for(int i=0;i<hobbyArray.length();i++){
            JSONObject hobbyObject = hobbyArray.getJSONObject(i);
            hobbyList += (hobbyObject.getString("hobby_id").trim() + (i==hobbyArray.length()-1?"":"--"));
        }
        DataCache.putString("hobbyList",hobbyList,context);
    }
}
