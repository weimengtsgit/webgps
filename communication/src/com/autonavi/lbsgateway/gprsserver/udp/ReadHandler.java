package com.autonavi.lbsgateway.gprsserver.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.IOException;
import java.lang.*;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.GPRSThread;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;
 
// Class: Handler
// 1. Read a message,
// 2. Set processor for the massage on the works' queue of the thread pool
class ReadHandler
    implements Runnable {
  private GprsSocketChannel gprsSocketChannel;
  private ThreadPool pool;
  private DataPool dataPool;
  private SelectionKey key;
  private DatagramSocket ds;
  public ReadHandler(ThreadPool p,DataPool dataPool,  SelectionKey key) {
    this.pool = p;
    this.dataPool=dataPool;
    //this.gprsSocketChannel = s;
    this.key = key;
    
  }

  public synchronized void run() {
    // read all data in the socket.
 

	 this.gprsSocketChannel = new GprsSocketChannel((DatagramChannel)this.key.channel());
     byte[] socketData = this.gprsSocketChannel.readSocketBytes(this.key); // manipulates dataFromSocket
     
    if (socketData == null || socketData.length==0) {
      return;
    }
    pool.add(new GPRSThread(dataPool,gprsSocketChannel, socketData));
  }

}
 