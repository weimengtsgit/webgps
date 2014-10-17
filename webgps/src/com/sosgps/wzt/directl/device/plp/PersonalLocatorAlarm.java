/**
 * 
 */
package com.sosgps.wzt.directl.device.plp;

import java.text.NumberFormat;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.AlarmAdapter;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

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
		head = head + "000000"+",";
		this.terminalParam.setGPRSModal(false);
	}
	
	public PersonalLocatorAlarm(){}
	
	public  String setCicleArea(String seq, String type, String x, String y, String r){
		String ret = "";
		//#TH100,000000,RM,M,100, N24.123456,E121.123456*
		if (type.equals("2")){
			//#TH100,000000,RM,OFF* 
			ret = head + "RM,OFF"+end;
			
		}else {
			ret = head+"RM,M,"+r+",N"+y+",E"+x+end;
		}
		return ret;
		
	}
	
}


