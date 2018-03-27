package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.SchoolAnnouceReceive;
import cn.xiaocool.dezhischool.bean.SchoolAnnouncement;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.CustomHeader;
import cn.xiaocool.dezhischool.view.NiceDialog;
import cn.xiaocool.dezhischool.view.PopWindowManager;

public class SchoolAnnounceActivity extends BaseActivity {


    @BindView(R.id.school_news_lv)
    ListView schoolNewsLv;
    @BindView(R.id.school_news_srl)
    XRefreshView schoolNewsSrl;

    private CommonAdapter adapter;
    private List<SchoolAnnouncement> schoolAnnouncements;
    private List<SchoolAnnouceReceive> schoolAnnouceReceives;
    private Context context;
    private int type;
    private int beginid = 0;
    private NiceDialog mDialog = null;
    private String delet_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_news);
        ButterKnife.bind(this);
        context = this;
        schoolAnnouncements = new ArrayList<>();
        schoolAnnouceReceives = new ArrayList<>();
        setTopName("校园公告");
        hideRightText();
        checkIdentity();
        //校长有发送权限
        if(type == 1){
            setRightImg(R.drawable.ic_fabu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(AddSchoolAnnounceActivity.class);
                }
            });
        }
        settingRefresh();
    }

    /**
     * 判断身份
     * 1-----校长
     * 2-----老师
     */
    private void checkIdentity() {
        if(SPUtils.get(context, LocalConstant.USER_IS_PRINSIPLE,"").equals("y")){
            type = 1;
        }else{
            type = 2;
        }
    }


    /**
     * 设置
     */
    private void settingRefresh() {
        schoolNewsSrl.setPullRefreshEnable(true);
        schoolNewsSrl.setPullLoadEnable(true);
        schoolNewsSrl.setCustomHeaderView(new CustomHeader(this,2000));
        schoolNewsSrl.setAutoRefresh(true);
        schoolNewsSrl.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                beginid = 0;
                requsetData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        schoolNewsSrl.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (type == 2) {
                    beginid = schoolAnnouceReceives.size();
                } else if (type == 1) {
                    beginid = schoolAnnouncements.size();
                }
                requsetData();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        schoolNewsSrl.stopLoadMore();
                    }
                }, 2000);
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
            }
        });

    }

    @Override
    public void requsetData() {
        String url = "";
//        if(type == 1){
//            url = NetConstantUrl.GET_ANNOUNCE_SEND + "&userid=" + SPUtils.get(context,LocalConstant.USER_ID,"")+"&beginid=" + beginid;
//        }else if(type == 2){
            url = NetConstantUrl.GET_ANNOUNCE_RECEIVE + "&receiverid=" + SPUtils.get(context,LocalConstant.USER_ID,"")+"&beginid=" + beginid;
//        }
        Log.e("requsetData",url);
        VolleyUtil.VolleyGetRequest(this, url, new
                VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        schoolNewsSrl.stopLoadMore();
                        schoolNewsSrl.stopRefresh();
                        if (JsonResult.JSONparser(getBaseContext(), result)) {
                            setAdapter(result);
                        }else {
                        }
                    }

                    @Override
                    public void onError() {
                        schoolNewsSrl.stopLoadMore();
                        schoolNewsSrl.stopRefresh();
                    }
                });
    }

    /**
     * 设置删除Dialog
     */
    private void setVersionDialog(String url) {
        delet_url = NetConstantUrl.SCHOOL_ANNOUNCE_DELET + url;
        mDialog = new NiceDialog(SchoolAnnounceActivity.this);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = width-300;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.setTitle("提示");
        mDialog.setContent("确认删除吗?");
        mDialog.setOKButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyUtil.VolleyGetRequest(context, delet_url, new VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (JsonResult.JSONparser(getBaseContext(), result)) {
                            requsetData();
                        }else {
                            ToastUtil.show(context , "失败" , Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onError() {
                        schoolNewsSrl.stopLoadMore();
                        schoolNewsSrl.startRefresh();

                    }
                });
                mDialog.dismiss();
            }
        });
        mDialog.setCancelButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * list 设置adapter
     * @param result
     */
    private void setAdapter(String result) {
//        if(type == 1){
//            schoolAnnouncements.clear();
//            schoolAnnouncements.addAll(getBeanFromJsonSend(result));
//            if (adapter != null) {
//                adapter.notifyDataSetChanged();
//            } else {
//                adapter = new CommonAdapter<SchoolAnnouncement>(getBaseContext(), schoolAnnouncements, R.layout.school_announcement_item) {
//                    @Override
//                    public void convert(ViewHolder holder, SchoolAnnouncement datas) {
//                        setSendItem(holder, datas);
//                    }
//                };
//                schoolNewsLv.setAdapter(adapter);
//            }
//        }else if(type == 2){
            schoolAnnouceReceives.clear();
            schoolAnnouceReceives.addAll(getBeanFromJsonReceive(result));
            Collections.sort(schoolAnnouceReceives, new Comparator<SchoolAnnouceReceive>() {
                @Override
                public int compare(SchoolAnnouceReceive lhs, SchoolAnnouceReceive rhs) {
                    return (int) (Long.parseLong(rhs.getNotice_info().get(0).getCreate_time())-Long.parseLong(lhs.getNotice_info().get(0).getCreate_time()));
                }
            });
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new CommonAdapter<SchoolAnnouceReceive>(getBaseContext(), schoolAnnouceReceives, R.layout.school_announcement_item) {
                    @Override
                    public void convert(ViewHolder holder, SchoolAnnouceReceive datas) {
                        setReceiveItem(holder, datas);
                    }
                };
                schoolNewsLv.setAdapter(adapter);
            }
