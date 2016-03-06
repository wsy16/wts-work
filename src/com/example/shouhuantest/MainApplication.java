package com.example.shouhuantest;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MainApplication  extends Application{
	   private List<Activity> mList = new LinkedList(); 
	    private static MainApplication instance; 
	    @Override
	    public void onCreate() {
	    	// TODO Auto-generated method stub
	    	super.onCreate();
	    }
	 
	    private MainApplication() {   
	    } 
	    public synchronized static MainApplication getInstance() { 
	        if (null == instance) { 
	            instance = new MainApplication(); 
	        } 
	        return instance; 
	    } 
	    // add Activity  
	    public void addActivity(Activity activity) { 
	        mList.add(activity); 
	    } 
	 
	    public void exit() { 
	        try { 
	            for (Activity activity : mList) { 
	                if (activity != null) 
	                    activity.finish(); 
	            } 
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	        } finally { 
	            System.exit(0); 
	        } 
	    } 
	    public void onLowMemory() { 
	        super.onLowMemory();     
	        System.gc(); 
	    }  
	

}
