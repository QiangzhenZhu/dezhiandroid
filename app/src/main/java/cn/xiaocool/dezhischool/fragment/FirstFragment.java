package cn.xiaocool.dezhischool.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransitionImpl;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.AttanceActivity;
import cn.xiaocool.dezhischool.activity.ClassNewsActivity;
import cn.xiaocool.dezhischool.activity.DormitoryActivity;
import cn.xiaocool.dezhischool.activity.Main2Activity;
import cn.xiaocool.dezhischool.activity.MainActivity;
import cn.xiaocool.dezhischool.activity.MapActivity;
import cn.xiaocool.dezhischool.activity.SpaceClickClassActivity;
import cn.xiaocool.dezhischool.activity.SpaceClickHomeworkActivity;
import cn.xiaocool.dezhischool.activity.SpaceClickLeaveActivity;
import cn.xiaocool.dezhischool.activity.SpaceClickTeacherReviewActivity;
import cn.xiaocool.dezhischool.activity.TeacherReviewDetailActivity;
import cn.xiaocool.dezhischool.activity.WebListActivity;
import cn.xiaocool.dezhischool.activity.WebMetting;
import cn.xiaocool.dezhischool.adapter.GridIconAdapter;
import cn.xiaocool.dezhischool.bean.Classevents;
import cn.xiaocool.dezhischool.bean.GridIcon;
import cn.xiaocool.dezhischool.bean.WebListInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseFragment;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.view.CustomHeader;
import cn.xiaocool.dezhischool.view.NoScrollListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private MainActivity homeActivity;
    @BindView(R.id.first_top_name)
    TextView firstTopName;
    @BindView(R.id.announcement)
    RelativeLayout announcement;
    @BindView(R.id.web_gonggao_list)
    NoScrollListView webGonggaoList;
    @BindView(R.id.web_news_trends)
    RelativeLayout webNewsTrends;
    @BindView(R.id.web_news_list)
    NoScrollListView webNewsList;
    @BindView(R.id.slider)
    Banner slider;
    @BindView(R.id.school_news_srl)
    XRefreshView schoolNewsSrl;
    @BindView(R.id.web_button_grid_view)
    GridView webButtonGridView;
    private RelativeLayout mWebRlMap;
    private int tag = 0;
    private int type;
    //行为轨迹，会议考勤,宿舍考勤，门口考勤
    private String showbehaviour;
    private String showMeetingAndDormitoryAndDor;
    private Context mContext;
//    private ArrayList<WebListInfo> announceList;
//    private ArrayList<WebListInfo> newsList;
    private ArrayList<Classevents.PicBean> picBeans;
    private BaseAdapter adapter = null;
    private List<GridIcon> icons = new ArrayList<>();

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.fragment_first, container, false);
    }


    @Override
    protected void initEvent() {
        super.initEvent();
//        announceList = new ArrayList<>();
//        newsList = new ArrayList<>();
        picBeans = new ArrayList<>();
        //设置标题
        firstTopName.setText("校网");
        //设置下拉刷新
        settingRefresh();

    }

    /**
     * 设置
     */
    private void settingRefresh() {
        schoolNewsSrl.setPullRefreshEnable(true);
        schoolNewsSrl.setPullLoadEnable(false);
        schoolNewsSrl.setPinnedTime(2000);
        schoolNewsSrl.setCustomHeaderView(new CustomHeader(mActivity, 2000));
        schoolNewsSrl.setAutoRefresh(false);
        schoolNewsSrl.setAutoLoadMore(false);
        schoolNewsSrl.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
//                initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        schoolNewsSrl.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        schoolNewsSrl.stopLoadMore();
                    }
                }, 2000);
            }

        });

    }

    private void setLunBo() {
        HashMap<String, String> file_maps = new HashMap<String, String>();
        for (int i = 0; i < picBeans.size(); i++) {
            file_maps.put("" + i, NetConstantUrl.IMAGE_URL + picBeans.get(i).getPicture_url());
        }

        showViewPager(file_maps);
    }

    //轮播图片
    private void showViewPager(HashMap<String, String> file_maps) {
        ArrayList<String> images = new ArrayList<>();
        String[] titles = new String[file_maps.size()];
        int i = 0;
        for (String name : file_maps.keySet()) {
            images.add(file_maps.get(name));
            titles[i] = picBeans.get(i).getDescription();
            i++;
        }
        Banner banner = (Banner) getView().findViewById(R.id.slider);
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerTitles(Arrays.asList(titles));
        banner.isAutoPlay(true);
        banner.setDelayTime(2500);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }

    @Override
    public void initData() {
        String schoolid = (String) SPUtils.get(mActivity, LocalConstant.SCHOOL_ID, "1");

        //获取轮播图
        String slideUrl = NetConstantUrl.GET_SLIDER_URL + schoolid;
        VolleyUtil.VolleyGetRequest(mActivity, slideUrl, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(mActivity, result)) {
                    picBeans.clear();
                    picBeans.addAll(JsonParserPic(result));
                }
                setLunBo();
            }

            @Override
            public void onError() {

            }
        });

    }

    /**
     * 校园公告适配器
     */
