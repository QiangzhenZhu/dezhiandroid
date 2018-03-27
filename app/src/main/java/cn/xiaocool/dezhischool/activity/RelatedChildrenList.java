package cn.xiaocool.dezhischool.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.WebHome;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RelatedChildrenList extends BaseActivity {

    private ListView mChildrenLiseView;
    private List<String> children = new ArrayList<>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_related_children_list);
        setTopName("选择请假人");
        hideRightText();
        mChildrenLiseView = (ListView) findViewById(R.id.children_list);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,children);
        mChildrenLiseView.setAdapter(adapter);

    }

    @Override
    public void requsetData() {

        String address = Uri.parse(WebHome.LEAVE_STU).buildUpon()
                .appendQueryParameter("userid", (String) SPUtils.get(mContext, LocalConstant.USER_ID,""))
                .build().toString();

        OkhttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }


}


