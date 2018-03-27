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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.TeacherReview;
import cn.xiaocool.dezhischool.net.NetConstantUrl;

/**
 * Created by mac on 2017/6/19.
 */
public class TeacherReviewDetailAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<TeacherReview> list;
    public TeacherReviewDetailAdapter(Context context, ArrayList<TeacherReview> list){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_teacher_review_detail,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_title.setText(list.get(position).getTeacherName());
        Date date = new Date();
        date.setTime(Long.parseLong(list.get(position).getTime()) * 1000);
        holder.item_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        ImageLoader.getInstance().displayImage(NetConstantUrl.NET_CIRCLEPIC_HOST+list.get(position).getTeacherAvator(),holder.iv_head);

        if (list.get(position).getLearn().equals("1")){
            holder.xuexi_1.setSelected(true);
        }else {
            holder.xuexi_1.setSelected(false);
        }
        if (list.get(position).getLearn().equals("2")){
            holder.xuexi_2.setSelected(true);
        }else {
            holder.xuexi_2.setSelected(false);
        }
        if (list.get(position).getLearn().equals("3")){
            holder.xuexi_3.setSelected(true);
        }else {
            holder.xuexi_3.setSelected(false);
        }



        if (list.get(position).getWork().equals("1")){
            holder.dongshou_1.setSelected(true);
        }else {
            holder.dongshou_1.setSelected(false);
        }
        if (list.get(position).getWork().equals("2")){
            holder.dongshou_2.setSelected(true);
        }else {
            holder.dongshou_2.setSelected(false);
        }
        if (list.get(position).getWork().equals("3")){
            holder.dongshou_3.setSelected(true);
        }else {
            holder.dongshou_3.setSelected(false);
        }


        if (list.get(position).getSing().equals("1")){
            holder.sing_1.setSelected(true);
        }else {
            holder.sing_1.setSelected(false);
        }
        if (list.get(position).getSing().equals("2")){
            holder.sing_2.setSelected(true);
        }else {
            holder.sing_2.setSelected(false);
        }
        if (list.get(position).getSing().equals("3")){
            holder.sing_3.setSelected(true);
        }else {
            holder.sing_3.setSelected(false);
        }


        if (list.get(position).getLabour().equals("1")){
            holder.laodong_1.setSelected(true);
        }else {
            holder.laodong_1.setSelected(false);
        }
        if (list.get(position).getLabour().equals("2")){
            holder.laodong_2.setSelected(true);
        }else {
            holder.laodong_2.setSelected(false);
        }
        if (list.get(position).getLabour().equals("3")){
            holder.laodong_3.setSelected(true);
        }else {
            holder.laodong_3.setSelected(false);
        }


        if (list.get(position).getStrain().equals("1")){
            holder.yingbian_1.setSelected(true);
        }else {
            holder.yingbian_1.setSelected(false);
        }
        if (list.get(position).getStrain().equals("2")){
            holder.yingbian_2.setSelected(true);
        }else {
            holder.yingbian_2.setSelected(false);
        }
        if (list.get(position).getStrain().equals("3")){
            holder.yingbian_3.setSelected(true);
        }else {
            holder.yingbian_3.setSelected(false);
        }

        holder.item_content.setText(list.get(position).getComment());

        return convertView;
    }


    class ViewHolder{
        TextView item_title,item_time,item_content;
        LinearLayout xuexi_1,xuexi_2,xuexi_3,dongshou_1,dongshou_2,dongshou_3,sing_1,sing_2,sing_3,
                laodong_1,laodong_2,laodong_3,yingbian_1,yingbian_2,yingbian_3;
        ImageView iv_head;

        public ViewHolder(View convertView) {
            item_title = (TextView) convertView.findViewById(R.id.item_title);
            item_time = (TextView) convertView.findViewById(R.id.item_time);
            item_content = (TextView) convertView.findViewById(R.id.item_content);
            iv_head = (ImageView) convertView.findViewById(R.id.item_head);

            xuexi_1 = (LinearLayout) convertView.findViewById(R.id.xuexi_1);
            xuexi_2 = (LinearLayout) convertView.findViewById(R.id.xuexi_2);
            xuexi_3 = (LinearLayout) convertView.findViewById(R.id.xuexi_3);


            dongshou_1 = (LinearLayout) convertView.findViewById(R.id.dongshou_1);
            dongshou_2 = (LinearLayout) convertView.findViewById(R.id.dongshou_2);
            dongshou_3 = (LinearLayout) convertView.findViewById(R.id.dongshou_3);


            sing_1 = (LinearLayout) convertView.findViewById(R.id.sing_1);
            sing_2 = (LinearLayout) convertView.findViewById(R.id.sing_2);
            sing_3 = (LinearLayout) convertView.findViewById(R.id.sing_3);


            laodong_1 = (LinearLayout) convertView.findViewById(R.id.laodong_1);
            laodong_2 = (LinearLayout) convertView.findViewById(R.id.laodong_2);
            laodong_3 = (LinearLayout) convertView.findViewById(R.id.laodong_3);


            yingbian_1 = (LinearLayout) convertView.findViewById(R.id.yingbian_1);
            yingbian_2 = (LinearLayout) convertView.findViewById(R.id.yingbian_2);
            yingbian_3 = (LinearLayout) convertView.findViewById(R.id.yingbian_3);


        }
    }
}
