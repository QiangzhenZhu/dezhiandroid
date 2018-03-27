package cn.xiaocool.dezhischool.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

//import com.maiml.wechatrecodervideolibrary.recoder.WechatRecoderActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.callback.PushVideo;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.SendRequest;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.JsonResult;
import cn.xiaocool.dezhischool.utils.ProgressUtil;
import cn.xiaocool.dezhischool.utils.PushVideoUtil;
import cn.xiaocool.dezhischool.utils.SPUtils;
import cn.xiaocool.dezhischool.utils.ToastUtil;

public class PostVideoActivity extends BaseActivity {
    @BindView(R.id.activity_post_trend_ed_content)
    EditText activityPostTrendEdContent;
    @BindView(R.id.addsn_tv_choose_class)
    TextView addsnTvChooseClass;
    @BindView(R.id.tv_select_count)
    TextView tvSelectCount;
    @BindView(R.id.videoview)
    VideoView videoview;
    private Context context;
    private String videoPath;
    private MediaController mediaController;
    private String userid, classid;
    private int type;
    private static final int REQ_CODE = 10001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x110:
                    ProgressUtil.dissmisLoadingDialog();
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))) {
                            finish();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WechatRecoderActivity.launchActivity(this,REQ_CODE);
        setContentView(R.layout.activity_post_video);
        ButterKnife.bind(this);
        setTopName("新增动态");
        setRightText("发布").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTrend();
            }
        });
        context = this;

        userid = SPUtils.get(context, LocalConstant.USER_ID, "").toString();
        classid = null;
//        classid = SPUtils.get(context, LocalConstant.USER_CLASSID, "").toString();
        checkIdentity();
    }

    /**
     * 判断身份
     * 1-----家长
     * 2-----校长
     * 3-----老师
     */
    private void checkIdentity() {
        if (SPUtils.get(context, LocalConstant.USER_TYPE, "").equals("0")) {
            type = 1;
        } else if (SPUtils.get(context, LocalConstant.USER_IS_PRINSIPLE, "").equals("y")) {
            type = 2;
        } else {
            type = 3;
        }
    }


    private void sendTrend() {
        if (activityPostTrendEdContent.getText().toString().trim().equals("")) {
            ToastUtil.showShort(context, "发送内容不能为空！");
            return;
        }
        if (classid == null) {
            ToastUtil.showShort(context, "请选择班级！");
            return;
        }

        if (videoPath==null||videoPath.equals("")){
            ToastUtil.showShort(context,"无视频,不能发布!");
            return;
        }



        ProgressUtil.showLoadingDialog(PostVideoActivity.this);

        new PushVideoUtil().pushVideo(context, videoPath, new PushVideo() {
            @Override
            public void success(String videoName, String imageName) {
                new SendRequest(context, handler).send_trend(userid,
                        SPUtils.get(context, LocalConstant.SCHOOL_ID, "1").toString(),
                        classid, activityPostTrendEdContent.getText().toString(), "null",videoName,imageName, 0x110);
            }

            @Override
            public void error() {
                ProgressUtil.dissmisLoadingDialog();
                ToastUtil.showShort(context,"上传失败，请重试!");
            }
        });
    }

    @Override
    public void requsetData() {

    }


    @OnClick(R.id.addsn_tv_choose_class)
    public void onClick() {
        Intent intent = new Intent(PostVideoActivity.this, ChooseClassActivity.class);
        intent.putExtra("type", "");
        startActivityForResult(intent, 101);
    }

    /**
     * 获取返回的接收人
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 101:
                    if (data != null) {
                        ArrayList<String> ids = data.getStringArrayListExtra("ids");
                        ArrayList<String> names = data.getStringArrayListExtra("names");
                        String haschoose = "";
                        for (int i = 0; i < names.size(); i++) {
                            if (i < 3) {
                                if (names.get(i) != null || names.get(i) != "null") {
                                    haschoose = haschoose + names.get(i) + "、";
                                }
                            } else if (i == 4) {
                                haschoose = haschoose.substring(0, haschoose.length() - 1);
                                haschoose = haschoose + "等...";
                            }

                        }

                        classid = null;
                        for (int i = 0; i < ids.size(); i++) {
                            classid = classid + "," + ids.get(i);
                        }
                        classid = classid.substring(5, classid.length());
                        tvSelectCount.setText("共选择" + ids.size() + "个班级");
//                        addsnTvChooseClass.setText(haschoose);
                    }

                    break;
                case REQ_CODE:
                   // String videoPath = data.getStringExtra(WechatRecoderActivity.VIDEO_PATH);

                    this.videoPath = videoPath;
                    Log.d("onActivityResult", "onActivityResult: "+videoPath);
                    play(videoPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void play(final String path) {


        mediaController = new MediaController(this);
        videoview.setVideoPath(path);
        // 设置VideView与MediaController建立关联
        videoview.setMediaController(mediaController);
//        // 设置MediaController与VideView建立关联
        mediaController.setMediaPlayer(videoview);
        mediaController.setVisibility(View.INVISIBLE);
        // 让VideoView获取焦点
//        videoView.requestFocus();
        // 开始播放
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.setVideoPath(path);
                videoview.start();
            }
        });

        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {


                return false;
            }
        });
    }
}
