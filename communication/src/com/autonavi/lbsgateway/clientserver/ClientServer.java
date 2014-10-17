package com.autonavi.lbsgateway.clientserver;

import java.net.*;
import java.io.*;

import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;

import java.util.*;
import java.nio.charset.*;

import java.util.logging.*;
import com.autonavi.directl.Config;
import com.autonavi.directl.Log;

import com.autonavi.lbsgateway.poolsave.DataPool;

/**
 * λ�����صĿͻ��˷����������տͻ��˷�����ָ��,Ȼ���·����ն�,���ն˽��п���,���ؿ��ƽ����
 */

public class ClientServer
    extends Thread {
  final Selector selector;
  final ServerSocketChannel serverSocketChannel;

  private int port;
  private ClientPool pool;

  private Logger logger;

  public ClientServer(int port, Logger log, int threadCount) throws IOException {
    logger = log;
    //����һ���ͻ������ݽ����̳߳�
    pool = new ClientPool(threadCount);
    pool.startPool();
    //����һ���µ�selector
    selector = Selector.open();
    //����һ���µ�ServerSocketChannel
    serverSocketChannel = ServerSocketChannel.open();
    //�󶨱��ض˿�
    ServerSocket sock = serverSocketChannel.socket();
    sock.bind(new InetSocketAddress(port), 3000);
    Log.getInstance().outLog("�����ն�Client����ɹ�!�˿ں�:" + port);
    //����Ϊ�Ƕ���ģʽ
    serverSocketChannel.configureBlocking(false);

    //ע�ᡮ���ա��¼�
    SelectionKey sk = serverSocketChannel.register(selector,
        SelectionKey.OP_ACCEPT);
    //�ڡ����ա��¼�������ִ���¼���.
    sk.attach(new AcceptHandler(pool, selector, serverSocketChannel, logger));
  }

  public int getPort() {
    return port;
  }

  // Function: run
  // Detects events on the socket channels via the Selector,
  // and reacts according to the runnable object attached to them.
  public void run() {
    try {
      while (true) {
        try {
          selector.select();
          Set selected = selector.selectedKeys();
          Iterator it = selected.iterator();
          while (it.hasNext()) {
            SelectionKey key = (SelectionKey) it.next();
            Runnable r = (Runnable) key.attachment();
            if (r != null) {
              r.run();
            }
          }
          selected.clear();
        }
        catch (Exception ex) {
          Log.getInstance().errorLog("λ������:�ͻ����շ�������쳣:"+ex.getMessage(),ex);
        }
      }
    }
    catch (Exception ex) {
      Log.getInstance().outLog("λ������:�ͻ����շ�������쳣���������������ݽ��շ���!!!!!!!!!!!!!!!!");
    }
  }

}
