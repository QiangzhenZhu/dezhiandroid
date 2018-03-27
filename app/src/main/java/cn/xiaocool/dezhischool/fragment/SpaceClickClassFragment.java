package cn.xiaocool.dezhischool.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.SpaceClickClassActivity;
import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.ClassBean;
import cn.xiaocool.dezhischool.bean.MonthAttendance;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.DataFactory;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.ToastUtils;


public class SpaceClickClassFragment extends Fragment implements SpaceClickClassActivity.UpdataListener {


    String weekdate;
    @BindView(R.id.class_lv)
    ListView classLv;
    Unbinder unbinder;
    Context mContext;
    SubjectAdapter subjectAdapter;
    List<ClassBean> lists;
    int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_space_click_class, container, false);
        unbinder = ButterKnife.bind(this, view);
        weekdate = getArguments().getString("weekdate");
        lists = new ArrayList<>();
        mContext = getActivity();
        checkIdentity();
        if (type == 1) {
            requsetList( SPUtils.get(mContext, LocalConstant.USER_CLASSID, "")+"");
        } else {
            requsetList(MyApplication.classid);
        }

        subjectAdapter = new SubjectAdapter(mContext, lists);
        classLv.setAdapter(subjectAdapter);

        return view;

    }

    private void checkIdentity() {
        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
        } else {
            /*if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("y"))
                type = 4;
            if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("n"))
                type = 2;
            if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("n")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("y"))
                type = 3;*/
            type = 2;
        }
    }

    public static SpaceClickClassFragment newInstance(String weekdate) {

        SpaceClickClassFragment fragment = new SpaceClickClassFragment();
        Bundle bundle = new Bundle();
        bundle.putString("weekdate", weekdate);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void requsetList(String classid) {

        ProgressUtil.showLoadingDialog((Activity) mContext);
        if ((classid != null) && (!classid.isEmpty())) {
            ProgressUtil.dissmisLoadingDialog();
            String Url = NetConstantUrl.NETBASEURL + "index.php?g=apps&m=school&a=GetClassSyllabus" + "&classid=" + classid;
            Log.d("NETBASEURL", Url);
            VolleyUtil.VolleyGetRequest(mContext, Url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    ProgressUtil.dissmisLoadingDialog();
                    Log.d("onResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(result);
                        String state = jsonObject.optString("status");
                        if (state.equals("success")) {
                            String attendancedata = jsonObject.optString("data");
                            JSONObject mons = new JSONObject(attendancedata);
                            JSONArray mon = mons.optJSONArray("mon");
                            JSONArray tu = mons.getJSONArray("tu");
                            JSONArray we = mons.getJSONArray("we");
                            JSONArray th = mons.getJSONArray("th");
                            JSONArray fri = mons.getJSONArray("fri");
                            JSONArray sat = mons.getJSONArray("sat");
                            JSONArray sun = mons.getJSONArray("sun");
                            Log.d("attentdata", mon.length() + "");
                            List<ClassBean> list = new ArrayList<ClassBean>();
                            ClassBean classBean;
                            switch (weekdate) {
                                case "1":

                                    for (int i = 0; i < mon.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = mon.optJSONObject(i);
                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    Log.d("attentdatalist", list.size() + "");
//                                    list = new Gson().fromJson(mon,
//                                            new TypeToken<List<ClassBean>>() {
//                                            }.getType());
//                                    list = DataFactory.jsonToArrayList(mon, bean);
                                    break;
                                case "2":
                                    for (int i = 0; i < tu.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = tu.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    break;
                                case "3":
                                    for (int i = 0; i < we.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = we.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    break;
                                case "4":
                                    for (int i = 0; i < th.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = th.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    break;
                                case "5":
                                    for (int i = 0; i < fri.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = fri.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    break;
                                case "6":

                                    for (int i = 0; i < sat.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = sat.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    break;
                                case "7":
                                    for (int i = 0; i < sun.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = sun.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                                    break;

                                default:
                                    for (int i = 0; i < mon.length(); i++) {
                                        classBean = new ClassBean();
                                        JSONObject jsonObjects = mon.optJSONObject(i);

                                        classBean.setSubject(jsonObjects.optString("subject"));
                                        classBean.setNo(jsonObjects.optString("no"));
                                        classBean.setEndtime(jsonObjects.optString("endtime"));
                                        classBean.setBegintime(jsonObjects.optString("begintime"));
                                        list.add(classBean);
                                    }
                            }

                            lists.clear();
                            lists.addAll(list);
                            Log.d("attentdataall", lists.size() + "");
                            subjectAdapter.notifyDataSetChanged();
                        } else {
                            ProgressUtil.dissmisLoadingDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtil.dissmisLoadingDialog();
                    }

                }

                @Override
                public void onError() {
                    ProgressUtil.dissmisLoadingDialog();
                    ToastUtils.ToastShort(mContext, "失败");
                }
            });
        } else

        {
            ProgressUtil.dissmisLoadingDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onUpdata(String classId) {
        requsetList(classId);
    }


    static class SubjectAdapter extends BaseAdapter {

        private Context context;
        private List<ClassBean> list;

        public SubjectAdapter(Context context, List<ClassBean> list) {
            this.list = list;
            this.context = context;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
            ViewHolder holder;
            if (convertView == null) {
                convertView = view;
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //+"("++")"
            ClassBean info = list.get(position);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String begintime;
            String endtime;

            holder.day.setText("第" + (position + 1) + "节课" + "(" + info.getBegintime() + "~" + info.getEndtime() + ")");

            if (list.get(position).getSubject().equals("") || list.get(position).getSubject() == null) {
                holder.subject.setText("( ~ )");
            } else {
                holder.subject.setText(list.get(position).getSubject());
            }
//            holder.day.setText(list.get(0).getMon().get(position).get_$1().toString());
//            if (redEnvelopeState.equals("1")){
//                holder.itemIsUse.setVisibility(View.GONE);
//            }else if (redEnvelopeState.equals("2")){
//                holder.itemIsUse.setVisibility(View.VISIBLE);
//            }
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.day)
            TextView day;
            @BindView(R.id.subject)
            TextView subject;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && getView() != null) {

            requsetList(MyApplication.classid);
        }
    }

}
