package cn.xiaocool.dezhischool.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;

public class PhotoActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView img;
String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
    }

    @Override
    public void requsetData() {
        url = getIntent().getStringExtra("photo");
        ImgLoadUtil.display( url, img);
    }
}
