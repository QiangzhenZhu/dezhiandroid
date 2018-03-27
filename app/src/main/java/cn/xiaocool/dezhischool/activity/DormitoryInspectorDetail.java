package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.DormitoryInspectInfo;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.ToastUtil;

public class DormitoryInspectorDetail extends BaseActivity  {


    @BindView(R.id.tv_yingdao_count)
    TextView tvYingdaoCount;
    @BindView(R.id.tv_shidao_count)
    TextView tvShidaoCount;
    @BindView(R.id.tv_qingjia_count)
    TextView tvQingjiaCount;
    @BindView(R.id.tv_queqin_count)
    TextView tvQueqinCount;
    private String dormitoryId;
    private Context mContext;
    private DormitoryInspectInfo inspectInfo;
    private String dormitoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_inspector_detail);
        mContext = this;
        ButterKnife.bind(this);
        setTopName("宿舍考勤统计");
        hideRightText();
        dormitoryId = getIntent().getStringExtra("dormitory_id");
        dormitoryName = getIntent().getStringExtra("dormitory_name");

    }

    @Override
    public void requsetData() {
        final String url = NetConstantUrl.GET_DORMITORY_COUNT + "&dormid=" + dormitoryId;
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if ("success".equals(status)) {
                        String data = object.getString("data");
                        inspectInfo = new Gson().fromJson(data, DormitoryInspectInfo.class);
                        tvYingdaoCount.setText(inspectInfo.getStudent_dorm_count() + "人");
                        tvShidaoCount.setText(inspectInfo.getActual_number() + "人");
                        tvQingjiaCount.setText(inspectInfo.getLeave_number() + "人");
                        tvQueqinCount.setText(inspectInfo.getCha_number() + "人");

                    } else {
                        ProgressUtil.dissmisLoadingDialog();
                        ToastUtil.show(mContext, "网络加载错误", Toast.LENGTH_SHORT);
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


    @OnClick({R.id.tv_yingdao_count, R.id.tv_shidao_count, R.id.tv_qingjia_count, R.id.tv_queqin_count})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.tv_yingdao_count:

                break;
            case R.id.tv_shidao_count:
                break;
            case R.id.tv_qingjia_count:
                break;
            case R.id.tv_queqin_count:
                intent = new Intent(mContext,AbsentDormitoryInfoActivity.class);
                intent.putExtra("titleString",dormitoryName+"缺勤"+inspectInfo.getCha_number()+"人");
                intent.putExtra("dormitoryid",dormitoryId);
                startActivity(intent);
                break;
        }
    }
}
