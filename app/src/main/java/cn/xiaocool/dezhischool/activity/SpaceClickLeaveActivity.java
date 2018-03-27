package cn.xiaocool.dezhischool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.LeaveApplicationAdapter;
import cn.xiaocool.dezhischool.adapter.SpaceItemDecoration;
import cn.xiaocool.dezhischool.bean.BabyInfo;
import cn.xiaocool.dezhischool.bean.SingleBabyInfos;
import cn.xiaocool.dezhischool.bean.leaveDetail;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;


/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickLeaveActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_exit)
    ImageView btnExit;
    @BindView(R.id.up_jiantou)
    RelativeLayout upJiantou;
    @BindView(R.id.leave_add)
    ImageView leaveAdd;
    @BindView(R.id.rl_addleave)
    RelativeLayout rlAddleave;
    @BindView(R.id.rv_leave_application)
    XRecyclerView rvLeaveApplication;
    private LeaveApplicationAdapter adapter;
    private Context mContext;
    private List<leaveDetail> detailList;
    private String usertype;
    private RelativeLayout leave_add;
    private RelativeLayout back;
    private List<BabyInfo> mBabyInfos;
    private String type;
    private static final String TAG = "SpaceClickLeaveActivity";
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_open_leave);
        ButterKnife.bind(this);
        mContext = this;
        //ProgressUtil.showLoadingDialog(this);
        setTopName("在线请假");
        userType = (String) SPUtils.get(mContext,LocalConstant.USER_TYPE,"");
        if ("1".equals(userType)){
            rlAddleave.setVisibility(View.GONE);
        }
        detailList = new ArrayList<>();
        rvLeaveApplication.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requsetData();
            }

            @Override
            public void onLoadMore() {
                rvLeaveApplication.loadMoreComplete();
            }
        });
        hideTopView();
        type = (String) SPUtils.get(mContext, LocalConstant.USER_TYPE, "");
        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setOrNotifyData();
    }

    @Override
    public void requsetData() {
        String url = "";

        if ("1".equals(type)) {
            url = NetConstantUrl.CLASS_QINGJIA + "&teacherid=" + SPUtils.get(mContext, LocalConstant.USER_ID, "");
        } else {
            url = NetConstantUrl.STUDENT_QINGJIA + "&studentid=" + SPUtils.get(mContext, LocalConstant.USER_BABYID, "");
        }
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    rvLeaveApplication.refreshComplete();
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    List<leaveDetail> details = new Gson().fromJson(data,new TypeToken<List<leaveDetail>>(){}.getType());
                    Log.d(TAG, "onResume: ");
                    detailList.clear();
                    detailList.addAll(details);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setOrNotifyData();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError() {

            }
        });


    }




    public void setOrNotifyData(){
        if (adapter == null){
            adapter = new LeaveApplicationAdapter(detailList,type);
            rvLeaveApplication.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
            Log.d(TAG, "setOrNotifyData: ");
        }
    }
    public void init() {
        upJiantou.setOnClickListener(this);
        rlAddleave.setOnClickListener(this);
        rvLeaveApplication.setLayoutManager(new LinearLayoutManager(mContext));
        rvLeaveApplication.addItemDecoration(new SpaceItemDecoration(16));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                ((Activity)mContext).finish();
                break;
            case R.id.rl_addleave:
                Intent intent = new Intent(this,Leave_add_activity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }
}


