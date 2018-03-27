package cn.xiaocool.dezhischool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.client.methods.HttpOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.AbsentDormitoryInfo;
import cn.xiaocool.dezhischool.bean.DormitoryInspectInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;

/**
 * Created by 10835 on 2018/3/6.
 */

public class AbsentDormitoryAdapter extends RecyclerView.Adapter<AbsentDormitoryAdapter.ViewHolder> {
    private List<AbsentDormitoryInfo> absentDormitoryInfos;

    static

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile_dormitory_absent)
        ImageView ivProfileDormitoryAbsent;
        @BindView(R.id.tv_absent_student_name)
        TextView tvAbsentStudentName;
        @BindView(R.id.tv_absent_dormitory)
        TextView tvAbsentDormitory;
        @BindView(R.id.tv_absent_class)
        TextView tvAbsentClass;
        @BindView(R.id.tv_absent_state)
        TextView tvAbsentState;
        @BindView(R.id.tv_absent_replen)
        TextView tvAbsentReplen;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public AbsentDormitoryAdapter(List<AbsentDormitoryInfo> infos) {
        absentDormitoryInfos = infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dormitory_absent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AbsentDormitoryInfo info = absentDormitoryInfos.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(NetConstantUrl.IMAGE_URL+info.getPhoto())
                .into(holder.ivProfileDormitoryAbsent);
        if (info.getIsReply()){
            holder.tvAbsentReplen.setEnabled(false);
            holder.tvAbsentReplen.setText("补签成功");
        }else {
            holder.tvAbsentReplen.setEnabled(true);
            holder.tvAbsentReplen.setText("补签");
        }
        holder.tvAbsentClass.setText(info.getClassname());
        holder.tvAbsentDormitory.setText(info.getRoom_name());

        holder.tvAbsentStudentName.setText(info.getName());
        holder.tvAbsentReplen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = NetConstantUrl.TODAY_RETROACTIVE + "&studentid=" + info.getStudentid()+"&teacherid="
                        + (String) SPUtils.get(holder.itemView.getContext(),LocalConstant.USER_ID,"")
                        +"&schoolid="+(String) SPUtils.get(holder.itemView.getContext(),LocalConstant.SCHOOL_ID,"");
                VolleyUtil.VolleyGetRequest(holder.itemView.getContext(), url, new VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getString("status");
                            if ("success".equals(status)) {
                                String data = object.getString("data");
                                holder.tvAbsentReplen.setText("补签成功");
                                holder.tvAbsentReplen.setEnabled(false);
                                absentDormitoryInfos.get(position).setIsReply(true);

                            } else {
                                ProgressUtil.dissmisLoadingDialog();
                                ToastUtil.show(holder.itemView.getContext(), "网络加载错误", Toast.LENGTH_SHORT);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(holder.itemView.getContext(), "数据加载错误", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return absentDormitoryInfos.size();
    }



}
