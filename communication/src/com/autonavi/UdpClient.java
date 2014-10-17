/**
 * 
 */
package com.autonavi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import com.autonavi.directl.Tools;
import com.autonavi.lbsgateway.service.update.RemoteUpdateProgram;
import com.vividsolutions.jts.noding.IntersectionAdder;

/**
 * @author shiguang.zhou
 * 
 */
public class UdpClient {

	private String remoteHost = "211.137.182.233";
	private int remotePort = 5566;
	private DatagramSocket socket;

	public UdpClient() throws IOException {
		
	}

	public static void main(String[] args) throws Exception {
		UdpClient c = new UdpClient();
		c.talk();
	}

	public void talk() throws Exception {
		InetAddress ip = InetAddress.getByName(remoteHost);
		String af = "41204620353535353a3135313635303738303934203039313231313039333830377c307c307c307c307c307c307c31303030307c204245360d0a";
		byte[] baf = Tools.fromHexString(af);
		socket = new DatagramSocket();
		DatagramPacket dps = new DatagramPacket(baf, baf.length, ip, remotePort);
		socket.send(dps);
		socket.close();
		Thread.sleep(1000);
		
		
		 String head = "A U 5555:13406333442 091211093820 1 ";
		 
		byte[] b = null;
        RemoteUpdateProgram up = new RemoteUpdateProgram();
        int total = up.getDataHash().size();
        
		try {
			 
		for (int i = 0; i < total; i++) {
			socket = new DatagramSocket();
			byte[] bb = up.getPackBytes((i+1)+"");
			
//			ByteBuffer buf = ByteBuffer.allocate(8+head.getBytes().length+end.getBytes().length+bb.length);
//			buf.clear();
//			 
//			buf.put(head.getBytes());
//			buf.put((Integer.toHexString(total)).getBytes());
//			buf.put(" ".getBytes());
//			buf.put((Integer.toHexString(i+1)).getBytes());
//			buf.put(" ".getBytes());
//			buf.put("<".getBytes());
//			buf.put(bb);
//			buf.put(">".getBytes());
//			buf.put(end.getBytes());
//			buf.put((byte)0x0d);
//			buf.put((byte)0x0a);
			 
			
			String cmd =  head + Integer.toHexString(total) +" "+Integer.toHexString(i+1)+" <";
			String hex = Tools.bytesToHexString(cmd.getBytes());
			hex = hex + Tools.bytesToHexString(bb) + Tools.bytesToHexString("> a7e2".getBytes())+"0d0a";
			 
			 b =   Tools.fromHexString(hex);
			 
			DatagramPacket dp = new DatagramPacket(b, b.length, ip, remotePort);
			  
			socket.send(dp);
			socket.close();
			System.out.println(hex);
			Thread.sleep(1000);
		}
		 
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
