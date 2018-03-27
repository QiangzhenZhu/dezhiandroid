package cn.xiaocool.dezhischool.utils;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 10835 on 2018/1/30.
 */

public class OkhttpUtil {

    //发起网络请求
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback ){
        OkHttpClient cilent = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        cilent.newCall(request).enqueue(callback);
    }
    public  static boolean isSuccess(String result)  {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("status");
            if ("success".equals(status)) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
