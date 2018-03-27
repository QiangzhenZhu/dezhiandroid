package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.CreateWebMetting;
import cn.xiaocool.dezhischool.activity.MettingDetail;
import cn.xiaocool.dezhischool.bean.MettingInfo;
import cn.xiaocool.dezhischool.ui.MettingInfoLabel;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by 10835 on 2018/2/2.
 */

public class MettingAdapter extends RecyclerView.Adapter<MettingAdapter.ViewHolder> {
    private List<MettingInfo> mInfos;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMettingTitle;
        private TextView mMettingLocation;
        private TextView mMettingPeople;
        private TextView mPerusePeople;
        private TextView mMoreText;
        private MettingInfoLabel mMeetingLabel;
        Context context;
        public ViewHolder(View itemView)  {
            super(itemView);
            context = itemView.getContext();
            mMeetingLabel = itemView.findViewById(R.id.metting_label_rv);
            mMettingTitle = itemView.findViewById(R.id.textView26);
            mMettingLocation = itemView.findViewById(R.id.textView28);
            mMettingPeople = itemView.findViewById(R.id.textView29);
            mPerusePeople = itemView.findViewById(R.id.textView30);
            mMoreText = itemView.findViewById(R.id.textView27);
        }
        public void bind(MettingInfo info){
            mMettingTitle.setText(info.getTitle());
            mMettingLocation.setText(info.getRoomname());
            mMettingPeople.setText(info.getUsercount()+"人");
            mPerusePeople.setText(info.getReadcount()+"人");
//            mMeetingLabel.setmStateTitle(info.getState());
            long beginTime = Long.parseLong(info.getBegintime());
            long endTime = Long.parseLong(info.getEndtime());
            Date startDate = new Date(beginTime*1000L);
            Date endDate = new Date(endTime*1000L);
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
            mMeetingLabel.setMstartData(format.format(startDate));
            mMeetingLabel.setmStartTime(new SimpleDateFormat("HH:mm").format(startDate)+"-"+new SimpleDateFormat("HH:mm").format(endDate));
            Date date = new Date();
            long nowData = date.getTime();
            String state = null;
            if (nowData < startDate.getTime()){
                state = "即将开始";
                mMeetingLabel.setmStateTitle("即将开始");
            } else if (nowData <= endDate.getTime()&&nowData>=startDate.getTime()){
                state = "进行中";
                mMeetingLabel.setmStateTitle("进行中");
            } else if (nowData>endDate.getTime()){
                state = "已结束";
                mMeetingLabel.setmStateTitle("已结束");
            }

            //对会议状态进行判断来确定颜色
            if ("即将开始".equals(state)){
                mMeetingLabel.setColorLable(R.color.meeting_state_will);
            } else if ("进行中".equals(state)){
                mMeetingLabel.setColorLable(R.color.meeting_state_now);
            } else {
                mMeetingLabel.setColorLable(R.color.meeting_state_over);
            }

        }
    }

    public MettingAdapter(List<MettingInfo> infos) {
        mInfos = infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metting_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MettingInfo info = mInfos.get(position);
        holder.bind(info);
        holder.mMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MettingDetail.class);
                intent.putExtra("type",CheckMeetingState(info));
                intent.putExtra("id",Integer.parseInt(info.getId()));
                 v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    /**
     *
     * @param info
     * @return 0即将开始 1 进行中 2 已结束
     */
    public int CheckMeetingState(MettingInfo info) {
        Date date = new Date();
        long nowData = date.getTime();
        long beginTime = Long.valueOf(info.getBegintime())*1000L;
        long endTime = Long.valueOf(info.getEndtime())*1000L;
        if (nowData < beginTime) {
            return 0;
        } else if (nowData <= endTime && nowData >= beginTime) {
            return 1;
        } else  {
            return  2;
        }
    }


}
