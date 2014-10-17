package com.autonavi.lbsgateway.poolsave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

import com.autonavi.directl.Log;
import com.autonavi.directl.parse.ParseBase;

public class DataPool {
	private ArrayList workThreadList;
	private DataWorkQueue workQueue;
	private java.util.Timer timer = null; // ��ʱ��
	private int checkTime = 5;//�߳��Լ�ķ�����
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
	public DataPool() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Create a thread pool.
	 * @arg int size - The number of threads initially created.
	 */
	public DataPool(int size) {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		workQueue = new DataWorkQueue(this);
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
				WorkerThread work = new WorkerThread("PoolDataThread: "
						+ workThreadList.size());
				workThreadList.add(work);
				work.start();
			}
			Log.getInstance().outLog(
					"start " + (workThreadList.size()) + " DataPool Success.");

			timer = new Timer(true); // ������ʱ��
			timer.schedule(new CheckDataPool(this, workQueue), new Date(System
					.currentTimeMillis()), checkTime * 60 * 1000);
			
			//������ⶨʱ��
			Timer saveTimer = new Timer();
			saveTimer.schedule(new TimerTask() {
				public void run() {
					setTimeUp(true);

					try {
						ArrayList works = (ArrayList) workQueue.getWork();

						Log.getInstance().outLog("��ʱ�����г��ȣ�" + works.size());
						BatchSave batchsave = new BatchSave();
						batchsave.setDataList(works);
						batchsave.batchSave();
						//setTimeUp(false);
					} catch (Exception ie) {
						Log.getInstance().errorLog(ie.getMessage(), ie);
					} finally {
						
					}
				}

			}, saveTime * 1000, saveTime * 1000);
		}
	}

	public synchronized void checkAllThreads() {
		Log.getInstance().outLog("start to check dataPool thread status");
		for (int i = workThreadList.size(); i > 0; i--) { // �������
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
				"end to check dataPool thread status,total thread count is:"
						+ workThreadList.size());
	}

	/**
	 * Add work to the queue.
	 * @arg Runnable task - the task that is to be run.
	 */
	public synchronized void add(ParseBase task) {
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
					ArrayList works = (ArrayList) workQueue.getWork();

					Log.getInstance().outLog("�����г��ȣ�" + works.size());
					BatchSave batchsave = new BatchSave();
					batchsave.setDataList(works);
					batchsave.batchSave();

				} catch (Exception ie) {
					isLive = false;
					//Log.getInstance().outLog(ie.getMessage());
					Log.getInstance().errorLog(ie.getMessage(), ie);
				}
			}
		}
	}
}
