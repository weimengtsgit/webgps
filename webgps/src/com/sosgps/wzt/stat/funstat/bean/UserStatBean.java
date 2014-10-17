/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

/**
 * @author xiaojun.luan
 * 
 */
public class UserStatBean {
	private Long lbsPersonalCount;
	private Long gpsPersonalCount;
	private Long lbsEnterPriseCount;
	private Long gpsEnterPriseCount;
	private String subCompany;


	public Long getLbsPersonalCount() {
		return lbsPersonalCount;
	}

	public void setLbsPersonalCount(Long lbsPersonalCount) {
		this.lbsPersonalCount = lbsPersonalCount;
	}

	public Long getGpsPersonalCount() {
		return gpsPersonalCount;
	}

	public void setGpsPersonalCount(Long gpsPersonalCount) {
		this.gpsPersonalCount = gpsPersonalCount;
	}

	public Long getLbsEnterPriseCount() {
		return lbsEnterPriseCount;
	}

	public void setLbsEnterPriseCount(Long lbsEnterPriseCount) {
		this.lbsEnterPriseCount = lbsEnterPriseCount;
	}

	public Long getGpsEnterPriseCount() {
		return gpsEnterPriseCount;
	}

	public void setGpsEnterPriseCount(Long gpsEnterPriseCount) {
		this.gpsEnterPriseCount = gpsEnterPriseCount;
	}

	public String getSubCompany() {
		return subCompany;
	}

	public void setSubCompany(String subCompany) {
		this.subCompany = subCompany;
	}
}
