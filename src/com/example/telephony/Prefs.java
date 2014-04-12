package com.example.telephony;


import android.content.Context;
import android.content.SharedPreferences;

public class Prefs 
{
	private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("myprefs", 0);
    }

    public static String getMyStringPref(Context context, String name) {
        return getPrefs(context).getString(name, "default");
    }

    public static Boolean getMyBoolPref(Context context, String name) 
    {
        return getPrefs(context).getBoolean(name, false);
    }
    
    public static int getMyIntPref(Context context, String name) 
    {
        return getPrefs(context).getInt(name, 0);
    }

    public static void setMyStringPref(Context context,String name, String value) 
    {
        // perform validation etc..
        getPrefs(context).edit().putString(name, value).commit();
    }
    
    public static void setMyIntPref(Context context,String name, int value) 
    {
        // perform validation etc..
        getPrefs(context).edit().putInt(name, value).commit();
    }
    
    
    public static void setMyBoolPref(Context context,String name, Boolean value) 
    {
        // perform validation etc..
        getPrefs(context).edit().putBoolean(name, value).commit();
    }
    
    public static void deletePref(Context context)
    {
            getPrefs(context).edit().clear().commit();
    }
    
    public static void delPref(Context context, String s)
    {
            getPrefs(context).edit().remove(s).commit();
    }
}
