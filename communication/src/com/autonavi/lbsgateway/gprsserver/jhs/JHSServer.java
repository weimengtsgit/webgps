package com.autonavi.lbsgateway.gprsserver.jhs;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;

 

public class JHSServer extends Thread  {
	  final Selector selector;
	  final ServerSocketChannel serverSocketChannel;
	  private int port;
	  private ThreadPool pool;
	  private DataPool dataPool;
	  public JHSServer(int port,Clientable lbsClient ) throws IOException {
		    pool = new ThreadPool(5);
		    pool.startPool();
		    dataPool = new DataPool(5);
		    //dataPool.startPool();
		    
		    selector = Selector.open();
		    serverSocketChannel = ServerSocketChannel.open();
		    ServerSocket sock= serverSocketChannel.socket();
		    sock.bind(new InetSocketAddress(port),3000);
		    Log.getInstance().outJHSLoger("启动交互式监听服务成功!端口号:" + port);
		    serverSocketChannel.configureBlocking(false);
		    SelectionKey sk = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
		    sk.attach(new JHSAcceptHandler(pool,dataPool,lbsClient, selector, serverSocketChannel));
	  }

	  //循环监控在selector上注册的事件
	  public void run() {
	    try {
	    SelectionKey key=null ;
	      while (true) {
	        try {
	          selector.select();
	          Set selected = selector.selectedKeys();
	          Iterator it = selected.iterator();
	          while (it.hasNext()) {
	             key = (SelectionKey) it.next();
	             Runnable r = (Runnable) key.attachment();
	            if (r != null)
	              r.run();
	          }
	          selected.clear();
	        }
	        catch (Exception ex) {
	          key.cancel();
	          Log.getInstance().errorLog("位置网关:数据接收服务出现异常:"+ex.getMessage(),ex);
	          ex.printStackTrace();
	        }
	      }
	    } catch (Exception ex) {
	        Log.getInstance().outJHSLoger("位置网关:数据接收服务出现异常，请重新启动数据接收服务!!!!!!!!!!!!!!!!");
	    }
	  }	

}
