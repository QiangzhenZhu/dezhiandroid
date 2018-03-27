package cn.xiaocool.dezhischool.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andview.refreshview.callback.IFooterCallBack;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.leaveInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.SPUtils;

public class LeaveResumptionActivity extends BaseActivity {

    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.tv_child_name)
    TextView tvChildName;
    @BindView(R.id.tv_leave_type)
    TextView tvLeaveType;
    @BindView(R.id.tv_sumbit_time)
    TextView tvSumbitTime;
    @BindView(R.id.xiaojiayuanyin)
    TextView xiaojiayuanyin;
    @BindView(R.id.tv_leave_resumption_reason)
    EditText tvLeaveResumptionReason;
    @BindView(R.id.xiaojianshijian)
    TextView xiaojianshijian;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.tv_approval_teacher)
    TextView tvApprovalTeacher;
    @BindView(R.id.tv_leave_start)
    TextView tvLeaveStart;
    @BindView(R.id.tv_leave_end)
    TextView tvLeaveEnd;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.mDestoryLeave)
    TextView tvDestory;
    private static final String TAG = "LeaveResumptionActivity";
    private leaveInfo mleaveInfo;
    private Context mContext;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Calendar startTime;
    private Calendar endTime;
    private int timeType = 1;
    private String startTimeString;
    private String endTimeString;
    private String mDestoryReson;
    private String userType;
    private String userid;
    private String teacherid;
    private String schoolid;
    private String leaveid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_resumption);
        ButterKnife.bind(this);
        setTopName("申请销假");
        mContext =this;
        hideRightText();
        userType = (String) SPUtils.get(mContext,LocalConstant.USER_TYPE,"0");
        mleaveInfo = (leaveInfo) getIntent().getSerializableExtra("leaveInfo");
        init();
        userid = (String) SPUtils.get(mContext,LocalConstant.USER_ID,"");
        teacherid = mleaveInfo.getTeacherid();
        schoolid = (String) SPUtils.get(mContext,LocalConstant.USER_CLASSID,"");
        leaveid = mleaveInfo.getId();


        setDataDialog();
        setTimerDialog();
    }

    @Override
    public void requsetData() {

    }
    public void init(){
        Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL+mleaveInfo.getStudentavatar()).into(ivProfile);
        tvChildName.setText(mleaveInfo.getStudentname());
        tvLeaveType.setText(mleaveInfo.getLeavetype());
        tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
        //nineGridIv
        tvApply.setText(mleaveInfo.getParentname());
        tvApprovalTeacher.setText(mleaveInfo.getTeachername());
        tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
        tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
        tvDestory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mDestoryReson = tvLeaveResumptionReason.getText().toString().trim();
                String url = "";
                if (TextUtils.isEmpty(endTimeString)){
                    Toast.makeText(mContext,"请填写销假时间",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(mDestoryReson)){
                    Toast.makeText(mContext,"请填写销假原因",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("1".equals(userType)){//老师
                     url = NetConstantUrl.APPLY_FOR_BACK_LEAVE +"&userid="+userid+"&teacherid="+teacherid+"&schoolid="+schoolid+"&leaveid="+leaveid+"&backtime="+endTimeString+"&reason="+mDestoryReson+"&applytype="+"1";
                }else {//家长
                     url = NetConstantUrl.APPLY_FOR_BACK_LEAVE +"&userid="+userid+"&teacherid="+teacherid+"&schoolid="+schoolid+"&leaveid="+leaveid+"&backtime="+endTimeString+"&reason="+mDestoryReson;

                }

                VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            String status  = object.getString("status");
                            if ("success".equals(status)){
                                if ("1".equals(userType)) {
                                    Toast.makeText(mContext, "销假成功", Toast.LENGTH_SHORT).show();
                                    button.setText("销假成功");
                                    button.setEnabled(false);
                                    finish();
                                }else {
                                    button.setText("已申请销假");
                                    button.setEnabled(false);
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(mContext,"数据加载错误",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    public String formatTime(String longTime,int type){
        if (type == 1){
            String data = "yyyy-MM-dd HH:mm";
            long time = Long.valueOf(longTime)*1000L;
            Date date = new Date(time);
            return new SimpleDateFormat(data).format(date);
        }else {
            String data = "MM-dd HH:mm";
            long time = Long.valueOf(longTime)*1000L;
            Date date = new Date(time);
            return new SimpleDateFormat(data).format(date);
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
                    tvDestory .setText((format.format(startTime.getTime())));
                    Log.d(TAG, "onTimeSet: " + startTime);
                } else {
                    endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endTime.set(Calendar.MINUTE, minute);
                    long timeStampSec = endTime.getTime().getTime()/1000;
                    String timestamp = String.format("%010d", timeStampSec);
                    endTimeString = String.valueOf(timestamp);
                    SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
                    tvDestory .setText((format.format(endTime.getTime())));
                    Log.d(TAG, "onTimeSet: " + startTime);
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

    }

}
