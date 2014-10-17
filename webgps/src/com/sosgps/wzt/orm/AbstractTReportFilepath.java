package com.sosgps.wzt.orm;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AbstractTReportFilepath entity provides the base persistence definition of
 * the TReportFilepath entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTReportFilepath implements java.io.Serializable {

	// Fields

	private Long id;
	private String deviceId;
	private String entCode;
	private String filePath;
	private Date reportDate;
	private Date createDate;

	// Constructors

	/** default constructor */
	public AbstractTReportFilepath() {
	}

	/** minimal constructor */
	public AbstractTReportFilepath(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractTReportFilepath(Long id, String deviceId,
			String entCode, String filePath, Date reportDate, Date createDate) {
		this.id = id;
		this.deviceId = deviceId;
		this.entCode = entCode;
		this.filePath = filePath;
		this.reportDate = reportDate;
		this.createDate = createDate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEntCode() {
		return this.entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}