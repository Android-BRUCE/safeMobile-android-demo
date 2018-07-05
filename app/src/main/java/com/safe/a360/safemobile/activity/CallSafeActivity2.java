package com.safe.a360.safemobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.adapter.MyBaseAdapter;
import com.safe.a360.safemobile.bean.BlackNumberInfo;
import com.safe.a360.safemobile.dao.BlackNumberDao;

import java.util.List;

/**
 * 分批加载数据
 * 黑名单数据显示
 * @author BRUCE
 *
 */
public class CallSafeActivity2 extends Activity {
	private CallSafeAdapter adapter;
	private int totalNmuber;
	private BlackNumberDao dao;
    private ListView list_view;
    private LinearLayout ll_pb;
	private List<BlackNumberInfo> blackNumberInfolist;
	/**
	 * 开始位置
	 */
	private int mstartIndex = 0;
	/**
	 * 每批次加载20条
	 */
	private final int maxCount = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.call_safe_activity2);
        initUI();//初始化UI
        initData();
    }
    private final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
    		ll_pb.setVisibility(View.INVISIBLE);
			System.out.println("=======handle1");
			if(adapter == null){  //判断适配器是否存在，存在用 notifyDataSetChanged()刷新页面 		
				adapter = new CallSafeAdapter(blackNumberInfolist, CallSafeActivity2.this);
				//System.out.println("=======handle2");
	    		list_view.setAdapter(adapter);//设置适配器
				//System.out.println("=======handle3");
				}else {
					adapter.notifyDataSetChanged();
				}
			System.out.println("=======handle3");
    	}
    };
	private ImageView iv_delete;
    /**
     * 初始化数据
     */
    private void initData() {
    	dao = new BlackNumberDao(CallSafeActivity2.this);
    	totalNmuber = dao.getAllNumber();
    	new Thread(){
			public void run() {
				/*
				  防止之前数据被覆盖
				 */
				if(blackNumberInfolist==null){
					blackNumberInfolist = dao.findPar2(mstartIndex, maxCount);
				//System.out.println(blackNumberInfolist);
				System.out.println("=======获取手机拦截数据");
				}else {
					blackNumberInfolist.addAll(dao.findPar2(mstartIndex, maxCount));
				}
    	    	handler.sendEmptyMessage(0);
    		};
    	}.start();
	}
    /**
     * 初始化界面
     */
	private void initUI() {
        list_view = (ListView) findViewById(R.id.lv_list);
		ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
		ll_pb.setVisibility(View.VISIBLE);//数据加载完之前 显示进度条。
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        /*
          AbsListView.OnScrollListener.SCROLL_STATE_IDLE 闲置状态
          AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 手指触摸的时候的状态
          AbsListView.OnScrollListener.SCROLL_STATE_FLING 惯性

          设置listView的滚动监听器
         */
        list_view.setOnScrollListener(new OnScrollListener() {
			
			/**
			 * 状态改变调用
			 */
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int lastitem = list_view.getLastVisiblePosition();
					if( lastitem == blackNumberInfolist.size() - 1) {
						mstartIndex += maxCount;
						if(mstartIndex >= totalNmuber){
							Toast.makeText(CallSafeActivity2.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
							return;
						}
						initData();
					}
					break;

				default:
					break;
				}
				
			}
			
			/**
			 * 滑动时候就调用，也就是实时调用
			 */
			public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {}
		});
       // final String items[] = new String[] {"短信拦截3","电话拦截2" ,"短信和电话拦截1"};
        final String items[] = new String[] {"短信和电话拦截","电话拦截" ,"短信拦截"};
        //设置更改黑名单号码的拦截模式。
        list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
				final int p = position;
				final BlackNumberInfo clickinfo = (BlackNumberInfo) list_view.getItemAtPosition(position);//拿到当前条目的对象
				final String number = clickinfo.getNumber();//通过该条目对象拿到该对象的号码
				
				AlertDialog.Builder builder = new AlertDialog.Builder(CallSafeActivity2.this);
				builder.setTitle("更改拦截模式");
				String mode = dao.findNumber(number);//拿到当前对象的拦截模式（模式有1,2,3.3种）
				int checkedstyle = Integer.parseInt(mode) - 1;
				builder.setSingleChoiceItems(items, checkedstyle,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					String mode = String.valueOf(which + 1);
					System.out.println(" 位置是 ： " + which);//which第一个位置是0
						
						//if(dao == null) {
							//dao = new BlackNumberDao(CallSafeActivity2.this);
						//}
						boolean b = dao.changeNumberMode(number, mode);//更新数据库内容
						if(b) {
							//将当前条目删除并重新写入并刷新界面
							BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
							blackNumberInfo.setNumber(number);
							blackNumberInfo.setMode(mode);
							blackNumberInfolist.remove(p);
							blackNumberInfolist.add(p, blackNumberInfo);
							adapter.notifyDataSetChanged();
						}
					}
				});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});
    }
	
	
	/**
	 * 适配器，显示拦截模式和删除功能
	 * @author BRUCE
	 *
	 */
	private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {
		private CallSafeAdapter(List<BlackNumberInfo> lists, Context context){
			super(lists,context);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView == null) {
				convertView = View.inflate(CallSafeActivity2.this, R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
				holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_number.setText(lists.get(position).getNumber());
			String mode = lists.get(position).getMode();
			if(mode.equals("1")){
				holder.tv_mode.setText("电话 + 短信拦截");
			}else if(mode.equals("2")) {
				holder.tv_mode.setText("电话拦截");
			}else if(mode.equals("3")) {
				holder.tv_mode.setText("短信拦截");
			}
			final BlackNumberInfo info = lists.get(position);//获取当前item对象。
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String number = info.getNumber();
					boolean result = dao.delete(number);
					if(result) {
						Toast.makeText(CallSafeActivity2.this, "删除成功", Toast.LENGTH_SHORT).show();
						lists.remove(info);
						adapter.notifyDataSetChanged();//刷新页面
					}
				}
			});
			return convertView;
			}
	}
	
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	public void addBlackNumber(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_add_black_number, null);
		dialog.setView(view, 0, 0, 0, 0);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText et_number = (EditText) view.findViewById(R.id.et_number);
		final CheckBox cb_message = (CheckBox) view.findViewById(R.id.cb_message);
		final CheckBox cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();	
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 String numberString = et_number.getText().toString().trim();
				if(TextUtils.isEmpty(numberString)) {
					Toast.makeText(CallSafeActivity2.this, "请输入号码", Toast.LENGTH_SHORT).show();
					return;
				}
				String mode = "0";
				if(cb_message.isChecked() && cb_phone.isChecked()) {
					mode = "1";
				}else if(cb_phone.isChecked()){
					mode = "2";
				}else if(cb_message.isChecked()){
					mode = "3";
				}else {
					Toast.makeText(CallSafeActivity2.this, "请勾选模式", Toast.LENGTH_SHORT).show();
				}
				BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
				blackNumberInfo.setNumber(numberString);
				blackNumberInfo.setMode(mode);
				
				blackNumberInfolist.add(0, blackNumberInfo);//添加到arraylist中
				dao.add(numberString, mode);//添加到数据库
				
				if(adapter == null) {
					adapter = new CallSafeAdapter(blackNumberInfolist, CallSafeActivity2.this); 
					list_view.setAdapter(adapter);
				}else {
					adapter.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});
		dialog.setView(view);
		dialog.show();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dao = null;
	}
}
