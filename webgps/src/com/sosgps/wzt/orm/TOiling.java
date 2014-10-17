package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TOiling implements java.io.Serializable {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOilLiter() {
		return oilLiter;
	}

	public void setOilLiter(Long oilLiter) {
		this.oilLiter = oilLiter;
	}

	public Long getOilCost() {
		return oilCost;
	}

	public void setOilCost(Long oilCost) {
		this.oilCost = oilCost;
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
	private Long oilLiter;
	private Long oilCost;
	private Long userId;
	private String deviceId;
	private String empCode;
	private Date createDate;
	private Date changeDate;
	
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

	public TOiling() {
	}

	public TOiling(String empCode, Long userId, String deviceId, Long oilLiter, Long oilCost, Date createDate, Date changeDate) {
		this.deviceId = deviceId;
		this.oilLiter = oilLiter;
		this.oilCost = oilCost;
		this.userId = userId;
		this.empCode = empCode;
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
