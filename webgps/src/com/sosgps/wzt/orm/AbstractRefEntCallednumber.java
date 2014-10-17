package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * AbstractRefEntCallednumber entity provides the base persistence definition of
 * the RefEntCallednumber entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefEntCallednumber implements
		java.io.Serializable {

	// Fields

	private Long id;
	private TEnt TEnt;
	private String calledNumber;
	private String numberName;
	private Date inputdate;

	// Constructors

	/** default constructor */
	public AbstractRefEntCallednumber() {
	}

	/** minimal constructor */
	public AbstractRefEntCallednumber(Date inputdate) {
		this.inputdate = inputdate;
	}

	/** full constructor */
	public AbstractRefEntCallednumber(TEnt TEnt, String calledNumber,
			Date inputdate) {
		this.TEnt = TEnt;
		this.calledNumber = calledNumber;
		this.inputdate = inputdate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TEnt getTEnt() {
		return this.TEnt;
	}

	public void setTEnt(TEnt TEnt) {
		this.TEnt = TEnt;
	}

	public String getCalledNumber() {
		return this.calledNumber;
	}

	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}

	public Date getInputdate() {
		return this.inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getNumberName() {
		return numberName;
	}

	public void setNumberName(String numberName) {
		this.numberName = numberName;
	}

}