package cn.xiaocool.dezhischool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.GridIcon;

/**
 * Created by 10835 on 2018/3/14.
 */

public class GridIconAdapter extends ArrayAdapter<GridIcon> {
    private List<GridIcon> iconList;
    private int resourceId;
    public GridIconAdapter(@NonNull Context context, int resource, @NonNull List<GridIcon> objects) {
        super(context, resource, objects);
        iconList = objects;
        resourceId =resource;
    }

    public class ViewHolder{
        private ImageView gridImageView;
        private TextView  gridTextView;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GridIcon gridIcon = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_icon,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.gridImageView = view.findViewById(R.id.iv_item_grid_icon);
            viewHolder.gridTextView =  view.findViewById(R.id.tv_item_grid_icon);
            view.setTag(viewHolder);
            view.setId(gridIcon.getViewId());

        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.gridTextView.setText(gridIcon.getName());
        viewHolder.gridImageView.setImageResource(gridIcon.getSrcId());
        return view;
    }
}
