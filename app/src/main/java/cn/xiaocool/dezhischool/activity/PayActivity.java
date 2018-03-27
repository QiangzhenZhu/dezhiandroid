package cn.xiaocool.dezhischool.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.alipay.OrderInfoUtil2_0;
import cn.xiaocool.dezhischool.utils.alipay.PayResult;

public class PayActivity extends BaseActivity {

    /**
     * 支付宝支付业务：入参app_id
    */
    public static final String APPID = "2016090601857786";

    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALP03ErcqDafqeX2" +
            "g3/sWY0ndQgqJ/n2EV3wAfsiDcq3XK/QLY8xGkD/ScwOQOcbdcDY1zMbpEoaFhvb" +
            "PgXv5oK70+dKaHdXxY8xaudHDqWjTkNaDDvQh6rirvJzOLEYuA2wTGwe2oJzR2BK" +
            "1sgrxYX73LHUiAJxXJUi2dnVzjNZAgMBAAECgYBhxQjii1vAwaI21GXBXJGSxPsq" +
            "gmzRu0TYOot4+5lup4xt4La/8zeGVKytdpkAdsTVD3prw/rQX7ffTpA1/Y7IUmka" +
            "ckUa0+bz7725yIoww61OGMdEzcD2lH6/fmzLfDftPi8OGKo87lxeqAah6Y21SMCh" +
            "vmIAsSyhSa7zBQU3YQJBAOkrzWzNY8mBqeHxyjvibFq4KGBgOfoQ6ZXxQEcEem90" +
            "e9HoC7a3FX973VI/HONeo6oQwHtBHrG0jXGkEuMkuEUCQQDFk0mf8wOdqtcn6tD8" +
            "7+gGf0lc/xEWT5yFSVJsxCsXu3TV5ECZ468JYj7hfVN6ym2ReFnAcB9jA3PEXKs9" +
            "FtIFAkAlPXSyst8EGOxsBLT6+X+6wXzZX6UC1l6oLv7IGOQBFwaLkNcGGJmbYcoI" +
            "T8IqYjwbdk93n//p+983TjAiDQRNAkBJV4uVHJjTBoAoCTrbKhNvlgCdzPGC3rqz" +
            "n6xBQ/DbNnMAk2KHvcVULmw5OFSs2EgkIBDxJq4AC2AGHIBUhQwhAkEAkCytZo/7" +
            "GptQQV/TMuIZhi0neAQFm7+WO/LeOUxJsTCqEBE4JE8FXosqvVTtV9KoE5d072wi" +
            "K87Xqu1RN8UDFA==";

    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        };
    };

    @BindView(R.id.pay)
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        setTopName("测试支付");
    }

    @Override
    public void requsetData() {

    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @OnClick(R.id.pay)
    public void onClick() {
        payV2();
    }

}
