package com.autonavi.lbsgateway.clientserver;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import com.autonavi.lbsgateway.LoginUser;
import com.autonavi.lbsgateway.ClientThreadList;
 
import com.autonavi.directl.Log;
/**
 * ClientSocketChannel:连接到网关的客户端类
 * 注意该类可能同时被多个线程使用,所以该类所有的方法必须加锁.
 */
public class ClientSocketChannel {
  private SocketChannel socketChannel;
  private LoginUser loginUser;//登入网关用户
  private DataSendTask dataSendTask;//下发数据到客户端线程
  //private mapabc.locate.Api CellLocator = new mapabc.locate.Api();//CellID 定位接口
  //private CellIDMultiLocator cellidMultiLocator;//cellid 连续定位接口

  public  ClientSocketChannel(SocketChannel socketChannel) {
    this.setSocketChannel(socketChannel);
    this.dataSendTask=new DataSendTask(socketChannel);
    this.dataSendTask.start();
  }

  /**
   * CELLID连续定位
   */
//  public synchronized void startCellIDMultiLocator(String simcards, String frec) {
//    sotpCellIDMultiLocator(); //关闭之前的连续定位
//    cellidMultiLocator = new CellIDMultiLocator(this.getLoginUser(), simcards, frec);
//    cellidMultiLocator.start();
//  }

  /**
   * 停止CELLID连续定位
   */
//  public synchronized void sotpCellIDMultiLocator() {
//    if (cellidMultiLocator != null) {
//      cellidMultiLocator.setIsLive(false);
//    }
//    cellidMultiLocator = null;
//  }
  /**
   * 关闭SocketChannel ,并且取消掉相关的任务
   * @param key SelectionKey
   */
  public synchronized void closeClient(SelectionKey key) {
  try {
    if(key!=null) key.cancel();
    this.getSocketChannel().socket().close();
    this.getSocketChannel().close();
  }
  catch (Exception ex) {
    Log.getInstance().outLog("关闭 ClientTread 失败." + ex.getMessage());
    Log.getInstance().errorLog("关闭 ClientTread 失败.", ex);
  }finally{
    if (this.getLoginUser()!=null) {
      ClientThreadList.getInstance().removeClient(this.getLoginUser());
    }
    if(this.getDataSendTask()!=null){
      this.getDataSendTask().setThreadFlag(false);
    }
    //this.sotpCellIDMultiLocator(); //关闭连续定位
  }
}


//  public synchronized  mapabc.locate.Api getCellLocator() {
//    return CellLocator;
//  }

  public synchronized SocketChannel getSocketChannel() {
    return socketChannel;
  }

  private synchronized void setSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  public synchronized LoginUser getLoginUser() {
    return loginUser;
  }

  public synchronized void setLoginUser(LoginUser loginUser) {
    this.loginUser = loginUser;
  }

  public synchronized DataSendTask getDataSendTask() {
    return dataSendTask;
  }

  /**
   * 从SocketChannel中读取数据
   * @param key SelectionKey
   * @return byte[]
   */
  public synchronized byte[] readSocketBytes(SelectionKey key) {
    SocketChannel socket=this.getSocketChannel();
    byte[] ret = null;
    boolean bRead = true;
    while (bRead) {
    try {
      ByteBuffer byteBuffer = ByteBuffer.allocate(256);
      byteBuffer.clear();
      int nbytes = socket.read(byteBuffer);
      if (nbytes == -1) {
        bRead = false;
       this.closeClient(key);
       return null;
      }
      if (nbytes > 0) {
        byteBuffer.flip();
        byte[] tmp = new byte[byteBuffer.limit()];
        byteBuffer.get(tmp, 0, byteBuffer.limit());
        if (ret == null) {
          ret = new byte[tmp.length];
          System.arraycopy(tmp, 0, ret, 0, tmp.length);
        }
        else {
          byte[] tmpret = new byte[ret.length];
          System.arraycopy(ret, 0, tmpret, 0, ret.length);
          ret = new byte[ret.length + tmp.length];
          System.arraycopy(tmpret, 0, ret, 0, tmpret.length);
          System.arraycopy(tmp, 0, ret, tmpret.length, tmp.length);
        }
      }
      if (nbytes < 256) {
        return ret;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      bRead = false;
      this.closeClient(key);
      break;
    }
  }
  return ret;
}


}
