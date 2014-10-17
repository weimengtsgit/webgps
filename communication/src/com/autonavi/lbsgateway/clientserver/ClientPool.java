package com.autonavi.lbsgateway.clientserver;

import com.autonavi.directl.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.Date;
import java.util.Iterator;

public class ClientPool {
  private ArrayList workThreadList;
  private ClientWorkQueue workQueue;
  private java.util.Timer timer = null; // 定时器
  private int checkTime=5;//线程自检的分钟数
  public static final int DEFAULT_SIZE = 3;
  private volatile boolean shouldRun;
  private boolean started;
  private int threadCount = 3;
  /**
   * Create a default size thread pool.
   */
  public ClientPool() {
    this(DEFAULT_SIZE);
  }

  /**
   * Create a thread pool.
   * @arg int size - The number of threads initially created.
   */
  public ClientPool(int size) {
    if (size < 1) {
      throw new IllegalArgumentException();
    }
    workQueue = new ClientWorkQueue();
    workThreadList = new ArrayList();
    this.threadCount = size;
    shouldRun = true;
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
        WorkerThread work = new WorkerThread("Thread: " + workThreadList.size());
        workThreadList.add(work);
        work.start();
      }
      // Log.getInstance().outLog("已经启动"+( pool.size())+"个线程.");
      timer = new Timer(true); // 启动定时器
      timer.schedule(new CheckClientPool(this),
                     new Date(System.currentTimeMillis()),
                     checkTime * 60 * 1000);

    }
  }

  public synchronized void checkAllThreads() {
	  Log.getInstance().outLog ("start to check dataPool thread status");
	  for (int i=workThreadList.size();i>0;i--) { // 逐个遍厉
	    WorkerThread workThread = (WorkerThread) workThreadList.get(i-1);
	    if (! (workThread.isAlive())) {
	      workThreadList.remove(i-1);
	    }
	  }
	  while(workThreadList.size()<this.threadCount){
	      WorkerThread work = new WorkerThread("dataThread: " +workThreadList.size());
	      workThreadList.add(work);
	      work.start();
	  }
	    Log.getInstance().outLog ("end to check dataPool thread status,total thread count is:"+workThreadList.size());
	  }

  /**
   * Stop the pool.
   */
  public void stopPool() {
    shouldRun = false;
  }

  /**
   * Add work to the queue.
   * @arg Runnable task - the task that is to be run.
   */
  public void add(Runnable task) {
    workQueue.addWork(task);
  }

  /**
   * inner class that does all the work
   */
  private class WorkerThread
      extends Thread {
    private boolean isLive = false;
    private WorkerThread(String name) {
      setName(name);
      isLive = true;
    }

    public void run() {
      while (isLive) {
        try {
          //   Log.getInstance().outLog("线程池共:"+pool.size()+"个线程");
          Runnable work = (Runnable) workQueue.getWork();
          work.run();
        }
        catch (Exception ie) {
          isLive = false;
          Log.getInstance().outLog(ie.getMessage());
          Log.getInstance().errorLog(ie.getMessage(), ie);
        }
      }
    }
  }
}
