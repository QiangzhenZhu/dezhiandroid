package cn.xiaocool.dezhischool.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.ProcessingInstruction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateWebMetting extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "CreateWebMetting";
    private ImageView ivLoading;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private EditText mMettingSubject;
    private EditText mMettingContent;
    private RelativeLayout mMettingLocation;
    private RelativeLayout mMettingStart;
    private RelativeLayout mMettingEnd;
    private RelativeLayout mMettingpeople;
    private RelativeLayout mMettingMoferator;
    private Button mPublishMetting;
    private TextView mMettingPeopleText;
    private TextView mMettongModeratorText;
    private TextView mMettingStartText;
    private TextView mMettingEndTeXT;
    private TextView mMettingLoation;
    private UserInfo mUserInfo;
    private String studentId;
    private String startTimeString;
    private String endTimeString;
    private String moderator;
    private String meetingTitle;
    private String meetingDesc;
    private String locationId;
    private String locationName;
    private String chairString;
    private Calendar startTime;
    private Calendar endTime;
    private int timeType;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_web_metting);
        setTopName("发布会议");
        hideRightText();
        mContext = this;
        ivLoading = findViewById(R.id.imageView9);
        ivLoading.setVisibility(View.GONE);
        mUserInfo = new UserInfo(CreateWebMetting.this);
        mPublishMetting = findViewById(R.id.publish_metting);
        mMettingSubject = findViewById(R.id.et_meeting_subject);
        mMettingContent = findViewById(R.id.et_meeting_content);
        mMettingLocation = findViewById(R.id.rl_meeting_location);
        mMettingStart = findViewById(R.id.rl_metting_start);
        mMettingEnd = findViewById(R.id.rl_metting_end);
        mMettingpeople = findViewById(R.id.rl_metting_people);
        mMettingMoferator = findViewById(R.id.rl_metting_moderator);
        mPublishMetting = findViewById(R.id.publish_metting);
        mMettingPeopleText = findViewById(R.id.meeting_people_list);
        mMettongModeratorText = findViewById(R.id.text_metting_moderator);
        mMettingStartText = findViewById(R.id.text_meeting_start);
        mMettingEndTeXT = findViewById(R.id.meeting_end_start);
        mMettingLoation = findViewById(R.id.text_meeting_location);
        mMettingLoation.setOnClickListener(this);
        mMettingEndTeXT.setOnClickListener(this);
        mMettingStartText.setOnClickListener(this);
        mMettingLocation.setOnClickListener(this);
        mMettingStart.setOnClickListener(this);
        mMettingEnd.setOnClickListener(this);
        mMettingpeople.setOnClickListener(this);
        mMettingMoferator.setOnClickListener(this);
        mPublishMetting.setOnClickListener(this);
        setDataDialog();
        setTimerDialog();

    }

    @Override
    public void requsetData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_meeting_location:
                Intent intent = new Intent(CreateWebMetting.this,SelectorMettingLocationActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_metting_start:
                timeType = 0;
                datePickerDialog.show();
                break;
            case R.id.rl_metting_end:
                timeType = 1;
                datePickerDialog.show();
                break;
            case R.id.rl_metting_people:
                Intent peopleIntent = new Intent(CreateWebMetting.this,MyClassListActivity.class);
                peopleIntent.putExtra("type","meeting");
                peopleIntent.putExtra("meeting","meeting");
                startActivityForResult(peopleIntent,3);
                break;
            case R.id.rl_metting_moderator:
                Intent moderatorIntent = new Intent(CreateWebMetting.this,MyClassListActivity.class);
                moderatorIntent.putExtra("type","meeting");
                startActivityForResult(moderatorIntent,4);
                break;
            case R.id.publish_metting:
                publishMeeting();

            default:
                break;
        }

    }
    public void publishMeeting(){
        if (!checkInformation()){
            return;
        }

        ivLoading.setVisibility(View.VISIBLE);
        String userid = (String) SPUtils.get(mContext, LocalConstant.USER_ID,"");
        String schoolid = (String) SPUtils.get(this, LocalConstant.SCHOOL_ID, "1");
        /*Uri uri = Uri.parse(NetConstantUrl.CREATE_CONFERENCE)
                .buildUpon()
                .appendQueryParameter("userid",userid)
                .appendQueryParameter("schoolid",schoolid)
                .appendQueryParameter("title",meetingTitle)
                .appendQueryParameter("description",meetingDesc)
                .appendQueryParameter("meetingroom",locatonString)
                .appendQueryParameter("begintime",startTimeString)
                .appendQueryParameter("endtime",endTimeString)
                .appendQueryParameter("personlist",studentId)
                .appendQueryParameter("chair_id",moderator)
                .build();*/
        String uri = NetConstantUrl.CREATE_CONFERENCE+"&userid="+userid+"&schoolid="+schoolid+"&title="+meetingTitle
                +"&description="+meetingDesc+"&meetingroom="+locationId+"&begintime="+startTimeString+"&endtime="+endTimeString
                +"&personlist="+studentId+"&chair_id="+moderator;
        OkhttpUtil.sendOkHttpRequest(uri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mContext,"数据获取失败",Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivLoading.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext,"发布成功",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext,"发布失败",Toast.LENGTH_SHORT).show();
                            ivLoading.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }
    public boolean checkInformation(){
        meetingTitle = mMettingSubject.getText().toString().trim();
        meetingDesc = mMettingContent.getText().toString().trim();

        if (TextUtils.isEmpty(startTimeString)){
            Toast.makeText(this,"请填写会议开始时间",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(endTimeString)){
            Toast.makeText(this,"请填写会议结束时间",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(meetingTitle)){
            Toast.makeText(this,"请填写会议主题",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(meetingDesc)){
            Toast.makeText(this,"请填写会议内容",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(studentId)){
            Toast.makeText(this,"请填写会议参与人",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(moderator)){
            Toast.makeText(this,"请填写会议主持人",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(locationId)){
            Toast.makeText(this,"请填写会议地点",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    public void setDataDialog(){
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(CreateWebMetting.this, new DatePickerDialog.OnDateSetListener() {
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
        timePickerDialog = new TimePickerDialog(CreateWebMetting.this,-1, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (timeType == 0) {
                    startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    startTime.set(Calendar.MINUTE, minute);
                    long timeStampSec = startTime.getTime().getTime()/1000;
                    String timestamp = String.format("%010d", timeStampSec);
                    startTimeString = String.valueOf(timestamp);
                    SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
                    mMettingStartText .setText((format.format(startTime.getTime())));
                    Log.d(TAG, "onTimeSet: " + startTime);
                } else {
                    endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endTime.set(Calendar.MINUTE, minute);
                    long timeStampSec = endTime.getTime().getTime()/1000;
                    String timestamp = String.format("%010d", timeStampSec);
                    endTimeString = String.valueOf(timestamp);
                    SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
                    mMettingEndTeXT .setText((format.format(endTime.getTime())));
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
                case 1:
                    locationId = data.getStringExtra("location");
                    locationName = data.getStringExtra("locationName");
                    mMettingLoation.setText(locationName);
                    break;
                case 2:

                    break;
                case 3:
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

                        for (int i = 0; i < ids.size(); i++){
                            studentId = studentId+","+ids.get(i);
                        }

                        studentId = studentId.substring(5,studentId.length());

                        mMettingPeopleText.setText(haschoose);
                    }

                    break;
                case 4:
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

                        for (int i = 0; i < ids.size(); i++){
                            studentId = studentId+","+ids.get(i);
                        }

                        moderator= studentId.substring(6,studentId.length());

                        mMettongModeratorText.setText(haschoose);
                    }

            }
        }
    }
}
