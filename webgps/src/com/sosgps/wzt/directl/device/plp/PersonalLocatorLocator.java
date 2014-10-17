/**
 * 
 */
package com.sosgps.wzt.directl.device.plp;

import com.sosgps.wzt.directl.idirectl.LocatorAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

/**
 * @author shiguang.zhou
 * 
 */
public class PersonalLocatorLocator extends LocatorAdaptor {

	String head = "#TH100,";
	String end = "*";
	String deviceId = null;

	public PersonalLocatorLocator(TerminalParam terminalParam) {
		this.terminalParam = terminalParam;
		this.deviceId = this.terminalParam.getSeriesNo();
		head = head + this.terminalParam.getProtocolPwd()+",";
		this.terminalParam.setGPRSModal(false);
	}

	public PersonalLocatorLocator() {

	}

	/**
	 * 设置按距离定位
	 * 
	 * @param distance
	 * @return
	 */
	public String setLocatorByDistance(String seq, String distance) {
		// #TH100,000000,RM,M,100*
		int d = Integer.parseInt(distance);
		String ret = null;
		String dis = null;
		if (distance != null && distance.trim().length() > 0) {
			dis = distance + "";
		}
		if (d < 55) {
			dis = "55";
		}
		if (d > 9999) {
			dis = "9999";
		}
		String cmd = "RM,M," + distance;
		ret = head + cmd + end;
		return ret;
	}

	//单次定位请求
	public String singleLocator(String seq) {
		String ret = "";
		// #TH100,000000,RP*
		ret = head + "RP" + end;
		return ret;
	}

}
