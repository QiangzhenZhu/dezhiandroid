package cn.xiaocool.dezhischool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class WebKeChengActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_ke_cheng);
        ButterKnife.bind(this);
        setTopName("学校课程");
    }

    @Override
    public void requsetData() {

    }

    @OnClick({R.id.web_kecheng_main, R.id.kecheng_xuanxiu})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.web_kecheng_main:
                intent = new Intent(this, WebOtherListActivity.class);
                intent.putExtra("title","校本课程");
                startActivity(intent);
                break;
            case R.id.kecheng_xuanxiu:
                intent = new Intent(this, WebOtherListActivity.class);
                intent.putExtra("title","选修课程");
                startActivity(intent);
                break;
        }
    }
}
