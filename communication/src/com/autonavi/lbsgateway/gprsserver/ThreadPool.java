package com.autonavi.lbsgateway.gprsserver;

 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.Date;

import com.autonavi.directl.Log;

public class ThreadPool {
  private ArrayList workThreadList;
  private WorkQueue workQueue;
  private java.util.Timer timer = null; // ��ʱ��
  private int checkTime=5;//�߳��Լ�ķ�����
  public static final int DEFAULT_SIZE = 3;
  private volatile boolean shouldRun;
  private boolean started;
  private int threadCount = 3;
  /**
   * Create a default size thread pool.
   */
  public ThreadPool() {
    this(DEFAULT_SIZE);
  }

  /**
   * Create a thread pool.
   * @arg int size - The number of threads initially created.
   */
  public ThreadPool(int size) {
    if (size < 1) {
      throw new IllegalArgumentException();
    }
    workQueue = new WorkQueue();
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
      // Log.getInstance().outLog("�Ѿ�����"+( pool.size())+"���߳�.");
      timer = new Timer(true); // ������ʱ��
      timer.schedule(new CheckThreadPool(this),
                     new Date(System.currentTimeMillis()),
                     checkTime * 60 * 1000);

    }
  }
  //����̳߳��ڴ��е��̻߳״̬
  public synchronized void checkAllThreads() {
	  Log.getInstance().outLog ("start to check dataPool thread status");
	  for (int i=workThreadList.size();i>0;i--) { // �������
	    WorkerThread workThread = (WorkerThread) workThreadList.get(i-1);
	    if (! (workThread.isAlive())) {//�Ѳ���Ľ����̴߳��ڴ����Ƴ�
	      workThreadList.remove(i-1);
	    }
	  }
	  while(workThreadList.size()<this.threadCount){//���䲻������߳���threadCount���̡߳�
	      WorkerThread work = new WorkerThread("PoolParseThread: " +workThreadList.size());
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
          
          Runnable work = (Runnable) workQueue.getWork();
          work.run();
        }
        catch (Exception ie) {
            isLive = false;
            //Log.getInstance().outLog(ie.getMessage());
            Log.getInstance().errorLog(ie.getMessage(), ie);
        }
      }
    }
  }
}
