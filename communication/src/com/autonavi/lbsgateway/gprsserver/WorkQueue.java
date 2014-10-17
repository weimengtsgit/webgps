package com.autonavi.lbsgateway.gprsserver;
/**
 * The queue where all the work is placed.
 */

import java.util.*;

import com.autonavi.directl.Log;
 
class WorkQueue
{
private LinkedList work;
  public WorkQueue() {
    work = new LinkedList();
  }

  public synchronized void addWork(Runnable task) {
     //Log.getInstance().outLog("There has "+work.size()+" Data in GateWayPool , waitting. ");
    work.add(task);
    notify();//有任务时，唤醒等待的线程
  }

  public synchronized Object getWork() throws InterruptedException {

    while (work.isEmpty()) {
      try {
        wait();
      }
      catch (InterruptedException ie) {
        throw ie;
      }
    }
    return work.remove(0);
  }
}

