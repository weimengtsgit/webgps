package com.autonavi.lbsgateway.gprsserver.tcp;

 
import java.io.*;
import java.nio.channels.*;
import java.util.logging.*;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;

// Class: Acceptor
// Accepts connection requests from clients that arrive at serverSocketChannel
// and set the new connection socket as SelecteableChannel
// on the selector, with register key over read operations
public class AcceptTcpHandler implements Runnable {
  final Selector selector;
  private ThreadPool pool;
  private DataPool dataPool;
  private ServerSocketChannel serverSocketChannel;
  private Logger logger;
  public AcceptTcpHandler( ThreadPool pool, DataPool dataPool,
                  Selector selector,
                  ServerSocketChannel serverSocketChannel,
                  Logger logger) {
    this.pool = pool;
    this.dataPool=dataPool;
    this.selector = selector;
    this.logger = logger;
    this.serverSocketChannel = serverSocketChannel;
  }

  public void run() {
    try {
      // Accept new client connection request
      SocketChannel socketChannel = serverSocketChannel.accept();
      if (socketChannel != null) {
	// Set the blocking mode of the SocketChannel to false
	// (i.e. doing I/O operation on this channel will block
	// until it completes)
	socketChannel.configureBlocking(false);

	// Define a selection key over the SocketChannel
	// by registering it over READ events
	// READ operation on the server socket will be selected
	// by the selector
	SelectionKey sk = socketChannel.register(selector,
                                                 SelectionKey.OP_READ);

        // Attach a Handler to the selection key
        // The Handler is defined over the SocketChannel and the ThreadPool
	
	sk.attach(new ReadTcpHandler(pool, dataPool,sk));

        // Cause the first selection operation that has not yet returned
	// to return immediately
//	selector.wakeup();
      }
    }
    catch(IOException ex) {
	logger.log(Level.WARNING ,"Acceptor : failed...");
        Log.getInstance().outLog(ex.getMessage());
        ex.printStackTrace();
    }
  }
}
