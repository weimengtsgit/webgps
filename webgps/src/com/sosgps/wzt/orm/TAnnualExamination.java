package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TAnnualExamination implements java.io.Serializable {

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
	private Date examinationDate;
	private String project;
	private String condition;
	private Long expenses;
	private String handler;
	private String demo;
	private Date expireDate;

	public TAnnualExamination(String empCode, Long userId, String deviceId, Date createDate, Date changeDate,
			Date examinationDate, String project, String condition, Long expenses,
			String handler, String demo, Date expireDate) {
		this.deviceId = deviceId;
		this.userId = userId;
		this.empCode = empCode;
		this.createDate = createDate;
		this.changeDate = changeDate;
		this.examinationDate = examinationDate;
		this.project = project;
		this.condition = condition;
		this.expenses = expenses;
		this.handler = handler;
		this.demo = demo;
		this.expireDate = expireDate;
	}

	public Date getExaminationDate() {
		return examinationDate;
	}

	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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

	public TAnnualExamination() {
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
