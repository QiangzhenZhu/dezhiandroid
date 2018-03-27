package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.TeacherReviewDetailAdapter;
import cn.xiaocool.dezhischool.adapter.TeacherReview_Adapter;
import cn.xiaocool.dezhischool.bean.TeacherReview;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetUtil;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.ui.ProgressViewUtil;
import cn.xiaocool.dezhischool.ui.list.PullToRefreshBase;
import cn.xiaocool.dezhischool.ui.list.PullToRefreshListView;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.DateUtils;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtils;

/**
 * Created by mac on 2017/6/19.
 */

public class TeacherReviewDetailActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout up_jiantou;
    private RelativeLayout last_month, next_month,rl_review;
    private TextView year_month;
    private int year, month;
    private PullToRefreshListView pull_listview;
    private String begintime,endtime;
    private TeacherReviewDetailAdapter adapter;
    private ArrayList<TeacherReview> teacherReviews;
    private ListView listView;
    private Context mContext;
    private TeacherReview_Adapter teacherReview_adapter;
    private String studentId,titlename;
    private int type;
    private TextView tv_class_name;
    private ImageView iv_down;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETTEACOMMENT:
                    JSONObject obj = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        teacherReviews.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < hwArray.length(); i++) {
                            itemObject = hwArray.optJSONObject(i);
                            TeacherReview teacherReview = new TeacherReview();
                            teacherReview.setId(itemObject.optString("comment_id"));
                            teacherReview.setTeacherAvator(itemObject.optString("teacher_photo"));
                            teacherReview.setTeacherName(itemObject.optString("teacher_name"));
                            teacherReview.setComment(itemObject.optString("comment_content"));
                            teacherReview.setTime(itemObject.optString("comment_time"));
                            teacherReview.setLearn(itemObject.optString("learn"));
                            teacherReview.setWork(itemObject.optString("work"));
                            teacherReview.setSing(itemObject.optString("sing"));
                            teacherReview.setLabour(itemObject.optString("labour"));
                            teacherReview.setStrain(itemObject.optString("strain"));
                            teacherReviews.add(teacherReview);
                        }
                        Collections.sort(teacherReviews, new Comparator<TeacherReview>() {
                            @Override
                            public int compare(TeacherReview lhs, TeacherReview rhs) {
                                return (int) (Long.parseLong(rhs.getTime())-Long.parseLong(lhs.getTime()));
                            }
                        });
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new TeacherReviewDetailAdapter(mContext,teacherReviews);
                            listView.setAdapter(adapter);
                        }
                    }else{
                        teacherReviews.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_teacher_review);
        mContext = this;
        hideTopView();

        ProgressViewUtil.show(mContext);
        SPUtils.remove(this, "JPUSHCOMMENT");
        teacherReviews = new ArrayList<>();
        checkIdentity();
        init();
        studentId = getIntent().getStringExtra("studentid");
        titlename = getIntent().getStringExtra("name");
        year = Integer.parseInt(getIntent().getStringExtra("year"));
        month = Integer.parseInt(getIntent().getStringExtra("month"));
        begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
        endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
        new SendRequest(mContext,handler).getTeacherComment(studentId,begintime,endtime);
    }
    @Override
    public void requsetData() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHCOMMENT");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHCOMMENT");
    }
    private void checkIdentity() {
        if(SPUtils.get(mContext, LocalConstant.USER_TYPE,"").equals("0")){
            type = 1;
        }else {
            type = 2;
        }
    }
    private void init() {
        pull_listview = (PullToRefreshListView) findViewById(R.id.teacher_review_listcontent);
        listView = pull_listview.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        last_month = (RelativeLayout) findViewById(R.id.rl_last);
        next_month = (RelativeLayout) findViewById(R.id.rl_next);
        year_month = (TextView) findViewById(R.id.year_month);
        teacherReview_adapter=new TeacherReview_Adapter(this);
        rl_review = (RelativeLayout) findViewById(R.id.rl_review);
        rl_review.setVisibility(View.GONE);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        tv_class_name.setVisibility(View.VISIBLE);
        tv_class_name.setText(titlename);
        if (type == 2){
            tv_class_name.setText("评价");
            rl_review.setVisibility(View.VISIBLE);
            final String studentId= getIntent().getStringExtra("studentid");
            rl_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,SpaceClickTeacherRemarkONActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("studentid",studentId);
                    startActivity(intent);
                }
            });
        }
        iv_down =(ImageView)findViewById(R.id.iv_xiala);
        iv_down.setVisibility(View.GONE);
//        if(type==2){
//            rl_review.setOnClickListener(this);
//        }else{

//        }
        //获取今天的年月
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;

        year_month = (TextView) findViewById(R.id.year_month);
        year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
        Log.d("年-月", String.valueOf(year) + String.valueOf(month));
        last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month - 1;
                if (month == 0) {
                    month = 12;
                    year = year - 1;
                }
                year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
                begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
                endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
                new SendRequest(mContext,handler).getTeacherComment(studentId,begintime, endtime);
            }
        });
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month + 1;
                if (month == 13) {
                    month = 1;
                    year = year + 1;
                }
                year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
                begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
                endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
                new SendRequest(mContext,handler).getTeacherComment(studentId,begintime, endtime);
            }
        });

        pull_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (NetUtil.isConnnected(getApplicationContext()) == true) {
                    getAllInformation();
                } else {
                    ToastUtils.ToastShort(getApplicationContext(), "暂无网络");
                }
                /**
                 * 过1秒结束下拉刷新
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull_listview.onPullDownRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                /**
                 * 过1秒后 结束向上加载
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull_listview.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
        }

    }
    private void getAllInformation() {
        begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
        endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
        new SendRequest(mContext,handler).getTeacherComment(studentId,begintime, endtime);
    }


}

