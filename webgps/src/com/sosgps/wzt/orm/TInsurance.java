package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TInsurance implements java.io.Serializable {

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
	private String insuranceNo;
	private String insuranceName;
	private String insuranceCo;
	private Date insuranceDate;
	private Date expireDate;
	private Long sumInsured;
	private Long premium;
	private String handler;

	public TInsurance(String empCode, Long userId, String deviceId, Date createDate, Date changeDate,
			String insuranceNo, String insuranceName, String insuranceCo, Date insuranceDate,
			Date expireDate, Long sumInsured, Long premium, String handler) {
		this.deviceId = deviceId;
		this.userId = userId;
		this.empCode = empCode;
		this.createDate = createDate;
		this.changeDate = changeDate;
		this.insuranceNo = insuranceNo;
		this.insuranceName = insuranceName;
		this.insuranceCo = insuranceCo;
		this.insuranceDate = insuranceDate;
		this.expireDate = expireDate;
		this.sumInsured = sumInsured;
		this.premium = premium;
		this.handler = handler;
	}

	public String getInsuranceNo() {
		return insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getInsuranceCo() {
		return insuranceCo;
	}

	public void setInsuranceCo(String insuranceCo) {
		this.insuranceCo = insuranceCo;
	}

	public Date getInsuranceDate() {
		return insuranceDate;
	}

	public void setInsuranceDate(Date insuranceDate) {
		this.insuranceDate = insuranceDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Long getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Long sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getPremium() {
		return premium;
	}

	public void setPremium(Long premium) {
		this.premium = premium;
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

	public TInsurance() {
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
