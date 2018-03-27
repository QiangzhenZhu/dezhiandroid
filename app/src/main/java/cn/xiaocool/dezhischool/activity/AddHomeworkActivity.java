package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.LocalImgGridAdapter;
import cn.xiaocool.dezhischool.bean.PhotoWithPath;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.callback.PushImage;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetUtil;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.GetImageUtil;
import cn.xiaocool.dezhischool.utils.PicassoImageLoader;
import cn.xiaocool.dezhischool.utils.PicassoPauseOnScrollListener;
import cn.xiaocool.dezhischool.utils.PushImageUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.StringJoint;
import cn.xiaocool.dezhischool.utils.ToastUtils;

/**
 * Created by mac on 2017/5/25.
 */

public class AddHomeworkActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_select_count;
    private ImageView btn_exit;
    private EditText homework_title;
    private EditText homework_content;
    private Intent intent;
    private TextView homework_send, tv_choose_class,choose_subject;
    private GridView homework_pic_grid;
    private static final int ADD_KEY = 4;
    private LocalImgGridAdapter localImgGridAdapter;
    private UserInfo user = new UserInfo();
    private Context mContext;
    private String studentids=null;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<PhotoWithPath> photoWithPaths;
    private String pushImgName;

    //    private ArrayList<String> mPhototNames;
    private KProgressHUD hud;
    private FunctionConfig functionConfig;
    private String subject = "null";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_KEY:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            hud.dismiss();

                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 5231:
                    mPhotoList.remove((int)msg.obj);
                    localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext);
                    homework_pic_grid.setAdapter(localImgGridAdapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);
        hideTopView();
        mContext = this;
        user.readData(mContext);
        initView();
    }

    @Override
    public void requsetData() {

    }

    private void initView() {

        tv_select_count = (TextView) findViewById(R.id.tv_select_count);
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        tv_choose_class = (TextView) findViewById(R.id.choose_class);
        tv_choose_class.setOnClickListener(this);
        choose_subject = (TextView) findViewById(R.id.choose_subject);
        choose_subject.setOnClickListener(this);
        homework_title = (EditText) findViewById(R.id.homework_title);
        homework_content = (EditText) findViewById(R.id.homework_content);
//         homework_receiveList = (TextView) findViewById(R.id.homework_receiveList);
        homework_pic_grid = (GridView) findViewById(R.id.homework_pic_grid);
        homework_send = (TextView) findViewById(R.id.homework_send);
        homework_send.setOnClickListener(this);
        intent = getIntent();
        String classname = intent.getStringExtra("className");

        tv_choose_class.setText(classname);
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
//        mPhototNames = new ArrayList<>();

        //添加图片按钮
        localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, AddHomeworkActivity.this);
        homework_pic_grid.setAdapter(localImgGridAdapter);
        homework_pic_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", String.valueOf(position));
                if (position == mPhotoList.size()) {
                    showActionSheet();
                }

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.choose_class:
                Intent intent = new Intent(AddHomeworkActivity.this, MyClassListActivity.class);
                intent.putExtra("type", "student");
                startActivityForResult(intent, 101);
                break;
            case R.id.homework_send:
                if (homework_title.getText().length() > 0 || homework_content.getText().length() > 0) {

                    if (subject.equals("null")||subject.equals("")){
                        Toast.makeText(this, "请选择科目！", Toast.LENGTH_SHORT).show();
                    }else {
                        if (NetUtil.isConnnected(this)) {
                            if (studentids.equals(null)){
                                Toast.makeText(this, "请选择接收人！", Toast.LENGTH_SHORT).show();
                            }else {
                                if (mPhotoList.size() > 0) {


                                    hud = KProgressHUD.create(this)
                                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                            .setCancellable(true);
                                    hud.show();
                                    new PushImageUtil().setPushIamge(mContext, photoWithPaths, new PushImage() {
                                        @Override
                                        public void success(boolean state) {
                                            hud.dismiss();
                                            //获得图片字符串
                                            ArrayList<String> picArray = new ArrayList<>();
                                            for (PhotoWithPath photo : photoWithPaths) {
                                                picArray.add(photo.getPicname());
                                            }
                                            pushImgName = StringJoint.arrayJointchar(picArray, ",");
//                                            pushImgName = StringUtils.listToString(mPhototNames,",");
                                            send();
                                        }

                                        @Override
                                        public void error() {
                                            hud.dismiss();
                                            ToastUtils.ToastShort(mContext,"图片上传失败！");
                                        }
                                    });


                                } else {
                                    hud = KProgressHUD.create(this)
                                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                            .setCancellable(true);
                                    hud.show();
                                    new SendRequest(AddHomeworkActivity.this,handler).send_homework(SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "")+"",SPUtils.get(mContext, LocalConstant.USER_ID, "")+"",homework_title.getText().toString(), homework_content.getText().toString(),studentids,pushImgName,subject,ADD_KEY);
//
                                }
                            }



                        } else {
                            Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    ToastUtils.ToastShort(AddHomeworkActivity.this, "发送内容不能为空");
                }


                break;
            case R.id.choose_subject:
                Intent intent1 = new Intent(AddHomeworkActivity.this, ChooseSubjectActivity.class);
                startActivityForResult(intent1, 102);
                break;

        }
    }

    private void send() {

        new SendRequest(AddHomeworkActivity.this,handler).send_homework(SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "")+"",SPUtils.get(mContext, LocalConstant.USER_ID, "")+"",homework_title.getText().toString(), homework_content.getText().toString(),studentids,pushImgName,subject,ADD_KEY);

    }

    /**
     * 展示dialog
     */

    private void showActionSheet() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new PicassoImageLoader();
        pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
        functionConfigBuilder.setMutiSelectMaxSize(9);
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#9BE5B4"))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.parseColor("#9BE5B4"))
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#9BE5B4"))
                .setIconBack(R.drawable.ic_fanhui)
                .setIconRotate(R.mipmap.ic_action_repeat)
                .setIconCrop(R.mipmap.ic_action_crop)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                                break;
                            case 1:

                                //获取拍照权限
                                if (hasPermission("android.permission.CAMERA")) {
                                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // 判断存储卡是否可以用，可用进行存储
                                    String state = Environment.getExternalStorageState();
                                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                        File file = new File(path, "newpic.png");
                                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                    }
                                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);

                                } else {


                                    String[] perms = {"android.permission.CAMERA"};

                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }

                                }

                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }


    private boolean canMakeSmores() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private boolean hasPermission(String permission) {

        if (canMakeSmores()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }

        }

        return true;

    }


    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtils.ToastShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }


    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {

                photoWithPaths.clear();
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                photoWithPaths.addAll(GetImageUtil.getImgWithPaths(resultList));

//                mPhotoList.clear();
//
//                mPhotoList.addAll(resultList);
                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, AddHomeworkActivity.this);
                homework_pic_grid.setAdapter(localImgGridAdapter);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    //重写onActivityResult以获得你需要的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case 101:
                    if (data != null) {
                        String sss = data.getStringExtra("sss");
                        Log.e("sssss", sss);
                        ArrayList<String> ids = data.getStringArrayListExtra("ids");
                        ArrayList<String> names = data.getStringArrayListExtra("names");
                        String haschoose = "";
                        for (int i = 0; i < names.size(); i++) {
                            if (i < 3) {
                                if (names.get(i) != null || names.get(i) != "null") {
                                    haschoose = haschoose + names.get(i) + "、";
                                }
                            } else if (i == 4) {
                                haschoose = haschoose.substring(0, haschoose.length() - 1);
                                haschoose = haschoose + "等...";
                            }

                        }

                        for (int i = 0; i < ids.size(); i++) {
                            studentids = studentids + "," + ids.get(i);
                        }
                        studentids = studentids.substring(5, studentids.length());
                        tv_choose_class.setText(haschoose);
                        tv_select_count.setText("共选择" + ids.size() + "人");
                    }

                    break;

                case 102:
                    subject = data.getStringExtra("subject");
                    choose_subject.setText(subject.equals("null")?"":subject);
                    break;
            }
        }


    }


}
