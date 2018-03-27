package cn.xiaocool.dezhischool.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.AddressActivity;
import cn.xiaocool.dezhischool.activity.MyIntegrationActivity;
import cn.xiaocool.dezhischool.activity.MyRelationActivity;
import cn.xiaocool.dezhischool.activity.PersonalInfoActivity;
import cn.xiaocool.dezhischool.activity.QRCodeActivity;
import cn.xiaocool.dezhischool.activity.SettingActivity;
import cn.xiaocool.dezhischool.activity.WebListActivity;
import cn.xiaocool.dezhischool.bean.BabyInfo;
import cn.xiaocool.dezhischool.bean.SingleBabyInfos;
import cn.xiaocool.dezhischool.bean.UserInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseFragment;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.view.RoundImageView;
import me.drakeet.materialdialog.MaterialDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment extends BaseFragment {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.fragment_four_iv_setting)
    ImageView fragmentFourIvSetting;
    @BindView(R.id.top_bar)
    RelativeLayout topBar;
    @BindView(R.id.fragment_four_iv_avatar)
    RoundImageView fragmentFourIvAvatar;
    @BindView(R.id.fragment_four_tv_name)
    TextView fragmentFourTvName;
    @BindView(R.id.fragment_four_rl_service)
    RelativeLayout fragmentFourRlService;
    @BindView(R.id.fragment_four_rl_address)
    RelativeLayout fragmentFourRlAddress;
    @BindView(R.id.fragment_four_rl_jifen)
    RelativeLayout fragmentFourRlJifen;
    @BindView(R.id.fragment_four_rl_system)
    RelativeLayout fragmentFourRlSystem;
    @BindView(R.id.fragment_four_rl_online)
    RelativeLayout fragmentFourRlOnline;
    @BindView(R.id.fragment_four_rl_code)
    RelativeLayout fragmentFourRlCode;
    @BindView(R.id.fragment_four_tv_change)
    TextView fragmentFourTvChange;
    @BindView(R.id.fragment_four_rl_changebaby)
    RelativeLayout fragmentFourRlChangebaby;
    @BindView(R.id.tv_relative_baby)
    TextView tvRelativeBaby;
    @BindView(R.id.fragment_four_rl_relation_ship)
    RelativeLayout fragmentFourRlRelationShip;
    private Context context;
    private UserInfo userInfo;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_four, container, false);
    }


    @Override
    public void initData() {
        String url = NetConstantUrl.GET_USER_INFO + "&userid=" + SPUtils.get(context, LocalConstant.USER_ID, "");
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    showUserInfo(result);
                }
            }

            @Override
            public void onError() {

            }

        });

        tvRelativeBaby.setText((CharSequence) SPUtils.get(context, LocalConstant.USER_BABYNAME, ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        if (SPUtils.get(context, LocalConstant.USER_TYPE, "").equals("1")) {
            fragmentFourRlChangebaby.setVisibility(View.GONE);//老师
            fragmentFourRlRelationShip.setVisibility(View.GONE);

        } else {
            getBabyInfo();//请求孩子信息

        }
        //隐藏左上方的切换按钮
        fragmentFourTvChange.setVisibility(View.GONE);
    }

    @OnClick({R.id.fragment_four_iv_setting, R.id.fragment_four_iv_avatar, R.id.fragment_four_rl_service, R.id.fragment_four_rl_address, R.id.fragment_four_rl_jifen, R.id.fragment_four_rl_system, R.id.fragment_four_rl_online, R.id.fragment_four_rl_code, R.id.fragment_four_rl_changebaby,R.id.fragment_four_rl_relation_ship})
    public void onClick(View view) {
        switch (view.getId()) {
            //设置
            case R.id.fragment_four_iv_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
            //个人信息
            case R.id.fragment_four_iv_avatar:
                context.startActivity(new Intent(context, PersonalInfoActivity.class));
                break;
            //服务购买
            case R.id.fragment_four_rl_service:
                ToastUtil.showShort(context, "当前版本为试用版本");
                //context.startActivity(new Intent(context, ServiceBuyActivity.class));
                break;
            //通讯录
            case R.id.fragment_four_rl_address:
                context.startActivity(new Intent(context, AddressActivity.class));
                break;
            //我的积分
            case R.id.fragment_four_rl_jifen:
                context.startActivity(new Intent(context, MyIntegrationActivity.class));
                break;
            //系统通知
            case R.id.fragment_four_rl_system:
                Intent intent = new Intent(mActivity, WebListActivity.class);
                intent.putExtra("title", "系统通知");
                intent.putExtra(LocalConstant.WEB_FLAG, LocalConstant.SYSTEN_NEWS);
                startActivity(intent);
                break;
            //在线客服
            case R.id.fragment_four_rl_online:
//                context.startActivity(new Intent(context, OnlineCommentActivity.class));
                Intent intentcall = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "0319-5255525");
                intentcall.setData(data);
                startActivity(intentcall);
                break;
            //客户端二维码
            case R.id.fragment_four_rl_code:
                context.startActivity(new Intent(context, QRCodeActivity.class));
                break;
            case R.id.fragment_four_rl_changebaby:// TODO: 16/11/7 家长切换孩子
                showChangeBaby();
                break;
            case R.id.fragment_four_rl_relation_ship:
                Intent relationShipIntent = new Intent(context, MyRelationActivity.class);
                startActivity(relationShipIntent);
                break;
        }
    }

    /**
     * 显示个人信息
     *
     * @param result
     */
    private void showUserInfo(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userInfo = new Gson().fromJson(data, new TypeToken<UserInfo>() {
        }.getType());
        fragmentFourTvName.setText(userInfo.getName());
        ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + userInfo.getPhoto(), fragmentFourIvAvatar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //null.unbind();


    }

    /**
     * 弹出选择孩子的dialog
     */
    private void showChangeBaby() {
        final List<String> babayList = new ArrayList<>();
        for (BabyInfo info : SingleBabyInfos.getmBabyInfos().getInfos()
                ) {
            babayList.add(info.getStudentname());
        }
        ListView listView = new ListView(context);
        listView.setDivider(null);
        listView.setAdapter(new ArrayAdapter<String>(context, R.layout.item_choose_class, babayList));
        final MaterialDialog mMaterialDialog = new MaterialDialog(context).setTitle("请选择").setContentView(listView);
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
                /*SPUtils.put(mContext, LocalConstant.USER_CLASSID, classInfoList.get(i).getId().toString());
                SPUtils.put(mContext, LocalConstant.CLASS_NAME, classInfoList.get(i).getClassname().toString());
                //topName.setText(classInfoList.get(i).getClassname());
                setTopName(classInfoList.get(i).getClassname());
                getAttendList();*/
                if (!TextUtils.isEmpty(SingleBabyInfos.getmBabyInfos().getInfos().get(i).getId())) {
                    SPUtils.put(context, LocalConstant.USER_BABYID, SingleBabyInfos.getmBabyInfos().getInfos().get(i).getStudentid());
                    SPUtils.put(context, LocalConstant.USER_BABYNAME, SingleBabyInfos.getmBabyInfos().getInfos().get(i).getStudentname());
                    SPUtils.put(context, LocalConstant.USER_CLASSID, SingleBabyInfos.getmBabyInfos().getInfos().get(i).getClasslist().get(0).getClassid());

                    tvRelativeBaby.setText((CharSequence) SPUtils.get(context, LocalConstant.USER_BABYNAME, ""));

                }

            }
        });
        mMaterialDialog.show();

    }

    private void getBabyInfo() {
        final String baby_url = NetConstantUrl.GET_USER_RELATION + "&userid=" + SPUtils.get(context, LocalConstant.USER_ID, "");
        VolleyUtil.VolleyGetRequest(context, baby_url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                List<BabyInfo> allbaby;
                allbaby = new ArrayList<BabyInfo>();
                allbaby = getBabyInfoFromJson(result);
                int babycount = allbaby.size();
                boolean hadbaby;
                hadbaby = false;
                if (babycount > 0) {
                    hadbaby = true;
                }
                SingleBabyInfos.getmBabyInfos().getInfos().clear();
                for (int i = 0; i < babycount; i++) {

                    SingleBabyInfos.getmBabyInfos().getInfos().add(allbaby.get(i));


                }
                if (hadbaby) {
                } else {
                    ProgressUtil.dissmisLoadingDialog();
                    ToastUtil.showShort(context, "你没有关联学生信息，请与班主任联系！");
                }
//                BabyInfo babyInfo = getBabyInfoFromJson(result).get(0);
//                SPUtils.put(context, LocalConstant.USER_BABYID, babyInfo.getStudentid());
//                SPUtils.put(context, LocalConstant.USER_CLASSID, babyInfo.getClasslist().get(0).getClassid());
//                SPUtils.put(context,LocalConstant.CLASS_NAME,babyInfo.getClasslist().get(0).getClassname());
//                SPUtils.put(context, LocalConstant.SCHOOL_ID,babyInfo.getClasslist().get(0).getSchoolid());
//                startActivity(MainActivity.class);
//                finish();
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 字符串转模型（宝宝信息）
     *
     * @param * @return
     */
    private List<BabyInfo> getBabyInfoFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<BabyInfo>>() {
        }.getType());
    }


    @OnClick(R.id.fragment_four_rl_relation_ship)
    public void onViewClicked() {
    }
}
