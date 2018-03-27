package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.EListAdapter;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.ClassParent;
import cn.xiaocool.dezhischool.bean.Group;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;

public class ChooseTrendClassActivity extends BaseActivity {

    @BindView(R.id.listView)
    ExpandableListView listView;
    @BindView(R.id.down_selected_num)
    TextView downSelectedNum;
    @BindView(R.id.quan_check)
    CheckBox quanCheck;
    private ArrayList<Group> groups;
    private ArrayList<Child> childs;
    private EListAdapter adapter;
    private List<ClassParent> classParents;
    private int size;
    private ArrayList<String> selectedIds, selectedNames;
    private Context context;
    private String hasData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);
        ButterKnife.bind(this);
        context = this;
        setTopName("选择班级");
        setRightText("完成").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasData.equals("error")){
                    ToastUtil.showShort(context, "暂无接收班级！");
                    return;
                }
                getAllMenbers();
                if (selectedIds.size() > 0) {

                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("ids", selectedIds);
                    intent.putStringArrayListExtra("names", selectedNames);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                    ToastUtil.showShort(context, "请选择接收班级！");
                }
            }
        });
        classParents = new ArrayList<>();
        groups = new ArrayList<>();
        childs = new ArrayList<>();
    }


    @Override
    public void requsetData() {
        String url = NetConstantUrl.GET_SCHOOL_CLASS + SPUtils.get(context, LocalConstant.SCHOOL_ID,"1");
        url = NetConstantUrl.GET_PARENT_BYTEACHERID + "&teacherid=" + SPUtils.get(context,LocalConstant.USER_ID,"")+"&schoolid="+SPUtils.get(context, LocalConstant.SCHOOL_ID, "");
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context,result)){
                    classParents.clear();
                    classParents.addAll(getBeanFromJson(result));
                    setAdapter();
                }else {

                }

            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {

        changeModelForElistmodel();
        if (adapter==null){
            adapter = new EListAdapter(context, groups, quanCheck, downSelectedNum ,"1");
            listView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }

        listView.setGroupIndicator(null);
    }

    /**
     * 转换模型
     */
    private void changeModelForElistmodel() {
        Group group = new Group("1", SPUtils.get(context,LocalConstant.SCHOOL_NAME,"").toString());
        for (int j = 0; j < classParents.size(); j++) {
            Child child = new Child(classParents.get(j).getClassid(), classParents.get(j).getClassname(),
                    classParents.get(j).getClassname());
            group.addChildrenItem(child);
        }
        groups.add(group);
    }


    /**
     * 字符串转模型
     *
     * @param result
     * @return
     */
    private List<ClassParent> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ClassParent>>() {
        }.getType());
    }

    @OnClick(R.id.quan_check)
    public void onClick() {
        if (hasData.equals("error")){
            ToastUtil.showShort(context, "暂无接收班级！");
            return;
        }
        if (quanCheck.isChecked()) {
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
            downSelectedNum.setText("已选择" + size + "个班级");

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
            downSelectedNum.setText("已选择0个班级");
        }
    }


    /**
     * 获取群组初始人员
     */
    private void getAllMenbers() {
        selectedIds = new ArrayList<>();
        selectedNames = new ArrayList<>();
        for (int i = 0; i < adapter.getterGroups().size(); i++) {
            for (int j = 0; j < adapter.getterGroups().get(i).getChildrenCount(); j++) {
                if (adapter.getterGroups().get(i).getChildItem(j).getChecked()) {
                    selectedIds.add(adapter.getterGroups().get(i).getChildItem(j).getUserid());
                    selectedNames.add(adapter.getterGroups().get(i).getChildItem(j).getFullname());
                } else {
                    Log.e("checked", String.valueOf(adapter.getterGroups().get(i).getChildItem(j).getChecked()));

                }

            }
        }

    }

}
