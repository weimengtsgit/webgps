/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

import com.sosgps.wzt.orm.TLbsLocrecord;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;

/**
 * @author xiaojun.luan
 * 
 */
public class MultiLBSPositionStatBean {
	private TTerminal tTerminal;
	private TLbsLocrecord tLbsLocrecord;
	private TTermGroup tTermGroup;
	private String locDesc;



	public MultiLBSPositionStatBean(TTerminal tTerminal,
			TLbsLocrecord tLbsLocrecord,TTermGroup tTermGroup) {
		this.tLbsLocrecord = tLbsLocrecord;
		this.tTerminal = tTerminal;
		this.tTermGroup=tTermGroup;
	}

	public TLbsLocrecord getTLbsLocrecord() {
		return tLbsLocrecord;
	}

	public void setTLbsLocrecord(TLbsLocrecord lbsLocrecord) {
		tLbsLocrecord = lbsLocrecord;
	}

	public TTerminal getTTerminal() {
		return tTerminal;
	}

	public void setTTerminal(TTerminal terminal) {
		tTerminal = terminal;
	}

	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}

	public TTermGroup getTTermGroup() {
		return tTermGroup;
	}

	public void setTTermGroup(TTermGroup termGroup) {
		tTermGroup = termGroup;
	}
}
