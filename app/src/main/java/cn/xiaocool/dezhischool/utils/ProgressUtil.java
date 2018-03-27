package cn.xiaocool.dezhischool.utils;

import android.app.Activity;
import android.content.Context;

import cn.xiaocool.dezhischool.R;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class ProgressUtil {
    private static AppLoadingDialog loadingDialog;

    public static AppLoadingDialog getLoadingDialog(Context activity) {
        if (loadingDialog != null) {
            loadingDialog = null;
        }
        loadingDialog = new AppLoadingDialog(activity, R.style.alert_dialog);
        return loadingDialog;

//            if (loadingDialog == null) {
//                loadingDialog = new AppLoadingDialog(activity, R.style.alert_dialog);
//            }

//        return loadingDialog;
    }

    public static void showLoadingDialog(Context activity) {

        AppLoadingDialog dialog = getLoadingDialog(activity);
        dialog.show();
    }

    public static void dissmisLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
