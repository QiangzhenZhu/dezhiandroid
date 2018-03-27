package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ProcessingInstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.MettingAdapter;
import cn.xiaocool.dezhischool.adapter.MettingLocationAdapter;
import cn.xiaocool.dezhischool.bean.MeetingLocation;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectorMettingLocationActivity extends BaseActivity implements TextView.OnEditorActionListener{
    private RecyclerView mRecycleView;
    private EditText mSearchView;
    private MettingLocationAdapter adapter;
    private List<MeetingLocation> locations = new ArrayList<>();
    private RelativeLayout mRightText;
    private String locationId;
    private String locationString;
    private Context mContext;
    private String keyword = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_metting_location);
        setTopName("选择会议室");
        mContext = this;
        mRightText = (RelativeLayout) setRightText("完成");
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLocationIsSelected()){
                    Toast.makeText(SelectorMettingLocationActivity.this,"请选择会议室",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.putExtra("location",locationId);
                intent.putExtra("locationName",locationString);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mRecycleView = findViewById(R.id.rv_location);
        mSearchView = findViewById(R.id.metting_search_location);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mSearchView = findViewById(R.id.metting_search_location);
        mSearchView.setOnEditorActionListener(this);


    }
    public void setOrnotifyAdapter(){
        if (adapter == null){
            adapter = new MettingLocationAdapter(locations);
            adapter.setOnItemClickerListner(new MettingLocationAdapter.OnItemClickListner() {
                @Override
                public void onItemOnClick(View view, int position) {
                    locations.get(position).setFlag(!(locations.get(position).isFlag()));
                    for (int i = 0; i <locations.size() ; i++) {
                        if (i != position){
                            locations.get(i).setFlag(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            mRecycleView.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public boolean checkLocationIsSelected(){
        for (int i = 0; i <locations.size() ; i++) {
            if (locations.get(i).isFlag()){
                locationId =locations.get(i).getId();
                locationString =locations.get(i).getTitle();
                return true;
            }
        }
        return false;
    }

    @Override
    public void requsetData() {
        String schoolid = (String) SPUtils.get(this, LocalConstant.SCHOOL_ID, "1");
        Uri uri = Uri.parse(NetConstantUrl.GET_MEETINGROOM_LIST)
                .buildUpon()
                .appendQueryParameter("schoolid",schoolid)
                .appendQueryParameter("keyword",keyword)
                .build();
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"数据获取失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call,  final Response response) throws IOException {
                String result = response.body().string();

                if (OkhttpUtil.isSuccess(result)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        final String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<MeetingLocation> list = new ArrayList<>();
                        list = gson.fromJson(data,new TypeToken<List<MeetingLocation>>(){}.getType());
                        locations.clear();
                        locations.addAll(list);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setOrnotifyAdapter();

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    final String re = result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String errorTitle = "";

                            try {
                                JSONObject object = new JSONObject(re);
                                errorTitle = object.getString("data");
                                locations.clear();
                                setOrnotifyAdapter();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(mContext,errorTitle,Toast.LENGTH_SHORT).show();
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
            keyword= mSearchView.getText().toString().trim();
            requsetData();

            return true;
        }
        return false;
    }
}
