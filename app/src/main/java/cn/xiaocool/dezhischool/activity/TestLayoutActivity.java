package cn.xiaocool.dezhischool.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.Date;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.view.RefreshLayout;

public class TestLayoutActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swiperefreshlayout)
    RefreshLayout swiperefreshlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);
        ButterKnife.bind(this);
        //listview数据
        final LinkedList<String> listItems = new LinkedList<String>();
        for (int i = 0; i < 5; i++)
            listItems.add("Item - " + i);

        //listview适配器
        final BaseAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        swiperefreshlayout.setColorSchemeResources(R.color.white);
        swiperefreshlayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.themeColor));
        swiperefreshlayout.setProgressViewOffset(true, 10, 100);
        //下拉刷新
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swiperefreshlayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listItems.addFirst(new Date().toGMTString());
                        adapter.notifyDataSetChanged();

                        //关闭动画
                        swiperefreshlayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        //上拉加载
        swiperefreshlayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                swiperefreshlayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listItems.add(new Date().toGMTString());
                        adapter.notifyDataSetChanged();
                        swiperefreshlayout.setLoading(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void requsetData() {

    }
}
