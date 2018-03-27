package cn.xiaocool.dezhischool.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.view.MaterialProgressDrawable;

/**
 * Created by 10835 on 2018/2/2.
 */

public class MettingInfoLabel extends ConstraintLayout {
    private TextView mStateTitle;
    private TextView mstartData;
    private TextView mStartTime;
    private ConstraintLayout constraintLayout;

    public MettingInfoLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.metting_info_label,this);
        mStateTitle = findViewById(R.id.textView23);
        mstartData  = findViewById(R.id.textView24);
        mStartTime  = findViewById(R.id.textView25);
        constraintLayout = findViewById(R.id.metting_label);

    }
    public void setmStateTitle(String text){
        mStateTitle.setText(text);
    }
    public void setMstartData(String text){
        mstartData.setText(text);
    }
    public void setmStartTime(String text){
        mStartTime.setText(text);
    }
    public void setColorLable(int color){
        constraintLayout.setBackgroundResource(color);
    }


}
