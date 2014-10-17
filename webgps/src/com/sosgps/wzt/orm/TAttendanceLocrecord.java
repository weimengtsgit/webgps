package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * TAttendanceLocrecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TAttendanceLocrecord extends AbstractTAttendanceLocrecord
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public TAttendanceLocrecord() {
	}

	/** full constructor */
	public TAttendanceLocrecord(Long attRuleId,
			Date inputdate, String deviceId, Float x, Float y, String entCode,
			String locDesc, String attendanceResult, Long locId, String jmx,
			String jmy, Date attTime) {
		super(attRuleId, inputdate, deviceId, x, y, entCode, locDesc,
				attendanceResult, locId, jmx, jmy, attTime);
	}

}
