package cn.xiaocool.dezhischool.adapter;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import org.apache.http.client.methods.HttpOptions;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.Group;

/**
 * Created by 10835 on 2018/2/3.
 */

public class MeetingPeopleAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Group> groups;
    private List<Child> realChildren;
    private List<Child> children;
    private List<Integer> counts;
    private List<Integer> Heards;
    private OnItemOnCliclkListner itemOnCliclkListner;
    public interface OnItemOnCliclkListner{
        public void onClick1(View view,int position);
        public void onClick2(View view ,int position);
        public void onClick3(View view ,int position);
    }
    public void setItemOnCliclkListner(OnItemOnCliclkListner itemOnCliclkListner){
        this.itemOnCliclkListner = itemOnCliclkListner;
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ImageView mZhanImageView;
        private ImageView mSelecetAllImageView;
        private TextView  mGroupName;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mZhanImageView = itemView.findViewById(R.id.imageView8);
            mSelecetAllImageView = itemView.findViewById(R.id.selectAll);
            mGroupName = itemView.findViewById(R.id.textView33);
        }
        public void bind(Child child){
            mGroupName.setText(child.getFullname());
            if (child.isFlag()){
                mZhanImageView.setImageResource(R.mipmap.zhankai);
            } else {
                mZhanImageView.setImageResource(R.mipmap.weizhankai);
            }
            if (child.isSelectedAll()){
                mSelecetAllImageView.setImageResource(R.mipmap.yixuanzeda);
            } else {
                mSelecetAllImageView.setImageResource(R.mipmap.weixuanzeda);
            }

        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder{
        private TextView mChidName;
        private ImageView mSelectedChild;
        public ChildViewHolder(View itemView) {
            super(itemView);
            mChidName = itemView.findViewById(R.id.textView34);
            mSelectedChild =itemView.findViewById(R.id.select_child);
        }
        public void bind(Child child){
            mChidName.setText(child.getFullname());
            if (child.getChecked()){
                mSelectedChild.setImageResource(R.mipmap.yixuanzexiao);
            } else {
                mSelectedChild.setImageResource(R.mipmap.weixuanzexiao);
            }
        }
    }

    public MeetingPeopleAdapter(List<Child> children1,List<Group> groups) {
        this.children = children1;
        /*this.groups = groups;
        children = new ArrayList<>();
        //头部信息在RealChildren的位置
        Heards = new ArrayList<>();
        //每个item的Child的数量
        counts = new ArrayList<>();
        int temp = 0;
        Heards.add(0);
        for (int i = 0; i <groups.size() ; i++) {

            if (i != groups.size()-1) {
                    temp++;
                    temp = temp + groups.get(i).getChildrenCount();
                    Heards.add(temp);

            }

            counts.add(groups.get(i).getChildrenCount());

        }*/
        /*//只添加头部信息；
        for (int i = 0; i <Heards.size() ; i++) {
            children.add(realChildren.get(Heards.get(i)));
        }
        */
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_people_item,parent,false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metting_people_item_student,parent,false);
            return new ChildViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder){
            final Child child = children.get(position);
            ((HeaderViewHolder) holder).bind(child);
            /*if (child.isFlag()){
                ((HeaderViewHolder) holder).mSelecetAllImageView.setImageResource(R.mipmap.yixuanzeda);
            }*/
            ((HeaderViewHolder) holder).mSelecetAllImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnCliclkListner.onClick3(v,holder.getLayoutPosition()-1);
                }
            });
            ((HeaderViewHolder) holder).mZhanImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imageView8:
                            itemOnCliclkListner.onClick1(v,holder.getLayoutPosition()-1);
                            break;
                        default:
                            break;
                    }

                }
            });

        }else if (holder instanceof  ChildViewHolder){
            Child child = children.get(position);
            ((ChildViewHolder) holder).bind(child);
            ((ChildViewHolder) holder).mSelectedChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnCliclkListner.onClick2(v,holder.getLayoutPosition()-1);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return children.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (children.get(position).isHeader()){
            return -1;
        }else {
            return 1;
        }
    }
}
