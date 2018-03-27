package cn.xiaocool.dezhischool.view;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.xiaocool.dezhischool.R;

/**
 * 用于复制
 * Created by Administrator on 2016/10/21.
 */
public class PopWindowManager {
    public static void showCopyDialg(final Context context, final TextView textView){
        View view = LayoutInflater.from(context).inflate(R.layout.copy_popupwindow, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        Button popupbtn = (Button) view.findViewById(R.id.popbtn);
        popupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
//                CopyTextView();

                ClipboardManager cm =(ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                //将文本数据复制到剪贴板
                ClipData clip = ClipData.newPlainText("copy", textView.getText().toString());
                cm.setPrimaryClip(clip);
                dialog.dismiss();
                Toast.makeText(context.getApplicationContext(), "已经复制成功喽！！！", Toast.LENGTH_SHORT).show();
            }
        });

        // 弹出
        dialog.show();
    }
}
