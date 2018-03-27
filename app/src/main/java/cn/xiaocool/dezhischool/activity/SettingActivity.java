package cn.xiaocool.dezhischool.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.CheckVersionModel;
import cn.xiaocool.dezhischool.bean.SystemNews;
import cn.xiaocool.dezhischool.callback.VersionUpdateImpl;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.service.DownloadService;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.DataCleanManager;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.VersionUpdate;
import cn.xiaocool.dezhischool.view.NiceDialog;

public class SettingActivity extends BaseActivity implements VersionUpdateImpl {
    @BindView(R.id.activity_setting_rl_help)
    RelativeLayout activitySettingRlHelp;
    @BindView(R.id.activity_setting_rl_feedback)
    RelativeLayout activitySettingRlFeedback;
    @BindView(R.id.activity_setting_aboutUs)
    RelativeLayout activitySettingAboutUs;
    @BindView(R.id.activity_setting_rl_update)
    RelativeLayout activitySettingRlUpdate;
    @BindView(R.id.activity_setting_rl_clean)
    RelativeLayout activitySettingRlClean;
    @BindView(R.id.activity_setting_tv_quit)
    TextView activitySettingTvQuit;
    @BindView(R.id.tv_cleansize)
    TextView tvcleansize;
    private Context context;
    private CheckVersionModel versionModel;


    private static final int REQUEST_WRITE_STORAGE = 111;
    private NiceDialog mDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        context = this;
        setVersionDialog();
        setTopName("设置");
        hideRightText();
        String cacheSize="";
        try {
            cacheSize = DataCleanManager.getTotalCacheSize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvcleansize.setText(cacheSize);

    }

    private void setVersionDialog() {
        mDialog = new NiceDialog(SettingActivity.this);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = width-300;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void requsetData() {

    }

    @OnClick({R.id.activity_setting_rl_help, R.id.activity_setting_rl_feedback, R.id.activity_setting_aboutUs, R.id.activity_setting_rl_update, R.id.activity_setting_rl_clean, R.id.activity_setting_tv_quit})
    public void onClick(View view) {
        Intent intent;

        Bundle bundle = new Bundle();
        switch (view.getId()) {
            //使用帮助
            case R.id.activity_setting_rl_help:
                SystemNews systemAbout;
                systemAbout = new SystemNews();
                systemAbout.setId("16");
                systemAbout.setPost_title("使用帮助");
                intent = new Intent(context, SystemNewsDetailActivity.class);
                bundle.putSerializable(LocalConstant.WEB_FLAG, systemAbout);
                startActivity(SystemNewsDetailActivity.class, bundle);
                break;
            //意见反馈
            case R.id.activity_setting_rl_feedback:
                startActivity(new Intent(context, OnlineCommentActivity.class));
                break;
            //关于我们
            case R.id.activity_setting_aboutUs:
                SystemNews systemNewses;
                systemNewses = new SystemNews();
                systemNewses.setId("17");
                systemNewses.setPost_title("关于我们");
                intent = new Intent(context, SystemNewsDetailActivity.class);
                bundle.putSerializable(LocalConstant.WEB_FLAG, systemNewses);
                startActivity(SystemNewsDetailActivity.class, bundle);
                break;
            //版本更新
            case R.id.activity_setting_rl_update:
                chechVersion();
                break;
            //清除缓存
            case R.id.activity_setting_rl_clean:
                ProgressUtil.dissmisLoadingDialog();

                DataCleanManager.cleanExternalCache(context);
//                DataCleanManager.clean
                mDialog.setTitle("清除缓存");
                mDialog.setContent("已清除");
                mDialog.setOKButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
                break;
            //退出
            case R.id.activity_setting_tv_quit:
                String account = String.valueOf(SPUtils.get(context, LocalConstant.USER_ACCOUNT, ""));
                SPUtils.clear(context);
                SPUtils.put(context, LocalConstant.USER_ACCOUNT, account);
                JPushInterface.stopPush(context);
                startActivity(LoginActivity.class);
                MyApplication.getInstance().onTerminate();
                break;
        }
    }

    private void chechVersion() {
        ProgressUtil.showLoadingDialog(this);
        String versionId = getResources().getString(R.string.versionid).toString();
        String url =  NetConstantUrl.CHECK_VERSION + versionId;
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)){
                    ProgressUtil.dissmisLoadingDialog();
                    versionModel = getBeanFromJson(result);
                    showDialogByYorNo(versionModel.getVersionid());
                }else {
                    ProgressUtil.dissmisLoadingDialog();
                    mDialog.setTitle("暂无最新版本");
                    mDialog.setContent("感谢您的使用！");
                    mDialog.setOKButton("确定", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                    mDialog.show();
                }
            }

