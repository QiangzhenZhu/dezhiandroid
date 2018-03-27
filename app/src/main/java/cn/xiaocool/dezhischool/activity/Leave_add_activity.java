package cn.xiaocool.dezhischool.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.LocalImgGridAdapter;
import cn.xiaocool.dezhischool.bean.ApprovalTeacherInfo;
import cn.xiaocool.dezhischool.bean.BabyInfo;
import cn.xiaocool.dezhischool.bean.PhotoWithPath;
import cn.xiaocool.dezhischool.bean.SingleBabyInfos;
import cn.xiaocool.dezhischool.callback.PushImage;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.net.WebHome;
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

//import android.graphics.Color;

/**
 * Created by mac on 2017/6/7.
 */

public class Leave_add_activity extends BaseActivity implements View.OnClickListener {
   private final int REQUEST_CODE_CAMERA = 1000;
   private final int REQUEST_CODE_GALLERY = 1001;
   private static final String TAG = "Leave_add_activity";
   private Context mContext;
   private TimePickerDialog timePickerDialog;
   private DatePickerDialog datePickerDialog;
   private ImageView mProfileImage;
   private TextView mApplyPeopleText;
   private EditText mApplyReasonEdit;
   private RadioButton mCasualLeave;
   private RadioButton mSickLeave;
   private TextView mLeaveStart;
   private TextView mLeaveEnd;
   private Spinner mSpinner;
   private TextView mSubmit;
   private TextView mApprovalTeacher;
   private Calendar startTime;
   private Calendar endTime;
   private RadioGroup group;
   private int timeType;
   private List<String> mSpinnerList;
   private List<ApprovalTeacherInfo> mTeacherSpinnerList;
   private ArrayAdapter<String> adapter;
   private ArrayAdapter<String> mTeacherSpinerAdapter;
   private ApprovalTeacherInfo teacherInfo;
   private LocalImgGridAdapter localImgGridAdapter;
   private NoScrollGridView activityPostTrendGvAddpic;
   private ArrayList<PhotoInfo> mPhotoList;
   private ArrayList<PhotoWithPath> photoWithPaths;
   private GalleryFinalUtil galleryFinalUtil;
   private List<BabyInfo> mBabyInfos;
   private String schoolId;
   private String studentId;
   private String parentId;
   private String teacherId;
   private String classId;
   private String startTimeString;
   private String endTimeString;
   private String leaveReason;
   private String leaveType;

