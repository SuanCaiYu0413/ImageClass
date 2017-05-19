package InterFace;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/5/6.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
