/**
 * 
 */
package com.sosgps.wzt.area.form;

import org.apache.struts.action.ActionForm;

/**
 * @author shiguang.zhou
 *
 */
public class AreaForm extends ActionForm {
	
	private String deviceId;
	private String xy;
	private String epcode;
	private String desc;
	
	public String getDeviceId() {
		return this.deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getXy() {
		return this.xy;
	}
	public void setXy(String xy) {
		this.xy = xy;
	}
	public String getEpcode() {
		return this.epcode;
	}
	public void setEpcode(String epcode) {
		this.epcode = epcode;
	}
	public String getDesc() {
		return this.desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

}