//        }
    }

    /**
     * 对item 操作（发送）
     * @param holder
     * @param datas
     */
    private void setSendItem(final ViewHolder holder,final SchoolAnnouncement datas) {

        //获取图片字符串数组
        ArrayList<String> images = new ArrayList<>();
        for (int i=0;i<datas.getPic().size();i++){
            images.add(datas.getPic().get(i).getPictureurl());
        }

        //判断已读和未读

        final ArrayList<SchoolAnnouncement.ReceiveListBean> notReads = new ArrayList<>();
        final ArrayList<SchoolAnnouncement.ReceiveListBean> alreadyReads = new ArrayList<>();
        if (datas.getReceive_list().size()>0){
            for (int i=0;i<datas.getReceive_list().size();i++){
                if (datas.getReceive_list().get(i).getCreate_time()==null||datas.getReceive_list().get(i).getCreate_time().equals("0")){
                    notReads.add(datas.getReceive_list().get(i));
                }else {
                    alreadyReads.add(datas.getReceive_list().get(i));
                }
            }
        }

        holder.setText(R.id.item_sn_content, datas.getContent())
        .setTimeText(R.id.item_sn_time, datas.getCreate_time())
        .setText(R.id.item_sn_nickname, datas.getUsername())
                .setImageByUrl(R.id.item_sn_head_iv, datas.getAvatar())
        .setItemImages(this, R.id.item_sn_onepic, R.id.item_sn_gridpic, images)
        .setText(R.id.item_sn_read,"总发" + datas.getReceive_list().size()+" 已读"+alreadyReads.size()+" 未读"+notReads.size());

        //进入已读未读界面
        holder.getView(R.id.item_sn_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("yidu", alreadyReads);
                bundle.putSerializable("weidu", notReads);
                startActivity(ReadListSchoolAnnonceActivity.class, bundle);
//                startActivity(ReadListSchoolAnnonceActivity.class);
            }
        });

        //长按复制
        holder.getView(R.id.item_sn_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopWindowManager.showCopyDialg(context, (TextView) holder.getView(R.id.item_sn_content));
                return true;
            }
        });

        //判断是否本人
        if(SPUtils.get(context , LocalConstant.USER_ID ,"").toString().equals(datas.getUserid())){
            holder.setMyVisibility(R.id.item_sn_delet,1);
            holder.getView(R.id.item_sn_delet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setVersionDialog(datas.getId());
                }
            });
        }else{
            holder.setMyVisibility(R.id.item_sn_delet,2);
        }
    }

    /**
     * 对item 操作（发送）
     * @param holder
     * @param datas
     */
    private void setReceiveItem(final ViewHolder holder,final SchoolAnnouceReceive datas) {

        //获取图片字符串数组
        ArrayList<String> images = new ArrayList<>();
        for (int i=0;i<datas.getPic().size();i++){
            images.add(datas.getPic().get(i).getPhoto());
        }

        //判断已读和未读

        final ArrayList<SchoolAnnouceReceive.ReceivListBean> notReads = new ArrayList<>();
        final ArrayList<SchoolAnnouceReceive.ReceivListBean> alreadyReads = new ArrayList<>();
        if (datas.getReceiv_list().size()>0){
            for (int i=0;i<datas.getReceiv_list().size();i++){
                if (datas.getReceiv_list().get(i).getCreate_time()==null||datas.getReceiv_list().get(i).getCreate_time().equals("0")){
                    notReads.add(datas.getReceiv_list().get(i));
                }else {
                    alreadyReads.add(datas.getReceiv_list().get(i));
                }
            }
        }

        holder.setText(R.id.item_sn_content, datas.getNotice_info().get(0).getContent())
                .setTimeText(R.id.item_sn_time, datas.getNotice_info().get(0).getCreate_time())
                .setText(R.id.item_sn_nickname, datas.getNotice_info().get(0).getName())
                .setItemImages(this, R.id.item_sn_onepic, R.id.item_sn_gridpic, images)
                .setImageByUrl(R.id.item_sn_head_iv, datas.getNotice_info().get(0).getPhoto())
                .setText(R.id.item_sn_read, "总发" + datas.getReceiv_list().size() + " 已读" + alreadyReads.size() + " 未读" + notReads.size());

        //长按复制
        holder.getView(R.id.item_sn_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopWindowManager.showCopyDialg(context, (TextView) holder.getView(R.id.item_sn_content));
                return true;
            }
        });

        //判断是否本人
        if(SPUtils.get(context , LocalConstant.USER_ID ,"").toString().equals(datas.getNotice_info().get(0).getUserid())){
            holder.setMyVisibility(R.id.item_sn_delet,1);
            holder.getView(R.id.item_sn_delet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setVersionDialog(datas.getId());
                }
            });
        }else{
            holder.setMyVisibility(R.id.item_sn_delet,2);
        }
    }

    /**
     * 字符串转模型(发送)
     * @param result
     * @return
     */
    private List<SchoolAnnouncement> getBeanFromJsonSend(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<SchoolAnnouncement>>() {
        }.getType());
    }

    /**
     * 字符串转模型(接收)
     * @param result
     * @return
     */
    private List<SchoolAnnouceReceive> getBeanFromJsonReceive(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<SchoolAnnouceReceive>>() {
        }.getType());
    }
}
