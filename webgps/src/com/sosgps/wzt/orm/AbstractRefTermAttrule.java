package com.sosgps.wzt.orm;

/**
 * AbstractRefTermAttrule entity provides the base persistence definition of the
 * RefTermAttrule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefTermAttrule implements java.io.Serializable {

	// Fields

	private RefTermAttruleId id;
	private TAttendanceRule TAttendanceRule;
	private TTerminal TTerminal;

	// Constructors

	/** default constructor */
	public AbstractRefTermAttrule() {
	}

	/** minimal constructor */
	public AbstractRefTermAttrule(RefTermAttruleId id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractRefTermAttrule(RefTermAttruleId id,
			TAttendanceRule TAttendanceRule, TTerminal TTerminal) {
		this.id = id;
		this.TAttendanceRule = TAttendanceRule;
		this.TTerminal = TTerminal;
	}

	// Property accessors

	public RefTermAttruleId getId() {
		return this.id;
	}

	public void setId(RefTermAttruleId id) {
		this.id = id;
	}

	public TAttendanceRule getTAttendanceRule() {
		return this.TAttendanceRule;
	}

	public void setTAttendanceRule(TAttendanceRule TAttendanceRule) {
		this.TAttendanceRule = TAttendanceRule;
	}

	public TTerminal getTTerminal() {
		return this.TTerminal;
	}

	public void setTTerminal(TTerminal TTerminal) {
		this.TTerminal = TTerminal;
	}

}