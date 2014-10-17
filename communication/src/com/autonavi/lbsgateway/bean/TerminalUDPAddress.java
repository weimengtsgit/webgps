package com.autonavi.lbsgateway.bean;

import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Date;

public class TerminalUDPAddress implements Serializable{
	
	private String deviceSN;
	
	private SocketAddress socketAddress;
	
	private DatagramChannel datagramChannel;
	
	private Date date;

	public  Date getDate() {
		return date;
	}

	public synchronized void setDate(Date date) {
		this.date = date;
	}

	public  DatagramChannel getDatagramChannel() {
		return datagramChannel;
	}

	public  void setDatagramChannel(DatagramChannel datagramChannel) {
		this.datagramChannel = datagramChannel;
	}

	public  SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public  void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public  String getDeviceSN() {
		return deviceSN;
	}

	public  void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}
	
	

}
