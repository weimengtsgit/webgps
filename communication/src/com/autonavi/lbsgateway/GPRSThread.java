package com.autonavi.lbsgateway;

import com.autonavi.directl.*;
 
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.parse.*;
import com.autonavi.lbsgateway.bean.TerminalUDPAddress;
import com.autonavi.lbsgateway.gprsserver.udp.GprsSocketChannel;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Date;

import com.autonavi.lbsgateway.poolsave.DataPool;
import java.io.*;

/*
 *�ն˽�����,����GPRS�ն��ϱ���GPS����,�������������ϱ���GPS����
 */
public class GPRSThread implements Runnable {
	public GPRSThread() {
	}

	private byte[] socketBytes;

	private com.autonavi.lbsgateway.gprsserver.udp.GprsSocketChannel gprsSocketChannel;
	
 

	private DataPool dataPool;

	public static boolean isInterval = false;

	public GPRSThread(DataPool dataPool, GprsSocketChannel gprsSocketChannel,
			byte[] socketBytes) {
		this.dataPool = dataPool;
		this.setGprsSocketChannel(gprsSocketChannel);
		this.socketBytes = socketBytes;
	}

	private String trcode = "";;

	// �����ϱ����ݣ��õ��ն�����
	private synchronized String getTerminalCode(String hexContent) {

		trcode = PrepareParse.getInstance().getTerminalCode(hexContent);
		return trcode;
	}

	public synchronized String getTrcode() {

		return this.trcode;
	}

