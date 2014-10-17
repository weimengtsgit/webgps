/**
 * 
 */
package com.autonavi.lbsgateway.service.update;

import java.io.Serializable;
import java.util.Date;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
 
import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.bean.TerminalUDPAddress;

/**
 * @author shiguang.zhou
 * 
 */
public class UpdateBean implements Serializable {

	private String deviceId;

	private int curPackNo;

	private int totalPackNum;

	private byte[] packCont;

	private Date date;

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return this.deviceId;
	}

	/**
	 * @return the curPackNo
	 */
	public int getCurPackNo() {
		return this.curPackNo;
	}

	/**
	 * @return the totalPackNum
	 */
	public int getTotalPackNum() {
		return this.totalPackNum;
	}

	/**
	 * @return the packCont
	 */
	public byte[] getPackCont() {
		return this.packCont;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @param curPackNo
	 *            the curPackNo to set
	 */
	public void setCurPackNo(int curPackNo) {
		this.curPackNo = curPackNo;
	}

	/**
	 * @param totalPackNum
	 *            the totalPackNum to set
	 */
	public void setTotalPackNum(int totalPackNum) {
		this.totalPackNum = totalPackNum;
	}

	/**
	 * @param packCont
	 *            the packCont to set
	 */
	public void setPackCont(byte[] packCont) {
		this.packCont = packCont;
	}

	public synchronized void checkIsResponse() {
		Date preDate = this.getDate();
		if (preDate == null)
			return;

		Date curDate = new Date();
		String interval = Config.getInstance().getString("checkUpdateTime");
		String ivl = ((interval == null) || (interval.trim().length() <= 0)) ? "5"
				: interval;
		int val = Integer.parseInt(ivl);

		if (curDate.getTime() - preDate.getTime() > val * 1000) {
			 
			GPRSThreadList udpObjList = GPRSThreadList.getInstance();
			TerminalUDPAddress udp = udpObjList.getGpsThreadBySim(this
					.getDeviceId());
			if (udp != null) {
				 
 

			}
//			this.setDate(curDate);
//			RemoteUpdateList.getInstance().addRemoteProgram(
//					this.getDeviceId(), this);
		}

//		this.setDate(new Date(System.currentTimeMillis()+10*1000));
//		this.setCurPackNo(curPackNo + 1);
//		 
//		RemoteUpdateList.getInstance().addRemoteProgram(this.getDeviceId(),
//				this);
	}

	 
	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {
		String ret = "";
		ret = "dsn=" + this.deviceId + ",total=" + this.totalPackNum
				+ ",curPNO=" + this.curPackNo + ",date="
				+ Tools.formatDate2Str(this.getDate(), "yyyy-MM-dd HH:mm:ss");

		return ret;
	}

}
