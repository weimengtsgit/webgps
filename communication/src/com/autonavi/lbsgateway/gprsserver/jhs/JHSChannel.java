package com.autonavi.lbsgateway.gprsserver.jhs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.autonavi.directl.Log;

 

public class JHSChannel {
	private SocketChannel socketChannel;
	public JHSChannel(SocketChannel socketChannel) {
		this.setSocketChannel(socketChannel);
	}

	public synchronized SocketChannel getSocketChannel() {
		return socketChannel;
	}
	
	private synchronized void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	  // ����byte[]����
	  public synchronized String sendByteArrayData(byte[] data) {
	    if (data == null || data.length == 0) {
	      return "error";
	    }
	    String ret = "error";
	    try {
	      ByteBuffer bf = ByteBuffer.wrap(data);
	      this.getSocketChannel().write(bf);
	      ret = "ok";
	    }
	    catch (Exception ex) {
	      Log.getInstance().outJHSLoger("����byte���ݸ��ն� �����쳣:" + ex.getMessage());
	    }
	    return ret;
	  }
	  
	// ��Socket�ж���������
	public synchronized byte[] readSocketBytes(SelectionKey key) {
		SocketChannel socket = this.getSocketChannel();		
		
		byte[] ret = null;
		boolean bRead = true;
		while (bRead) {
			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(256);
				byteBuffer.clear();
				int nbytes = socket.read(byteBuffer);
				if (nbytes == -1) {
					key.cancel();
					socket.socket().close();
					socket.close();
					bRead = false;
					Log.getInstance().outJHSLoger("��ȡ�ն�����ʧ��,�ն� �ر�����,read=-1");
					Log.getInstance().errorLog("��ȡ�ն�����ʧ��,�ն� �ر�����,read=-1", null);
					return null;
				}
				if (nbytes > 0) {
					byteBuffer.flip();
					byte[] tmp = new byte[byteBuffer.limit()];
					byteBuffer.get(tmp, 0, byteBuffer.limit());
					if (ret == null) {
						ret = new byte[tmp.length];
						System.arraycopy(tmp, 0, ret, 0, tmp.length);
					} else {
						byte[] tmpret = new byte[ret.length];
						System.arraycopy(ret, 0, tmpret, 0, ret.length);
						ret = new byte[ret.length + tmp.length];
						System.arraycopy(tmpret, 0, ret, 0, tmpret.length);
						System.arraycopy(tmp, 0, ret, tmpret.length,tmp.length);
					}
				}
				if (nbytes < 256) {
					return ret;
				}
			} catch (Exception e) {
				bRead = false;
				Log.getInstance().outJHSLoger("��ȡ�ն�����ʧ��:" + e.getMessage());
				key.cancel();
				try {
					socket.socket().close();
					socket.close();
				} catch (IOException ex) {
				}
				break;
			}
		}
		return ret;
	}
}
