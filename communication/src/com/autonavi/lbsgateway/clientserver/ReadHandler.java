package com.autonavi.lbsgateway.clientserver;

import java.nio.channels.*;
import java.lang.*;
import com.autonavi.lbsgateway.poolsave.DataPool;
import com.autonavi.lbsgateway.ClientThread;
// Class: Handler
// 1. Read a message,
// 2. Set processor for the massage on the works' queue of the thread pool
class ReadHandler
    implements Runnable {
  private ClientSocketChannel sc;
  private ClientPool pool;
  private SelectionKey key;
  public ReadHandler(ClientPool p, ClientSocketChannel sc,  SelectionKey key) {
    this.pool = p;
    this.sc = sc;
    this.key = key;
  }

  public void run() {
    // read all data in the socket.
    byte[] socketData = this.sc.readSocketBytes(this.key);
    if (socketData == null) {
      return;
    }
    pool.add(new ClientThread(this.sc, socketData));
  }
}