	public synchronized void run() {
		if (!this.gprsSocketChannel.getSocketChannel().isOpen()) {
			GPRSThreadList.getInstance().remove(
					this.gprsSocketChannel.getParseBase().getDeviceSN());
			return;
		}
		String line = null;
		String hexGpsData = null;
		try {
			line = new String(socketBytes, "ISO8859-1");
			hexGpsData = this.bytesToHexString(socketBytes);
			//Log.getInstance().outLog("��������:" + hexGpsData);

			String gpsCode = null;
			if (line.trim().length() == 0) {
				return;
			}
			if (line.startsWith("autonaviSM")) { // ���ն�������ת������������
				line = line.trim();
				String simcard = ParseUtility.getSimcard(line);
				String smContent = ParseUtility.getSMContent(line);
				Log.getInstance().outLog(
						"������������ת������:sim=" + simcard + " content=" + smContent);
				parseSMData(simcard, smContent);
			} else // ����GPRS�ն˵�����
			{
				//if (this.gprsSocketChannel.getParseBase() == null) {
					// gpsCode=getTerminalCode(hexGpsData);
					String terminalDesc = getTerminalCode(hexGpsData);

					Log.getInstance().outLog("Desc:" + terminalDesc);

					if (terminalDesc == null
							|| terminalDesc.trim().length() == 0) {
						Log.getInstance().outLog(
								"����δ֪Э����������:" + hexGpsData + " �رո�����.");
						//����δ֪Э��ʱ����down�� sjw
//						this.gprsSocketChannel.getSocketChannel().socket()
//								.close();
//						this.gprsSocketChannel.getSocketChannel().close();
						// this.gprsSocketChannel.getClientChannel().socket()
						// .close();
						// this.gprsSocketChannel.getClientChannel().close();
						return;
					}
					String[] tmpsp = terminalDesc.split("#");
					gpsCode = tmpsp[0];
					if (!tmpsp[1].equalsIgnoreCase("null"))
						this.gprsSocketChannel.setHexStart(tmpsp[1]);
					if (!tmpsp[2].equalsIgnoreCase("null"))
						this.gprsSocketChannel.setHexEnd(tmpsp[2]);
					if (gpsCode == null || gpsCode.trim().length() == 0) {
						////����δ֪Э��ʱ����down�� sjw
//						this.gprsSocketChannel.getSocketChannel().socket()
//								.close();
//						this.gprsSocketChannel.getSocketChannel().close();
						// this.gprsSocketChannel.getClientChannel().socket()
						// .close();
						// this.gprsSocketChannel.getClientChannel().close();
						Log.getInstance().outLog(
								"����δ֪Э����������:" + hexGpsData + " �رո�����.");
						return;
					} else {
						// �����ն�����,������Ӧ���ն˽�����
						com.autonavi.directl.parse.ParseFactory pf = new ParseFactory();
						com.autonavi.directl.parse.ParseBase pb = pf
								.createParse(gpsCode);
						if (pb == null)
							Log.getInstance().outLog(
									"����������: " + gpsCode + " ʧ��");
						this.gprsSocketChannel.setParseBase(pb);
						this.gprsSocketChannel.setTrcode(gpsCode);
						
 					}
				//}

				Log.getInstance().outLog(
						"���� " + this.gprsSocketChannel.getTrcode() + " �ն�����:"
								+ hexGpsData);
				// ���ն����ݽ����з�,�ֳ��ն˽���Э�����ܹ�ʶ�������
				String[] dataLines = this.gprsSocketChannel
						.getSocketLines(hexGpsData);

				if (dataLines == null)
					return;
				for (int i = 0; i < dataLines.length; i++) {
					String tmpline = dataLines[i];
//					Log.getInstance().outLog(
//							"���� " + this.gprsSocketChannel.getTrcode()
//									+ " �ն�����:" + tmpline);
					// ��������
					this.gprsSocketChannel.getParseBase().parseGPRS(tmpline);
					// �Ѹ�GPS���ӷ���GPS�б�
					if (this.gprsSocketChannel.getParseBase().getDeviceSN() != null) {
//						GPRSThreadList.getInstance().add(
//								this.gprsSocketChannel.getParseBase()
//										.getDeviceSN(), this);
						TerminalUDPAddress udpbean = new TerminalUDPAddress();
						Date date = new Date();
						udpbean.setDeviceSN(this.gprsSocketChannel.getParseBase().getDeviceSN());
						udpbean.setDatagramChannel(this.getGprsSocketChannel().getSocketChannel());
						udpbean.setSocketAddress(this.gprsSocketChannel.getClientAddress());
						udpbean.setDate(date);
						GPRSThreadList.getInstance().add(this.gprsSocketChannel.getParseBase().getDeviceSN(),udpbean);
					} else {

						Log.getInstance().outLog(
								"�ն�����Ϊ:" + this.gprsSocketChannel.getTrcode()
										+ " �Ľ�����,û�н������豸ID,����!!!!!!!!!!!!!!!"
										+ " �ն�����:" + hexGpsData);
					}

//					// ͨ�����������豸ID�ţ��ҵ��ֻ���
//					if (this.gprsSocketChannel.getParseBase().getPhnum() == null
//							|| this.gprsSocketChannel.getParseBase().getPhnum()
//									.trim().length() <= 0) {
//						this.gprsSocketChannel.getParseBase().setPhnum(
//								ParseUtility.getSimBySn(this.gprsSocketChannel
//										.getParseBase().getDeviceSN()));
//					}
					if (this.gprsSocketChannel.getParseBase().getPhnum() == null) {
						Log.getInstance()
								.outLog(
										"�ն�����Ϊ:"
												+ this.gprsSocketChannel
														.getTrcode()
												+ " �豸IDΪ:"
												+ this.gprsSocketChannel
														.getParseBase()
														.getDeviceSN()
												+ " û�а��ֻ���,����!");
					}

					// ����Ҫ�������ݷ������ݳ�
					if (this.gprsSocketChannel.getParseBase().isIsPost()
							&& this.gprsSocketChannel.getParseBase().getPhnum() != null) {
						 
						 
						Log.getInstance().outLog("���ݳ����ݣ�"+this.gprsSocketChannel.getParseBase().getDeviceSN()+","+ tmpline);
						 
						ParseBase pb = this.gprsSocketChannel.getParseBase();
						ArrayList<ParseBase> pblist = pb.getParseList();
						
						if (pblist.size() > 0) {
							for (int k = 0; k < pblist.size(); k++) {
								this.dataPool.add(pblist.get(k)); // ��ӵ����ݳ�
							}
							pb.getParseList().clear();
						} else {
							this.dataPool.add(pb);
						}
						
						this.gprsSocketChannel.getParseBase().setIsPost(false);
					}

					// �ظ��ն�
					String extend1 = this.gprsSocketChannel.getParseBase()
							.getExtend1();
					String extend2 = this.gprsSocketChannel.getParseBase()
							.getExtend2();
					String extend3 = this.gprsSocketChannel.getParseBase()
							.getExtend3();
					byte[] repB = this.gprsSocketChannel.getParseBase()
							.getReplyByte();
					byte[] repB1 = this.gprsSocketChannel.getParseBase()
							.getReplyByte1();
					byte[] repB2 = this.gprsSocketChannel.getParseBase()
							.getReplyByte2();
					if (extend1 != null && extend1.trim().length() > 0) {
						this.sentData(extend1);
						this.gprsSocketChannel.getParseBase().setExtend1(null);
					}
					if (extend2 != null && extend2.trim().length() > 0) {
						this.sentData(extend2);
						this.gprsSocketChannel.getParseBase().setExtend2(null);
						if (this.gprsSocketChannel.getTrcode()
								.equalsIgnoreCase("GP-VIKON-SM"))
							Thread.sleep(10 * 1000);// ά�����ն˱���ͣ��10��
					}
					if (extend3 != null && extend3.trim().length() > 0) {
						this.sentData(extend3);
						this.gprsSocketChannel.getParseBase().setExtend3(null);
					}
					if (repB != null && repB.length > 0) {
						this.sendUDPByteData(repB);
						this.gprsSocketChannel.getParseBase()
								.setReplyByte(null);
					}
					if (repB1 != null && repB1.length > 0) {
						this.sendUDPByteData(repB1);
						this.gprsSocketChannel.getParseBase().setReplyByte1(
								null);
					}
					if (repB2 != null && repB2.length > 0) {
						this.sendUDPByteData(repB2);
						this.gprsSocketChannel.getParseBase().setReplyByte2(
								null);
					}

				}
			}
		} catch (Exception ex) {
			String errinfo = "GPRSThread�������ݳ����쳣:�ն�����Ϊ "
					+ this.gprsSocketChannel.getTrcode() + " ";
			errinfo += "�豸���Ϊ "
					+ this.gprsSocketChannel.getParseBase().getDeviceSN() + " ";
			errinfo += "����Ϊ " + hexGpsData;
			errinfo += ex.getMessage();
			Log.getInstance().outLog(errinfo);
			Log.getInstance().errorLog(errinfo, ex);
		} finally {
			this.socketBytes = null;
		}

	}

