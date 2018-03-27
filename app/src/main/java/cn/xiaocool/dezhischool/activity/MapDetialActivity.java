package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.NewClassAttendance;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.RoundImageView;

public class MapDetialActivity extends BaseActivity {

    @BindView(R.id.item_list)
    ListView attanceList;
    NewClassAttendance classAttendance;
    @BindView(R.id.student_avatar)
    RoundImageView studentAvatar;
    @BindView(R.id.student_name)
    TextView studentName;
    CommonAdapter adapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detial);
        ButterKnife.bind(this);
        mContext = this;
        classAttendance = (NewClassAttendance) getIntent().getSerializableExtra("attendanceInfo");
        ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + classAttendance.getPhoto(), studentAvatar);
        studentName.setText(classAttendance.getName());
        setAdapter();
    }

    private void setAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {


            adapter = new CommonAdapter<NewClassAttendance.attendance_info>(mContext, classAttendance.getInfo(), R.layout.item_map_item_list) {
                @Override
                public void convert(ViewHolder viewholder, NewClassAttendance.attendance_info attendance_info) {
                    viewholder.setText(R.id.tv_map_address, attendance_info.getLocation());
                    viewholder.setText(R.id.tv_map_time, attendance_info.getTime());
//                    ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + attendance_info.g(), (ImageView) holder.getView(R.id.student_avatar));
                }
            };

        }
        attanceList.setAdapter(adapter);
    }

    @Override
    public void requsetData() {

    }
}
