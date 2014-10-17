package com.autonavi.lbsgateway.clientserver;
/**
 * The queue where all the work is placed.
 */

import java.util.*;
import com.autonavi.directl.Log;
class ClientWorkQueue
{
private LinkedList work;
  public ClientWorkQueue() {
    work = new LinkedList();
  }

  public synchronized void addWork(Runnable task) {
     Log.getInstance().outLog("There has "+work.size()+" Data in GateWayPool , waitting. ");
    work.add(task);
    notify();
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

