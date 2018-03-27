package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.MyRelationShipAdapter;
import cn.xiaocool.dezhischool.adapter.SpaceItemDecoration;
import cn.xiaocool.dezhischool.bean.RelationShipInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyRelationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_tips)
    TextView tvTips;
    private RelativeLayout rlAddRelationShip;
    private Context mContext;
    private RecyclerView xRecyclerView;
    private MyRelationShipAdapter adapter;
    private List<RelationShipInfo> infos = new ArrayList<>();
    private boolean defaultFistRelationShip = false;
    private String setMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_relation);
        ButterKnife.bind(this);
        mContext = this;
        setTopName("我的亲属");
        rlAddRelationShip = (RelativeLayout) setRightImg(R.drawable.add_relationship);
        rlAddRelationShip.setOnClickListener(this);
        xRecyclerView = findViewById(R.id.rv_relationship_list);
        /*xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requsetData();
            }

            @Override
            public void onLoadMore() {

            }
        });*/
        /*xRecyclerView.setLoadingMoreEnabled(false);*/

    }

    @Override
    public void requsetData() {
        getInviteFamily();
        getAvailableRelationCount();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.top_right:
                intent = new Intent(mContext, AddRelationShipActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    public void setOrNotifyAdapter() {

        if (adapter == null) {
            xRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new MyRelationShipAdapter(infos, new MyRelationShipAdapter.onItemClickListner() {
                @Override
                public void onSetMain(int position, String userid) {

                    setMainParent(userid);

                }

                @Override
                public void onDelete(int position, String userid, int candelete) {

                    if (1 == candelete) {
                        Toast.makeText(mContext, "请先将其他联系人设为第一联系人，再执行删除操作", Toast.LENGTH_SHORT).show();
                    } else {
                        deleteFamily(userid);
                    }

                }
            });
            xRecyclerView.setAdapter(adapter);
            xRecyclerView.addItemDecoration(new SpaceItemDecoration(8));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void getAvailableRelationCount() {
        Uri uri = Uri.parse(NetConstantUrl.GET_AVALIABLE_RELATION_COUNT)
                .buildUpon()
                .appendQueryParameter("studentid", (String) SPUtils.get(mContext, LocalConstant.USER_BABYID, ""))
                .build();
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "网络加载错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        //xRecyclerView.refreshComplete();


                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject data = object.getJSONObject("data");
                        final String availablerelationcount = data.getString("availablerelationcount");
                        String can_add_relatives_number = data.getString("can_add_relatives_number");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTips.setText("剩余可修改次数："+availablerelationcount);

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            }
        });
    }

    public void getInviteFamily() {
        Uri uri = Uri.parse(NetConstantUrl.GET_INVITE_FAMILY)
                .buildUpon()
                .appendQueryParameter("studentid", (String) SPUtils.get(mContext, LocalConstant.USER_BABYID, ""))
                .build();
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "网络加载错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        //xRecyclerView.refreshComplete();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String data = object.getString("data");
                        List<RelationShipInfo> list = new Gson().fromJson(data, new TypeToken<List<RelationShipInfo>>() {
                        }.getType());
                        infos.clear();
                        infos.addAll(list);
                        Collections.reverse(infos);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setOrNotifyAdapter();
                                //xRecyclerView.refreshComplete();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            infos.clear();
                            setOrNotifyAdapter();
                            //xRecyclerView.refreshComplete();
                        }
                    });

                }
            }
        });
    }

    //删除联系人
    public void deleteFamily(String userid) {
        Uri uri = Uri.parse(NetConstantUrl.DELETE_FAMILY)
                .buildUpon()
                .appendQueryParameter("studentid", (String) SPUtils.get(mContext, LocalConstant.USER_BABYID, ""))
                .appendQueryParameter("userid", userid)
                .build();
        ProgressUtil.showLoadingDialog(mContext);
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            private String error = "";

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "网络加载错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        ProgressUtil.dissmisLoadingDialog();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        final String data = object.getString("data");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressUtil.dissmisLoadingDialog();
                                Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                                getInviteFamily();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);
                        String data = object.getString("data");
                        error = data;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();

                            ProgressUtil.dissmisLoadingDialog();
                        }
                    });

                }
            }
        });
    }

    //设置成第一联系人
    public void setMainParent(String first) {
        Uri uri = Uri.parse(NetConstantUrl.SET_MAIN_PARENT)
                .buildUpon()
                .appendQueryParameter("studentid", (String) SPUtils.get(mContext, LocalConstant.USER_BABYID, ""))
                .appendQueryParameter("userid", first)
                .build();
        ProgressUtil.showLoadingDialog(mContext);
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            private String error = "";

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "网络加载错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        ProgressUtil.dissmisLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        final String data = object.getString("data");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressUtil.dissmisLoadingDialog();
                                Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                                getInviteFamily();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);
                        String data = object.getString("data");
                        error = data;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();

                            ProgressUtil.dissmisLoadingDialog();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                defaultFistRelationShip = data.getBooleanExtra("DEFAULT_FIRST_RELATION_SHIP", false);
            }
        }
    }


}
