package com.sosgps.v21.model;

import java.math.BigDecimal;

public class Cash implements java.io.Serializable {

	private static final long serialVersionUID = 539443637326985525L;

	private Long id;

	private String deviceId;

	private Long poiId;

	private BigDecimal cashAmount;
	private BigDecimal cashAmount2;
	private BigDecimal cashAmount3;
	private BigDecimal cashAmount4;
	private BigDecimal cashAmount5;
	private BigDecimal cashAmount6;
	private BigDecimal cashAmount7;
	private BigDecimal cashAmount8;
	private BigDecimal cashAmount9;
	private BigDecimal cashAmount10;
	private BigDecimal cashAmount11;

	public BigDecimal getCashAmount11() {
		return cashAmount11;
	}

	public void setCashAmount11(BigDecimal cashAmount11) {
		this.cashAmount11 = cashAmount11;
	}

	public BigDecimal getCashAmount2() {
		return cashAmount2;
	}

	public void setCashAmount2(BigDecimal cashAmount2) {
		this.cashAmount2 = cashAmount2;
	}

	public BigDecimal getCashAmount3() {
		return cashAmount3;
	}

	public void setCashAmount3(BigDecimal cashAmount3) {
		this.cashAmount3 = cashAmount3;
	}

	public BigDecimal getCashAmount4() {
		return cashAmount4;
	}

	public void setCashAmount4(BigDecimal cashAmount4) {
		this.cashAmount4 = cashAmount4;
	}

	public BigDecimal getCashAmount5() {
		return cashAmount5;
	}

	public void setCashAmount5(BigDecimal cashAmount5) {
		this.cashAmount5 = cashAmount5;
	}

	public BigDecimal getCashAmount6() {
		return cashAmount6;
	}

	public void setCashAmount6(BigDecimal cashAmount6) {
		this.cashAmount6 = cashAmount6;
	}

	public BigDecimal getCashAmount7() {
		return cashAmount7;
	}

	public void setCashAmount7(BigDecimal cashAmount7) {
		this.cashAmount7 = cashAmount7;
	}

	public BigDecimal getCashAmount8() {
		return cashAmount8;
	}

	public void setCashAmount8(BigDecimal cashAmount8) {
		this.cashAmount8 = cashAmount8;
	}

	public BigDecimal getCashAmount9() {
		return cashAmount9;
	}

	public void setCashAmount9(BigDecimal cashAmount9) {
		this.cashAmount9 = cashAmount9;
	}

	public BigDecimal getCashAmount10() {
		return cashAmount10;
	}

	public void setCashAmount10(BigDecimal cashAmount10) {
		this.cashAmount10 = cashAmount10;
	}

	private Long renderTime;

	private Integer approved = 0;

	private String verifier;

	private Integer states = 0;

	private String createBy;

	private String lastUpdateBy;

	private Long createOn;

	private Long lastUpdateOn;

	private String groupName;

	private String vehicleNumber;

	private String poiName;

	private String entCode;

	private Long entId;

	private Long groupId;
	

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	private String terminalName;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public Long getRenderTime() {
		return renderTime;
	}

	public void setRenderTime(Long renderTime) {
		this.renderTime = renderTime;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
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

	public Long getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Long createOn) {
		this.createOn = createOn;
	}

	public Long getLastUpdateOn() {
		return lastUpdateOn;
	}

	public void setLastUpdateOn(Long lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
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

}
