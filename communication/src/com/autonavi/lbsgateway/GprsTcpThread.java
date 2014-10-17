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
 *终端解析类,解析GPRS终端上报的GPS数据,解析短信网关上报的GPS数据
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

	// 根据上报数据，得到终端类型
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
			Log.getInstance().outLog("接收数据:" + hexGpsData);

			String gpsCode = null;
			if (line.trim().length() == 0) {
				this.closeSocket();
				return;
			}
			if (line.startsWith("autonaviSM")) { // 接收短信网关转发过来的数据
				line = line.trim();
				String simcard = ParseUtility.getSimcard(line);
				String smContent = ParseUtility.getSMContent(line);
				Log.getInstance().outLog(
						"解析短信网关转换数据:sim=" + simcard + " content=" + smContent);
				parseSMData(simcard, smContent);
			} else // 接收GPRS终端的数据
			{
				if (this.gprsSocketTcpChannel.getParseBase() == null) {
					// gpsCode=getTerminalCode(hexGpsData);
					String terminalDesc = getTerminalCode(hexGpsData);

					Log.getInstance().outLog("Desc:" + terminalDesc);

					if (terminalDesc == null
							|| terminalDesc.trim().length() == 0) {
						Log.getInstance().outLog(
								"接收未知协议类型数据:" + hexGpsData + " 关闭该连接.");
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
								"接收未知协议类型数据:" + hexGpsData + " 关闭该连接.");
						return;
					} else {
						// 根据终端类型,创建相应的终端解析类
						com.autonavi.directl.parse.ParseFactory pf = new ParseFactory();
						com.autonavi.directl.parse.ParseBase pb = pf
								.createParse(gpsCode);
						if (pb == null)
							Log.getInstance().outLog(
									"创建解析类: " + gpsCode + " 失败");
						Socket socket = this.gprsSocketTcpChannel
								.getSocketChannel().socket();
						pb.setSocket(socket);
						this.gprsSocketTcpChannel.setParseBase(pb);
						this.gprsSocketTcpChannel.setTrcode(gpsCode);
					}
				}

				// Log.getInstance().outLog("接收 " +
				// this.gprsSocketTcpChannel.getTrcode() + " 终端数据:" +
				// hexGpsData);
				// 把终端数据进行切分,分成终端解析协议类能够识别的数据
				String[] dataLines = this.gprsSocketTcpChannel
						.getSocketLines(hexGpsData);

				if (dataLines == null)
					return;
				for (int i = 0; i < dataLines.length; i++) {
					String tmpline = dataLines[i];
					Log.getInstance().outLog(
							"解析 " + this.gprsSocketTcpChannel.getTrcode()
									+ " 终端数据:" + tmpline);
					// 解析数据
					this.gprsSocketTcpChannel.getParseBase().parseGPRS(tmpline);

					if (this.gprsSocketTcpChannel.getParseBase().getPhnum() == null) {
						this.closeSocket();
						Log.getInstance().outLog(
								"终端类型为:"
										+ this.gprsSocketTcpChannel.getTrcode()
										+ " 设备ID为:"
										+ this.gprsSocketTcpChannel
												.getParseBase().getDeviceSN()
										+ " 没有绑定手机号,错误!");
						return;
					}

					// 把需要入库的数据放入数据池
					if (this.gprsSocketTcpChannel.getParseBase().isIsPost()
							&& this.gprsSocketTcpChannel.getParseBase()
									.getPhnum() != null) {
						ParseBase pb = this.gprsSocketTcpChannel.getParseBase();
						ArrayList<ParseBase> pblist = pb.getParseList();
						
						if (pblist.size() > 0) {
							for (int k = 0; k < pblist.size(); k++) {
								this.dataPool.add(pblist.get(k)); // 添加到数据池
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
								"数据池数据："
										+ this.gprsSocketTcpChannel
												.getParseBase().getDeviceSN()
										+ "," + tmpline);

						this.gprsSocketTcpChannel.getParseBase().setIsPost(
								false);
					}

					// 回复终端
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
							Thread.sleep(10 * 1000);// 维康的终端必须停留10秒
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

					// 把改GPS连接放入GPS列表
					if (this.gprsSocketTcpChannel.getParseBase().getDeviceSN() != null) {
						GPRSThreadList.getInstance().add(
								this.gprsSocketTcpChannel.getParseBase()
										.getDeviceSN(), this);
					} else {
						Log.getInstance().outLog(
								"终端类型为:"
										+ this.gprsSocketTcpChannel.getTrcode()
										+ " 的解析类,没有解析到设备ID,错误!!!!!!!!!!!!!!!"
										+ " 终端数据:" + hexGpsData);
					}

				}
			}
		} catch (Exception ex) {
			// String errinfo="GPRSThread解析数据出现异常:终端类型为
			// "+this.gprsSocketTcpChannel.getTrcode()+" ";
			// errinfo+="设备编号为 "+
			// this.gprsSocketTcpChannel.getParseBase().getDeviceSN()+" ";
			// errinfo+="数据为 "+hexGpsData;
			// errinfo+=ex.getMessage();
			// Log.getInstance().outLog(errinfo );
			this.closeSocket();

			Log.getInstance().errorLog("解析异常", ex);
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

	// 发送byte[]数据
	public synchronized String sendByteArrayData(byte[] data) {
		if (data == null || data.length == 0) {
			return "error";
		}
		String ret = "error";
		try {
			ByteBuffer bf = ByteBuffer.wrap(data);

			this.gprsSocketTcpChannel.getSocketChannel().write(bf);
			Log.getInstance().outLog("回复终端指令："+Tools.bytesToHexString(data));
			ret = "ok";
		} catch (Exception ex) {
			Log.getInstance().outLog(
					"发送byte数据给终端 出现异常:" + ex.getMessage() + " "
							+ bytesToHexString(data));
			Log.getInstance().errorLog(
					"发送byte数据给终端 出现异常:" + " " + bytesToHexString(data), ex);
		}
		return ret;
	}

	/**
	 * 解析收到的短信数据,然后入库
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
				// 判断是否充值有效
				if (pb.isIsPost()) {
					this.dataPool.add(this.gprsSocketTcpChannel.getParseBase()); // 添加到数据池
				}
			}
		}
	}

	// 把byte数组转换成16进制字符
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
