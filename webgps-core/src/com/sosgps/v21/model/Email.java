package com.sosgps.v21.model;

/**
 * Email entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Email implements java.io.Serializable {

	// Fields

	private Long id;
	private String entCode;
	private String contactName;
	private String position;
	private Integer sendTime;
	private Integer sendFrequency;
	private Long deleteFlag;
	private String email;
	private Long lastTime;
	private Integer sendWeekday;
	private Integer sendXun;
	private Integer sendDay;
	private Long userId;
	private String lastupdateby;
	private Long createon;
	private String createby;
	private Long lastupdateon;

	// Constructors

	/** default constructor */
	public Email() {
	}

	/** full constructor */
	public Email(String entCode, String contactName, String position,
			Integer sendTime, Integer sendFrequency, Long deleteFlag,
			String email, Long lastTime, Integer sendWeekday, Integer sendXun,
			Integer sendDay, Long userId, String lastupdateby, Long createon,
			String createby, Long lastupdateon) {
		this.entCode = entCode;
		this.contactName = contactName;
		this.position = position;
		this.sendTime = sendTime;
		this.sendFrequency = sendFrequency;
		this.deleteFlag = deleteFlag;
		this.email = email;
		this.lastTime = lastTime;
		this.sendWeekday = sendWeekday;
		this.sendXun = sendXun;
		this.sendDay = sendDay;
		this.userId = userId;
		this.lastupdateby = lastupdateby;
		this.createon = createon;
		this.createby = createby;
		this.lastupdateon = lastupdateon;
	}

	// Property accessors
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getSendTime() {
		return sendTime;
	}

	public void setSendTime(Integer sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getSendFrequency() {
		return sendFrequency;
	}

	public void setSendFrequency(Integer sendFrequency) {
		this.sendFrequency = sendFrequency;
	}

	public Long getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Long deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public Integer getSendWeekday() {
		return sendWeekday;
	}

	public void setSendWeekday(Integer sendWeekday) {
		this.sendWeekday = sendWeekday;
	}

	public Integer getSendXun() {
		return sendXun;
	}

	public void setSendXun(Integer sendXun) {
		this.sendXun = sendXun;
	}

	public Integer getSendDay() {
		return sendDay;
	}

	public void setSendDay(Integer sendDay) {
		this.sendDay = sendDay;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLastupdateby() {
		return lastupdateby;
	}

	public void setLastupdateby(String lastupdateby) {
		this.lastupdateby = lastupdateby;
	}

	public Long getCreateon() {
		return createon;
	}

	public void setCreateon(Long createon) {
		this.createon = createon;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public Long getLastupdateon() {
		return lastupdateon;
	}

	public void setLastupdateon(Long lastupdateon) {
		this.lastupdateon = lastupdateon;
	}
}