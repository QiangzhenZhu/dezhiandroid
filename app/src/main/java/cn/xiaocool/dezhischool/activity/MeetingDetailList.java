package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.MettingAdapter;
import cn.xiaocool.dezhischool.adapter.MettingDetailAdapter;
import cn.xiaocool.dezhischool.adapter.SpaceItemDecoration;
import cn.xiaocool.dezhischool.bean.Meeting_people_detail;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class MeetingDetailList extends BaseActivity {
    private RecyclerView recyclerView;
    private Context mContext;
    private TextView countText;
    private TextView countTextGreen;
    private int type;
    private Context context;
    private List<Meeting_people_detail> details;
    private MettingDetailAdapter adapter;
    private ListView mListView;
    private List<String> list;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail_list);
        countText = findViewById(R.id.count_text);
        countTextGreen = findViewById(R.id.meeting_people_count);
        mListView = findViewById(R.id.lv_meeting_detail_list);
        list = new ArrayList<>();

        hideRightText();
        context = this;
        type = getIntent().getIntExtra("type",0);
        details = (List<Meeting_people_detail>) getIntent().getSerializableExtra("peopleList");
        countTextGreen.setText(details.size()+"");
        for (int i = 0; i <details.size() ; i++) {
            list.add(details.get(i).getName());
        }
        mContext = this;
        recyclerView = findViewById(R.id.rv_meeting_detail_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        setTopName();

    }
    public void setTopName(){
        switch (type){
            case 0:
                setTopName("参会人员");
                countText.setText("参会人员:");
                recyclerView.setVisibility(View.GONE);
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                mListView.setAdapter(arrayAdapter);
                break;
            case 1:
                setTopName("已读");
                countText.setText("已读:");
                recyclerView.setVisibility(View.GONE);
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                mListView.setAdapter(arrayAdapter);
                break;
            case 2:
                setTopName("应到");
                countText.setText("应到:");
                recyclerView.setVisibility(View.GONE);
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                mListView.setAdapter(arrayAdapter);
                break;
            case 3:
                setTopName("实到");
                countText.setText("实到:");
                mListView.setVisibility(View.GONE);
                adapter = new MettingDetailAdapter(details);
                recyclerView.setAdapter(adapter);
                break;
            case 4:
                setTopName("迟到");
                countText.setText("迟到:");
                mListView.setVisibility(View.GONE);
                adapter = new MettingDetailAdapter(details);
                recyclerView.setAdapter(adapter);
                break;
            case 5:
                setTopName("请假");
                countText.setText("请假:");
                mListView.setVisibility(View.GONE);
                adapter = new MettingDetailAdapter(details);
                recyclerView.setAdapter(adapter);
                break;
            case 6:
                setTopName("早退");
                countText.setText("早退");
                mListView.setVisibility(View.GONE);
                adapter = new MettingDetailAdapter(details);
                recyclerView.setAdapter(adapter);
                break;
            case 7:
                setTopName("缺勤");
                countText.setText("缺勤:");
                recyclerView.setVisibility(View.GONE);
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                mListView.setAdapter(arrayAdapter);
                break;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //if ()
    }
    public void setOrnotifyAdapter(){
        if (adapter == null){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //adapter = new MettingAdapter(mInfos);
            recyclerView.addItemDecoration(new SpaceItemDecoration(16));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void requsetData() {

    }
}
