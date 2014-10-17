package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractTAttendanceArea entity provides the base persistence definition of
 * the TAttendanceArea entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTAttendanceArea implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String points;
	private Date createTime;
	private Date modifyDate;
	private String entCode;
	private Set TAttendanceRules = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractTAttendanceArea() {
	}

	/** full constructor */
	public AbstractTAttendanceArea(String name, String points, Date createTime,
			String entCode, Set TAttendanceRules) {
		this.name = name;
		this.points = points;
		this.createTime = createTime;
		this.entCode = entCode;
		this.TAttendanceRules = TAttendanceRules;
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

	public String getPoints() {
		return this.points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEntCode() {
		return this.entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Set getTAttendanceRules() {
		return this.TAttendanceRules;
	}

	public void setTAttendanceRules(Set TAttendanceRules) {
		this.TAttendanceRules = TAttendanceRules;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}