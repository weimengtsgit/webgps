package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TToll implements java.io.Serializable {

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
	private Date payDate;
	private String payPlace;
	private Long expenses;
	private String handler;
	private String demo;

	public TToll(String empCode, Long userId, String deviceId, Date createDate, Date changeDate,
			Date payDate, String payPlace, Long expenses, String handler,  String demo) {
		this.deviceId = deviceId;
		this.userId = userId;
		this.empCode = empCode;
		this.createDate = createDate;
		this.changeDate = changeDate;
		this.payDate = payDate;
		this.payPlace = payPlace;
		this.expenses = expenses;
		this.handler = handler;
		this.demo = demo;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getPayPlace() {
		return payPlace;
	}

	public void setPayPlace(String payPlace) {
		this.payPlace = payPlace;
	}

	public Long getExpenses() {
		return expenses;
	}

	public void setExpenses(Long expenses) {
		this.expenses = expenses;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
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

	public TToll() {
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