	public synchronized void sentData(String data) {
		byte[] bs = null;
		try {
			bs = data.getBytes("ISO8859-1");
		} catch (UnsupportedEncodingException ex) {
		}
		if (bs != null) {
			this.sendUDPByteData(bs);
		}
	}

	// ����byte[]����---TCP
	public synchronized String sendByteArrayData(byte[] data) {
		if (data == null || data.length == 0) {
			return "error";
		}
		String ret = "error";
		try {
			ByteBuffer bf = ByteBuffer.wrap(data);
			this.gprsSocketChannel.getSocketChannel().write(bf);
			ret = "ok";
		} catch (Exception ex) {
			Log.getInstance().outLog(
					"����byte���ݸ��ն� �����쳣:" + ex.getMessage() + " "
							+ bytesToHexString(data));
			Log.getInstance().errorLog(
					"����byte���ݸ��ն� �����쳣:" + " " + bytesToHexString(data), ex);
		}
		return ret;
	}

	// UDP�·�byte[]����
	public synchronized String sendUDPByteData(byte[] data) {
		if (data == null || data.length == 0) {
			return "error";
		}
		String ret = "error";
		try {
			ByteBuffer bf = ByteBuffer.allocate(data.length);
			bf.clear();
			bf.put(data);
			bf.flip();

			SocketAddress clientAddress = this.gprsSocketChannel
					.getClientAddress();// �ͻ��˵�ַ
			DatagramChannel channel = this.gprsSocketChannel.getClientChannel();

			int lent = channel.send(bf, clientAddress); // ���͵��ͻ���
			Log.getInstance().outLog(
					this.gprsSocketChannel.getParseBase().getDeviceSN()
							+ "�·����ĵ�:" + clientAddress + " ����Ϊ��"
							+ new String(bf.array()));
			ret = "ok";
		} catch (Exception ex) {
			Log.getInstance().errorLog(
					"����byte���ݸ��ն� �����쳣:" + " " + bytesToHexString(data), ex);
		}
		return ret;
	}

	/**
	 * �����յ��Ķ�������,Ȼ�����
	 */
	private synchronized void parseSMData(String simCard, String line) {
		String terminalType = null;
		if (line != null) {
			// terminalType = ParseUtility.getGPSType(line);
			byte[] bs = null;
			try {
				bs = line.getBytes("ISO8859-1");
			} catch (UnsupportedEncodingException ex1) {
			}
			terminalType = this.getTerminalCode(this.bytesToHexString(bs));
		}
		if (terminalType != null) {
			ParseFactory pf = new ParseFactory();
			ParseBase pb = null;
			try {
				pb = pf.createParse(terminalType);
			} catch (InstantiationException ex) {
				Log.getInstance().outLog("debug_info_31" + ex.getMessage());
				Log.getInstance().errorLog(ex.getMessage(), ex);
			} catch (IllegalAccessException ex) {
				Log.getInstance().outLog("debug_info_32" + ex.getMessage());
				Log.getInstance().errorLog(ex.getMessage(), ex);
			} catch (ClassNotFoundException ex) {
				Log.getInstance().outLog("debug_info_33" + ex.getMessage());
				Log.getInstance().errorLog(ex.getMessage(), ex);
			}
			if (pb != null) {
				pb.parseSMS(simCard, line);
				this.gprsSocketChannel.setParseBase(pb);
				// �ж��Ƿ��ֵ��Ч
				if (pb.isIsPost()) {
					this.dataPool.add(this.gprsSocketChannel.getParseBase()); // ��ӵ����ݳ�
				}
			}
		}
	}

	// ��byte����ת����16�����ַ�
	private String bytesToHexString(byte[] bs) {
		String s = "";
		for (int i = 0; i < bs.length; i++) {
			String tmp = Integer.toHexString(bs[i] & 0xff);
			if (tmp.length() < 2) {
				tmp = "0" + tmp;
			}
			s = s + tmp;
		}
		return s;
	}

	public synchronized boolean isInterval() {
		return isInterval;
	}

	public synchronized void setInterval(boolean isInterval) {
		this.isInterval = isInterval;
	}

	public synchronized GprsSocketChannel getGprsSocketChannel() {
		return gprsSocketChannel;
	}

	public synchronized void setGprsSocketChannel(
			GprsSocketChannel gprsSocketChannel) {
		this.gprsSocketChannel = gprsSocketChannel;
	}

}
