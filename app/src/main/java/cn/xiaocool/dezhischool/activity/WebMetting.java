package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.callback.IFooterCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnEditorAction;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.MettingAdapter;
import cn.xiaocool.dezhischool.adapter.SpaceItemDecoration;
import cn.xiaocool.dezhischool.bean.MettingInfo;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WebMetting extends BaseActivity implements TextView.OnEditorActionListener {
    private static final String TAG = "WebMetting";
    private View mCreateMetting;
    private EditText mMettingSearch;
    private XRecyclerView mMettingRecycleView;
    private MettingAdapter adapter;
    private ImageView ivLoading;
    Context context;
    private List<MettingInfo> mInfos;
    private UserInfo mUserInfo;
    private SwitchCompat switchCompat;
    private boolean flag;
    private String keyWord =  "";
    private String error = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_metting);
        flag = false;
        context = this;
        mInfos = new ArrayList<>();
        hideRightText();
        setTopName("会议");
        ivLoading = findViewById(R.id.iv_loading);
        ivLoading.setVisibility(View.VISIBLE);
        mMettingSearch = findViewById(R.id.metting_search);
        mMettingSearch.setOnEditorActionListener(this);
        mCreateMetting = setRightImg(R.mipmap.create_meeting);
        mCreateMetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebMetting.this,CreateWebMetting.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        switchCompat = (SwitchCompat) findViewById(R.id.switch1);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    switchCompat.setText("我管理的");
                    flag = true;
                    requsetData();
                }else {
                    switchCompat.setText("我参与的");
                    flag =false;
                    requsetData();
                }
            }
        });
        mMettingRecycleView = findViewById(R.id.rv_meeting);
        mMettingRecycleView.setLoadingMoreEnabled(false);
        mMettingRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requsetData();
            }

            @Override
            public void onLoadMore() {

            }
        });



    }
    public void setOrnotifyAdapter(){
        if (adapter == null){
            mMettingRecycleView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new MettingAdapter(mInfos);
            mMettingRecycleView.addItemDecoration(new SpaceItemDecoration(16));
            mMettingRecycleView.setAdapter(adapter);
        } else {
          adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void requsetData() {
        String schoolid = (String) SPUtils.get(this, LocalConstant.SCHOOL_ID, "1");
        String userid = (String) SPUtils.get(this, LocalConstant.USER_ID,"");
        Uri uri;
        if (flag){
            uri = Uri.parse(NetConstantUrl.GET_MY_MANAGE_CONFERENCE_LIST_BY_USER_ID)
                    .buildUpon()
                    .appendQueryParameter("schoolid", schoolid)
                    .appendQueryParameter("userid",userid)
                    .appendQueryParameter("keyword",keyWord)
                    .build();
        }else {
            uri = Uri.parse(NetConstantUrl.GET_CONFERENCE_LIST_BY_SCHOOL_ID)
                    .buildUpon()
                    .appendQueryParameter("schoolid", schoolid)
                    .appendQueryParameter("keyword",keyWord)
                    .build();
        }
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"数据获取失败",Toast.LENGTH_SHORT).show();
                        mInfos.clear();
                        setOrnotifyAdapter();

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                boolean isSuccess = OkhttpUtil.isSuccess(result);

                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();

                        List<MettingInfo> tempInfo = gson.fromJson(data,new TypeToken<List<MettingInfo>>(){}.getType());
                        mInfos.clear();
                        mInfos.addAll(tempInfo);
                        runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               setOrnotifyAdapter();
                               mMettingRecycleView.refreshComplete();
                               ivLoading.setVisibility(View.GONE);
                           }
                        });

                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ",e );
                        ivLoading.setVisibility(View.GONE);
                    }

                }else {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        error = jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivLoading.setVisibility(View.GONE);
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                            mInfos.clear();
                            setOrnotifyAdapter();
                        }
                    });

                }

            }
        });
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
            keyWord = mMettingSearch.getText().toString().trim();
            requsetData();

            return true;
        }
        return false;
    }
}
