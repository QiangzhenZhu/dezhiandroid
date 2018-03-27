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
import cn.xiaocool.dezhischool.bean.ClassParent;
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
public class ParentFragment extends Fragment {

    @BindView(R.id.fragment_parent_elv_parent)
    ExpandableListView fragmentParentElvParent;
    private Context context;
    private int type;
    private List<ClassParent> classParents;
    private List<Group> groups;
    private AddressListAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        checkIdentity();
        classParents = new ArrayList<>();
        groups = new ArrayList<>();
    }

    /**
     * 判断身份
     * 1-----家长
     * 2-----校长
     * 3-----老师
     */
    private void checkIdentity() {
        if(SPUtils.get(context, LocalConstant.USER_TYPE,"").equals("0")){
            type = 1;
        }else if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")){
            type = 2;
        }else{
            type =3;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("test1", "onResume parent: ");
        getData();

    }

    /**
     * 获取数据
     */
    private void getData() {

        String url = "";
      /*  if(type == 2){
            url = NetConstantUrl.GET_PARENT_ALL + SPUtils.get(context,LocalConstant.SCHOOL_ID,"1");
        }else*/ if(type == 2||type == 3){
            url = NetConstantUrl.GET_PARENT_BYTEACHERID + "&teacherid=" + SPUtils.get(context,LocalConstant.USER_ID,"")+"&schoolid="+SPUtils.get(context, LocalConstant.SCHOOL_ID, "");;
        }else{
            url = NetConstantUrl.GET_PARENT_BYCLASSID + "&classid=" + SPUtils.get(context,LocalConstant.USER_CLASSID,"");
        }
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context,result)){
                    ProgressUtil.dissmisLoadingDialog();
                    classParents.clear();
                    classParents.addAll(getBeanFromJson(result));
                    setAdapter();
                }else {
                    ProgressUtil.dissmisLoadingDialog();
                }

            }

            @Override
            public void onError() {
                ProgressUtil.dissmisLoadingDialog();
            }
        });
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {

        changeModelForElistmodel();
        adapter = new AddressListAdapter(context, groups,1);
        fragmentParentElvParent.setAdapter(adapter);
        fragmentParentElvParent.setGroupIndicator(null);
    }

    /**
     * 转换模型
     */
    private void changeModelForElistmodel() {
        groups.clear();
        for (int i = 0; i < classParents.size(); i++) {
            Group group = new Group(classParents.get(i).getClassid(), classParents.get(i).getClassname());
            for (int j = 0; j < classParents.get(i).getStudent_list().size(); j++) {
                Child child = new Child(classParents.get(i).getStudent_list().get(j).getParent_list().get(0).getId()
                        , classParents.get(i).getStudent_list().get(j).getParent_list().get(0).getName()
                        , classParents.get(i).getStudent_list().get(j).getParent_list().get(0).getName());
                child.setChildName(classParents.get(i).getStudent_list().get(j).getName());
                child.setPhone(classParents.get(i).getStudent_list().get(j).getParent_list().get(0).getPhone());
                group.addChildrenItem(child);
            }
            groups.add(group);
        }

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
}
