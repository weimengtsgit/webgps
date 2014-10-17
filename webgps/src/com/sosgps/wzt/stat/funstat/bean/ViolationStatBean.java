package com.sosgps.wzt.stat.funstat.bean;

import com.sosgps.wzt.orm.TJbWfdm;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TYwClwfSdall;

/**
 * @Title:
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2009-9-4 ÏÂÎç06:52:55
 */
public class ViolationStatBean {
	private TTerminal tTerminal;
	private TTermGroup tTermGroup;
	private TYwClwfSdall ywClwfSdall;
	private TJbWfdm jbWfdm;

	public TJbWfdm getJbWfdm() {
		return jbWfdm;
	}

	public void setJbWfdm(TJbWfdm jbWfdm) {
		this.jbWfdm = jbWfdm;
	}

	public TYwClwfSdall getYwClwfSdall() {
		return ywClwfSdall;
	}

	public void setYwClwfSdall(TYwClwfSdall ywClwfSdall) {
		this.ywClwfSdall = ywClwfSdall;
	}

	public ViolationStatBean(TTerminal terminal, TTermGroup termGroup,
			TYwClwfSdall clwfSdall, TJbWfdm wfdm) {
		super();
		tTerminal = terminal;
		tTermGroup = termGroup;
		ywClwfSdall = clwfSdall;
		jbWfdm = wfdm;
	}

	public TTerminal getTTerminal() {
		return tTerminal;
	}

	public void setTTerminal(TTerminal terminal) {
		tTerminal = terminal;
	}

	public TTermGroup getTTermGroup() {
		return tTermGroup;
	}

	public void setTTermGroup(TTermGroup termGroup) {
		tTermGroup = termGroup;
	}

}
