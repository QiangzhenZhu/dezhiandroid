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
import cn.xiaocool.dezhischool.bean.SchoolNewsReceiver;
import cn.xiaocool.dezhischool.bean.SchoolNewsSend;
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

public class SchoolNewsActivity extends BaseActivity {


    @BindView(R.id.school_news_lv)
    ListView schoolNewsLv;
    @BindView(R.id.school_news_srl)
    XRefreshView schoolNewsSrl;
    private Context context;
    private int type;
    private int beginid = 0;
    private NiceDialog mDialog = null;
    private String delet_url;

    private CommonAdapter adapter;
    private List<SchoolNewsSend> schoolNewsSendList;
    private List<SchoolNewsReceiver> schoolNewsReceiverList;
    private List<SchoolNewsReceiver.ReceiveBean> receiveBeans;
    public static final String TAG = "学校消息";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_news);
        ButterKnife.bind(this);
        schoolNewsSendList = new ArrayList<>();
        schoolNewsReceiverList = new ArrayList<>();
        receiveBeans = new ArrayList<>();
        setTopName("学校消息");
        hideRightText();
        context = this;
        //判断身份
        checkIdentity();
        //如果是校长，显示右侧发布学校消息按钮
        if(type == 2) {
            setRightImg(R.drawable.ic_fabu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(AddSchoolNewsActivity.class);
                }
            });
        }
        settingRefresh();
    }

    /**
     * 判断身份
     * 1----家长
     * 2----校长
     * 3----老师
     */
    private void checkIdentity() {
        if(SPUtils.get(context, LocalConstant.USER_TYPE,"").equals("0")){
            type = 1;
        }else if(SPUtils.get(context,LocalConstant.USER_IS_PRINSIPLE,"").equals("y")){
            type = 2;
        }else{
            type =3;
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
                if(type == 1||type ==3){
                    beginid = schoolNewsReceiverList.size();
                }else if (type == 2){
                    beginid = schoolNewsSendList.size();
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
        String schoolid = (String) SPUtils.get(context,LocalConstant.SCHOOL_ID,"");
        String classid = (String) SPUtils.get(context,LocalConstant.USER_CLASSID,"");
        //判断身份并请求对应数据 chuanru classid
        if(type == 1){
            url = NetConstantUrl.GET_SCHOOL_NEWS_RECEIVE + "&userid=" + SPUtils.get(context,LocalConstant.USER_ID,"")+"&beginid="+beginid + "&classid=" + classid;
        }else if(type == 2){
            url = NetConstantUrl.GET_SCHOOL_NEWS_SEND + "&send_user_id=" +SPUtils.get(context,LocalConstant.USER_ID,"")+"&beginid=" + beginid + "&schoolid=" + schoolid;
        }else if(type == 3){
            url = NetConstantUrl.GET_SCHOOL_NEWS_RECEIVE + "&userid=" + SPUtils.get(context,LocalConstant.USER_ID,"") + "&type=1"+"&beginid=" + beginid +"&classid=" + classid;
        }
        Log.e(TAG, url);
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
        delet_url = NetConstantUrl.SCHOOL_MNEWS_DELET + url;
        mDialog = new NiceDialog(SchoolNewsActivity.this);
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
        if(type == 1||type ==3){
            schoolNewsReceiverList.clear();
            schoolNewsReceiverList.addAll(getBeanFromJsonReceive(result));
            receiveBeans.clear();
            receiveBeans.addAll(changeBean(schoolNewsReceiverList));
            Collections.sort(receiveBeans, new Comparator<SchoolNewsReceiver.ReceiveBean>() {
                @Override
                public int compare(SchoolNewsReceiver.ReceiveBean lhs, SchoolNewsReceiver.ReceiveBean rhs) {
                    return (int) (Long.parseLong(rhs.getSend_message().getMessage_time()) - Long.parseLong(lhs.getSend_message().getMessage_time()));
                }
            });
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new CommonAdapter<SchoolNewsReceiver.ReceiveBean>(getBaseContext(), receiveBeans, R.layout.school_news_item) {
                    @Override
                    public void convert(ViewHolder holder, SchoolNewsReceiver.ReceiveBean datas) {
                        setReceiveItem(holder, datas);
                    }
                };
                schoolNewsLv.setAdapter(adapter);
            }
        }else if(type == 2){
            schoolNewsSendList.clear();
            schoolNewsSendList.addAll(getBeanFromJsonSend(result));
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new CommonAdapter<SchoolNewsSend>(getBaseContext(), schoolNewsSendList, R.layout.school_news_item) {
                    @Override
                    public void convert(ViewHolder holder, SchoolNewsSend datas) {
                        setSendItem(holder, datas);
                    }
                };
                schoolNewsLv.setAdapter(adapter);
            }
        }

    }

    private List<SchoolNewsReceiver.ReceiveBean> changeBean(List<SchoolNewsReceiver> schoolNewsReceiverList) {
        List<SchoolNewsReceiver.ReceiveBean> receiver = new ArrayList<>();
        for(int i = 0;i<schoolNewsReceiverList.size();i++){
            receiver.addAll(schoolNewsReceiverList.get(i).getReceive());
        }
        return receiver;
    }


    /**
     * 对item 操作(接收)
     * @param holder
     * @param datas
     */
    private void setReceiveItem(final ViewHolder holder,final SchoolNewsReceiver.ReceiveBean datas) {

        //获取图片字符串数组
        ArrayList<String> images = new ArrayList<>();
        for (int i=0;i<datas.getPic().size();i++){
            images.add(datas.getPic().get(i).getPicture_url());
        }

        /*//判断已读和未读

        final ArrayList<SchoolNewsReceiver.ReceiverBean> notReads = new ArrayList<>();
        final ArrayList<SchoolNewsReceiver.ReceiverBean> alreadyReads = new ArrayList<>();
        if (datas.getReceiver().size()>0){
            for (int i=0;i<datas.getReceiver().size();i++){
                if (datas.getReceiver().get(i).getRead_time()==null||datas.getReceiver().get(i).getRead_time().equals("null")){
                    notReads.add(datas.getReceiver().get(i));
                }else {
                    alreadyReads.add(datas.getReceiver().get(i));
                }
            }
        }*/

        holder.setText(R.id.item_sn_content, datas.getSend_message().getMessage_content())
                .setTimeText(R.id.item_sn_time, datas.getSend_message().getMessage_time())
                .setText(R.id.item_sn_nickname,datas.getSend_message().getSend_user_name())
                .setImageByUrl(R.id.item_sn_head_iv,datas.getSend_message().getPhoto())
                .setItemImages(this, R.id.item_sn_onepic, R.id.item_sn_gridpic, images);
                //.setText(R.id.item_sn_read, "总发" + datas.getReceiver().size() + " 已读" + alreadyReads.size() + " 未读" + notReads.size());
        //长按复制
        holder.getView(R.id.item_sn_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopWindowManager.showCopyDialg(context, (TextView) holder.getView(R.id.item_sn_content));
                return true;
            }
        });

    }

    /**
     * 对item 操作(发送)
     * @param holder
     * @param datas
     */
    private void setSendItem(final ViewHolder holder, final SchoolNewsSend datas) {

        //获取图片字符串数组
        ArrayList<String> images = new ArrayList<>();
        for (int i=0;i<datas.getPicture().size();i++){
            images.add(datas.getPicture().get(i).getPicture_url());
        }

        /*//判断已读和未读

        final ArrayList<SchoolNewsSend.ReceiverBean> notReads = new ArrayList<>();
        final ArrayList<SchoolNewsSend.ReceiverBean> alreadyReads = new ArrayList<>();
        if (datas.getReceiver().size()>0){
            for (int i=0;i<datas.getReceiver().size();i++){
                if (datas.getReceiver().get(i).getRead_time()==null||datas.getReceiver().get(i).getRead_time().equals("null")){
                    notReads.add(datas.getReceiver().get(i));
                }else {
                    alreadyReads.add(datas.getReceiver().get(i));
                }
            }
        }*/

        holder.setText(R.id.item_sn_content, datas.getMessage_content())
        .setTimeText(R.id.item_sn_time, datas.getMessage_time())
        .setText(R.id.item_sn_nickname, datas.getSend_user_name())
                .setImageByUrl(R.id.item_sn_head_iv, datas.getUserphoto())
                .setItemImages(this, R.id.item_sn_onepic, R.id.item_sn_gridpic, images);
        //.setText(R.id.item_sn_read,"总发" + datas.getReceiver().size()+" 已读"+alreadyReads.size()+" 未读"+notReads.size());

        //进入已读未读界面
        /*holder.getView(R.id.item_sn_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("yidu",alreadyReads);
                bundle.putSerializable("weidu",notReads);
                startActivity(ReadListSchoolAnnonceActivity.class,bundle);
            }
        });*/

        //长按复制
        holder.getView(R.id.item_sn_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopWindowManager.showCopyDialg(context, (TextView) holder.getView(R.id.item_sn_content));
                return true;
            }
        });

        //判断是否本人
        if(SPUtils.get(context , LocalConstant.USER_ID ,"").toString().equals(datas.getSend_user_id())){
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
     * 字符串转模型（发送）
     * @param result
     * @return
     */
    private List<SchoolNewsSend> getBeanFromJsonSend(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<SchoolNewsSend>>() {
        }.getType());
    }

    /**
     * 字符串转模型（接收）
     * @param result
     * @return
     */
    private List<SchoolNewsReceiver> getBeanFromJsonReceive(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<SchoolNewsReceiver>>() {
        }.getType());
    }
}
