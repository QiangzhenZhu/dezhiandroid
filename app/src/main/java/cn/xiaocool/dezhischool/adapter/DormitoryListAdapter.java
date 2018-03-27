package cn.xiaocool.dezhischool.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.methods.HttpOptions;

import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.DormitoryInspectorDetail;
import cn.xiaocool.dezhischool.bean.DormitoryInfo;

/**
 * Created by 10835 on 2018/3/6.
 */

public class DormitoryListAdapter extends RecyclerView.Adapter<DormitoryListAdapter.ViewHolder> {

    private List<DormitoryInfo> dormitoryInfos;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDormitoryName;
        private Button mInspectButton;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDormitoryName = itemView.findViewById(R.id.tv_dormitory_name);
            mInspectButton = itemView.findViewById(R.id.bt_inspect);
        }
    }

    public DormitoryListAdapter(List<DormitoryInfo> infos) {
        dormitoryInfos = infos;
    }

    @Override
    public DormitoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dormitory_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DormitoryListAdapter.ViewHolder holder, int position) {
        final DormitoryInfo info = dormitoryInfos.get(position);
        holder.tvDormitoryName.setText(info.getDorm_name());
        holder.mInspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),DormitoryInspectorDetail.class);
                intent.putExtra("dormitory_id",info.getId());
                intent.putExtra("dormitory_name",info.getDorm_name());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dormitoryInfos.size();
    }


}
