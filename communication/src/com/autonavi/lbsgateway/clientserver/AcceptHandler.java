package com.autonavi.lbsgateway.clientserver;
import java.io.*;
import java.nio.channels.*;
import java.util.logging.*;
// Class: Acceptor
// Accepts connection requests from clients that arrive at serverSocketChannel
// and set the new connection socket as SelecteableChannel
// on the selector, with register key over read operations
class AcceptHandler implements Runnable {
  final Selector selector;
  private ClientPool pool;
  private ServerSocketChannel serverSocketChannel;
  private Logger logger;
  public AcceptHandler(ClientPool pool, Selector selector,
                  ServerSocketChannel serverSocketChannel,
                  Logger logger) {
    this.pool = pool;
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
	sk.attach(new ReadHandler(pool,new ClientSocketChannel(socketChannel),sk));

        // Cause the first selection operation that has not yet returned
	// to return immediately
//	selector.wakeup();
      }
    }
    catch(IOException ex) {
	logger.log(Level.WARNING ,"Acceptor : failed...");
    }
  }
}
