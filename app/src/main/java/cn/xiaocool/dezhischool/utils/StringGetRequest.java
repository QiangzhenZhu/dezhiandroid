package cn.xiaocool.dezhischool.utils;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by fu
 * Volley框架Get请求方法
 */
public class StringGetRequest extends StringRequest {



    public StringGetRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    public StringGetRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET,url, listener, errorListener);


    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }




}
