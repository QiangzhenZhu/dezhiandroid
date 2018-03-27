package cn.xiaocool.dezhischool.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.AddressListAdapter;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.ClassTeacher;
import cn.xiaocool.dezhischool.bean.ClassTeacherParent;
import cn.xiaocool.dezhischool.bean.Group;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherFragment extends Fragment {

    @BindView(R.id.fragment_teacher_elv_teacher)
    ExpandableListView fragmentTeacherElvTeacher;
    private Context context;
    private List<ClassTeacher> classTeachers;
    private List<ClassTeacherParent> classTeacherParents;
    private AddressListAdapter adapter;
    private ArrayList<Group> groups;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, null);
        ButterKnife.bind(this, view);
        classTeachers = new ArrayList<>();
        classTeacherParents = new ArrayList<>();
        groups = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        Log.d("test1", "onResume teacher: ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

        }

    }

    /**
     * 加载数据
     */
    private void getData() {
        ProgressUtil.showLoadingDialog(getActivity());
        if (SPUtils.get(context,LocalConstant.USER_TYPE,"").equals("0")){
            String url = NetConstantUrl.GET_MTCLASS_TEACHER + SPUtils.get(context, LocalConstant.USER_CLASSID,"1");
            VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    if (JsonResult.JSONparser(context, result)) {
                        classTeacherParents.clear();
                        classTeacherParents.addAll(getBeanFromJsonParent(result));
                        setParentAdapter();
                    }
                }

                @Override
                public void onError() {
                    ProgressUtil.dissmisLoadingDialog();
                }
            });
        }else {
            String url = NetConstantUrl.GET_CLASS_TEACHER + SPUtils.get(context, LocalConstant.SCHOOL_ID,"1");
            VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    if (JsonResult.JSONparser(context, result)) {
                        classTeachers.clear();
                        classTeachers.addAll(getBeanFromJson(result));
                        setAdapter();
                    }
                }

                @Override
                public void onError() {
                    ProgressUtil.dissmisLoadingDialog();
                }
            });
        }

    }

    /**
     * 设置家长获取适配器
     */
    private void setParentAdapter() {

        changeParentModelForElistmodel();
        adapter = new AddressListAdapter(context, groups,0);
        fragmentTeacherElvTeacher.setAdapter(adapter);
        fragmentTeacherElvTeacher.setGroupIndicator(null);
    }
    /**
     * 设置适配器
     */
    private void setAdapter() {

        changeModelForElistmodel();
        adapter = new AddressListAdapter(context, groups,0);
        fragmentTeacherElvTeacher.setAdapter(adapter);
        fragmentTeacherElvTeacher.setGroupIndicator(null);
    }

    /**
     * 转换模型
     */
    private void changeModelForElistmodel() {
        groups.clear();
        for (int i = 0; i < classTeachers.size(); i++) {
            Group group = new Group(classTeachers.get(i).getClassid(), classTeachers.get(i).getClassname());
            for (int j = 0; j < classTeachers.get(i).getTeacherlist().size(); j++) {
                Child child = new Child(classTeachers.get(i).getTeacherlist().get(j).getId(), classTeachers.get(i).getTeacherlist().get(j).getName(),
                        classTeachers.get(i).getTeacherlist().get(j).getName());
                child.setPhone(classTeachers.get(i).getTeacherlist().get(j).getPhone());
                group.addChildrenItem(child);
            }
            groups.add(group);
        }

    }

    /**
     * 转换家长模型模型
     */
    private void changeParentModelForElistmodel() {
        groups.clear();
        Group group = new Group(String.valueOf(SPUtils.get(context, LocalConstant.USER_CLASSID, "")),String.valueOf(SPUtils.get(context, LocalConstant.CLASS_NAME, "")));
        for (int j = 0; j < classTeacherParents.size(); j++) {
            Child child = new Child(classTeacherParents.get(j).getId(), classTeacherParents.get(j).getName(),
                    classTeacherParents.get(j).getName());
            child.setPhone(classTeacherParents.get(j).getPhone());
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
    private List<ClassTeacher> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ClassTeacher>>() {
        }.getType());
    }

    /**
     * 字符串转模型
     *
     * @param result
     * @return
     */
    private List<ClassTeacherParent> getBeanFromJsonParent(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ClassTeacherParent>>() {
        }.getType());
    }
}
