package cn.xiaocool.dezhischool.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xiaocool.dezhischool.R;


/**
 * Created by 10835 on 2018/2/28.
 */

public class LeaveStatusTextView extends android.support.v7.widget.AppCompatTextView{
    TextView mStatusText;
    public LeaveStatusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.simple_text,null);
        mStatusText = findViewById(R.id.text);

    }
    //public void set
}
