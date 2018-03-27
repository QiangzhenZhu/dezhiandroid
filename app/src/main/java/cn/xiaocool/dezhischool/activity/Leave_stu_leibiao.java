package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.Leave_stu_bean;
import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.SPUtils;

/**
 * Created by mac on 2017/6/15.
 */

public class Leave_stu_leibiao extends BaseActivity {


    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    private Leave_stu_Adapter leave_stu_adapter;
    private RelativeLayout up_jiantou;
    private ListView leave_listView_stu;
    private Context context;
    private List<Leave_stu_bean.DataEntity> list_data = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case CommunalInterfaces.LEAVE_STU:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");
                    Log.e("reason", status);
                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        Log.e("reason", "Array.length" + Array.length());
                        list_data.clear();
                        if (Array != null) {
                            for (int i = 0; i < Array.length(); i++) {
                                JSONObject Object = Array.optJSONObject(i);
                                Leave_stu_bean.DataEntity leave_stu_bean = new Leave_stu_bean().new DataEntity();
                                leave_stu_bean.setId(Object.optString("id"));
                                leave_stu_bean.setStudentid(Object.optString("studentid"));
                                leave_stu_bean.setUserid(Object.optString("userid"));
                                leave_stu_bean.setAppellation_id(Object.optString("appellation_id"));
                                leave_stu_bean.setRelation_rank(Object.optString("relation_rank"));
                                leave_stu_bean.setPreferred(Object.optString("preferred"));
                                leave_stu_bean.setTime(Object.optString("time"));
                                leave_stu_bean.setStudentname(Object.optString("studentname"));
                                leave_stu_bean.setStudentavatar(Object.optString("studentavatar"));
                                list_data.add(leave_stu_bean);

                            }

                        }
                        leave_stu_adapter = new Leave_stu_Adapter();
                        leave_listView_stu.setAdapter(leave_stu_adapter);


                    }


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_stu_leibiao);
        context = this;
        hideTopView();
        init();


    }

    @Override
    public void requsetData() {

    }

    private void init() {
        leave_listView_stu = (ListView) findViewById(R.id.leave_list_stu);

        new SendRequest(context, handler).getLeave_stu(SPUtils.get(context, LocalConstant.USER_ID,"")+"");
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        leave_listView_stu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String name = list_data.get(position).getStudentname();
                String studentid = list_data.get(position).getStudentid();
                Intent intent = new Intent();
                intent.putExtra("studentid", studentid);
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();


            }
        });


    }


    class Leave_stu_Adapter extends BaseAdapter {

        public Leave_stu_Adapter() {

            displayImage = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                    .cacheInMemory(true).cacheOnDisc(true).build();


        }

        @Override
        public int getCount() {
            return list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return list_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_leave_stu, null);
                vh = new ViewHolder(convertView);

                convertView.setTag(vh);


            } else {
                vh = (ViewHolder) convertView.getTag();
            }


            if (list_data.get(position).getStudentavatar().length() > 1) {
                vh.image_user.setVisibility(View.VISIBLE);
                imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
                imageLoader.displayImage("http://dezhi.xiaocool.net/uploads/avatar/" + list_data.get(position).getStudentavatar(), vh.image_user, displayImage);
                Log.d("img", "http://dezhi.xiaocool.net/uploads/avatar/" + list_data.get(position).getStudentavatar());
                vh.image_user.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }


            vh.tv_name.setText(list_data.get(position).getStudentname());


            return convertView;
        }


        class ViewHolder {
            private TextView tv_name;
            private ImageView image_user;

            public ViewHolder(View convertView) {
                tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                image_user = (ImageView) convertView.findViewById(R.id.image_user);

            }
        }


    }


}
