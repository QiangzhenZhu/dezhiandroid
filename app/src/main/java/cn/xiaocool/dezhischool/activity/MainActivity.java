package cn.xiaocool.dezhischool.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.CheckVersionModel;
import cn.xiaocool.dezhischool.callback.VersionUpdateImpl;
import cn.xiaocool.dezhischool.fragment.FirstFragment;
import cn.xiaocool.dezhischool.fragment.FourFragment;
import cn.xiaocool.dezhischool.fragment.SecondFragment;
import cn.xiaocool.dezhischool.fragment.SecondParentFragment;
import cn.xiaocool.dezhischool.fragment.ThirdFragment;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.service.DownloadService;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.LocalVersionUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.VersionUpdate;
import cn.xiaocool.dezhischool.view.BottomNavigationViewHelper;
import cn.xiaocool.dezhischool.view.NiceDialog;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class MainActivity extends BaseActivity implements VersionUpdateImpl {

    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    /*@BindView(R.id.main_tab_home)
    RadioButton mainTabHome;
    @BindView(R.id.main_tab_sort)
    RadioButton mainTabSort;
    @BindView(R.id.main_tab_quick)
    RadioButton mainTabQuick;
    @BindView(R.id.main_tab_mine)
    RadioButton mainTabMine;*/
    private BottomNavigationView mBottomNavigationView;
    private int index, currentTabIndex;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourFragment fourFragment;
    private SecondParentFragment secondParentFragment;
    private Fragment[] fragments;
    private Context context;

    private NiceDialog mDialog = null;
    private CheckVersionModel versionModel;
    private static final int REQUEST_WRITE_STORAGE = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideTopView();
        mBottomNavigationView = findViewById(R.id.bnv_bootom);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        context = this;
        chechVersion();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        setVersionDialog();
        init();
        Log.e("TAG", SPUtils.get(context, LocalConstant.USER_IS_PRINSIPLE,"").toString()
                + SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").toString()
                + SPUtils.get(context, LocalConstant.USER_CLASSID,"").toString());
        /*TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
       *//* String DEVICE_ID = tm.getDeviceId();
        Log.e("TAG",DEVICE_ID);*/
        if (getIntent().getBooleanExtra("isParent",false)){
            showSecondParentFragment();
        }
        // TODO: 2018/3/22
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_tab_home:
                        index = 0;
                        break;
                    case R.id.main_tab_sort:
                        index = 1;
                        break;
                    case R.id.main_tab_quick:
                        index = 2;
                        break;
                    case R.id.main_tab_mine:
                        index = 3;
                        break;
                }
                if (currentTabIndex != index) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.hide(fragments[currentTabIndex]);
                    if (!fragments[index].isAdded()) {
                        transaction.add(R.id.fragment_container, fragments[index]);
                    }
                    transaction.show(fragments[index]);
                    transaction.commit();

                }
                currentTabIndex = index;
                return true;

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    private void setVersionDialog() {
        mDialog = new NiceDialog(MainActivity.this);
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

    @Override
    protected void onStart() {
        super.onStart();

        String url = NetConstantUrl.GET_PARENT_BYTEACHERID + "&teacherid=" + SPUtils.get(context, LocalConstant.USER_ID, "")+"&schoolid="+SPUtils.get(context, LocalConstant.SCHOOL_ID, "");
        Log.e("child",url);
        VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(MainActivity.this, result)) {
                    SPUtils.put(MainActivity.this, LocalConstant.IS_TEACH, "1");
                } else {
                    SPUtils.put(MainActivity.this, LocalConstant.IS_TEACH, "2");
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void init() {
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        fourFragment = new FourFragment();
        secondParentFragment = new SecondParentFragment();
        if(SPUtils.get(context,LocalConstant.USER_TYPE,"1").equals("0")){
            fragments = new Fragment[]{firstFragment,secondParentFragment,thirdFragment,fourFragment};
        }else{
            fragments = new Fragment[]{firstFragment,secondFragment,thirdFragment,fourFragment};
        }
        getFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
// TODO: 2018/3/22
       /* setDrawableSize(mainTabHome,R.drawable.main_home_selector);
        setDrawableSize(mainTabSort,R.drawable.main_second_selector);
        setDrawableSize(mainTabQuick,R.drawable.main_third_selector);
        setDrawableSize(mainTabMine,R.drawable.main_four_selector);*/
    }

    // TODO: 2018/3/22
    /*@OnClick({R.id.main_tab_home, R.id.main_tab_sort, R.id.main_tab_quick, R.id.main_tab_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_home:
                index = 0;
                break;
            case R.id.main_tab_sort:
                index = 1;
                break;
            case R.id.main_tab_quick:
                index = 2;
                break;
            case R.id.main_tab_mine:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                transaction.add(R.id.fragment_container, fragments[index]);
            }
            transaction.show(fragments[index]);
            transaction.commit();

        }
        currentTabIndex = index;
    }*/
    private void setDrawableSize(RadioButton mTab, int btn_bar_home_seclect) {
        Drawable drawable1 = getResources().getDrawable(btn_bar_home_seclect);
        drawable1.setBounds(0, 0, 40, 40);
        mTab.setCompoundDrawables(null, drawable1, null, null);
    }


    /**
     * 检查版本更新
     */
    private void chechVersion() {

        final String versionId = String.valueOf(LocalVersionUtil.getLocalVersion(context));
        String url =  NetConstantUrl.CHECK_VERSION + versionId;
        VolleyUtil.VolleyGetRequest(getBaseContext(), url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    versionModel = getBeanFromJson(result);
                    /*showDialogByYorNo(versionModel.getVersionid());
                    mDialog.show();*/
                    DownloadBuilder builder = AllenVersionChecker
                            .getInstance()
                            .downloadOnly(
                                    UIData.create().setDownloadUrl(versionModel.getUrl())
                                    .setTitle("版本更新")
                                    .setContent(versionModel.getDescription()))
                            .setForceRedownload(true);
                    if (Integer.parseInt(versionModel.getForce()) == 1 ){

                        builder.setForceUpdateListener(new ForceUpdateListener() {
                            @Override
                            public void onShouldForceUpdate() {

                            }
                        });

                    }
                    builder.excuteMission(context);
                } else {
//                    mDialog.setTitle("暂无最新版本");
//                    mDialog.setContent("感谢您的使用！");
//                    mDialog.setOKButton("确定", new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            mDialog.dismiss();
//                        }
//                    });

                }
            }

            @Override
            public void onError() {

            }
        });

    }

    private void showSecondParentFragment(){
        // TODO: 2018/3/22
        //mainTabSort.setChecked(true);
        if (currentTabIndex != 1) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.hide(fragments[currentTabIndex]);
            if (!fragments[1].isAdded()) {
                transaction.add(R.id.fragment_container, fragments[1]);
            }
            transaction.show(fragments[1]);
            transaction.commit();

        }
        currentTabIndex = 1;
        if (getIntent().getStringExtra("pushtype").equals("school")){
            secondParentFragment.pushtype = 1;
        }

    }

    //展示dialog
    private void showDialogByYorNo(String versionid) {

        if (Integer.valueOf(versionid)>LocalVersionUtil.getLocalVersion(context)){
            mDialog.setTitle("发现新版本");
            mDialog.setContent(versionModel.getDescription());
            mDialog.setOKButton("立即更新", new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //请求存储权限
//                    boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//                    if (!hasPermission) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
//                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) getBaseContext(),
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    } else {
                        //下载
                        startDownload();
//                    }

                }
            });
            mDialog.setCancelButton("退出", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
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
                    Toast.makeText(context, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
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
//        Intent it = new Intent(getBaseContext(), UpdateService.class);
//        //下载地址
//        Log.e("apkUrl", versionModel.getUrl());
//        it.putExtra("apkUrl", APK_DOWNLOAD_URL);
//        startService(it);

        removeOldApk();

        VersionUpdate.checkVersion(this,versionModel.getUrl());
        mDialog.dismiss();
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
                    Log.i(TAG, "下载进度：" + fraction);


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
    /**
     * 删除上次更新存储在本地的apk
     */
    private void removeOldApk() {
        //获取老ＡＰＫ的存储路径
        File fileName = new File((String) SPUtils.get(context,LocalConstant.SP_DOWNLOAD_PATH, ""));
        Log.i(TAG, "老APK的存储路径 =" + SPUtils.get(context,LocalConstant.SP_DOWNLOAD_PATH, ""));

        if (fileName != null && fileName.exists() && fileName.isFile()) {
            fileName.delete();
            Log.i(TAG, "存储器内存在老APK，进行删除操作");
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
}
