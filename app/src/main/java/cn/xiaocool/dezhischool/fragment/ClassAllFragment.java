package cn.xiaocool.dezhischool.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.StudentGridListAdapter;
import cn.xiaocool.dezhischool.bean.ClassAttendance;
import cn.xiaocool.dezhischool.bean.NewClassAttendance;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.NoScrollListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassAllFragment extends Fragment {

    private RelativeLayout last_month, next_month,send_btn;
    private TextView year_month,tv_class_attendance;
    private Calendar c;
    private ListView gv_childrenList;
    private NoScrollListView buqian_list;
    private StudentGridListAdapter studentGridListAdapter;
    private View anchor;
//    private CheckBox checkbox_all;
    private Context mContext;
    private RequestQueue mQueue;
    private int type;
    private int years, month, day;
    private int nowyear,nowmonth,nowday;
    private ArrayList<ClassAttendance> studentDataArrayList;
    private ArrayList<NewClassAttendance> AttentdataDataArrayList;

    private long lastClickTime;
    private int allarrive=0;
    private int notarrive = 0;
    private int leave = 0;
    private CommonAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_class_all, container, false);

        return view;

    }
    private void checkIdentity() {
        if(SPUtils.get(mContext, LocalConstant.USER_TYPE,"").equals("0")){
            type = 1;
        }else {
            /*if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("y"))
                type = 4;
            if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("n"))
                type = 2;
            if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("n")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("y"))
                type = 3;*/
            type = 2;
        }
    }

    /**
     * 获取班级考勤数据
     */
    private void getAttendList() {
        c.set(years, month-1, day, 0, 0, 0);
        String sign_date = String.valueOf(c.getTimeInMillis()/1000);
        if(type==1){
            mQueue = Volley.newRequestQueue(mContext);
            String URL = NetConstantUrl.ParentGetStudentAttendanceList + "&parentid=" + SPUtils.get(mContext, LocalConstant.USER_ID,"") + "&sign_date=" + sign_date;
            Log.d("parent",URL);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String data) {
                    Log.d("onResponse", data);
                    ProgressUtil.dissmisLoadingDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String state = jsonObject.optString("status");
                        Log.d("attentdata",state);
                        if(state.equals("success")){
                            if (JsonResult.JSONparser(mContext,data)) {
                                AttentdataDataArrayList.clear();
                                JSONObject alldata = new JSONObject(jsonObject.optString("data"));
                                JSONObject numberdata = new JSONObject(alldata.optString("numberInfo"));
                                allarrive = numberdata.optInt("student_count");
                                leave = numberdata.optInt("leave_count");
                                notarrive = numberdata.optInt("noarrive");
                                String attendancedata = alldata.optString("attendancelist");
                                Log.d("attentdata",jsonObject.optString("data"));
                                Gson gson = new Gson();
                                ArrayList<NewClassAttendance> arrayList = gson.fromJson(attendancedata, new TypeToken<List<NewClassAttendance>>() {
                                }.getType());
                                AttentdataDataArrayList.addAll(arrayList);
                                setText();

                                ParentsetAdapter();
                            }
                        }else{

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
        }else{
            mQueue = Volley.newRequestQueue(mContext);
            String URL = NetConstantUrl.TeacherGetStudentAttendanceList + "&classid=" + SPUtils.get(mContext, LocalConstant.USER_CLASSID,"") + "&sign_date=" + sign_date;
            Log.d("teacher",URL);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String data) {
                    Log.d("onResponse", data);
                    ProgressUtil.dissmisLoadingDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String state = jsonObject.optString("status");
                        Log.d("attentdata",state);
                        if(state.equals("success")){
                            if (JsonResult.JSONparser(mContext,data)) {
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

                                for(int i = 0; i < length;i++){
                                    NewClassAttendance AttenBean = new NewClassAttendance();
                                    List<NewClassAttendance.attendance_info> InfoDataArray ;
                                    attendanceObject = (JSONObject)attendanceArray.get(i);
                                    AttenBean.setStudentid(attendanceObject.getString("userid"));
                                    AttenBean.setName(attendanceObject.getString("name"));
                                    AttenBean.setPhoto(attendanceObject.getString("photo"));
                                    JSONArray infoArray = attendanceObject.getJSONArray("attendance_info");
                                    int infolength = infoArray.length();
                                    JSONObject infoObject;
                                    InfoDataArray = new ArrayList<>();
                                    for(int l = 0;l < infolength;l++){
                                        infoObject = (JSONObject)infoArray.get(l);
                                        NewClassAttendance.attendance_info InfoBean = new NewClassAttendance.attendance_info();
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                ParentsetAdapter();
                            }
                        }else{

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
        }


    }
    /**
     * 字符串转模型
     *
     * @param
     * @return
     */
    private List<NewClassAttendance> getBeanFromJsonSystem(String data) {
//        String data = "";
//        try {
//            JSONObject json = new JSONObject(result);
//            data = json.getString("data");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return new Gson().fromJson(data, new TypeToken<List<NewClassAttendance>>() {
        }.getType());
    }
    /**
     * 显示已到，未到，请假人数
     * */
    private void setText() {
        tv_class_attendance.setText("学生总数:"+allarrive+" 未到:" + notarrive+ " 请假:" + leave);
    }
    /**
     *
     */
    /**
     * 用户补签
     *
     * */
    private void sendBuQian() {

        String id = getIds();
        if (id.length()>1){
            String URL = NetConstantUrl.CLASS_RESIGN + "&userid="+ id +"&schoolid="+SPUtils.get(mContext,LocalConstant.SCHOOL_ID,"");
            Log.e("sendBuQian", URL);
            URL = URL.trim();
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String arg0) {

                    Log.d("onResponse", arg0);

                    try {
                        JSONObject jsonObject = new JSONObject(arg0);
                        String state = jsonObject.optString("status");
                        if (JsonResult.JSONparser(mContext,arg0)) {

                            ToastUtil.showShort(mContext,"补签成功!");
                            getAttendList();

                        }else {
                            ToastUtil.showShort(mContext, "补签失败" + jsonObject.optString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    ToastUtil.showShort(mContext, arg0.toString());
                }
            });
            mQueue.add(request);
        }else {
            ToastUtil.showShort(mContext,"暂无需要补签的人！");
        }



    }

    /**
     * [防止快速点击]
     *
     * @return
     */
    private boolean fastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    /**
     * 获取补签的id
     * */
    private String getIds() {
        String ids = "";
        for (int i=0;i<studentDataArrayList.size();i++){
            if (studentDataArrayList.get(i).getCheckedType().equals("2")){
                ids = ids+","+studentDataArrayList.get(i).getUserid();
            }
        }
        if (ids.length()>1){
            ids= ids.substring(1,ids.length());
        }

        Log.e("ids",ids);
        return ids;
    }

    /**
     * 设置适配器
     * */
    private void setAdapter() {
        if (studentGridListAdapter!=null){
            studentGridListAdapter.notifyDataSetChanged();
        }else {
            studentGridListAdapter = new StudentGridListAdapter(studentDataArrayList,mContext,"1");
            gv_childrenList.setAdapter(studentGridListAdapter);
        }
    }
    /**
     * 设置适配器
     * */
    private void ParentsetAdapter() {
            if ( adapter!= null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new CommonAdapter<NewClassAttendance>(mContext, AttentdataDataArrayList, R.layout.item_attendance_list) {

                    @Override
                    public void convert(ViewHolder holder, NewClassAttendance attendanceInfo) {
                        holder.setText(R.id.student_name, attendanceInfo.getName());
//                                .setText(R.id.post_content, webListInfo.getPost_excerpt())
//                                .setText(R.id.post_date, webListInfo.getPost_date());
                        if((attendanceInfo.getAttendance_infolist() == null)||(attendanceInfo.getAttendance_infolist().size() == 0)){
                            holder.setText(R.id.attance_content_red
                                    , "缺勤");
                            holder.getView(R.id.attance_content).setVisibility(View.GONE);
                            holder.getView(R.id.attance_content_red).setVisibility(View.VISIBLE);
//                            (TextView) txx = (TextView)holder.getView(R.id.attance_content);
                        }else{
                            String atta_str="";
                            List<NewClassAttendance.attendance_info> array =  new ArrayList<>();
                            array = attendanceInfo.getAttendance_infolist();
                            int length = array.size();
                            NewClassAttendance.attendance_info info = new NewClassAttendance.attendance_info();
                            for (int i=0;i<length;i++){
                                info = array.get(i);
                                if(info.getType().equals("1")){

                                    atta_str =  atta_str+ "进校:"+info.getTime()+"  ";
                                }else{
                                    atta_str  = atta_str+ "\n离校:"+info.getTime()+"  ";
                                }
                            }
                            holder.getView(R.id.attance_content).setVisibility(View.VISIBLE);
                            holder.getView(R.id.attance_content_red).setVisibility(View.GONE);
                            holder.setText(R.id.attance_content, atta_str);
                        }
                        if (attendanceInfo.getPhoto().equals("")) {
//                            holder.getView(R.id.student_avatar).setVisibility(View.VISIBLE);
                        } else {
//                            holder.getView(R.id.student_avatar).setVisibility(View.VISIBLE);
                            Log.d("photo",NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto());
                            ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto(), (ImageView) holder.getView(R.id.student_avatar));
                        }
                    }

                };
                gv_childrenList.setAdapter(adapter);
            }

    }

    @Override
    public void onResume() {
        super.onResume();
        getAttendList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        studentDataArrayList = new ArrayList<>();
        AttentdataDataArrayList = new ArrayList<>();
//        InfoDataArray = new ArrayList<>();
        mContext = getActivity();
        ProgressUtil.showLoadingDialog((Activity) mContext);
        mQueue = Volley.newRequestQueue(mContext);
        checkIdentity();
        initView();

    }

    /**
     * 初始化控件
     * */
    private void initView() {


        tv_class_attendance = (TextView) getView().findViewById(R.id.tv_class_attendance);
//        checkbox_all = (CheckBox) getView().findViewById(R.id.checkbox_all);
//        checkbox_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkall();
//            }
//        });
//        send_btn = (RelativeLayout) getView().findViewById(R.id.send_btn);
//        send_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fastClick())return;
//                sendBuQian();
//            }
//        });
        anchor = getView().findViewById(R.id.anchor);
        gv_childrenList = (ListView) getView().findViewById(R.id.attance_list);
        buqian_list = (NoScrollListView) getView().findViewById(R.id.buqian_list);


//        gv_childrenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (studentDataArrayList.get(position).getCheckedType().equals("1")) {
//                    studentDataArrayList.get(position).setCheckedType("2");
//                } else if (studentDataArrayList.get(position).getCheckedType().equals("2")) {
//                    studentDataArrayList.get(position).setCheckedType("1");
//                }
//
//                int flag =0;
//                for (int i=0;i<studentDataArrayList.size();i++){
//                    if (studentDataArrayList.get(i).getCheckedType().equals("1")) {
//                        flag =1;
//                    }
//                }
//
//                if (flag==0){
//
//                    checkbox_all.setChecked(true);
//                }else {
//                    checkbox_all.setChecked(false);
//                }
//
//                studentGridListAdapter.notifyDataSetChanged();
//                Log.e("dsdsd", "sadsad");
//            }
//        });


        //获取今天的年月
        c = Calendar.getInstance();
        years = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        nowyear = years;
        nowmonth = month;
        nowday = day;
        year_month = (TextView) getView().findViewById(R.id.year_month);
        year_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateText(year_month);
            }
        });
        year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-"+ String.valueOf(day));
        Log.d("年-月", String.valueOf(years) + String.valueOf(month));
        last_month = (RelativeLayout) getView().findViewById(R.id.rl_last);
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
                getAttendList();


                year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
            }
        });
        next_month = (RelativeLayout) getView().findViewById(R.id.rl_next);
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                checkbox_all.setChecked(false);
                if (years>=nowyear&&month>=nowmonth&&day>=nowday){
                    ToastUtil.showShort(getActivity(),"您切换的日期大于当前日期！");

                }else {

                    day = day + 1;
                    Log.e("getMonthDay",getMonthDay()+"");
                    if (day == getMonthDay()+1) {

                        day = 1;
                        month = month + 1;
                        if (month == 13) {
                            month = 1;
                            years = years + 1;
                        }
                    }
                    c.set(years, month, day);
                    getAttendList();
                    year_month.setText(String.valueOf(years) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                }

            }
        });
    }



    private int getMonthDay() {
        //判断是否闰年
        if (isRun(years)){
            //判断月份有多少天
            return getMonthCountForRun(month);
        }else {
            return getMonthCountForNotRun(month);
        }

    }


    /**
     * 获取闰年该月多少天
     * */
    private int getMonthCountForNotRun(int month) {

        if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
            return 31;
        }else if (month==2){
            return 28;
        }else {
            return 30;
        }
    }

    /**
     * 获取平年该月多少天
     * */
    private int getMonthCountForRun(int month) {
        if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
            return 31;
        }else if (month==2){
            return 29;
        }else {
            return 30;
        }



    }

    /**
     * 判断是否闰年
     * */
    private Boolean isRun(int year) {
        if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
            return true;
        }else{
            return false;
        }
    }
//    /**
//     * 将可以补签的人全选
//     */
//    private void checkall() {
//
//        if (checkbox_all.isChecked()){
//
//            for (int i = 0; i < studentDataArrayList.size(); i++) {
//                if (studentDataArrayList.get(i).getCheckedType().equals("1")) {
//                    studentDataArrayList.get(i).setCheckedType("2");
//                }
//
//            }
//
//        }else {
//
//            for (int i = 0; i < studentDataArrayList.size(); i++) {
//                if (studentDataArrayList.get(i).getCheckedType().equals("2")) {
//                    studentDataArrayList.get(i).setCheckedType("1");
//                }
//
//            }
//
//        }
//
//
//        studentGridListAdapter.notifyDataSetChanged();
//
//    }




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

                if (year>=nowyear&&monthOfYear>=nowmonth&&dayOfMonth>=nowday){
                    ToastUtil.showShort(getActivity(),"您选择的日期大于今天日期！");
                }else {

                    years = year;
                    month = monthOfYear;
                    day = dayOfMonth;

                    String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                    v.setText(data);

                    getAttendList();
                }


            }

        }, years, month, day);
        dlg.show();

    }

    /**
     * 将时间转换为时间戳
     */
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
//            Log.d("--444444---", times);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

}

