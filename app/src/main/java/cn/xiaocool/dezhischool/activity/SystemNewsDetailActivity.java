package cn.xiaocool.dezhischool.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.app.MyApplication;
import cn.xiaocool.dezhischool.bean.SystemNews;
import cn.xiaocool.dezhischool.net.LocalConstant;
import cn.xiaocool.dezhischool.net.NetConstantUrl;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.view.SharePopupWindow;

public class SystemNewsDetailActivity extends BaseActivity {

    @BindView(R.id.swd_webview)
    WebView swdWebview;
    private SystemNews systemNews;
    SharePopupWindow takePhotoPopWin;
    private   int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_web_detail);
        ButterKnife.bind(this);

        systemNews = (SystemNews) getIntent().getSerializableExtra(LocalConstant.WEB_FLAG);
        setTopName(systemNews.getPost_title());
        setRightText("分享").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom(v);
            }
        });
    }

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(swdWebview, Gravity.BOTTOM, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    setting();
                    break;
                case R.id.dongtai:
                    history();
                    break;
            }
        }
    };
    @Override
    public void requsetData() {
        String schoolid = systemNews.getId();

        String url = NetConstantUrl.SYSTEM_H5 + schoolid;
        swdWebview.loadUrl(url);
        swdWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回的是true，说明是使用webview打开的网页。否则使用系统默认的浏览器
                return true;
            }
        });

    }

    /**
     * 分享到微信好友
     */
    private void setting() {

        ToastUtil.showShort(this, "分享到微信好友");
        flag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        ToastUtil.showShort(this, "分享到微信朋友圈");
        flag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
    }



    /**
     * 微信分享网页
     * */
    private void shareWX() {
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage =new WXWebpageObject();
        webpage.webpageUrl= NetConstantUrl.SYSTEM_H5 + systemNews.getId();
        WXMediaMessage msg =new WXMediaMessage(webpage);
        msg.title=systemNews.getPost_title();
        msg.description= systemNews.getPost_excerpt();
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "分享";
        req.message = msg;
        req.scene = flag==0? SendMessageToWX.Req.WXSceneSession: SendMessageToWX.Req.WXSceneTimeline;
        MyApplication application = MyApplication.getInstance();
        application.api.sendReq(req);
    }

}
