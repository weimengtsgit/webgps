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
 * 用来保存已经连立GPRS连接的终端
 * 
 * 以终端系列号为key
 * 
 * 终端系列号与TLGPRSThread一一对应
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
	 * 添加一个新的TCP连接
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
	// Log.getInstance().outLog(key + " tcp链路地址：" + socketAddress);
	// }
	//
	// }
	//
	// }
	// if (gpsClient.getGprsSocketTcpChannel().getSocketChannel().isOpen()) {
	// instance.put(key, gpsClient);
	// Log.getInstance().outLog("共有" + instance.size() + " 个终端进入 GPRS");
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
			// 新的连接链路
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
									" tcp链路地址：" + newAddress + "，对应终端序列号："
											+ key);
							Log.getInstance().outLog(
									key + " tcp new 客户端连接地址：" + newAddress
											+ ",instance.tcpLinkList size="
											+ instance.tcpLinkList.size());
						} else {

							try {
								Log.getInstance().outLog(
										key + " tcp new 链路客户端地址为空,"
												+ newSocket.toString());
								newSocket.close();
								newSocketChannel.close();
								Log.getInstance().outLog(
										key + " 关闭 tcp new 链路客户端为空地址!");
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
				// 旧的连接链路
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
										key + " tcp OLD 客户端连接地址：" + oldAddress);
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
															+ " tcp 旧链路客户端地址为空，删除old channel!");
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
								key + " tcp 链接发生变化，删除旧的连接:" + oldAddress);
					}
					removeChangeOldLink(oldSocket);
				}

			} catch (IOException e) {

				e.printStackTrace();
				Log.getInstance().errorLog(key + "关闭旧连接异常", e);

			} finally {
				instance.remove(key);
			}

		}

		try {
			if (newSocketChannel != null) {
				if (newSocketChannel.isOpen()) {

					instance.put(key, newClient);

					Log.getInstance().outLog(
							"共有" + instance.size() + " 个TCP终端进入 GPRS");
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
			Log.getInstance().errorLog("增加新连接异常", e);
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
	 * 添加一个新的UDP连接
	 * 
	 * @param key
	 *            String
	 * @param gpsClient
	 *            GPSClient
	 */
	public synchronized void add(String key, TerminalUDPAddress gpsClient) {
		if (instance.get(key) != null) {
			instance.remove(key);
			// Log.getInstance().outLog("共有" + instance.size() + " 辆车进入 GPRS");
			// return;
		}
		instance.put(key, gpsClient);
		Log.getInstance().outLog("共有" + instance.size() + " 个终端进入 GPRS");

	}

	/**
	 * 判断是否已经存在
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
										"yyyy-MM-dd HH:mm:ss") + ",时间差："
								+ (curDate.getTime() - connDate.getTime())
								/ 1000 + " 秒。");

				if (curcal.compareTo(conncal) > 0) {// tcpTime分钟内无新数据则从列表中剔除
					
					tcpLinkList.remove(client);
					if (client.getSocket() != null
							&& !client.getSocket().isClosed()) {
						Log.getInstance().outLog(
								value
										+ " 超时不活动连接，删除.当前 tcplinklist size:"
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
	 * 检查终端连接状态,
	 * 
	 * @param simCard
	 *            String
	 * @return Socket
	 */
	public synchronized void checkUDPState() throws Exception {

		Object obj = null;
		boolean flag = false;
		Iterator it = instance.keySet().iterator();
		Log.getInstance().outLog("检测连接数：" + instance.size());
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

				if (curcal.compareTo(conncal) > 0) {// 一分钟内无新数据则从列表中剔除
					it.remove();
					instance.remove(gpsClient.getDeviceSN());
					Log.getInstance().outLog(
							gpsClient.getDeviceSN() + " 退出了UDP连接," + "当前连接数="
									+ instance.size());
				}

			} else if (obj instanceof GprsTcpThread) {
				GprsTcpThread tcpClient = (GprsTcpThread) obj;
				Date connDate = tcpClient.getDate();
				Calendar conncal = Calendar.getInstance();
				conncal.setTime(connDate);
				conncal.add(Calendar.MINUTE, 8);

				if (curcal.compareTo(conncal) > 0) {// 8分钟内无新数据则从列表中剔除
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
										(String) key + " 8分钟不活动,tcp 连接被断开！");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.getInstance().outLog(
							tcpClient.getGprsSocketTcpChannel().getParseBase()
									.getDeviceSN()
									+ "退出了TCP连接," + "当前连接数=" + instance.size());
				}

			}
		}

	}

	/**
	 * 通过手机号得到UDP线程
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
				Log.getInstance().outLog("通过手机号获取UDP连接对象异常：" + ex.getMessage());
			}
		}
		if (flag) {
			return gpsClient;
		} else {
			return null;
		}
	}

	/**
	 * 通过手机号得到Tcp Socket
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
				Log.getInstance().errorLog("通过手机号获取TCP处理线程异常", ex);
				Log.getInstance().outLog("通过手机号获取TCP处理线程异常：" + ex.getMessage());
			}
		}
		if (flag) {
			return gpsClient;
		} else {
			return null;
		}
	}

	// 通过手机号获取UDP和TCP的连接线程
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
	 * 删除UDP连接
	 * 
	 * @param key
	 *            String
	 */
	public synchronized void removeUDP(String key) {
		TerminalUDPAddress gpsClient = (TerminalUDPAddress) instance.get(key);
		if (gpsClient != null) {

			instance.remove(key);

			Log.getInstance().outLog(gpsClient.getDeviceSN() + "退出了UDP GPRS");
		}
	}

	// 删除TCP连接
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
							+ "退出了TCP GPRS");
		}
	}

	/**
	 * 通过TCP地址获取设备ID
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
