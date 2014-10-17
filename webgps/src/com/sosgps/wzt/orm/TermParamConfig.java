package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * 模块参数配置表
 * 
 * @author Administrator
 * 
 */
public class TermParamConfig implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deviceId;
	private String content;
	private String entCode;
	private Long entId;
	private Date createOn;
	private String createBy;
	private Date LastUpdateOn;
	private String lastUpdateBy;
	private Integer type;

	public TermParamConfig() {
	}

	public TermParamConfig(Long id, String deviceId, String content,
			String entCode, Long entId, Date createOn, String createBy,
			Date lastUpdateOn, String lastUpdateBy, Integer type) {
		this.id = id;
		this.deviceId = deviceId;
		this.content = content;
		this.entCode = entCode;
		this.createOn = createOn;
		this.createBy = createBy;
		LastUpdateOn = lastUpdateOn;
		this.lastUpdateBy = lastUpdateBy;
		this.type = type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public Date getLastUpdateOn() {
		return LastUpdateOn;
	}

	public void setLastUpdateOn(Date lastUpdateOn) {
		LastUpdateOn = lastUpdateOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}