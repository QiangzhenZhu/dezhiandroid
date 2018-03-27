package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
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
import cn.xiaocool.dezhischool.adapter.DormitoryListAdapter;
import cn.xiaocool.dezhischool.adapter.SpaceItemDecoration;
import cn.xiaocool.dezhischool.bean.DormitoryInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;

public class DormitoryActivity extends BaseActivity {
    XRecyclerView rvDormitoryList;
    private Context mContext;

    private List<DormitoryInfo> dormitoryInfos = new ArrayList<>();

    private DormitoryListAdapter dormitoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory);
        rvDormitoryList =  findViewById(R.id.rv_dormitory_list);
        mContext = this;
        setTopName("宿舍考勤");
        hideRightText();
        rvDormitoryList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requsetData();
            }

            @Override
            public void onLoadMore() {
                rvDormitoryList.loadMoreComplete();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOrNotifyAdapter();

    }
    public void setOrNotifyAdapter(){
        if (dormitoryListAdapter == null){
            dormitoryListAdapter = new DormitoryListAdapter(dormitoryInfos);
            rvDormitoryList.setLayoutManager(new LinearLayoutManager(mContext));
            rvDormitoryList.addItemDecoration(new SpaceItemDecoration(15));
            rvDormitoryList.setAdapter(dormitoryListAdapter);
        }else {
            dormitoryListAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void requsetData() {
            final String url = NetConstantUrl.GET_DORMITORY_LIST+ "&teacherid="+ SPUtils.get(mContext, LocalConstant.USER_ID,"");
            VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        if ("success".equals(status)) {
                            String data = object.getString("data");
                            dormitoryInfos.clear();
                            List<DormitoryInfo> infos = new Gson().fromJson(data,new TypeToken<List<DormitoryInfo>>(){}.getType());
                            dormitoryInfos.addAll(infos);
                            setOrNotifyAdapter();
                            rvDormitoryList.refreshComplete();

                        }else {
                            ProgressUtil.dissmisLoadingDialog();
                            ToastUtil.show(mContext,"网络加载错误",Toast.LENGTH_SHORT);
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
}
