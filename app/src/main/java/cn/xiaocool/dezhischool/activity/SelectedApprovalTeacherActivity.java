package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.ApprovalTeacherInfo;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.net.VolleyUtil;
import cn.xiaocool.dezhischool.utils.BaseActivity;

public class SelectedApprovalTeacherActivity extends BaseActivity{
    private static final String TAG = "SelectedApprovalTeacher";
    private ListView mApprovalTeacherList;
    private List<ApprovalTeacherInfo> infos;
    private List<String> teachers;
    private ArrayAdapter<String> adapter;
    private Context mContext;
    private String schoolId;
    private String classId;
    private String teacherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_approval_teacher);
        setTopName("请选择请假老师");
        mContext = this;
        hideRightText();
        mApprovalTeacherList = findViewById(R.id.lv_apparoval_teacher);
        schoolId = getIntent().getStringExtra("schoolid");
        classId = getIntent().getStringExtra("classid");
        getApprovalTeacherInfo();
        mApprovalTeacherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                teacherId = infos.get(position).getId();
                Intent intent = new Intent();
                intent.putExtra("teacherid",teacherId);
                intent.putExtra("teachername",infos.get(position).getName());
                setResult(RESULT_OK,intent);
                finish();

            }
        });



    }

    @Override
    public void requsetData() {

    }

    public static Intent newIntent(Context context,String classid,String schoolid){
        Intent intent = new Intent(context,SelectedApprovalTeacherActivity.class);
        intent.putExtra("classid",classid);
        intent.putExtra("schoolid",schoolid);
        return intent;
    }
    /**
     *
     * @param result
     * @return
     */
    public List<ApprovalTeacherInfo> getApprovalTeacherInfo(String result){
        String data ="";
        try {
            JSONObject object = new JSONObject(result);
            data = object.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data,new TypeToken<List<ApprovalTeacherInfo>>(){}.getType());
    }

    public void getApprovalTeacherInfo(){
        Log.d(TAG, "getApprovalTeacherInfo: "+schoolId  + classId);
        final String approvalteacher = NetConstantUrl.GET_LEAVERMANAGERLIST + "&schoolid=" +schoolId+"&classid="+classId;
        VolleyUtil.VolleyGetRequest(mContext, approvalteacher, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                infos= getApprovalTeacherInfo(result);
                teachers = new ArrayList<>();
                for (int i = 0; i <infos.size() ; i++) {
                    teachers.add(infos.get(i).getName());

                }
                adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,teachers);
                mApprovalTeacherList.setAdapter(adapter);


            }

            @Override
            public void onError() {

            }
        });
    }

}
