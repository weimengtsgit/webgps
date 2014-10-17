 package com.autonavi.lbsgateway.gprsserver.tcp;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.util.*;
import java.nio.charset.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

import com.autonavi.directl.Log;

import com.autonavi.lbsgateway.TcpLinkCache;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;

/**
 * 位置网关终端服务.用来接收终端上报的数据,并且进行相应的处理.
 */

public class GprsTcpServer extends Thread {
	final Selector selector;
	final ServerSocketChannel serverSocketChannel;
	private SelectionKey sk ;
	private SelectionKey csk ;
	private SocketChannel socketChannel;
	private int port;
	private ThreadPool pool;
	private DataPool dataPool;
	private Logger logger;
	private ExecutorService exec;

	/**
	 * 
	 * @param port
	 *            int:端口号
	 * @param parsethreadCount
	 *            int:解析线程池线程个数
	 * @param savethreadCount
	 *            int:入库线程池线程个数
	 * @param log
	 *            Logger: 日志类
	 * @throws IOException
	 */
	public GprsTcpServer(int port, int parsethreadCount, int savethreadCount) throws IOException {
		Log.getInstance().outLog("启动TCP通讯服务");
 

		// 构造一个GPRS数据解析线程池
		pool = new ThreadPool(parsethreadCount);
		pool.startPool();
		// 构造一个数据入库线程池
		  dataPool = new DataPool(savethreadCount);
		dataPool.startPool();

		// parsePool = new TcpReadPool(conThdNum,dataPool,pool);
		// parsePool.startPool();

		// 创建一个新的selector
		selector = Selector.open();

		// 创建一个新的serverSocketChannel
		serverSocketChannel = ServerSocketChannel.open();

		// 绑定本地端口
		ServerSocket sock = serverSocketChannel.socket();
		// 缓存大小设为50M
		sock.setReceiveBufferSize(50 * 1024 * 1024);
		sock.setReuseAddress(true);
		sock.bind(new InetSocketAddress(port), 3000);
		// 设置为非堵塞模式
		serverSocketChannel.configureBlocking(false);

		Log.getInstance().outLog("启动终端TCP GPRS服务成功!端口号:" + port);

		// 注册‘接收’事件,serverSocketChannel注册ACCEPT事件，SelectionKey跟踪被注册的事件。
		  sk = serverSocketChannel.register(selector,
				SelectionKey.OP_ACCEPT);
		// 在‘接收’事件中设置执行事件类,SelectionKey关联一个附件AcceptTcpHandler，当事件发生时可以从SelectionKey
		// 中获得这个附件，该附件用来包含和处理这个时间的相关信息。
//		sk.attach(new AcceptTcpHandler(pool, dataPool, selector,
//				serverSocketChannel, logger));

		exec = Executors.newFixedThreadPool(100);
	}

	public int getPort() {
		return port;
	}

	// 循环监控在selector上注册的事件
	public void run() {
		try {
			SelectionKey key = null;
			while (true) {
				try {
					int n = selector.select();
					if (n == 0)
						continue;

					Set selected = selector.selectedKeys();

					Iterator it = selected.iterator();
					while (it.hasNext()) {
						key = (SelectionKey) it.next();
						it.remove();
						if (!key.isValid()){
							continue;
						}
 
						if (key.isAcceptable()) {
							 socketChannel = serverSocketChannel
									.accept();

							if (socketChannel != null) {
								Log.getInstance().outLog("connection accept, from "
										+ socketChannel.socket().getInetAddress().getHostAddress() + " : "
										+ socketChannel.socket().getPort());
								Socket socket = socketChannel.socket();
								TcpLinkCache.getInstance().addToTcpCache(socket, new Date());
								
 								socketChannel.configureBlocking(false);
 
 								csk = socketChannel.register(
										selector, SelectionKey.OP_READ);
								csk.attach(new ReadTcpHandler(this.pool, this.dataPool, csk));
 
							}
						}else if (key.isReadable()){

							ReadTcpHandler readHandler = (ReadTcpHandler)key.attachment();
							if (readHandler != null)
							exec.execute(readHandler);
							// Log.getInstance().outLog("read socketChannel csk.isAcceptable()="+csk.isAcceptable()+",csk.isReadable()="+csk.isReadable()+",csk.isValid()"+csk.isValid());

						}

//						Runnable r = (Runnable) key.attachment();
//						if (r != null) {
//
//							exec.execute(r);
//							Log.getInstance()
//									.outLog(
//											"receive socketChannel key.isAcceptable()="
//													+ key.isAcceptable()
//													+ ",key.isReadable()="
//													+ key.isReadable()
//													+ ",key.isValid()="
//													+ key.isValid());
// 
//							// r.run();
//							// new Thread(r).start();
//						}
						selected.remove(key);

					}
 					selected.clear();
 
				} catch (Exception ex) {
					// ((SocketChannel)key.channel()).socket().close();
//					key.channel().close();
//
//					key.cancel();
					ex.printStackTrace();
					Log.getInstance().errorLog(
							"Gps tcp server select exception!", ex);
				}
			}
		} catch (Exception ex) {
		}
	}
}
