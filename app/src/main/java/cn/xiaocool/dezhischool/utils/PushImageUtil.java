package cn.xiaocool.dezhischool.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.PhotoWithPath;
import cn.xiaocool.dezhischool.callback.PushImage;
import cn.xiaocool.dezhischool.net.NetConstantUrl;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PushImageUtil {
    private static final int ADD_KEY = 4;
    private static final int ADD_IMG_KEY1 = 101;
    private static final int ADD_IMG_KEY2 = 102;
    private static final int ADD_IMG_KEY3 = 103;
    private static final int ADD_IMG_KEY4 = 104;
    private static final int ADD_IMG_KEY5 = 105;
    private static final int ADD_IMG_KEY6 = 106;
    private static final int ADD_IMG_KEY7 = 107;
    private static final int ADD_IMG_KEY8 = 108;
    private static final int ADD_IMG_KEY9 = 109;
    private static Context mContext;
    private int imgNums = 0;
    private ArrayList<PhotoWithPath> photoWithPaths;
//    private List<PhotoInfo> photoWithPaths;
    private List<String> arrayList=new ArrayList<>();
    private boolean isOk;
    private PushImage pushIamge;
//    public void setPushIamge(Context context, List<PhotoInfo> p, List<String> list, PushImage pushIamge) {
//        this.photoWithPaths = p;
//        this.arrayList=list;
//        this.mContext = context;
//        this.pushIamge = pushIamge;
//        if (photoWithPaths.size()>0){
//            pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY1);
//        }else {
//            pushIamge.error();
//        }
//
//    }

    public void setPushIamge(Context context,ArrayList<PhotoWithPath> p,PushImage pushIamge) {
        this.photoWithPaths = p;
        this.mContext = context;
        this.pushIamge = pushIamge;
        if (photoWithPaths.size()>0){
            pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY1);
        }else {
            pushIamge.error();
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_IMG_KEY1:
                    if (msg.obj != null) {
                      if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                          imgNums = 1;
                          if (imgNums < photoWithPaths.size()) {
                              pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY2);
                          }else {
                              pushIamge.success(true);
                              isOk = true;
                          }
                      }else {
                          pushIamge.error();
                          Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                      }
                    }
                    break;
                case ADD_IMG_KEY2:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 2;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY3);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY3:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 3;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY4);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY4:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 4;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY5);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY5:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 5;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY6);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY6:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 6;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY7);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY7:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 7;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY8);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY8:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 8;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY9);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY9:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 9;
                            isOk = true;
                            pushIamge.success(true);

                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

            }
        }
    };

    private void pushImage(PhotoWithPath photoWithPath, int addImgKey) {
        compressImageWithRatio(photoWithPath, addImgKey);
    }

    public void pushImg(final String picPath,final int KEY){
        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("upfile",picPath));
                String result = NetBaseUtils.getResponseForImg(NetConstantUrl.PUSH_IMAGE, params, mContext);
                try {
                    JSONObject obj = new JSONObject(result);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    public void compressImageWithRatio(PhotoWithPath photoWithPath,int addImgKey){
        File appDir = new File(Environment.getExternalStorageDirectory(), "hyschool");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName =photoWithPath.getPicname();
        File file = new File(appDir, fileName);
        updatePhoto(ImageCompress.compressPicture(photoWithPath.getPicPath(), file), addImgKey);

    }

    public void updatePhoto(final File f,final int KEY){

        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<Part> list=new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile",f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1= NetConstantUrl.PUSH_IMAGE;
                VolleyPostFileRequest request=new VolleyPostFileRequest(url1, list.toArray(new Part[list.size()]),new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject obj = new JSONObject(s);
                            msg.what = KEY;
                            msg.obj = obj;
                            Log.d("===图片张数", imgNums + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            handler.sendMessage(msg);
                        }
                        Log.d("===  图片上传", s);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                MyApplication.getFileRequestQueue().add(request);
            }
        }.start();

    }
}
