package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.BabyInfo;
import cn.xiaocool.dezhischool.bean.SingleBabyInfos;
import cn.xiaocool.dezhischool.bean.leaveDetail;
import cn.xiaocool.dezhischool.bean.leaveInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.SPUtils;

public class LeavDetailActivity extends BaseActivity {
    private static final String TAG = "LeavDetailActivity";
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.tv_child_name)
    TextView tvChildName;
    @BindView(R.id.tv_leave_type)
    TextView tvLeaveType;
    @BindView(R.id.tv_sumbit_time)
    TextView tvSumbitTime;
    @BindView(R.id.tv_leave_reason)
    TextView tvLeaveReason;
    @BindView(R.id.nine_grid_iv)
    NineGridImageView nineGridIv;
    @BindView(R.id.tv_approval)
    TextView tvApproval;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.tv_approval_teacher)
    TextView tvApprovalTeacher;
    @BindView(R.id.tv_leave_start)
    TextView tvLeaveStart;
    @BindView(R.id.tv_leave_end)
    TextView tvLeaveEnd;
    @BindView(R.id.button)
    TextView tvButton;
    @BindView(R.id.textView13)
    TextView stateButton;
    @BindView(R.id.shenpiyijian)
    TextView shenpiyijian;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.tv_leave_resumeption_reason)
    TextView tvLeaveResumeptionReason;
    @BindView(R.id.tv_leave_resumeption_reply)
    TextView tvLeaveResumeptionReply;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.tv_resumption_apply)
    TextView tvResumptionApply;
    @BindView(R.id.tv_resumption_approval_teacher)
    TextView tvResumptionApprovalTeacher;
    @BindView(R.id.tv_resumption_leave_time)
    TextView tvResumptionLeaveTime;
    @BindView(R.id.lv_leave_resumption)
    LinearLayout lvLeaveResumption;

    @BindView(R.id.ll_resumption)
            //会隐藏 当status 为1
    LinearLayout llResumption;
    @BindView(R.id.ll_leave_rectange)
    LinearLayout llLeaveRectangle;

    //type 0待批准 1已同意
    private int type = 0;
    private Context mContext;
    private List<String> iamges;
    private leaveDetail detail;
    private String leaveid;
    private leaveInfo mleaveInfo;
    private NineGridImageViewAdapter<String> nineGridViewAdapter = new NineGridImageViewAdapter<String>() {

        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
            Picasso.with(context)
                    .load(NetConstantUrl.IMAGE_URL+s)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leav_detail);
        mContext = this;
        ButterKnife.bind(this);
        setTopName("假条信息");
        hideRightText();
        detail = (leaveDetail) getIntent().getSerializableExtra("leaveDetail");
        leaveid = detail.getId();
        iamges = new ArrayList<>();
        type = 1;
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void requsetData() {
        final String url = NetConstantUrl.GET_LEAVE_INFO + "&leaveid=" + leaveid;
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String data = object.getString("data");
                    mleaveInfo = new Gson().fromJson(data,leaveInfo.class);
                    for (leaveInfo.PicBean pic:mleaveInfo.getPic()
                         ) {
                        iamges .add(pic.getPicture_url());
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                Toast.makeText(mContext,"数据加载错误",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void init() {
        //根据type的值来判断现不现实 老师的审批意见
        if (type == 0) {
            shenpiyijian.setVisibility(View.GONE);
            tvApproval.setVisibility(View.GONE);

        } else {
            stateButton.setText("已同意");
            tvButton.setText("申请销假");
        }

        nineGridIv.setAdapter(nineGridViewAdapter);
        nineGridIv.setImagesData(iamges);
        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    tvButton.setText("已撤销请假");
                    tvButton.setEnabled(false);
                    stateButton.setVisibility(View.GONE);
                }
                if (type == 1) {
                    Intent intent = new Intent(LeavDetailActivity.this, LeaveResumptionActivity.class);
                    intent.putExtra("leaveInfo",mleaveInfo);
                    startActivity(intent);
                }

            }
        });
    }
    public void setData() {
        // TODO: 2018/2/27 状态
        //0=》等待，－1=》驳回 1=》同意，10=>已经销假
        nineGridIv.setAdapter(nineGridViewAdapter);
        nineGridIv.setImagesData(iamges);
        if ("0".equals(detail.getStatus())) {

            //Glide.with(mContext).load(NetConstantUrl.IMAGE_URL+detail.getStudentavatar()).into(ivProfile);
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL+mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());

            tvApply.setText(mleaveInfo.getParentname());
            tvApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("待批准");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_wait));

            shenpiyijian.setVisibility(View.GONE);
            tvApproval.setVisibility(View.GONE);
            lvLeaveResumption.setVisibility(View.GONE);
            tvButton.setVisibility(View.GONE);
        } else if ("-1".equals(mleaveInfo.getStatus())){
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL+mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            //nineGridIv
            tvApply.setText(mleaveInfo.getParentname());
            tvApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("驳回");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_refuse));
            tvApproval.setText(mleaveInfo.getFeedback());
            lvLeaveResumption.setVisibility(View.GONE);

        }
        if ("1".equals(mleaveInfo.getStatus())){
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL+mleaveInfo.getStudentavatar()).into(ivProfile);

            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            //nineGridIv
            tvApply.setText(mleaveInfo.getParentname());
            tvApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("批准");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_approve));

            tvApproval.setText(mleaveInfo.getFeedback());
            lvLeaveResumption.setVisibility(View.GONE);
            Log.d(TAG, "setData: "+mleaveInfo.getReportback().size());
            if(mleaveInfo.getReportback() != null&&mleaveInfo.getReportback().size()>0){
                for (int i = 0; i <mleaveInfo.getReportback().size() ; i++) {
                    Log.d(TAG, "setData: ");
                    //隐藏原来的销假列表，动态生成新的
                    lvLeaveResumption.setVisibility(View.VISIBLE);
                    llResumption.setVisibility(View.GONE);
                    llLeaveRectangle.setVisibility(View.GONE);

                    View view = LayoutInflater.from(mContext).inflate(R.layout.leave_reasumption_add,null);
                    setViewData(view,mleaveInfo.getReportback().get(i),mleaveInfo);
                    lvLeaveResumption.addView(view);
                    //设置状态：

                    tvButton.setText("已销假");
                    if (i == (mleaveInfo.getReportback().size()-1)){
                        if ("0".equals(mleaveInfo.getReportback().get(i).getStatus())){
                            stateButton.setText("待批准");
                            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_wait));
                            tvButton.setText("撤回销假申请");
                            tvButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendWithDrawlLeaveBack(mleaveInfo);
                                }
                            });

                        }else {
                            stateButton.setVisibility(View.GONE);
                            tvButton.setText("已销假");
                            tvButton.setEnabled(false);
                        }
                    }
                }







            }







        }else if ("10".equals(mleaveInfo.getStatus())){
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL+mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            //nineGridIv
            tvApply.setText(mleaveInfo.getParentname());
            tvApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("已销假");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_over));
            tvLeaveResumeptionReason.setText(mleaveInfo.getReportback().get(0).getReason());
            tvLeaveResumeptionReply.setText(mleaveInfo.getReportback().get(0).getFeedback());
            tvResumptionApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvResumptionLeaveTime.setText(formatTime(mleaveInfo.getReportback().get(0).getDeal_time(),2));
            tvResumptionApply.setText(mleaveInfo.getParentname());
            tvButton.setEnabled(false);
            tvButton.setText("已销假");
            if(mleaveInfo.getReportback() != null) {
                for (int i = 0; i < mleaveInfo.getReportback().size(); i++) {
                    Log.d(TAG, "setData: ");
                    //隐藏原来的销假列表，动态生成新的
                    lvLeaveResumption.setVisibility(View.VISIBLE);
                    llResumption.setVisibility(View.GONE);
                    llLeaveRectangle.setVisibility(View.GONE);

                    View view = LayoutInflater.from(mContext).inflate(R.layout.leave_reasumption_add, null);
                    setViewData(view, mleaveInfo.getReportback().get(i), mleaveInfo);
                    lvLeaveResumption.addView(view);
                    //设置状态：
                    stateButton.setVisibility(View.GONE);
                    tvButton.setText("已销假");
                    tvButton.setEnabled(false);
                }
            }
        }

        long nowData = new Date().getTime();
        long endData = Long.valueOf(detail.getEndtime())*1000L;
        if (nowData > endData){
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL+mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            //nineGridIv
            tvApply.setText(mleaveInfo.getParentname());
            tvApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("已销假");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_over));
            if(!TextUtils.isEmpty(mleaveInfo.getFeedback())) {
                tvLeaveResumeptionReason.setText(mleaveInfo.getReportback().get(0).getReason());
                tvLeaveResumeptionReply.setText(mleaveInfo.getReportback().get(0).getFeedback());
                tvResumptionLeaveTime.setText(formatTime(mleaveInfo.getReportback().get(0).getDeal_time(),2));
            }
            tvResumptionApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvResumptionApply.setText(mleaveInfo.getParentname());
            tvButton.setEnabled(false);
            tvButton.setText("已销假");
            if(mleaveInfo.getReportback() != null) {
                for (int i = 0; i < mleaveInfo.getReportback().size(); i++) {
                    Log.d(TAG, "setData: ");
                    //隐藏原来的销假列表，动态生成新的
                    lvLeaveResumption.setVisibility(View.VISIBLE);
                    llResumption.setVisibility(View.GONE);
                    llLeaveRectangle.setVisibility(View.GONE);
                    View view = LayoutInflater.from(mContext).inflate(R.layout.leave_reasumption_add, null);
                    setViewData(view, mleaveInfo.getReportback().get(i), mleaveInfo);
                    lvLeaveResumption.addView(view);
                    //设置状态：
                    stateButton.setVisibility(View.GONE);
                    tvButton.setText("已销假");
                    tvButton.setEnabled(false);
                }
            }
        }
       /*shenpiyijian;
       imageView2;
       tvLeaveResumeptionReason;
       tvLeaveResumeptionReply;
       imageView3;
       tvResumptionApply;
       tvResumptionApprovalTeacher;
       tvResumptionLeaveTime;
       lvLeaveResumption;*/
    }

    public String formatTime(String longTime,int type){
        if (type == 1){
            String data = "yyyy-MM-dd HH:mm";
            long time = Long.valueOf(longTime)*1000L;
            Date date = new Date(time);
            return new SimpleDateFormat(data).format(date);
        }else {
            String data = "MM-dd HH:mm";
            long time = Long.valueOf(longTime)*1000L;
            Date date = new Date(time);
            return new SimpleDateFormat(data).format(date);
        }

    }
    public void setViewData(View view,leaveInfo.ReportbackBean  reportbackBean,leaveInfo mleaveInfo){
        TextView mTextResumptionReason;
        TextView mTextFeedBack;
        TextView mTextResumptionApply;
        TextView mTextResumptionTeacher;
        TextView mTextResumptionLeave;

        mTextResumptionReason = view.findViewById(R.id.tv_leave_resumeption_reason);
        mTextFeedBack = view.findViewById(R.id.tv_leave_resumeption_reply);
        mTextResumptionApply = view.findViewById(R.id.tv_resumption_apply);
        mTextResumptionTeacher = view.findViewById(R.id.tv_resumption_approval_teacher);
        mTextResumptionLeave = view.findViewById(R.id.tv_resumption_leave_time);

        mTextResumptionReason.setText(reportbackBean.getReason());
        if (reportbackBean.getFeedback().isEmpty()){
            mTextFeedBack.setText("暂无审批意见");
        }else {
            mTextFeedBack.setText(reportbackBean.getFeedback());
        }
        mTextResumptionApply.setText(mleaveInfo.getParentname());
        mTextResumptionTeacher.setText(mleaveInfo.getTeachername());
        mTextResumptionLeave.setText(reportbackBean.getCreate_time());

    }
    public void sendWithDrawlLeaveBack(final leaveInfo mleaveInfo){
        String applyid = mleaveInfo.getReportback().get(mleaveInfo.getReportback().size()-1).getId();
        String userid = (String) SPUtils.get(mContext,LocalConstant.USER_ID,"");
        final String url = NetConstantUrl.WITH_DRAWAL_LEAVE_BACK + "&applyid=" + applyid+"&userid="+userid;
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if ("success".equals(status)){
                        Toast.makeText(mContext,"撤销销假申请成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                Toast.makeText(mContext,"数据加载错误",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
