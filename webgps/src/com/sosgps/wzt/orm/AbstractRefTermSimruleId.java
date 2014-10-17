package com.sosgps.wzt.orm;

/**
 * AbstractRefTermSimruleId entity provides the base persistence definition of
 * the RefTermSimruleId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefTermSimruleId implements java.io.Serializable {

	// Fields

	private TTerminal TTerminal;
	private TSimpleRule TSimpleRule;

	// Constructors

	/** default constructor */
	public AbstractRefTermSimruleId() {
	}

	/** full constructor */
	public AbstractRefTermSimruleId(TTerminal TTerminal, TSimpleRule TSimpleRule) {
		this.TTerminal = TTerminal;
		this.TSimpleRule = TSimpleRule;
	}

	// Property accessors

	public TTerminal getTTerminal() {
		return this.TTerminal;
	}

	public void setTTerminal(TTerminal TTerminal) {
		this.TTerminal = TTerminal;
	}

	public TSimpleRule getTSimpleRule() {
		return this.TSimpleRule;
	}

	public void setTSimpleRule(TSimpleRule TSimpleRule) {
		this.TSimpleRule = TSimpleRule;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractRefTermSimruleId))
			return false;
		AbstractRefTermSimruleId castOther = (AbstractRefTermSimruleId) other;

		return ((this.getTTerminal() == castOther.getTTerminal()) || (this
				.getTTerminal() != null
				&& castOther.getTTerminal() != null && this.getTTerminal()
				.equals(castOther.getTTerminal())))
				&& ((this.getTSimpleRule() == castOther.getTSimpleRule()) || (this
						.getTSimpleRule() != null
						&& castOther.getTSimpleRule() != null && this
						.getTSimpleRule().equals(castOther.getTSimpleRule())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTTerminal() == null ? 0 : this.getTTerminal().hashCode());
		result = 37
				* result
				+ (getTSimpleRule() == null ? 0 : this.getTSimpleRule()
						.hashCode());
		return result;
	}

}