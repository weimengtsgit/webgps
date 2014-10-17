package com.sosgps.wzt.stat.alarmstat.bean;

import java.util.Date;

public class AlarmTjBean {
	private Long id;
	private String epCode;
	private String deviceId;
	private Long speedAlarms;
	private Long inactiveAlarms;
	private Long areaAlarms;
	private Long lineAlarms;
	private Long callRecord;
	private Long shortMessage;
	private Date tjdate;
	private String locateType;

	public String getLocateType() {
		return locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	private Long distance;
	
	private String termName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTjdate() {
		return tjdate;
	}

	public void setTjdate(Date tjdate) {
		this.tjdate = tjdate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getSpeedAlarms() {
		return speedAlarms;
	}

	public void setSpeedAlarms(Long speedAlarms) {
		this.speedAlarms = speedAlarms;
	}

	public Long getInactiveAlarms() {
		return inactiveAlarms;
	}

	public void setInactiveAlarms(Long inactiveAlarms) {
		this.inactiveAlarms = inactiveAlarms;
	}

	public Long getAreaAlarms() {
		return areaAlarms;
	}

	public void setAreaAlarms(Long areaAlarms) {
		this.areaAlarms = areaAlarms;
	}

	public Long getLineAlarms() {
		return lineAlarms;
	}

	public void setLineAlarms(Long lineAlarms) {
		this.lineAlarms = lineAlarms;
	}

	public String getEpCode() {
		return epCode;
	}

	public void setEpCode(String epCode) {
		this.epCode = epCode;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public Long getCallRecord() {
		return callRecord;
	}

	public void setCallRecord(Long callRecord) {
		this.callRecord = callRecord;
	}

	public Long getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(Long shortMessage) {
		this.shortMessage = shortMessage;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

}
