package com.sosgps.wzt.orm;

/**
 * AbstractRefTermSimrule entity provides the base persistence definition of the
 * RefTermSimrule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefTermSimrule implements java.io.Serializable {

	// Fields

	private RefTermSimruleId id;
	private TSimpleRule TSimpleRule;
	private TTerminal TTerminal;

	// Constructors

	/** default constructor */
	public AbstractRefTermSimrule() {
	}

	/** minimal constructor */
	public AbstractRefTermSimrule(RefTermSimruleId id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractRefTermSimrule(RefTermSimruleId id, TSimpleRule TSimpleRule,
			TTerminal TTerminal) {
		this.id = id;
		this.TSimpleRule = TSimpleRule;
		this.TTerminal = TTerminal;
	}

	// Property accessors

	public RefTermSimruleId getId() {
		return this.id;
	}

	public void setId(RefTermSimruleId id) {
		this.id = id;
	}

	public TSimpleRule getTSimpleRule() {
		return this.TSimpleRule;
	}

	public void setTSimpleRule(TSimpleRule TSimpleRule) {
		this.TSimpleRule = TSimpleRule;
	}

	public TTerminal getTTerminal() {
		return this.TTerminal;
	}

	public void setTTerminal(TTerminal TTerminal) {
		this.TTerminal = TTerminal;
	}

}