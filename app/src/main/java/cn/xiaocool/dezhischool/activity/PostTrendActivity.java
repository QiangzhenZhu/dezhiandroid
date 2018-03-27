package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.LocalImgGridAdapter;
import cn.xiaocool.dezhischool.bean.PhotoWithPath;
import cn.xiaocool.dezhischool.callback.PushImage;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.GalleryFinalUtil;
import cn.xiaocool.dezhischool.utils.GetImageUtil;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.PushImageUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.StringJoint;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.view.NoScrollGridView;

public class PostTrendActivity extends BaseActivity {

    @BindView(R.id.activity_post_trend_ed_content)
    EditText activityPostTrendEdContent;
    @BindView(R.id.activity_post_trend_gv_addpic)
    NoScrollGridView activityPostTrendGvAddpic;
    @BindView(R.id.addsn_tv_choose_class)
    TextView addsnTvChooseClass;
    @BindView(R.id.tv_select_count)
    TextView tvSelectCount;
    private Context context;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<PhotoWithPath> photoWithPaths;
    private LocalImgGridAdapter localImgGridAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private GalleryFinalUtil galleryFinalUtil;
    private String userid, classid;
    private int type;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x110:
                    ProgressUtil.dissmisLoadingDialog();
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))) {
                            finish();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_trend);
        ButterKnife.bind(this);
        setTopName("新增动态");
        setRightText("发布").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTrend();
            }
        });
        context = this;
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(9);
        userid = SPUtils.get(context, LocalConstant.USER_ID, "").toString();
        classid = null;
//        classid = SPUtils.get(context, LocalConstant.USER_CLASSID, "").toString();
        checkIdentity();
        setGrigView();
    }

    /**
     * 判断身份
     * 1-----家长
     * 2-----校长
     * 3-----老师
     */
    private void checkIdentity() {
        if (SPUtils.get(context, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
        } else if (SPUtils.get(context, LocalConstant.USER_IS_PRINSIPLE, "").equals("y")) {
            type = 2;
        } else {
            type = 3;
        }
    }


    private void sendTrend() {
        if (activityPostTrendEdContent.getText().toString().trim().equals("")) {
            ToastUtil.showShort(context, "发送内容不能为空！");
            return;
        }
        if (classid==null){
            ToastUtil.showShort(context, "请选择班级！");
            return;
        }
        if (photoWithPaths.size()==0){
            new SendRequest(context, handler).send_trend(userid,
                    SPUtils.get(context, LocalConstant.SCHOOL_ID, "1").toString(),
                    classid, activityPostTrendEdContent.getText().toString(), "null", "","",0x110);
            return;
        }
        ProgressUtil.showLoadingDialog(PostTrendActivity.this);
        //上传图片成功后发布
        new PushImageUtil().setPushIamge(this, photoWithPaths, new PushImage() {
            @Override
            public void success(boolean state) {
                //获得图片字符串
                ArrayList<String> picArray = new ArrayList<>();
                for (PhotoWithPath photo : photoWithPaths) {
                    picArray.add(photo.getPicname());
                }
                String picname = StringJoint.arrayJointchar(picArray, ",");
                new SendRequest(context, handler).send_trend(userid,
                        SPUtils.get(context, LocalConstant.SCHOOL_ID, "1").toString(),
                        classid, activityPostTrendEdContent.getText().toString(), picname,"","", 0x110);
            }

            @Override
            public void error() {
                ToastUtil.showShort(context, "图片上传失败!");
                ProgressUtil.dissmisLoadingDialog();
            }
        });
    }

    @Override
    public void requsetData() {

    }

    /**
     * 设置添加图片
     */
    private void setGrigView() {
        localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, context);
        activityPostTrendGvAddpic.setAdapter(localImgGridAdapter);
        activityPostTrendGvAddpic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mPhotoList.size()) {
                    showActionSheet();
                }
            }
        });
    }

    /**
     * 相册拍照弹出框
     */
    private void showActionSheet() {
        ActionSheet.createBuilder(context, getSupportFragmentManager())
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
                                galleryFinalUtil.openAblum(context, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(context, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                                    return;
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

                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, context);
                activityPostTrendGvAddpic.setAdapter(localImgGridAdapter);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 授权权限
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    galleryFinalUtil.openCamera(context, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtil.showShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }

    @OnClick(R.id.addsn_tv_choose_class)
    public void onClick() {
        Intent intent = new Intent(PostTrendActivity.this, ChooseClassActivity.class);
        intent.putExtra("type", "");
        startActivityForResult(intent, 101);
    }

    /**
     * 获取返回的接收人
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 101:
                    if (data != null) {
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

                        classid =null;
                        for (int i = 0; i < ids.size(); i++) {
                            classid = classid + "," + ids.get(i);
                        }
                        classid = classid.substring(5, classid.length());
                        tvSelectCount.setText("共选择" + ids.size() + "个班级");
//                        addsnTvChooseClass.setText(haschoose);
                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
