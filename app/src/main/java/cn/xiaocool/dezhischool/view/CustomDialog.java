package cn.xiaocool.dezhischool.view;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import cn.xiaocool.dezhischool.R;


public class CustomDialog extends AlertDialog{
	
	public CustomDialog(Context context) {
		super(context, R.style.CustomDialog);
	}
	

	public CustomDialog(Context context, boolean cancelable,
						OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}


	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar_loading_layout);
		setCanceledOnTouchOutside(false);
	}
	
	

}
