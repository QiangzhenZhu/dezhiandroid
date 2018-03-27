package cn.xiaocool.dezhischool.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.fragment.ClassAllFragment;


public class SpaceClassAttendanceActivity extends Activity implements View.OnClickListener{
    private RelativeLayout up_jiantou;
    private RelativeLayout[] mTabs;
    private Fragment[] fragments;
    private int index;
    private int currentIndex;
//    private ClassLeaveFragment collectFinishedFragment;
    private ClassAllFragment collectPendingFragment;
    private FragmentManager fragmentManager;
    private ImageView add_collect;
    private View location_pop;
//    private SegmentControl mSegmentHorzontal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_class_attendance);
        initView();

        collectPendingFragment = new ClassAllFragment();
//        collectFinishedFragment = new ClassLeaveFragment();

        //装实例化好的fragment的数组
        fragments = new Fragment[]{collectPendingFragment};
        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_content, collectPendingFragment);
        transaction.commit();
        fragmentManager = getFragmentManager();
    }

    private void initView() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
//        buttons = new Button[3];
//        buttons[0] = (Button) findViewById(R.id.btn_suspend);
//        buttons[1] = (Button) findViewById(R.id.btn_finished);
//        buttons[2] = (Button) findViewById(R.id.btn_expired);
//        buttons[0].setOnClickListener(this);
//        buttons[1].setOnClickListener(this);
//        buttons[2].setOnClickListener(this);
//        buttons[0].setSelected(true);
        location_pop = findViewById(R.id.location_pop);

        mTabs = new RelativeLayout[4];
//        mTabs[0] = (RelativeLayout) findViewById(R.id.address_parent);
//        mTabs[0].setOnClickListener(this);
//        mTabs[1] = (RelativeLayout)findViewById(R.id.address_gardener);
//        mTabs[1].setOnClickListener(this);

        //设置第一个按钮为选中状态
//        mTabs[0].setSelected(true);

//        mSegmentHorzontal = (SegmentControl) findViewById(R.id.segment_control);
//        mSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
//            @Override
//            public void onSegmentControlClick(int index) {
//                Log.i("Tag", "onSegmentControlClick: index = " + index);
//                if (currentIndex != index){
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    transaction.hide(fragments[currentIndex]);
//                    if (!fragments[index].isAdded()){
//                        transaction.add(R.id.fragment_content,fragments[index]);
//
//                    }
//                    transaction.show(fragments[index]);
//                    transaction.commit();
//                }
////                mTabs[currentIndex].setSelected(false);
////                mTabs[index].setSelected(true);
//                currentIndex = index;
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;


        }

    }
}