            @Override
            public void onError() {
                ProgressUtil.dissmisLoadingDialog();
            }
        });
//        mDialog = new NiceDialog(SettingActivity.this);
//        mDialog.setTitle("发现新版本");
//        mDialog.setContent("为了给大家提供更好的用户体验，每次应用的更新都包含速度和稳定性的提升，感谢您的使用！");
//        mDialog.setOKButton("立即更新", new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //请求存储权限
//                boolean hasPermission = (ContextCompat.checkSelfPermission(SettingActivity.this,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//                if (!hasPermission) {
//                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
//                    ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                } else {
//                    //下载
//                    startDownload();
//                }
//
//            }
//        });
//        mDialog.show();
    }


    //展示dialog
    private void showDialogByYorNo(String versionid) {

        if (Integer.valueOf(versionid)>Integer.valueOf(getResources().getString(R.string.versionid).toString())){
            mDialog.setTitle("发现新版本");
            mDialog.setContent(versionModel.getDescription());
            mDialog.setOKButton("立即更新", new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //请求存储权限
//                    boolean hasPermission = (ContextCompat.checkSelfPermission(SettingActivity.this,
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//                    if (!hasPermission) {
//                        ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
//                        ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    } else {
                        //下载
                        startDownload();
//                    }

                }
            });
            mDialog.show();
        }else {
            mDialog.setTitle("已经是最新版本");
            mDialog.setContent("感谢您的使用！");
            mDialog.setOKButton("确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    startDownload();
                } else {
                    Toast.makeText(SettingActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 启动下载
     */
    private void startDownload() {
//        Uri uri = Uri.parse(APK_DOWNLOAD_URL);
//        Intent it = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(it);
//        Intent it = new Intent(SettingActivity.this, UpdateService.class);
//        //下载地址
//        Log.e("apkUrl",versionModel.getUrl());
//        it.putExtra("apkUrl", APK_DOWNLOAD_URL);
//        startService(it);

        removeOldApk();

        VersionUpdate.checkVersion(this,versionModel.getUrl());
        mDialog.dismiss();
    }
    /**
     * 删除上次更新存储在本地的apk
     */
    private void removeOldApk() {
        //获取老ＡＰＫ的存储路径
        File fileName = new File((String) SPUtils.get(context,LocalConstant.SP_DOWNLOAD_PATH, ""));
//        Log.i(TAG, "老APK的存储路径 =" + SPUtils.get(context,LocalConstant.SP_DOWNLOAD_PATH, ""));

        if (fileName != null && fileName.exists() && fileName.isFile()) {
            fileName.delete();
//            Log.i(TAG, "存储器内存在老APK，进行删除操作");
        }
    }
    /**
     * 字符串转模型
     * @param result
     * @return
     */
    private CheckVersionModel getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<CheckVersionModel>() {
        }.getType());
    }

    @Override
    public void bindService(String apkUrl) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, apkUrl);
        isBindService = bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private boolean isBindService;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
            DownloadService downloadService = binder.getService();

            //接口回调，下载进度
            downloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                @Override
                public void onProgress(float fraction) {
//                    Log.i(TAG, "下载进度：" + fraction);


                    //判断是否真的下载完成进行安装了，以及是否注册绑定过服务
                    if (fraction == DownloadService.UNBIND_SERVICE && isBindService) {
                        unbindService(conn);
                        isBindService = false;
                        ToastUtil.showShort(context,"下载完成！");
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
