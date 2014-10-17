package com.autonavi.lbsgateway.gprsserver.jhs;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.gprsserver.tcp.GprsSocketTcpChannel;
import com.autonavi.lbsgateway.poolsave.DataPool;
 

public class JHSAcceptHandler implements Runnable {
	  final Selector selector;
	  private ThreadPool pool;
	  private DataPool dataPool;
	  private Clientable lbsClient;
	  private ServerSocketChannel serverSocketChannel;
	  public JHSAcceptHandler( ThreadPool pool, DataPool dataPool,Clientable lbsClient,
	                  Selector selector,
	                  ServerSocketChannel serverSocketChannel) {
	    this.pool = pool;
	    this.lbsClient=lbsClient;
	    this.selector = selector;
	    this.serverSocketChannel = serverSocketChannel;
	    this.dataPool = dataPool;
	  }

	  public void run() {
	    try {
	    	SocketChannel socketChannel = serverSocketChannel.accept();
	      	if (socketChannel != null) {
				socketChannel.configureBlocking(false);
				SelectionKey sk = socketChannel.register(selector,SelectionKey.OP_READ);
				sk.attach(new JHSReadHandler(pool,dataPool,new JHSChannel(socketChannel),sk,lbsClient));
	      }
	    }
	    catch(IOException ex) {
	        Log.getInstance().outLog(ex.getMessage());	   
	    }
	  }
	}