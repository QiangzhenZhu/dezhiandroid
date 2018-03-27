package cn.xiaocool.dezhischool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.adapter.MeetingPeopleAdapter;
import cn.xiaocool.dezhischool.bean.Child;
import cn.xiaocool.dezhischool.bean.Group;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class MeetingPeopleActivity extends BaseActivity {
    private RecyclerView mGroupReCycleView;
    private SearchView mSearchView;
    private MeetingPeopleAdapter adapter;
    private List<Group> groups;
    private List<Child> children;
    private List<Integer> counts;
    private List<Integer> Heards;
    private List<Child> realChildren;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_people);
        groups = new ArrayList<>();
        counts = new ArrayList<>();
        Heards = new ArrayList<>();
        realChildren = new ArrayList<>();
        for (int i = 0; i <3 ; i++) {
            Group group = new Group(i+"","高"+i+" "+i+"班");
            for (int j = 0; j <5 ; j++) {
                group.addChildrenItem(new Child(i+" "+j,"王"+i,"李"+i));
            }
            groups.add(group);
        }
        children = new ArrayList<>();
        for (int k = 0; k <groups.size() ; k++) {
            Group group1 = groups.get(k);
            realChildren.add(new Child(group1.getTitle(),group1.getChecked(),true));
            for (int l = 0; l <group1.getChildrenCount() ; l++) {
                realChildren.add(group1.getChildItem(l));
            }
        }
        int temp = 0;
        Heards.add(0);
        for (int i = 0; i <groups.size() ; i++) {

            if (i != groups.size()-1) {
                temp++;
                temp = temp + groups.get(i).getChildrenCount();
                Heards.add(temp);

            }

            counts.add(groups.get(i).getChildrenCount());

        }
        //只添加头部信息；
        for (int i = 0; i <Heards.size() ; i++) {
            children.add(realChildren.get(Heards.get(i)));
        }
        setTopName("选择参会人员");
        setRightText("完成");
        mGroupReCycleView = findViewById(R.id.rv_meeting_people);
        mGroupReCycleView.setLayoutManager(new LinearLayoutManager(this));
        mSearchView = findViewById(R.id.metting_search_people);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter == null){
            adapter = new MeetingPeopleAdapter(children,groups);
            adapter.setItemOnCliclkListner(new MeetingPeopleAdapter.OnItemOnCliclkListner() {
               //头部加载更多
                @Override
                public void onClick1(View view, int position) {
                        if (!children.get(position).isFlag()) {
                            int temp = position;
                            int heard = 0;
                            while (position - counts.get(heard) > 0) {
                                heard = heard + 1;
                                position = position - 1;
                            }
                            children.addAll(position + 1, groups.get(heard).getChildren());
                            children.get(position + counts.get(heard)).setFlag(!children.get(position).isFlag());
                        } else {
                            //刷新展开按钮
                            children.get(position).setFlag(!children.get(position).isFlag());
                            //
                        }
                    //adapter.notifyItemRangeInserted(position+1,counts.get(heard));
                    adapter.notifyDataSetChanged();
                }
                //Child 布局
                @Override
                public void onClick2(View view, int position) {
                    children.get(position).setChecked(!(children.get(position).getChecked()));
                    adapter.notifyDataSetChanged();
                }
                //头布局 选择全部
                @Override
                public void onClick3(View view, int position) {
                    children.get(position).setSelectedAll(!children.get(position).isSelectedAll());
                    adapter.notifyDataSetChanged();
                }
            });
            mGroupReCycleView.setAdapter(adapter);
        }if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requsetData() {

    }
}
