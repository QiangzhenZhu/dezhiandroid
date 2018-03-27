package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cn.xiaocool.dezhischool.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ListRelationShipActivity extends BaseActivity {

    @BindView(R.id.tv_select_father)
    LinearLayout tvSelectFather;
    @BindView(R.id.tv_select_mother)
    LinearLayout tvSelectMother;
    @BindView(R.id.tv_select_grandfather)
    LinearLayout tvSelectGrandfather;
    @BindView(R.id.tv_select_grandmother)
    LinearLayout tvSelectGrandmother;
    @BindView(R.id.tv_select_uncle)
    LinearLayout tvSelectUncle;
    @BindView(R.id.tv_select_aunt)
    LinearLayout tvSelectAunt;
    @BindView(R.id.tv_select_maternal_grandmom)
    LinearLayout tvSelectMaternalGrandmom;
    @BindView(R.id.tv_select_maternal_grandpap)
    LinearLayout tvSelectMaternalGrandpap;
    @BindView(R.id.et_relationship_appellation)
    EditText etRelationshipAppellation;
    private RelativeLayout mRight;
    private String relationAppellation;
    private Context mContext;
    private String customRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_relation_ship);
        ButterKnife.bind(this);
        setTopName("关系列表");
        mContext = this;
        mRight = (RelativeLayout) setRightText("保存");
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relationAppellation = etRelationshipAppellation.getText().toString().trim();
                if (TextUtils.isEmpty(relationAppellation)) {
                    Toast.makeText(mContext,"请输入与学生的关系",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    setResult(relationAppellation);
                }
            }
        });

    }

    @Override
    public void requsetData() {

    }


    @OnClick({R.id.tv_select_father, R.id.tv_select_mother, R.id.tv_select_grandfather, R.id.tv_select_grandmother, R.id.tv_select_uncle, R.id.tv_select_aunt, R.id.tv_select_maternal_grandmom, R.id.tv_select_maternal_grandpap, R.id.et_relationship_appellation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_father:
                setResult("爸爸");
                break;
            case R.id.tv_select_mother:
                setResult("妈妈");
                break;
            case R.id.tv_select_grandfather:
                setResult("爷爷");
                break;
            case R.id.tv_select_grandmother:
                setResult("奶奶");
                break;
            case R.id.tv_select_uncle:
                setResult("叔叔");
                break;
            case R.id.tv_select_aunt:
                setResult("阿姨");
                break;
            case R.id.tv_select_maternal_grandmom:
                setResult("姥姥");
                break;
            case R.id.tv_select_maternal_grandpap:
                setResult("姥爷");
                break;

        }
    }

    public void setResult(String appliation){
        Intent intent = new Intent();
        intent.putExtra("APPLIATION",appliation);
        setResult(RESULT_OK,intent);
        finish();
    }
}
