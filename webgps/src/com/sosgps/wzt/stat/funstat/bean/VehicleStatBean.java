/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

import com.sosgps.wzt.orm.TLocrecord;

/**
 * @author xiaojun.luan
 *
 */
public class VehicleStatBean extends TLocrecord {
	private String showTime;
	private String locDesc;
	private String termName;
	
	
	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}
}
