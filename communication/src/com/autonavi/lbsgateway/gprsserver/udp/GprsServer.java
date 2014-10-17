package com.autonavi.lbsgateway.gprsserver.udp;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.util.*;
import java.nio.charset.*;
import java.util.logging.*;

import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.gprsserver.ThreadPool;
import com.autonavi.lbsgateway.poolsave.DataPool;
import com.autonavi.lbsgateway.poolsave.ReadPool;
 
/**
 * 位置网关终端服务.用来接收终端上报的数据,并且进行相应的处理.
 */

public class GprsServer  extends Thread {
  final Selector selector;
  final DatagramChannel serverSocketChannel;
  private int port;
  private ThreadPool pool;
  private ReadPool parsePool;
  private Logger logger;

  /**
   *
   * @param port int:端口号
   * @param parsethreadCount int:解析线程池线程个数
   * @param savethreadCount int:入库线程池线程个数
   * @param log Logger: 日志类
   * @throws IOException
   */
  public GprsServer(int port,int parsethreadCount,int savethreadCount,Logger log) throws IOException {
	  Log.getInstance().outLog("启动UDP通讯服务");
	  logger = log;
    // 构造一个GPRS数据解析线程池
    pool = new ThreadPool(parsethreadCount);
    pool.startPool();
    //构造一个数据入库线程池
    DataPool dataPool=new DataPool(savethreadCount);
    dataPool.startPool();
    
    //读取池
    parsePool=new ReadPool(20,dataPool,pool);
    parsePool.startPool();
    //创建一个新的selector
    selector = Selector.open();
    //创建一个新的serverSocketChannel
    serverSocketChannel = DatagramChannel.open();

    //绑定本地端口
    DatagramSocket sock= serverSocketChannel.socket();
    
    //缓存大小设为50M
    sock.setReceiveBufferSize(50*1024*1024);
   
    sock.bind(new InetSocketAddress(port));
   // Log.getInstance().outLog("sock.getReceiveBufferSize:"+sock.getReceiveBufferSize());
    Log.getInstance().outLog("启动终端UDP服务成功!端口号:" + port);
   
   
	
    //设置为非堵塞模式
    serverSocketChannel.configureBlocking(false);
    //注册‘接收’事件
    SelectionKey sk = serverSocketChannel.register(selector,
                                                   SelectionKey.OP_READ);
    
    //在‘接收’事件中设置执行事件类.
    //sk.attach(new AcceptHandler(pool,dataPool, selector, sk, logger));
    
    
   // sk.attach(new ReadHandler(pool, dataPool,new GprsSocketChannel((DatagramChannel)sk.channel()), sk) );
    
    // sk.attach(new ReadHandler(pool, dataPool, sk) );
       
    
   // GprsSocketChannel gs = new GprsSocketChannel(serverSocketChannel);
  }

  public int getPort() {
    return port;
  }

  //循环监控在selector上注册的事件
  public synchronized void run() {
    try {
    SelectionKey key=null ;
      while (true) {
        try {
          selector.select();
          Set selected = selector.selectedKeys();
          Iterator it = selected.iterator();
          while (it.hasNext()) {
            key = (SelectionKey) it.next();
            parsePool.add(key);
            it.remove();
            //Runnable r = (Runnable) key.attachment();
            //if (r != null){
            //  r.run();
            //  it.remove();
            //}
          }
         
          selected.clear();
        }
        catch (Exception ex) {
          key.cancel();
          key.channel().close();
          Log.getInstance().errorLog("位置网关:数据接收服务出现异常:"+ex.getMessage(),ex);
          ex.printStackTrace();
        }
      }
    } catch (Exception ex) {
        Log.getInstance().outLog("位置网关:数据接收服务出现异常，请重新启动数据接收服务!!!!!!!!!!!!!!!!");
    }
  }
}
