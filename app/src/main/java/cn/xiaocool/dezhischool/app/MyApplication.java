package cn.xiaocool.dezhischool.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.yixia.camera.VCamera;
//import com.yixia.camera.util.DeviceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.utils.SPUtils;


/**
 * Created by Administrator on 2016/3/14.
 */
public class MyApplication extends Application {

    private static RequestQueue requestQueue;
    private static RequestQueue requestQueueFile;
    private static MyApplication myApplication;
    public static  String classid="";
    public static  String begintime="";
    public static  String endtime="";
    public IWXAPI api; //第三方app与微信通信的openapi接口
    public static final String APP_ID = "wxc72796fa99c1cdea";

    Context mContext;

    /**打开的activity**/
    private List<Activity> activities = new ArrayList<Activity>();
    /**应用实例**/
    private static MyApplication instance;
    /**
     *  获得实例
     * @return
     */
    public static MyApplication getInstance(){
        return instance;
    }
    /**
     * 新建了一个activity
     * @param activity
     */
    public void addActivity(Activity activity){
        activities.add(activity);
    }
    /**
     *  结束指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity!=null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        for (Activity activity : activities) {
            activity.finish();
        }

        System.exit(0);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //请求队列
        requestQueue = Volley.newRequestQueue(this);
        requestQueueFile = Volley.newRequestQueue(this);
        myApplication = new MyApplication();
        instance = this;
        initImageLoder(mContext);
        setWeShare();
        setJpush();
        //setViedioFilePath();
    }

    /**
     *  设置拍摄视频缓存路径
     */
   /* private void setViedioFilePath() {
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/recoder/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/recoder/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/hyxJuns/");
        }

//		VCamera.setVideoCachePath(FileUtils.getRecorderPath());
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);

    }*/

    /**
     * 设置JPUSH
     */
    private void setJpush() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        String registerId = JPushInterface.getRegistrationID(mContext);
        SPUtils.put(mContext, LocalConstant.JPUSH_REGISTERID,registerId);


    }


    /**
     * 微信分享
     */
    private void setWeShare() {
        api = WXAPIFactory.createWXAPI(this, APP_ID,true); //初始化api
        api.registerApp(APP_ID); //将APP_ID注册到微信中
    }

    /**
     * 配置Imageloader
     */
    private void initImageLoder(Context mContext) {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(), "hyx/Cache");
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext()).memoryCacheExtraOptions(480, 800)
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
//                .threadPoolSize(3)
//                .threadPriority(Thread.NORM_PRIORITY - 1).tasksProcessingOrder(QueueProcessingType.FIFO)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new WeakMemoryCache())
//                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
//                .discCacheSize(30 * 1024 * 1024)
//                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(1000)
//                .discCache(new UnlimitedDiscCache(cacheDir))
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(getBaseContext(), 5 * 1000, 30 * 1000))
//                .writeDebugLogs()
//                .build();
//        ImageLoader.getInstance().init(config);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(mContext);
        ImageLoader.getInstance().init(configuration);// 全局初始化此配置
    }


    /**
     * 拿到消息队列
     */
    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * 拿到消息队列
     */
    public static RequestQueue getFileRequestQueue() {
        return requestQueueFile;
    }

    /**
     * 拿到消息队列
     */
    public static Application getApplication() {
        return myApplication;
    }

}
