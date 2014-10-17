package com.autonavi.lbsgateway.gprsserver.tcp;

import java.nio.channels.*;
import java.lang.*;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.GPRSThread;
import com.autonavi.lbsgateway.GprsTcpThread;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;
 
// Class: Handler
// 1. Read a message,
// 2. Set processor for the massage on the works' queue of the thread pool
class ReadTcpHandler
    implements Runnable {
  private GprsSocketTcpChannel gprsSocketChannel;
  private ThreadPool pool;
  private DataPool dataPool;
  private SelectionKey key;
  public ReadTcpHandler(ThreadPool p,DataPool dataPool,  SelectionKey key) {
    this.pool = p;
    this.dataPool=dataPool;

    this.key = key;
  }

  public void run() {
    // read all data in the socket.
	 this.gprsSocketChannel = new GprsSocketTcpChannel((SocketChannel)key.channel());
     byte[] socketData = gprsSocketChannel.readSocketBytes(this.key); // manipulates dataFromSocket
     
    if (socketData == null) {
      return;
    }
    pool.add(new GprsTcpThread(dataPool,gprsSocketChannel, socketData));
  }

}