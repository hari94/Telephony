
package com.example.telephony;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> grid1,grid2,grid3,grid4,grid5;
	LayoutInflater inflater;
	Typeface font;
	int style;
	private static int ORDER_BY_NUM=1;
	private static int ORDER_BY_NAME=2;
	private static int ORDER_BY_TYPE=3;
	CallerListAdapter cadp;
	public GridAdapter(Context context, ArrayList<String> gridData1,
			ArrayList<String> gridData2, ArrayList<String> gridData3,
			ArrayList<String> gridData4, ArrayList<String> gridData5) {
		this.context = context;
		grid1 = gridData1;
		grid3 = gridData2;
		grid2 = gridData3;
		grid4 = gridData4;
		grid5 = gridData5;
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
		return grid2.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class ViewHolder {
		TextView tvCellName, tvId, tvDate, tvNum, tvType;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		View cell = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			cell = inflater.inflate(R.layout.gv_row, null);
			holder.tvCellName = (TextView) cell.findViewById(R.id.tvRowName);
			holder.tvNum = (TextView) cell.findViewById(R.id.tvRowNum);
			holder.tvType = (TextView) cell.findViewById(R.id.tvRowType);
			holder.tvId = (TextView) cell.findViewById(R.id.tvId);
			holder.tvDate = (TextView) cell.findViewById(R.id.tvDate);
			cell.setTag(holder);
			cell.setTag(R.id.tvRowName, holder.tvCellName);
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
		if(pos > 0)		
			style = Typeface.BOLD;
		else
			style = Typeface.BOLD_ITALIC;
		
		holder.tvCellName.setTypeface(font,style);
		holder.tvId.setTypeface(font,style);
		holder.tvType.setTypeface(font,style);
		holder.tvNum.setTypeface(font,style);
		holder.tvDate.setTypeface(font,style);
		
		holder.tvType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(!holder.tvType.getText().toString().equals("TYPE"))
				fetch(holder.tvType.getText().toString(),ORDER_BY_TYPE);	
			}
		});
		
		holder.tvCellName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(!holder.tvCellName.getText().toString().equals("NAME"))
				fetch(holder.tvCellName.getText().toString(),ORDER_BY_NAME);	
			}
		});
		
		holder.tvNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(!holder.tvNum.getText().toString().equals("NUMBER"))
				fetch(holder.tvNum.getText().toString(),ORDER_BY_NUM);	
			}
		});
		
		return cell;
	}

	
	
	
	protected void fetch(final String logType,final int action) 
	{
		class Loaddb extends AsyncTask<Void, Void, Boolean>
		{
			ArrayList<String> callerNames = new ArrayList<String>();
			ArrayList<String> times = new ArrayList<String>();
			ProgressDialog pdia;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pdia = new ProgressDialog(context);
				pdia.setMessage("Fetching data ...");
				pdia.show();
			}

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				
				try{
					CallLogDB db = new CallLogDB(context);
					db.open();
					String data[] = new String[db.getDataByName(logType).split("\n").length];
					switch(action)
					{
					case 1:
						data = db.getDataByNum(logType).split("\n");
						break;
					case 2:  
						data = db.getDataByName(logType).split("\n");
						break;
					case 3:
						data = db.getDataByType(logType).split("\n");
						break;
					}
					
					callerNames.clear();
					times.clear();
					for(int i=0;i<data.length;i++)
					{
						String[] sub =data[i].split(";");
						if(sub.length==2)
						{
							callerNames.add(sub[0]);
							times.add(sub[1]);
						}
					}
					db.close();
					
				return true;
				
				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) 
			{
				super.onPostExecute(result);
				pdia.dismiss();
				if(result.booleanValue())
				{
					
					LayoutInflater li = LayoutInflater.from(context);
			        View vi = li.inflate(R.layout.alert_dialog_logged_list, null);		        
			        ListView lv = (ListView)vi.findViewById(R.id.lvlogged);
			        TextView tv = (TextView)vi.findViewById(R.id.tvTitle);
			        
			        String title="";
			        switch(action)
			        {
			        case 1: title = "Calls mapped to  " + logType;
			        	break;
			        case 2: title = "Calls mapped to  " + logType;
			        		break;
			        case 3: title = "Call Type : " + logType;
			        	break;
			        }
			        tv.setText(title);
			        tv.setTypeface(font,Typeface.BOLD_ITALIC);
			        
			        cadp = new CallerListAdapter(context,callerNames,times);
			        lv.setAdapter(cadp);
			        AlertDialog.Builder builder = new AlertDialog.Builder(context);		        
			        builder.setView(vi);
					
			        AlertDialog dialog = builder.create();
			        dialog.show();			        
				}
				
			}
			
		}
		
		Loaddb ldb = new Loaddb();
		ldb.execute();
	}

}
