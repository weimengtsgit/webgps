package com.autonavi.lbsgateway.gprsserver.jhs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.autonavi.directl.Log;

/**
 * ����,��������ĳSOCKET����Ŀͻ���,֧���Զ�����
 * 
 * @author jiang
 * 
 */
public abstract class Clientable extends Thread {
	private String host = "";

	private int port = 0;

	private String clientName = "";

	private Socket socket = null;

	private OutputStream oos = null;

	private BufferedInputStream is;

	private PrintWriter os = null;

	private boolean listening;

	public Clientable(String clientName, String host, int port) {
		this.clientName = clientName;
		this.host = host;
		this.port = port;
		listening = true;
		try {
			socket = new Socket(host, port);
			is = new BufferedInputStream(this.socket.getInputStream());
			oos = this.socket.getOutputStream();
			os = new PrintWriter(oos);
			try {
				sleep(500);
			} catch (InterruptedException ex1) {
				Log.getInstance().outJHSLoger(
						"�ͻ���:" + this.clientName + "û��������:" + this.host + ":"
								+ this.port);
				Log.getInstance().errorLog(ex1.getMessage(), ex1);
			}
		} catch (IOException ex) {
			Log.getInstance().outJHSLoger(
					"�ͻ���:" + this.clientName + "û��������:" + this.host + ":"
							+ this.port);
			Log.getInstance().errorLog(ex.getMessage(), ex);
		}
	}

	public abstract void process(String xml);

	public void run() {
		int c = 0;
		while (listening) {
			while (socket == null) {
				Log.getInstance().outJHSLoger(
						"�ͻ���:" + this.clientName + "û��������:" + this.host + ":"
								+ this.port + ", �����ѶϿ�,���ڳ�����������...");
				try {
					socket = new Socket(host, port);
					if (socket != null) {
						is = new BufferedInputStream(this.socket
								.getInputStream());
						oos = this.socket.getOutputStream();
						os = new PrintWriter(oos);
						try {
							Thread.sleep(500);
						} catch (InterruptedException ex2) {
						}
						Log.getInstance().outJHSLoger(
								"�ͻ���:" + this.clientName + " ��������:" + this.host
										+ ":" + this.port + " �ɹ�!");
						break;
					}
				} catch (UnknownHostException ex1) {
					Log.getInstance().outJHSLoger(
							"�ͻ���:" + this.clientName + " ��������:" + this.host
									+ ":" + this.port + " ʧ��!ԭ��:"
									+ ex1.getMessage());
					Log.getInstance().errorLog(ex1.getMessage(), ex1);
					try {
						this.sleep(1000 * 5);
					} catch (InterruptedException ex2) {
					}
					continue;
				} catch (IOException ex1) {
					Log.getInstance().outJHSLoger(
							"�ͻ���:" + this.clientName + " ��������:" + this.host
									+ ":" + this.port + " ʧ��!ԭ��:"
									+ ex1.getMessage());
					Log.getInstance().errorLog(ex1.getMessage(), ex1);

					try {
						this.sleep(1000 * 5);
					} catch (InterruptedException ex2) {
					}
					continue;
				} catch (Exception e) {
					Log.getInstance().outJHSLoger(
							"�ͻ���:" + this.clientName + " ��������:" + this.host
									+ ":" + this.port + " ʧ��!ԭ��:"
									+ e.getMessage());
					Log.getInstance().errorLog(e.getMessage(), e);
					try {
						this.sleep(1000 * 5);
					} catch (InterruptedException ex2) {
					}
					continue;

				}
				try {
					this.sleep(1000 * 5);
				} catch (InterruptedException ex2) {
				}
			}
			try {
				Log.getInstance().outJHSLoger(
						this.clientName + " ���ڼ���" + this.host + ":" + this.port
								+ "......");
				byte[] b = new byte[5 * 1024];
				c = is.read(b, 0, b.length);
				if (c != -1) {
					try {
						String requestXml = new String(b).trim();
						Log.getInstance().outJHSLoger(
								"����:" + this.clientName + ":" + requestXml);
						this.process(requestXml);
					} catch (Exception e) {
						Log.getInstance().outJHSLoger(
								"�ͻ���:" + this.clientName + " ��ȡ���߽�������ʧ��,ԭ��:"
										+ e.getMessage());
					}
				} else {
					closeSocket();
					socket = null;
				}
			} catch (IOException ex) {
				closeSocket();
				socket = null;
			} catch (Exception e) {
				closeSocket();
				socket = null;
			}
		}
	}

	public String sentDataString(String data) {
		String ret = "error";
		try {
//			Log.getInstance().outJHSLoger(
//					this.clientName + "�·�����:" + new String(data));
			this.os.write(data);
			this.os.flush();
			ret = "ok";
		} catch (Exception ex) {
			ret = "error";
			Log.getInstance().outJHSLoger(
					"�ͻ���:" + this.clientName + " �·���Ϣ:" + data + "ʧ��,ԭ��:"
							+ ex.getMessage());
		}
		return ret;
	}

	public String sentDataBytes(byte[] data) {
		String ret = "error";
		try {
			this.oos.write(data);
			this.oos.flush();
			Log.getInstance().outJHSLoger(
					this.clientName + "�·�����:" + new String(data));
			ret = "ok";
		} catch (Exception ex) {
			ret = "error";
			Log.getInstance().outJHSLoger(
					"�ͻ���:" + this.clientName + " �·���Ϣ:" + new String(data)
							+ "ʧ��,ԭ��:" + ex.getMessage());
		}
		return ret;
	}

	public void closeSocket() {
		try {
			if (this.is != null) {
				this.is.close();
				this.is = null;
			}
			if (this.os != null) {
				this.os.close();
				this.os = null;
			}
			if (this.oos != null) {
				this.oos.close();
				this.oos = null;
			}
			if (this.socket != null) {
				this.socket.close();
				this.socket = null;
			}
			Log.getInstance().outJHSLoger("�ر� :" + this.clientName + " ���ӳɹ�!");
		} catch (Exception ex) {
			Log.getInstance().outJHSLoger(
					"�ر� :" + this.clientName + " ����ʧ��!" + ex.getMessage());
		}
		this.socket = null;
	}

}