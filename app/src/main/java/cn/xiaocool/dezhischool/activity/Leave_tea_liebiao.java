package cn.xiaocool.dezhischool.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.PriorityQueue;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.TeacherListAdapter;
import cn.xiaocool.dezhischool.bean.Teacher;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ProgressUtil;

/**
 * Created by mac on 2017/6/7.
 */

public class Leave_tea_liebiao extends BaseActivity {
    private Context context;
    private RelativeLayout up_jiantou;
    private ArrayList<Teacher> teachers;
    private ListView listView;
    private String childId;
    private ImageView mNodateImageView;
    private RelativeLayout mNoDateRelative;
    private RelativeLayout mContentRelative;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.MESSAGEADDRESS:
                    JSONObject object = (JSONObject) msg.obj;
                    if(object.optString("status").equals("success")) {
                        ProgressUtil.dissmisLoadingDialog();
                        JSONObject data = object.optJSONObject("data");
                        JSONArray teacherArray = data.optJSONArray("teacherinfo");
                        teachers.clear();
//                        for(int i = 0;i < array.length();i++){
//                            JSONObject object1 = array.optJSONObject(i);
//                            JSONArray teacherArray = object1.optJSONArray("teacherinfo");
                        if(teacherArray.length()>0){
                            for(int j=0;j<teacherArray.length();j++){
                                JSONObject teacherObject = teacherArray.optJSONObject(j);
                                Teacher teacher = new Teacher();
                                teacher.setClassid(data.optString("classid"));
                                teacher.setClassName(data.optString("classname"));
                                teacher.setId(teacherObject.optString("id"));
                                teacher.setName(teacherObject.optString("name"));
                                teacher.setPhoto(teacherObject.optString("photo"));
                                teachers.add(teacher);
                            }
                            TeacherListAdapter adapter = new TeacherListAdapter(context,teachers);
                            listView.setAdapter(adapter);
                        }else{

                        }

                    } else if(object.optString("status").equals("error")) {
                        ProgressUtil.dissmisLoadingDialog();
                        listView.setVisibility(View.GONE);
                        mNoDateRelative.setVisibility(View.VISIBLE);



                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_tea_liebiao);
        context = this;
        childId = getIntent().getStringExtra("childId");
        hideTopView();
        initView();

    }

    private void initView() {
        teachers = new ArrayList<>();
        mNoDateRelative = (RelativeLayout) findViewById(R.id.rl_tea_liebiao_nodata);
        mNoDateRelative.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.list);
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = teachers.get(position).getName();
                String teacherid = teachers.get(position).getId();
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("teacherid", teacherid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
//        teacher_ex_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                String name = teachersAddresses.get(groupPosition).get(childPosition).get("name");
//                String teacherid = teachersAddresses.get(groupPosition).get(childPosition).get("id");
//                Intent intent = new Intent();
//                intent.putExtra("name", name);
//                intent.putExtra("teacherid", teacherid);
//                setResult(RESULT_OK, intent);
//                finish();
//                return false;
//            }
//        });
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void News() {
        ProgressUtil.showLoadingDialog(this);
        new SendRequest(context, handler).addressParentNew(childId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        News();
    }

    @Override
    public void requsetData() {

    }
}