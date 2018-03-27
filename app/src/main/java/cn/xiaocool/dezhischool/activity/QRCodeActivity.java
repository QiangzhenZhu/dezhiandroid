package cn.xiaocool.dezhischool.activity;

import android.os.Bundle;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class QRCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        setTopName("二维码");
    }

    @Override
    public void requsetData() {

    }
}
