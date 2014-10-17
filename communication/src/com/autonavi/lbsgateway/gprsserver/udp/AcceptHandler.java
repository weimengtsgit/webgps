package com.autonavi.lbsgateway.gprsserver.udp;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.logging.*;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;

// Class: Acceptor
// Accepts connection requests from clients that arrive at serverSocketChannel
// and set the new connection socket as SelecteableChannel
// on the selector, with register key over read operations
class AcceptHandler implements Runnable {
	final Selector selector;

	private ThreadPool pool;

	private DataPool dataPool;

	private SelectionKey sk;

	private Logger logger;

	public AcceptHandler(ThreadPool pool, DataPool dataPool, Selector selector,
			SelectionKey sk, Logger logger) {
		this.pool = pool;
		this.dataPool = dataPool;
		this.selector = selector;
		this.logger = logger;
		this.sk = sk;
	}

	public synchronized void run() {
		try {
			// Accept new client connection request
			DatagramChannel socketChannel = (DatagramChannel) sk.channel();

			if (socketChannel != null) {

				socketChannel.configureBlocking(false);
				
				SelectionKey skc = socketChannel.register(selector,
                        SelectionKey.OP_READ);

 				sk.attach(new ReadHandler(pool, dataPool, skc));
			}
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Acceptor : failed...");
			Log.getInstance().outLog(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
