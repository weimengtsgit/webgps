package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractTSimpleRule entity provides the base persistence definition of the
 * TSimpleRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTSimpleRule implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String ruleType;
	private String effectPeriod;
	private String ruleContent;
	private Long interval;
	private String entCode;
	private Date inputdate;
	private String ruleDesc;
	private Set refTermSimrules = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractTSimpleRule() {
	}

	/** minimal constructor */
	public AbstractTSimpleRule(String name, String ruleType,
			String effectPeriod, String ruleContent, Long interval,
			String entCode) {
		this.name = name;
		this.ruleType = ruleType;
		this.effectPeriod = effectPeriod;
		this.ruleContent = ruleContent;
		this.interval = interval;
		this.entCode = entCode;
	}

	/** full constructor */
	public AbstractTSimpleRule(String name, String ruleType,
			String effectPeriod, String ruleContent, Long interval,
			String entCode, Date inputdate, String ruleDesc, Set refTermSimrules) {
		this.name = name;
		this.ruleType = ruleType;
		this.effectPeriod = effectPeriod;
		this.ruleContent = ruleContent;
		this.interval = interval;
		this.entCode = entCode;
		this.inputdate = inputdate;
		this.ruleDesc = ruleDesc;
		this.refTermSimrules = refTermSimrules;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRuleType() {
		return this.ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getEffectPeriod() {
		return this.effectPeriod;
	}

	public void setEffectPeriod(String effectPeriod) {
		this.effectPeriod = effectPeriod;
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

	public Date getInputdate() {
		return this.inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getRuleDesc() {
		return this.ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public Set getRefTermSimrules() {
		return this.refTermSimrules;
	}

	public void setRefTermSimrules(Set refTermSimrules) {
		this.refTermSimrules = refTermSimrules;
	}

}