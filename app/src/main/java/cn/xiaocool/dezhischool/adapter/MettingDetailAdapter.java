package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.DESedeKeySpec;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.Meeting_people_detail;

/**
 * Created by 10835 on 2018/2/5.
 */

public class MettingDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Meeting_people_detail>details;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView job;
        public TextView comeTime;
        public TextView leveTime;
        public ViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.textView37);
            job = itemView.findViewById(R.id.textView42);
            comeTime = itemView.findViewById(R.id.textView43);
            leveTime = itemView.findViewById(R.id.textView44);
        }
        public void bind(Meeting_people_detail detail, Context context){
            name.setText(detail.getName());
            long comeTimelong =Long.valueOf(detail.getComeTime())*1000L;
            long levelTimelong = Long.valueOf(detail.getLevelTime())*1000L;
            comeTime.setText(context.getString(R.string.meeting_detail_attentence_come,new SimpleDateFormat("HH:mm").format(comeTimelong)));
            leveTime.setText(context.getString(R.string.meeting_detail_attentence_come,new SimpleDateFormat("HH:mm").format(levelTimelong)));
        }

    }
    public class LeaveViewHolder extends RecyclerView.ViewHolder{
        public ExpandableTextView expandableTextView1;
        public TextView name;
        public LeaveViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView50);
            expandableTextView1 = itemView.findViewById(R.id.expand_text_view1);
        }
        public void bind(Meeting_people_detail detail,Context context){
            name.setText(detail.getName());
            expandableTextView1.setText(detail.getLevelReason());
        }

    }


    public MettingDetailAdapter(List<Meeting_people_detail> details) {
        this.details = details;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_meeting_detail,parent,false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_meeting_level,parent,false);
            return new LeaveViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Meeting_people_detail detail = details.get(position);
            long comeTimelong =Long.valueOf(detail.getComeTime())*1000L;
            long levelTimelong = Long.valueOf(detail.getLevelTime())*1000L;
            ((ViewHolder) holder).name.setText(detail.getName());
            ((ViewHolder) holder).leveTime.setText(holder.itemView.getContext().getString(R.string.meeting_detail_attentence_come,new SimpleDateFormat("HH:mm").format(levelTimelong)));
            ((ViewHolder) holder).comeTime.setText(holder.itemView.getContext().getString(R.string.meeting_detail_attentence_come,new SimpleDateFormat("HH:mm").format(comeTimelong)));
        } else if (holder instanceof LeaveViewHolder){
            Meeting_people_detail detail = details.get(position);
           ((LeaveViewHolder) holder).expandableTextView1.setText(detail.getLevelReason());
            ((LeaveViewHolder) holder).name.setText(detail.getName());
        }
    }


    @Override
    public int getItemCount() {
        return details.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(details.get(position).getLevelReason())){
            return 0;
        }
        return 1;
    }
}
