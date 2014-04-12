package com.example.telephony;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> grid1,grid2,grid3,grid4,grid5;
	LayoutInflater inflater;
	int width, height;

	public GridAdapter(Context context, ArrayList<String> gridData1,
			ArrayList<String> gridData2, ArrayList<String> gridData3,
			ArrayList<String> gridData4, ArrayList<String> gridData5) {
		this.context = context;
		grid1 = gridData1;
		grid2 = gridData2;
		grid3 = gridData3;
		grid4 = gridData4;
		grid5 = gridData5;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return grid1.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class ViewHolder {
		TextView tvCellName, tvId, tvDate, tvNum, tvType;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View cell = convertView;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			cell = inflater.inflate(R.layout.gv_row, null);
			holder.tvCellName = (TextView) cell.findViewById(R.id.tvRowNmae);
			holder.tvNum = (TextView) cell.findViewById(R.id.tvRowNum);
			holder.tvType = (TextView) cell.findViewById(R.id.tvRowType);
			holder.tvId = (TextView) cell.findViewById(R.id.tvId);
			holder.tvDate = (TextView) cell.findViewById(R.id.tvDate);
			cell.setTag(holder);
			cell.setTag(R.id.tvRowNmae, holder.tvCellName);
			cell.setTag(R.id.tvRowNum, holder.tvNum);
			cell.setTag(R.id.tvRowType, holder.tvType);
			cell.setTag(R.id.tvDate, holder.tvDate);
			cell.setTag(R.id.tvId, holder.tvId);
		} else
			holder = (ViewHolder) cell.getTag();

		holder.tvId.setText(grid1.get(pos));
		holder.tvCellName.setText(grid2.get(pos));
		holder.tvNum.setText(grid3.get(pos));
		holder.tvType.setText(grid4.get(pos));
		holder.tvDate.setText(grid5.get(pos));
		return cell;
	}

}
