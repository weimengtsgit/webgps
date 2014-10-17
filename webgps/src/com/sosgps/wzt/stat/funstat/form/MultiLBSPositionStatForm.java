/**
 * 
 */
package com.sosgps.wzt.stat.funstat.form;

/**
 * @author xiaojun.luan
 * 
 */
public class MultiLBSPositionStatForm extends TimeStatForm {
	private String startDate;
	private String endDate;
	private String deviceIds;
	
	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
