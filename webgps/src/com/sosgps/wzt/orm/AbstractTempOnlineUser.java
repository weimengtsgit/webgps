package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * AbstractTempOnlineUser entity provides the base persistence definition of the
 * TempOnlineUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTempOnlineUser implements java.io.Serializable {

	// Fields

	private Long id;
	private Date createTime;
	private Long userId;
	private String sessionid;
	private String empCode;

	// Constructors

	/** default constructor */
	public AbstractTempOnlineUser() {
	}

	/** full constructor */
	public AbstractTempOnlineUser(Date createTime, Long userId,
			String sessionid, String empCode) {
		this.createTime = createTime;
		this.userId = userId;
		this.sessionid = sessionid;
		this.empCode = empCode;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

}