package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.ClassAttendance;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.view.RoundImageView;


/**
 * Created by Administrator on 2016/5/23.
 */
public class StudentGridListAdapter extends BaseAdapter{
    private ArrayList<ClassAttendance> classListDataList;
    private LayoutInflater inflater;
    private Context context;
    private String what;
    public StudentGridListAdapter(ArrayList<ClassAttendance> classListDataList, Context mContext,String what) {
        this.context = mContext;
        this.classListDataList = classListDataList;
        this.inflater = LayoutInflater.from(context);
        this.what = what;
    }

    @Override
    public int getCount() {
        return classListDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_class_student,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.student_name.setText(classListDataList.get(position).getName());

        ImgLoadUtil.display(NetConstantUrl.IMAGE_URL+classListDataList.get(position).getPhoto(),holder.student_avatar);

        Log.e("classListDataList",classListDataList.get(position).getCheckedType());

       if (what.equals("1")){

           if (classListDataList.get(position).getCheckedType().equals("0")){
               holder.type_show.setVisibility(View.GONE);
           }else if (classListDataList.get(position).getCheckedType().equals("1")){
               holder.type_show.setVisibility(View.VISIBLE);
               holder.type_show.setImageResource(R.drawable.ic_wei);
           }else if (classListDataList.get(position).getCheckedType().equals("2")){
               holder.type_show.setVisibility(View.VISIBLE);
               holder.type_show.setImageResource(R.drawable.ic_qian);
           }else {
               holder.type_show.setVisibility(View.VISIBLE);
               holder.type_show.setImageResource(R.drawable.ic_jia);
           }

       }else {

           if (classListDataList.get(position).getCheckedTypeByGo().equals("0")){
               holder.type_show.setVisibility(View.GONE);
           }else if (classListDataList.get(position).getCheckedTypeByGo().equals("1")){
               holder.type_show.setVisibility(View.VISIBLE);
               holder.type_show.setImageResource(R.drawable.ic_wei);
           }else if (classListDataList.get(position).getCheckedTypeByGo().equals("2")){
               holder.type_show.setVisibility(View.VISIBLE);
               holder.type_show.setImageResource(R.drawable.ic_qian);
           }else {
               holder.type_show.setVisibility(View.VISIBLE);
               holder.type_show.setImageResource(R.drawable.ic_jia);
           }
       }


        return convertView;
    }
    class ViewHolder{
        TextView student_name;
        ImageView type_show;
        RoundImageView student_avatar;
        public ViewHolder(View convertView) {
            student_name = (TextView) convertView.findViewById(R.id.student_name);
            type_show = (ImageView) convertView.findViewById(R.id.type_show);
            student_avatar = (RoundImageView) convertView.findViewById(R.id.student_avatar);
        }
    }
}
