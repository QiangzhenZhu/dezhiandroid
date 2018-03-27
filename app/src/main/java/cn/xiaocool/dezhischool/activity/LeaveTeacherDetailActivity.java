package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import cn.xiaocool.dezhischool.bean.leaveDetail;
import cn.xiaocool.dezhischool.bean.leaveInfo;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class LeaveTeacherDetailActivity extends BaseActivity {

    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.tv_child_name)
    TextView tvChildName;
    @BindView(R.id.tv_leave_type)
    TextView tvLeaveType;
    @BindView(R.id.tv_sumbit_time)
    TextView tvSumbitTime;
    @BindView(R.id.textView13)
    TextView stateButton;
    @BindView(R.id.tv_leave_reason)
    TextView tvLeaveReason;
    @BindView(R.id.nine_grid_iv)
    NineGridImageView nineGridIv;
    @BindView(R.id.tv_approval_teacher_text)
    TextView tvApprovalTeachertext;
    @BindView(R.id.tv_leave_begin)
    TextView tvLeavebegin;
    @BindView(R.id.tv_leave_end)
    TextView tvLeaveEnd;
    @BindView(R.id.et_leave_teacher_apply)
    EditText etLeaveTeacherApply;
    @BindView(R.id.tv_approval_teacher)
    TextView tvApprovalTeacher;
    @BindView(R.id.tv_leave_start)
    TextView tvLeaveStart;
    @BindView(R.id.tv_leave_over)
    TextView tvLeaveover;
    @BindView(R.id.bt_refused)
    TextView btRefused;
    @BindView(R.id.bt_agree)
    TextView btAgree;
    @BindView(R.id.bt_resumption)
    TextView btResumption;
    @BindView(R.id.shenpiyijian)
    TextView shenpiyijian;
    @BindView(R.id.tv_approval)
    TextView tvApproval;
    @BindView(R.id.ll_leave_teacher_add)
    LinearLayout llLeaveTeacherAdd;
    @BindView(R.id.ll_examine)
    LinearLayout llExamine;
    private Context mContext;
    private String leaveid;
    private leaveDetail detail;
    private leaveInfo mleaveInfo;
    private String type;
    private String teacherid;
    private String feedback;
    private List<String> iamges;
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
        setContentView(R.layout.activity_leave_teacher_detail);
        ButterKnife.bind(this);
        mContext = this;
        setTopName("假条详情");
        hideRightText();
        detail = (leaveDetail) getIntent().getSerializableExtra("leaveDetail");
        leaveid = detail.getId();
        teacherid = detail.getTeacherid();
        iamges = new ArrayList<>();


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
                    mleaveInfo = new Gson().fromJson(data, leaveInfo.class);
                    type = mleaveInfo.getStatus();
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
                Toast.makeText(mContext, "数据加载错误", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setData() {
        //0=》等待，－1=》驳回 1=》同意，10=>已经销假
        nineGridIv.setAdapter(nineGridViewAdapter);
        nineGridIv.setImagesData(iamges);
        if ("0".equals(type)) {
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL + mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            //nineGridIv
            shenpiyijian.setVisibility(View.GONE);
            tvApproval.setVisibility(View.GONE);
            tvApprovalTeachertext.setText(mleaveInfo.getTeachername());
            tvLeavebegin.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveover.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("待批准");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_wait));
            tvApprovalTeacher.setText(mleaveInfo.getTeachername());
            tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(), 2));
            btResumption.setVisibility(View.GONE);
            btRefused.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedback = etLeaveTeacherApply.getText().toString().trim();
                    if (TextUtils.isEmpty(feedback)) {
                        Toast.makeText(mContext, "请填写审批意见", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String url = NetConstantUrl.REPLY_LEAVE + "&leaveid=" + leaveid + "&teacherid=" + teacherid + "&feedback=" + feedback + "&status=-1";
                    VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                String data = object.getString("status");
                                if ("success".equals(data)) {
                                    btAgree.setVisibility(View.GONE);
                                    stateButton.setVisibility(View.GONE);
                                    btRefused.setText("已驳回");
                                    btRefused.setEnabled(false);
                                    etLeaveTeacherApply.setEnabled(false);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError() {
                            Toast.makeText(mContext, "数据加载错误", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
            btAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedback = etLeaveTeacherApply.getText().toString().trim();
                    if (TextUtils.isEmpty(feedback)) {
                        Toast.makeText(mContext, "请填写理由", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String url = NetConstantUrl.REPLY_LEAVE + "&leaveid=" + leaveid + "&teacherid=" + teacherid + "&feedback=" + feedback + "&status=1";
                    VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                String data = object.getString("status");
                                if ("success".equals(data)) {
                                    btAgree.setVisibility(View.GONE);
                                    btRefused.setVisibility(View.GONE);
                                    btResumption.setVisibility(View.VISIBLE);
                                    stateButton.setText("已同意");
                                    stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_approve));
                                    etLeaveTeacherApply.setEnabled(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError() {
                            Toast.makeText(mContext, "数据加载错误", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });


        } else if ("1".equals(type)) {
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL + mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            etLeaveTeacherApply.setText(mleaveInfo.getFeedback());
            etLeaveTeacherApply.setEnabled(false);
            //nineGridIv
            llExamine.setVisibility(View.GONE);
            tvApproval.setText(mleaveInfo.getFeedback());
            tvApprovalTeachertext.setText(mleaveInfo.getTeachername());
            tvLeavebegin.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveover.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("已批准");
            stateButton.setVisibility(View.VISIBLE);
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_approve));
            btAgree.setVisibility(View.GONE);
            btRefused.setVisibility(View.GONE);
            btResumption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LeaveResumptionActivity.class);
                    intent.putExtra("leaveInfo", mleaveInfo);
                    startActivity(intent);
                }
            });

            if (mleaveInfo.getReportback() != null&&mleaveInfo.getReportback().size()>0){
                if ("0".equals(mleaveInfo.getReportback().get((mleaveInfo.getReportback().size()-1)).getStatus())){
                    View view = LayoutInflater.from(mContext).inflate(R.layout.leave_reasumption_add,null);
                    setViewData(view,mleaveInfo.getReportback().get(mleaveInfo.getReportback().size()-1),mleaveInfo,0);
                    llLeaveTeacherAdd.addView(view);
                    llExamine.setVisibility(View.VISIBLE);
                    stateButton.setText("待批准销假");
                    stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_wait));
                    btResumption.setText("同意销假");
                    btRefused.setText("驳回销假");
                    etLeaveTeacherApply.setEnabled(true);
                    etLeaveTeacherApply.setText("");
                    tvApprovalTeacher.setText(mleaveInfo.getTeachername());
                    tvLeaveStart.setText(formatTime(mleaveInfo.getBegintime(),2));
                    tvLeaveEnd.setText(formatTime(mleaveInfo.getEndtime(),1));
                    btRefused.setVisibility(View.VISIBLE);
                    btRefused.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReplyLeaveBack("-1",mleaveInfo.getReportback().get(mleaveInfo.getReportback().size()-1).getId());
                        }
                    });
                    btResumption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReplyLeaveBack("1",mleaveInfo.getReportback().get(mleaveInfo.getReportback().size()-1).getId());
                        }
                    });
                }else {
                    llExamine.setVisibility(View.GONE);
                }
            }

        } else if ("-1".equals(type)) {
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL + mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            etLeaveTeacherApply.setText(mleaveInfo.getFeedback());
            etLeaveTeacherApply.setEnabled(false);
            //nineGridIv
            llExamine.setVisibility(View.GONE);
            tvApproval.setText(mleaveInfo.getFeedback());
            tvApprovalTeachertext.setText(mleaveInfo.getTeachername());
            tvLeavebegin.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveover.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("驳回");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_refuse));
            btAgree.setVisibility(View.GONE);
            btRefused.setText("已驳回");
            btRefused.setEnabled(false);
            btResumption.setVisibility(View.GONE);

        } else if ("10".equals(type)) {
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL + mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            etLeaveTeacherApply.setText(mleaveInfo.getFeedback());
            etLeaveTeacherApply.setEnabled(false);
            //nineGridIv
            llExamine.setVisibility(View.GONE);
            tvApproval.setText(mleaveInfo.getFeedback());
            tvApprovalTeachertext.setText(mleaveInfo.getTeachername());
            tvLeavebegin.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveover.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("已销假");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_over));
            btResumption.setVisibility(View.GONE);
            btRefused.setVisibility(View.GONE);
            btAgree.setText("已销假");
            btAgree.setEnabled(false);
            if(mleaveInfo.getReportback() != null&&mleaveInfo.getReportback().size()>0) {
                for (int i = 0; i < mleaveInfo.getReportback().size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.leave_reasumption_add,null);
                    setViewData(view,mleaveInfo.getReportback().get(mleaveInfo.getReportback().size()-1),mleaveInfo,1);
                    llLeaveTeacherAdd.addView(view);
                }
            }

        }
        long nowData = new Date().getTime();
        long endData = Long.valueOf(detail.getEndtime())*1000L;
        //时间已过，自动销假
        if (nowData> endData){
            Picasso.with(mContext).load(NetConstantUrl.IMAGE_URL + mleaveInfo.getStudentavatar()).into(ivProfile);
            tvChildName.setText(mleaveInfo.getStudentname());
            tvLeaveType.setText(mleaveInfo.getLeavetype());
            tvSumbitTime.setText(formatTime(mleaveInfo.getCreate_time(), 1));
            tvLeaveReason.setText(mleaveInfo.getReason());
            etLeaveTeacherApply.setText(mleaveInfo.getFeedback());
            etLeaveTeacherApply.setEnabled(false);
            //nineGridIv
            llExamine.setVisibility(View.GONE);
            tvApproval.setText(mleaveInfo.getFeedback());
            tvApprovalTeachertext.setText(mleaveInfo.getTeachername());
            tvLeavebegin.setText(formatTime(mleaveInfo.getBegintime(), 2));
            tvLeaveover.setText(formatTime(mleaveInfo.getEndtime(), 2));
            stateButton.setText("已销假");
            stateButton.setBackground(getResources().getDrawable(R.drawable.leave_text_view_over));
            btResumption.setVisibility(View.GONE);
            btRefused.setVisibility(View.GONE);
            btAgree.setText("已销假");
            btAgree.setEnabled(false);
            if(mleaveInfo.getReportback() != null&&mleaveInfo.getReportback().size()>0) {
                for (int i = 0; i < mleaveInfo.getReportback().size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.leave_reasumption_add,null);
                    setViewData(view,mleaveInfo.getReportback().get(mleaveInfo.getReportback().size()-1),mleaveInfo,1);
                    llLeaveTeacherAdd.addView(view);
                }
            }
        }
    }
    public void ReplyLeaveBack(final String status, String applyid){
        String back = etLeaveTeacherApply.getText().toString().trim();
        if (TextUtils.isEmpty(back)){
            Toast.makeText(mContext,"审批意见不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //-1 不同意 1同意
        final String url = NetConstantUrl.REPLY_LEAVE_BACK + "&applyid=" + applyid + "&teacherid=" + teacherid + "&feedback=" + back + "&status="+status;
        VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String data = object.getString("status");
                    if ("success".equals(data)) {
                        if ("1".equals(status)){
                            btRefused.setVisibility(View.GONE);
                            btResumption.setText("已同意销假");
                            btResumption.setEnabled(false);
                        }else {
                            btResumption.setVisibility(View.GONE);
                            btRefused.setText("已驳回销假");
                            btRefused.setEnabled(false);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "数据加载错误", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public String formatTime(String longTime, int type) {
        if (type == 1) {
            String data = "yyyy-MM-dd HH:mm";
            long time = Long.valueOf(longTime) * 1000L;
            Date date = new Date(time);
            return new SimpleDateFormat(data).format(date);
        } else {
            String data = "MM-dd HH:mm";
            long time = Long.valueOf(longTime) * 1000L;
            Date date = new Date(time);
            return new SimpleDateFormat(data).format(date);
        }

    }
    public void setViewData(View view,leaveInfo.ReportbackBean  reportbackBean,leaveInfo mleaveInfo,int type){
        TextView mTextResumptionReason;
        TextView mTextFeedBack;
        TextView mTextResumptionApply;
        TextView mTextResumptionTeacher;
        TextView mTextResumptionLeave;
        TextView mTextXiaoJiaShenPi;

        mTextResumptionReason = view.findViewById(R.id.tv_leave_resumeption_reason);
        mTextFeedBack = view.findViewById(R.id.tv_leave_resumeption_reply);
        mTextXiaoJiaShenPi = view .findViewById(R.id.xiaojiashenpiyijia);
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
        if (type == 0){
            mTextXiaoJiaShenPi.setVisibility(View.GONE);
            mTextFeedBack.setVisibility(View.GONE);
        }
        mTextResumptionTeacher.setText(mleaveInfo.getTeachername());
        mTextResumptionLeave.setText(formatTime(reportbackBean.getCreate_time(),2));

    }

}
