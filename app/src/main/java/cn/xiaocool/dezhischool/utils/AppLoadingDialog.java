/**
 * 
 */
package cn.xiaocool.dezhischool.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import cn.xiaocool.dezhischool.R;


public class AppLoadingDialog extends Dialog{

	/**
	 * @param context
	 * @param theme
	 */
	public AppLoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_dialog);
		setCanceledOnTouchOutside(false);
		
	}
	
}
