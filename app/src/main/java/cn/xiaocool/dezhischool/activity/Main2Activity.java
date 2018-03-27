package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.GridIconAdapter;
import cn.xiaocool.dezhischool.bean.GridIcon;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.utils.SPUtils;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.web_button_grid_view)
    GridView webButtonGridView;
    private BaseAdapter adapter = null;
    private List<GridIcon> icons = new ArrayList<>();
    private Context mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_button_layout_grid_view);
        ButterKnife.bind(this);
        mActivity = this;
        initGridButton();
        adapter = new GridIconAdapter(this,R.layout.item_grid_icon,icons);
        webButtonGridView.setAdapter(adapter);
        webButtonGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridIcon gridIcon = icons.get(position);
                Intent intent;
                switch (gridIcon.getViewId()) {
                    case R.id.btn_project_studenthomework:
                        break;
                    case R.id.web_rl_schoolnotice://校园公告
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "校园公告");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_NOTICE);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_schoolnotice1://板报
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "板报");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_NEWS);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_classnotice://班级通知
                        startActivity(new Intent(mActivity, ClassNewsActivity.class));
                        break;
                    case R.id.web_project_homework://班级作业
                        startActivity(new Intent(mActivity, SpaceClickHomeworkActivity.class));
                        break;
                    case R.id.web_rl_clubactivity://社团活动
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "社团活动");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_ACTIVITY);
                        startActivity(intent);
                        break;

                    case R.id.web_rl_schoolevaluate://在校点评
                       /* checkIdentity();
                        if(type==1){
                            //获取今天的年月
                            Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH)+1;
                            Intent intentv = new Intent(mActivity, TeacherReviewDetailActivity.class);
                            intentv.putExtra("name", SPUtils.get(mActivity, LocalConstant.USER_NAME,"")+"");
                            intentv.putExtra("year",year+"");
                            intentv.putExtra("month",month+"");
                            intentv.putExtra("userid",SPUtils.get(mActivity, LocalConstant.USER_ID,"")+"");
                            intentv.putExtra("studentid", SPUtils.get(mActivity, LocalConstant.USER_BABYID,"")+"");
                            startActivity(intentv);
                        }else if(type==2){
                            startActivity(new Intent(mActivity,SpaceClickTeacherReviewActivity.class));
                        }*/
                        break;
                    case R.id.web_rl_schoolreport://成绩单
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "成绩单");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_STUDENT);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_alumniassociation://网络课堂
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "网络课堂");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_TEACHER);
                        startActivity(intent);
                        break;
                    case R.id.news_more://
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "新闻动态");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_NEWS);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_map://行为轨迹
                        intent = new Intent(mActivity, MapActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_classkaoqin://学生考勤
                        intent = new Intent(mActivity, AttanceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_olineqinjia://请假申请
                        intent = new Intent(mActivity, SpaceClickLeaveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_classlesson://课程表
                        intent = new Intent(mActivity, SpaceClickClassActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_metting://会议考勤
                        intent = new Intent(mActivity, Main2Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_dormitory_attendance://宿舍考勤
                /*intent = new Intent(mActivity, AttanceActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);*/
                        intent = new Intent(mActivity,DormitoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_school_attendance:
                        intent = new Intent(mActivity, AttanceActivity.class);
                        intent.putExtra("type",1);
                        startActivity(intent);
                        break;

                    default:break;
                }

            }
        });
    }
    public void initGridButton(){

        final GridIcon icon = new GridIcon(R.drawable.schoolnotice,"校园公告",R.id.web_rl_schoolnotice);
        icons.add(icon);
        GridIcon icon1 = new GridIcon(R.drawable.classnotice,"班级通知",R.id.web_rl_classnotice);
        icons.add(icon1);
        GridIcon icon2 = new GridIcon(R.mipmap.come_leave_school,"门口考勤",R.id.web_rl_school_attendance);
        icons.add(icon2);
        GridIcon icon3 = new GridIcon(R.mipmap.domitory_attendance,"宿舍考勤",R.id.web_rl_dormitory_attendance);
        icons.add(icon3);
        GridIcon icon4 = new GridIcon(R.drawable.studentattendance,"学生考勤",R.id.web_rl_classkaoqin);
        icons.add(icon4);
        GridIcon icon5 = new GridIcon(R.drawable.askforleave,"请假申请",R.id.web_rl_olineqinjia);
        icons.add(icon5);
        GridIcon icon6 = new GridIcon(R.drawable.timetable,"课程表",R.id.web_rl_classlesson);
        icons.add(icon6);
        GridIcon icon7 = new GridIcon(R.drawable.studenthomework,"学生作业",R.id.web_project_homework);
        icons.add(icon7);
        GridIcon icon8 = new GridIcon(R.drawable.schoolreport,"成绩单",R.id.web_rl_schoolreport);
        icons.add(icon8);
        GridIcon icon9 = new GridIcon(R.drawable.schoolnewspaper,"板报",R.id.web_rl_schoolnotice1);
        icons.add(icon9);
        GridIcon icon10 = new GridIcon(R.drawable.clubactivity,"社团活动",R.id.web_rl_clubactivity);
        icons.add(icon10);
        GridIcon icon11 = new GridIcon(R.drawable.schoolnotice,"网络课堂",R.id.web_rl_alumniassociation);
        icons.add(icon11);
        GridIcon icon12 = new GridIcon(R.drawable.schoolevaluate,"在校评价",R.id.web_rl_schoolevaluate);
        icons.add(icon12);
        GridIcon icon13 = new GridIcon(R.mipmap.meeting_attendance,"会议考勤",R.id.web_rl_metting);
        icons.add(icon13);
        GridIcon icon14 = new GridIcon(R.drawable.xingweiguiji,"行为轨迹",R.id.web_rl_map);
        icons.add(icon14);
    }
}
