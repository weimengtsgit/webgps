package com.autonavi.lbsgateway.poolsave;

import java.util.ArrayList;
import java.util.TimerTask;

import com.autonavi.directl.Log;

public class CheckDataPool
    extends TimerTask {
  private static boolean isRunning = false;
  private DataPool pool;
  private DataWorkQueue workQueue = null;
  public CheckDataPool(DataPool datapool, DataWorkQueue workQueue) {
    this.pool=datapool;
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
