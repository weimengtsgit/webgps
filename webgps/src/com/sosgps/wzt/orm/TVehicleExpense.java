package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TVehicleExpense implements java.io.Serializable {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVehicleDepreciation() {
		return vehicleDepreciation;
	}

	public void setVehicleDepreciation(Long vehicleDepreciation) {
		this.vehicleDepreciation = vehicleDepreciation;
	}

	public Long getPersonalExpenses() {
		return personalExpenses;
	}

	public void setPersonalExpenses(Long personalExpenses) {
		this.personalExpenses = personalExpenses;
	}

	public Long getInsurance() {
		return insurance;
	}

	public void setInsurance(Long insurance) {
		this.insurance = insurance;
	}

	public Long getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(Long maintenance) {
		this.maintenance = maintenance;
	}

	public Long getToll() {
		return toll;
	}

	public void setToll(Long toll) {
		this.toll = toll;
	}

	public Long getAnnualCheck() {
		return annualCheck;
	}

	public void setAnnualCheck(Long annualCheck) {
		this.annualCheck = annualCheck;
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
	private Long vehicleDepreciation;
	private Long personalExpenses;
	private Long insurance;
	private Long maintenance;
	private Long toll;
	private Long annualCheck;
	private Date createDate;
	private Date changeDate;
	private Long userId;
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

	private String empCode;

	public TVehicleExpense() {
	}

	public TVehicleExpense(String deviceId, Long vehicleDepreciation, Long personalExpenses, 
			Long insurance, Long maintenance, Long toll, Long annualCheck, 
			Date createDate, Date changeDate, String empCode, Long userId) {
		this.empCode = empCode;
		this.userId = userId;
		this.deviceId = deviceId;
		this.vehicleDepreciation = vehicleDepreciation;
		this.personalExpenses = personalExpenses;
		this.insurance = insurance;
		this.maintenance = maintenance;
		this.toll = toll;
		this.annualCheck = annualCheck;
		this.createDate = createDate;
		this.changeDate = changeDate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
