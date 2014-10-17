package com.autonavi.lbsgateway.clientserver;

import java.util.TimerTask;

public class CheckClientPool
    extends TimerTask {
  private static boolean isRunning = false;
  private ClientPool pool;
  public CheckClientPool(ClientPool threadPool) {
    this.pool=threadPool;
  }
  public void run() {
    if (!isRunning) {
      isRunning = true;
      pool.checkAllThreads();
      isRunning = false;
    }
  }
}
