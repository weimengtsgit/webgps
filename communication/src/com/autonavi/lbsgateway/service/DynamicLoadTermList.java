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
import com.autonavi.lbsgateway.bean.TerminalUDPAddress;

public class DynamicLoadTermList extends TimerTask{
	
	public DynamicLoadTermList(){
		Log.getInstance().outLog("加载系统终端信息！");
	}

	@Override
	public void run() {
		
		com.autonavi.lbsgateway.GBLTerminalList.getInstance().clear();
		com.autonavi.lbsgateway.GBLTerminalList.getInstance().loadTerminals();
		 		 		 
	}
	
 

}
