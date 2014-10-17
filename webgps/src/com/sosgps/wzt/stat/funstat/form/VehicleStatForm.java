/**
 * 
 */
package com.sosgps.wzt.stat.funstat.form;

/**
 * @author xiaojun.luan
 *
 */
public class VehicleStatForm extends TimeStatForm {
	private String intervalTime;
	private String startDate;
	private String endDate;
	
	
	public String getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
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
