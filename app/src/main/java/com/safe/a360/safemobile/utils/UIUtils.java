/*
package com.safe.a360.safemobile.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.a360.safemobile.R;

public class UIUtils {
	private static View view;
	private static WindowManager mWM;
	private static WindowManager.LayoutParams params;
	*/
/**
	 * 
	 * @param ccontext 上下文
	 * @param text 文本内容
	 * @param style Toast样式
	 *//*

	public static void showToast(Context ccontext, String text, int style) {
		mWM = (WindowManager) ccontext.getSystemService(Context.WINDOW_SERVICE);


		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;// 电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
		//params.gravity = Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
		// 也就是(0,0)从左上方开始,而不是默认的重心位置
		params.setTitle("Toast");
		
		// view = new TextView(this);
		view = View.inflate(ccontext, R.layout.toast_address, null);

		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };

		view.setBackgroundResource(bgs[style]);// 根据存储的样式更新背景

		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		imageView.setVisibility(ImageView.GONE);
		tvText.setTextSize(15);
		tvText.setText(text);
		mWM.addView(view, params);// 将view添加在屏幕上(Window)

	}
}
*/
