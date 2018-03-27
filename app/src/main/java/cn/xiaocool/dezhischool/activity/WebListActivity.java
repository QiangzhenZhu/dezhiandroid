package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.ClassInfo;
import cn.xiaocool.dezhischool.bean.Examinfo;
import cn.xiaocool.dezhischool.bean.MonthAttendance;
import cn.xiaocool.dezhischool.bean.SystemNews;
import cn.xiaocool.dezhischool.bean.WebListInfo;
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
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.CustomHeader;
import me.drakeet.materialdialog.MaterialDialog;

public class WebListActivity extends BaseActivity {

    @BindView(R.id.web_list)
    ListView webList;
    @BindView(R.id.web_list_swip)
    XRefreshView webListSwip;
    private int beginid = 0;

    private ArrayList<WebListInfo> webListInfoArrayList;
    private ArrayList<SystemNews> systemNewses;

    private ArrayList<Examinfo> examinfos;
    private CommonAdapter adapter;
    Context mContext;
    int type;
    private RequestQueue mQueue;
    private RelativeLayout mRightText;
    private List<ClassInfo> classInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_list);
        ButterKnife.bind(this);
        classInfoList = new ArrayList<>();
        setTopName("考试信息");
        mRightText = (RelativeLayout) setRightText("切换");

        mContext = this;
        mQueue = Volley.newRequestQueue(mContext);
        examinfos = new ArrayList<>();
        checkIdentity();
