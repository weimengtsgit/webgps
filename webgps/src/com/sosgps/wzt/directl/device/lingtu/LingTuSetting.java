package com.sosgps.wzt.directl.device.lingtu;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalQueryAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;

import java.nio.ByteBuffer;
import java.io.*;

public class LingTuSetting extends TerminalSettingAdaptor {
	public LingTuSetting() {
	}

	String head = "C M ";
	String oemcode = "";
	String deviceid = "";

	public LingTuSetting(TerminalParam tparam) {
		this.terminalParam = tparam;
		this.oemcode = tparam.getOemCode();
		this.deviceid = tparam.getSeriesNo();
	}

	private String getIp(String ip) {
		String temp = ip;
		if (temp != null && temp.trim().length() != 0) {
			while (temp.length() < 3) {
				temp = "0" + temp;
			}
		}
		return temp;
	}

	public String setIPPort(String serverIP, String serverPort, String reDial) {
		// （RPM,13512345678,202105138021,11000,2048,T,3,01,,）
		String content = "";

		return null;
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
		String cnt = "";
		int tt = Integer.parseInt(t);
		time = Integer.toHexString(tt).toUpperCase();
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));
		 
		head = head + seq + " ";
		//time = Integer.toHexString(Integer.parseInt(t));
		String cmd = this.oemcode + ":" + this.deviceid + "|5|0;" + time + "!"
				+ time + "; ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
		//（C M c 4C54:1001|60|1 38B）
//		String ret = "";
//        head = head+seq+" ";
//        String cmd =  this.oemcode+":"+this.deviceid+"|60|2 ";
//        String vcode = Tools.getVerfyCode(cmd);
//        ret = head + cmd + vcode;
//        return ret;

	}

	public static void main(String[] args) {
		LingTuSetting s = new LingTuSetting();

	}

	public String setAPN(String v) {
		String ret = "";

		return ret;
	}

	private String convertToHex(String num, int n) {
		String temp = "";
		int i = Integer.parseInt(num);
		String hex = Integer.toHexString(i).toUpperCase();
		if (hex.length() > n) {
			int off = 0;
			while (off < n) {
				temp = temp + "F";
				off++;
			}
			return temp;
		} else if (hex.length() < n) {
			while (hex.length() < n) {
				hex = "0" + hex;
			}
			temp = hex;
		} else {
			temp = hex;
		}
		return temp;
	}
}
