package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * AbstractRefTermAttruleWait entity provides the base persistence definition of
 * the RefTermAttrule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefTermAttruleWait implements
		java.io.Serializable {

	// Fields

	private RefTermAttruleWaitId id;
	private TAttendanceRule TAttendanceRule;
	private TTerminal TTerminal;
	private Long isvalid;
	private Date modifyDate;
	private Date inputdate;

	// Constructors

	/** default constructor */
	public AbstractRefTermAttruleWait() {
	}

	/** minimal constructor */
	public AbstractRefTermAttruleWait(RefTermAttruleWaitId id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractRefTermAttruleWait(RefTermAttruleWaitId id,
			TAttendanceRule TAttendanceRule, TTerminal TTerminal,
			Long isvalid, Date modifyDate, Date inputdate) {
		this.id = id;
		this.TAttendanceRule = TAttendanceRule;
		this.TTerminal = TTerminal;
		this.isvalid = isvalid;
		this.modifyDate = modifyDate;
		this.inputdate = inputdate;
	}

	// Property accessors

	public RefTermAttruleWaitId getId() {
		return this.id;
	}

	public void setId(RefTermAttruleWaitId id) {
		this.id = id;
	}

	public TAttendanceRule getTAttendanceRule() {
		return this.TAttendanceRule;
	}

	public void setTAttendanceRule(TAttendanceRule TAttendanceRule) {
		this.TAttendanceRule = TAttendanceRule;
	}

	public TTerminal getTTerminal() {
		return this.TTerminal;
	}

	public void setTTerminal(TTerminal TTerminal) {
		this.TTerminal = TTerminal;
	}

	public Long getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(Long isvalid) {
		this.isvalid = isvalid;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Date getInputdate() {
		return inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

}