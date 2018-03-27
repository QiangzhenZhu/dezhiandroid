package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

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
import cn.xiaocool.dezhischool.adapter.AbsentDormitoryAdapter;
import cn.xiaocool.dezhischool.adapter.SpaceItemDecoration;
import cn.xiaocool.dezhischool.bean.AbsentDormitoryInfo;
import cn.xiaocool.dezhischool.bean.DormitoryInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;

public class AbsentDormitoryInfoActivity extends BaseActivity {

    @BindView(R.id.rv_dormitory_absent)
    XRecyclerView rvDormitoryAbsent;

    private AbsentDormitoryAdapter absentDormitoryAdapter;
    private List<AbsentDormitoryInfo> absentDormitoryInfos = new ArrayList<>();
    private Context mContext;
    private String dormitoryId;
    private String  beginid = "";
    private Boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_dormitory_info);
        ButterKnife.bind(this);
        mContext = this;
        setTopName(getIntent().getStringExtra("titleString"));
        hideRightText();
        dormitoryId = getIntent().getStringExtra("dormitoryid");
        setOrNotifyAdapter();
        //设置刷新适配器
        rvDormitoryAbsent.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                rvDormitoryAbsent.refreshComplete();
            }

            @Override
            public void onLoadMore() {
               beginid = absentDormitoryInfos.get(absentDormitoryInfos.size()-1).getId();
               isLoadMore = true;
               requsetData();
            }
        });

    }

    @Override
    public void requsetData() {
        ProgressUtil.showLoadingDialog(mContext);
        String url = "";
        if (isLoadMore) {
             url= NetConstantUrl.GET_DORMITORY_CHECK + "&dormid=" + dormitoryId + "&beginid=" + beginid;
        }else {
             url = NetConstantUrl.GET_DORMITORY_CHECK + "&dormid=" + dormitoryId;
        }
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if ("success".equals(status)) {
                        if (!isLoadMore) {
                            absentDormitoryInfos.clear();
                        }
                        String data = object.getString("data");
                        List<AbsentDormitoryInfo> infos = new Gson().fromJson(data,new TypeToken<List<AbsentDormitoryInfo>>(){}.getType());
                        absentDormitoryInfos.addAll(infos);
                        setOrNotifyAdapter();
                        rvDormitoryAbsent.refreshComplete();
                        rvDormitoryAbsent.loadMoreComplete();
                        isLoadMore = false;
                        ProgressUtil.dissmisLoadingDialog();

                    }else {
                        ProgressUtil.dissmisLoadingDialog();
                        ToastUtil.show(mContext,"网络加载错误", Toast.LENGTH_SHORT);
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

    public void setOrNotifyAdapter(){
        if (absentDormitoryAdapter == null){
            absentDormitoryAdapter = new AbsentDormitoryAdapter(absentDormitoryInfos);
            rvDormitoryAbsent.setLayoutManager(new LinearLayoutManager(mContext));
            rvDormitoryAbsent.addItemDecoration(new SpaceItemDecoration(15));
            rvDormitoryAbsent.setAdapter(absentDormitoryAdapter);
        }else {
            absentDormitoryAdapter.notifyDataSetChanged();
        }
    }
}
