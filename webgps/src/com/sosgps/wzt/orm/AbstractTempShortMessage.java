package com.sosgps.wzt.orm;

/**
 * AbstractTempShortMessage entity provides the base persistence definition of
 * the TempShortMessage entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTempShortMessage implements java.io.Serializable {

	// Fields

	private Long id;
	private String phoneNumber;
	private Long createTime;
	private Long userId;
	private String dynamicPassword;
	private String empCode;
	private Long type;

	// Constructors

	/** default constructor */
	public AbstractTempShortMessage() {
	}

	/** full constructor */
	public AbstractTempShortMessage(String phoneNumber, Long createTime,
			Long userId, String dynamicPassword) {
		this.phoneNumber = phoneNumber;
		this.createTime = createTime;
		this.userId = userId;
		this.dynamicPassword = dynamicPassword;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDynamicPassword() {
		return this.dynamicPassword;
	}

	public void setDynamicPassword(String dynamicPassword) {
		this.dynamicPassword = dynamicPassword;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

}