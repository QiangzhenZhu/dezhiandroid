package cn.xiaocool.dezhischool.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.StudentGridListAdapter;
import cn.xiaocool.dezhischool.bean.ClassAttendance;
import cn.xiaocool.dezhischool.bean.ClassInfo;
import cn.xiaocool.dezhischool.bean.DormitoryInfo;
import cn.xiaocool.dezhischool.bean.MonthAttendance;
import cn.xiaocool.dezhischool.bean.NewClassAttendance;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.DateUtils;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.NoScrollListView;
import info.hoang8f.android.segmented.SegmentedGroup;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by mac on 2017/6/15.
 */

public class AttanceActivity extends BaseActivity {
    private static final String TAG = "AttanceActivity";
    @BindView(R.id.tv_class_all)
    TextView tvClassAll;
    @BindView(R.id.tv_class_attendance)
    TextView tvClassAttendance;
    @BindView(R.id.tv_class_noattendance)
    TextView tvClassNoattendance;
    @BindView(R.id.tv_class_mounce)
    TextView tvClassMounce;
    @BindView(R.id.segmented2)
    SegmentedGroup segmentedGroup;
    @BindView(R.id.scca_top_title_zoudu)
    RadioButton radioButtonzoudu;
    @BindView(R.id.scca_top_title_zhusu)
    RadioButton radioButtonzhusu;
    @BindView(R.id.mSwitchClass)
    TextView mSwitchClass;
    @BindView(R.id.change_class)
    LinearLayout changeClass;
    @BindView(R.id.tv_top_name)
    TextView tvTopName;
    @BindView(R.id.buqian_list)
    NoScrollListView buqianList;
    @BindView(R.id.attance_list)
    ListView attanceList;
    private RelativeLayout last_month, next_month, send_btn, upjiantou;
    private TextView year_month;
    private Calendar c;
    private ListView gv_childrenList;
    private NoScrollListView buqian_list;
    private StudentGridListAdapter studentGridListAdapter;
    private View anchor;
    //    private CheckBox checkbox_all;
    private Context mContext;
    private RequestQueue mQueue;
    private int type, statetype;//statetype 1>缺勤2>请假
    private int years, month, day;
    private int nowyear, nowmonth, nowday;
    private ArrayList<ClassAttendance> studentDataArrayList;
    private ArrayList<NewClassAttendance> AttentdataDataArrayList;
    private ArrayList<NewClassAttendance> newAttentdataDataArrayList;
    private ArrayList<MonthAttendance> monthAttendanceArrayList;
    private ArrayList<MonthAttendance> newMonthAttendanceArrayList;
    private long lastClickTime;
    private int allarrive = 0;
    private int notarrive = 0;
    private int leave = 0;
    private CommonAdapter adapter;
    private CommonAdapter mounseadapter;
    private View mRightText;
    private List<ClassInfo> classInfoList;
    private int startType;
    private List<DormitoryInfo> dormitoryInfos;
    private int studentType = 2;
    private RelativeLayout rvTitle;

    @Override
    public void requsetData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_class_all);
        ButterKnife.bind(this);
        rvTitle = findViewById(R.id.rv_title);
        startType = getIntent().getIntExtra("type", 0);

        if (startType == 0) {//0 学生考勤 // 1 门口考勤
            /*rvTitle.setVisibility(View.GONE);
            hideRightText();*/
        } else {
            setTopName("门口考勤");
            hideTopView();
        }
        classInfoList = new ArrayList<>();
        studentDataArrayList = new ArrayList<>();
        AttentdataDataArrayList = new ArrayList<>();
        newAttentdataDataArrayList = new ArrayList<>();
        monthAttendanceArrayList = new ArrayList<>();
        newMonthAttendanceArrayList = new ArrayList<>();
