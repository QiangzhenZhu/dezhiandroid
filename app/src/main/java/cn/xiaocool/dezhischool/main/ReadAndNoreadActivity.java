package cn.xiaocool.dezhischool.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.ReadOrNotReadAdapter;
import cn.xiaocool.dezhischool.bean.LikeBean;
import cn.xiaocool.dezhischool.bean.NewsGroupRecive;
import cn.xiaocool.dezhischool.bean.NoticeRecive;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.view.NoScrollListView;

/**
 * Created by hzh on 17/3/21.
 */

public class ReadAndNoreadActivity extends BaseActivity {

    private NoScrollListView already_list,notready_list;
    private ArrayList<LikeBean> notReads,alreadyReads;
    private ArrayList<NewsGroupRecive.DataBean.ReceiverBean> rnotReads,ralreadyReads;
    private ArrayList<NoticeRecive.DataBean.ReceivListBean> nnotReads,nalreadyReads;
    private RelativeLayout up_jiantou;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_and_noread);

        getListByIntent();
        initView();


    }

    @Override
    public void requsetData() {

    }


    private void getListByIntent() {
        Intent intent = this.getIntent();

        type = intent.getStringExtra("type");

        if (type.equals("recive")){
            ralreadyReads =  (ArrayList<NewsGroupRecive.DataBean.ReceiverBean>)intent.getSerializableExtra("alreadyReads");
            rnotReads = (ArrayList<NewsGroupRecive.DataBean.ReceiverBean>)intent.getSerializableExtra("notReads");
        }else if (type.equals("recivenotice")){
            nalreadyReads =  (ArrayList<NoticeRecive.DataBean.ReceivListBean>)intent.getSerializableExtra("alreadyReads");
            nnotReads = (ArrayList<NoticeRecive.DataBean.ReceivListBean>)intent.getSerializableExtra("notReads");

        }else {
            alreadyReads =  (ArrayList<LikeBean>)intent.getSerializableExtra("alreadyReads");
            notReads = (ArrayList<LikeBean>)intent.getSerializableExtra("notReads");
        }



    }

    private void initView() {
        already_list = (NoScrollListView) findViewById(R.id.already_list);
        notready_list = (NoScrollListView) findViewById(R.id.notready_list);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (type.equals("recive")){
//            already_list.setAdapter(new ReciveReadOrNotReadAdapter(ralreadyReads,this,"0"));
//            notready_list.setAdapter(new ReciveReadOrNotReadAdapter(rnotReads,this,"1"));

        }else if (type.equals("recivenotice")){
//            already_list.setAdapter(new NoticeReciveReadOrNotReadAdapter(nalreadyReads,this,"0"));
//            notready_list.setAdapter(new NoticeReciveReadOrNotReadAdapter(nnotReads,this,"1"));
        }else {
            already_list.setAdapter(new ReadOrNotReadAdapter(alreadyReads,this,"0"));
            notready_list.setAdapter(new ReadOrNotReadAdapter(notReads,this,"1"));
        }


    }


}