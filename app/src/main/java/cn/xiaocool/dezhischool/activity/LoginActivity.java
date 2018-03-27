package cn.xiaocool.dezhischool.activity;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.BabyInfo;
import cn.xiaocool.dezhischool.bean.ClassInfo;
import cn.xiaocool.dezhischool.bean.LoginReturn;
import cn.xiaocool.dezhischool.bean.SingleBabyInfos;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.LogUtils;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.view.CleanableEditText;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_ed_phone)
    CleanableEditText activityLoginEdPhone;
    @BindView(R.id.activity_login_ed_psw)
    CleanableEditText activityLoginEdPsw;
    @BindView(R.id.activity_login_btn_login)
    TextView activityLoginBtnLogin;
    @BindView(R.id.activity_login_btn_forgetpsw)
    TextView activityLoginBtnForgetpsw;
    @BindView(R.id.login_rb_parent)
    RadioButton loginRbParent;
    @BindView(R.id.login_rb_teacher)
    RadioButton loginRbTeacher;
    @BindView(R.id.login_rg_licence)
    RadioGroup loginRgLicence;
    private Context context;
    private String type = "0";
    private LoginReturn loginReturn;
    private String isPrinsiple, isClassleader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTopName(this.getString(R.string.app_name));
        hideLeft();
        hideRightText();
        context = this;
        activityLoginEdPhone.setText((String)SPUtils.get(context,LocalConstant.USER_ACCOUNT,""));
        licenceIS();
    }

    @Override
    public void requsetData() {

    }

    @OnClick({R.id.activity_login_btn_login, R.id.activity_login_btn_forgetpsw})
    public void onClick(View view) {
        switch (view.getId()) {
            //登录
            case R.id.activity_login_btn_login:
                login();
                break;
            //忘记密码
            case R.id.activity_login_btn_forgetpsw:
                startActivity(ForgetPswActivity.class);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        //判断手机号位数
        if (activityLoginEdPhone.getText().length() != 11) {
            ToastUtil.showShort(context, "请输入正确的手机号!");
            return;
        }
        //判断是否选择身份
        //licenceIS();

        //判断是否输入密码
        if (activityLoginEdPsw.getText().length() == 0) {
            ToastUtil.showShort(context, "请输入密码!");
            return;
        }
        ProgressUtil.showLoadingDialog(this);
        //调用登录接口
        String url;
        String registerId = (String) SPUtils.get(context,LocalConstant.JPUSH_REGISTERID,"");
        //判断使用的身份网址
        if (type.equals("0")) {
            url = NetConstantUrl.LOGIN_URL + "&phone=" + activityLoginEdPhone.getText().toString() + "&password=" + activityLoginEdPsw.getText().toString()+"&jgtoken=" + registerId+ "&type=" + type;
        } else {
            url = NetConstantUrl.LOGIN_URL + "&phone=" + activityLoginEdPhone.getText().toString() + "&password=" + activityLoginEdPsw.getText().toString()
                    + "&type=" + type+"&jgtoken=" + registerId;
        }

        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    Log.d("TAG_login", result.toString()+"1111");
                    loginReturn = new LoginReturn();
                    loginReturn = getBeanFromJson(result);
                    Log.d("test", "onSuccess: " +loginReturn);
                    SPUtils.put(context,LocalConstant.USER_ACCOUNT,activityLoginEdPhone.getText().toString());
                    SPUtils.put(context, LocalConstant.USER_PASSWORD, activityLoginEdPsw.getText().toString());
                    spInLocal();
                    getDuty();
                    setJpushAlias();
                }else {
                    ProgressUtil.dissmisLoadingDialog();
                    ToastUtil.showShort(context,"密码错误！");
                }
            }

            @Override
            public void onError() {
                ProgressUtil.dissmisLoadingDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //JPushInterface.resumePush(context);
        JPushInterface.init(this);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    /**
     * 极光设置别名
     */
    private void setJpushAlias() {
        String userid = (String) SPUtils.get(context,LocalConstant.USER_ID,"");
        LogUtils.i("userid",userid);
        Log.d("test", "setJpushAlias: " +userid);
        JPushInterface.setAlias(getApplicationContext(), userid, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                String logs;
                switch (i) {
                    case 0:
                        logs = "Set tag and alias success";
                        Log.i("setAlias", logs);
                        // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                        break;
                    case 6002:
                        logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                        Log.i("setAlias", logs);
                        // 延迟 60 秒来调用 Handler 设置别名
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                        break;
                    default:
                        logs = "Failed with errorCode = " + i;
                        Log.e("setAlias", logs);
                }

            }
        });
        JPushInterface.resumePush(context);
    }

    /**
     * 根据登录信息获取职务
     */
    private void getDuty() {
        //老师登录-->获取职务
        //家长登录-->获取宝宝的信息
        if (SPUtils.get(context, LocalConstant.USER_TYPE, "").equals("1")) {
            String url_duty = NetConstantUrl.GET_DUTY + "&teacherid=" + loginReturn.getId();
            VolleyUtil.VolleyGetRequest(context, url_duty, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    if (JsonResult.JSONparser(context, result)) {
                        //判断是否为校长
                        checkisPrinsiple(result);
                        //判断是否为班主任
                        //checkisClassleader(result);
                        SPUtils.put(context, LocalConstant.USER_IS_PRINSIPLE, isPrinsiple);
                        SPUtils.put(context, LocalConstant.USER_IS_CLASSLEADER, "y");
                        getClassInfomation();
                        /*//如果是班主任，获取所任班级信息，否则跳转到主页面
                        if (isClassleader.equals("y")) {
                            //获取班主任班级信息
                            getClassInfomation();
                        } else {
                            startActivity(MainActivity.class);
                            finish();
                        }*/
                        startActivity(MainActivity.class);
                        finish();
                    }
                }

                @Override
                public void onError() {

                }
            });

        } else {
            //获取家长对应的宝宝信息，并存入本地
            getBabyInfo();
        }
    }

    /**
     * 获取家长所关联的宝宝信息
     */
    private void getBabyInfo() {
        final String baby_url = NetConstantUrl.GET_USER_RELATION + "&userid=" + loginReturn.getId();
        VolleyUtil.VolleyGetRequest(context, baby_url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                List<BabyInfo> allbaby ;
                allbaby = new ArrayList<BabyInfo>();
                allbaby = getBabyInfoFromJson(result);
                int babycount=allbaby.size();
                boolean hadbaby;
                hadbaby=false;
                if (babycount > 0){
                    hadbaby = true;
                }
                if (!TextUtils.isEmpty(allbaby.get(0).getStudentid())){
                    SPUtils.put(context, LocalConstant.USER_BABYID, allbaby.get(0).getStudentid());
                    SPUtils.put(context, LocalConstant.USER_BABYNAME, allbaby.get(0).getStudentname());
                }
                for(int i=0;i<babycount;i++){

                        SingleBabyInfos.getmBabyInfos().getInfos().add(allbaby.get(i));


                }
                if(hadbaby){
                    startActivity(MainActivity.class);

                    finish();
                }else{
                    ProgressUtil.dissmisLoadingDialog();
                    ToastUtil.showShort(context,"你没有关联学生信息，请与班主任联系！");
                }
                  BabyInfo babyInfo = getBabyInfoFromJson(result).get(0);
//                SPUtils.put(context, LocalConstant.USER_BABYID, babyInfo.getStudentid());
                  SPUtils.put(context, LocalConstant.USER_CLASSID, babyInfo.getClasslist().get(0).getClassid());
//                SPUtils.put(context,LocalConstant.CLASS_NAME,babyInfo.getClasslist().get(0).getClassname());
//                SPUtils.put(context, LocalConstant.SCHOOL_ID,babyInfo.getClasslist().get(0).getSchoolid());
//                startActivity(MainActivity.class);
//                finish();
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 获取班主任所任班级信息
     */
    private void getClassInfomation() {
        String url_classinfo = NetConstantUrl.GET_CLASSINFO + "&teacherid=" + loginReturn.getId();
        VolleyUtil.VolleyGetRequest(context, url_classinfo, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    //得到班级信息
                    ClassInfo classInfo = getClassInfoFromJson(result).get(0);
                    //记录班级id到本地
                    SPUtils.put(context, LocalConstant.USER_CLASSID, classInfo.getClassid());
                    startActivity(MainActivity.class);
                    finish();
                }
            }

            @Override
            public void onError() {

            }
        });

        String url = NetConstantUrl.TC_GET_CLASS + "&teacherid=" + SPUtils.get(context,LocalConstant.USER_ID,"");
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                ProgressUtil.dissmisLoadingDialog();
                if (JsonResult.JSONparser(context,result)){
                    List<ClassInfo> classInfos = getClassListBeans(result);
                    if (classInfos.size()>0){
                        SPUtils.put(context,LocalConstant.USER_CLASSID,classInfos.get(0).getId().toString());
                        SPUtils.put(context,LocalConstant.CLASS_NAME,classInfos.get(0).getClassname().toString());
                    }
                }else {

                }
            }

            @Override
            public void onError() {
                ProgressUtil.dissmisLoadingDialog();
            }
        });
    }

    /**
     * 获取老师默认班级ID 默认班级列表第一个
     * @param result
     * @return
     */
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
     * 判断是否为校长
     *
     * @param result
     * @return
     * s 校长
     * y >5时有高级权限
     * n 无高级权限
     */
    private void checkisPrinsiple(String result) {
        JSONArray data = null;
        try {
            JSONObject json = new JSONObject(result);
            data = json.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject itemObject = data.optJSONObject(i);
            if (Integer.parseInt(itemObject.optString("ispower"))>5) {
                isPrinsiple = "y";
                return;
            }
        }
        isPrinsiple = "n";
    }

    /**
     * 判断是否为班主任
     *
     * @param result
     * @return
     */
    private void checkisClassleader(String result) {
        JSONArray data = null;
        try {
            JSONObject json = new JSONObject(result);
            data = json.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject itemObject = data.optJSONObject(i);
            if (itemObject.optString("id").equals("3")) {
                isClassleader = "y";
                return;
            }
        }
        isClassleader = "n";
    }


    /**
     * 将需要的信息存储到本地
     */
    private void spInLocal() {
        if (type.equals("1")){
            SPUtils.put(context,LocalConstant.SCHOOL_ID,loginReturn.getSchoolid());
        }
        SPUtils.put(context, LocalConstant.USER_ID, loginReturn.getId());
        SPUtils.put(context, LocalConstant.USER_NAME, loginReturn.getName());
        SPUtils.put(context, LocalConstant.USER_PHOTO, loginReturn.getPhoto());
        SPUtils.put(context, LocalConstant.SCHOOL_ID, loginReturn.getSchoolid());
        SPUtils.put(context, LocalConstant.SCHOOL_NAME, loginReturn.getSchoolname());
        SPUtils.put(context, LocalConstant.USER_TYPE, type);
        SPUtils.put(context, LocalConstant.SHOW_BEHAVIOR_HISTORY,loginReturn.getHas_behavior_trajectory());
        Log.d("test", "spInLocal: " +loginReturn.getHas_behavior_trajectory());
    }

    /**
     * 判断选择身份登录
     */
    private void licenceIS() {
        loginRgLicence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == loginRbParent.getId()) {//家长身份登录
                    type = "0";
                } else if (checkedId == loginRbTeacher.getId()) {//教师身份登录
                    type = "1";
                }
            }
        });
    }

    /**
     * 字符串转模型（登录信息）
     *
     * @param result
     * @return
     */
    private LoginReturn getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<LoginReturn>() {
        }.getType());
    }

    /**
     * 字符串转模型（班级信息）
     *
     * @param result
     * @return
     */
    private List<ClassInfo> getClassInfoFromJson(String result) {
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
     * 字符串转模型（宝宝信息）
     *
     * @param * @return
     */
    private List<BabyInfo> getBabyInfoFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<BabyInfo>>() {
        }.getType());
    }
}
