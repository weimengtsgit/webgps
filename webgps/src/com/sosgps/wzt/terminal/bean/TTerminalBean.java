package com.sosgps.wzt.terminal.bean;

import java.util.Date;

@SuppressWarnings("serial")
public class TTerminalBean implements java.io.Serializable {
	private String entCode;// 企业代码
	private String deviceId;// 终端序列号
	private String typeCode;// 终端类型
	private String termName;// 名称
	private String simcard;// 手机号
	private String locateType;// 类型0手机1GPS
	private Long groupId;// 组id
	private String driverNumber;// 司机电话
	private String termDesc;// 描述
	private String province;// 省
	private String city;// 市
	private String startTime;// 起始时间
	private String endTime;// 终止时间
	private Long getherInterval;// 采集时间（单位分钟）
	private String vehicleNumber;// 车牌号
	private String vehicleType;// 车辆类型
	private Long oilSpeedLimit;// 断油断电速度阀值
	private Long speedAlarmLimit;// 超速限值
	private Long speedAlarmLast;// 超速持续时间（单位分钟）
	private String holdAlarmFlag;// 劫警标志0关1开
	private String isAllocate;
	private String imgUrl;
	private Long week;
	private Long carTypeInfo;
	private Date setExpirationTime;
	private String imsi;
	public Date getSetExpirationTime() {
		return setExpirationTime;
	}

	public void setSetExpirationTime(Date setExpirationTime) {
		this.setExpirationTime = setExpirationTime;
	}

	public Long getCarTypeInfo() {
		return carTypeInfo;
	}

	public void setCarTypeInfo(Long carTypeInfo) {
		this.carTypeInfo = carTypeInfo;
	}

	public Long getWeek() {
		return week;
	}

	public void setWeek(Long week) {
		this.week = week;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIsAllocate() {
		return isAllocate;
	}

	public void setIsAllocate(String isAllocate) {
		this.isAllocate = isAllocate;
	}

	public TTerminalBean() {
		super();
	}

	public TTerminalBean(String entCode, String deviceId, String typeCode,
			String termName, String simcard, String locateType, Long groupId,
			String driverNumber, String termDesc, String province, String city,
			String startTime, String endTime, Long getherInterval,
			String vehicleNumber, String vehicleType, Long oilSpeedLimit,
			Long speedAlarmLimit, Long speedAlarmLast, String holdAlarmFlag, String imgUrl,String imsi) {
		super();
		this.entCode = entCode;
		this.deviceId = deviceId;
		this.typeCode = typeCode;
		this.termName = termName;
		this.simcard = simcard;
		this.locateType = locateType;
		this.groupId = groupId;
		this.driverNumber = driverNumber;
		this.termDesc = termDesc;
		this.province = province;
		this.city = city;
		this.startTime = startTime;
		this.endTime = endTime;
		this.getherInterval = getherInterval;
		this.vehicleNumber = vehicleNumber;
		this.vehicleType = vehicleType;
		this.oilSpeedLimit = oilSpeedLimit;
		this.speedAlarmLimit = speedAlarmLimit;
		this.speedAlarmLast = speedAlarmLast;
		this.holdAlarmFlag = holdAlarmFlag;
		this.imgUrl = imgUrl;
		this.imsi = imsi;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getSimcard() {
		return simcard;
	}

	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public String getLocateType() {
		return locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getDriverNumber() {
		return driverNumber;
	}

	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}

	public String getTermDesc() {
		return termDesc;
	}

	public void setTermDesc(String termDesc) {
		this.termDesc = termDesc;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getGetherInterval() {
		return getherInterval;
	}

	public void setGetherInterval(Long getherInterval) {
		this.getherInterval = getherInterval;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Long getOilSpeedLimit() {
		return oilSpeedLimit;
	}

	public void setOilSpeedLimit(Long oilSpeedLimit) {
		this.oilSpeedLimit = oilSpeedLimit;
	}

	public Long getSpeedAlarmLimit() {
		return speedAlarmLimit;
	}

	public void setSpeedAlarmLimit(Long speedAlarmLimit) {
		this.speedAlarmLimit = speedAlarmLimit;
	}

	public Long getSpeedAlarmLast() {
		return speedAlarmLast;
	}

	public void setSpeedAlarmLast(Long speedAlarmLast) {
		this.speedAlarmLast = speedAlarmLast;
	}

	public String getHoldAlarmFlag() {
		return holdAlarmFlag;
	}

	public void setHoldAlarmFlag(String holdAlarmFlag) {
		this.holdAlarmFlag = holdAlarmFlag;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	

}
