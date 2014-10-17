/**
 * 
 */
package com.autonavi.lbsgateway;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;

/**
 * @author shiguang.zhou
 *
 */
public class TcpLinkCache {
	
	static TcpLinkCache instance = null;
	
	private ConcurrentHashMap<Socket, Date> tcpAllCache = new ConcurrentHashMap<Socket, Date>();
 
	public synchronized static TcpLinkCache getInstance() {
		if (instance == null) {
			instance = new TcpLinkCache();
		}
		return instance;
	}

 

	/**
	 * @param tcpCache the tcpCache to set
	 */
	public  void addToTcpCache(Socket socket,Date date) {
		if (this.tcpAllCache.get(socket) != null){			
			Log.getInstance().tcpLinkLoger("����tcp��·ʱ�䣺"+socket.getRemoteSocketAddress().toString()+",new date:"+Tools.formatDate2Str(date, "yyyy-MM-dd HH:mm:ss"));			
		}else{
			Log.getInstance().tcpLinkLoger("tcp����������·��"+socket.getRemoteSocketAddress().toString()+",new date:"+Tools.formatDate2Str(date, "yyyy-MM-dd HH:mm:ss"));
		}
		this.tcpAllCache.put(socket, date);
		Log.getInstance().tcpLinkLoger("tcp������·������"+this.tcpAllCache.size());

	}
 
	public void checkTcpCache(){

		Date curDate = new Date();
		Calendar curcal = Calendar.getInstance();
		curcal.setTime(curDate);

		Set tmpTcpSet = this.tcpAllCache.entrySet();
		List keyList = new ArrayList(tmpTcpSet);
		Iterator iterator = keyList.iterator();
		Calendar conncal = Calendar.getInstance();
 		
		while (iterator.hasNext()) {
			try {
				Map.Entry met = (Map.Entry) iterator.next();
				Socket client =(Socket)  met.getKey();
				Date indate = (Date) met.getValue();
				 
				conncal.setTime(indate);
				conncal.add(Calendar.MINUTE, 5);
				
				Log.getInstance().tcpLinkLoger(
						client.getRemoteSocketAddress()
								+ " tcpcache socket old  time:"
								+ Tools.formatDate2Str(indate,
										"yyyy-MM-dd HH:mm:ss")
								+ ",tcpcache socket new  time:"
								+ Tools.formatDate2Str(curDate,
										"yyyy-MM-dd HH:mm:ss") + ",ʱ��"
								+ (curDate.getTime() - indate.getTime())
								/ 1000 + " �롣");

				if (curcal.compareTo(conncal) > 0) {// tcpTime������������������б����޳�
					
					this.tcpAllCache.remove(client);
					
					if (client != null && !client.isClosed()){	
						Log.getInstance().tcpLinkLoger(client.getRemoteSocketAddress()+" ��tcpcache����5���Ӳ�����ӻ�����ɾ�����رո���·��");
						client.close();
					}
				
					
				}

			} catch (Exception e) {
				Log.getInstance().errorLog("check tcplink error", e);
			}
		}

	}
	
	public static void main(String[] args){
		  ConcurrentHashMap<Socket, Date> tcpAllCache = new ConcurrentHashMap<Socket, Date>();
		  Socket socket = new Socket();
		  try {
			socket.setSoTimeout(300);
			tcpAllCache.put(socket, new Date());
			Thread.sleep(5000);
			socket.setSoTimeout(800);
			tcpAllCache.put(socket, new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		
	}
	

}
