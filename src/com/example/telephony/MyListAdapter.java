package com.example.telephony;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter 
{
	ArrayList<String> names,type,times;
	Activity a;
	LayoutInflater layoutInflater ;
	boolean[] isChecked;
	Typeface font;
	public MyListAdapter(Activity a,ArrayList<String> names,ArrayList<String> type,ArrayList<String> times)
	{
		this.a = a;
		this.names = names;
		this.type = type;
		this.times = times;
		layoutInflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		isChecked = new boolean[names.size()];
		for(int x = 0 ;x<isChecked.length;x++)
			isChecked[x] = false;
		font = Typeface.createFromAsset(a.getAssets(), "ArchitectsDaughter.ttf");
	}
	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	boolean checked(int pos){
		return isChecked[pos];
	}
	public static class ViewHolder 
	{

		public TextView name,type,time;
		public CheckBox check;
	}
	
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) 
	{
	View view = convertView;
	final ViewHolder holder ;
	if(convertView == null)
	{
		holder = new ViewHolder();
		view = layoutInflater.inflate(R.layout.layout_list, null);
		holder.name = (TextView)view.findViewById(R.id.tvName);
		holder.type = (TextView)view.findViewById(R.id.tvType);
		holder.time = (TextView)view.findViewById(R.id.tvTimestamp);
		holder.check = (CheckBox)view.findViewById(R.id.cbCheck);
		view.setTag(holder);
		view.setTag(R.id.tvName, holder.name);
		view.setTag(R.id.tvType, holder.type);
		view.setTag(R.id.tvTimestamp, holder.time);
        view.setTag(R.id.cbCheck, holder.check);
        
	}
	else
			holder = (ViewHolder) view.getTag();
		
		holder.check.setTag(pos);
	
	holder.name.setText(names.get(pos));
	holder.type.setText(type.get(pos));
	holder.time.setText(times.get(pos));
	holder.name.setTypeface(font, Typeface.BOLD);
	holder.type.setTypeface(font, Typeface.BOLD);
	holder.time.setTypeface(font, Typeface.BOLD);
	if(isChecked[pos])
		holder.check.setChecked(true);
	
	holder.check.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) 
		{
			isChecked[pos] = holder.check.isChecked();
		}
	});
	return view;
	}
}
