package cn.xiaocool.dezhischool.activity;

import android.os.Bundle;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class SystemNewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_news);
        setTopName("系统通知");
    }

    @Override
    public void requsetData() {

    }
}
