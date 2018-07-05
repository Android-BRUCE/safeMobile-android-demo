package com.safe.a360.safemobile.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safe.a360.safemobile.R;

public class DragViewActivity extends Activity {
	
	private ImageView ivView;
	private TextView tvTop;
	private TextView tvBottom;
	private SharedPreferences mPref;
	long[] mHits = new long[2];// 数组长度表示要点击的次数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_view_activity);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		ivView = (ImageView) findViewById(R.id.iv_drag);
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		
		//获取屏幕的宽高
		final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		final int winHeight = getWindowManager().getDefaultDisplay().getHeight();
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		//根据图片的位置，决定提示框显示和隐藏
		if(lastY > winHeight / 2 ) {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		}else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}
		
		//注意是RelativeLayout.LayoutParams，因为ivView的父布局是RelativeLayout。
		RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams) ivView.getLayoutParams();//获取布局对象
		layoutParams.leftMargin = lastX;//设置左边和top的
		layoutParams.topMargin = lastY;
		//疑问.是否可以动态获取当前坐标点直接设置布局文件中图片的margintop和margainleft呢？
		ivView.setLayoutParams(layoutParams);//将配制好的参数重新设置。这样重新进入这个页面会将归属地图标显示上次更改的地方。
		
		//点击监听。
		
		ivView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 把图片居中
					ivView.layout(winWidth / 2 - ivView.getWidth() / 2,
							ivView.getTop(), winWidth / 2 + ivView.getWidth()
									/ 2, ivView.getBottom());
				}
			}
		});
		//c触摸监听
		ivView.setOnTouchListener(new OnTouchListener() {
			
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://当前初始坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_MOVE:
					//现在移动后的坐标
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					
					//计算偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					
					//计算出现在位置。ivView获取的是界面上显示的位置，当手指滑动触发这个case此时并没有跟新位置。
					int l = ivView.getLeft() + dx;
					int r = ivView.getRight() + dx;
					int t = ivView.getTop() + dy;
					int b = ivView.getBottom() +dy;
					
					//判断是否超出屏幕边界
					if(l < 0 || r > winWidth || t < 0 || b > winHeight - ivView.getHeight()) {
						break;
					}
					
					//根据图片的位置，决定提示框显示和隐藏
					if(t > winHeight / 2 ) {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					}else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}
					
					//更新位置
					ivView.layout(l, t, r, b);
					//再次记录跟新当前位置。
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://记录坐标，只需要记录左和上。
					Editor editor = mPref.edit();
					editor.putInt("lastX", ivView.getLeft());
					editor.putInt("lastY", ivView.getTop());
					editor.commit();
					break;
				default:
					break;
				}
				return false;//事件要向下传递,让onclick(双击事件)可以响应
			}
		});
	}
}
