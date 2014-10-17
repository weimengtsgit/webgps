package com.sosgps.wzt.orm;

/**
 * RefTermAttrule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefTermAttrule extends AbstractRefTermAttrule implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefTermAttrule() {
	}

	/** minimal constructor */
	public RefTermAttrule(RefTermAttruleId id) {
		super(id);
	}

	/** full constructor */
	public RefTermAttrule(RefTermAttruleId id, TAttendanceRule TAttendanceRule,
			TTerminal TTerminal) {
		super(id, TAttendanceRule, TTerminal);
	}

}
