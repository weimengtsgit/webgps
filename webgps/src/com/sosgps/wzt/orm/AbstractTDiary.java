package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public abstract class AbstractTDiary implements java.io.Serializable {

	private Long id;
	private String termName;
	private String title;
	private String content;
	private String remark;
	private Date createDate;
	private Date modifyDate;
	private String entCode;
	private Long userId;
	private Date diaryDate;
	private Date remarkDate;
	private String deviceId;

	/** default constructor */
	public AbstractTDiary() {
	}

	/** full constructor */
	public AbstractTDiary(Long id, String termName, String title, String content,
			String remark, Date createDate, Date modifyDate, Long userId, String entCode, Date diaryDate, Date remarkDate, String deviceId) {
		this.id = id;
		this.termName = termName;
		this.title = title;
		this.content = content;
		this.remark = remark;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.userId = userId;
		this.entCode = entCode;
		this.diaryDate = diaryDate;
		this.remarkDate = remarkDate;
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getRemarkDate() {
		return remarkDate;
	}

	public void setRemarkDate(Date remarkDate) {
		this.remarkDate = remarkDate;
	}

	public Date getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(Date diaryDate) {
		this.diaryDate = diaryDate;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}