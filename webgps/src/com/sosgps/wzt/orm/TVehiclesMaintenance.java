package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TVehiclesMaintenance implements java.io.Serializable {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	private Long id;
	private String deviceId;
	private String empCode;
	private Long userId;
	private Date createDate;
	private Date changeDate;
	private Date expireDate;
	private Long expenses;
	private String condition;
	private String handler;
	private String demo;
	private Date maintenanceDate;

	public Long getExpenses() {
		return expenses;
	}

	public void setExpenses(Long expenses) {
		this.expenses = expenses;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	public Date getMaintenanceDate() {
		return maintenanceDate;
	}

	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	public TVehiclesMaintenance(String empCode, Long userId, String deviceId, Date createDate, Date changeDate,
			String insuranceNo, String insuranceName, String insuranceCo, Date insuranceDate,
			Date expireDate, Long sumInsured, Long premium, String handler, Long expenses, String condition,
			String demo, Date maintenanceDate) {
		this.deviceId = deviceId;
		this.userId = userId;
		this.empCode = empCode;
		this.createDate = createDate;
		this.changeDate = changeDate;
		this.expireDate = expireDate;
		this.handler = handler;
		this.expenses = expenses;
		this.condition = condition;
		this.demo = demo;
		this.maintenanceDate = maintenanceDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public TVehiclesMaintenance() {
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
