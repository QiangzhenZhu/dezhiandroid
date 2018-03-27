package cn.xiaocool.dezhischool.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.ClassAttendance;
import cn.xiaocool.dezhischool.bean.ClassInfo;
import cn.xiaocool.dezhischool.bean.NewClassAttendance;
import cn.xiaocool.dezhischool.fragment.MapFragment;
import cn.xiaocool.dezhischool.fragment.SpaceClickClassFragment;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.Utility;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.NoScrollListView;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;

/**
 * Created by mac on 2017/7/7.
 */

public class MapActivity extends BaseActivity {
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp_view)
    ViewPager vpView;
    private Context mContext;
    private ArrayList<ClassAttendance> studentDataArrayList;
    private ArrayList<NewClassAttendance> AttentdataDataArrayList;
    private RequestQueue mQueue;
    private int years, month, day;
    private int endday_years, endday_month, endday_day;
    private int nowyear, nowmonth, nowday;
    private Calendar c;
    private TextView year_month, tv_class_attendance;
    private View anchor;
    private ListView gv_childrenList;
    private NoScrollListView buqian_list;
    private RelativeLayout last_month, next_month;
    private int allarrive = 0;
    private int notarrive = 0;
    private int leave = 0;
    private CommonAdapter adapter;
    private CommonAdapter itemAdapter;
    private int type;
    private List<String> mTitleList;//页卡标题集合
    private List<Fragment> fragments;
    MyPagerAdapter pageradapter;
    private RelativeLayout mRightText;
    private List<ClassInfo> classInfoList;
    private Set<String> locationList;

    @Override
    public void requsetData() {

    }

    //初始化
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_class_map);
        ButterKnife.bind(this);
        classInfoList = new ArrayList<>();
        studentDataArrayList = new ArrayList<>();
        AttentdataDataArrayList = new ArrayList<>();
        mContext = this;

        mQueue = Volley.newRequestQueue(mContext);
        checkIdentity();
        initView();
        setTopName("行为轨迹");
        getAttendList(MyApplication.begintime, MyApplication.endtime,"");
    }

    private void checkIdentity() {
        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
        } else {
            type = 2;
        }
    }
    //    /**
//     * 获取行为轨迹数据
//     */
    private void getAttendList(String begintime, String endtime, String location) {
        ProgressUtil.showLoadingDialog( mContext);
        AttentdataDataArrayList.clear();
        if (type == 1) {
            OkHttpUtils.get().url(NetConstantUrl.ParentGetFaceInfoWithLocation)
                    .addParams("userid", SPUtils.get(mContext, LocalConstant.USER_ID, "") + "")
                    .addParams("begintime", begintime)
                    .addParams("endtime", endtime)
                    .addParams("location", location)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ProgressUtil.dissmisLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ProgressUtil.dissmisLoadingDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String state = jsonObject.optString("status");
                                String attentdata = jsonObject.optString("data");
                                //com.yixia.camera.util.Log.d("attentdata", attentdata);
                                if (state.equals("success")) {

                                    AttentdataDataArrayList.clear();
                                    List<NewClassAttendance> list = new Gson().fromJson(attentdata,
                                            new TypeToken<List<NewClassAttendance>>() {
                                            }.getType());
                                    AttentdataDataArrayList.addAll(list);
                                    initlocation();
                                    inittab();

                                } else {
//                                    ToastUtils.ToastShort(mContext,attentdata);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } else {

            OkHttpUtils.get().url(NetConstantUrl.TeacherGetFaceInfoWithLocation)
                    .addParams("classid", SPUtils.get(mContext, LocalConstant.USER_CLASSID, "") + "")
                    .addParams("begintime", begintime)
                    .addParams("endtime", endtime)
                    .addParams("location", location)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ProgressUtil.dissmisLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ProgressUtil.dissmisLoadingDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String state = jsonObject.optString("status");
                                String attentdata = jsonObject.optString("data");
                                //com.yixia.camera.util.Log.d("attentdata", attentdata);
                                if (state.equals("success")) {

                                    List<NewClassAttendance> list = new Gson().fromJson(attentdata,
                                            new TypeToken<List<NewClassAttendance>>() {
                                            }.getType());
                                    AttentdataDataArrayList.addAll(list);
                                    initlocation();
                                    inittab();


                                } else {
                                    AttentdataDataArrayList.clear();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }

    }
    public void initlocation(){
        locationList = new HashSet<>();

            for (int j = 0; j <AttentdataDataArrayList.size() ; j++) {
                NewClassAttendance attendance = AttentdataDataArrayList.get(j);
                List<NewClassAttendance.attendance_info> info = attendance.getInfo();
                for (int k = 0; k <info.size() ; k++) {
                    NewClassAttendance.attendance_info attendance_info = info.get(k);
                    String location= attendance_info.getLocation();
                    locationList.add(location);

                }

            }

    }


    private void initView() {
        mRightText = (RelativeLayout) setRightText("切换");
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsHasClassOrBaby();
            }
        });
        tv_class_attendance = (TextView) findViewById(R.id.tv_class_attendance);
        anchor = findViewById(R.id.anchor);
        gv_childrenList = (ListView) findViewById(R.id.attance_list);
        buqian_list = (NoScrollListView) findViewById(R.id.buqian_list);
        //获取今天的年月
        c = Calendar.getInstance();
        nowyear = c.get(Calendar.YEAR);
        nowmonth = c.get(Calendar.MONTH) + 1;
        nowday = c.get(Calendar.DAY_OF_MONTH);
          years=nowyear;
         month=nowmonth;
         day=nowday;
        c.set(nowyear, nowmonth, nowday);
        c.set(years, month - 1, day, 0, 0, 0);
        MyApplication.begintime = String.valueOf(c.getTimeInMillis() / 1000);
        c.set(years, month - 1, day+1, 0, 0, 0);
        MyApplication.endtime = String.valueOf(c.getTimeInMillis() / 1000);
        year_month = (TextView) findViewById(R.id.year_month);
        year_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateText(year_month);
            }
        });
        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
        Log.d("年-月", String.valueOf(years) + String.valueOf(month));
        last_month = (RelativeLayout) findViewById(R.id.rl_last);
        last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkbox_all.setChecked(false);
                day = day - 1;
                if (day == 0) {
                    c.set(years, month - 1, day);
                    day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    month = month - 1;
                    if (month == 0) {
                        month = 12;
                        years = years - 1;
                    }
                }
                c.set(years, month, day);
