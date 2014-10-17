package com.autonavi.lbsgateway.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.lbsgateway.bean.BaJuTask;
import com.autonavi.lbsgateway.bean.InstructionBean;

/**
 * @author shiguang.zhou
 * 
 */
public class BaJuTaskSend extends TimerTask {

	private static boolean isRunning = false;
	static long num = 0;

	String url = "http://localhost:7203";

	public BaJuTaskSend() {
		try {
			url = Config.getInstance().getProperty("url");

		} catch (Exception ex) {
			url = "http://localhost:7203";
		}
		if (url == null || url.equals("")) {
			url = "http://localhost:7203";
		}

	}

	@Override
	public void run() {
		// TODO 自动生成方法存根
		if (!isRunning) {// 防止与下一次任务重叠
			try {
				isRunning = true;
				Log.getInstance().outLog("第" + num + "次监测导航任务！");
				ArrayList<BaJuTask> list = this.getBaJuTaskByState("1");
				ArrayList<BaJuTask> ulist = new ArrayList<BaJuTask>();
				if (null != list) {
					for (int i = 0; i < list.size(); i++) {

						BaJuTask ib = list.get(i);
						String xml = ib.getTaskContet();
						String deviceid = ib.getDeviceId();
						String state = ib.getState();
						String type = ib.getType();
						boolean flag = false;
						if (xml == null || xml.trim().equals(""))
							continue;

						if (!type.equals("2")) {
							flag = this.Send(60 * 1000, xml);
							Log.getInstance().outJHSLoger(
									"发送" + xml + " 是否成功：" + flag);
						} else {
							// 下发路径
							org.dom4j.Document doc = null;

							try {
								doc = org.dom4j.DocumentHelper.parseText(xml);
							} catch (Exception e) {
								// TODO 自动生成 catch 块
								e.printStackTrace();
							}
							org.dom4j.Element root = doc.getRootElement();

							// org.dom4j.Element mapabc =
							// root.element("mapabc");
							org.dom4j.Element taskE = root.element("request");
							org.dom4j.Element req = taskE
									.element("taskDispatchRequest");
							org.dom4j.Element tinfo = req.element("taskInfo");
							org.dom4j.Element naviInfo = tinfo
									.element("navigationInfo");

							String startPointX = naviInfo.element("startPoint")
									.element("x").getTextTrim();
							String startPointY = naviInfo.element("startPoint")
									.element("y").getTextTrim();

							String endPointX = naviInfo.element("endPoint")
									.element("x").getTextTrim();
							String endPointY = naviInfo.element("endPoint")
									.element("y").getTextTrim();
							List virPointsList = naviInfo.elements("viaPoint");

							com.mapabc.geom.CoordCvtAPI cca = new com.mapabc.geom.CoordCvtAPI();
							double slng = Double.parseDouble(startPointX) / 3600000;
							double slat = Double.parseDouble(startPointY) / 3600000;
							double elng = Double.parseDouble(endPointX) / 3600000;
							double elat = Double.parseDouble(endPointY) / 3600000;
							String jiamiSX = slng+"";//cca.encrypt(slng);
							String jiamiSY = slat+"";//cca.encrypt(slat); 
							String jiamiEX = elng+"";//cca.encrypt(elng);
							String jiamiEY = elat+"";//cca.encrypt(elat);
							String[] virx = new String[virPointsList.size()];
							String[] viry = new String[virPointsList.size()];

							for (int j = 0; j < virPointsList.size(); j++) {

								org.dom4j.Element e = (org.dom4j.Element) virPointsList
										.get(j);
								String vx = e.elementText("x");
								String vy = e.elementText("y");
								double dvx = Double.parseDouble(vx) / 3600000;
								double dvy = Double.parseDouble(vy) / 3600000;
								virx[j] = dvx+"";//cca.encrypt(dvx);
								viry[j] = dvy+"";//cca.encrypt(dvy);

							}

							Route route = new Route();
							byte[] routebs = route.getRouteBytes(jiamiSX,
									jiamiSY, jiamiEX, jiamiEY, virx, viry);
							ByteBuffer bbuf = this.makeRequestRouteBs(routebs,
									xml);
							flag = this.SendBytes(60 * 1000, bbuf);
							Log.getInstance().outJHSLoger(
									"下发路径" + xml + " 是否成功：" + flag);
						}
						Date crtdate = ib.getCrtdate();// 指令创建日期

						SimpleDateFormat simpleDate = null;
						simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String cdate = simpleDate.format(crtdate);

						Calendar crtCld = Calendar.getInstance();
						crtCld.setTime(crtdate);
						// crtCld.add(Calendar.HOUR, 24);
						crtCld.add(Calendar.MINUTE, 10);
						Date d1 = crtCld.getTime();
						String cd1 = simpleDate.format(d1);

						Date curdate = new Date();
						Calendar cal = Calendar.getInstance();
						cal.setTime(curdate);

						Date d2 = cal.getTime();
						String cd2 = simpleDate.format(d2);

						if (crtCld.compareTo(cal) < 0 && state.equals("1")
								&& !flag) {// 10分钟仍为待发，则把状态置为失败
							ib.setState("2");
							ulist.add(ib);
						}
						if (flag) {// 发送成功
							ib.setState("0");
							ulist.add(ib);
						}

					}
				}

				if (ulist != null && ulist.size() > 0) {
					this.updateBaJuTaskState(ulist);// 更新状态
				}
				isRunning = false;
				num++;
			} catch (Exception e) {
				Log.getInstance().errorLog("下发任务异常", e);
				isRunning = false;
			}
		}

	}

