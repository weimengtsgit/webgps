package com.autonavi.lbsgateway;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;

import com.autonavi.lbsgateway.bean.TerminalUDPAddress;
import com.autonavi.lbsgateway.gprsserver.tcp.GprsSocketTcpChannel;

/**
 * 
 * ���������Ѿ�����GPRS���ӵ��ն�
 * 
 * ���ն�ϵ�к�Ϊkey
 * 
 * �ն�ϵ�к���TLGPRSThreadһһ��Ӧ
 * 
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * 
 * @version 1.0
 */
public class GPRSThreadList extends Hashtable {
	static GPRSThreadList instance = null;
	private ConcurrentHashMap<ClientSocket, String> tcpLinkList = new ConcurrentHashMap<ClientSocket, String>();

	public synchronized static GPRSThreadList getInstance() {
		if (instance == null) {
			instance = new GPRSThreadList();
		}
		return instance;
	}

	private float count = 0;

	public synchronized void addCount() {
		count++;
	}

	public synchronized float getCount() {
		return count;
	}

	private float sec = 0;

	public synchronized void addSec(float l) {
		sec += l;
	}

	public synchronized float getSec() {
		return sec;
	}

	/**
	 * ���һ���µ�TCP����
	 * 
	 * @param key
	 *            String
	 * @param gpsClient
	 *            GPSClient
	 */
	// public synchronized void add(String key, GprsTcpThread gpsClient) {
	// if (instance.get(key) != null) {
	//
	// instance.remove(key);
	// SocketChannel socketChannel = gpsClient.getGprsSocketTcpChannel()
	// .getSocketChannel();
	// if (socketChannel != null) {
	// Socket socket = socketChannel.socket();
	// SocketAddress socketAddress = socket.getRemoteSocketAddress();
	// if (socketAddress != null) {
	// this.tcpLinkList.put(socketAddress, key);
	// Log.getInstance().outLog(key + " tcp��·��ַ��" + socketAddress);
	// }
	//
	// }
	//
	// }
	// if (gpsClient.getGprsSocketTcpChannel().getSocketChannel().isOpen()) {
	// instance.put(key, gpsClient);
	// Log.getInstance().outLog("����" + instance.size() + " ���ն˽��� GPRS");
	// }
	//
	// }
	public synchronized void add(String key, GprsTcpThread newClient) {
		GprsTcpThread oldClient = (GprsTcpThread) instance.get(key);
		Socket oldSocket = null;
		Socket newSocket = null;
		String oldAddress = null;
		String newAddress = null;
		SocketChannel oldSocketChannel = null;
		SocketChannel newSocketChannel = null;
		GprsSocketTcpChannel oldGprsChannel = null;
		GprsSocketTcpChannel newGprsChannel = null;
		ClientSocket clientSocketTmp = new ClientSocket();

		if (newClient != null) {
			newGprsChannel = newClient.getGprsSocketTcpChannel();
			// �µ�������·
			if (newGprsChannel != null) {
				newSocketChannel = newGprsChannel.getSocketChannel();// tcpClient.getSocketChannel();
				if (newSocketChannel != null) {
					newSocket = newSocketChannel.socket();
					clientSocketTmp.setSocket(newSocket);
					clientSocketTmp.setDate(new Date());

					if (newSocket != null) {

						SocketAddress newSocketAddress = newSocket
								.getRemoteSocketAddress();
						if (newSocketAddress != null) {

							newAddress = newSocketAddress.toString();

							Set tmpTcpSet = tcpLinkList.entrySet();
							List keyList = new ArrayList(tmpTcpSet);
							Iterator iterator = keyList.iterator();
							boolean isExit = false;
							ClientSocket client = null;
							while (iterator.hasNext()) {
								try {
									Map.Entry met = (Map.Entry) iterator.next();
									client = (ClientSocket) met.getKey();
									String value = (String) met.getValue();
									if (value != null && value.equals(key)) {
										if (client != null
												&& client.getSocket().equals(
														newSocket)) {
											isExit = true;
											break;
										}
									}

								} catch (Exception e) {
									Log.getInstance().errorLog(
											"check tcplink error", e);
								}
							}
							if (!isExit) {
								tcpLinkList.put(clientSocketTmp, key);
							} else {
								client.setDate(new Date());
								tcpLinkList.put(client, key);
							}
							
							TcpLinkCache.getInstance().addToTcpCache(newSocket, new Date());

							Log.getInstance().outLog(
									" tcp��·��ַ��" + newAddress + "����Ӧ�ն����кţ�"
											+ key);
							Log.getInstance().outLog(
									key + " tcp new �ͻ������ӵ�ַ��" + newAddress
											+ ",instance.tcpLinkList size="
											+ instance.tcpLinkList.size());
						} else {

							try {
								Log.getInstance().outLog(
										key + " tcp new ��·�ͻ��˵�ַΪ��,"
												+ newSocket.toString());
								newSocket.close();
								newSocketChannel.close();
								Log.getInstance().outLog(
										key + " �ر� tcp new ��·�ͻ���Ϊ�յ�ַ!");
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		if (oldClient != null) {
			try {
				oldGprsChannel = oldClient.getGprsSocketTcpChannel();
				// �ɵ�������·
				if (oldGprsChannel != null) {
					oldSocketChannel = oldGprsChannel.getSocketChannel();// tcpClient.getSocketChannel();
					if (oldSocketChannel != null) {
						oldSocket = oldSocketChannel.socket();
						if (oldSocket != null) {
							SocketAddress oldSocketAddress = oldSocket
									.getRemoteSocketAddress();
							if (oldSocketAddress != null) {
								oldAddress = oldSocketAddress.toString();
								Log.getInstance().outLog(
										key + " tcp OLD �ͻ������ӵ�ַ��" + oldAddress);
							} else {
								try {
									removeChangeOldLink(oldSocket);
								} catch (Exception e) {
								}

								try {
									oldSocket.close();
									oldSocketChannel.close();
									Log
											.getInstance()
											.outLog(
													key
															+ " tcp ����·�ͻ��˵�ַΪ�գ�ɾ��old channel!");
								} catch (IOException e) {

									e.printStackTrace();
								}
							}
						}
					}
				}

				if (oldAddress != null && newAddress != null
						&& !oldAddress.equals(newAddress)) {

					if (oldSocketChannel != null) {
						oldSocketChannel.close();
						oldSocketChannel = null;
					}
					if (oldSocket != null) {
						oldSocket.close();
						oldSocket = null;
						Log.getInstance().outLog(
								key + " tcp ���ӷ����仯��ɾ���ɵ�����:" + oldAddress);
					}
					removeChangeOldLink(oldSocket);
				}

			} catch (IOException e) {

				e.printStackTrace();
				Log.getInstance().errorLog(key + "�رվ������쳣", e);

			} finally {
				instance.remove(key);
			}

		}

		try {
			if (newSocketChannel != null) {
				if (newSocketChannel.isOpen()) {

					instance.put(key, newClient);

					Log.getInstance().outLog(
							"����" + instance.size() + " ��TCP�ն˽��� GPRS");
				} else {
					// newSocket.close();
					// newSocketChannel.close();
					Log.getInstance().outLog(
							key + " tcp newSocketChannel.isOpen()="
									+ newSocketChannel.isOpen());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.getInstance().errorLog("�����������쳣", e);
		}

	}

	private void removeChangeOldLink(Socket socket) {

		Set tmpTcpSet = tcpLinkList.entrySet();
		List keyList = new ArrayList(tmpTcpSet);
		Iterator iterator = keyList.iterator();
		boolean isExit = false;

		while (iterator.hasNext()) {
			try {
				Map.Entry met = (Map.Entry) iterator.next();
				ClientSocket client = (ClientSocket) met.getKey();
				String value = (String) met.getValue();
				if (client.getSocket().equals(socket)) {
					tcpLinkList.remove(client);
					if (!client.getSocket().isClosed()) {
						Log.getInstance().outLog(
								"removeChangeOldLink:" + client.toString()
										+ ",tcplink size:" + tcpLinkList.size());
						client.getSocket().close();
					}
					
					break;
				}

			} catch (Exception e) {
				Log.getInstance().errorLog("check tcplink error", e);
			}
		}
	}

	/**
	 * ���һ���µ�UDP����
	 * 
	 * @param key
	 *            String
	 * @param gpsClient
	 *            GPSClient
	 */
	public synchronized void add(String key, TerminalUDPAddress gpsClient) {
		if (instance.get(key) != null) {
			instance.remove(key);
			// Log.getInstance().outLog("����" + instance.size() + " �������� GPRS");
			// return;
		}
		instance.put(key, gpsClient);
		Log.getInstance().outLog("����" + instance.size() + " ���ն˽��� GPRS");

	}

	/**
	 * �ж��Ƿ��Ѿ�����
	 * 
	 * @param key
	 *            String
	 * @return boolean
	 */
	public boolean isExist(String key) {
		if (instance.get(key) == null)
			return false;
		return true;
	}

	public void checkTcpLinkList() {

		Date curDate = new Date();
		Calendar curcal = Calendar.getInstance();
		curcal.setTime(curDate);

		Set tmpTcpSet = tcpLinkList.entrySet();
		List keyList = new ArrayList(tmpTcpSet);
		Iterator iterator = keyList.iterator();
		Calendar conncal = Calendar.getInstance();

		while (iterator.hasNext()) {
			try {
				Map.Entry met = (Map.Entry) iterator.next();
				ClientSocket client = (ClientSocket) met.getKey();
				String value = (String) met.getValue();

				Date connDate = client.getDate();
				conncal.setTime(connDate);
				conncal.add(Calendar.MINUTE, 5);
				Log.getInstance().outLog(
						value
								+ " old tcplink time:"
								+ Tools.formatDate2Str(connDate,
										"yyyy-MM-dd HH:mm:ss")
								+ ",new tcplink time:"
								+ Tools.formatDate2Str(curDate,
										"yyyy-MM-dd HH:mm:ss") + ",ʱ��"
								+ (curDate.getTime() - connDate.getTime())
								/ 1000 + " �롣");

				if (curcal.compareTo(conncal) > 0) {// tcpTime������������������б����޳�
					
					tcpLinkList.remove(client);
					if (client.getSocket() != null
							&& !client.getSocket().isClosed()) {
						Log.getInstance().outLog(
								value
										+ " ��ʱ������ӣ�ɾ��.��ǰ tcplinklist size:"
										+ instance.tcpLinkList.size()
										+ ",delete socket:"
										+ client.toString());
						client.getSocket().close();
					}
					

				}

			} catch (Exception e) {
				Log.getInstance().errorLog("check tcplink error", e);
			}
		}

	}

	/**
	 * ����ն�����״̬,
	 * 
	 * @param simCard
	 *            String
	 * @return Socket
	 */
	public synchronized void checkUDPState() throws Exception {

		Object obj = null;
		boolean flag = false;
		Iterator it = instance.keySet().iterator();
		Log.getInstance().outLog("�����������" + instance.size());
		while (it.hasNext()) {

			Date curDate = new Date();
			Calendar curcal = Calendar.getInstance();
			curcal.setTime(curDate);
			String key = (String) it.next();
			obj = instance.get(key);

			if (obj instanceof TerminalUDPAddress) {
				TerminalUDPAddress gpsClient = (TerminalUDPAddress) obj;

				Date connDate = gpsClient.getDate();
				Calendar conncal = Calendar.getInstance();
				conncal.setTime(connDate);
				conncal.add(Calendar.MINUTE, 5);

				if (curcal.compareTo(conncal) > 0) {// һ������������������б����޳�
					it.remove();
					instance.remove(gpsClient.getDeviceSN());
					Log.getInstance().outLog(
							gpsClient.getDeviceSN() + " �˳���UDP����," + "��ǰ������="
									+ instance.size());
				}

			} else if (obj instanceof GprsTcpThread) {
				GprsTcpThread tcpClient = (GprsTcpThread) obj;
				Date connDate = tcpClient.getDate();
				Calendar conncal = Calendar.getInstance();
				conncal.setTime(connDate);
				conncal.add(Calendar.MINUTE, 8);

				if (curcal.compareTo(conncal) > 0) {// 8������������������б����޳�
					it.remove();
					instance.remove(tcpClient.getGprsSocketTcpChannel()
							.getParseBase().getDeviceSN());
					GprsSocketTcpChannel gprsChannel = tcpClient
							.getGprsSocketTcpChannel();
					if (gprsChannel != null) {
						try {
							SocketChannel socket = gprsChannel
									.getSocketChannel();
							if (socket != null) {
								Socket address = socket.socket();
								this.removeChangeOldLink(address);

								socket.socket().setKeepAlive(false);
								socket.socket().close();
								socket.close();
								socket = null;

								Log.getInstance().outLog(
										(String) key + " 8���Ӳ��,tcp ���ӱ��Ͽ���");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.getInstance().outLog(
							tcpClient.getGprsSocketTcpChannel().getParseBase()
									.getDeviceSN()
									+ "�˳���TCP����," + "��ǰ������=" + instance.size());
				}

			}
		}

	}

	/**
	 * ͨ���ֻ��ŵõ�UDP�߳�
	 * 
	 * @param simCard
	 *            String
	 * @return Socket
	 */
	public synchronized TerminalUDPAddress getGpsThreadBySim(String simCard) {
		TerminalUDPAddress gpsClient = null;
		boolean flag = false;
		Iterator it = instance.keySet().iterator();

		while (it.hasNext()) {
			try {
				Object obj = instance.get(it.next());
				if (obj instanceof TerminalUDPAddress) {

					gpsClient = (TerminalUDPAddress) obj;
					if (gpsClient.getDeviceSN().equalsIgnoreCase(simCard)) {
						flag = true;
						break;
					}
				}
			} catch (Exception ex) {
				Log.getInstance().outLog("ͨ���ֻ��Ż�ȡUDP���Ӷ����쳣��" + ex.getMessage());
			}
		}
		if (flag) {
			return gpsClient;
		} else {
			return null;
		}
	}

	/**
	 * ͨ���ֻ��ŵõ�Tcp Socket
	 * 
	 * @param simCard
	 *            String
	 * @return Socket
	 */
	public synchronized GprsTcpThread getGpsTcpThreadBySim(String simCard) {
		GprsTcpThread gpsClient = null;
		boolean flag = false;
		Iterator it = instance.keySet().iterator();
		while (it.hasNext()) {
			try {
				Object obj = instance.get(it.next());

				if (obj instanceof GprsTcpThread) {

					gpsClient = (GprsTcpThread) obj;
					if (gpsClient.getGprsSocketTcpChannel().getParseBase()
							.getDeviceSN().equalsIgnoreCase(simCard)) {
						flag = true;

						break;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.getInstance().errorLog("ͨ���ֻ��Ż�ȡTCP�����߳��쳣", ex);
				Log.getInstance().outLog("ͨ���ֻ��Ż�ȡTCP�����߳��쳣��" + ex.getMessage());
			}
		}
		if (flag) {
			return gpsClient;
		} else {
			return null;
		}
	}

	// ͨ���ֻ��Ż�ȡUDP��TCP�������߳�
	public synchronized Object getGprsThreadBySim(String simCard) {
		Object retObj = null;
		boolean flag = false;
		Iterator it = instance.keySet().iterator();
		while (it.hasNext()) {
			try {
				Object obj = instance.get(it.next());
				if (obj instanceof GprsTcpThread) {

					GprsTcpThread gpsClient = (GprsTcpThread) obj;
					if (gpsClient.getGprsSocketTcpChannel().getParseBase()
							.getPhnum().equalsIgnoreCase(simCard)) {
						flag = true;
						retObj = gpsClient;
						break;
					}
				} else if (obj instanceof TerminalUDPAddress) {
					TerminalUDPAddress udpObj = (TerminalUDPAddress) obj;
					if (udpObj.getDeviceSN().equalsIgnoreCase(simCard)) {
						flag = true;
						retObj = udpObj;
						break;
					}
				}
			} catch (Exception ex) {
				Log.getInstance().outLog(ex.getMessage());
			}
		}
		if (flag) {
			return retObj;
		} else {
			return null;
		}
	}

	/**
	 * ɾ��UDP����
	 * 
	 * @param key
	 *            String
	 */
	public synchronized void removeUDP(String key) {
		TerminalUDPAddress gpsClient = (TerminalUDPAddress) instance.get(key);
		if (gpsClient != null) {

			instance.remove(key);

			Log.getInstance().outLog(gpsClient.getDeviceSN() + "�˳���UDP GPRS");
		}
	}

	// ɾ��TCP����
	public synchronized void remove2(String key) {
		GprsTcpThread gpsClient = (GprsTcpThread) instance.get(key);
		if (gpsClient != null) {

			instance.remove(key);

			GprsSocketTcpChannel gprsChannel = gpsClient
					.getGprsSocketTcpChannel();
			if (gprsChannel != null) {
				try {
					SocketChannel socket = gprsChannel.getSocketChannel();
					if (socket != null) {
						Socket address = socket.socket();
						this.removeChangeOldLink(address);

						socket.socket().setKeepAlive(false);
						socket.socket().close();
						socket.close();
						socket = null;

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Log.getInstance().outLog(
					gpsClient.getGprsSocketTcpChannel().getParseBase()
							.getDeviceSN()
							+ "�˳���TCP GPRS");
		}
	}

	/**
	 * ͨ��TCP��ַ��ȡ�豸ID
	 * 
	 * @return the tcpLinkList
	 */
	public String getDeviceIdByTcpAddress(Socket socketAddress) {

		ClientSocket tmpCs = new ClientSocket();
		tmpCs.setSocket(socketAddress);

		return this.tcpLinkList.get(tmpCs);
	}

	/**
	 * @param tcpLinkList
	 *            the tcpLinkList to set
	 */
	public void setTcpLinkList(
			ConcurrentHashMap<ClientSocket, String> tcpLinkList) {
		this.tcpLinkList = tcpLinkList;
	}

	/**
	 * @return the tcpLinkList
	 */
	public ConcurrentHashMap<ClientSocket, String> getTcpLinkList() {
		return this.tcpLinkList;
	}
}
