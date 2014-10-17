package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.Set;

import com.sun.java.swing.plaf.motif.resources.motif;

/**
 * TAttendanceRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TAttendanceRuleWait extends AbstractTAttendanceRuleWait implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TAttendanceRuleWait() {
	}

	/** minimal constructor */
	public TAttendanceRuleWait(String name, String effectPeriod,
			String ruleType, String ruleContent, Long interval, Long isvalid,
			Date modifyDate) {
		super(name, effectPeriod, ruleType, ruleContent, interval, isvalid,
				modifyDate);
	}

	/** full constructor */
	public TAttendanceRuleWait(TAttendanceArea TAttendanceArea, String name,
			String effectPeriod, String ruleType, String ruleContent,
			Long interval, String entCode, Date inuptdate, String ruleDesc,
			Set TAttendanceLocrecords, Set refTermAttruleWaits, Long isvalid,
			Date modifyDate) {
		super(TAttendanceArea, name, effectPeriod, ruleType, ruleContent,
				interval, entCode, inuptdate, ruleDesc, TAttendanceLocrecords,
				refTermAttruleWaits, isvalid, modifyDate);
	}

}
