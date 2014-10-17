package com.autonavi.lbsgateway.gprsserver.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.nio.CharBuffer;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.parse.ParseBase;
import com.autonavi.lbsgateway.GPRSThreadList;

/**
 * GprsSocketChannel:���ӵ����ص�GPRS�ն��� ע��������ͬʱ������߳�ʹ�� ���Ը������еķ����������.
 */
public class GprsSocketChannel {
	private DatagramChannel socketChannel;

	private DatagramChannel clientChannel;

	private SocketAddress clientAddress; // ���ͷ���ַ

	private com.autonavi.directl.parse.ParseBase parseBase;// �ն˽�����

	private String trcode;// �ն�����

	private String hexStart;// ��ͷ��ʶ����ʶͷ��ʶ����,�����ж����ʶ��,�м��ö��Ÿ�����

	private String hexEnd;// ��β��ʶ,��ʶβ��ʶ����,Ŀǰֻ֧��һ����ʶ��

	public GprsSocketChannel(DatagramChannel socketChannel) {
		// Log
		// .getInstance()
		// .outLog(
		// "------------------------------------------����һ��
		// GprsSocketChannel����");
		this.setSocketChannel(socketChannel);
	}

	public synchronized DatagramChannel getSocketChannel() {
		return socketChannel;
	}

