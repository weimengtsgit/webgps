package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * RefTermAttruleWait entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefTermAttruleWait extends AbstractRefTermAttruleWait implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefTermAttruleWait() {
	}

	/** minimal constructor */
	public RefTermAttruleWait(RefTermAttruleWaitId id) {
		super(id);
	}

	/** full constructor */
	public RefTermAttruleWait(RefTermAttruleWaitId id,
			TAttendanceRule TAttendanceRule, TTerminal TTerminal,
			Long isvalid, Date modifyDate, Date inputdate) {
		super(id, TAttendanceRule, TTerminal, isvalid, modifyDate, inputdate);
	}

}
