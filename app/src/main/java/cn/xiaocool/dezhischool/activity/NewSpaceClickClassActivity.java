package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.fragment.SpaceClickClassFragment;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class NewSpaceClickClassActivity extends BaseActivity {

    @BindView(R.id.scca_top_title)
    TextView sccaTopTitle;
    @BindView(R.id.change_class)
    LinearLayout changeClass;
    @BindView(R.id.btn_exit)
    ImageView btnExit;
    @BindView(R.id.up_jiantou)
    RelativeLayout upJiantou;
    @BindView(R.id.location_pop)
    View locationPop;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp_view)
    ViewPager vpView;
    private RequestQueue mQueue;
    private List<String> mTitleList;//页卡标题集合
    private List<Fragment> fragments;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_space_click_class);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mContext = this;
        mTitleList = new ArrayList<>();
        mTitleList.add("周一");
        mTitleList.add("周二");
        mTitleList.add("周三");
        mTitleList.add("周四");
        mTitleList.add("周五");
        mTitleList.add("周六");
        mTitleList.add("周天");

        //添加tab选项卡，默认第一个选中
        tabs.addTab(tabs.newTab().setText(mTitleList.get(0)), true);
        tabs.addTab(tabs.newTab().setText(mTitleList.get(1)));

        SpaceClickClassFragment fragment1 = SpaceClickClassFragment.newInstance("1");
        SpaceClickClassFragment fragment2 = SpaceClickClassFragment.newInstance("2");
        SpaceClickClassFragment fragment3 = SpaceClickClassFragment.newInstance("3");
        SpaceClickClassFragment fragment4 = SpaceClickClassFragment.newInstance("4");
        SpaceClickClassFragment fragment5 = SpaceClickClassFragment.newInstance("5");
        SpaceClickClassFragment fragment6 = SpaceClickClassFragment.newInstance("6");
        SpaceClickClassFragment fragment7 = SpaceClickClassFragment.newInstance("7");

        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        fragments.add(fragment6);
        fragments.add(fragment7);

        //设置TabLayout的模式
        tabs.setTabMode(TabLayout.MODE_FIXED);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, mTitleList);
//        MyPagerAdapter mAdapter = new MyPagerAdapter(fragments);
        //给ViewPager设置适配器
        vpView.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tabs.setupWithViewPager(vpView);
//        //给Tabs设置适配器
        tabs.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public void requsetData() {

    }
    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list_fragment;                         //fragment列表
        private List<String> list_Title;                              //tab名的列表


        public MyPagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
            super(fm);
            this.list_fragment = list_fragment;
            this.list_Title = list_Title;
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_Title.size();
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {

            return list_Title.get(position % list_Title.size());
        }
    }
}
