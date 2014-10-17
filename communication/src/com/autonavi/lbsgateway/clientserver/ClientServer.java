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
 * 位置网关的客户端服务。用来接收客户端发出的指令,然后下发到终端,对终端进行控制,返回控制结果。
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
    //构造一个客户端数据解析线程池
    pool = new ClientPool(threadCount);
    pool.startPool();
    //创建一个新的selector
    selector = Selector.open();
    //创建一个新的ServerSocketChannel
    serverSocketChannel = ServerSocketChannel.open();
    //绑定本地端口
    ServerSocket sock = serverSocketChannel.socket();
    sock.bind(new InetSocketAddress(port), 3000);
    Log.getInstance().outLog("启动终端Client服务成功!端口号:" + port);
    //设置为非堵塞模式
    serverSocketChannel.configureBlocking(false);

    //注册‘接收’事件
    SelectionKey sk = serverSocketChannel.register(selector,
        SelectionKey.OP_ACCEPT);
    //在‘接收’事件中设置执行事件类.
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
          Log.getInstance().errorLog("位置网关:客户接收服务出现异常:"+ex.getMessage(),ex);
        }
      }
    }
    catch (Exception ex) {
      Log.getInstance().outLog("位置网关:客户接收服务出现异常，请重新启动数据接收服务!!!!!!!!!!!!!!!!");
    }
  }

}
