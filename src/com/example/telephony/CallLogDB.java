package com.example.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CallLogDB {
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_NUM = "num";
	public static final String KEY_TYPE = "type";
	public static final String KEY_TIME = "time";


	private static final String DATABASE_NAME = "Callersdb";
	private static final String DATABASE_TABLE = "CallsInfo";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NUM
			+ " TEXT NOT NULL, " + KEY_NAME
			+ " TEXT NOT NULL, " + KEY_TYPE
			+ " TEXT NOT NULL, " + KEY_TIME
			+ " TEXT NOT NULL)";

	
	
	ContactClass cc;
	SQLiteDatabase db;
	Context c;
	
	private class ContactClass extends SQLiteOpenHelper
	{

		public ContactClass(Context context) 
		{
			super(context, DATABASE_NAME , null , DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		}
		
	}
	
	public CallLogDB(Context c) 
	{
		this.c = c;
	}
	
	public CallLogDB open() throws SQLException
	{
		cc = new ContactClass(c);
		db = cc.getWritableDatabase();
		return this;
	}
	
	public CallLogDB close()
	{
		cc.close();
		return this;
	}
	
	public long insertData(String name,String num,String type,String time)
	{
		ContentValues cv=new ContentValues();	
		
		cv.put(KEY_NUM,num);
		cv.put(KEY_NAME, name);
		cv.put(KEY_TYPE, type);
		cv.put(KEY_TIME,time);
		
		
		return db.insert(DATABASE_TABLE,null,cv);
	}
	
	public String getData() throws SQLException
	{
		String[] columns = new String[] { KEY_ID,KEY_NUM,KEY_NAME,KEY_TYPE,KEY_TIME };
		Cursor c = db.query(DATABASE_TABLE, columns, null, null, null, null,KEY_ID);
		String result = "";

		int iPos = c.getColumnIndex(KEY_ID);
		int iNum = c.getColumnIndex(KEY_NUM);
		int iName = c.getColumnIndex(KEY_NAME);
		int iType=c.getColumnIndex(KEY_TYPE);
		int iTime=c.getColumnIndex(KEY_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result  
					+ c.getString(iPos)	+ ";"
					+ c.getString(iNum)+ ";"
					+ c.getString(iName)+ ";"
					+ c.getString(iType) + ";"
					+ c.getString(iTime) + "\n";
		}
		c.close();
		return result;
	}

	public String getDataByType(String s) {
		String[] columns = new String[] { KEY_NAME,KEY_TIME };
		Cursor c = db.query(DATABASE_TABLE, columns,KEY_TYPE + "='" + s +"'", null, null, null,KEY_ID);
		String result = "";

		
		int iName = c.getColumnIndex(KEY_NAME);		
		int iTime=c.getColumnIndex(KEY_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result					
					+ c.getString(iName)+ ";"					
					+ c.getString(iTime) + "\n";
		}
		c.close();
		return result;
	}
	
	public String getDataByNum(String s) {
		String[] columns = new String[] { KEY_NAME,KEY_TIME };
		Cursor c = db.query(DATABASE_TABLE, columns,KEY_NUM + "='" + s +"'", null, null, null,KEY_ID);
		String result = "";

		
		int iName = c.getColumnIndex(KEY_NAME);		
		int iTime=c.getColumnIndex(KEY_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result					
					+ c.getString(iName)+ ";"					
					+ c.getString(iTime) + "\n";
		}
		c.close();
		return result;
	}

	public String getDataByName(String s) {
		String[] columns = new String[] { KEY_NUM,KEY_TIME };
		Cursor c = db.query(DATABASE_TABLE, columns,KEY_NAME + "='" + s +"'", null, null, null,KEY_ID);
		String result = "";

		
		int iNum = c.getColumnIndex(KEY_NUM);		
		int iTime=c.getColumnIndex(KEY_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result					
					+ c.getString(iNum)+ ";"					
					+ c.getString(iTime) + "\n";
		}
		c.close();
		return result;
	}

}
