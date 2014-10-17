/**
 * 
 */
package com.autonavi.directl.device.plp;

import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.TerminalParam;
import com.autonavi.directl.idirectl.TerminalSettingAdaptor;

/**
 * @author shiguang.zhou
 * 
 */
public class PersonalLocatorSetting extends TerminalSettingAdaptor {

	String head = "#TH100,";
	String end = "*";
	String deviceId = null;

	public PersonalLocatorSetting(TerminalParam terminalParam) {
		this.terminalParam = terminalParam;
		this.deviceId = this.terminalParam.getSeriesNo();
		head = head + "000000"+",";
		this.terminalParam.setGPRSModal(false);
	}

	public PersonalLocatorSetting() {

	}

	/**
	 * TerminalSetting.java: 发送间隔时间设置
	 * 
	 * @param intervalType
	 * 
	 * @param t
	 *            String 间隔时间
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		String ret = "";
		String time = "";
		// #TH100,000000,SP,ON,10,1*
		int interval = Integer.parseInt(t);
		time = interval + "";
		if (interval < 10) {
			time = "10";
		}
		if (interval > 1000) {
			time = "1000";
		}

		String cmd = "SP,ON," + time + ",1";
		ret = head + cmd + end;
		return ret;

	}

	/**
	 * 设置快捷拨号号码
	 */
	public String setCallLimitMode(String seq, String[] nums) {
		String ret = "";
		// #TH100,000000,PN,13910009001,SOS,13910010020,Mama,13910010021,Davi,13911018898,Father,100869080,Center,13390809789,School*
		String cmd = "PN,";
		;

		for (int i = 0; i < nums.length; i++) {
			if (i == nums.length - 1) {
				cmd += nums[i] + ",num" + i;
			} else {
				cmd += nums[i] + ",num" + i + ",";
			}

		}
		ret = head + cmd + end;
		return ret;
	}

	public String changeMode(String seq, String pMode) {
		String ret = "";

		return ret;
	}

	// 设置电量不足报告
	public String startSaveMode(String seq, String v) {
		// #TH100,000000,PS,ON*
		String ret = null;
		String cmd = "PS,ON";
		ret = head + cmd + end;
		return ret;
	}

}
