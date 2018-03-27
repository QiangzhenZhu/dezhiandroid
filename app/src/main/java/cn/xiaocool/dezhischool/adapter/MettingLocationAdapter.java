package cn.xiaocool.dezhischool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.MeetingLocation;
import cn.xiaocool.dezhischool.bean.MettingInfo;

/**
 * Created by 10835 on 2018/2/3.
 */

public class MettingLocationAdapter extends RecyclerView.Adapter<MettingLocationAdapter.ViewHolder>  {
    private List<MeetingLocation> locations;
    private boolean isSelected;
    private boolean flag;
    private int mPosition;

    private OnItemClickListner itemClickListner = null;
    //定义一个接口
    public  static interface OnItemClickListner {
        void onItemOnClick(View view,int position);
    }
    public void setOnItemClickerListner(OnItemClickListner clickerListner){
        itemClickListner = clickerListner;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mLocationName;
        public ImageView mLocationSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mLocationName = itemView.findViewById(R.id.metting_location_name);
            mLocationSelected = itemView.findViewById(R.id.metting_location_selected);
        }
    }







    public MettingLocationAdapter(List<MeetingLocation> mLocation) {
        locations = mLocation;

    }

    @Override
    public MettingLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metting_location_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MettingLocationAdapter.ViewHolder holder, int position) {
        final MeetingLocation location = locations.get(position);
        holder.mLocationName.setText(location.getTitle());
        if (location.isFlag()) {
            holder.mLocationSelected.setImageResource(R.mipmap.metting_round_selected);
        }else {
            holder.mLocationSelected.setImageResource(R.mipmap.metting_round);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition()-1;
                itemClickListner.onItemOnClick(v,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }




}
