package cn.xiaocool.dezhischool.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.callback.IFooterCallBack;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.MettingLocationAdapter;
import cn.xiaocool.dezhischool.bean.MeetingDetailBean;
import cn.xiaocool.dezhischool.bean.Meeting_people_detail;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MettingDetail extends BaseActivity implements View.OnClickListener{
    private LinearLayout mMeetingDetailTwo;
    private LinearLayout mMeetingDetailThree;
    private EditText mMettingContentText;
    private EditText mMettingTitleText;
    private TextView mMeetingLocation;
    private TextView mMeetingStart;
    private TextView mMeetingEnd;
    private TextView mMeetingPeople;
    private TextView mMeetingPeopleDetail;
    private TextView mMeetingModerator;
    private TextView mMeetingModeratorDetail;
    private TextView mMeetingRead;
    private TextView mMeetingReadDetail;
    private TextView mMeetingYindao;
    private TextView mMeetingYingdaoDetail;
    private TextView mMeetingShidao;
    private TextView mMeetingShidaoDetail;
    private TextView mMeetingChidao;
    private TextView mMeetingChidaoDetail;
    private TextView mMeetingQinagjia;
    private TextView mMeetingQinagjiaDetail;
    private TextView mMeetingZaotui;
    private TextView mMeetingZaotuiDetail;
    private TextView mMeetingQueqin;
    private TextView mMeetingQueqinDetail;
    private int type;
    private int id;
    private List<MeetingDetailBean.UserlistBean> userlist;
    private List<Meeting_people_detail> joinMeetingPeople;
    private List<Meeting_people_detail> readPeople;
    private List<Meeting_people_detail> yingdaoPeople;
    private List<Meeting_people_detail> shidaoPeople;
    private List<Meeting_people_detail> chidaoLatePeople;
    private List<Meeting_people_detail> qingjiaPeople;
    private List<Meeting_people_detail> leaveEarlyPeople;
    private List<Meeting_people_detail> queqinPeople;
    private List<Meeting_people_detail> test;
    private List<Meeting_people_detail> test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_detail);
        setTopName("会议详情");
        hideRightText();
        mMeetingDetailTwo = findViewById(R.id.meeting_detail_two);
        mMeetingDetailThree = findViewById(R.id.meeting_detail_three);
        mMettingTitleText = findViewById(R.id.meeting_detail_et_meeting_subject);
        mMettingContentText = findViewById(R.id.metting_detail_et_meeting_content);
        mMeetingLocation = findViewById(R.id.meeting_detail_text_meeting_room);
        mMeetingStart = findViewById(R.id.meeting_detail_text_meeting_start);
        mMeetingEnd = findViewById(R.id.meeting_detail_meeting_end_start);
        mMeetingPeople = findViewById(R.id.meeting_detail_meeting_people_list);
        mMeetingPeopleDetail = findViewById(R.id.meeting_detail_people_detail);
        mMeetingModerator = findViewById(R.id.meeting_detail_text_metting_moderator);
        mMeetingRead = findViewById(R.id.meeting_detail_meeting_read_list);
        mMeetingReadDetail = findViewById(R.id.meeting_detail_read_detail);
        mMeetingYindao = findViewById(R.id.meeting_detail_meeting_yingdao_list);
        mMeetingYingdaoDetail = findViewById(R.id.meeting_detail_yingdao_detail);
        mMeetingShidao = findViewById(R.id.meeting_detail_meeting_shidao_list);
        mMeetingShidaoDetail = findViewById(R.id.meeting_detail_shidao_detail);
        mMeetingChidao = findViewById(R.id.meeting_detail_meeting_chidao_list);
        mMeetingChidaoDetail = findViewById(R.id.meeting_detail_chidao_detail);
        mMeetingQinagjia = findViewById(R.id.meeting_detail_meeting_qingjia);
        mMeetingQinagjiaDetail = findViewById(R.id.meeting_detail_chidao_qingjia);
        mMeetingZaotui = findViewById(R.id.meeting_detail_meeting_zaotui);
        mMeetingZaotuiDetail = findViewById(R.id.meeting_detail_chidao_zaotui);
        mMeetingQueqin = findViewById(R.id.meeting_detail_text_meeting_queqin);
        mMeetingQueqinDetail = findViewById(R.id.meeting_detail_text_queqin);
        mMeetingShidaoDetail.setOnClickListener(this);
        mMeetingPeopleDetail.setOnClickListener(this);
        mMeetingReadDetail.setOnClickListener(this);
        mMeetingYingdaoDetail.setOnClickListener(this);
        mMeetingChidaoDetail.setOnClickListener(this);
        mMeetingQinagjiaDetail.setOnClickListener(this);
        mMeetingZaotuiDetail.setOnClickListener(this);
        mMeetingQueqinDetail.setOnClickListener(this);
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getIntExtra("id", 0);
        joinMeetingPeople = new ArrayList<>();
        readPeople = new ArrayList<>();
        yingdaoPeople = new ArrayList<>();
        shidaoPeople = new ArrayList<>();
        chidaoLatePeople = new ArrayList<>();
        qingjiaPeople = new ArrayList<>();
        leaveEarlyPeople = new ArrayList<>();
        queqinPeople = new ArrayList<>();
        test = new ArrayList<>();
        test1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Meeting_people_detail detail = new Meeting_people_detail();
            detail.setName("#测试" + i);
            detail.setComeTime(String.valueOf(1517982728));
            detail.setLevelTime(String.valueOf(1517982728));
            test.add(detail);
        }

        for (int j = 0; j < 10; j++) {
            Meeting_people_detail detail1 = new Meeting_people_detail();
            detail1.setName("#测试" + j);
            detail1.setLevelReason("起床没起来，上班迟到，上班迟到，起床没起来，上班迟到，上班迟到，起床没起来，上班迟到，上班迟到，起床没起来，上班迟到，上班迟到");
            test1.add(detail1);
        }


            if (type == 0) {
                mMeetingDetailTwo.setVisibility(View.GONE);
            } else if (type == 1) {
                mMeetingDetailThree.setVisibility(View.GONE);
            } else if (type == 3) {

            }
        }
    public void setDetailText(MeetingDetailBean bean){
        mMettingTitleText.setText(bean.getTitle());
        mMettingContentText.setText(bean.getDescription());
        mMeetingLocation.setText(bean.getRoom_name());
        mMeetingStart.setText(new SimpleDateFormat("M月dd日 HH:mm").format(Long.valueOf(bean.getBegintime())*1000L));
        mMeetingEnd.setText(new SimpleDateFormat("M月dd日 HH:mm").format(Long.valueOf(bean.getEndtime())*1000L));
        mMeetingPeople.setText(appendText(joinMeetingPeople));
        mMeetingModerator.setText(bean.getChair_name());
        mMeetingRead.setText(appendText(readPeople)); ;
        // TODO: 应道人未实现，接口问题，待证实













        if (type == 0){

        } else if (type == 1){

            mMeetingShidao.setText(appendText(shidaoPeople));
            mMeetingChidao.setText(appendText(chidaoLatePeople));
            mMeetingYindao.setText(appendText(joinMeetingPeople));
            //ToDo: 请假部分待实现
            mMeetingQinagjia.setText(appendText(qingjiaPeople)); ;


        } else if (type == 2){
            mMeetingShidao.setText(appendText(shidaoPeople));
            mMeetingChidao.setText(appendText(chidaoLatePeople));
            mMeetingZaotui.setText(appendText(leaveEarlyPeople));
            mMeetingYindao.setText(appendText(joinMeetingPeople));
            mMeetingQinagjia.setText(appendText(qingjiaPeople)); ;
            //ToDo 缺勤
            mMeetingQueqin.setText(appendText(queqinPeople));
        }

    }
    public String  appendText(List<Meeting_people_detail>list){
        StringBuilder builder = new StringBuilder();
        if (list.size() == 1) {
            builder.append(list.get(0).getName());
        }else if (list.size()>1 && list.size()<3){
            for (int i = 0; i < 2; i++) {
                builder.append(list.get(i).getName());
                builder.append("丶");
            }
        }else if (list.size()>2){
            for (int i = 0; i < 3; i++) {
                builder.append(list.get(i).getName());
                builder.append("丶");
            }
        }else if (list.size() == 0){
            builder.append("暂无");
        }
        return builder.toString();
    }

    @Override
    public void requsetData() {
        String schoolid = (String) SPUtils.get(this, LocalConstant.SCHOOL_ID, "1");
        final String userid = (String) SPUtils.get(this, LocalConstant.USER_ID,"");
        Uri uri = Uri.parse(NetConstantUrl.GET_CONFERENCE_BY_ID)
                .buildUpon()
                .appendQueryParameter("userid",userid)
                .appendQueryParameter("schoolid",schoolid)
                .appendQueryParameter("conference_id",String .valueOf(id))
                .build();
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MettingDetail.this,"数据获取错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String date = object.getString("data");
                        Gson gson = new Gson();
                        final MeetingDetailBean detailBean =gson.fromJson(date,MeetingDetailBean.class);
                        userlist = detailBean.getUserlist();
                        joinMeetingPeople.clear();
                        readPeople.clear();
                        yingdaoPeople.clear();
                        shidaoPeople.clear();
                        chidaoLatePeople.clear();
                        qingjiaPeople.clear();
                        leaveEarlyPeople.clear();
                        queqinPeople.clear();
                        for (int i = 0; i <userlist.size() ; i++) {
                            MeetingDetailBean.UserlistBean user = userlist.get(i);
                            //参会人员
                            Meeting_people_detail detail = new Meeting_people_detail();
                            detail.setId(user.getId());
                            detail.setName(user.getUsername());
                            joinMeetingPeople.add(detail);

                            if (!"0".equals(user.getReadtime())){
                                //已读
                                Meeting_people_detail detail1 = new Meeting_people_detail();
                                detail1.setId(user.getId());
                                detail1.setName(user.getUsername());
                                readPeople.add(detail1);
                            }
                            if (!"0".equals(user.getCometime())&&Long.valueOf(user.getCometime()) > Long.valueOf(detailBean.getBegintime())){
                                //迟到
                                Meeting_people_detail detail2 = new Meeting_people_detail();
                                detail2.setId(user.getId());
                                detail2.setName(user.getUsername());
                                detail2.setComeTime(user.getCometime());
                                detail2.setLevelTime(user.getLeavetime());
                                chidaoLatePeople.add(detail2);
                            }
                            if (!"0".equals(user.getLeavetime()) && Long.valueOf(user.getLeavetime()) < Long.valueOf(detailBean.getEndtime())) {
                                //早退
                                Meeting_people_detail detail3 = new Meeting_people_detail();
                                detail3.setId(user.getId());
                                detail3.setName(user.getUsername());
                                detail3.setComeTime(user.getCometime());
                                detail3.setLevelTime(user.getLeavetime());
                                leaveEarlyPeople.add(detail3);
                            }

                            if (!"0".equals(user.getCometime())&&Long.valueOf(user.getCometime())<Long.valueOf(detailBean.getBegintime())){
                                //实到
                                Meeting_people_detail detail4 = new Meeting_people_detail();
                                detail4.setId(user.getId());
                                detail4.setName(user.getUsername());
                                detail4.setComeTime(user.getCometime());
                                detail4.setLevelTime(user.getLeavetime());
                                shidaoPeople.add(detail4);
                            }
                            if ("0".equals(user.getCometime())&&"0".equals(user.getLeavetime())){
                                Meeting_people_detail detail5 = new Meeting_people_detail();
                                detail5.setId(user.getId());
                                detail5.setName(user.getUsername());
                                detail5.setComeTime(user.getCometime());
                                detail5.setLevelTime(user.getLeavetime());
                                queqinPeople.add(detail5);
                            }


                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDetailText(detailBean);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.meeting_detail_people_detail:
                //参会
                Intent intent = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent.putExtra("peopleList", (Serializable) joinMeetingPeople);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.meeting_detail_read_detail:
                //已读
                Intent intent1 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent1.putExtra("peopleList", (Serializable) readPeople);
                intent1.putExtra("type",1);
                startActivity(intent1);
                break;
            case R.id.meeting_detail_yingdao_detail:
                //应到
                Intent intent2 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent2.putExtra("peopleList", (Serializable) joinMeetingPeople);
                intent2.putExtra("type",2);
                startActivity(intent2);
                break;
            case R.id.meeting_detail_shidao_detail:

                Intent intent3 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent3.putExtra("peopleList", (Serializable) shidaoPeople);
                intent3.putExtra("type",3);
                startActivity(intent3);
                break;
            case R.id.meeting_detail_chidao_detail:
                Intent intent4 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent4.putExtra("peopleList", (Serializable) chidaoLatePeople);
                intent4.putExtra("type",4);
                startActivity(intent4);
                break;
            case R.id.meeting_detail_chidao_qingjia:
                Intent intent5 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent5.putExtra("peopleList", (Serializable) qingjiaPeople);
                intent5.putExtra("type",5);
                startActivity(intent5);
                break;
            case R.id.meeting_detail_chidao_zaotui:
                Intent intent6 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent6.putExtra("peopleList", (Serializable) leaveEarlyPeople);
                intent6.putExtra("type",6);
                startActivity(intent6);
                break;
            case R.id.meeting_detail_text_queqin:
                //缺勤
                Intent intent7 = new Intent(MettingDetail.this,MeetingDetailList.class);
                intent7.putExtra("peopleList", (Serializable) queqinPeople);
                intent7.putExtra("type",7);
                startActivity(intent7);
                break;

        }
    }
}
