package com.autonavi.lbsgateway.clientserver;

import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;

public class DataSendTask
    extends Thread {
  public java.util.ArrayList dataList = new java.util.ArrayList();
  public SocketChannel sc = null;
  boolean threadFlag = true;
  public void setThreadFlag(boolean b) {
    threadFlag = b;
  }

  public DataSendTask(SocketChannel sc) {
    this.sc=sc;
  }
  public synchronized void insertData(String dataStr) {
    dataList.add(dataStr);
  }
  private synchronized String getDataStr() {
    if (dataList.size() == 0)return null;
    String retStr = (String) dataList.get(0);
    if (retStr != null) {
      dataList.remove(0);
    }
    return retStr;
  }

  public void run() {
    while (threadFlag) {
      try {
        String sendData = getDataStr();
        if (sc != null && sendData != null) {
       System.out.println("ÏÂ·¢:"+sendData);
          byte[] bs=sendData.getBytes("GB2312");
          ByteBuffer bf=ByteBuffer.wrap(bs);
         this.sc.write(bf);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      finally {
        try {
          sleep(100);
        }
        catch (Exception ex1) {}
      }
    }
  }
  }
