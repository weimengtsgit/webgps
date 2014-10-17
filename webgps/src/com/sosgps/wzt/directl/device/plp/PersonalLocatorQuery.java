/**
 * 
 */
package com.sosgps.wzt.directl.device.plp;

import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalQueryAdaptor;

/**
 * @author shiguang.zhou
 *
 */
public class PersonalLocatorQuery extends TerminalQueryAdaptor {

	String head = "#TH100,";
	String end = "*";
	String deviceId = null;
	
	public PersonalLocatorQuery(TerminalParam terminalParam) {
		this.terminalParam = terminalParam;
		this.deviceId = this.terminalParam.getSeriesNo();
		head = head + this.terminalParam.getProtocolPwd()+",";
		this.terminalParam.setGPRSModal(false);
	}
	
	public PersonalLocatorQuery() {

	}
	
	public String getStateParam(){
		String ret = null;
		
		return ret;
	}
	
}
