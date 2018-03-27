package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.ChooseStudentExpandbleListViewAdapter;
import cn.xiaocool.dezhischool.adapter.EListAdapter;
import cn.xiaocool.dezhischool.bean.AbsentDormitoryInfo;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.ClassList;
import cn.xiaocool.dezhischool.bean.Group;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.ToastUtils;

/**
 * Created by mac on 2017/5/26.
 */

public class MyClassListActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Group> groups;
    private ExpandableListView listView;
    private EListAdapter adapter;
    private ImageView btn_exit;
    private ExpandableListView class_list;
    private String classID, className, type;
    private ChooseStudentExpandbleListViewAdapter classListAdapter;
    private List<ClassList.ClassListData> classListDataList;
    private List<List<ClassList.ClassStudentData>> studentDataList;
    private List<String> classesID;
    private CheckBox quan_check;
    private int size;
    private String[] allMembers;
    private TextView down_selected_num;
    private RelativeLayout up_jiantou, btn_finish;
    private ArrayList<Child> selectedUsers;
    private ArrayList<String> selectedIds, selectedNames;
    private UserInfo user = new UserInfo();
    private Context mContext;
    private String isMeeting;
    private String keyWord = "";
    private EditText mSearchEdit;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.CLASS_STU:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            groups = new ArrayList<Group>();
                            JSONArray dataArray = obj.optJSONArray("data");
                            JSONObject itemObject;
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Group group = new Group(itemObject.optString("classid"), itemObject.optString("classname"));
                                JSONArray stuArray = itemObject.optJSONArray("studentlist");
                                for (int j = 0; j < stuArray.length(); j++) {
                                    JSONObject object = stuArray.optJSONObject(j);
                                    Child child = new Child(object.optString("id"), object.optString("name"),
                                            itemObject.optString("name"));
                                    group.addChildrenItem(child);
                                }
                                groups.add(group);
                            }
                            adapter = new EListAdapter(MyClassListActivity.this, groups, quan_check, down_selected_num);
                            listView.setAdapter(adapter);
                            listView.setOnChildClickListener(adapter);
                        }
                    }
                    break;
                case CommunalInterfaces.TEACHER:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            groups = new ArrayList<Group>();
                            JSONArray dataArray = obj.optJSONArray("data");
                            JSONObject itemObject;
                            /*Group group = new Group("teacher", "老师");
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Child child = new Child(itemObject.optString("id"), itemObject.optString("name"),
                                        itemObject.optString("name"));
                                group.addChildrenItem(child);

                            }
                            groups.add(group);*/
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Group group = new Group(itemObject.optString("classid"), itemObject.optString("classname"));
                                JSONArray stuArray = itemObject.optJSONArray("teacherlist");
                                for (int j = 0; j < stuArray.length(); j++) {
                                    JSONObject object = stuArray.optJSONObject(j);
                                    Child child = new Child(object.optString("id"), object.optString("name"),
                                            itemObject.optString("name"));
                                    group.addChildrenItem(child);
                                }
                                groups.add(group);
                            }
                            adapter = new EListAdapter(MyClassListActivity.this, groups, quan_check, down_selected_num);
                            listView.setAdapter(adapter);
                            listView.setOnChildClickListener(adapter);
                        }
                    }


                    break;
                case CommunalInterfaces.MEETING_TEACHER:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            groups = new ArrayList<Group>();
                            JSONArray dataArray = obj.optJSONArray("data");
                            JSONObject itemObject;
                            /*Group group = new Group("teacher", "老师");
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Child child = new Child(itemObject.optString("id"), itemObject.optString("name"),
                                        itemObject.optString("name"));
                                group.addChildrenItem(child);

                            }
                            groups.add(group);*/
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Group group = new Group(itemObject.optString("classid"), itemObject.optString("name"));
                                JSONArray stuArray = itemObject.optJSONArray("teacherlist");
                                for (int j = 0; j < stuArray.length(); j++) {
                                    JSONObject object = stuArray.optJSONObject(j);
                                    Child child = new Child(object.optString("id"), object.optString("name"),
                                            itemObject.optString("name"));
                                    group.addChildrenItem(child);
                                }
                                groups.add(group);
                            }
                            adapter = new EListAdapter(MyClassListActivity.this, groups, quan_check, down_selected_num);
                            listView.setAdapter(adapter);
                            listView.setOnChildClickListener(adapter);
                        }
                    }



                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        mContext=this;
        user.readData(mContext);
        hideTopView();
        initView();
    }

    @Override
    public void requsetData() {

        if (type.equals("student")) {
            new SendRequest(MyClassListActivity.this, handler).myclass_stu(SPUtils.get(mContext, LocalConstant.USER_ID, "")+"");
        }else if (type.equals("meeting")){
            new SendRequest(MyClassListActivity.this, handler).getMeetingTeacher(mContext,SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "")+"",keyWord);
        } else {
            new SendRequest(MyClassListActivity.this, handler).getTeacher(SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "")+"");
        }
    }

    private void initView() {
        mSearchEdit = findViewById(R.id.metting_people_search);
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
                    keyWord= mSearchEdit.getText().toString().trim();
                    requsetData();

                    return true;
                }
                return false;
            }
        });
        classesID = new ArrayList<>();
        studentDataList = new ArrayList<>();
        classListDataList = new ArrayList<>();
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        btn_finish = (RelativeLayout) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);
        quan_check = (CheckBox) findViewById(R.id.quan_check);
        quan_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });
        listView = (ExpandableListView) findViewById(R.id.listView);
        listView.setGroupIndicator(null);
        down_selected_num = (TextView) findViewById(R.id.down_selected_num);
        type = getIntent().getStringExtra("type");
        isMeeting = getIntent().getStringExtra("meeting");



        if (type.equals("student")) {
            new SendRequest(MyClassListActivity.this, handler).myclass_stu(SPUtils.get(mContext, LocalConstant.USER_ID, "")+"");
        }else if (type.equals("meeting")){
            new SendRequest(MyClassListActivity.this, handler).getMeetingTeacher(mContext,SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "")+"",keyWord);
        } else {
            new SendRequest(MyClassListActivity.this, handler).getTeacher(SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "")+"");
        }
       /*if ("meeting".equals(isMeeting)){
            new SendRequest(MyClassListActivity.this,handler).meeting_teacher(MyClassListActivity.this);
       }
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.btn_finish:
                getAllMenbers();
                if (selectedIds.size() > 0) {

                    Intent intent = new Intent();
                    intent.putExtra("sss", "ssssss");
                    intent.putStringArrayListExtra("ids", selectedIds);
                    intent.putStringArrayListExtra("names", selectedNames);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                    ToastUtils.ToastShort(this, "请选择接收人！");
                }

                break;
        }
    }

    private void checkAll() {
        if (quan_check.isChecked()) {
            size = 0;
            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).setChecked(true);
                size += groups.get(i).getChildrenCount();
                for (int j = 0; j < groups.get(i).getChildrenCount(); j++) {
                    groups.get(i).getChildItem(j).setChecked(true);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setOnChildClickListener(adapter);
            for (int i = 0; i < groups.size(); i++) {
                listView.expandGroup(i);
            }
            down_selected_num.setText("已选择" + size + "人");

        } else {
            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).setChecked(false);
                for (int j = 0; j < groups.get(i).getChildrenCount(); j++) {
                    groups.get(i).getChildItem(j).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setOnChildClickListener(adapter);
            for (int i = 0; i < groups.size(); i++) {
                listView.expandGroup(i);
            }
            down_selected_num.setText("已选择0人");
        }
    }

    /**
     * 获取群组初始人员
     */
    private void getAllMenbers() {
        ArrayList<String> newG = new ArrayList<>();
        selectedUsers = new ArrayList<>();
        selectedIds = new ArrayList<>();
        selectedNames = new ArrayList<>();
        for (int i = 0; i < adapter.getterGroups().size(); i++) {
            for (int j = 0; j < adapter.getterGroups().get(i).getChildrenCount(); j++) {
                if (adapter.getterGroups().get(i).getChildItem(j).getChecked()) {

                    Child child = new Child(adapter.getterGroups().get(i).getChildItem(j).getUserid(), adapter.getterGroups().get(i).getChildItem(j).getFullname(), adapter.getterGroups().get(i).getChildItem(j).getUsername());
                    selectedUsers.add(child);
                    selectedIds.add(adapter.getterGroups().get(i).getChildItem(j).getUserid());
                    selectedNames.add(adapter.getterGroups().get(i).getChildItem(j).getFullname());
                } else {
                    Log.e("checked", String.valueOf(adapter.getterGroups().get(i).getChildItem(j).getChecked()));

                }

            }
        }

    }



}

