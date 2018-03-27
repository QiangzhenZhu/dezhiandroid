package cn.xiaocool.dezhischool.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.ClassScheduleExAdapter;
import cn.xiaocool.dezhischool.adapter.SelectClassAdapter;
import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.ClassList;
import cn.xiaocool.dezhischool.fragment.SpaceClickClassFragment;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;


/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickClassActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp_view)
    ViewPager vpView;
    private RelativeLayout up_jiantou;
    //    private ExpandableListView class_schedule_list;
    private ArrayList<ArrayList<String>> classlists;
    private ClassScheduleExAdapter classScheduleExAdapter;
    private RequestQueue mQueue;
    private Context mContext;
    private ArrayList<ClassList.ClassListData> arrayList;
    private TextView scca_top_title;
    private LinearLayout change_class;
    private int type;
    private List<String> mTitleList;//页卡标题集合
    private List<Fragment> fragments;
    MyPagerAdapter adapter;
    UpdataListener updataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_class);
        ButterKnife.bind(this);
        mContext = this;
        mQueue = Volley.newRequestQueue(mContext);
        arrayList = new ArrayList<>();
        hideTopView();
        checkIdentity();
        initView();

    }

    @Override
    public void requsetData() {

    }

    private void initView() {
        classlists = new ArrayList<>();
//        class_schedule_list = (ExpandableListView) findViewById(R.id.class_schedule_list);
//        class_schedule_list.setGroupIndicator(null);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        change_class = (LinearLayout) findViewById(R.id.change_class);
        scca_top_title = (TextView) findViewById(R.id.scca_top_title);
        change_class.setOnClickListener(this);
        //获取班级
        if (type == 1) {
            inittab();
        } else {
            volleyGetClassList();
        }

    }

    /**
     * 判断身份
     * 1-----家长
     * 2-----校长
     * 3-----班主任
     * 4-----校长+班主任
     */
    private void checkIdentity() {
        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
        } else {
            /*if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("y"))
                type = 4;
            if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("n"))
                type = 2;
            if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("n")&&SPUtils.get(context, LocalConstant.USER_IS_CLASSLEADER,"").equals("y"))
                type = 3;*/
            type = 2;
        }
    }

    private void volleyGetClassList() {
        ProgressUtil.showLoadingDialog((Activity) mContext);
//        ProgressUtil.showLoadingDialog((Activity) mContext);
        if (type == 1) {
//            requsetList(SPUtils.get(mContext, LocalConstant.USER_CLASSID, "") + "");
            ProgressUtil.dissmisLoadingDialog();
        } else {

            String URL = NetConstantUrl.NETBASEURL + "index.php?g=apps&m=teacher&a=getteacherclasslist&schoolid=" + SPUtils.get(mContext, LocalConstant.SCHOOL_ID, "") + "&teacherid=" + SPUtils.get(mContext, LocalConstant.USER_ID, "");
            Log.e("uuuurrrrll", URL);

            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String arg0) {
                    Log.d("onResponse", arg0);
                    JSONObject jsonObject = null;
                    ProgressUtil.dissmisLoadingDialog();
                    try {
                        jsonObject = new JSONObject(arg0);
                        String state = jsonObject.optString("status");
                        if (state.equals("success")) {
                            if (JsonResult.JSONparser(mContext, arg0)) {
                                JSONArray dataArray = jsonObject.optJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.optJSONObject(i);
                                    ClassList.ClassListData classListData = new ClassList.ClassListData();
                                    classListData.setClassid(dataObject.optString("classid"));
                                    classListData.setClassname(dataObject.optString("classname"));
                                    arrayList.add(classListData);
                                }

                                fillData();
                                inittab();

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    ToastUtil.showShort(mContext, arg0.toString());
                    ProgressUtil.dissmisLoadingDialog();
                    Log.d("onErrorResponse", arg0.toString());
                }
            });
            mQueue.add(request);

        }
    }

    private void fillData() {
        if (arrayList.size() > 0) {
            scca_top_title.setText(arrayList.get(0).getClassname());
//            requsetList(arrayList.get(0).getClassid());
            MyApplication.classid = arrayList.get(0).getClassid();
        } else {

        }

    }

    private void inittab() {

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
        for (int i = 0; i < mTitleList.size(); i++) {
            tabs.addTab(tabs.newTab().setText(mTitleList.get(i)));
        }

//        tabs.addTab(tabs.newTab().setText(mTitleList.get(3)),true);
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(0)));
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(1)));
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(2)));
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(3)), true);
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(4)));
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(5)));
//        tabs.addTab(tabs.newTab().setText(mTitleList.get(6)));


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
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, mTitleList);

//        MyPagerAdapter mAdapter = new MyPagerAdapter(fragments);
        //给ViewPager设置适配器
        vpView.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tabs.setupWithViewPager(vpView);
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("E");
        switch (format.format(date)) {
            case "周一":
                vpView.setCurrentItem(0);
                tabs.getTabAt(0).select();
                break;

            case "周二":
                vpView.setCurrentItem(1);
                tabs.getTabAt(1).select();
                break;
            case "周三":
                vpView.setCurrentItem(2);
                tabs.getTabAt(2).select();
                break;
            case "周四":
                vpView.setCurrentItem(3);
                tabs.getTabAt(3).select();
                break;
            case "周五":
                vpView.setCurrentItem(4);
                tabs.getTabAt(4).select();
                break;
            case "周六":
                vpView.setCurrentItem(5);
                tabs.getTabAt(5).select();
                break;
            case "周日":
                vpView.setCurrentItem(6);
                tabs.getTabAt(6).select();
                break;

        }

//        //给Tabs设置适配器
        tabs.setTabsFromPagerAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;

            case R.id.change_class:
                showPopupWindow();
                break;
        }

    }

    //显示下拉列表
    private void showPopupWindow() {
        /**
         *显示选择菜单
         * */
        View layout = LayoutInflater.from(mContext).inflate(R.layout.select_class_menu, null);
        ListView list = (ListView) layout.findViewById(R.id.select_class_list);
        final SelectClassAdapter adapter = new SelectClassAdapter(mContext, arrayList);
        list.setAdapter(adapter);
        //初始化popwindow
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        final PopupWindow popupWindow = new PopupWindow(layout, width, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置弹出位置
        int[] location = new int[2];
        up_jiantou.getLocationOnScreen(location);
        popupWindow.showAsDropDown(up_jiantou);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                scca_top_title.setText(arrayList.get(position).getClassname());
//                requsetList(arrayList.get(position).getClassid());
                MyApplication.classid = arrayList.get(position).getClassid();
                notifyUpdata(arrayList.get(position).getClassid());
                popupWindow.dismiss();


//                SpaceClickClassFragment.Refresh();


            }
        });
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
            return list_fragment.size();
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
//            return list_Title.get(position);
            return list_Title.get(position % list_Title.size());
        }
    }

    public interface UpdataListener {
        void onUpdata(String classId);
    }

    public void notifyUpdata(String classid) {
//        getSupportFragmentManager().getFragments()
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof UpdataListener) {
                ((UpdataListener) fragment).onUpdata(classid);
            }
        }
    }
}
