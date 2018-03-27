package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.WebListInfo;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;


/**
 * Created by Administrator on 2016/6/17.
 */
public class WebMaxThreeAdapter extends BaseAdapter {

    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    private ArrayList<WebListInfo> list;
    private LayoutInflater inflater;
    private String type;
    public WebMaxThreeAdapter(ArrayList<WebListInfo> list, Context context) {
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.type = type;

    }

    @Override
    public int getCount() {

            return list.size()>3?3:list.size();


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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_web_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
//        holder.teacher_img.setImageResource(R.drawable.hyx_default);
        if (list.get(position).getThumb().equals("")){
            holder.teacher_img.setVisibility(View.GONE);
        }else {
            holder.teacher_img.setVisibility(View.VISIBLE);
            ImgLoadUtil.display(NetConstantUrl.WEB_IMAGE_URL + list.get(position).getThumb(), holder.teacher_img);
        }




        holder.post_title.setText(list.get(position).getPost_title());
        holder.post_date.setText(list.get(position).getPost_date());
        holder.post_content.setText(list.get(position).getPost_excerpt());
        if (list.get(position).getPost_excerpt()!=null){
            Log.e("post_content", list.get(position).getPost_excerpt());
        }

        return convertView;
    }

    public class ViewHolder {
        TextView post_title, post_content,post_date;
        ImageView teacher_img;

        public ViewHolder(View convertView) {
//            post_date = (TextView) convertView.findViewById(R.id.post_date);
            post_title = (TextView) convertView.findViewById(R.id.post_title);
            post_content = (TextView) convertView.findViewById(R.id.post_content);

        }
    }
}
