package com.safe.a360.safemobile.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.safe.a360.safemobile.R;
import com.safe.a360.safemobile.bean.AppInfo;
import com.safe.a360.safemobile.engine.AppInfos;

import java.util.List;

public class UnLockFragment extends Fragment {
	
	private View view;
	public ListView list_view;
	@SuppressWarnings("unused")
	private TextView list_unlock;
	private List<AppInfo> appInfos;
	private UnLockAdapter adapter;



	/*
	 * 类似activity里面的setContentView
	 */
	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.item_unclok_fragment, null);
		list_view = (ListView) view.findViewById(R.id.List_View);
		list_unlock = (TextView) view.findViewById(R.id.tv_unlock);
	
		return view;
	}
	
	
	/**
	 * 业务逻辑写在这里，每次切换页面会再次调用这个方法，也即是说返回这个页面时候会再次调用这方法
	 * 
	 */
	@Override
	public void onStart() {
		super.onStart();
		//appInfos = TaskInfoParser.getTaskInfos(getActivity());
		appInfos = AppInfos.getAppInfos(getActivity());
		adapter = new UnLockAdapter();
		list_view.setAdapter(adapter);
		System.out.println("==========================这是unfragment");
	}
	
	public class UnLockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return appInfos.size();
		}

		@Override
		public Object getItem(int posion) {
		return appInfos.get(posion);
		}

		@Override
		public long getItemId(int posion) {
			// TODO Auto-generated method stub
			return posion;
		}

		@Override
		public View getView(int posion, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view;
			final AppInfo infoParser;
			if(convertView == null) {
				System.out.println("==========================这是unfragmenttest02");
				view = View.inflate(getActivity(), R.layout.item_unclock, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.iv_unlock = (ImageView) view.findViewById(R.id.iv_unlock);
				
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			infoParser = appInfos.get(posion);
			
			holder.iv_icon.setImageDrawable(infoParser.getIcon());
			holder.tv_name.setText(infoParser.getApkName());
			return view;
		}
	}
	
	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_unlock;
	}
}
