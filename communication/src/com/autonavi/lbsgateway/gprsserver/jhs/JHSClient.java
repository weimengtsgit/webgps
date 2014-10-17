package com.autonavi.lbsgateway.gprsserver.jhs;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
 

/**
 * 连接交互式导航服务器的客户端,负责接收数据与下发任务
 * @author jiang
 *
 */
public class JHSClient extends Clientable{
	LbsClient lbsClient;
	public JHSClient(String clientName, String host, int port) {
		super(clientName, host, port);
	}
	
	public void setLbsClinet(LbsClient lbsclient){
		this.lbsClient = lbsclient;
	}

	//下发任务
	public void sentTask(byte[] data){
		this.sentDataBytes(data);
	}
	
	
	//接收数据,并且转发给位置网关
	@Override
	public void process(String xml) {   
	    String[] datas= xml.split("</mapabc>");
	    for(int i=0;i < datas.length;i++){
	    	String temp= "baju"+datas[i]+"</mapabc>";
	    	this.sentToLbsgateWay(temp);
	    }
	}
	
	private void sentToLbsgateWay(String gpsdate){
		this.lbsClient.sentDataString(gpsdate);
	}
	
	private void sentToLbsgateWay2(String data){
		 Socket socket = null;
		 OutputStream oos = null;
		 BufferedInputStream is;
		 PrintWriter os = null;
		 String ip= Config.getInstance().getString("IP");	
	     int port=Integer.parseInt(Config.getInstance().getString("GPRSPORT"));
	     boolean isConnect=false;
	     while(!isConnect){
		     try{
				 socket = new Socket(ip, port);
				 is = new BufferedInputStream(socket.getInputStream());
				 oos = socket.getOutputStream();
				 os = new PrintWriter(oos);
				 isConnect =true;
		     }catch (Exception ex){
		    	 Log.getInstance().outJHSLoger("JHSClient 下发数据到网关,连接到位置网关失败,进行重新连接...");
		    	 try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }	    	 
	     }
		    try {
			      os.write(data);
			      os.flush();
			 }
			 catch (Exception ex) {
			      Log.getInstance().outJHSLoger("发送到位置网关数据:"+data+"失败,原因:"+ex.getMessage());
			 }	 
	}
	
	
	
	
}
