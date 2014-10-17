package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.Set;

/**
 * TAttendanceRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TAttendanceRule extends AbstractTAttendanceRule implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TAttendanceRule() {
	}

	/** minimal constructor */
	public TAttendanceRule(String name, String effectPeriod, String ruleType,
			String ruleContent, Long interval, Long isvalid, Date modifyDate) {
		super(name, effectPeriod, ruleType, ruleContent, interval, isvalid,
				modifyDate);
	}

	/** full constructor */
	public TAttendanceRule(TAttendanceArea TAttendanceArea, String name,
			String effectPeriod, String ruleType, String ruleContent,
			Long interval, String entCode, Date inputdate, String ruleDesc,
			 Set refTermAttrules, Long isvalid,
			Date modifyDate) {
		super(TAttendanceArea, name, effectPeriod, ruleType, ruleContent,
				interval, entCode, inputdate, ruleDesc, 
				refTermAttrules, isvalid, modifyDate);
	}

}