//    private void setNewsAdapter() {
//        webNewsList.setAdapter(new WebMaxThreeAdapter(newsList, mActivity));
//        webNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, SchoolWebDetailActivity.class);
//                Bundle bundle = new Bundle();
//                newsList.get(position).setWhere(LocalConstant.WEB_NEWS);
//                bundle.putSerializable(LocalConstant.WEB_FLAG, newsList.get(position));
//                intent.putExtras(bundle);
//                mActivity.startActivity(intent);
//            }
//        });
//    }

    /**
     * 新闻动态适配器
     */
//    private void setAnnounceAdapter() {
//        webGonggaoList.setAdapter(new WebMaxThreeAdapter(announceList, mActivity));
//        webGonggaoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, SchoolWebDetailActivity.class);
//                Bundle bundle = new Bundle();
//                announceList.get(position).setWhere(LocalConstant.WEB_NOTICE);
//                bundle.putSerializable(LocalConstant.WEB_FLAG, announceList.get(position));
//                intent.putExtras(bundle);
//                mActivity.startActivity(intent);
//            }
//        });
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        showMeetingAndDormitoryAndDor = (String) SPUtils.get(container.getContext(),LocalConstant.USER_TYPE,"1");
        showbehaviour = (String) SPUtils.get(container.getContext(),LocalConstant.SHOW_BEHAVIOR_HISTORY,"1");
        if ("0".equals(showbehaviour)){
          /*  mWebRlMap = (RelativeLayout) rootView.findViewById(R.id.web_rl_map);
            mWebRlMap.setVisibility(View.INVISIBLE);
            mWebRlMap.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));*/

        }
        //会议 宿舍考勤
        if ("0".equals(showMeetingAndDormitoryAndDor)){
            /*webMetting.setVisibility(View.INVISIBLE);
            webDormitory.setVisibility(View.INVISIBLE);*/
        }
        initGridButton();
        adapter = new GridIconAdapter(mContext,R.layout.item_grid_icon,icons);
        webButtonGridView.setAdapter(adapter);
        webButtonGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridIcon gridIcon = icons.get(position);
                Intent intent;
                switch (gridIcon.getViewId()) {
                    case R.id.btn_project_studenthomework:
                        break;
                    case R.id.web_rl_schoolnotice://校园公告
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "校园公告");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_NOTICE);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_schoolnotice1://板报
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "板报");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_NEWS);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_classnotice://班级通知
                        startActivity(new Intent(mActivity, ClassNewsActivity.class));
                        break;
                    case R.id.web_project_homework://班级作业
                        startActivity(new Intent(mActivity, SpaceClickHomeworkActivity.class));
                        break;
                    case R.id.web_rl_clubactivity://社团活动
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "社团活动");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_ACTIVITY);
                        startActivity(intent);
                        break;

                    case R.id.web_rl_schoolevaluate://在校点评
                        checkIdentity();
                        if(type==1){
                            //获取今天的年月
                            Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH)+1;
                            Intent intentv = new Intent(mActivity, TeacherReviewDetailActivity.class);
                            intentv.putExtra("name", SPUtils.get(mActivity, LocalConstant.USER_NAME,"")+"");
                            intentv.putExtra("year",year+"");
                            intentv.putExtra("month",month+"");
                            intentv.putExtra("userid",SPUtils.get(mActivity, LocalConstant.USER_ID,"")+"");
                            intentv.putExtra("studentid", SPUtils.get(mActivity, LocalConstant.USER_BABYID,"")+"");
                            startActivity(intentv);
                        }else if(type==2){
                            startActivity(new Intent(mActivity,SpaceClickTeacherReviewActivity.class));
                        }
                        break;
                    case R.id.web_rl_schoolreport://成绩单
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "成绩单");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_STUDENT);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_alumniassociation://网络课堂
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "网络课堂");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_TEACHER);
                        startActivity(intent);
                        break;
                    case R.id.news_more://
                        intent = new Intent(mActivity, WebListActivity.class);
                        intent.putExtra("title", "新闻动态");
                        intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.WEB_NEWS);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_map://行为轨迹
                        intent = new Intent(mActivity, MapActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_classkaoqin://学生考勤
                        intent = new Intent(mActivity, AttanceActivity.class);
                        intent.putExtra("type",0);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_olineqinjia://请假申请
                        intent = new Intent(mActivity, SpaceClickLeaveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_classlesson://课程表
                        intent = new Intent(mActivity, SpaceClickClassActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_metting://会议考勤
                        intent = new Intent(mActivity, WebMetting.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_dormitory_attendance://宿舍考勤
                /*intent = new Intent(mActivity, AttanceActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);*/
                        intent = new Intent(mActivity,DormitoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.web_rl_school_attendance://门口考勤
                        intent = new Intent(mActivity, AttanceActivity.class);
                        intent.putExtra("type",1);
                        startActivity(intent);
                        break;

                    default:break;
                }

            }
        });
        return rootView;
    }
    private void checkIdentity() {
        if(SPUtils.get(mActivity, LocalConstant.USER_TYPE,"").equals("0")){
            type = 1;
        }else {
            type = 2;
        }
    }
    public List<Classevents.PicBean> JsonParserPic(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<Classevents.PicBean>>() {
        }.getType());
    }

    public List<WebListInfo> JsonParser(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<WebListInfo>>() {
        }.getType());
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class GlideImageLoader implements ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    public void initGridButton(){

        final GridIcon icon = new GridIcon(R.drawable.schoolnotice,"校园公告",R.id.web_rl_schoolnotice);
        icons.add(icon);
        GridIcon icon1 = new GridIcon(R.drawable.classnotice,"班级通知",R.id.web_rl_classnotice);
        icons.add(icon1);
        //对家长 隐藏门口考勤宿舍考勤
        if ("1".equals(showMeetingAndDormitoryAndDor)) {
            GridIcon icon2 = new GridIcon(R.mipmap.come_leave_school, "门口考勤", R.id.web_rl_school_attendance);
            icons.add(icon2);
            GridIcon icon3 = new GridIcon(R.mipmap.domitory_attendance, "宿舍考勤", R.id.web_rl_dormitory_attendance);
            icons.add(icon3);
        }
        //对老师隐藏 学生考勤
        if ("0".equals(showMeetingAndDormitoryAndDor)) {
            GridIcon icon4 = new GridIcon(R.drawable.studentattendance, "学生考勤", R.id.web_rl_classkaoqin);
            icons.add(icon4);
        }
        GridIcon icon5 = new GridIcon(R.drawable.askforleave,"请假申请",R.id.web_rl_olineqinjia);
        icons.add(icon5);
        GridIcon icon6 = new GridIcon(R.drawable.timetable,"课程表",R.id.web_rl_classlesson);
        icons.add(icon6);
        GridIcon icon7 = new GridIcon(R.drawable.studenthomework,"学生作业",R.id.web_project_homework);
        icons.add(icon7);
        GridIcon icon8 = new GridIcon(R.drawable.schoolreport,"成绩单",R.id.web_rl_schoolreport);
        icons.add(icon8);
        if (false) {//暂时隐藏这三项
            GridIcon icon9 = new GridIcon(R.drawable.schoolnewspaper, "板报", R.id.web_rl_schoolnotice1);
            icons.add(icon9);
            GridIcon icon10 = new GridIcon(R.drawable.clubactivity, "社团活动", R.id.web_rl_clubactivity);
            icons.add(icon10);
            GridIcon icon11 = new GridIcon(R.drawable.schoolnotice, "网络课堂", R.id.web_rl_alumniassociation);
            icons.add(icon11);
        }
        GridIcon icon12 = new GridIcon(R.drawable.schoolevaluate,"在校评价",R.id.web_rl_schoolevaluate);
        icons.add(icon12);
        if ("1".equals(showMeetingAndDormitoryAndDor)) {
            GridIcon icon13 = new GridIcon(R.mipmap.meeting_attendance, "会议考勤", R.id.web_rl_metting);
            icons.add(icon13);
        }
        if("1".equals(showbehaviour)) {
            GridIcon icon14 = new GridIcon(R.drawable.xingweiguiji, "行为轨迹", R.id.web_rl_map);
            icons.add(icon14);
        }
    }
}
