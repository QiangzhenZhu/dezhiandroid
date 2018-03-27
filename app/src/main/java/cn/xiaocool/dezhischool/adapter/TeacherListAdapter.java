package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.Teacher;
import cn.xiaocool.dezhischool.net.NetConstantUrl;

/**
 * Created by mac on 2017/6/7.
 */

public class TeacherListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Teacher> teachers;
    private LayoutInflater inflater;
    public TeacherListAdapter(Context context,ArrayList<Teacher> teachers){
        this.context = context;
        this.teachers = teachers;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public Object getItem(int position) {
        return teachers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_teacher,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(teachers.get(position).getName());
        holder.tv_class_name.setText(teachers.get(position).getClassName());
        ImageLoader.getInstance().displayImage(NetConstantUrl.NET_CIRCLEPIC_HOST+teachers.get(position).getPhoto(),holder.iv_avatar);
        String section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.ll_class_name.setVisibility(View.VISIBLE);
        } else {
            holder.ll_class_name.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_class_name,tv_name;
        ImageView iv_avatar;
        LinearLayout ll_class_name;
        public ViewHolder(View view){
            tv_class_name = (TextView) view.findViewById(R.id.tv_class_name);
            tv_name = (TextView) view.findViewById(R.id.item_name);
            iv_avatar = (ImageView) view.findViewById(R.id.item_avator);
            ll_class_name = (LinearLayout) view.findViewById(R.id.ll_class_name);
        }
    }

    public String getSectionForPosition(int position) {
        return teachers.get(position).getClassName();
    }


    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String firstChar = teachers.get(i).getClassName();
            if (firstChar.equals(section) ) {
                return i;
            }
        }
        return -1;
    }
}

