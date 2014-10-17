/**
 * 
 */
package com.autonavi.lbsgateway.poolsave;

import java.nio.channels.SelectionKey;
import java.util.LinkedList;

import com.autonavi.directl.Log;

/**
 * @author xiaojun.luan
 *
 */
class ReadWorkQueue {

private LinkedList work;
  public ReadWorkQueue() {
    work = new LinkedList();
  }

  public synchronized void addWork(SelectionKey task) {
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