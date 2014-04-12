package com.example.telephony;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ContentResolver contentResolver;
	ListView gv;
	MyListAdapter adapter ;
	GridAdapter gadp;
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> type = new ArrayList<String>();
	ArrayList<Integer> numList = new ArrayList<Integer>();
	ArrayList<String> gridData1 = new ArrayList<String>();
	ArrayList<String> gridData2= new ArrayList<String>();
	ArrayList<String> gridData3 = new ArrayList<String>();
	ArrayList<String> gridData4 = new ArrayList<String>();
	ArrayList<String> gridData5 = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		gv = (ListView)findViewById(R.id.lvTable);
		Prefs.setMyIntPref(this, "status", 0);
		Prefs.setMyIntPref(this, "no", 0);
		onNewIntent(getIntent());
		loadtable();
		gadp = new GridAdapter(MainActivity.this,gridData1,gridData2,gridData3,gridData4,gridData5);
		gv.setAdapter(gadp);
	}

	private void getAllCallLogs(ContentResolver cr,int repeat,int i,int flag) 
	{
		int x=0;
		// reading all data in descending order according to DATE
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
		Uri callUri = Uri.parse("content://call_log/calls");
		Cursor cur = cr.query(callUri, null, null, null, strOrder);
		// loop through cursor
		CallLogDB db = new CallLogDB(this);
		db.open();		
		String calltype = new String();
		while (cur.moveToNext() && repeat !=0) 
		{
			
			
			String callNumber = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
			String callName = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
			String callDate = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DATE));
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
			String dateString = formatter.format(new Date(Long.parseLong(callDate)));

			int callType = Integer.parseInt(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE)));
			String isCallNew = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NEW));
			String duration = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
			
			
			switch(callType)
			{
			//incoming
			case 1:
				calltype = "INCOMING";
				break;
			//outgoing
			case 2:
				calltype = "OUTGOING";
				break;
			//missed
			case 3:
				calltype = "MISSED";
				break;
			}
			
			
			
			if(callName == null)
				callName = "Unknown (X)";
			if(flag == 0)
			{
			names.add(callName);
			type.add(calltype);
			}
			else if(x == i && flag==1)
			{
			Log.d("DETAILS","num = " + callNumber + " name :"+callName+" duration :"+duration+" type :"+callType);
			db.insertData(callName, callNumber, calltype, dateString);
			}
			repeat --;x++;
			}			
		
		db.close();
	}
	
	
	
	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
		
		Bundle extras = intent.getExtras();
		
	    if(extras != null){
	    
	        if(extras.containsKey("num"))
	        {
	        	Prefs.setMyIntPref(this	, "no",0);
	        	
	        	// Data From Notification	 
	        	names.clear();
	        	type.clear();
	        	numList.clear();
	            final int n = extras.getInt("num") ;
	            //for(int x = 0 ;x<names.size();x++)
					getAllCallLogs(getContentResolver(),n,0,0);
	           
	            
	            LayoutInflater li = LayoutInflater.from(MainActivity.this);
		        View alertDialogView = li.inflate(R.layout.alert_dialog_list, null);		        
		        
		        ListView lv = (ListView)alertDialogView.findViewById(R.id.lvList);
		        adapter = new MyListAdapter(MainActivity.this, names, type);
		        lv.setAdapter(adapter);
		        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);		        
		        builder.setView(alertDialogView);
		        builder.setPositiveButton("Log it!", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) 
					{
						for(int i=0;i<names.size();i++)
						{
							if(adapter.checked(i))
								{numList.add(i);Log.d("NUM", numList.get(i)+"");}
						}						
						refresh(n);
					}
				});
		        builder.setNegativeButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		        AlertDialog dialog = builder.create();
		        dialog.show();
	        }
	    }

	}

	protected void refresh(final int n) 
	{
		class Loaddb extends AsyncTask<Void, Void, Boolean>{
			ProgressDialog pdia;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pdia = new ProgressDialog(MainActivity.this);
				pdia.setMessage("Logging ...");
				pdia.show();
			}

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				try{
				for(int x=0;x<numList.size();x++)
					getAllCallLogs(getContentResolver(),n,numList.get(x),1);
					loadtable();
				return true;
				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result.booleanValue())
				{
					Toast.makeText(MainActivity.this, "Successfully Logged", Toast.LENGTH_SHORT).show();
					gadp = new GridAdapter(MainActivity.this,gridData1,gridData2,gridData3,gridData4,gridData5);
					gv.setAdapter(gadp);
				}
				pdia.dismiss();
			}
			
		}
		
		Loaddb ldb = new Loaddb();
		ldb.execute();
	}

	public void loadtable() 
	{
		CallLogDB db = new CallLogDB(MainActivity.this);
		db.open();
		gridData1.clear();
		gridData2.clear();
		gridData3.clear();
		gridData4.clear();
		gridData5.clear();
		gridData1.add("ID");
		gridData2.add("NAME");
		gridData3.add("NUMBER");
		gridData4.add("TYPE");
		gridData5.add("TIME");
		
		String[] data = db.getData().split("\n");
		for(int i=0 ; i<data.length; i++)
		{
			String[] subData = data[i].split(";");	
			if(subData.length==5)
			{
			gridData1.add(subData[0]);
			gridData2.add(subData[1]);
			gridData3.add(subData[2]);
			gridData4.add(subData[3]);
			gridData5.add(subData[4]);
			}
			
		}
		
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
