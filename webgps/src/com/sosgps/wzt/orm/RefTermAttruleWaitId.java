package com.sosgps.wzt.orm;

/**
 * RefTermAttruleWaitId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefTermAttruleWaitId extends AbstractRefTermAttruleWaitId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefTermAttruleWaitId() {
	}

	/** full constructor */
	public RefTermAttruleWaitId(TTerminal TTerminal, TAttendanceRule TAttendanceRule) {
		super(TTerminal, TAttendanceRule);
	}

}
