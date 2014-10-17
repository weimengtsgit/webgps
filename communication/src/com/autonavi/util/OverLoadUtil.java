/**
 * 
 */
package com.autonavi.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.autonavi.directl.Log;

/**
 * 
 * @author shiguang.zhou
 * 
 */
public class OverLoadUtil {

	public static void sendToTcp(String host, int port, byte[] data) {
		Socket socket = null;
		OutputStream oos = null;
		BufferedInputStream is = null;
		PrintWriter os = null;

		try {
			socket = new Socket(host, port);
			is = new BufferedInputStream(socket.getInputStream());
			oos = socket.getOutputStream();
			sentDataBytes(oos, data);

		} catch (IOException ex) {
			Log.getInstance().outJHSLoger("客户端没有连接上:" + host + ":" + port);
			Log.getInstance().errorLog(ex.getMessage(), ex);
		} finally {
			try {
				if (oos != null) {
					oos.close();
					oos = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if (socket != null) {
					socket.close();
					socket = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void sendToUdp(String host, int port, byte[] b) {
		DatagramSocket socket = null;
		InetAddress ip = null;

		try {
			ip = InetAddress.getByName(host);
			socket = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(b, b.length, ip, port);
			socket.send(dp);
			Log.getInstance().outLog("UDP转发内容：" + new String(b));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		}

	}

	public static String sentDataBytes(OutputStream oos, byte[] data) {
		String ret = "error";
		try {
			oos.write(data);
			oos.flush();
			Log.getInstance().outLog("下发数据:" + new String(data));
			ret = "ok";
		} catch (Exception ex) {
			ret = "error";
			Log.getInstance().outLog(
					"客户端下发消息:" + new String(data) + "失败,原因:" + ex.getMessage());
		}
		return ret;
	}

}
