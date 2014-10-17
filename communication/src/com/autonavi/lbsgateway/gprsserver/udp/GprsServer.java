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
 * λ�������ն˷���.���������ն��ϱ�������,���ҽ�����Ӧ�Ĵ���.
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
   * @param port int:�˿ں�
   * @param parsethreadCount int:�����̳߳��̸߳���
   * @param savethreadCount int:����̳߳��̸߳���
   * @param log Logger: ��־��
   * @throws IOException
   */
  public GprsServer(int port,int parsethreadCount,int savethreadCount,Logger log) throws IOException {
	  Log.getInstance().outLog("����UDPͨѶ����");
	  logger = log;
    // ����һ��GPRS���ݽ����̳߳�
    pool = new ThreadPool(parsethreadCount);
    pool.startPool();
    //����һ����������̳߳�
    DataPool dataPool=new DataPool(savethreadCount);
    dataPool.startPool();
    
    //��ȡ��
    parsePool=new ReadPool(20,dataPool,pool);
    parsePool.startPool();
    //����һ���µ�selector
    selector = Selector.open();
    //����һ���µ�serverSocketChannel
    serverSocketChannel = DatagramChannel.open();

    //�󶨱��ض˿�
    DatagramSocket sock= serverSocketChannel.socket();
    
    //�����С��Ϊ50M
    sock.setReceiveBufferSize(50*1024*1024);
   
    sock.bind(new InetSocketAddress(port));
   // Log.getInstance().outLog("sock.getReceiveBufferSize:"+sock.getReceiveBufferSize());
    Log.getInstance().outLog("�����ն�UDP����ɹ�!�˿ں�:" + port);
   
   
	
    //����Ϊ�Ƕ���ģʽ
    serverSocketChannel.configureBlocking(false);
    //ע�ᡮ���ա��¼�
    SelectionKey sk = serverSocketChannel.register(selector,
                                                   SelectionKey.OP_READ);
    
    //�ڡ����ա��¼�������ִ���¼���.
    //sk.attach(new AcceptHandler(pool,dataPool, selector, sk, logger));
    
    
   // sk.attach(new ReadHandler(pool, dataPool,new GprsSocketChannel((DatagramChannel)sk.channel()), sk) );
    
    // sk.attach(new ReadHandler(pool, dataPool, sk) );
       
    
   // GprsSocketChannel gs = new GprsSocketChannel(serverSocketChannel);
  }

  public int getPort() {
    return port;
  }

  //ѭ�������selector��ע����¼�
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
          Log.getInstance().errorLog("λ������:���ݽ��շ�������쳣:"+ex.getMessage(),ex);
          ex.printStackTrace();
        }
      }
    } catch (Exception ex) {
        Log.getInstance().outLog("λ������:���ݽ��շ�������쳣���������������ݽ��շ���!!!!!!!!!!!!!!!!");
    }
  }
}