	private synchronized void setSocketChannel(DatagramChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public synchronized String getTrcode() {
		return trcode;
	}

	public synchronized void setTrcode(String trcode) {
		this.trcode = trcode;
	}

	public synchronized String getHexStart() {
		return hexStart;
	}

	public synchronized void setHexStart(String hexstart) {
		this.hexStart = hexstart;
	}

	public synchronized String getHexEnd() {
		return hexEnd;
	}

	public synchronized void setHexEnd(String hexend) {
		this.hexEnd = hexend;
	}

	public synchronized ParseBase getParseBase() {
		return parseBase;
	}

	public synchronized void setParseBase(ParseBase parseBase) {
		this.parseBase = parseBase;
	}

	// ��Socket�ж���������
	public synchronized byte[] readSocketBytes(SelectionKey key) {

		DatagramChannel schanel = this.socketChannel;

		byte[] ret = null;
		boolean bRead = true;
		String validHex = "";// ��������Ч����

		try {

			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			byteBuffer.clear();
			clientAddress = (InetSocketAddress) schanel.receive(byteBuffer);
			int leng = byteBuffer.position(); // ��Ч���ݳ���
			byteBuffer.flip();

			byte[] bcont = byteBuffer.array(); // ��������
			byte[] validData = new byte[leng]; // ��Ч����
			System.arraycopy(bcont, 0, validData, 0, leng);

			this.setClientChannel(schanel);
			this.setClientAddress(clientAddress);

			if (validData.length > 0) {
				String hex = Tools.bytesToHexString(validData);// ��byteBufferת����16����
				Log.getInstance()
						.outLog(
								"UDP�����յ�ԭʼ�����ֽ���=" + hex.length() / 2
										+ "ԭʼ���ݱ����ݣ�" + hex);
			}
			return validData;// byteBuffer.array();//
								// Tools.fromHexString(validHex);

		} catch (Exception e) {
			bRead = false;
			Log.getInstance().errorLog("��ȡ�ն�����ʧ��:" + e.getMessage(), e);
			key.cancel();

			try {
				schanel.socket().close();
				schanel.close();

			} catch (IOException ex) {
			}
			if (this.getParseBase() != null) {
				GPRSThreadList.getInstance().removeUDP(
						this.getParseBase().getDeviceSN());
			}

		}

		return null;
	}

	// �ѽ��յ������ݰ���Э��ı�ʶͷβ�������з�,�ֳɶ���.
	// ע�⣺��ֹ��ʶβ�ж����ʶ����!
	public synchronized String[] getSocketLines(String hex) {
		String[] ret = null;
		try {
			String tmpstart = this.getHexStart();
			String tmpend = this.getHexEnd();
			if (tmpstart != null && tmpstart.split(",").length > 1) {
				String[] tmplines = tmpstart.split(",");
				for (int i = 0; i < tmplines.length; i++) {
					String tmpline = tmplines[i];

					if (tmpline == null || tmpline.trim().length() == 0)
						continue;
					ret = getsplitLines(hex, tmpline, tmpend);
					// Log.getInstance().outLog("�з�ָ�"+ret+"tmpline:"+tmpline);
					if (ret != null)
						break;
				}
			} else {
				ret = getsplitLines(hex, tmpstart, tmpend);
			}
			// Log.getInstance().outLog("�ָ�ɶ������ݳɹ�!һ��:" + ret.length + "������.");
		} catch (Exception ex) {
			String errinfo = "�ָ����ݳ��ִ���:" + ex.getMessage();
			errinfo += "����:" + hex + "��ʶͷ:" + this.getHexStart() + "��ʶβ:"
					+ this.getHexEnd();
			Log.getInstance().outLog(errinfo);
			Log.getInstance().errorLog(errinfo, ex);
			String[] shytdata = new String[1];
			shytdata[0] = hex;
			return shytdata;
		}
		return ret;
	}

	// �зֺ���
	private synchronized String[] getsplitLines(String hex, String hexstart,
			String hexend) {
		if (hex == null || (hex.length() % 2 != 0))
			return null;
		if (hexstart == null && hexend == null)
			return null;
		if (hexstart.trim().length() <= 0 && hexend.trim().length() <= 0)
			return null;

		String cData = addSpaceToHexstring(hex);
		String cStartData = addSpaceToHexstring(hexstart);
		String cEndData = addSpaceToHexstring(hexend);
		String[] rettmp = null;
		String strTmp = "";
		int f = -1;
		if (hexstart != null && hexend != null && hexstart.trim().length() > 0
				&& hexend.trim().length() > 0) {
			// ��ͷ��β,ɾ����ͷǰ�Ͱ�β�������
			int m = cData.indexOf(cStartData);
			int n = cData.lastIndexOf(cEndData);
			if (m == -1 || n == -1)
				return null;

			cData = cData.substring(m, cData.lastIndexOf(cEndData)
					+ cEndData.length());
			//4210....0d0a4210....0d0a
			if (hexstart.equals("3032") && hexend.equals("23")) {
				rettmp = cData.split(cEndData);
			} else {
				int tempS  = cData.indexOf(cStartData);
				int tempE =  cData.indexOf(cEndData);
				
				rettmp = cData.split(cStartData);
			}

			f = 0;
		} else if (hexstart != null && hexstart.trim().length() > 0) {
			// ��ͷ,ɾ����ͷǰ������
			int m = cData.indexOf(cStartData);
			if (m == -1)
				return null;
			cData = cData.substring(cData.indexOf(cStartData), cData.length());
			rettmp = cData.split(cStartData);
			f = 1;
		} else if (hexend != null && hexend.trim().length() > 0) {
			// ��β,ɾ����β�������

			int n = cData.lastIndexOf(cEndData);
			if (n == -1)
				return null;

			cData = cData.substring(0, cData.lastIndexOf(cEndData)
					+ cEndData.length());
			rettmp = cData.split(cEndData);
			f = 2;
		}

		for (int i = 0; i < rettmp.length; i++) {
			String tmp = rettmp[i];

			if (tmp == null || tmp.trim().length() == 0)
				continue;
			if (hexstart.equals("3032") && hexend.equals("23") && f == 0) {
				tmp = tmp + cEndData;
			} else if (f == 0 || f == 1) {
				tmp = cStartData + tmp;
			}

			if (f == 2)
				tmp = tmp + cEndData;
			if (f == 0) {
				if (tmp.startsWith(cStartData) && tmp.endsWith(cEndData)) {
					strTmp += tmp + ",";

				}
			} else if (f == 1) {
				if (tmp.startsWith(cStartData)) {
					strTmp += tmp + ",";
				}
			} else if (f == 2) {
				if (tmp.endsWith(cEndData)) {
					strTmp += tmp + ",";
				}
			}
		}

		if (strTmp.trim().length() <= 0) {
			return null;
		}
		strTmp = strTmp.substring(0, strTmp.length() - 1);
		strTmp = strTmp.replaceAll(" ", "");
		return strTmp.split(",");
	}

	// ��ʮ�����ƶȵ��ַ���ÿ���ֽ�֮����' ' ������
	private synchronized String addSpaceToHexstring(String hex) {
		if (hex == null || hex.trim().length() == 0 || (hex.length() % 2 != 0)) {
			return null;
		}
		char c = ' ';
		char[] cs = hex.toCharArray();
		CharBuffer cb = java.nio.CharBuffer.wrap(cs);
		int size = cs.length * 2;
		CharBuffer result = CharBuffer.allocate(size);
		for (int i = 0; i < hex.length(); i++) {
			result.put(cb.get(i));
			if (i > 0 && (i % 2 == 1)) {
				result.put(c);
			}
		}
		result.flip();
		return result.toString();
	}

	public static void main(String[] args) {
		GprsSocketChannel gs = new GprsSocketChannel(null);

		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		byteBuffer.put("hello".getBytes());
		byteBuffer.flip();
		System.out.println(byteBuffer.position());

	}

	public synchronized SocketAddress getClientAddress() {
		return clientAddress;
	}

	public synchronized void setClientAddress(SocketAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	public synchronized DatagramChannel getClientChannel() {
		return clientChannel;
	}

	public synchronized void setClientChannel(DatagramChannel clientChannel) {
		this.clientChannel = clientChannel;
	}

}
