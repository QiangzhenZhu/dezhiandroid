package cn.xiaocool.dezhischool.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.yixia.camera.util.Log;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.activity.MapActivity;
import cn.xiaocool.dezhischool.activity.PhotoActivity;
import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.NewClassAttendance;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtils;
import cn.xiaocool.dezhischool.utils.Utility;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements MapActivity.UpdataListener {


    @BindView(R.id.map_list)
    ListView mapList;
    Unbinder unbinder;
    int type;
    Context mContext;
    String addressState;

    private RequestQueue mQueue;
    private ArrayList<NewClassAttendance> AttentdataDataArrayList;
    CommonAdapter adapter;
    CommonAdapter itemAdapter;
    String begintime, endtime;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mQueue = Volley.newRequestQueue(mContext);
        AttentdataDataArrayList = new ArrayList<>();
        checkIdentity();
        getData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static MapFragment newInstance(String addressState) {

        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("addressState", addressState);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getData() {
        //获取今天的年月

        addressState = getArguments().getString("addressState");
        getAttendList(MyApplication.begintime, MyApplication.endtime, addressState);

//        textView.setText(addressState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void checkIdentity() {
        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
        } else {
            type = 2;
        }
    }

    //    /**
//     * 获取行为轨迹数据
//     */
    private void getAttendList(String begintime, String endtime, String location) {
//        ProgressUtil.showLoadingDialog( mContext);
        AttentdataDataArrayList.clear();
        if (type == 1) {
            OkHttpUtils.get().url(NetConstantUrl.ParentGetFaceInfoWithLocation)
                    .addParams("userid", SPUtils.get(mContext, LocalConstant.USER_ID, "") + "")
                    .addParams("begintime", begintime)
                    .addParams("endtime", endtime)
                    .addParams("location", location)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
//                            ProgressUtil.dissmisLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
//                            ProgressUtil.dissmisLoadingDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String state = jsonObject.optString("status");
                                String attentdata = jsonObject.optString("data");
                                Log.d("attentdata", attentdata);
                                if (state.equals("success")) {
                                    AttentdataDataArrayList.clear();
                                    List<NewClassAttendance> list = new Gson().fromJson(attentdata,
                                            new TypeToken<List<NewClassAttendance>>() {
                                            }.getType());
                                    AttentdataDataArrayList.addAll(list);
                                    ParentsetAdapter();

                                } else {
//                                    ToastUtils.ToastShort(mContext,attentdata);
                                    ParentsetAdapter();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } else {

            OkHttpUtils.get().url(NetConstantUrl.TeacherGetFaceInfoWithLocation)
                    .addParams("classid", SPUtils.get(mContext, LocalConstant.USER_CLASSID, "") + "")
                    .addParams("begintime", begintime)
                    .addParams("endtime", endtime)
                    .addParams("location", location)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ProgressUtil.dissmisLoadingDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String state = jsonObject.optString("status");
                                String attentdata = jsonObject.optString("data");
                                Log.d("attentdata", attentdata);
                                if (state.equals("success")) {

                                    List<NewClassAttendance> list = new Gson().fromJson(attentdata,
                                            new TypeToken<List<NewClassAttendance>>() {
                                            }.getType());
                                    AttentdataDataArrayList.addAll(list);

                                    ParentsetAdapter();

                                } else {
                                    AttentdataDataArrayList.clear();
                                    ParentsetAdapter();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }

    }


    /**
     * 设置适配器
     */
    private void ParentsetAdapter() {

        adapter = new CommonAdapter<NewClassAttendance>(mContext, AttentdataDataArrayList, R.layout.item_map_list) {

            @Override
            public void convert(ViewHolder holder, final NewClassAttendance attendanceInfo) {

                holder.setText(R.id.student_name, attendanceInfo.getName());
                ListView maplv = (ListView) holder.getView(R.id.item_list);
                ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto(), (ImageView) holder.getView(R.id.student_avatar));


                Log.d("AttentdataDataArrayList", attendanceInfo.getInfo().size() + "");

                itemAdapter = new CommonAdapter<NewClassAttendance.attendance_info>(mContext, attendanceInfo.getInfo(), R.layout.item_map_item_list) {
                    @Override
                    public void convert(ViewHolder viewholder, final NewClassAttendance.attendance_info attendance_info) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        long times = Long.parseLong(attendance_info.getTime());
//                                        policyData.setCreate_time(simpleDateFormat.format(new Date(time * 1000)));
                        String time = simpleDateFormat.format(new Date(times * 1000));
                        viewholder.setText(R.id.tv_map_address, attendance_info.getLocation());
                        viewholder.setText(R.id.tv_map_time, time);
                        Log.d("---photo", NetConstantUrl.IMAGE_URL + attendance_info.getNew_photo());
                        Button button = viewholder.getView(R.id.btn_parent_send);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, PhotoActivity.class);
                                intent.putExtra("photo", attendance_info.getNew_photo());
                                startActivity(intent);
                            }
                        });
                    }
                };
                ListView lv = (ListView) holder.getView(R.id.item_list);
                lv.setAdapter(itemAdapter);
                Utility.setListViewHeightBasedOnChildren(lv);


                if (attendanceInfo.getPhoto().equals("")) {
//                            holder.getView(R.id.student_avatar).setVisibility(View.VISIBLE);
                } else {
//                            holder.getView(R.id.student_avatar).setVisibility(View.VISIBLE);
                    Log.d("attphoto", NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto());
                    ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + attendanceInfo.getPhoto(), (ImageView) holder.getView(R.id.student_avatar));
                }


            }
        };
        mapList.setAdapter(adapter);
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser && getView() != null) {
//
//            getAttendList(MyApplication.begintime, MyApplication.endtime, addressState);
//        }
//    }

    @Override
    public void onUpdata(String begintime, String endtime) {
        if (getView() != null) {
            MyApplication.begintime = begintime;
            MyApplication.endtime = endtime;
            getAttendList(MyApplication.begintime, MyApplication.endtime, addressState);
            Log.d("begintime", begintime);
            Log.d("endtime", endtime);
        }

    }
}
