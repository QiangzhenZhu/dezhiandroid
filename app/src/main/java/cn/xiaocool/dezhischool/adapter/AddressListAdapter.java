package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.Group;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class AddressListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Group> groups;
    private LayoutInflater inflater;
    private int type;       //1家长 2老师

    public AddressListAdapter(Context context, List<Group> groups ,int type) {
        this.context = context;
        this.groups = groups;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildrenCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildItem(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_nocb_layout, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group.getTitle());
        ImageView jiantou =  (ImageView) convertView.findViewById(R.id.jiantou);
        if (isExpanded){
            jiantou.setImageResource(R.drawable.ic_jiantouxia);

        }else {
            jiantou.setImageResource(R.drawable.ic_jiantouyou);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Child child = groups.get(groupPosition).getChildItem(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_nocb_layout, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
        tv.setText(child.getFullname());
        TextView tv_childName = (TextView) convertView.findViewById(R.id.tv_child_name);
        if(type == 1){
            tv_childName.setVisibility(View.VISIBLE);
            tv_childName.setText("("+child.getChildName()+")");
        }
        //打电话
        ImageView iv_phone = (ImageView) convertView.findViewById(R.id.iv_phone);
        final String phone = child.getPhone();
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
