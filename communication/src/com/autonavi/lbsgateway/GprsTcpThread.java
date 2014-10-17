package com.autonavi.lbsgateway;

import com.autonavi.directl.*;
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.parse.*;
import com.autonavi.lbsgateway.gprsserver.udp.GprsSocketChannel;
import com.autonavi.lbsgateway.gprsserver.tcp.GprsSocketTcpChannel;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;

import com.autonavi.lbsgateway.poolsave.DataPool;
import java.io.*;

/*
 *�ն˽�����,����GPRS�ն��ϱ���GPS����,�������������ϱ���GPS����
 */
public class GprsTcpThread implements Runnable {
	public GprsTcpThread() {
	}

	private byte[] socketBytes;
	private GprsSocketTcpChannel gprsSocketTcpChannel;
	private DataPool dataPool;
	public static boolean isInterval = false;
	private Date date;

	public GprsTcpThread(DataPool dataPool,
			GprsSocketTcpChannel gprsSocketChannel, byte[] socketBytes) {
		this.dataPool = dataPool;
		this.gprsSocketTcpChannel = gprsSocketChannel;
		this.socketBytes = socketBytes;
		this.date = new Date();
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

	public void run() {
		if (!this.gprsSocketTcpChannel.getSocketChannel().isOpen()) {
			this.closeSocket();
			return;
		}
		String line = null;
		String hexGpsData = null;
		try {
			line = new String(socketBytes, "ISO8859-1");
			hexGpsData = this.bytesToHexString(socketBytes);
			Log.getInstance().outLog("��������:" + hexGpsData);

			String gpsCode = null;
			if (line.trim().length() == 0) {
				this.closeSocket();
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
				if (this.gprsSocketTcpChannel.getParseBase() == null) {
					// gpsCode=getTerminalCode(hexGpsData);
					String terminalDesc = getTerminalCode(hexGpsData);

					Log.getInstance().outLog("Desc:" + terminalDesc);

					if (terminalDesc == null
							|| terminalDesc.trim().length() == 0) {
						Log.getInstance().outLog(
								"����δ֪Э����������:" + hexGpsData + " �رո�����.");
						this.closeSocket();
						return;
					}
					String[] tmpsp = terminalDesc.split("#");
					gpsCode = tmpsp[0];
					if (!tmpsp[1].equalsIgnoreCase("null"))
						this.gprsSocketTcpChannel.setHexStart(tmpsp[1]);
					if (!tmpsp[2].equalsIgnoreCase("null"))
						this.gprsSocketTcpChannel.setHexEnd(tmpsp[2]);
					if (gpsCode == null || gpsCode.trim().length() == 0) {
						this.closeSocket();
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
						Socket socket = this.gprsSocketTcpChannel
								.getSocketChannel().socket();
						pb.setSocket(socket);
						this.gprsSocketTcpChannel.setParseBase(pb);
						this.gprsSocketTcpChannel.setTrcode(gpsCode);
					}
				}

				// Log.getInstance().outLog("���� " +
				// this.gprsSocketTcpChannel.getTrcode() + " �ն�����:" +
				// hexGpsData);
				// ���ն����ݽ����з�,�ֳ��ն˽���Э�����ܹ�ʶ�������
				String[] dataLines = this.gprsSocketTcpChannel
						.getSocketLines(hexGpsData);

				if (dataLines == null)
					return;
				for (int i = 0; i < dataLines.length; i++) {
					String tmpline = dataLines[i];
					Log.getInstance().outLog(
							"���� " + this.gprsSocketTcpChannel.getTrcode()
									+ " �ն�����:" + tmpline);
					// ��������
					this.gprsSocketTcpChannel.getParseBase().parseGPRS(tmpline);

					if (this.gprsSocketTcpChannel.getParseBase().getPhnum() == null) {
						this.closeSocket();
						Log.getInstance().outLog(
								"�ն�����Ϊ:"
										+ this.gprsSocketTcpChannel.getTrcode()
										+ " �豸IDΪ:"
										+ this.gprsSocketTcpChannel
												.getParseBase().getDeviceSN()
										+ " û�а��ֻ���,����!");
						return;
					}

					// ����Ҫ�������ݷ������ݳ�
					if (this.gprsSocketTcpChannel.getParseBase().isIsPost()
							&& this.gprsSocketTcpChannel.getParseBase()
									.getPhnum() != null) {
						ParseBase pb = this.gprsSocketTcpChannel.getParseBase();
						ArrayList<ParseBase> pblist = pb.getParseList();
						
						if (pblist.size() > 0) {
							for (int k = 0; k < pblist.size(); k++) {
								this.dataPool.add(pblist.get(k)); // ��ӵ����ݳ�
							}
							pb.getParseList().clear();
						} else {
							this.dataPool.add(pb);
						}

						String tempLog = new String(Tools
								.fromHexString(tmpline));
						String tempDeviceID = this.gprsSocketTcpChannel
								.getParseBase().getDeviceSN();

						Log.getInstance().outLog(
								"���ݳ����ݣ�"
										+ this.gprsSocketTcpChannel
												.getParseBase().getDeviceSN()
										+ "," + tmpline);

						this.gprsSocketTcpChannel.getParseBase().setIsPost(
								false);
					}

					// �ظ��ն�
					String extend1 = this.gprsSocketTcpChannel.getParseBase()
							.getExtend1();
					String extend2 = this.gprsSocketTcpChannel.getParseBase()
							.getExtend2();
					String extend3 = this.gprsSocketTcpChannel.getParseBase()
							.getExtend3();
					byte[] repB = this.gprsSocketTcpChannel.getParseBase()
							.getReplyByte();
					byte[] repB1 = this.gprsSocketTcpChannel.getParseBase()
							.getReplyByte1();
					byte[] repB2 = this.gprsSocketTcpChannel.getParseBase()
							.getReplyByte2();
					if (extend1 != null && extend1.trim().length() > 0) {
						this.sentData(extend1);
						this.gprsSocketTcpChannel.getParseBase().setExtend1(
								null);
					}
					if (extend2 != null && extend2.trim().length() > 0) {
						this.sentData(extend2);
						this.gprsSocketTcpChannel.getParseBase().setExtend2(
								null);
						if (this.gprsSocketTcpChannel.getTrcode()
								.equalsIgnoreCase("GP-VIKON-SM"))
							Thread.sleep(10 * 1000);// ά�����ն˱���ͣ��10��
					}
					if (extend3 != null && extend3.trim().length() > 0) {
						this.sentData(extend3);
						this.gprsSocketTcpChannel.getParseBase().setExtend3(
								null);
					}
					if (repB != null && repB.length > 0) {
						this.sendByteArrayData(repB);
						this.gprsSocketTcpChannel.getParseBase().setReplyByte(
								null);
					}
					if (repB1 != null && repB1.length > 0) {
						this.sendByteArrayData(repB1);
						this.gprsSocketTcpChannel.getParseBase().setReplyByte1(
								null);
					}
					if (repB2 != null && repB2.length > 0) {
						this.sendByteArrayData(repB2);
						this.gprsSocketTcpChannel.getParseBase().setReplyByte1(
								null);
					}

					// �Ѹ�GPS���ӷ���GPS�б�
					if (this.gprsSocketTcpChannel.getParseBase().getDeviceSN() != null) {
						GPRSThreadList.getInstance().add(
								this.gprsSocketTcpChannel.getParseBase()
										.getDeviceSN(), this);
					} else {
						Log.getInstance().outLog(
								"�ն�����Ϊ:"
										+ this.gprsSocketTcpChannel.getTrcode()
										+ " �Ľ�����,û�н������豸ID,����!!!!!!!!!!!!!!!"
										+ " �ն�����:" + hexGpsData);
					}

				}
			}
		} catch (Exception ex) {
			// String errinfo="GPRSThread�������ݳ����쳣:�ն�����Ϊ
			// "+this.gprsSocketTcpChannel.getTrcode()+" ";
			// errinfo+="�豸���Ϊ "+
			// this.gprsSocketTcpChannel.getParseBase().getDeviceSN()+" ";
			// errinfo+="����Ϊ "+hexGpsData;
			// errinfo+=ex.getMessage();
			// Log.getInstance().outLog(errinfo );
			this.closeSocket();

			Log.getInstance().errorLog("�����쳣", ex);
		} finally {
			this.socketBytes = null;
		}

	}

	private synchronized void closeSocket() {
		try {
			GprsSocketTcpChannel gsc = this.getGprsSocketTcpChannel();
			if (null != gsc) {
				SocketChannel sc = gsc.getSocketChannel();
				if (null != sc) {
					Socket s = sc.socket();
					if (null != s) {
						s.close();
						Log.getInstance().outLog(
								"close socket:" + s.getRemoteSocketAddress());
					}
				}
				sc.close();
			}
		} catch (IOException e) {
			try {
				this.getGprsSocketTcpChannel().getSocketChannel().close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.getInstance().errorLog("close channel exception", e);
			e.printStackTrace();
		}
	}

	public synchronized void sentData(String data) {
		byte[] bs = null;
		try {
			bs = data.getBytes("ISO8859-1");
		} catch (UnsupportedEncodingException ex) {
		}
		if (bs != null) {
			this.sendByteArrayData(bs);
		}
	}

	// ����byte[]����
	public synchronized String sendByteArrayData(byte[] data) {
		if (data == null || data.length == 0) {
			return "error";
		}
		String ret = "error";
		try {
			ByteBuffer bf = ByteBuffer.wrap(data);

			this.gprsSocketTcpChannel.getSocketChannel().write(bf);
			Log.getInstance().outLog("�ظ��ն�ָ�"+Tools.bytesToHexString(data));
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
				this.gprsSocketTcpChannel.setParseBase(pb);
				// �ж��Ƿ��ֵ��Ч
				if (pb.isIsPost()) {
					this.dataPool.add(this.gprsSocketTcpChannel.getParseBase()); // ��ӵ����ݳ�
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

	public Date getDate() {
		return date;
	}

	public synchronized void setDate(Date date) {
		this.date = date;
	}

	public synchronized GprsSocketTcpChannel getGprsSocketTcpChannel() {
		return gprsSocketTcpChannel;
	}

	public synchronized void setGprsSocketTcpChannel(
			GprsSocketTcpChannel gprsSocketTcpChannel) {
		this.gprsSocketTcpChannel = gprsSocketTcpChannel;
	}

}
