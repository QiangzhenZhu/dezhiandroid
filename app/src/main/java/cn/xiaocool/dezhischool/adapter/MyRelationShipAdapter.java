package cn.xiaocool.dezhischool.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.RelationShipInfo;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.ui.RoundImageView;

public class MyRelationShipAdapter extends RecyclerView.Adapter<MyRelationShipAdapter.ViewHolder> {


    private List<RelationShipInfo> infos;
    private onItemClickListner clickListner;
    private int  candelete = 0;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_set_first_relationship)
        TextView tvSetFirstRelationship;
        @BindView(R.id.tv_delect_relationship)
        TextView tvDelectRelationship;
        @BindView(R.id.tv_first_relation)
        TextView tvFirstRelation;
        @BindView(R.id.roundImageView)
        RoundImageView roundImageView;
        @BindView(R.id.tv_call_real_name)
        TextView tvCallRealName;
        @BindView(R.id.tv_relationship_phone)
        TextView tvRelationshipPhone;
        @BindView(R.id.tv_call_name)
        TextView tvCallName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    public MyRelationShipAdapter(List<RelationShipInfo> infos, onItemClickListner clickListner) {
        this.clickListner = clickListner;
        this.infos = infos;
        candelete = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relationship_lv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final RelationShipInfo info = infos.get(position);
        Picasso.with(holder.itemView.getContext()).load(NetConstantUrl.IMAGE_URL + info.getParent_info().getPhoto()).into(holder.roundImageView);
        holder.tvCallRealName.setText(info.getParent_info().getName());
        holder.tvCallName.setText(info.getAppellation());
        if ("1".equals(info.getType())) {
            holder.tvFirstRelation.setVisibility(View.VISIBLE);
            candelete = 1;
            holder.tvSetFirstRelationship.setVisibility(View.GONE);
            holder.tvDelectRelationship.setVisibility(View.GONE);
        } else {
            holder.tvFirstRelation.setVisibility(View.GONE);
            holder.tvSetFirstRelationship.setVisibility(View.VISIBLE);
            holder.tvDelectRelationship.setVisibility(View.VISIBLE);
        }
        holder.tvRelationshipPhone.setText(info.getParent_info().getPhone());
        holder.tvSetFirstRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.onSetMain(position,info.getUserid());
            }
        });
        holder.tvDelectRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.onDelete(position,info.getUserid(),candelete);
            }
        });

    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public interface onItemClickListner {
        void onSetMain(int position,String userid);
        void onDelete(int position,String userid,int candelete);
    }
}
