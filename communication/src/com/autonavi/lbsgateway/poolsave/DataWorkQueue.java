package com.autonavi.lbsgateway.poolsave;

import java.util.*;

import java.text.SimpleDateFormat;

import com.autonavi.directl.Log;
import com.autonavi.directl.parse.ParseBase;

class DataWorkQueue {
	private LinkedList work;

	private int maxBatch = 200;

	private DataPool dataPool = null;

	public DataWorkQueue(DataPool dataPool) {
		this.dataPool = dataPool;
		work = new LinkedList();
	}

	public synchronized void addWork(ParseBase task) {
 		work.add(task);
		notify();
 
	}

	/**
	 * 保存到文件 格式为:手机号^原始经度^原始纬度^系统时间^GPS时间^.......
	 * 
	 * @param pb
	 *            ParseBase
	 */
	private void saveToFile(ParseBase pb) {
		String data = "";
		SimpleDateFormat simpleDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = simpleDate.format(Calendar.getInstance().getTime());
		String split = "^";
		data = pb.getPhnum() + split;
		data += pb.getCoordX() + split;
		data += pb.getCoordY() + split;
		data += date + split;
		data += pb.getTime() + split;
		data += pb.getSpeed() + split;
		data += pb.getDirection() + split;
		data += pb.getLC() + split;
		data += pb.getPicURL() + split;
		data += pb.getAlarmDesc() + split;
		data += pb.getAltitude() + split;
		data += pb.getSatellites() + split;
		data += pb.getStatus() + split;
		data += pb.getAdress() + split;
		data += pb.getExtend1() + split;
		data += pb.getExtend2() + split;
		data += pb.getExtend3();
		// Log.getInstance().outGPSData(data);
	}

	public synchronized ArrayList getWork() throws InterruptedException {
		// Log.getInstance().outLog("There has "+work.size()+" Data in DataPool
		// , waitting. ");
		if (!dataPool.isTimeUp()) {
			while (work.isEmpty() || work.size() < maxBatch) { //
				try {
					wait();
				} catch (InterruptedException ie) {
					throw ie;
				}
			}
		}
		ArrayList ls = new ArrayList();
		 
		for (int i = 0; i < work.size(); i++) {
			ls.add(work.get(i));
		}
 		 
		 work = new LinkedList();
		dataPool.setTimeUp(false);

		return ls;
	}
}
