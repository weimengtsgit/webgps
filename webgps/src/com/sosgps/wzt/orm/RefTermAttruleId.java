package com.sosgps.wzt.orm;

/**
 * RefTermAttruleId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefTermAttruleId extends AbstractRefTermAttruleId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefTermAttruleId() {
	}

	/** full constructor */
	public RefTermAttruleId(TTerminal TTerminal, TAttendanceRule TAttendanceRule) {
		super(TTerminal, TAttendanceRule);
	}

}