//                getAttendList();

                c.set(years, month - 1, day, 0, 0, 0);
                String begintime = String.valueOf(c.getTimeInMillis() / 1000);
                c.set(years, month - 1, day+1, 0, 0, 0);
                String endtime = String.valueOf(c.getTimeInMillis() / 1000);
                notifyUpdata(begintime, endtime);
                year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
            }
        });
        next_month = (RelativeLayout) findViewById(R.id.rl_next);
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (years >= nowyear && month >= nowmonth && day >= nowday) {
                    ToastUtil.showShort(mContext, "您切换的日期大于当前日期！");

                } else {
                    day = day + 1;
                    Log.e("getMonthDay", getMonthDay() + "");
                    if (day == getMonthDay() + 1) {
                        day = 1;
                        month = month + 1;
                        if (month == 13) {
                            month = 1;
                            years = years + 1;
                        }
                    }
                    c.set(years, month, day);
                    c.set(years, month - 1, day, 0, 0, 0);
                    String begintime = String.valueOf(c.getTimeInMillis() / 1000);
                    c.set(years, month - 1, day+1, 0, 0, 0);
                    String endtime = String.valueOf(c.getTimeInMillis() / 1000);
                    notifyUpdata(begintime, endtime);
//                getAttendList();
                    year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                }

            }
        });
    }

    private int getMonthDay() {
        //判断是否闰年
        if (isRun(years)) {
            //判断月份有多少天
            return getMonthCountForRun(month);
        } else {
            return getMonthCountForNotRun(month);
        }

    }

    /**
     * 判断是否有班级切换
     */
    private void checkIsHasClassOrBaby() {
        if (type != 1) {// TODO: 16/11/5 teacher get class
            ProgressUtil.showLoadingDialog(mContext);
            String url = NetConstantUrl.TC_GET_CLASS + "&teacherid=" + SPUtils.get(mContext, LocalConstant.USER_ID, "");
            VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    ProgressUtil.dissmisLoadingDialog();
                    if (JsonResult.JSONparser(mContext, result)) {
                        classInfoList.clear();
                        classInfoList.addAll(getClassListBeans(result));
                        showChooseDialog();// TODO: 16/11/5 展示选择列表
                    } else {
                        ToastUtil.showShort(mContext, "暂无可切换选项!");
                    }
                }

                @Override
                public void onError() {
                    ProgressUtil.dissmisLoadingDialog();
                }
            });

        } else {// TODO: 16/11/5 parent get baby class

        }

    }

    public List<ClassInfo> getClassListBeans(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ClassInfo>>() {
        }.getType());
    }
    /**
     * 弹出选择班级的dialog
     */
    private void showChooseDialog() {
        ListView listView = new ListView(mContext);
        listView.setDivider(null);
        listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_choose_class, getClassStringData()));
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext).setTitle("班级选择").setContentView(listView);
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mMaterialDialog.dismiss();
                SPUtils.put(mContext, LocalConstant.USER_CLASSID, classInfoList.get(i).getId().toString());
                SPUtils.put(mContext, LocalConstant.CLASS_NAME, classInfoList.get(i).getClassname().toString());
                //topName.setText(classInfoList.get(i).getClassname());
                setTopName(classInfoList.get(i).getClassname());
                ProgressUtil.showLoadingDialog(mContext);
                notifyUpdata(MyApplication.begintime, MyApplication.endtime);


            }
        });
        mMaterialDialog.show();

    }

    private ArrayList<String> getClassStringData() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < classInfoList.size(); i++) {
            strings.add(classInfoList.get(i).getClassname());
        }
        return strings;
    }


    /**
     * 获取闰年该月多少天
     */
    private int getMonthCountForNotRun(int month) {

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 2) {
            return 28;
        } else {
            return 30;
        }
    }

    /**
     * 获取平年该月多少天
     */
    private int getMonthCountForRun(int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 2) {
            return 29;
        } else {
            return 30;
        }
    }

    /**
     * 判断是否闰年
     */
    private Boolean isRun(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setDateText(final TextView v) {
        Calendar cal = Calendar.getInstance();
        Date myData = new Date();
        cal.setTime(myData);

        //获取系统的时间
        years = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);
        final int second = cal.get(Calendar.SECOND);

        Log.e("MONTH", "year" + years);
        Log.e("MONTH", "month" + month);
        Log.e("MONTH", "day" + day);
        Log.e("MONTH", "hour" + hour);
        Log.e("MONTH", "minute" + minute);
        Log.e("MONTH", "second" + second);

        DatePickerDialog dlg = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("MONTH", "monthOfYear" + monthOfYear);
                monthOfYear += 1;//monthOfYear 从0开始

                if (year >= years && monthOfYear >= month && dayOfMonth > day) {
                    ToastUtil.showShort(mContext, "您选择的日期大于今天日期！");
                } else {

                    years = year;
                    month = monthOfYear;
                    day = dayOfMonth;

                    String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                    v.setText(data);
                    c.set(years, month - 1, day, 0, 0, 0);
                    String begintime = String.valueOf(c.getTimeInMillis() / 1000);
                    c.set(years, month - 1, day+1, 0, 0, 0);
                    String endtime = String.valueOf(c.getTimeInMillis() / 1000);
                    notifyUpdata(begintime, endtime);
//                    getAttendList();
                }


            }

        }, years, month, day);
        dlg.show();

    }


    private void inittab() {

        mTitleList = new ArrayList<>();
        mTitleList.add("全部");
       /* mTitleList.add("1号宿舍楼");
        mTitleList.add("邢台");*/
        for (int i = 0; i <locationList.size() ; i++) {
            for(Iterator it = locationList.iterator(); it.hasNext(); i++)
            {

                mTitleList.add((String)it.next());
            }
        }


        //添加tab选项卡，默认第一个选中
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(0)), true);
        tabs.addTab(tabs.newTab().setText(mTitleList.get(0)),true);
        MapFragment fragment1 = MapFragment.newInstance("");
        fragments = new ArrayList<>();
        fragments.add(fragment1);
        for (int i = 1; i < mTitleList.size(); i++) {
            tabs.addTab(tabs.newTab().setText(mTitleList.get(i)));
            MapFragment fragment =MapFragment.newInstance(mTitleList.get(i));
            fragments.add(fragment);
        }
        /*tabs.addTab(tabs.newTab().setText(mTitleList.get(0)),true);
        tabs.addTab(tabs.newTab().setText(mTitleList.get(1)));
        tabs.addTab(tabs.newTab().setText(mTitleList.get(2)));*/

        /*MapFragment fragment1 = MapFragment.newInstance("");
        MapFragment fragment2 = MapFragment.newInstance("一号宿舍楼");
        MapFragment fragment3 = MapFragment.newInstance("邢台");*/




        /*fragments.add(fragment2);
        fragments.add(fragment3);*/


        //设置TabLayout的模式
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        pageradapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, mTitleList);

//        MyPagerAdapter mAdapter = new MyPagerAdapter(fragments);
        //给ViewPager设置适配器
        vpView.setAdapter(pageradapter);
        //将TabLayout和ViewPager关联起来
        tabs.setupWithViewPager(vpView);
//        //给Tabs设置适配器
        tabs.setTabsFromPagerAdapter(pageradapter);
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list_fragment;                         //fragment列表
        private List<String> list_Title;                              //tab名的列表


        public MyPagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
            super(fm);
            this.list_fragment = list_fragment;
            this.list_Title = list_Title;
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
//            return list_Title.get(position);
            return list_Title.get(position % list_Title.size());
        }
    }

    public interface UpdataListener {
        void onUpdata(String begintime, String endtime);
    }

    public void notifyUpdata(String begintime, String endtime) {
//        getSupportFragmentManager().getFragments()
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MapActivity.UpdataListener) {
                ((MapActivity.UpdataListener) fragment).onUpdata(begintime, endtime);
            }
        }
    }
}