	/**
	 * 根据指令状态获取指令状态对象
	 * 
	 * @param flag
	 * @return
	 */
	private ArrayList<BaJuTask> getBaJuTaskByState(String flag) {
		ArrayList list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = DbOperation.getConnection();
		String sql = "select * from t_BaJu_Task where state=?";

		try {

			pst = conn.prepareStatement(sql);
			pst.setString(1, flag);
			rs = pst.executeQuery();

			while (rs.next()) {
				BaJuTask bean = new BaJuTask();
				bean.setTaskContet(rs.getString("task_content"));
				bean.setState(flag);
				bean.setDeviceId(rs.getString("device_id"));
				bean.setReply(rs.getString("reply"));
				bean.setType(rs.getString("type"));
				bean.setId(rs.getLong("id"));
				bean.setCrtdate(rs.getTimestamp("crtdate"));
				list.add(bean);
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();

		} finally {
			DbOperation.release(null, rs, pst, conn);
		}

		return list;
	}

	private boolean updateBaJuTaskState(ArrayList<BaJuTask> beanList) {
		ArrayList list = new ArrayList();
		Connection conn = DbOperation.getConnection();
		PreparedStatement pst = null;
		Statement stm = null;

		boolean flag = false;

		if (beanList == null || beanList.size() <= 0)
			return false;
		String sql = "update t_BaJu_Task set state=? where id=?";
		// String caseSql = "update t_speed_case set flag=0 where device_id="+;

		try {

			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);// conn.createStatement();

			for (int i = 0; i < beanList.size(); i++) {
				// String sql ="update t_structions set
				// state='"+beanList.get(i).getState()+"' where
				// id="+beanList.get(i).getId();
				pst.setString(1, beanList.get(i).getState());
				pst.setLong(2, beanList.get(i).getId());
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;

		} finally {
			DbOperation.release(stm, null, pst, conn);
		}
		return flag;
	}

	public boolean Send(int timeOut, String xml) {
		boolean retValue = false;
		String ip = getIP();
		String uri = getURI();
		int port = getPort();
		Socket skt = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedWriter bout = null;
		BufferedReader bin = null;

		try {
			skt = new Socket(ip, port);
			skt.setSoTimeout(timeOut);
			skt.setKeepAlive(false);
			if (skt.isConnected()) {
				in = skt.getInputStream();
				out = skt.getOutputStream();
				bout = new BufferedWriter(new OutputStreamWriter(out));
				String content = MakeHttpContent(ip, uri, port, xml);
				Log.getInstance().outJHSLoger("下发任务内容：" + content);
				bout.write(content);
				bout.flush();
				bin = new BufferedReader(new InputStreamReader(in));
				String oneLine = null;
				StringBuffer sb = new StringBuffer();
				System.out.println("开始等待数据:");
				while ((oneLine = bin.readLine()) != null) {
					System.out.println("接受到数据:" + oneLine);
					if (oneLine.trim().equalsIgnoreCase("OK")) {
						retValue = true;
						break;
					}
				}

			}
		} catch (UnknownHostException ex) {
			System.out.println("UnknownHostException");
			ex.printStackTrace();
			retValue = false;
		} catch (java.net.SocketTimeoutException ex) {
			System.out.println("SocketTimeoutException");
			ex.printStackTrace();
			retValue = false;
		} catch (IOException ex) {
			System.out.println("IOException");
			ex.printStackTrace();
			retValue = false;
		} finally {
			try {
				if (bout != null) {
					bout.close();
				}
				if (bin != null) {
					bin.close();
				}
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (skt != null) {
					skt.close();
				}
			} catch (IOException ex1) {
			}
		}

		return retValue;
	}

	public String getIP() {
		String ip = "";
		int bIndex = url.indexOf("http://") + 7;
		String tmp = url.substring(bIndex);
		int eIndex = tmp.indexOf(":");
		ip = tmp.substring(0, eIndex);
		return ip;
	}

	public int getPort() {
		int port = 80;
		int bIndex = url.indexOf("http://") + 7;
		String tmp = url.substring(bIndex);
		bIndex = tmp.indexOf(":") + 1;
		int eIndex = tmp.indexOf("/");
		String sport = "";
		if (eIndex >= 0) {
			sport = tmp.substring(bIndex, eIndex);
		} else {
			sport = tmp.substring(bIndex);
		}
		port = Integer.parseInt(sport);
		return port;
	}

	public String getURI() {
		String uri = "";
		int bIndex = url.indexOf("http://") + 7;
		String tmp = url.substring(bIndex);
		int eIndex = tmp.indexOf("/");
		if (eIndex >= 0) {
			uri = tmp.substring(eIndex);
		}
		return uri;
	}

	/**
	 * 拼凑完整的协议
	 * 
	 * @param ip
	 * @param uri
	 * @param port
	 * @param content
	 * @return
	 */
	public String MakeHttpContent(String ip, String uri, int port,
			String content) {
		String retValue = "";
		retValue = "POST http://" + ip + ":" + port + uri + " HTTP/1.1"
				+ "\r\n" + "Connection: Keep-Alive" + "\r\n"
				+ "Content-Type: text/xml" + "\r\n" + "Content-Length: "
				+ content.length() + "\r\n\r\n" + content + "\r\n";
		return retValue;
	}

	/**
	 * 路径下发协议头
	 * 
	 * @param ip
	 * @param uri
	 * @param port
	 * @param len
	 * @return
	 */
	private String getHttpPostHead(String ip, String uri, int port, int len) {
		String retValue = "";
		retValue = "POST http://" + ip + ":" + port + uri + " HTTP/1.1"
				+ "\r\n" + "Connection: Keep-Alive" + "\r\n"
				+ "Content-Type: application/octet-stream" + "\r\n"
				+ "Content-Length: " + len + "\r\n\r\n";
		return retValue;

	}

	private ByteBuffer makeRequestRouteBs(byte[] routebs, String xml) {

		ByteBuffer bf = null;
		try {

			String ip = getIP();
			String uri = getURI();
			int port = getPort();

			byte[] xmlBs = xml.getBytes();
			 
			String httpHead = getHttpPostHead(ip, uri, port,
					(xmlBs.length + 1 + routebs.length));
			byte[] headBs = httpHead.getBytes();
			int bfSize = headBs.length + xmlBs.length + routebs.length + 1;

			bf = ByteBuffer.allocate(bfSize);
			bf.clear();
			bf.put(headBs).put(xmlBs).put((byte) 0).put(routebs);
			bf.flip();
		} catch (Exception ex) {
			 Log.getInstance().errorLog("构建naviserver路径内容错误",ex);
			bf = null;
			ex.printStackTrace();
		}
		return bf;

	}

	public boolean SendBytes(int timeOut, ByteBuffer bf) {
		boolean retValue = false;
		String ip = getIP();
		int port = getPort();
		Socket skt = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedWriter bout = null;
		BufferedReader bin = null;
		try {
			skt = new Socket(ip, port);
			skt.setSoTimeout(timeOut);
			skt.setKeepAlive(false);
			if (skt.isConnected()) {
				in = skt.getInputStream();
				out = skt.getOutputStream();
				bf.position(0);
				while (bf.position() < bf.limit()) {
					out.write(bf.get());
				}

				out.flush();
				bin = new BufferedReader(new InputStreamReader(in));
				String oneLine = null;
				StringBuffer sb = new StringBuffer();
				while ((oneLine = bin.readLine()) != null) {
					if (oneLine.trim().equalsIgnoreCase("OK")) {
						retValue = true;
						break;
					}
				}

			}
		} catch (UnknownHostException ex) {
			System.out.println("UnknownHostException");
			ex.printStackTrace();
		} catch (java.net.SocketTimeoutException ex) {
			System.out.println("SocketTimeoutException");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("IOException");
			ex.printStackTrace();
		} finally {
			try {
				if (bout != null) {
					bout.close();
				}
				if (bin != null) {
					bin.close();
				}
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (skt != null) {
					skt.close();
				}
			} catch (IOException ex1) {
			}

		}

		return retValue;
	}

	 

}
