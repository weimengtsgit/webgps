package com.sosgps.wzt.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.mapabc.geom.CoordCvtAPI;

public class CoordConvertAPI {
	private String serviceURL;// 坐标偏转服务URL
	private double x;// 经度
	private double y;// 纬度

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setXY(double x, double y) {
		this.y = y;
		this.x = x;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	// 坐标加密
	public static String enCoord(double coordDouble){
		String encoord=null;
		try{
		com.mapabc.encrptor.EncrptorFactory encrptorFactory = com.mapabc.encrptor.EncrptorFactory
				.getInstance();
		encoord= encrptorFactory.getStrFormCoordinate(coordDouble, 0);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return encoord;
		
	}

	// 坐标解密
	public static double deCoord(String encrptValue) {
		com.mapabc.encrptor.EncrptorFactory encrptorFactory = com.mapabc.encrptor.EncrptorFactory
				.getInstance();
		return encrptorFactory.getCoordinate(encrptValue);
	}

	// 坐标偏转
	public DPoint enConvert(double x, double y) throws Exception {
		this.setXY(x, y);
		DPoint rPnt = new DPoint();
		if (x < 10) {
			rPnt.x = x;
			rPnt.y = y;
			return rPnt;
		}
		String reuqestURL = getXML();
		byte[] requestData = reuqestURL.getBytes("ISO8859-1");
		byte[] rBytes = getPostURLData(serviceURL, requestData);
		int rxInt = getInt(rBytes, 0);
		int ryInt = getInt(rBytes, 4);
		rPnt.x = (double) rxInt / 1000000;
		rPnt.y = (double) ryInt / 1000000;
		return rPnt;
	}

	// 坐标反偏转，有误差
	public DPoint deConvert(double x, double y) throws Exception {
		DPoint XY = this.enConvert(x, y);
		DPoint fPoint = new DPoint();
		fPoint.x = x * 2 - XY.x;
		fPoint.y = y * 2 - XY.y;
		DPoint tPoint = this.enConvert(fPoint.x, fPoint.y);
		DPoint ret = new DPoint();
		ret.x = fPoint.x + (x - tPoint.x);
		ret.y = fPoint.y + (y - tPoint.y);
		return ret;
	}

	static int getInt(byte[] b, int startIndex) {
		int ch1 = convertbytetoint(b[3 + startIndex]);
		int ch2 = convertbytetoint(b[2 + startIndex]);
		int ch3 = convertbytetoint(b[1 + startIndex]);
		int ch4 = convertbytetoint(b[0 + startIndex]);
		int r = (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
		return r;
	}

	static int convertbytetoint(byte b) {
		return b & 0xff;

	}

	private String getXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<route>");
		sb.append("\r\n");
		sb.append("<startpoint type=1>");
		sb.append("\r\n");
		sb.append("<x>" + x + "</x>");
		sb.append("\r\n");
		sb.append("<y>" + y + "</y>");
		sb.append("\r\n");
		sb.append("</startpoint>");
		sb.append("\r\n");
		sb.append("<endpoint>");
		sb.append("\r\n");
		sb.append("</endpoint>");
		sb.append("\r\n");
		sb.append("</route>");
		sb.append("\r\n");
		return sb.toString();

	}

	// post data
	public static byte[] getPostURLData(String urlStr, byte[] postData)
			throws Exception {
		URL url = new URL(urlStr);
		URLConnection urlConn = null;
		DataOutputStream printout = null;
		DataInputStream input = null;
		java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
		try {
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			// Let the RTS know that we want to do output.
			urlConn.setDoOutput(true);
			// No caching, we want the real thing.
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConn.setRequestProperty("Content=length", String
					.valueOf(postData.length));
			printout = new DataOutputStream(urlConn.getOutputStream());
			for (int i = 0; i < postData.length; i++) {
				printout.write(postData[i]);
			}
			printout.flush();
			printout.close();
			input = new DataInputStream(urlConn.getInputStream());
			byte[] bufferByte = new byte[256];
			int l = -1;
			int downloadSize = 0;
			while ((l = input.read(bufferByte)) > -1) {
				downloadSize += l;
				bout.write(bufferByte, 0, l);
				bout.flush();
			}
			byte[] btemp = bout.toByteArray();
			input.close();
			input = null;
			return btemp;
		} catch (Exception ex) {
			throw ex;
		} finally {

		}
	}
	
	@SuppressWarnings("static-access")
	public static String getLocDescByLngLat(Double longitude, Double latitude, Logger logger){
		String locDesc = "";
		CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
		if (longitude > 0 && latitude > 0) {
			double[] xs = { longitude };
			double[] ys = { latitude };
			try {
				com.sos.sosgps.api.DPoint[] dPoint = coordApizw.encryptConvert(xs, ys);
				String lngX = dPoint[0].getEncryptX();
				String latY = dPoint[0].getEncryptY();
				// 取得位置描述
				locDesc = coordCvtApi.getAddress(lngX, latY);
			} catch (Exception ex) {
				logger.error("[getLocDescByLngLat] encryptConvert error," + ex.getMessage());
			}
		}
		return locDesc;
	}
	
	 public static void main(String[] arg){
		 com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
		 //CoordConvertAPI coordAPI= new CoordConvertAPI();
		// coordAPI.setServiceURL("http://60.247.103.59:7130/route");//设置偏转服务URL
		 String xy = "IOMQTPTQULLHL,MHGOSTMULDHHL,JIOMQTKORRHHLL,LQGWYQLQMHLHH,JIOMSSLNQPLLHL,LQGWURLRLLHL,JIOMVRJRPQLDHH,LQGXTNPPRDLHH,JIOMVRJRPQLHHH,LQGXTNPPRLLDH";
		 String[] xys = xy.split(",");
		String x = coordAPI.getAddress("JIOMQTKORRHHLL", "LQGWYQLQMHLHH");
//		   try {
//			com.sosgps.geom.DPoint p =coordAPI.encryptConvert(118.10978, 30.192662);
//			String xx =coordAPI.encrypt(p.x);
//			 String yy = coordAPI.encrypt(p.y);
//			 System.out.println(xx+","+yy);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
		 
		 
	 }
}
