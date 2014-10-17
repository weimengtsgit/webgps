package com.sosgps.v21.model;

/**
 * 目标数据实体
 * 
 * @author liuhx
 * 
 */
public class TargetTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer type;

	private String deviceId;

	private Integer visitAmount;//客户拜访

	private Double cashAmount;//回款

	private Double billAmount;//签单

	private Double costAmount;//费用

	private Integer year;

	private Integer targetOn;

	private Long groupId;

	private String groupName;

	private String vehicleNumber;

	private String entCode;

	private Long entId;

	private Integer states;

	private Long createOn;

	private String createBy;

	private Long lastUpdateOn;

	private String lastUpdateBy;
	
	private String terminalName;
	
	private Integer cusVisitAmount;//客户被拜访

	public Integer getCusVisitAmount() {
		return cusVisitAmount;
	}

	public void setCusVisitAmount(Integer cusVisitAmount) {
		this.cusVisitAmount = cusVisitAmount;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getVisitAmount() {
		return visitAmount;
	}

	public void setVisitAmount(Integer visitAmount) {
		this.visitAmount = visitAmount;
	}

	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(Double costAmount) {
		this.costAmount = costAmount;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public Long getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Long createOn) {
		this.createOn = createOn;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Long getLastUpdateOn() {
		return lastUpdateOn;
	}

	public void setLastUpdateOn(Long lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getTargetOn() {
		return targetOn;
	}

	public void setTargetOn(Integer targetOn) {
		this.targetOn = targetOn;
	}
}