//        InfoDataArray = new ArrayList<>();
        mContext = this;
        dormitoryInfos = new ArrayList<>();
        initView();
        mQueue = Volley.newRequestQueue(mContext);
        checkIdentity();
        switch (type) {
            case 1:
                tvClassMounce.setVisibility(View.GONE);
                tvClassMounce.setClickable(false);
                tvClassNoattendance.setClickable(false);
                tvClassAttendance.setClickable(false);
                tvClassAll.setClickable(false);
                break;
        }

        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            mSwitchClass.setVisibility(View.GONE);
            segmentedGroup.setVisibility(View.GONE);
            hideTopView();
            setTopName("学生考勤");

        } else {
            tvTopName.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        segmentedGroup.check(R.id.scca_top_title_zoudu);

        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.scca_top_title_zoudu:
                        studentType = 2;
                        getAttendList();
                        Log.d(TAG, "onCheckedChanged: " + studentType);
                        break;
                    case R.id.scca_top_title_zhusu:
                        studentType = 1;
                        getAttendList();
                        Log.d(TAG, "onCheckedChanged: " + studentType);
                        break;

                    default:
                        break;
                }
            }
        });
        segmentedGroup.setTintColor(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_bg_color));

        upjiantou = findViewById(R.id.up_jiantou);
        upjiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (startType == 0) {//0 学生考勤  // 1 门口考勤
            mRightText = setRightText("切换");

        } else {
            mRightText = findViewById(R.id.mSwitchClass);
        }

        anchor = findViewById(R.id.anchor);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startType == 1) {
                    checkIsHasClassOrBaby();
                } else if (startType == 0) {
                    showChooseDormitory();
                }
            }
        });
        gv_childrenList = (ListView) findViewById(R.id.attance_list);
        buqian_list = (NoScrollListView) findViewById(R.id.buqian_list);
        //获取今天的年月
        c = Calendar.getInstance();
        years = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        nowyear = years;
        nowmonth = month;
        nowday = day;
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
                if (type == 2) {
                    tvClassAll.setSelected(true);
                    tvClassMounce.setSelected(false);
                    tvClassNoattendance.setSelected(false);
                    tvClassAttendance.setSelected(false);
                }
                switch (tvClassMounce.getText().toString()) {
                    case "日":
                        day = day - 1;
                        break;
                    case "月":
                        day = 0;
                        break;
                    default:
                        day = day - 1;
                }
//                day = day - 1;
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


                switch (tvClassMounce.getText().toString()) {
                    case "日":
                        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                        getAttendList();
                        break;
                    case "月":
                        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month));
                        getMounthAttance();
                        break;
                    default:
                        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                }

            }
        });
        next_month = (RelativeLayout) findViewById(R.id.rl_next);
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 2) {
                    tvClassAll.setSelected(true);
                    tvClassMounce.setSelected(false);
                    tvClassNoattendance.setSelected(false);
                    tvClassAttendance.setSelected(false);
                }
