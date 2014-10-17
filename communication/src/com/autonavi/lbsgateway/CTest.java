package com.autonavi.lbsgateway;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class CTest {
	
	public static void main(String[] args){
		
		DatagramChannel chanel;
		try {
			chanel = DatagramChannel.open();
			DatagramSocket sck = chanel.socket();
			SocketAddress sa = new InetSocketAddress(7001);
			SocketAddress asa = new InetSocketAddress(InetAddress.getByName("localhost"),7002);
			sck.bind(sa);
			ByteBuffer bb = ByteBuffer.allocate(512);
			String s = "test tesgt";
			bb.put(s.getBytes());
			
			int n = chanel.send(bb, asa);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		
	}

}
