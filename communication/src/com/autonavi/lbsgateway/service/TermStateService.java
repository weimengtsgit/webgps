package com.autonavi.lbsgateway.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

import com.autonavi.directl.Log;

import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.TcpLinkCache;
import com.autonavi.lbsgateway.bean.TerminalUDPAddress;

public class TermStateService extends TimerTask {

	public TermStateService() {
		Log.getInstance().outLog("启动终端状态检测服务！");
	}

	@Override
	public synchronized void run() {

		try {
			TcpLinkCache.getInstance().checkTcpCache();
			GPRSThreadList.getInstance().checkTcpLinkList();
			GPRSThreadList.getInstance()
					.checkUDPState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.getInstance().errorLog("检测连接出现异常", e);
		}

	}

}