//        getData();

        webListInfoArrayList = new ArrayList<>();
        systemNewses = new ArrayList<>();
        settingRefresh();
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsHasClassOrBaby();
            }
        });
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
                requsetData();

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
     * 设置
     */
    private void settingRefresh() {

        webListSwip.setPullRefreshEnable(true);
        webListSwip.setPullLoadEnable(true);
        webListSwip.setCustomHeaderView(new CustomHeader(this,2000));
        webListSwip.setAutoRefresh(false);
        webListSwip.setAutoLoadMore(false);
        webListSwip.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                beginid = 0;
                requsetData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webListSwip.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (getIntent().getStringExtra(LocalConstant.WEB_FLAG).equals(LocalConstant.SYSTEN_NEWS)) {
                    beginid = systemNewses.size();
                } else {
                    beginid = webListInfoArrayList.size();
                }
                requsetData();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        webListSwip.stopLoadMore();
                    }
                }, 2000);
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
            }
        });

        setTopName(getIntent().getStringExtra("title") != null ? getIntent().getStringExtra("title") : "列表");

        webList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                if (getIntent().getStringExtra(LocalConstant.WEB_FLAG).equals(LocalConstant.SYSTEN_NEWS)) {
                    bundle.putSerializable(LocalConstant.WEB_FLAG, systemNewses.get(position));
                    startActivity(SystemNewsDetailActivity.class, bundle);
                } else {
                    webListInfoArrayList.get(position).setWhere(getIntent().getStringExtra(LocalConstant.WEB_FLAG));
                    bundle.putSerializable(LocalConstant.WEB_FLAG, webListInfoArrayList.get(position));
                    startActivity(SchoolWebDetailActivity.class, bundle);
                }
            }
        });
    }
    @Override
    public void requsetData() {

        String schoolid = (String) SPUtils.get(this, LocalConstant.SCHOOL_ID, "1");

        String url = "";

        switch (getIntent().getStringExtra(LocalConstant.WEB_FLAG)) {
            case LocalConstant.WEB_INTROUCE:

                url = NetConstantUrl.GET_WEB_SCHOOL_INTROUCE + schoolid + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
            case LocalConstant.WEB_TEACHER:

                url = NetConstantUrl.GET_WEB_SCHOOL_TEACHER + schoolid + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
            case LocalConstant.WEB_STUDENT:
                getData();

                break;
            case LocalConstant.WEB_ACTIVITY:
                url = NetConstantUrl.GET_WEB_SCHOOL_ACTIVITY + schoolid + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
            case LocalConstant.WEB_NOTICE:
                url = NetConstantUrl.GET_WEB_SCHOOL_NOTICE + schoolid + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
            case LocalConstant.WEB_NEWS:
                url = NetConstantUrl.GET_WEB_SCHOOL_NEWS + schoolid + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
            case LocalConstant.SYSTEN_NEWS:
                url = NetConstantUrl.GET_WEB_SYSTEM_NEWS + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
            case LocalConstant.WEB_ABOUT:
                url = NetConstantUrl.GET_WEB_SYSTEM_NEWS + "&beginid=" + beginid;
                getWebData(url);
                settingRefresh();
                break;
        }
        //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getteacherinfos&schoolid=1    教师风采
        //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getbabyinfos&schoolid=1       学生秀场
        //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getWebSchoolInfos&schoolid=1  学校介绍
        //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getInterestclass&schoolid=1   精彩活动
        //系统消息


    }
    private void getWebData(String url){
        if (getIntent().getStringExtra(LocalConstant.WEB_FLAG).equals(LocalConstant.SYSTEN_NEWS)) {
            VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    webListSwip.stopLoadMore();
                    webListSwip.startRefresh();
                    if (JsonResult.JSONparser(WebListActivity.this, result)) {
                        systemNewses.clear();
                        systemNewses.addAll(getBeanFromJsonSystem(result));
                        setSystemAdapter();
                    } else {
                    }
                }

                @Override
                public void onError() {
                    webListSwip.stopLoadMore();
                    webListSwip.startRefresh();
                }
            });
        } else {
            VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    webListSwip.stopLoadMore();
                    webListSwip.startRefresh();
                    if (JsonResult.JSONparser(WebListActivity.this, result)) {
                        webListInfoArrayList.clear();
                        webListInfoArrayList.addAll(getBeanFromJson(result));
                        setAdapter();
                    } else {
                    }
                }

                @Override
                public void onError() {
                    webListSwip.stopLoadMore();
                    webListSwip.startRefresh();
                }
            });
        }
    }
    private void getData() {
        examinfos.clear();
        String url;
        if (type==0){
            url = NetConstantUrl.ParentGetExam + "&studentid=" + SPUtils.get(mContext, LocalConstant.USER_BABYID, "");
        }else {
            url = NetConstantUrl.TeacherGetExam + "&classid=" + SPUtils.get(mContext, LocalConstant.USER_CLASSID, "");
        }

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String data) {
                Log.d("onResponse", data);
                ProgressUtil.dissmisLoadingDialog();
                try {
                    ProgressUtil.dissmisLoadingDialog();
                    JSONObject jsonObject = new JSONObject(data);
                    String state = jsonObject.optString("status");
                    Log.d("attentdata", state);
                    if (state.equals("success")) {
                        String attendancedata = jsonObject.optString("data");
                        Log.d("attentdata", attendancedata);
                        Gson gson = new Gson();
                        ArrayList<Examinfo> arrayList = gson.fromJson(attendancedata, new TypeToken<List<Examinfo>>() {
                        }.getType());

                        examinfos.addAll(arrayList);
                        setsubjectAdapter();
                    } else {
                        ProgressUtil.dissmisLoadingDialog();
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
        mQueue.add(request);
        webList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,ExamDetialActivity.class);
                intent.putExtra("examid",examinfos.get(position).getId());
                intent.putExtra("title",examinfos.get(position).getExam_name());
                startActivity(intent);
            }
        });
    }

    private void checkIdentity() {
        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
            hideRightText();
        } else {

            type = 2;
        }

    }

    private void setsubjectAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<Examinfo>(this, examinfos, R.layout.item_web_list) {
                @Override
                public void convert(ViewHolder holder, Examinfo examinfo) {
                    holder.setText(R.id.post_title, examinfo.getExam_name())
                            .setText(R.id.post_content,"科目："+examinfo.getSubject().toString());

//                    if (webListInfo.getThumb().equals("")) {
//                        holder.getView(R.id.teacher_img).setVisibility(View.GONE);
//                    } else {
//                        holder.getView(R.id.teacher_img).setVisibility(View.VISIBLE);
//                        ImgLoadUtil.display(NetConstantUrl.WEB_IMAGE_URL + webListInfo.getThumb(), (ImageView) holder.getView(R.id.teacher_img));
//                    }
                }

            };
            webList.setAdapter(adapter);
        }
    }
    private void setAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<WebListInfo>(this, webListInfoArrayList, R.layout.item_web_list) {
                @Override
                public void convert(ViewHolder holder, WebListInfo webListInfo) {
                    LinearLayout linearLayout = (LinearLayout)holder.getView(R.id.web_ll);
                    linearLayout.setVisibility(View.VISIBLE);
                    TextView tv=holder.getView(R.id.post_title);
                    TextView tv2=holder.getView(R.id.post_content);
                    tv.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    holder.setText(R.id.post_titles, webListInfo.getPost_title())
                            .setText(R.id.post_contents, webListInfo.getPost_excerpt())
                            .setText(R.id.post_date, webListInfo.getPost_date());
                    if (webListInfo.getThumb().equals("")) {
                        holder.getView(R.id.teacher_img).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.teacher_img).setVisibility(View.VISIBLE);
                        ImgLoadUtil.display(NetConstantUrl.WEB_IMAGE_URL + webListInfo.getThumb(), (ImageView) holder.getView(R.id.teacher_img));
                    }
                }

            };
            webList.setAdapter(adapter);
        }
    }

    private void setSystemAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<SystemNews>(this, systemNewses, R.layout.item_web_list) {
                @Override
                public void convert(ViewHolder holder, SystemNews webListInfo) {
                    holder.setText(R.id.post_title, webListInfo.getPost_title())
                            .setText(R.id.post_content, webListInfo.getPost_excerpt())
                            .setText(R.id.post_date, webListInfo.getPost_date());
                    if (webListInfo.getThumb().equals("")) {
                        holder.getView(R.id.teacher_img).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.teacher_img).setVisibility(View.VISIBLE);
                        ImgLoadUtil.display(NetConstantUrl.WEB_IMAGE_URL + webListInfo.getThumb(), (ImageView) holder.getView(R.id.teacher_img));
                    }
                }

            };
            webList.setAdapter(adapter);
        }
    }


    /**
     * 字符串转模型
     *
     * @param result
     * @return
     */
    private List<WebListInfo> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<WebListInfo>>() {
        }.getType());
    }

    /**
     * 字符串转模型
     *
     * @param result
     * @return
     */
    private List<SystemNews> getBeanFromJsonSystem(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<SystemNews>>() {
        }.getType());
    }
}
