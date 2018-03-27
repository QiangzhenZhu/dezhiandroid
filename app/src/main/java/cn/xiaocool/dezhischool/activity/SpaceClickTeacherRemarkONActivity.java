package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.TextAdapter;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtils;
import cn.xiaocool.dezhischool.view.NoScrollListView;

/**
 * Created by mac on 2017/5/31.
 */

public class SpaceClickTeacherRemarkONActivity extends BaseActivity implements View.OnClickListener {


    private UserInfo user = new UserInfo();
    private ImageView btn_exit;
    private TextView tv_choose_child,tv_continue,tv_complete,tv_comment1,btn_recommend_comment;
    private EditText et_comment;
    private LinearLayout ll_recommend;
    private Context mContext;
    private ArrayList<String> datas;
    private NoScrollListView tuijain_list;
    private String studentId;
    private RequestQueue mQueue;
    private LinearLayout xuexi_1,xuexi_2,xuexi_3,dongshou_1,dongshou_2,dongshou_3,sing_1,sing_2,sing_3,
            laodong_1,laodong_2,laodong_3,yingbian_1,yingbian_2,yingbian_3;

    private LinearLayout xuexiTabs[],dongshou[],sing[],laodong[],yiban[];


    private int index1=0;
    private int currentTabIndex1=0;
    private int index2=0;
    private int currentTabIndex2=0;
    private int index3=0;
    private int currentTabIndex3=0;
    private int index4=0;
    private int currentTabIndex4=0;
    private int index5=0;
    private int currentTabIndex5=0;
    private static final int ADD_KEY = 4;
    private int type;
    private RelativeLayout rl_choose;

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_KEY:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            ToastUtils.ToastShort(SpaceClickTeacherRemarkONActivity.this,"点评成功");
                            finish();
                        } else {
                            ToastUtils.ToastShort(SpaceClickTeacherRemarkONActivity.this,"点评失败");

//                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_teacher_comments);
        //type 标识是否 定向评价 即是否是在TeacherReviewDetailActivivty启动的
        type = getIntent().getIntExtra("type",2);
        mContext = this;
        user.readData(this);
        mQueue = Volley.newRequestQueue(this);
        initView();
        hideTopView();
        getData();

    }

    @Override
    public void requsetData() {

    }

    /**
     *
     * 获取推荐点评记录
     * */
    private void getData() {

//            &learn=1&work=1&sing=1&labour=1&strain=1<>
        String URL = "http://dezhi.xiaocool.net/index.php?g=apps&m=index&a=getDictionaryList&type=1";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                Log.d("onResponse", arg0);

                try {
                    JSONObject jsonObject = new JSONObject(arg0);
                    String state = jsonObject.optString("status");
                    if (state.equals(CommunalInterfaces._STATE)) {
                        datas = new ArrayList<>();
                        JSONArray dataArray = jsonObject.optJSONArray("data");
                        for (int i=0;i<dataArray.length();i++){

                            datas.add(dataArray.optJSONObject(i).optString("name"));

                        }

                        tuijain_list.setAdapter(new TextAdapter(SpaceClickTeacherRemarkONActivity.this,datas));
                        tuijain_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                et_comment.setText(datas.get(position));
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.d("onErrorResponse", arg0.toString());
            }
        });
        mQueue.add(request);

    }

    private void initView() {

        btn_recommend_comment = (TextView) findViewById(R.id.btn_recommend_comment);
        btn_recommend_comment.setOnClickListener(this);
        et_comment = (EditText) findViewById(R.id.et_comment);
        ll_recommend = (LinearLayout) findViewById(R.id.recommend_comment);
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        tv_choose_child = (TextView) findViewById(R.id.choose_class);
        tv_choose_child.setOnClickListener(this);
        tv_complete = (TextView) findViewById(R.id.complete_comment);
        tv_complete.setOnClickListener(this);
        tv_continue = (TextView) findViewById(R.id.continue_comment);
        tv_continue.setOnClickListener(this);
        tuijain_list = (NoScrollListView) findViewById(R.id.tuijain_list);




        xuexi_1 = (LinearLayout) findViewById(R.id.xuexi_1);
        xuexi_2 = (LinearLayout) findViewById(R.id.xuexi_2);
        xuexi_3= (LinearLayout) findViewById(R.id.xuexi_3);
        xuexi_1.setOnClickListener(this);
        xuexi_2.setOnClickListener(this);
        xuexi_3.setOnClickListener(this);
        xuexiTabs = new LinearLayout[]{xuexi_1,xuexi_2,xuexi_3};

        dongshou_1 = (LinearLayout) findViewById(R.id.dongshou_1);
        dongshou_2 = (LinearLayout) findViewById(R.id.dongshou_2);
        dongshou_3 = (LinearLayout) findViewById(R.id.dongshou_3);
        dongshou_1.setOnClickListener(this);
        dongshou_2.setOnClickListener(this);
        dongshou_3.setOnClickListener(this);
        dongshou = new LinearLayout[]{dongshou_1,dongshou_2,dongshou_3};

        sing_1 = (LinearLayout) findViewById(R.id.sing_1);
        sing_2 = (LinearLayout) findViewById(R.id.sing_2);
        sing_3 = (LinearLayout) findViewById(R.id.sing_3);
        sing_1.setOnClickListener(this);
        sing_2.setOnClickListener(this);
        sing_3.setOnClickListener(this);
        sing = new LinearLayout[]{sing_1,sing_2,sing_3};

        laodong_1 = (LinearLayout) findViewById(R.id.laodong_1);
        laodong_2 = (LinearLayout) findViewById(R.id.laodong_2);
        laodong_3 = (LinearLayout) findViewById(R.id.laodong_3);
        laodong_1.setOnClickListener(this);
        laodong_2.setOnClickListener(this);
        laodong_3.setOnClickListener(this);
        laodong = new LinearLayout[]{laodong_1,laodong_2,laodong_3};

        yingbian_1 = (LinearLayout) findViewById(R.id.yingbian_1);
        yingbian_2 = (LinearLayout) findViewById(R.id.yingbian_2);
        yingbian_3 = (LinearLayout) findViewById(R.id.yingbian_3);
        yingbian_1.setOnClickListener(this);
        yingbian_2.setOnClickListener(this);
        yingbian_3.setOnClickListener(this);
        yiban = new LinearLayout[]{yingbian_1,yingbian_2,yingbian_3};


        xuexiTabs[index1].setSelected(true);
        dongshou[index2].setSelected(true);
        sing[index3].setSelected(true);
        laodong[index4].setSelected(true);
        yiban[index5].setSelected(true);
        if (type == 1){
            tv_continue.setVisibility(View.GONE);
            rl_choose = (RelativeLayout) findViewById(R.id.rl_choose);
            rl_choose.setVisibility(View.GONE);
            studentId = getIntent().getStringExtra("studentid");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.choose_class:
                Intent intent = new Intent(SpaceClickTeacherRemarkONActivity.this, MyClassListActivity.class);
                intent.putExtra("type", "student");
                startActivityForResult(intent, 101);
                break;
            case R.id.complete_comment:
                sendRemark();
                break;
            case R.id.continue_comment:     //继续点评
                refresh();
                break;
            case R.id.btn_recommend_comment:
                ll_recommend.setVisibility(View.VISIBLE);
                break;


            case R.id.xuexi_1:
                index1 = 0;
                break;
            case R.id.xuexi_2:
                index1 = 1;
                break;
            case R.id.xuexi_3:
                index1 = 2;
                break;

            case R.id.dongshou_1:
                index2 = 0;
                break;
            case R.id.dongshou_2:
                index2 = 1;
                break;
            case R.id.dongshou_3:
                index2 = 2;
                break;

            case R.id.sing_1:
                index3 = 0;
                break;
            case R.id.sing_2:
                index3 = 1;
                break;
            case R.id.sing_3:
                index3 = 2;
                break;

            case R.id.laodong_1:
                index4 = 0;
                break;
            case R.id.laodong_2:
                index4 = 1;
                break;
            case R.id.laodong_3:
                index4 = 2;
                break;

            case R.id.yingbian_1:
                index5 = 0;
                break;
            case R.id.yingbian_2:
                index5 = 1;
                break;
            case R.id.yingbian_3:
                index5 = 2;
                break;
        }


        xuexiTabs[currentTabIndex1].setSelected(false);
        xuexiTabs[index1].setSelected(true);
        currentTabIndex1 = index1;

        dongshou[currentTabIndex2].setSelected(false);
        dongshou[index2].setSelected(true);
        currentTabIndex2 = index2;

        sing[currentTabIndex3].setSelected(false);
        sing[index3].setSelected(true);
        currentTabIndex3 = index3;

        laodong[currentTabIndex4].setSelected(false);
        laodong[index4].setSelected(true);
        currentTabIndex4 = index4;

        yiban[currentTabIndex5].setSelected(false);
        yiban[index5].setSelected(true);
        currentTabIndex5 = index5;
    }

    /**
     *
     * 发送点评
     * */
    private void sendRemark() {
        if (et_comment.getText().length()>0){
            if (studentId!=null) {

                new SendRequest(this, handler).send_teacherRemark(SPUtils.get(mContext, LocalConstant.USER_ID, "")+"",studentId, et_comment.getText().toString(), (currentTabIndex1 + 1) + "", (currentTabIndex2 + 1) + "", (currentTabIndex3 + 1) + "",
                        (currentTabIndex4 + 1) + "", (currentTabIndex5 + 1) + "", ADD_KEY);
            }else {
                ToastUtils.ToastShort(SpaceClickTeacherRemarkONActivity.this,"请选择点评的孩子！");
            }

        }else {
            ToastUtils.ToastShort(SpaceClickTeacherRemarkONActivity.this,"评价内容不能为空！");
        }

    }

    //继续点评清空数据
    private void refresh() {
        tv_choose_child.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                        for (int i = 0; i < ids.size(); i++){
                            studentId = studentId+","+ids.get(i);
                        }

                        studentId = studentId.substring(5,studentId.length());

                        tv_choose_child.setText(haschoose);
                    }

                    break;
            }
        }
    }



}