//                checkbox_all.setChecked(false);
                if (type == 1) {
                    if (years >= nowyear && month >= nowmonth && day >= nowday) {
                        ToastUtil.showShort(mContext, "您切换的日期大于当前日期！");
                    } else {
                        nextMounth();
                    }
                } else {
                    switch (tvClassMounce.getText().toString()) {
                        case "日":
                            day = day + 1;
                            nextMounth();
                            break;
                        case "月":

                            if (years >= nowyear && month >= nowmonth) {
                                ToastUtil.showShort(mContext, "您切换的日期大于当前日期！");
                            } else {
                                day = getMonthDay() + 1;
                                nextMounth();

                            }

                            break;
                        default:
                            day = day + 1;
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 2018/3/1
        if (startType == 1) {//门口考勤
            getAttendList();
        } else if (startType == 0) {//学生考勤
            getAttendList();
        }

    }

    /**
     * 判断是否有班级切换
     */
    private void checkIsHasClassOrBaby() {
        if (type != 1) {// TODO: 16/11/5 teacher get class
            ProgressUtil.showLoadingDialog(AttanceActivity.this);
            String url = NetConstantUrl.TC_GET_CLASS + "&teacherid=" + SPUtils.get(mContext, LocalConstant.USER_ID, "");
            VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    ProgressUtil.dissmisLoadingDialog();
                    if (JsonResult.JSONparser(mContext, result)) {
                        classInfoList.clear();
                        classInfoList.addAll(getClassListBeans(result));
                        List<ClassInfo> infos = new ArrayList<>();
                        for (int i = 0; i < classInfoList.size(); i++) {
                            if (!(infos.contains(classInfoList.get(i)))) {
                                infos.add(classInfoList.get(i));
                            }
                        }
                        classInfoList.clear();
                        classInfoList.addAll(infos);
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
        listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_choose_class, getClassStringData(1)));
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
                getAttendList();

            }
        });
        mMaterialDialog.show();

    }

    // TODO: 2018/3/1 宿舍选择
    private void showChooseDormitory() {
        ListView listView = new ListView(mContext);
        listView.setDivider(null);
        listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_choose_class, getClassStringData(0)));
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext).setTitle("宿舍选择").setContentView(listView);
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


            }
        });
        mMaterialDialog.show();

    }

    private ArrayList<String> getClassStringData(int type) {
        if (type == 1) {
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < classInfoList.size(); i++) {
                strings.add(classInfoList.get(i).getClassname());
            }
            return strings;
        } else {
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < dormitoryInfos.size(); i++) {
                strings.add(dormitoryInfos.get(i).getDorm_name());
            }
            return strings;
        }
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

                if (year >= nowyear && monthOfYear >= nowmonth && dayOfMonth > nowday) {
                    ToastUtil.showShort(mContext, "您选择的日期大于今天日期！");
                } else {

                    years = year;
                    month = monthOfYear;
                    day = dayOfMonth;
                    String data;
                    if (type == 1) {
                        data = year + "-" + monthOfYear + "-" + dayOfMonth;
                    } else {
                        switch (tvClassMounce.getText().toString()) {
                            case "月":
                                data = year + "-" + monthOfYear;

                                break;
                            case "日":
                                data = year + "-" + monthOfYear + "-" + dayOfMonth;
                                break;
                            default:
                                data = year + "-" + monthOfYear + "-" + dayOfMonth;
                        }
                    }
                    v.setText(data);

                    getAttendList();
                }


            }

        }, years, month, day);
        dlg.show();

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

    // TODO: 2018/3/1  
    public void getDormList() {
        final String url = NetConstantUrl.GET_DORMITORY_LIST + "&teacherid=" + SPUtils.get(mContext, LocalConstant.USER_ID, "");
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if ("success".equals(status)) {
                        String data = object.getString("data");
                        dormitoryInfos = new Gson().fromJson(data, new TypeToken<List<DormitoryInfo>>() {
                        }.getType());
                        if (dormitoryInfos.size() > 0) {
                            String id = dormitoryInfos.get(0).getId();
                            ProgressUtil.dissmisLoadingDialog();
                            showChooseDormitory();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "数据加载错误", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * 获取班级考勤数据
     */
    private void getAttendList() {
        ProgressUtil.showLoadingDialog((Activity) mContext);
        c.set(years, month - 1, day, 0, 0, 0);
        String sign_date = String.valueOf(c.getTimeInMillis() / 1000);
        if (type == 1) {
            mQueue = Volley.newRequestQueue(mContext);
            String URL = NetConstantUrl.ParentGetStudentAttendanceList + "&parentid=" + SPUtils.get(mContext, LocalConstant.USER_ID, "") + "&sign_date=" + sign_date + "&studenttype=" + studentType;
            Log.d("parent", URL);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String data) {
                    Log.d("onResponse", data);
                    ProgressUtil.dissmisLoadingDialog();
                    try {

                        JSONObject jsonObject = new JSONObject(data);
                        String state = jsonObject.optString("status");
                        Log.d("attentdata", state);
                        if (state.equals("success")) {
                            if (JsonResult.JSONparser(mContext, data)) {
                                AttentdataDataArrayList.clear();
                                JSONObject alldata = new JSONObject(jsonObject.optString("data"));
                                JSONObject numberdata = new JSONObject(alldata.optString("numberInfo"));
                                allarrive = numberdata.optInt("student_count");
                                leave = numberdata.optInt("leave_count");
                                notarrive = numberdata.optInt("noarrive");
                                String attendancedata = alldata.optString("attendancelist");
                                Log.d("attentdata", jsonObject.optString("data"));
                                Gson gson = new Gson();
                                ArrayList<NewClassAttendance> arrayList = gson.fromJson(attendancedata, new TypeToken<List<NewClassAttendance>>() {
                                }.getType());
                                AttentdataDataArrayList.clear();
                                AttentdataDataArrayList.addAll(arrayList);


                                setText();

                                ParentsetAdapter(AttentdataDataArrayList);
                            }
                        } else {
                            AttentdataDataArrayList.clear();
                            ParentsetAdapter(AttentdataDataArrayList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    ProgressUtil.dissmisLoadingDialog();
                }
            });
            mQueue.add(request);
        } else {
            mQueue = Volley.newRequestQueue(mContext);
            String URL = NetConstantUrl.TeacherGetStudentAttendanceList + "&classid=" + SPUtils.get(mContext, LocalConstant.USER_CLASSID, "") + "&sign_date=" + sign_date + "&studenttype=" + studentType;
            Log.d("teacher", URL);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String data) {
                    Log.d("onResponse", data);
                    ProgressUtil.dissmisLoadingDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String state = jsonObject.optString("status");
                        Log.d("attentdata", state);
                        if (state.equals("success")) {
                            if (JsonResult.JSONparser(mContext, data)) {
                                AttentdataDataArrayList.clear();
                                JSONObject alldata = new JSONObject(jsonObject.optString("data"));
                                JSONObject numberdata = new JSONObject(alldata.optString("numberInfo"));
                                allarrive = numberdata.optInt("student_count");
                                leave = numberdata.optInt("leave_count");
                                notarrive = numberdata.optInt("noarrive");
                                String attendancedata = alldata.optString("attendancelist");
                                JSONArray attendanceArray = alldata.getJSONArray("attendancelist");
                                int length = attendanceArray.length();
                                JSONObject attendanceObject;

                                for (int i = 0; i < length; i++) {
                                    NewClassAttendance AttenBean = new NewClassAttendance();
                                    List<NewClassAttendance.attendance_info> InfoDataArray;
                                    attendanceObject = (JSONObject) attendanceArray.get(i);
                                    AttenBean.setStudentid(attendanceObject.getString("userid"));
                                    AttenBean.setName(attendanceObject.getString("name"));
                                    AttenBean.setPhoto(attendanceObject.getString("photo"));
                                    AttenBean.setLeave(attendanceObject.getString("isleave"));
                                    JSONArray infoArray = attendanceObject.getJSONArray("attendance_info");
                                    int infolength = infoArray.length();
                                    JSONObject infoObject;
                                    InfoDataArray = new ArrayList<>();
                                    for (int l = 0; l < infolength; l++) {
                                        infoObject = (JSONObject) infoArray.get(l);
                                        NewClassAttendance.attendance_info InfoBean = new NewClassAttendance.attendance_info();
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                        long time = Long.parseLong(infoObject.getString("time"));
//                                        policyData.setCreate_time(simpleDateFormat.format(new Date(time * 1000)));
                                        InfoBean.setTime(simpleDateFormat.format(new Date(time * 1000)));
                                        InfoBean.setType(infoObject.getString("type"));
                                        InfoDataArray.add(InfoBean);
                                    }
//                                    NewClassAttendance.attendance_info InfoBean = new NewClassAttendance.attendance_info();
//                                    InfoBean.setTime("1231231231-"+i);
//                                    InfoBean.setType("1");
//                                    InfoBean
//                                    InfoDataArray.add(InfoBean);
                                    AttenBean.setAttendance_infolist(InfoDataArray);

                                    AttentdataDataArrayList.add(AttenBean);

                                }
//                                AttentdataDataArrayList.addAll(getBeanFromJsonSystem(attendancedata));
                                setText();
//                                Log.d("",AttentdataDataArrayList.toString());
                                ParentsetAdapter(AttentdataDataArrayList);
                            }
                        } else {
                            AttentdataDataArrayList.clear();
                            ParentsetAdapter(AttentdataDataArrayList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    ProgressUtil.dissmisLoadingDialog();
                    Toast.makeText(AttanceActivity.this, "网络连接错误le ", Toast.LENGTH_SHORT).show();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(request);
        }


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

    /**
     * 显示已到，未到，请假人数
     */
    private void setText() {
        tvClassAll.setText("学生总数:" + allarrive);
        tvClassAttendance.setText(" 未到:" + notarrive);
        tvClassNoattendance.setText(" 请假:" + leave);
    }

    /**
     * 设置适配器
     */
    private void ParentsetAdapter(List<NewClassAttendance> attendances) {
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        } else {
        adapter = new CommonAdapter<NewClassAttendance>(mContext, attendances, R.layout.item_attendance_list) {

            @Override
            public void convert(ViewHolder holder, NewClassAttendance attendanceInfo) {
                holder.setText(R.id.student_name, attendanceInfo.getName());
//                                .setText(R.id.post_content, webListInfo.getPost_excerpt())
//                                .setText(R.id.post_date, webListInfo.getPost_date());
                if ((attendanceInfo.getAttendance_infolist() == null) || (attendanceInfo.getAttendance_infolist().size() == 0)) {
                    holder.setText(R.id.attance_content_red
                            , "缺勤", R.color.red);
                    holder.setText(R.id.attance_content
                            , "缺勤", R.color.red);
//                            (TextView) txx = (TextView)holder.getView(R.id.attance_content);
                } else {

                    String atta_str = "";
                    String atta_strs = "";
                    String time;
                    List<NewClassAttendance.attendance_info> array = new ArrayList<>();
                    array = attendanceInfo.getAttendance_infolist();
                    int length = array.size();
                    NewClassAttendance.attendance_info info = new NewClassAttendance.attendance_info();
                    for (int i = 0; i < length; i++) {
                        info = array.get(i);
                        if (type == 1) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            long times = Long.parseLong(info.getTime());
//                                        policyData.setCreate_time(simpleDateFormat.format(new Date(time * 1000)));
                            time = simpleDateFormat.format(new Date(times * 1000));
                        } else {
                            time = info.getTime();
                        }
                        if (info.getType().equals("1")) {
                            if (atta_str.isEmpty()) {
                                atta_str = "进校:" + time + "  ";
                            } else {
                                atta_str = atta_str + "\n进校:" + time + "  ";
                            }

                        } else {
                            if (atta_strs.isEmpty()) {
                                atta_strs = "离校:" + time + "  ";
                            } else {
                                atta_strs = atta_strs + "\n离校:" + time + "  ";
                            }
                        }
                    }
                    if (atta_str.isEmpty()) {
                        holder.setText(R.id.attance_content, "缺勤", R.color.red);
                    } else {
                        holder.setText(R.id.attance_content, atta_str, R.color.green);
                    }
                    if (atta_strs.isEmpty()) {
                        holder.setText(R.id.attance_content_red, "缺勤", R.color.red);
                    } else {
                        holder.setText(R.id.attance_content_red, atta_strs, R.color.green);
                    }


                }
                if (attendanceInfo.getLeave().equals("1")) {
                    holder.getView(R.id.attance_content_red).setVisibility(View.GONE);
                    holder.setText(R.id.attance_content, "请假", R.color.red);
                } else if (attendanceInfo.getLeave().equals("0")) {
                    holder.getView(R.id.attance_content_red).setVisibility(View.VISIBLE);
                }
                if ("".equals(attendanceInfo.getPhoto())) {
//                            holder.getView(R.id.student_avatar).setVisibility(View.VISIBLE);
                } else {
//                            holder.getView(R.id.student_avatar).setVisibility(View.VISIBLE);
                    Log.d("attphoto", NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto());
                    ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto(), (ImageView) holder.getView(R.id.student_avatar));
                }
            }

        };
        gv_childrenList.setAdapter(adapter);
//        }

    }

    @OnClick({R.id.tv_class_all, R.id.tv_class_attendance, R.id.tv_class_noattendance, R.id.tv_class_mounce})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_class_all:

                tvClassAll.setSelected(true);
                tvClassNoattendance.setSelected(false);
                tvClassAttendance.setSelected(false);

                ParentsetAdapter(AttentdataDataArrayList);
                break;
            case R.id.tv_class_attendance:
                statetype = 1;
                tvClassAttendance.setSelected(true);
                tvClassNoattendance.setSelected(false);
                tvClassAll.setSelected(false);
                newAttentdataDataArrayList.clear();
                if (tvClassMounce.getText().toString().equals("日")) {
                    for (int i = 0; i < AttentdataDataArrayList.size(); i++) {
                        if ((AttentdataDataArrayList.get(i).getAttendance_infolist() == null) || (AttentdataDataArrayList.get(i).getAttendance_infolist().size() == 0)) {
                            newAttentdataDataArrayList.add(AttentdataDataArrayList.get(i));
                        }
                    }
                    ParentsetAdapter(newAttentdataDataArrayList);
                } else if (tvClassMounce.getText().toString().equals("月")) {
                    getMounthAttance();
                }
                break;
            case R.id.tv_class_noattendance:
                statetype = 2;
                tvClassNoattendance.setSelected(true);
                tvClassAttendance.setSelected(false);
                tvClassAll.setSelected(false);
                newAttentdataDataArrayList.clear();
                if (tvClassMounce.getText().toString().equals("日")) {
                    for (int i = 0; i < AttentdataDataArrayList.size(); i++) {
                        if (AttentdataDataArrayList.get(i).getLeave().equals("1")) {
                            newAttentdataDataArrayList.add(AttentdataDataArrayList.get(i));
                        }
                    }
                    ParentsetAdapter(newAttentdataDataArrayList);
                } else if (tvClassMounce.getText().toString().equals("月")) {
                    newMonthAttendanceArrayList.clear();
                    for (int i = 0; i < monthAttendanceArrayList.size(); i++) {
                        if (monthAttendanceArrayList.get(i).getLeave_arr().size() > 1) {
                            newMonthAttendanceArrayList.add(monthAttendanceArrayList.get(i));
                        }
                    }
                    MounsesetAdapter(newMonthAttendanceArrayList);
                }

                break;
            case R.id.tv_class_mounce:
                switch (tvClassMounce.getText().toString()) {
                    case "月":
                        getAttendList();
                        tvClassAll.setVisibility(View.VISIBLE);
                        tvClassMounce.setText("日");
                        tvClassAttendance.setSelected(false);
                        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                        break;
                    case "日":
                        statetype = 1;
                        tvClassAll.setVisibility(View.GONE);
                        tvClassMounce.setText("月");
                        tvClassAttendance.setSelected(true);
                        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month));
                        getMounthAttance();
                        break;
                }
                tvClassMounce.setSelected(true);
                tvClassNoattendance.setSelected(false);

                tvClassAll.setSelected(false);
                break;

        }
    }

    private void nextMounth() {
        switch (tvClassMounce.getText().toString()) {
            case "日":
                day = day + 1;
                break;
            case "月":
                day = getMonthDay() + 1;
                break;
            default:
                day = day + 1;
        }
//                    day = day + 1;
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

        switch (tvClassMounce.getText().toString()) {
            case "日":
                year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                getAttendList();
                break;
            case "月":
                year_month.setText(String.valueOf(years) + "-" + String.valueOf(month));
                tvClassNoattendance.setSelected(true);
                getMounthAttance();
                break;
            default:
                year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
        }
//                    year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
    }

    private void getMounthAttance() {
        mQueue = Volley.newRequestQueue(mContext);
        String URL = NetConstantUrl.GetClassStudentAttendanceList + "&classid=" + SPUtils.get(mContext, LocalConstant.USER_CLASSID, "")
                + "&begintime=" + DateUtils.getMonthBeginTimestamp(years, month) + "&endtime=" + DateUtils.getMonthBeginTimestamp(years, month + 1) + "&studenttype=" + studentType;
        Log.d("ClassStudent", URL);
        ProgressUtil.showLoadingDialog(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String data) {
                Log.d("onResponse", data);
                ProgressUtil.dissmisLoadingDialog();
                try {

                    JSONObject jsonObject = new JSONObject(data);
                    String state = jsonObject.optString("status");
                    Log.d("attentdata", state);
                    if (state.equals("success")) {
                        String attendancedata = jsonObject.optString("data");
                        Log.d("attentdata", attendancedata);
                        Gson gson = new Gson();
                        ArrayList<MonthAttendance> arrayList = gson.fromJson(attendancedata, new TypeToken<List<MonthAttendance>>() {
                        }.getType());
                        monthAttendanceArrayList.clear();
                        newMonthAttendanceArrayList.clear();
                        monthAttendanceArrayList.addAll(arrayList);

                        tvClassAttendance.setText(" 未到");
                        tvClassNoattendance.setText(" 请假");
                        for (int i = 0; i < monthAttendanceArrayList.size(); i++) {
                            if (monthAttendanceArrayList.get(i).getNoarrive_arr().size() > 0) {
                                newMonthAttendanceArrayList.add(monthAttendanceArrayList.get(i));
                            }
                        }

                        MounsesetAdapter(monthAttendanceArrayList);
                        Log.d("monthAttendanceArrayLis", monthAttendanceArrayList.size() + "");
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ProgressUtil.dissmisLoadingDialog();
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                ProgressUtil.dissmisLoadingDialog();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

    /*月视图适配器*/
    private void MounsesetAdapter(ArrayList<MonthAttendance> attendances) {
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        } else {

        adapter = null;
        adapter = new CommonAdapter<MonthAttendance>(mContext, attendances, R.layout.item_attendance_list) {

            @Override
            public void convert(ViewHolder holder, MonthAttendance attendanceInfo) {

                String time;
                String attr = "";
                String timeattr = "";


                holder.setText(R.id.student_name, attendanceInfo.getName());
                if (attendanceInfo.getPhoto().equals("")) {
                } else {
                    Log.d("attphoto", NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto());
                    ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto(), (ImageView) holder.getView(R.id.student_avatar));
                }
                if (statetype == 1) {

//                    for (int i = 0; i < attendanceInfo.getNoarrive_arr().size(); i++) {
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
//                        long times = Long.parseLong(attendanceInfo.getNoarrive_arr().get(i).toString());
////                                        policyData.setCreate_time(simpleDateFormat.format(new Date(time * 1000)));
//                        time = simpleDateFormat.format(new Date(times * 1000));
//                        attr = attr + "\n缺勤:";
//                        timeattr = timeattr + "\n" + time;
//                    }
                    holder.setText(R.id.attance_content_red
                            , "缺勤" + attendanceInfo.getNoarrive() + "天", R.color.red);
                    holder.setText(R.id.attance_content
                            , "请假" + attendanceInfo.getLeave_count() + "天", R.color.red);
                } else if (statetype == 2) {

                    for (int i = 0; i < attendanceInfo.getLeave_arr().size(); i++) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                        long times = Long.parseLong(attendanceInfo.getLeave_arr().get(i).toString());
//                                        policyData.setCreate_time(simpleDateFormat.format(new Date(time * 1000)));
                        time = simpleDateFormat.format(new Date(times * 1000));
                        attr = attr + "\n请假:";
                        timeattr = timeattr + "\n" + time;
                    }
                    holder.setText(R.id.attance_content_red
                            , timeattr, R.color.red);
                    holder.setText(R.id.attance_content
                            , attr, R.color.red);
                }


            }
        };
        gv_childrenList.setAdapter(adapter);
    }
}

