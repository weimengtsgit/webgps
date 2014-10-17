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
 * λ�������ն˷���.���������ն��ϱ�������,���ҽ�����Ӧ�Ĵ���.
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
	 *            int:�˿ں�
	 * @param parsethreadCount
	 *            int:�����̳߳��̸߳���
	 * @param savethreadCount
	 *            int:����̳߳��̸߳���
	 * @param log
	 *            Logger: ��־��
	 * @throws IOException
	 */
	public GprsTcpServer(int port, int parsethreadCount, int savethreadCount) throws IOException {
		Log.getInstance().outLog("����TCPͨѶ����");
 

		// ����һ��GPRS���ݽ����̳߳�
		pool = new ThreadPool(parsethreadCount);
		pool.startPool();
		// ����һ����������̳߳�
		  dataPool = new DataPool(savethreadCount);
		dataPool.startPool();

		// parsePool = new TcpReadPool(conThdNum,dataPool,pool);
		// parsePool.startPool();

		// ����һ���µ�selector
		selector = Selector.open();

		// ����һ���µ�serverSocketChannel
		serverSocketChannel = ServerSocketChannel.open();

		// �󶨱��ض˿�
		ServerSocket sock = serverSocketChannel.socket();
		// �����С��Ϊ50M
		sock.setReceiveBufferSize(50 * 1024 * 1024);
		sock.setReuseAddress(true);
		sock.bind(new InetSocketAddress(port), 3000);
		// ����Ϊ�Ƕ���ģʽ
		serverSocketChannel.configureBlocking(false);

		Log.getInstance().outLog("�����ն�TCP GPRS����ɹ�!�˿ں�:" + port);

		// ע�ᡮ���ա��¼�,serverSocketChannelע��ACCEPT�¼���SelectionKey���ٱ�ע����¼���
		  sk = serverSocketChannel.register(selector,
				SelectionKey.OP_ACCEPT);
		// �ڡ����ա��¼�������ִ���¼���,SelectionKey����һ������AcceptTcpHandler�����¼�����ʱ���Դ�SelectionKey
		// �л������������ø������������ʹ������ʱ��������Ϣ��
//		sk.attach(new AcceptTcpHandler(pool, dataPool, selector,
//				serverSocketChannel, logger));

		exec = Executors.newFixedThreadPool(100);
	}

	public int getPort() {
		return port;
	}

	// ѭ�������selector��ע����¼�
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
