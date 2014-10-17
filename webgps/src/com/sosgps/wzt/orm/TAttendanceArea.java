package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.Set;

/**
 * TAttendanceArea entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TAttendanceArea extends AbstractTAttendanceArea implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TAttendanceArea() {
	}

	/** full constructor */
	public TAttendanceArea(String name, String points, Date createTime,
			String entCode, Set TAttendanceRules) {
		super(name, points, createTime, entCode, TAttendanceRules);
	}

}
