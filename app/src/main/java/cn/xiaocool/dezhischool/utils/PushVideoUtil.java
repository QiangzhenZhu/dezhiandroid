package cn.xiaocool.dezhischool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.callback.PushVideo;
import cn.xiaocool.dezhischool.net.NetConstantUrl;

/**
 * Created by xiaocool on 17/1/7.
 */

public class PushVideoUtil {
    private PushVideo pushVideo;
    private String videoPath;
    private String fileName,imageName;
    private Context context;
    private File image;
    public void pushVideo(Context context, String videoPath, PushVideo pushVideo){
        this.pushVideo = pushVideo;
        this.videoPath = videoPath;
        this.context = context;
        image = getImgFirstVideo();
        newVideoFile();

    }

    private File getImgFirstVideo() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
        return convertBitmap(bitmap);
    }

    public File convertBitmap(Bitmap bitmap){
        File appDir = new File(Environment.getExternalStorageDirectory(), "hyxvideo");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        imageName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".jpg";

        File file = new File(appDir, imageName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
           return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void newVideoFile() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "hyxvideo");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        fileName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".mp4";
        File file = new File(appDir, fileName);
        File oldFile = new File(videoPath);
        oldFile.renameTo(file);
        upDateVideo(file);

    }

    private void upDateVideo(final File file) {

        new Thread(){

            @Override
            public void run() {
                List<Part> list=new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile",file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1= NetConstantUrl.PUSH_VIDEO;
                VolleyPostFileRequest request=new VolleyPostFileRequest(url1, list.toArray(new Part[list.size()]),new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (JsonResult.JSONparser(context,s)){

                           upDateVideoImage(image);

                        }else {
                            pushVideo.error();
                        }
                        Log.d("===  video上传", s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pushVideo.error();
                    }
                });
                MyApplication.getFileRequestQueue().add(request);
            }
        }.start();
    }

    private void upDateVideoImage(final File file) {

        if (file==null){
            ToastUtil.showShort(context,"获取第一帧图片失败");
            pushVideo.error();
            return;
        }
        new Thread(){

            @Override
            public void run() {
                List<Part> list=new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile",file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1= NetConstantUrl.PUSH_VIDEO;
                VolleyPostFileRequest request=new VolleyPostFileRequest(url1, list.toArray(new Part[list.size()]),new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (JsonResult.JSONparser(context,s)){
                            pushVideo.success(fileName,imageName);
                        }else {
                            pushVideo.error();
                        }
                        Log.d("===  video上传", s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pushVideo.error();
                    }
                });
                MyApplication.getFileRequestQueue().add(request);
            }
        }.start();
    }

}
