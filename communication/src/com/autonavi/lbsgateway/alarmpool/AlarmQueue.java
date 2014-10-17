/**
 * 
 */
package com.autonavi.lbsgateway.alarmpool;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.autonavi.directl.Log;
import com.autonavi.directl.parse.ParseBase;

/**
 * @author shiguang.zhou
 *
 */
public class AlarmQueue extends LinkedList<ParseBase>{
	
	static AlarmQueue instance;
	
	public static synchronized AlarmQueue getInstance(){
		
		if (instance == null){
			instance = new AlarmQueue();
		}
		
		return instance;
	}
	
	public synchronized void addAlarm(Object parse){
		
		if (parse != null){
			instance.add((ParseBase)parse);
		}
		Log.getInstance().outLog("缓存共有"+instance.size()+"条报警记录");
	}
	
	public synchronized ParseBase removeParseBase(){
		return (ParseBase)instance.remove();
	}
	


}
