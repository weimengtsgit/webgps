package com.autonavi.lbsgateway.alarmpool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

import com.autonavi.directl.Log;
import com.autonavi.directl.parse.ParseBase;
import com.autonavi.lbsgateway.poolsave.BatchSave;

public class AlarmDataPool {
	private ArrayList workThreadList;
	private AlarmLocQueue workQueue;
	private java.util.Timer timer = null; // 定时器
	private int checkTime = 5;//线程自检的分钟数
	public static final int DEFAULT_SIZE = 3;
	private int saveTime = 30;
	private  boolean started = false;
	private int threadCount = 3;
	private int count = 0;
	private int count2 = 0;
	private boolean timeUp = false;

	/**
	 * Create a default size thread pool.
	 */
	public AlarmDataPool() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Create a thread pool.
	 * @arg int size - The number of threads initially created.
	 */
	public AlarmDataPool(int size) {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		workQueue = new AlarmLocQueue(this);
		workThreadList = new ArrayList();
		this.threadCount = size;
		started = false;
	}

	/**
	 * Starts the thread pool running. Each thread in the pool waits
	 * for work to be added using the add() method.
	 */
	public void startPool() {
		if (!started) {
			started = true;
			for (int i = 0; i < this.threadCount; i++) {
				WorkerThread work = new WorkerThread("AlarmPoolDataThread: "
						+ workThreadList.size());
				workThreadList.add(work);
				work.start();
			}
			Log.getInstance().outLog(
					"start " + (workThreadList.size()) + " AlarmDataPool Success.");

			timer = new Timer(true); // 启动定时器
			timer.schedule(new CheckAlarmPool(this, workQueue), new Date(System
					.currentTimeMillis()), checkTime * 60 * 1000);
			
			//批量入库定时器
			Timer saveTimer = new Timer();
			saveTimer.schedule(new TimerTask() {
				public void run() {
					setTimeUp(true);

					try {
						ArrayList works = (ArrayList) workQueue.getActiveWork();
						ArrayList speedworks = (ArrayList) workQueue.getSpeedWork();
						ArrayList areaworks = (ArrayList) workQueue.getAreaWork();
						ArrayList lineworks = (ArrayList) workQueue.getLineWork();

						Log.getInstance().outLog("active alarm 按时入库队列长度：" + works.size());
						Log.getInstance().outLog("speed alarm 按时入库队列长度：" + speedworks.size());
						Log.getInstance().outLog("area alarm 按时入库队列长度：" + areaworks.size());
						Log.getInstance().outLog("line alarm 按时入库队列长度：" + lineworks.size());
						 
					} catch (Exception ie) {
						Log.getInstance().errorLog(ie.getMessage(), ie);
					} finally {
						
					}
				}

			}, saveTime * 1000, saveTime * 1000);
		}
	}

	public synchronized void checkAllThreads() {
		Log.getInstance().outLog("start to check alarm dataPool thread status");
		for (int i = workThreadList.size(); i > 0; i--) { // 逐个遍厉
			WorkerThread workThread = (WorkerThread) workThreadList.get(i - 1);
			if (!(workThread.isAlive())) {
				workThreadList.remove(i - 1);
			}
		}
		while (workThreadList.size() < this.threadCount) {
			WorkerThread work = new WorkerThread("dataThread: "
					+ workThreadList.size());
			workThreadList.add(work);
			work.start();
		}
		Log.getInstance().outLog(
				"end to check alarm dataPool thread status,total thread count is:"
						+ workThreadList.size());
	}

	/**
	 * Add work to the queue.
	 * @arg Runnable task - the task that is to be run.
	 */
	public  void add(ParseBase task) {
		count++;
		workQueue.addWork((ParseBase) task.clone());

	}

	public  void setTimeUp(boolean timeUp) {
		this.timeUp = timeUp;
	}

	public boolean isTimeUp() {
		return timeUp;
	}

	/**
	 * inner class that does all the work
	 */
	private class WorkerThread extends Thread {
		private boolean isLive = false;

		private WorkerThread(String name) {
			setName(name);
			isLive = true;
		}

		public synchronized void run() {
			while (isLive) {
				try {
					ArrayList works = (ArrayList) workQueue.getActiveWork();
					ArrayList speedworks = (ArrayList) workQueue.getSpeedWork();
					ArrayList areaworks = (ArrayList) workQueue.getAreaWork();
					ArrayList lineworks = (ArrayList) workQueue.getLineWork();

					Log.getInstance().outLog("active alarm 按批入库队列长度：" + works.size());
					Log.getInstance().outLog("speed alarm 按批入库队列长度：" + speedworks.size());
					Log.getInstance().outLog("area alarm 按批入库队列长度：" + areaworks.size());
					Log.getInstance().outLog("line alarm 按批入库队列长度：" + lineworks.size());
					 

				} catch (Exception ie) {
					isLive = false;
					//Log.getInstance().outLog(ie.getMessage());
					Log.getInstance().errorLog(ie.getMessage(), ie);
				}
			}
		}
	}
}
