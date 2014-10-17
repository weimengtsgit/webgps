/**
 * 
 */
package com.autonavi.lbsgateway.alarmpool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import com.autonavi.directl.parse.ParseBase;

/**
 * @author shiguang.zhou
 * 
 */
public class AlarmLocQueue {

	private LinkedList activeWork;
	private LinkedList speedWork;
	private LinkedList areaWork;
	private LinkedList lineWork;

	private int maxBatch = 200;

	private AlarmDataPool dataPool = null;

	public AlarmLocQueue(AlarmDataPool dataPool) {
		this.dataPool = dataPool;
		activeWork = new LinkedList();
		speedWork = new LinkedList();
		areaWork = new LinkedList();
		lineWork = new LinkedList();
		 
	}

	public synchronized void addWork(ParseBase task) {
		activeWork.add(task);
		notify();

	}
	public synchronized void addSpeedWork(ParseBase task) {
		speedWork.add(task);
		notify();

	}
	public synchronized void addAreaWork(ParseBase task) {
		areaWork.add(task);
		notify();

	}
	public synchronized void addLineWork(ParseBase task) {
		lineWork.add(task);
		notify();

	}

	public synchronized ArrayList getActiveWork() throws InterruptedException {
		// Log.getInstance().outLog("There has "+work.size()+" Data in DataPool
		// , waitting. ");

		while (activeWork.isEmpty() || activeWork.size() < maxBatch) {
			try {
				wait();
			} catch (InterruptedException ie) {
				throw ie;
			}
		}

		ArrayList ls = new ArrayList();

		for (int i = 0; i < activeWork.size(); i++) {
			ls.add(activeWork.remove(i));
		}

		return ls;
	}
	public synchronized ArrayList getSpeedWork() throws InterruptedException {
		// Log.getInstance().outLog("There has "+work.size()+" Data in DataPool
		// , waitting. ");

		while (speedWork.isEmpty() || speedWork.size() < maxBatch) {
			try {
				wait();
			} catch (InterruptedException ie) {
				throw ie;
			}
		}

		ArrayList ls = new ArrayList();

		for (int i = 0; i < speedWork.size(); i++) {
			ls.add(speedWork.remove(i));
		}

		return ls;
	}
	public synchronized ArrayList getAreaWork() throws InterruptedException {
		// Log.getInstance().outLog("There has "+work.size()+" Data in DataPool
		// , waitting. ");

		while (areaWork.isEmpty() || areaWork.size() < maxBatch) {
			try {
				wait();
			} catch (InterruptedException ie) {
				throw ie;
			}
		}

		ArrayList ls = new ArrayList();

		for (int i = 0; i < areaWork.size(); i++) {
			ls.add(areaWork.remove(i));
		}

		return ls;
	}
	public synchronized ArrayList getLineWork() throws InterruptedException {
		// Log.getInstance().outLog("There has "+work.size()+" Data in DataPool
		// , waitting. ");

		while (lineWork.isEmpty() || lineWork.size() < maxBatch) {
			try {
				wait();
			} catch (InterruptedException ie) {
				throw ie;
			}
		}

		ArrayList ls = new ArrayList();

		for (int i = 0; i < lineWork.size(); i++) {
			ls.add(lineWork.remove(i));
		}

		return ls;
	}

}
