package cn.xiaocool.dezhischool.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaocool.dezhischool.R;
import cn.xiaocool.dezhischool.utils.BaseActivity;
import cn.xiaocool.dezhischool.utils.ToastUtil;
import cn.xiaocool.dezhischool.utils.alipay.OrderInfoUtil2_0;
import cn.xiaocool.dezhischool.utils.alipay.PayResult;

public class ServiceBuyActivity extends BaseActivity {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016091001880788";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMKzst6rclrJd7Ht" +
            "FnVrJXzGbnej7vRlU+M+3/T0v3T9SEITeLDZb4WD2fkpHEa5pGMEYLxMz9MdVys8" +
            "TaXp8CvUC+Hl/lPvsz98cVJ2P7nGjCT2k+dFWqBO+WRZGta+YYzgt2q73XfXuABK" +
            "YX/n8ucOskO7Z0oyxKErZhYRT5/fAgMBAAECgYEAnA3L4A0qqvmvpjyRM6udcFTb" +
            "ValfbSOKCSnr9znz+qDHua5Bnf2pFSqJGtuIfmdtCrAHmOU37c4mf9Dlq4XFqPVz" +
            "5HjzO2epAUbrk+89TW+SEYgHsIVvpdWwoNhfLclbEzDBVA6FvRt23hDwSgrvMqoC" +
            "rJlcSFiN+ul+f5kO5YECQQDhrSFNMX1Qy66XqcTadhnLE/4TNCa+xNULQTUrjrrG" +
            "h2uSeaDgmuVaOFQ17BTGFmCtRaoCBUrOCZhLyHLjF2YTAkEA3N0a/Paw547csCj4" +
            "2lHKLu/XiS/pApGHPf7gxEcRtibgct5mC8rR9UCyq4JPKWFVDc5uE3tBBuSUaq6D" +
            "pA0IhQJBAJ/X89VsLzmR+ujbS1389on7cCOD9cl7OvbMye8/GhXSByRpV8SekcKU" +
            "UTkcLR6c7P7tb9wciX5kF2Xd5Vnp0dcCQQDCWRn3gQh4KoFNzi/0xMX5+XkbMfqD" +
            "FRYB2rdkX/lY5OraiZMYS1fnzQ+r1hXcntZeOMkqAWpeK2PiYEBcak+VAkEApL7+" +
            "UPxlp1KcKl2GP+ZX9eUmLQ7egTNJ3HuZHPYbnztjmnZV9rb8m7K2wF57TH5a1JxY" +
            "B154rmeyQcmo1hLu+A==";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.activity_service_buy_cb)
    CheckBox activityServiceBuyCb;
    @BindView(R.id.activity_service_buy_btn)
    TextView activityServiceBuyBtn;
    private Context context;

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
                        Toast.makeText(ServiceBuyActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ServiceBuyActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_buy);
        ButterKnife.bind(this);
        setTopName("服务购买");
        context = this;
    }

    @Override
    public void requsetData() {

    }

    @OnClick(R.id.activity_service_buy_btn)
    public void onClick() {
        //立即支付
        if(!activityServiceBuyCb.isChecked()){
            ToastUtil.showShort(context,"请选择支付方式！");
        }else{
            payV2();
        }
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
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ServiceBuyActivity.this);
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
}
