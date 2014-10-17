package com.sosgps.wzt.locate.bean;

import java.util.Date;

import com.sosgps.wzt.util.PointBean;

public class Locrecord extends PointBean {
	private Long id;
	private String deviceId;
	private String jmx;
	private String jmy;
	private Float speed;
	private Float direction;
	private Float height;
	private Float distance;
	private Long statlliteNum;
	private Date inputdate;
	private String locDesc;
	private Long signalFlag;

	private Long poiId;
	private String poiName;
	private String locateType;

	private Float temperature;
	private String varExt1;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getJmx() {
		return jmx;
	}

	public void setJmx(String jmx) {
		this.jmx = jmx;
	}

	public String getJmy() {
		return jmy;
	}

	public void setJmy(String jmy) {
		this.jmy = jmy;
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Float getDirection() {
		return direction;
	}

	public void setDirection(Float direction) {
		this.direction = direction;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Long getStatlliteNum() {
		return statlliteNum;
	}

	public void setStatlliteNum(Long statlliteNum) {
		this.statlliteNum = statlliteNum;
	}

	public Date getInputdate() {
		return inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}

	public Long getSignalFlag() {
		return signalFlag;
	}

	public void setSignalFlag(Long signalFlag) {
		this.signalFlag = signalFlag;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getLocateType() {
		return locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public String getVarExt1() {
		return varExt1;
	}

	public void setVarExt1(String varExt1) {
		this.varExt1 = varExt1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
