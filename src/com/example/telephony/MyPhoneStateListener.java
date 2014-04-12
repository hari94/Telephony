package com.example.telephony;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener 
{
Context context;
NotificationManager notificationManager ;
String longText ="...";
int num = 0;
	public MyPhoneStateListener(Context context) 
	{
		this.context = context;
		notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE); 
		
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCallStateChanged(int state, String incomingNumber) 
	{
		super.onCallStateChanged(state, incomingNumber);
		
		switch (state)
		{
		case TelephonyManager.CALL_STATE_IDLE:
			Log.d("Call State ", "ended");
			int curr = Prefs.getMyIntPref(context, "status");			
			if(curr == 1)
				{
				Prefs.setMyIntPref(context, "status",0);
				Log.d("Call State", "call ended");
				
				//Modify no. of pending calls to be logged
				if(Prefs.getMyIntPref(context, "no")==0)
				{
					Prefs.setMyIntPref(context, "no",1);
					num = 0;
				}
				
				//Notification

				Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra("num", num+1);
			    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			    PendingIntent pi = PendingIntent.getActivity(context,1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			    
			    
			    
			    // Build notification
			    
			    Notification noti = new Notification.Builder(context)
			        .setContentTitle("Log It !")
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setNumber(++num)
			        .setStyle(new Notification.BigTextStyle().bigText("You've got new call(s) to log. Tap Here to view"))
			        .setContentIntent(pi)
			        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI).build();
			    	
			    
			    
			    
			    
			    // hide the notification after its selected
			    noti.flags |= Notification.FLAG_AUTO_CANCEL;
			    notificationManager.notify(1, noti);
			    
				}
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Prefs.setMyIntPref(context, "status", 1);
			Log.d("Call State ", "outgoing");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			Prefs.setMyIntPref(context, "status", 1);
			Log.d("Call State ", "incoming");
			break;
		}
	}
	
}
