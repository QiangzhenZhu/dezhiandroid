package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.ShortMessage;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ViewHolder;
import cn.xiaocool.dezhischool.view.CustomHeader;
import cn.xiaocool.dezhischool.view.PopWindowManager;

public class MessageActivity extends BaseActivity {


    @BindView(R.id.school_news_lv)
    ListView schoolNewsLv;
    @BindView(R.id.school_news_srl)
    XRefreshView schoolNewsSrl;

    private CommonAdapter adapter;
    private List<ShortMessage> shortMessages;
    private Context context;
    private View rightLocation;
    private int beginid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_news);
        ButterKnife.bind(this);
        shortMessages = new ArrayList<>();
        context = this;
        setTopName("短信列表");
        rightLocation = setRightImg(R.drawable.ic_fabu);
        rightLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPUtils.get(context, LocalConstant.USER_IS_PRINSIPLE, "").equals("y")) {
                    showPopupMenu();
                    return;
                }
                startActivity(AddGroupMessageActivity.class);
            }
        });
        settingRefresh();
    }


    /**
     *显示选择菜单
     * */
    private void showPopupMenu() {

        //自定义布局
        View layout = LayoutInflater.from(this).inflate(R.layout.address_add_menu, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置弹出位置
        int[] location = new int[2];
        rightLocation.getLocationOnScreen(location);

        popupWindow.showAsDropDown(rightLocation);

        final TextView add_qun = (TextView)layout.findViewById(R.id.add_qun);
        TextView tong_bu = (TextView)layout.findViewById(R.id.tong_bu);

        add_qun.setText("群发教师");
        tong_bu.setText("群发学生");

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        add_qun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history();
                popupWindow.dismiss();
            }
        });
        tong_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tongbu();
                popupWindow.dismiss();
            }
        });



    }

    /**
     * 学生短信
     * */
    private void tongbu() {

        Intent intent = new Intent(this,AddGroupMessageActivity.class);
        intent.putExtra("type", "");
        startActivity(intent);
    }

    /**
     * 教师短信
     * */
    private void history() {


        Intent intent = new Intent(this,AddGroupMessageActivity.class);
        intent.putExtra("type", "teacher");
        startActivity(intent);
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
                beginid = shortMessages.size();
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
        String url = NetConstantUrl.GET_SHORT_MESSAGE + SPUtils.get(context, LocalConstant.USER_ID,"")+"&beginid=" + beginid + "&schoolid="+SPUtils.get(context, LocalConstant.SCHOOL_ID,"");
        VolleyUtil.VolleyGetRequest(this, url, new
                VolleyUtil.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (JsonResult.JSONparser(getBaseContext(), result)) {
                            schoolNewsSrl.stopLoadMore();
                            schoolNewsSrl.stopRefresh();
                            setAdapter(result);
                        } else {
                            schoolNewsSrl.stopLoadMore();
                            schoolNewsSrl.stopRefresh();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    /**
     * list 设置adapter
     * @param result
     */
    private void setAdapter(String result) {
        shortMessages.clear();
        shortMessages.addAll(getBeanFromJson(result));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<ShortMessage>(getBaseContext(), shortMessages, R.layout.short_message_item) {
                @Override
                public void convert(ViewHolder holder, ShortMessage datas) {
                    setItem(holder, datas);
                }
            };
            schoolNewsLv.setAdapter(adapter);
        }
    }

    /**
     * 对item 操作
     * @param holder
     * @param datas
     */
    private void setItem(final ViewHolder holder, ShortMessage datas) {

        holder.setText(R.id.item_sn_content, datas.getMessage_content())
                .setTimeText(R.id.item_sn_time,datas.getCreate_time())
                .setText(R.id.item_sn_nickname,datas.getName())
                .setImageByUrl(R.id.item_sn_head_iv, datas.getPhoto());
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
     * 字符串转模型
     * @param result
     * @return
     */
    private List<ShortMessage> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ShortMessage>>() {
        }.getType());
    }
}
