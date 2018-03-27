package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.ExamDetial;
import cn.xiaocool.dezhischool.bean.Examinfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.CommonAdapter;
import cn.xiaocool.dezhischool.utils.ImgLoadUtil;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.Utility;
import cn.xiaocool.dezhischool.utils.ViewHolder;

public class ExamDetialActivity extends BaseActivity {

    @BindView(R.id.exam_list)
    ListView examList;
    int type;
    Context mContext;
    String examid, title;
    List<ExamDetial> list;
    private RequestQueue mQueue;
    CommonAdapter adapter;
    CommonAdapter resultsAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detial);
        ButterKnife.bind(this);
        examid = getIntent().getStringExtra("examid");
        title = getIntent().getStringExtra("title");
        setTopName(title);
        mContext = this;
        checkIdentity();
        list = new ArrayList<>();
        mQueue = Volley.newRequestQueue(mContext);
        getData();
    }

    @Override
    public void requsetData() {

    }

    private void checkIdentity() {
        if (SPUtils.get(mContext, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 0;
        } else {

            type = 1;
        }
    }

    private void getData() {
        String url;
        if (type == 0) {
            url = NetConstantUrl.ParentGetPerformance + "&exam_id=" + examid + "&studentid=" + SPUtils.get(mContext, LocalConstant.USER_BABYID, "");
        } else {
            url = NetConstantUrl.TeacherGetPerformance + "&exam_id=" + examid + "&classid=" + SPUtils.get(mContext, LocalConstant.USER_CLASSID, "");
        }
        ProgressUtil.showLoadingDialog(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String data) {
                Log.d("onResponse", data);
                ProgressUtil.dissmisLoadingDialog();
                try {

                    JSONObject jsonObject = new JSONObject(data);
                    String state = jsonObject.optString("status");
                    Log.d("attentdata", state);
                    if (state.equals("success")) {
                        String attendancedata = jsonObject.optString("data");
                        Log.d("attentdata", attendancedata);
                        Gson gson = new Gson();
                        ArrayList<ExamDetial> arrayList = gson.fromJson(attendancedata, new TypeToken<List<ExamDetial>>() {
                        }.getType());
                        list.clear();
                        list.addAll(arrayList);
                        setAdapter();
                    } else {
                        ProgressUtil.dissmisLoadingDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ProgressUtil.dissmisLoadingDialog();
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                ProgressUtil.dissmisLoadingDialog();
            }
        });
        mQueue.add(request);
    }

    private void setAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<ExamDetial>(this, list, R.layout.item_exam_detial) {
                @Override
                public void convert(ViewHolder holder, ExamDetial examDetial) {
                    holder.setText(R.id.student_name, examDetial.getName())
                            .setText(R.id.sum, "总成绩：" + examDetial.getScore_count())
                            .setText(R.id.ranking, "排名：" + examDetial.getRank());
                    ImgLoadUtil.display(NetConstantUrl.IMAGE_URL + examDetial.getPhoto(), (ImageView) holder.getView(R.id.student_avatar));
                    ListView lv = (ListView) holder.getView(R.id.item_list);
                    setResultsAdapter(lv, examDetial.getSubject());
                }

            };
            examList.setAdapter(adapter);
        }
    }

    private void setResultsAdapter(ListView lv, List<ExamDetial.SubjectBean> list) {

        resultsAdpter = new CommonAdapter<ExamDetial.SubjectBean>(this, list, R.layout.item_subject) {
            @Override
            public void convert(ViewHolder holder, ExamDetial.SubjectBean examDetial) {
                holder.setText(R.id.subject, examDetial.getSubject()+":")
                        .setText(R.id.results, examDetial.getScore());

            }
        };

        lv.setAdapter(resultsAdpter);
        Utility.setListViewHeightBasedOnChildren(lv);
    }
}
