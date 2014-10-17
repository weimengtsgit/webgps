package com.autonavi.lbsgateway.service;

import javax.servlet.*;
import javax.servlet.http.*;
import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.lbsgateway.GBLTerminalList;
import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.alarmpool.AlarmThread;
import com.autonavi.lbsgateway.gprsserver.tcp.GprsTcpServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Title: 服务管理Servlet
 * </p>
 * 
 * <p>
 * Description: 本类负责通讯网关的初始化，实现了从数据库中加载终端信息和负责内存的垃圾回收
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: 
 * </p>
 * 
 * @author 
 * @version 1.0
 */

@SuppressWarnings("serial")
public class StartServlet extends HttpServlet {
	private com.autonavi.lbsgateway.gprsserver.udp.GprsServer reactor;
	private com.autonavi.lbsgateway.gprsserver.tcp.GprsTcpServer reactorTcp;
	
	/**
	 * 从数据库中加载终端信息
	 * 
	 * @throws ServletException
	 */
	@SuppressWarnings("deprecation")
	public void init() throws ServletException {
		// new TrimTrackServer().start();
		try {
			Logger log = Logger.global;
			log.setLevel(Level.ALL);
			Logger log2 = Logger.global;
			log2.setLevel(Level.ALL);
			int udp_port = Integer.parseInt(Config.getInstance().getString("UDPPORT"));
			int tcp_port = Integer.parseInt(Config.getInstance().getString("TCPPORT"));
			//int listener_udp_port = Integer.parseInt(Config.getInstance().getString("LISTEN_UDP_PORT"));
			GBLTerminalList.getInstance().loadTerminals();
			// UDP服务
			com.autonavi.lbsgateway.gprsserver.udp.GprsServer reactor = new com.autonavi.lbsgateway.gprsserver.udp.GprsServer(udp_port, 40, 40, log);
			reactor.start();
			// TCP服务
			GprsTcpServer tcpserver = new GprsTcpServer(tcp_port, 40, 40);
			tcpserver.start();
			// reactor.join();
//			GprsTcpServer tcpserver1 = new GprsTcpServer(listener_udp_port, 1, 1, log);
//			tcpserver1.start();
			AlarmThread at = new AlarmThread();
			at.start();
// 			 
//             //启动长虹交互式导航服务		
//			 JHSListenServer jhsServer = new JHSListenServer();
//			 jhsServer.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Reactor 出现错误,已经关闭" + e);
			if (null != reactor && reactor.isAlive()) {
				reactor.interrupt();
				reactor = null;
				Log.getInstance().outLog("停止了UDP通讯服务");
			}
			if (null != reactorTcp && reactorTcp.isAlive()) {
				reactorTcp.interrupt();
				reactorTcp = null;
				Log.getInstance().outLog("停止了TCP通讯服务");
			}
		}
	}
	// Clean up resources
	public void destroy() {
		GPRSThreadList.getInstance().clear();
		if (null != reactor ) {
			reactor.interrupt();
			reactor = null;
			Log.getInstance().outLog("停止了UDP通讯服务");
		}
		if (null != reactorTcp ) {
			reactorTcp.interrupt();
			reactorTcp = null;
			Log.getInstance().outLog("停止了TCP通讯服务");
		}
	}
}
