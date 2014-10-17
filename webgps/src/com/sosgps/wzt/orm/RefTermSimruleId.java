package com.sosgps.wzt.orm;

/**
 * RefTermSimruleId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefTermSimruleId extends AbstractRefTermSimruleId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefTermSimruleId() {
	}

	/** full constructor */
	public RefTermSimruleId(TTerminal TTerminal, TSimpleRule TSimpleRule) {
		super(TTerminal, TSimpleRule);
	}

}
