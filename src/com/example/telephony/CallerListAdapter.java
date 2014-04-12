package com.example.telephony;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CallerListAdapter extends BaseAdapter{

	Context context;
	ArrayList<String> grid1, grid2;
	LayoutInflater inflater;
	Typeface font;
	
	public CallerListAdapter(Context context, ArrayList<String> callerNames,
			ArrayList<String> times) {

		this.context = context;
		grid1 = callerNames;
		grid2 = times;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		font = Typeface.createFromAsset(context.getAssets(), "ArchitectsDaughter.ttf");
	}

	@Override
	public int getCount() {
		return grid1.size();
	}

	@Override
	public Object getItem(int pos) {
		return grid1.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class ViewHolder {
		TextView tvCellName,  tvTime;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View cell = convertView;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			cell = inflater.inflate(R.layout.caller_list_layout, null);
			holder.tvCellName = (TextView) cell.findViewById(R.id.tvCallerName);			
			holder.tvTime = (TextView) cell.findViewById(R.id.tvCallerTime);
			
			cell.setTag(holder);
			cell.setTag(R.id.tvCallerName, holder.tvCellName);
			cell.setTag(R.id.tvCallerTime, holder.tvTime);
		} else
			holder = (ViewHolder) cell.getTag();

		holder.tvCellName.setText(grid1.get(pos));
		holder.tvTime.setText(grid2.get(pos));
		holder.tvCellName.setTypeface(font,Typeface.BOLD);
		holder.tvTime.setTypeface(font,Typeface.BOLD);
		return cell;
	}

}
