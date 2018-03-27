package cn.xiaocool.dezhischool.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondParentFragment extends BaseFragment {


    @BindView(R.id.second_top_name)
    TextView secondTopName;
    @BindView(R.id.activity_address_tab)
    PagerSlidingTabStrip activityAddressTab;
    @BindView(R.id.activity_address_viewPager)
    ViewPager activityAddressViewPager;

    public int pushtype = 0;

    private Context context;
    private ArrayList<Fragment> fragments;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.activity_message_parent, container, false);
    }


    @Override
    public void initData() {
        secondTopName.setText("消息");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        context = getActivity();
        setFragment();
        return rootView;
    }

    /**
     * 设置fragment
     */
    private void setFragment() {
        fragments = new ArrayList<>();
        SchoolNewsFragment schoolNewsFragment = new SchoolNewsFragment();
        ClassNewsFragment classNewsFragment = new ClassNewsFragment();
        fragments.add(classNewsFragment);
        fragments.add(schoolNewsFragment);
        activityAddressViewPager.setAdapter(new MyAdapter(mActivity.getSupportFragmentManager()));
        activityAddressTab.setViewPager(activityAddressViewPager);
    }

    /**
     * viewpager适配器
     */
    private class MyAdapter extends FragmentPagerAdapter {

        private String[] titles = {"班级消息", "学校消息"};

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

    @Override
    public void onResume() {
        super.onResume();
        activityAddressViewPager.setCurrentItem(pushtype);
        pushtype = 0;
    }
}
