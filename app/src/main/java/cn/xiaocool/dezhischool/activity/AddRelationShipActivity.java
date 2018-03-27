package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.bean.RelationShipInfo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.OkhttpUtil;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddRelationShipActivity extends BaseActivity {


    @BindView(R.id.et_relationship_name)
    EditText etRelationshipName;
    @BindView(R.id.iv_selecr_relationship)
    ImageView ivSelecrRelationship;
    @BindView(R.id.et_relationship_phone)
    EditText etRelationshipPhone;
    @BindView(R.id.cb_first_relationship)
    CheckBox cbFirstRelationship;
    @BindView(R.id.btn_add_relation_ship)
    TextView btnAddRelationShip;
    @BindView(R.id.tv_selected_relationship)
    TextView tvSelectedRelationship;
    private Context mContext;
    private String parentName;
    private String phone;
    private String application;
    private boolean isFirstChecked;
    private String setMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relation_ship);
        ButterKnife.bind(this);
        mContext = this;
        setTopName("邀请家人");
        hideRightText();
        cbFirstRelationship.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFirstChecked = isChecked;
            }
        });
    }

    @Override
    public void requsetData() {

    }
    public void addRelationShip(){
        Uri uri= Uri.parse(NetConstantUrl.ADD_INVITE_FAMILY)
                .buildUpon()
                .appendQueryParameter("studentid", (String) SPUtils.get(mContext, LocalConstant.USER_BABYID,""))
                .appendQueryParameter("parentname",parentName)
                .appendQueryParameter("appellation",application)
                .appendQueryParameter("phone",phone)
                .appendQueryParameter("photo","weixiaotong.png")
                .build();
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            private String error = "";
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"网络加载错误，请稍后再试",Toast.LENGTH_SHORT).show();
                        ProgressUtil.dissmisLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)){
                    try {
                        JSONObject object = new JSONObject(result);
                        setMain = object.getString("data");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressUtil.dissmisLoadingDialog();
                                if (isFirstChecked){
                                    setMainParent();
                                }else {
                                    finish();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);
                        String data = object.getString("data");
                        error = data;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();

                            ProgressUtil.dissmisLoadingDialog();
                        }
                    });

                }
            }
        });
    }

    @OnClick({R.id.iv_selecr_relationship, R.id.cb_first_relationship, R.id.btn_add_relation_ship})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_selecr_relationship:
                intent = new Intent(mContext, ListRelationShipActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_add_relation_ship:
                check();
                ProgressUtil.showLoadingDialog(mContext);
                addRelationShip();
                break;
            default:
                break;
        }
    }
    public void setMainParent(){
        Uri uri= Uri.parse(NetConstantUrl.SET_MAIN_PARENT)
                .buildUpon()
                .appendQueryParameter("studentid", (String) SPUtils.get(mContext, LocalConstant.USER_BABYID,""))
                .appendQueryParameter("userid",setMain)
                .build();
        ProgressUtil.showLoadingDialog(mContext);
        OkhttpUtil.sendOkHttpRequest(uri.toString(), new Callback() {
            private String error = "";
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"网络加载错误，请稍后再试",Toast.LENGTH_SHORT).show();
                        ProgressUtil.dissmisLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (OkhttpUtil.isSuccess(result)){
                    try {
                        JSONObject object = new JSONObject(result);
                        final String  data = object.getString("data");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext,data,Toast.LENGTH_SHORT).show();
                                ProgressUtil.dissmisLoadingDialog();
                                finish();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);
                        String data = object.getString("data");
                        error = data;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();

                            ProgressUtil.dissmisLoadingDialog();
                        }
                    });

                }
            }
        });
    }

    public void check(){
        parentName = etRelationshipName.getText().toString().trim();
        phone = etRelationshipPhone.getText().toString().trim();
        if (TextUtils.isEmpty(application)){
            Toast.makeText(mContext,"请填写与学生的关系",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(parentName)){
            Toast.makeText(mContext,"请填写家长姓名",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(mContext,"请填写手机号",Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void setResult(){
        Intent intent = new Intent();
        intent.putExtra("DEFAULT_FIRST_RELATION_SHIP",isFirstChecked);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (RESULT_OK == resultCode){
                application = data.getStringExtra("APPLIATION");
                tvSelectedRelationship.setText(application);

            }
        }
    }
}
