package com.autonavi.lbsgateway.clientserver;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import com.autonavi.lbsgateway.LoginUser;
import com.autonavi.lbsgateway.ClientThreadList;
 
import com.autonavi.directl.Log;
/**
 * ClientSocketChannel:���ӵ����صĿͻ�����
 * ע��������ͬʱ������߳�ʹ��,���Ը������еķ����������.
 */
public class ClientSocketChannel {
  private SocketChannel socketChannel;
  private LoginUser loginUser;//���������û�
  private DataSendTask dataSendTask;//�·����ݵ��ͻ����߳�
  //private mapabc.locate.Api CellLocator = new mapabc.locate.Api();//CellID ��λ�ӿ�
  //private CellIDMultiLocator cellidMultiLocator;//cellid ������λ�ӿ�

  public  ClientSocketChannel(SocketChannel socketChannel) {
    this.setSocketChannel(socketChannel);
    this.dataSendTask=new DataSendTask(socketChannel);
    this.dataSendTask.start();
  }

  /**
   * CELLID������λ
   */
//  public synchronized void startCellIDMultiLocator(String simcards, String frec) {
//    sotpCellIDMultiLocator(); //�ر�֮ǰ��������λ
//    cellidMultiLocator = new CellIDMultiLocator(this.getLoginUser(), simcards, frec);
//    cellidMultiLocator.start();
//  }

  /**
   * ֹͣCELLID������λ
   */
//  public synchronized void sotpCellIDMultiLocator() {
//    if (cellidMultiLocator != null) {
//      cellidMultiLocator.setIsLive(false);
//    }
//    cellidMultiLocator = null;
//  }
  /**
   * �ر�SocketChannel ,����ȡ������ص�����
   * @param key SelectionKey
   */
  public synchronized void closeClient(SelectionKey key) {
  try {
    if(key!=null) key.cancel();
    this.getSocketChannel().socket().close();
    this.getSocketChannel().close();
  }
  catch (Exception ex) {
    Log.getInstance().outLog("�ر� ClientTread ʧ��." + ex.getMessage());
    Log.getInstance().errorLog("�ر� ClientTread ʧ��.", ex);
  }finally{
    if (this.getLoginUser()!=null) {
      ClientThreadList.getInstance().removeClient(this.getLoginUser());
    }
    if(this.getDataSendTask()!=null){
      this.getDataSendTask().setThreadFlag(false);
    }
    //this.sotpCellIDMultiLocator(); //�ر�������λ
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
   * ��SocketChannel�ж�ȡ����
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
