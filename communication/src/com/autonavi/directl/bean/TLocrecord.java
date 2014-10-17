package com.autonavi.directl.bean;

import java.util.Date;

@SuppressWarnings("serial")
public class TLocrecord implements java.io.Serializable{

	private Long id;
	private String deviceId;
	private Float latitude;
	private Float longitude;
	private String jmx;
	private String jmy;
	private Float speed;
	private Float direction;
	private Float height;
	private Float distance;
	private Long statlliteNum;
	private Date gpstime;
	private Date inputdate;
	private String locateType;
	private Long signalFlag;
	private String programVersion;
	private String termStatus;
	private String imsi;
	private String locDesc;
	private String vehicleNumber;
	private String simcard;
	private Long week;
	private Long groupId;
	private String groupName;
	private Long entId;
	private String entCode;

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public TLocrecord() {
	}

	public TLocrecord(Long id) {
		this.id = id;
	}

	public TLocrecord(Long id, String deviceId, Float latitude,
			Float longitude, String jmx, String jmy, Float speed,
			Float direction, Float height, Float distance, Long statlliteNum,
			Date gpstime, Date inputdate, String locateType, Long signalFlag,
			String programVersion, String termStatus, String imsi) {
		this.id = id;
		this.deviceId = deviceId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.jmx = jmx;
		this.jmy = jmy;
		this.speed = speed;
		this.direction = direction;
		this.height = height;
		this.distance = distance;
		this.statlliteNum = statlliteNum;
		this.gpstime = gpstime;
		this.inputdate = inputdate;
		this.locateType = locateType;
		this.signalFlag = signalFlag;
		this.programVersion = programVersion;
		this.termStatus = termStatus;
		this.imsi = imsi;
	}

	public String getTermStatus() {
		return this.termStatus;
	}

	public void setTermStatus(String termStatus) {
		this.termStatus = termStatus;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
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

	public Float getSpeed() {
		return this.speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Float getDirection() {
		return this.direction;
	}

	public void setDirection(Float direction) {
		this.direction = direction;
	}

	public Float getHeight() {
		return this.height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getDistance() {
		return this.distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Long getStatlliteNum() {
		return this.statlliteNum;
	}

	public void setStatlliteNum(Long statlliteNum) {
		this.statlliteNum = statlliteNum;
	}

	public Date getGpstime() {
		return this.gpstime;
	}

	public void setGpstime(Date gpstime) {
		this.gpstime = gpstime;
	}

	public Date getInputdate() {
		return this.inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getLocateType() {
		return this.locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public Long getSignalFlag() {
		return this.signalFlag;
	}

	public void setSignalFlag(Long signalFlag) {
		this.signalFlag = signalFlag;
	}

	public String getProgramVersion() {
		return this.programVersion;
	}

	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}

	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getSimcard() {
		return simcard;
	}

	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public Long getWeek() {
		return week;
	}

	public void setWeek(Long week) {
		this.week = week;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
