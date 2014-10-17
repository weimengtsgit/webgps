/**
 * Copyright (C) 2012 SOSGPS All Rights Reserved
 */
package com.sosgps.v21.model;

import java.math.BigDecimal;

/**
 * TEmailSendLog entity.
 * 
 * @author zhouqiang
 */

public class EmailSendLog implements java.io.Serializable {

	// Fields

	private Long id;

	private String entCode;
  
	private String entName;
	
	private String email;

	private String sendTime;

	private String failDesc;

	private String content;

	private String contactName;

	private String position;

	private Long userId;

	private String lastupdateby;

	private BigDecimal lastupdateon;

	private String createby;

	private Long createon;

	private Long status;

	// Constructors

	/** default constructor */
	public EmailSendLog() {
	}

	/** minimal constructor */
	public EmailSendLog(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EmailSendLog(Long id, String entCode, String entName, String email,
			String sendTime, String failDesc, String content,
			String contactName, String position, Long userId,
			String lastupdateby, BigDecimal lastupdateon, String createby,
			Long createon, Long status) {
		this.id = id;
		this.entCode = entCode;
		this.entName = entName;
		this.email = email;
		this.sendTime = sendTime;
		this.failDesc = failDesc;
		this.content = content;
		this.contactName = contactName;
		this.position = position;
		this.userId = userId;
		this.lastupdateby = lastupdateby;
		this.lastupdateon = lastupdateon;
		this.createby = createby;
		this.createon = createon;
		this.status = status;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntCode() {
		return this.entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getEntName() {
	    return this.entName;
	}
	
	public void setEntName(String entName) {
	    this.entName = entName;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getFailDesc() {
		return this.failDesc;
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLastupdateby() {
		return this.lastupdateby;
	}

	public void setLastupdateby(String lastupdateby) {
		this.lastupdateby = lastupdateby;
	}

	public BigDecimal getLastupdateon() {
		return this.lastupdateon;
	}

	public void setLastupdateon(BigDecimal lastupdateon) {
		this.lastupdateon = lastupdateon;
	}

	public String getCreateby() {
		return this.createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public Long getCreateon() {
		return this.createon;
	}

	public void setCreateon(Long createon) {
		this.createon = createon;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}
