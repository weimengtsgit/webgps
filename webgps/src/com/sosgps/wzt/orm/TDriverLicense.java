package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TDriverLicense implements java.io.Serializable {

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
	private String demo;
	private Date expireDate;
	private String employeeNum;
	private String employeeWorknum;
	private String employeeName;
	private String employeeId;
	private String department;
	private String condition;
	

	public TDriverLicense(String empCode, Long userId, String deviceId, Date createDate, Date changeDate,
			Date examinationDate, String condition,String demo, Date expireDate, String employeeNum
			, String employeeWorknum, String employeeName, String employeeId, String department) {
		this.deviceId = deviceId;
		this.userId = userId;
		this.empCode = empCode;
		this.createDate = createDate;
		this.changeDate = changeDate;
		this.examinationDate = examinationDate;
		this.condition = condition;
		this.demo = demo;
		this.expireDate = expireDate;
		this.employeeNum = employeeNum;
		this.employeeWorknum = employeeWorknum;
		this.employeeName = employeeName;
		this.employeeId = employeeId;
		this.department = department;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}

	public String getEmployeeWorknum() {
		return employeeWorknum;
	}

	public void setEmployeeWorknum(String employeeWorknum) {
		this.employeeWorknum = employeeWorknum;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getExaminationDate() {
		return examinationDate;
	}

	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
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

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
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

	public TDriverLicense() {
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
