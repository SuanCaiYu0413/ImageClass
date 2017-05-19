package Public_Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import com.android.scy.pictureclass.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import InterFace.HttpCallbackListener;
import InterFace.HttpDownLoadListener;

/**
 * Created by Administrator on 2017/5/6.
 */

public class HttpUtil {
    final public static int GET = 1;
    final public static int POST = 2;
    public static void sendHttpRequest(final Context context,final String parma, final int method, final String func, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String urlStr = "";
                    if(method == GET){
                        urlStr = context.getResources().getString(R.string.api_url) + func + "?" + parma;
                    }else if(method == POST){
                        urlStr = context.getResources().getString(R.string.api_url) + func;
                    }else {

                    }
                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method==POST?"POST":"GET");
                    connection.setConnectTimeout(8000);
//                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setReadTimeout(8000);
                    if(method == POST){
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(parma);
                    }
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    Looper.prepare();
                    Log.d("response",response.toString());
                    listener.onFinish(response.toString());
                    Looper.loop();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    listener.onError(e);
                    Looper.loop();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    listener.onError(e);
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    listener.onError(e);
                    Looper.loop();
                }
            }
        }).start();
    }


    public static void sendHttpRequest(final Context context,final String urlStr, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    Looper.prepare();
                    Log.d("response",response.toString());
                    listener.onFinish(response.toString());
                    Looper.loop();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    listener.onError(e);
                    Looper.loop();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    listener.onError(e);
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    listener.onError(e);
                    Looper.loop();
                }
            }
        }).start();
    }

    /**
     * 获取网络图片
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public static void GetImageInputStream(final String imageurl,final HttpDownLoadListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection connection=null;
                Bitmap bitmap=null;
                try {
                    url = new URL(imageurl);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setConnectTimeout(6000); //超时设置
                    connection.setDoInput(true);
                    connection.setUseCaches(false); //设置不使用缓存
                    InputStream inputStream=connection.getInputStream();
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.prepare();
                listener.onDownLoad(bitmap);
                Looper.loop();
            }
        }).start();
    }

}
