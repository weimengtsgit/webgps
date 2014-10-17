/**
 * 
 */
package com.sosgps.wzt.stat.alarmstat.bean;

import java.util.Date;

import com.sosgps.wzt.orm.TAlarmShow;
import com.sosgps.wzt.orm.TAreaLocrecord;
import com.sosgps.wzt.orm.TSpeedCase;
import com.sosgps.wzt.orm.TTerminal;

/**
 * @author xiaojun.luan
 * 
 */
public class AlarmShowBean {
	private TAlarmShow tAlarmShow;
	private TTerminal tTerminal;
	private TAreaLocrecord tAreaLocrecord;
	private TSpeedCase tSpeedCase;

	public TAlarmShow getTAlarmShow() {
		return tAlarmShow;
	}

	public void setTAlarmShow(TAlarmShow alarmShow) {
		tAlarmShow = alarmShow;
	}

	public TTerminal getTTerminal() {
		return tTerminal;
	}

	public void setTTerminal(TTerminal terminal) {
		tTerminal = terminal;
	}

	public TAreaLocrecord getTAreaLocrecord() {
		return tAreaLocrecord;
	}

	public void setTAreaLocrecord(TAreaLocrecord areaLocrecord) {
		tAreaLocrecord = areaLocrecord;
	}

	public TSpeedCase getTSpeedCase() {
		return tSpeedCase;
	}

	public void setTSpeedCase(TSpeedCase speedCase) {
		tSpeedCase = speedCase;
	}
}
