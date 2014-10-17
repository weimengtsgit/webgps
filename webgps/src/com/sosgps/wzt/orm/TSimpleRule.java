package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.Set;

/**
 * TSimpleRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TSimpleRule extends AbstractTSimpleRule implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TSimpleRule() {
	}

	/** minimal constructor */
	public TSimpleRule(String name, String ruleType, String effectPeriod,
			String ruleContent, Long interval, String entCode) {
		super(name, ruleType, effectPeriod, ruleContent, interval, entCode);
	}

	/** full constructor */
	public TSimpleRule(String name, String ruleType, String effectPeriod,
			String ruleContent, Long interval, String entCode, Date inputdate,
			String ruleDesc, Set refTermSimrules) {
		super(name, ruleType, effectPeriod, ruleContent, interval, entCode,
				inputdate, ruleDesc, refTermSimrules);
	}

}
