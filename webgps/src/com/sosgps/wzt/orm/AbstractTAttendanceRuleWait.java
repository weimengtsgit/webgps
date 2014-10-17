package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractTAttendanceRule entity provides the base persistence definition of
 * the TAttendanceRuleWait entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTAttendanceRuleWait implements java.io.Serializable {

	// Fields

	private Long id;
	private TAttendanceArea TAttendanceArea;
	private String name;
	private String effectPeriod;
	private String ruleType;
	private String ruleContent;
	private Long interval;
	private String entCode;
	private Date inuptdate;
	private String ruleDesc;
	private Set TAttendanceLocrecords = new HashSet(0);
	private Set refTermAttruleWaits = new HashSet(0);
	private Long isvalid;
	private Date modifyDate;

	// Constructors

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(Long isvalid) {
		this.isvalid = isvalid;
	}

	/** default constructor */
	public AbstractTAttendanceRuleWait() {
	}

	/** minimal constructor */
	public AbstractTAttendanceRuleWait(String name, String effectPeriod,
			String ruleType, String ruleContent, Long interval, Long isvalid,Date modifyDate) {
		this.name = name;
		this.effectPeriod = effectPeriod;
		this.ruleType = ruleType;
		this.ruleContent = ruleContent;
		this.interval = interval;
		this.isvalid = isvalid;
		this.modifyDate = modifyDate;
	}

	/** full constructor */
	public AbstractTAttendanceRuleWait(TAttendanceArea TAttendanceArea,
			String name, String effectPeriod, String ruleType,
			String ruleContent, Long interval, String entCode, Date inuptdate,
			String ruleDesc, Set TAttendanceLocrecords, Set refTermAttruleWaits, Long isvalid,Date modifyDate) {
		this.TAttendanceArea = TAttendanceArea;
		this.name = name;
		this.effectPeriod = effectPeriod;
		this.ruleType = ruleType;
		this.ruleContent = ruleContent;
		this.interval = interval;
		this.entCode = entCode;
		this.inuptdate = inuptdate;
		this.ruleDesc = ruleDesc;
		this.TAttendanceLocrecords = TAttendanceLocrecords;
		this.refTermAttruleWaits = refTermAttruleWaits;
		this.isvalid = isvalid;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TAttendanceArea getTAttendanceArea() {
		return this.TAttendanceArea;
	}

	public void setTAttendanceArea(TAttendanceArea TAttendanceArea) {
		this.TAttendanceArea = TAttendanceArea;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEffectPeriod() {
		return this.effectPeriod;
	}

	public void setEffectPeriod(String effectPeriod) {
		this.effectPeriod = effectPeriod;
	}

	public String getRuleType() {
		return this.ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleContent() {
		return this.ruleContent;
	}

	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}

	public Long getInterval() {
		return this.interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	public String getEntCode() {
		return this.entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Date getInuptdate() {
		return this.inuptdate;
	}

	public void setInuptdate(Date inuptdate) {
		this.inuptdate = inuptdate;
	}

	public String getRuleDesc() {
		return this.ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public Set getTAttendanceLocrecords() {
		return this.TAttendanceLocrecords;
	}

	public void setTAttendanceLocrecords(Set TAttendanceLocrecords) {
		this.TAttendanceLocrecords = TAttendanceLocrecords;
	}

	public Set getRefTermAttruleWaits() {
		return this.refTermAttruleWaits;
	}

	public void setRefTermAttruleWaits(Set refTermAttruleWaits) {
		this.refTermAttruleWaits = refTermAttruleWaits;
	}

}