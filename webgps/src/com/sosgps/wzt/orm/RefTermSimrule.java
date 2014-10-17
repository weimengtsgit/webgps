package com.sosgps.wzt.orm;

/**
 * RefTermSimrule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefTermSimrule extends AbstractRefTermSimrule implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefTermSimrule() {
	}

	/** minimal constructor */
	public RefTermSimrule(RefTermSimruleId id) {
		super(id);
	}

	/** full constructor */
	public RefTermSimrule(RefTermSimruleId id, TSimpleRule TSimpleRule,
			TTerminal TTerminal) {
		super(id, TSimpleRule, TTerminal);
	}

}
