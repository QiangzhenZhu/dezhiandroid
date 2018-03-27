package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.SelectClassAdapter;
import cn.xiaocool.dezhischool.adapter.TeacherReviewAdat;
import cn.xiaocool.dezhischool.bean.ClassList;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.ui.ProgressViewUtil;
import cn.xiaocool.dezhischool.ui.list.PullToRefreshListView;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.TimeToolUtils;

/**
 * Created by hzh on 17/3/22.
 */

public class SpaceClickTeacherReviewActivity extends BaseActivity implements View.OnClickListener {

    private UserInfo user = new UserInfo();
    private RelativeLayout up_jiantou;
    private RelativeLayout last_month,next_month,rl_review;
    private Context mContext;
    private TextView year_month;
    private PullToRefreshListView pull_listview;
    private ListView listView;
    private View anchor;
    private TextView tv_class_name;
    private LinearLayout ll_down;
    private RequestQueue mQueue;
    private  int year,month,day;
    private ArrayList<ClassList.ClassListData> dataArrayList = new ArrayList<>();
    private TeacherReviewAdat teacherReviewAdat;
    private int index=0;
    private int type;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 101:

                    try {
//                        if (teacherReviewAdat!=null){
//                            teacherReviewAdat.notifyDataSetChanged();
//                        }else {
                        if (dataArrayList.size()>0){
                            teacherReviewAdat = new TeacherReviewAdat(mContext,dataArrayList.get(index).getStudentlist());
                            listView.setAdapter(teacherReviewAdat);
//                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_teacher_review);
        ProgressViewUtil.show(this);
        mContext = this;
        mQueue = Volley.newRequestQueue(mContext);
        user.readData(mContext);
        checkIdentity();
        hideTopView();
        initView();
        pull_listview = (PullToRefreshListView) findViewById(R.id.teacher_review_listcontent);
        listView = pull_listview.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
//        new NewsRequest(mContext,handler).teacherReview();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, TeacherReviewDetailActivity.class);

                intent.putExtra("name", dataArrayList.get(index).getStudentlist().get(position).getName());
                intent.putExtra("year",year+"");
                intent.putExtra("month",month+"");
                intent.putExtra("userid",user.getId());
                Bundle bundle = new Bundle();
                bundle.putSerializable("comments", dataArrayList.get(index).getStudentlist().get(position).getTeacherComments());

                intent.putExtra("studentid", dataArrayList.get(index).getStudentlist().get(position).getId());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }
    private void checkIdentity() {
        if(SPUtils.get(mContext, LocalConstant.USER_TYPE,"").equals("0")){
            type = 1;
        }else {
            type = 2;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        volleyRefrsh();
    }

    @Override
    public void requsetData() {

    }

    private void volleyRefrsh() {


        long begintime = TimeToolUtils.getMonthBeginTimestamp(year, month)/1000;
        long endtime = TimeToolUtils.getMonthEndTimestamp(year, month)/1000;
        mQueue = Volley.newRequestQueue(mContext);
        String URL = NetConstantUrl.GET_TEACHERREVIEW+"&teacherid="+ SPUtils.get(mContext, LocalConstant.USER_ID, "")+"&begintime="+begintime+"&endtime="+endtime;
        Log.e("uuuurrrrll", URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {



            @Override
            public void onResponse(String arg0) {
//                ToastUtils.ToastShort(mContext, arg0);
                Log.d("onResponse", arg0);
                ProgressViewUtil.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(arg0);
                    String state = jsonObject.optString("status");
                    if (state.equals(CommunalInterfaces._STATE)) {
                        JSONArray dataArray = jsonObject.optJSONArray("data");

                        dataArrayList.clear();
                        for (int k =0;k<dataArray.length();k++){
                            JSONObject classDataObject = dataArray.optJSONObject(k);
                            ClassList.ClassListData  classListData = new ClassList.ClassListData();
                            classListData.setClassid(classDataObject.optString("classid"));
                            classListData.setClassname(classDataObject.optString("classname"));
                            JSONArray studentArray = classDataObject.optJSONArray("studentlist");
                            ArrayList<ClassList.ClassStudentData> classStudentDataArrayList = new ArrayList<>();
                            for (int i = 0; i < studentArray.length(); i++) {
                                JSONObject itemObject = studentArray.optJSONObject(i);

                                ClassList.ClassStudentData stuListData = new ClassList.ClassStudentData();
                                stuListData.setName(itemObject.optString("name"));
                                stuListData.setPhone(itemObject.optString("phone"));
                                stuListData.setId(itemObject.optString("id"));
                                stuListData.setSex(itemObject.optString("sex"));
                                stuListData.setPhoto(itemObject.optString("photo"));
                                JSONArray commentArray = itemObject.optJSONArray("comments");

                                if (commentArray!=null){
                                    ArrayList<ClassList.ClassStudentData.TeacherComment> commentArrayList = new ArrayList<>();
                                    for (int j=0;j<commentArray.length();j++){
                                        JSONObject commentObject = commentArray.optJSONObject(commentArray.length()-j-1);
                                        ClassList.ClassStudentData.TeacherComment commentData = new ClassList.ClassStudentData.TeacherComment();
                                        commentData.setId(commentObject.optString("studentid"));
                                        commentData.setComment_time(commentObject.optString("comment_time"));
                                        commentData.setComment_status(commentObject.optString("comment_status"));
                                        commentData.setLearn(commentObject.optString("learn"));
                                        commentData.setWork(commentObject.optString("work"));
                                        commentData.setSing(commentObject.optString("sing"));
                                        commentData.setLabour(commentObject.optString("labour"));
                                        commentData.setStrain(commentObject.optString("strain"));
                                        commentData.setComment_content(commentObject.optString("comment_content"));
                                        commentData.setName(commentObject.optString("name"));
                                        commentData.setPhoto(commentObject.optString("photo"));
                                        commentArrayList.add(commentData);
                                    }

                                    stuListData.setTeacherComments(commentArrayList);
                                }
                                classStudentDataArrayList.add(stuListData);
                            }
                            Log.e("dataArrayList",jsonObject.optJSONArray("data").toString());
                            classListData.setStudentlist(classStudentDataArrayList);
                            dataArrayList.add(classListData);
                            tv_class_name.setText(dataArrayList.get(0).getClassname());
                        }
                        handler.sendEmptyMessage(101);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                ProgressViewUtil.dismiss();
            }
        });
        mQueue.add(request);
    }


    private void initView() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        ll_down = (LinearLayout) findViewById(R.id.down);
        ll_down.setOnClickListener(this);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        rl_review = (RelativeLayout) findViewById(R.id.rl_review);
        if(type==2){
            rl_review.setOnClickListener(this);
        }else{
            rl_review.setVisibility(View.GONE);
        }
        anchor = findViewById(R.id.anchor);
        //获取今天的年月
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        year_month = (TextView) findViewById(R.id.year_month);
        year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
        Log.d("年-月", String.valueOf(year) + String.valueOf(month));
        last_month = (RelativeLayout) findViewById(R.id.rl_last);
        last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month - 1;
                if (month==0){
                    month = 12;
                    year = year-1;
                }
                volleyRefrsh();
                year_month.setText(String.valueOf(year)+"年"+String.valueOf(month)+"月");
            }
        });
        next_month = (RelativeLayout) findViewById(R.id.rl_next);
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month + 1;
                if (month == 13) {
                    month = 1;
                    year = year + 1;
                }
                volleyRefrsh();
                year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
            }
        });
        year_month = (TextView) findViewById(R.id.year_month);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.down:
                showPopupWindow();
                break;
            case R.id.rl_review:
                Intent intent=new Intent(mContext,SpaceClickTeacherRemarkONActivity.class);
                startActivity(intent);
                break;
        }

    }
    //显示下拉列表
    private void showPopupWindow() {
        /**
         *显示选择菜单
         * */
        View layout = LayoutInflater.from(mContext).inflate(R.layout.select_class_menu, null);
        ListView list = (ListView) layout.findViewById(R.id.select_class_list);
        SelectClassAdapter adapter = new SelectClassAdapter(mContext,dataArrayList);
        list.setAdapter(adapter);
        //初始化popwindow
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        final PopupWindow popupWindow = new PopupWindow(layout, width, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置弹出位置
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        popupWindow.showAsDropDown(anchor);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = SpaceClickTeacherReviewActivity.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        SpaceClickTeacherReviewActivity.this.getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                SpaceClickTeacherReviewActivity.this.getWindow().setAttributes(lp);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_class_name.setText(dataArrayList.get(position).getClassname());
                teacherReviewAdat = new TeacherReviewAdat(mContext,dataArrayList.get(position).getStudentlist());
                listView.setAdapter(teacherReviewAdat);
                index = position;
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 获取当月第一天的时间戳
     * */
    public static long getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println(calendar.getTimeInMillis());

        return calendar.getTimeInMillis();
    }

}

