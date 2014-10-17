package com.autonavi.lbsgateway.gprsserver.jhs;

import java.nio.channels.SelectionKey;

import com.autonavi.lbsgateway.GprsTcpThread;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.gprsserver.tcp.GprsSocketTcpChannel;
import com.autonavi.lbsgateway.poolsave.DataPool;

 
public class JHSReadHandler 
implements Runnable {
	  private JHSChannel gprsSocketChannel;
	  private ThreadPool pool;
	  private DataPool dataPool;
	  private SelectionKey key;
	  private Clientable lbsClient;
	  public JHSReadHandler(ThreadPool p, DataPool dataPool,JHSChannel s,  SelectionKey key,Clientable lbsClient) {
	    this.pool = p;
	    this.gprsSocketChannel = s;
	    this.key = key;
	    this.lbsClient = lbsClient;
	    this.dataPool = dataPool;
	  }
	  public void run() {
	    byte[] socketData = this.gprsSocketChannel.readSocketBytes(this.key); // manipulates dataFromSocket
	    if (socketData == null) {
	      return;
	    }else{
		    String ret="OK\r\n";
		     this.gprsSocketChannel.sendByteArrayData(ret.getBytes());
	    }
	     pool.add(new JHSHandler( this.lbsClient,socketData));
	    // pool.add(new GprsTcpThread(dataPool,gprsSocketChannel, socketData));
	  }
	  
	}
