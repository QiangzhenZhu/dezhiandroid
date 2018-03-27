package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.fragment.ParentFragment;
import cn.xiaocool.dezhischool.fragment.TeacherFragment;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.SPUtils;

public class AddressActivity extends BaseActivity {
    @BindView(R.id.activity_address_tab)
    PagerSlidingTabStrip activityAddressTab;
    @BindView(R.id.activity_address_viewPager)
    ViewPager activityAddressViewPager;
    private Context context;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        context = this;
        setTopName("通讯录");
        setFragment();
    }

    /**
     * 设置fragment
     */
    private void setFragment() {
        if(SPUtils.get(context, LocalConstant.USER_TYPE,"").equals("0")){
            fragments = new ArrayList<>();
            TeacherFragment teacherFragment = new TeacherFragment();
            fragments.add(teacherFragment);
            activityAddressViewPager.setAdapter(new MyParentAdapter(getSupportFragmentManager()));
            activityAddressTab.setViewPager(activityAddressViewPager);
        }else{
            fragments = new ArrayList<>();
            TeacherFragment teacherFragment = new TeacherFragment();
            ParentFragment parentFragment = new ParentFragment();
            fragments.add(teacherFragment);
            fragments.add(parentFragment);
            activityAddressViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
            activityAddressTab.setViewPager(activityAddressViewPager);
        }
    }

    @Override
    public void requsetData() {

    }

    /**
     * viewpager适配器
     */
    private class MyAdapter extends FragmentPagerAdapter {

        private String[] titles = {"老师", "家长"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * viewpager适配器
     */
    private class MyParentAdapter extends FragmentPagerAdapter {

        private String[] titles = {"老师"};

        public MyParentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