   private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x110:
                    ProgressUtil.dissmisLoadingDialog();
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf(msg.obj))) {
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
        setContentView(R.layout.parent_request_leave);
        mContext = this;
        setTopName("申请请假");
        hideRightText();
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        schoolId = (String) SPUtils.get(mContext,LocalConstant.SCHOOL_ID,"");
        getBabyInfo();


        init();

        parentId = (String) SPUtils.get(mContext,LocalConstant.USER_ID,"");
        setGrigView();
        setDataDialog();
        setTimerDialog();



    }

    @Override
    public void requsetData() {

    }

    private void getBabyInfo() {
        final String baby_url = NetConstantUrl.GET_USER_RELATION + "&userid=" +SPUtils.get(mContext,LocalConstant.USER_ID,"0");
        VolleyUtil.VolleyGetRequest(mContext, baby_url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                List<BabyInfo> allbaby ;
                allbaby = new ArrayList<BabyInfo>();
                allbaby = getBabyInfoFromJson(result);
                int babycount=allbaby.size();
                boolean hadbaby;
                hadbaby=false;
                if (babycount > 0){
                    hadbaby = true;
                }
                SingleBabyInfos.getmBabyInfos().getInfos().clear();
                for(int i=0;i<babycount;i++){

                    SingleBabyInfos.getmBabyInfos().getInfos().add(allbaby.get(i));


                }
                mBabyInfos = SingleBabyInfos.getmBabyInfos().getInfos();
                setDate();
//                BabyInfo babyInfo = getBabyInfoFromJson(result).get(0);
//                SPUtils.put(context, LocalConstant.USER_BABYID, babyInfo.getStudentid());
//                SPUtils.put(context, LocalConstant.USER_CLASSID, babyInfo.getClasslist().get(0).getClassid());
//                SPUtils.put(context,LocalConstant.CLASS_NAME,babyInfo.getClasslist().get(0).getClassname());
//                SPUtils.put(context, LocalConstant.SCHOOL_ID,babyInfo.getClasslist().get(0).getSchoolid());
//                startActivity(MainActivity.class);
//                finish();
            }

            @Override
            public void onError() {

            }
        });
    }
    public void setDate(){
        List<String> mTeacherInfo = new ArrayList<>();
        mSpinnerList = new ArrayList<>();
        for (int i = 0; i < mSpinnerList.size(); i++) {
            mTeacherInfo.add(mTeacherSpinnerList.get(i).getName());
        }
        mTeacherSpinnerList = new ArrayList<>();
        for (int i = 0; i <mBabyInfos.size() ; i++) {
            mSpinnerList.add(mBabyInfos.get(i).getStudentname());
        }
        mTeacherSpinerAdapter = new ArrayAdapter<String>(mContext,R.layout.spinner_style,mTeacherInfo);
        adapter = new ArrayAdapter<String>(mContext,R.layout.spinner_style,mSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTeacherSpinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentId = mBabyInfos.get(position).getStudentid();
                classId = mBabyInfos.get(position).getClasslist().get(0).getClassid();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    /**
     * 字符串转模型（宝宝信息）
     *
     * @param * @return
     */
    private List<BabyInfo> getBabyInfoFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<BabyInfo>>() {
        }.getType());
    }




    public void init(){
        mApprovalTeacher = findViewById(R.id.textView48);
        mApprovalTeacher.setOnClickListener(this);
        mLeaveStart = findViewById(R.id.textView45);
        mLeaveEnd = findViewById(R.id.textView47);
        mLeaveStart.setOnClickListener(this);
        mLeaveEnd.setOnClickListener(this);
        mApplyReasonEdit = findViewById(R.id.editText);
        mApplyPeopleText = findViewById(R.id.textView19);
        mApplyPeopleText.setText((String)SPUtils.get(mContext,LocalConstant.USER_NAME,""));
        galleryFinalUtil = new GalleryFinalUtil(9);
        String userid = userid = SPUtils.get(mContext, LocalConstant.USER_ID, "").toString();
        activityPostTrendGvAddpic = findViewById(R.id.activity_parent_request_leave_add_pics);
        group = findViewById(R.id.radioGroup);
        mCasualLeave = findViewById(R.id.radioButton);
        mSickLeave = findViewById(R.id.radioButton2);

        // TODO: 2018/2/24
        mSubmit = findViewById(R.id.button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(mContext,LeavDetailActivity.class);
                //startActivity(intent);
                if (checkDate()){ //判断所有内容是否填写齐全
                    sendTrend();
                }

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //病假，事假
                if (mSickLeave.getId() == checkedId){
                    leaveType = "病假";

                }else if (mCasualLeave.getId() == checkedId){
                    leaveType = "事假";

                }
            }
        });
        mProfileImage = findViewById(R.id.imageView6);
        mApplyPeopleText = findViewById(R.id.textView19);
        mSpinner = findViewById(R.id.spinner);
        mApplyReasonEdit = findViewById(R.id.editText);

    }
    public boolean checkDate(){
        leaveReason = mApplyReasonEdit.getText().toString().trim();
        if (TextUtils.isEmpty(studentId)){
            Toast.makeText(mContext,"请填写请假人姓名",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(schoolId)){
            Toast.makeText(mContext,"schoolId不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(parentId)){
            Toast.makeText(mContext,"parentId不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(teacherId)){
            Toast.makeText(mContext,"teacherId不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(classId)){
            Toast.makeText(mContext,"classId不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(startTimeString)){
            Toast.makeText(mContext,"开始时间不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(endTimeString)){
            Toast.makeText(mContext,"结束时间不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(leaveReason)){
            Toast.makeText(mContext,"填写请假原因",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(leaveType)){
            Toast.makeText(mContext,"选择请假类型",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 设置添加图片
     */
    private void setGrigView() {
        localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext);
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
                                galleryFinalUtil.openAblum(mContext, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(mContext, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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

                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext);
                activityPostTrendGvAddpic.setAdapter(localImgGridAdapter);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
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
                    galleryFinalUtil.openCamera(mContext, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtil.showShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }

    private void sendTrend() {

        if (photoWithPaths.size()==0){
            /*ProgressUtil.showLoadingDialog(mContext);
            new SendRequest(mContext, handler).add_leave(schoolId,studentId,parentId,teacherId,classId,startTimeString,endTimeString,leaveReason,leaveType);
            return;*/
            Log.d(TAG, "getApprovalTeacherInfo: "+schoolId  + classId);
            final String add_leave = WebHome.LEAVE_ADD + "&begintime=" + startTimeString + "&endtime=" + endTimeString + "&parentid=" + parentId+
                    "&reason=" + leaveReason + "&studentid=" + studentId + "&teacherid=" + teacherId  + "&leavetype=" + leaveType+"&classid="+classId+"&schoolid="+schoolId;
            VolleyUtil.VolleyGetRequest(mContext, add_leave, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                   /* Intent intent = new Intent(mContext,LeavDetailActivity.class);
                    startActivity(intent);*/
                   finish();


                }

                @Override
                public void onError() {

                }
            });
            return;
        }
        ProgressUtil.showLoadingDialog(mContext);
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
                /*new SendRequest(mContext, handler).send_trend(userid,
                        SPUtils.get(context, LocalConstant.SCHOOL_ID, "1").toString(),
                        classid, activityPostTrendEdContent.getText().toString(), picname,"","", 0x110);*/
                final String add_leave = WebHome.LEAVE_ADD + "&begintime=" + startTimeString + "&endtime=" + endTimeString + "&parentid=" + parentId+
                        "&reason=" + leaveReason + "&studentid=" + studentId + "&teacherid=" + teacherId  + "&leavetype=" + leaveType+"&classid="+classId+"&schoolid="+schoolId + "&picture_url="+picname;
                VolleyUtil.VolleyGetRequest(mContext, add_leave, new VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        /*Intent intent = new Intent(mContext,LeavDetailActivity.class);
                        startActivity(intent);*/
                        finish();


                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void error() {
                ToastUtil.showShort(mContext, "图片上传失败!");
                ProgressUtil.dissmisLoadingDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView45:
                timeType = 0;
                datePickerDialog.show();
                break;
            case R.id.textView47:
                timeType = 1;
                datePickerDialog.show();
                break;
            case R.id.textView48:
                Intent intent = SelectedApprovalTeacherActivity.newIntent(mContext,classId,schoolId);
                startActivityForResult(intent,0);

            default:
                break;
        }
    }
   public void setDataDialog(){
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (timeType == 0) {
                    startTime = Calendar.getInstance();
                    startTime.set(year, month, dayOfMonth);
                    timePickerDialog.show();
                } else {
                    endTime = Calendar.getInstance();
                    endTime.set(year, month, dayOfMonth);
                    timePickerDialog.show();
                }

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

    }

    public void setTimerDialog(){
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(mContext,-1, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (timeType == 0) {
                    startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    startTime.set(Calendar.MINUTE, minute);
                    long timeStampSec = startTime.getTime().getTime()/1000;
                    String timestamp = String.format("%010d", timeStampSec);
                    startTimeString = String.valueOf(timestamp);
                    SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
                    mLeaveStart .setText((format.format(startTime.getTime())));
                    Log.d(TAG, "onTimeSet: " + startTime);
                } else {
                    endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endTime.set(Calendar.MINUTE, minute);
                    long timeStampSec = endTime.getTime().getTime()/1000;
                    String timestamp = String.format("%010d", timeStampSec);
                    endTimeString = String.valueOf(timestamp);
                    SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
                    mLeaveEnd .setText((format.format(endTime.getTime())));
                    Log.d(TAG, "onTimeSet: " + startTime);
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    teacherId = data.getStringExtra("teacherid");
                    String teacherName = data.getStringExtra("teachername");
                    mApprovalTeacher.setText(teacherName);
            }
        }
    }
}