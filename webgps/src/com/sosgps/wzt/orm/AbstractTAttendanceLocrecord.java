package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * AbstractTAttendanceLocrecord entity provides the base persistence definition
 * of the TAttendanceLocrecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTAttendanceLocrecord implements
		java.io.Serializable {

	// Fields

	private Long id;
	private Long attRuleId;
	private Date inputdate;
	private String deviceId;
	private Float x;
	private Float y;
	private String entCode;
	private String locDesc;
	private String attendanceResult;
	private Long locId;
	private String jmx;
	private String jmy;
	private Date attTime;

	// Constructors

	/** default constructor */
	public AbstractTAttendanceLocrecord() {
	}

	/** full constructor */
	public AbstractTAttendanceLocrecord(Long attRuleId,
			Date inputdate, String deviceId, Float x, Float y, String entCode,
			String locDesc, String attendanceResult, Long locId, String jmx,
			String jmy, Date attTime) {
		this.attRuleId = attRuleId;
		this.inputdate = inputdate;
		this.deviceId = deviceId;
		this.x = x;
		this.y = y;
		this.entCode = entCode;
		this.locDesc = locDesc;
		this.attendanceResult = attendanceResult;
		this.locId = locId;
		this.jmx = jmx;
		this.jmy = jmy;
		this.attTime = attTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInputdate() {
		return this.inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Float getX() {
		return this.x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return this.y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	public String getEntCode() {
		return this.entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getLocDesc() {
		return this.locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}

	public String getAttendanceResult() {
		return this.attendanceResult;
	}

	public void setAttendanceResult(String attendanceResult) {
		this.attendanceResult = attendanceResult;
	}

	public Long getLocId() {
		return this.locId;
	}

	public void setLocId(Long locId) {
		this.locId = locId;
	}

	public String getJmx() {
		return this.jmx;
	}

	public void setJmx(String jmx) {
		this.jmx = jmx;
	}

	public String getJmy() {
		return this.jmy;
	}

	public void setJmy(String jmy) {
		this.jmy = jmy;
	}

	public Date getAttTime() {
		return this.attTime;
	}

	public void setAttTime(Date attTime) {
		this.attTime = attTime;
	}

	public Long getAttRuleId() {
		return attRuleId;
	}

	public void setAttRuleId(Long attRuleId) {
		this.attRuleId = attRuleId;
	}

}