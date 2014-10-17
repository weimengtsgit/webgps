package com.autonavi.lbsgateway.alarmpool;

import java.util.TimerTask;

import com.autonavi.lbsgateway.poolsave.DataPool;

public class CheckAlarmPool extends TimerTask {
	private static boolean isRunning = false;
	private AlarmDataPool pool;
	private AlarmLocQueue workQueue = null;

	public CheckAlarmPool(AlarmDataPool datapool, AlarmLocQueue workQueue) {
		this.pool = datapool;
		this.workQueue = workQueue;
	}

	public void run() {
		if (!isRunning) {
			isRunning = true;
			pool.checkAllThreads();
			isRunning = false;
		}
	}
}