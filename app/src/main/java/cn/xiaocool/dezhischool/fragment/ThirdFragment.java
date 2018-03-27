package cn.xiaocool.dezhischool.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.ImageDetailActivity;
import cn.xiaocool.dezhischool.activity.PostTrendActivity;
import cn.xiaocool.dezhischool.activity.PostVideoActivity;
import cn.xiaocool.dezhischool.bean.ClassInfo;
import cn.xiaocool.dezhischool.bean.ClassParent;
import cn.xiaocool.dezhischool.bean.Trends;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseFragment;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.PopInputManager;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.CommentPopupWindow;
import cn.xiaocool.dezhischool.view.CustomHeader;
import cn.xiaocool.dezhischool.view.NiceDialog;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends BaseFragment {

    @BindView(R.id.fragment_third_iv_send)
    ImageView fragmentThirdIvSend;
    @BindView(R.id.fragment_third_lv_trend)
    ListView fragmentThirdLvTrend;
    @BindView(R.id.fragment_third_srl_trend)
    XRefreshView fragmentThirdSrlTrend;
    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.fragment_third_tv_change)
    TextView fragmentThirdTvChange;
    private Context context;
    private CommonAdapter adapter;
    private List<Trends> trendsList;
    private List<ClassInfo> classInfoList;// TODO: 16/11/5 save classList
    private CommentPopupWindow commentPopupWindow;
    private int type;
    private static long lastClickTime;
    private String userid, classid;
    private int beginId;
    private NiceDialog mDialog = null;
    private String delet_url;
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x110:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))) {
                            initData();
                            ToastUtil.showShort(context, "评论成功！");
                        }
                    }
                    break;
                case 0x111:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))) {
                            initData();
//                            ToastUtil.showShort(context, "取消点赞成功！");
                        }
                    }
                    break;
                case 0x112:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))) {
                            initData();
//                            ToastUtil.showShort(context, "点赞成功！");
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.fragment_third, container, false);
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void initData() {
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        String url = "";
        classid = SPUtils.get(context, LocalConstant.USER_CLASSID, "").toString();
        beginId = 0;
//        if (type == 1) {
//        }
//        if (classid.equals("")) {
//            return;
//        }
        url = NetConstantUrl.GET_TRENDS_PARENT + SPUtils.get(context, LocalConstant.SCHOOL_ID, "1")
                + setParams(userid, classid, beginId + "");

        VolleyUtil.VolleyGetRequest(context, url, new
                VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        ProgressUtil.dissmisLoadingDialog();
                        fragmentThirdSrlTrend.stopRefresh();
                        fragmentThirdSrlTrend.stopLoadMore();
                        if (JsonResult.JSONparser(context, result)) {
                            setAdapter(result);
                        } else {
                        }
                    }

                    @Override
                    public void onError() {
                        ProgressUtil.dissmisLoadingDialog();
                        fragmentThirdSrlTrend.stopRefresh();
                        fragmentThirdSrlTrend.stopLoadMore();
                    }
                });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        JCVideoPlayer.ACTION_BAR_EXIST = false;
        JCVideoPlayer.TOOL_BAR_EXIST = false;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        fragmentThirdSrlTrend.setPullRefreshEnable(true);
        fragmentThirdSrlTrend.setPullLoadEnable(true);
        fragmentThirdSrlTrend.setCustomHeaderView(new CustomHeader(mActivity,2000));
        fragmentThirdSrlTrend.setAutoRefresh(true);
        fragmentThirdSrlTrend.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentThirdSrlTrend.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loadTrend();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        fragmentThirdSrlTrend.stopLoadMore();
                    }
                }, 2000);
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
            }
        });
    }

    /**
     * 上拉加载数据
     */
    private void loadTrend() {
        String url = "";
        classid = SPUtils.get(context, LocalConstant.USER_CLASSID, "").toString();
        beginId = trendsList.size();
        url = NetConstantUrl.GET_TRENDS_PARENT + SPUtils.get(context, LocalConstant.SCHOOL_ID, "1")
                + setParams(userid, classid, beginId + "");

        VolleyUtil.VolleyGetRequest(context, url, new
                VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        fragmentThirdSrlTrend.stopRefresh();
                        fragmentThirdSrlTrend.stopLoadMore();
                        if (JsonResult.JSONparser(context, result)) {
                            setAdapter(result);
                        } else {
                        }
                    }

                    @Override
                    public void onError() {
                        fragmentThirdSrlTrend.stopRefresh();
                        fragmentThirdSrlTrend.stopLoadMore();
                    }
                });
    }


    private String setParams(String userid, String classid, String beginid) {
        return "&userid=" + userid + "&classid=" + classid + "&beginid=" + beginid;
    }

    /**
     * 设置删除Dialog
     */
    private void setVersionDialog( String url) {
        delet_url = NetConstantUrl.TREND_DELET + url;
        mDialog = new NiceDialog(context);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = width-300;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setTitle("提示");
        mDialog.setContent("确认删除吗?");
        mDialog.setOKButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyUtil.VolleyGetRequest(context, delet_url, new VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (JsonResult.JSONparser(context ,result)) {
                            initData();
                        }else {
                            ToastUtil.show(context , "失败" , Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onError() {
                        fragmentThirdSrlTrend.stopLoadMore();
                        fragmentThirdSrlTrend.stopRefresh();

                    }
                });
                mDialog.dismiss();
            }
        });
        mDialog.setCancelButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * list 设置adapter
     * @param result
     */
    private void setAdapter(String result) {
        trendsList.clear();
        trendsList.addAll(getBeanFromJson(result));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<Trends>(context, trendsList, R.layout.trend_item) {
                @Override
                public void convert(ViewHolder holder, Trends datas) {
                    setItem(holder, datas);
                }
            };
            fragmentThirdLvTrend.setAdapter(adapter);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        context = getActivity();
        checkIdentity();
        trendsList = new ArrayList<>();
        classInfoList = new ArrayList<>();

        userid = SPUtils.get(context, LocalConstant.USER_ID, "").toString();
        classid = SPUtils.get(context, LocalConstant.USER_CLASSID, "").toString();
        if (!classid.equals("")){
            fragmentThirdIvSend.setVisibility(View.VISIBLE);
            fragmentThirdTvChange.setVisibility(View.VISIBLE);
        }else {
//            ToastUtil.showShort(mActivity, "您没有可查看的班级动态!");
            fragmentThirdIvSend.setVisibility(View.GONE);
            fragmentThirdTvChange.setVisibility(View.GONE);
        }
        if (SPUtils.get(context, LocalConstant.USER_TYPE, "").toString().equals("0")){
            fragmentThirdTvChange.setVisibility(View.GONE);
        }
        return rootView;
    }


    /**
     * 字符串转模型
     *
     * @param result
     * @return
     */
    private List<ClassParent> getClassParentBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ClassParent>>() {
        }.getType());
    }

    /**
     * 判断身份
     * 1-----家长
     * 2-----老师
     * 3-----校长
     */
    private void checkIdentity() {
        if (SPUtils.get(context, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
            fragmentThirdTvChange.setVisibility(View.GONE);
        } else if (SPUtils.get(context, LocalConstant.USER_IS_PRINSIPLE, "").equals("y")) {
            type = 3;
            fragmentThirdTvChange.setVisibility(View.VISIBLE);
        } else {
            type = 2;
            fragmentThirdTvChange.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 对item 操作
     *
     * @param holder
     * @param datas
     */
    private void setItem(ViewHolder holder, final Trends datas) {

        //获取图片字符串数组
        final ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < datas.getPic().size(); i++) {
            images.add(datas.getPic().get(i).getPictureurl());
        }
    Log.d("dongtaiphoto",datas.getPhoto());
        holder.setImageByUrl(R.id.trend_item_iv_avatar, datas.getPhoto())
                .setText(R.id.trend_item_tv_content, datas.getContent())
                .setText(R.id.trend_item_tv_name, datas.getName())
                .setTimeText(R.id.trend_item_tv_time, datas.getWrite_time())
                .setText(R.id.trend_item_tv_praise, datas.getLike().size() + "")
                .setText(R.id.trend_item_tv_comment, datas.getComment().size() + "")
                .setItemImages(context, R.id.trend_item_iv_onepic, R.id.trend_item_gv_anypic, images);
        holder.getView(R.id.trend_item_iv_onepic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, ImageDetailActivity.class);
                intent.putStringArrayListExtra("Imgs", images);
                intent.putExtra("position", 0);
                intent.putExtra("type", "4");
                context.startActivity(intent);
            }
        });
        //显示评论
        ListView lv_comment = holder.getView(R.id.trend_item_lv_comment);
        lv_comment.setAdapter(new CommonAdapter<Trends.CommentBean>(context, datas.getComment(), R.layout.item_trend_comment) {
            @Override
            public void convert(ViewHolder holder, Trends.CommentBean commentBean) {
                holder.setImageByUrl(R.id.item_trend_comment_iv_avatar, commentBean.getAvatar())
                        .setText(R.id.item_trend_comment_tv_name, commentBean.getName())
                        .setText(R.id.item_trend_comment_tv_content, commentBean.getContent())
                        .setTimeText(R.id.item_trend_comment_tv_time, commentBean.getComment_time());
            }
        });
        //评论按钮
        holder.getView(R.id.trend_item_iv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPopupWindow = new CommentPopupWindow(context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_comment:
                                if (commentPopupWindow.ed_comment.getText().length() > 0) {
                                    new SendRequest(context, handler).send_remark(datas.getMid(), userid, commentPopupWindow.ed_comment.getText().toString(), "1", 0x110);
                                    commentPopupWindow.dismiss();
                                    commentPopupWindow.ed_comment.setText("");
                                } else {

                                    Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
                final EditText editText = commentPopupWindow.ed_comment;
                commentPopupWindow.showAtLocation(fragmentThirdSrlTrend, Gravity.BOTTOM, 0, 0);
                //弹出系统输入法
                PopInputManager.popInput(editText);
            }
        });
        //点赞
        holder.getView(R.id.trend_item_iv_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastClick()) {
                    return;
                } else {
                    if (isPraise(datas.getLike())) {
                        new SendRequest(context, handler).DelPraise(userid, datas.getMid(), 0x111);
                    } else {
                        new SendRequest(context, handler).Praise(userid, datas.getMid(), 0x112);
                    }
                }
            }
        });
            //判断本人是否已经点赞
            if (isPraise(datas.getLike())) {
                //点赞成功后图片变红
                holder.getView(R.id.trend_item_iv_praise).setSelected(true);
            } else {
            //取消点赞后
            holder.getView(R.id.trend_item_iv_praise).setSelected(false);
        }

        //判断是否本人
        if(SPUtils.get(context , LocalConstant.USER_ID ,"").toString().equals(datas.getUserid())){
            holder.setMyVisibility(R.id.item_sn_delet,1);
            holder.getView(R.id.item_sn_delet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setVersionDialog(datas.getMid());
                }
            });
        }else{
            holder.setMyVisibility(R.id.item_sn_delet,2);
        }

        JCVideoPlayerStandard jcVideoPlayer = holder.getView(R.id.videoplayer);
        if (datas.getVideo().equals("")||datas.getVideo_phone().equals("")){
            jcVideoPlayer.setVisibility(View.GONE);

        }else {
            jcVideoPlayer.setVisibility(View.VISIBLE);
            jcVideoPlayer.setUp(
                    NetConstantUrl.VIDEO_URL+datas.getVideo(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
            Picasso.with(context)
                    .load(NetConstantUrl.VIDEO_URL+datas.getVideo_phone())
                    .into(jcVideoPlayer.thumbImageView);
        }
    }


    /**
     * 字符串转模型
     * @param result
     * @return
     */
    private List<Trends> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<Trends>>() {
        }.getType());
    }


    /**
     * 防止快速点赞
     *
     * @return
     */
    public boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断当前用户是否点赞
     */
    private boolean isPraise(List<Trends.LikeBean> praises) {
        for (int i = 0; i < praises.size(); i++) {
            if (praises.get(i).getUserid().equals(userid)) {
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.fragment_third_tv_change, R.id.fragment_third_iv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_third_tv_change:
                checkIsHasClassOrBaby();// TODO: 16/11/5 判断是否有班级切换 or baby
                break;
            case R.id.fragment_third_iv_send:
                showPopupMenu();
                break;
        }
    }

    /**
     * 弹出选择班级的dialog
     */
    private void showChooseDialog() {
        ListView listView = new ListView(mActivity);
        listView.setDivider(null);
        listView.setAdapter(new ArrayAdapter<String>(mActivity, R.layout.item_choose_class, getClassStringData()));
        final MaterialDialog mMaterialDialog = new MaterialDialog(mActivity).setTitle("班级选择").setContentView(listView);
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mMaterialDialog.dismiss();
                SPUtils.put(mActivity, LocalConstant.USER_CLASSID, classInfoList.get(i).getId().toString());
                SPUtils.put(context, LocalConstant.CLASS_NAME, classInfoList.get(i).getClassname().toString());
                topName.setText(classInfoList.get(i).getClassname());
                ProgressUtil.showLoadingDialog(mActivity);
                initData();

            }
        });
        mMaterialDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        topName.setText(SPUtils.get(context, LocalConstant.CLASS_NAME, "动态").toString());
    }

    private ArrayList<String> getClassStringData() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < classInfoList.size(); i++) {
            strings.add(classInfoList.get(i).getClassname());
        }
        return strings;
    }

    /**
     * 判断是否有班级切换
     */
    private void checkIsHasClassOrBaby() {
        if (type != 1) {// TODO: 16/11/5 teacher get class
            ProgressUtil.showLoadingDialog(mActivity);
            String url = NetConstantUrl.TC_GET_CLASS + "&teacherid=" + SPUtils.get(mActivity, LocalConstant.USER_ID, "");
            VolleyUtil.VolleyGetRequest(mActivity, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    ProgressUtil.dissmisLoadingDialog();
                    if (JsonResult.JSONparser(mActivity, result)) {
                        classInfoList.clear();
                        classInfoList.addAll(getClassListBeans(result));
                        showChooseDialog();// TODO: 16/11/5 展示选择列表
                    } else {
                        ToastUtil.showShort(mActivity, "暂无可切换选项!");
                    }
                }

                @Override
                public void onError() {
                    ProgressUtil.dissmisLoadingDialog();
                }
            });

        } else {// TODO: 16/11/5 parent get baby class

        }

    }

    public List<ClassInfo> getClassListBeans(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ClassInfo>>() {
        }.getType());
    }

    /**
     *显示选择菜单
     * */
    private void showPopupMenu() {

        //自定义布局
        View layout = LayoutInflater.from(context).inflate(R.layout.address_add_menu, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置弹出位置
        int[] location = new int[2];
        getView().findViewById(R.id.fragment_third_iv_send).getLocationOnScreen(location);

        popupWindow.showAsDropDown(getView().findViewById(R.id.fragment_third_iv_send));

        final TextView add_qun = (TextView)layout.findViewById(R.id.add_qun);
        TextView tong_bu = (TextView)layout.findViewById(R.id.tong_bu);

        add_qun.setText("图文");
        tong_bu.setText("小视频");

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        mActivity.getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                mActivity.getWindow().setAttributes(lp);
            }
        });

        add_qun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history();
                popupWindow.dismiss();
            }
        });
        tong_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tongbu();
                popupWindow.dismiss();
            }
        });



    }

    /**
     * 视频
     * */
    private void tongbu() {
        Intent intent = new Intent(mActivity,PostVideoActivity.class);
        startActivity(intent);

    }

    /**
     * 图文
     * */
    private void history() {

        Intent intent = new Intent(mActivity,PostTrendActivity.class);
        startActivity(intent);

    }
}
