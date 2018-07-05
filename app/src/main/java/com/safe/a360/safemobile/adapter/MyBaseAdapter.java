package com.safe.a360.safemobile.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	
	public List<T> lists;
	public Context context;
	
	protected MyBaseAdapter(List<T> lists, Context context){
		this.context = context;
		this.lists = lists;
	}
	
	protected MyBaseAdapter() {
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
