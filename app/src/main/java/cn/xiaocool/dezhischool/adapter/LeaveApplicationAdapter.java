package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.LeavDetailActivity;
import cn.xiaocool.dezhischool.activity.LeaveTeacherDetailActivity;
import cn.xiaocool.dezhischool.bean.leaveDetail;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by 10835 on 2018/2/25.
 */

public class LeaveApplicationAdapter extends RecyclerView.Adapter<LeaveApplicationAdapter.ViewHolder> {

    private List<leaveDetail> detailList;
    private String userType;
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mProfileImage;
        TextView mLeaveStudent;
        TextView mLeaveType;
        TextView mApplicationTime;
        TextView mLeaveReson;
        TextView mStartTime;
        TextView mEndTime;
        TextView mApplicationPeople;
        TextView mApprovalTeacher;
        TextView mLeaveState;
        public ViewHolder(View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.imageView);
            mLeaveStudent = itemView.findViewById(R.id.textView3);
            mLeaveType = itemView.findViewById(R.id.textView4);
            mApplicationTime = itemView.findViewById(R.id.textView5);
            mLeaveReson = itemView.findViewById(R.id.textView6);
            mStartTime = itemView.findViewById(R.id.textView9);
            mEndTime = itemView.findViewById(R.id.textView10);
            mApplicationPeople = itemView.findViewById(R.id.textView11);
            mApprovalTeacher = itemView.findViewById(R.id.textView12);
            mLeaveState = itemView.findViewById(R.id.textView13);


        }
    }

    public LeaveApplicationAdapter( List<leaveDetail> leaveDetails,String userType) {
        detailList = leaveDetails;
        this.userType = userType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final leaveDetail detail = detailList.get(position);
        //Glide.with(holder.itemView.getContext()).load(NetConstantUrl.IMAGE_URL+detail.getStudentavatar()).into(holder.mProfileImage);
        holder.mLeaveStudent.setText(detail.getStudentname());
        holder.mApplicationTime.setText(formatTime(detail.getCreate_time(),1));
        holder.mLeaveType.setText(detail.getLeavetype());
        holder.mLeaveReson.setText(detail.getReason());
        holder.mStartTime.setText(formatTime(detail.getBegintime(),2));
        holder.mEndTime.setText(formatTime(detail.getEndtime(),2));
        holder.mApprovalTeacher.setText(detail.getTeachername());
        holder.mApplicationPeople.setText(detail.getParentname());
        long nowData = new Date().getTime();
        long endData = Long.valueOf(detail.getEndtime())*1000L;
        // TODO: 2018/2/27 状态
        if ("0".equals(detail.getStatus())){
            holder.mLeaveState.setText("待批准");
            holder.mLeaveState.setBackground(context.getResources().getDrawable(R.drawable.leave_text_view_wait));
            //holder.mLeaveState.setBackgroundColor(context.getResources().getColor(R.color.leave_state_wait));
        } else if ("-1".equals(detail.getStatus())){
            holder.mLeaveState.setText("驳回");
            holder.mLeaveState.setBackground(context.getResources().getDrawable(R.drawable.leave_text_view_refuse));
            //holder.mLeaveState.setBackgroundColor(context.getResources().getColor(R.color.leave_state_refuse));
        }else if ("1".equals(detail.getStatus())){
            holder.mLeaveState.setText("批准");
            holder.mLeaveState.setBackground(context.getResources().getDrawable(R.drawable.leave_text_view_approve));
            //holder.mLeaveState.setBackgroundColor(context.getResources().getColor(R.color.leave_state_agree));
        }else if ("10".equals(detail.getStatus())){
            holder.mLeaveState.setText("已销假");
            holder.mLeaveState.setBackground(context.getResources().getDrawable(R.drawable.leave_text_view_over));
            //holder.mLeaveState.setBackgroundColor(context.getResources().getColor(R.color.leave_state_agree));
        }

        if (nowData > endData){
            holder.mLeaveState.setText("已销假");
            holder.mLeaveState.setBackground(context.getResources().getDrawable(R.drawable.leave_text_view_over));
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("1".equals(userType)){
                    Intent intent = new Intent(v.getContext(), LeaveTeacherDetailActivity.class);
                    intent.putExtra("leaveDetail",detail);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), LeavDetailActivity.class);
                    intent.putExtra("leaveDetail",detail);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
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
}
