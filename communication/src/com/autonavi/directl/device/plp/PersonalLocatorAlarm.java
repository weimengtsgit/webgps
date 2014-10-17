/**
 * 
 */
package com.autonavi.directl.device.plp;

import java.text.NumberFormat;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.AlarmAdapter;
import com.autonavi.directl.idirectl.TerminalParam;

/**
 * @author shiguang.zhou
 *
 */
public class PersonalLocatorAlarm extends AlarmAdapter {
	String head = "#TH100,";
	String end = "*";
	String deviceId= null;
	
	public PersonalLocatorAlarm(TerminalParam terminalParam){
		this.terminalParam = terminalParam;
		this.deviceId = this.terminalParam.getSeriesNo();
		head = head + this.terminalParam.getProtocolPwd()+",";
		this.terminalParam.setGPRSModal(false);
	}
	
	public PersonalLocatorAlarm(){}
	
	public  String setCicleArea(String seq, String type, String x, String y, String r){
		String ret = "";
		
		
		return ret;
		
	}
	
}


