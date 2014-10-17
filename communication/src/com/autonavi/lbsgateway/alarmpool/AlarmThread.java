/**
 * 
 */
package com.autonavi.lbsgateway.alarmpool;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;
import com.autonavi.directl.parse.ParseBase;

/**
 * @author shiguang.zhou
 * 
 */
public class AlarmThread extends Thread {

	private static boolean isSaving = false;
	private Timer timer = new Timer();
	private Object obj = new Object();
	private int interval = 5;

	public AlarmThread() {
		String stime = Config.getInstance().getString("alarmSaveTime");
		interval = Integer.parseInt(stime == null ? "10" : stime);
		// Log.getInstance().outLog("start check alarmqueue by time:=" + stime);
		// timer.schedule(new AlarmTask(), new Date(), interval*1000);
	}

	public synchronized void run() {
		try {
			while (true) {
				if (!isSaving){
					isSaving = true;
					//暂时不保存报警
					 checkAlarmQueue();
					isSaving = false;
				}
				Thread.sleep(interval * 1000);
 			}
		} catch (Exception e) {
			isSaving = false;
			e.printStackTrace();
			Log.getInstance().errorLog("checking alarm queue error", e);
		}
	}

	private void checkAlarmQueue() {
 
			AlarmQueue queue = AlarmQueue.getInstance();
			ArrayList<ParseBase> pbList = new ArrayList<ParseBase>();

			// if (queue.size() > count) {
			for (int i = 0; i < queue.size(); i++) {
				try {
					ParseBase pb = queue.removeParseBase();
					pbList.add(pb);
				} catch (Exception e) {
					continue;
				}
			}
			if (pbList == null || pbList.size()<=0)
				return ;
			long s = System.currentTimeMillis();
			Log.getInstance().outLog("按批获取报警队列条数：" + pbList.size());
			DBService dbserv = new DBServiceImpl();
			try {
				
				dbserv.saveMoreAlarm(pbList);

			} catch (Exception e) {
				isSaving = false;
				Log.getInstance().errorLog("按批保存报警信息异常", e);
				e.printStackTrace();
			}
			long e = System.currentTimeMillis();
			Log.getInstance().outLog("按批保存报警队列耗时：" + (e - s) / 1000 + "秒");
			 
	}

}

// class AlarmTask extends TimerTask {
// private static boolean isSaved;
// private static long count=1;
//
// @Override
// public void run() {
//
// if (!isSaved) {
// Log.getInstance().outLog("start checking alarmqueue by time,check
// count="+count);
// AlarmQueue queue = AlarmQueue.getInstance();
// ArrayList<ParseBase> curList = new ArrayList<ParseBase>();
// for (int i = 0; i < queue.size(); i++) {
// try {
// ParseBase pb = queue.getParseBase();
// curList.add(pb);
// } catch (Exception e) {
// continue;
// }
// }
//
// if (curList.size() > 0) {
// long s = System.currentTimeMillis();
// Log.getInstance().outLog("按时获取报警队列条数：" + curList.size());
//
// try {
// DBService dbserv = new DBServiceImpl();
// dbserv.saveMoreAlarm(curList);
// } catch (Exception e) {
// // TODO Auto-generated catch block
// Log.getInstance().errorLog("按时保存报警信息异常", e);
// e.printStackTrace();
// isSaved = true;
// }
// long e = System.currentTimeMillis();
// Log.getInstance().outLog("按时保存报警队列耗时：" + (e - s) / 1000 + "秒");
// }
//
// isSaved = true;
// }
//
// }
//
// }
