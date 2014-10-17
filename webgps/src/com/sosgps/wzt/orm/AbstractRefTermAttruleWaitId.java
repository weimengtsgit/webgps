package com.sosgps.wzt.orm;

/**
 * AbstractRefTermAttruleWaitId entity provides the base persistence definition
 * of the RefTermAttruleId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefTermAttruleWaitId implements
		java.io.Serializable {

	// Fields

	private TTerminal TTerminal;
	private TAttendanceRule TAttendanceRule;

	// Constructors

	/** default constructor */
	public AbstractRefTermAttruleWaitId() {
	}

	/** full constructor */
	public AbstractRefTermAttruleWaitId(TTerminal TTerminal,
			TAttendanceRule TAttendanceRule) {
		this.TTerminal = TTerminal;
		this.TAttendanceRule = TAttendanceRule;
	}

	// Property accessors

	public TTerminal getTTerminal() {
		return this.TTerminal;
	}

	public void setTTerminal(TTerminal TTerminal) {
		this.TTerminal = TTerminal;
	}

	public TAttendanceRule getTAttendanceRule() {
		return this.TAttendanceRule;
	}

	public void setTAttendanceRule(TAttendanceRule TAttendanceRule) {
		this.TAttendanceRule = TAttendanceRule;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractRefTermAttruleId))
			return false;
		AbstractRefTermAttruleWaitId castOther = (AbstractRefTermAttruleWaitId) other;

		return ((this.getTTerminal() == castOther.getTTerminal()) || (this
				.getTTerminal() != null
				&& castOther.getTTerminal() != null && this.getTTerminal()
				.equals(castOther.getTTerminal())))
				&& ((this.getTAttendanceRule() == castOther
						.getTAttendanceRule()) || (this.getTAttendanceRule() != null
						&& castOther.getTAttendanceRule() != null && this
						.getTAttendanceRule().equals(
								castOther.getTAttendanceRule())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTTerminal() == null ? 0 : this.getTTerminal().hashCode());
		result = 37
				* result
				+ (getTAttendanceRule() == null ? 0 : this
						.getTAttendanceRule().hashCode());
		return result;
	}

}