package cn.xiaocool.dezhischool.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xiaocool.dezhischool.R;

/**
 * Created by 10835 on 2018/1/30.
 */

public class TitleLayout extends LinearLayout {

    private ImageView mBack;
    private TextView mTitleText;
    private ImageView mRightImage;
    private TextView mRightText;

    public TitleLayout(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_layout,this);
        mBack = (ImageView) findViewById(R.id.title_layout_back);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mRightImage = (ImageView) findViewById(R.id.title_layout_rightImage);
        mRightText = (TextView) findViewById(R.id.title_layout_rightText);
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
            }
        });


    }


}
