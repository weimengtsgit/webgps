package com.sosgps.v21.model;

import java.math.BigDecimal;

public class Cost implements java.io.Serializable {

    private static final long serialVersionUID = 6677923178800141029L;

    private Long id;

    private String deviceId;

    private BigDecimal meal;

    private BigDecimal transportation;

    private BigDecimal accommodation;

    private BigDecimal communication;

    private BigDecimal gift;

    private BigDecimal other;

    private BigDecimal expand1;

    private BigDecimal expand2;

    private BigDecimal expand3;

    private BigDecimal expand4;

    private BigDecimal expand5;

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

    private String entCode;

    private Long entId;

    private Long groupId;

    private String terminalName;

    private String remarks;

    private Long dateTime;

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

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

    public BigDecimal getMeal() {
        return meal;
    }

    public void setMeal(BigDecimal meal) {
        this.meal = meal;
    }

    public BigDecimal getTransportation() {
        return transportation;
    }

    public void setTransportation(BigDecimal transportation) {
        this.transportation = transportation;
    }

    public BigDecimal getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(BigDecimal accommodation) {
        this.accommodation = accommodation;
    }

    public BigDecimal getCommunication() {
        return communication;
    }

    public void setCommunication(BigDecimal communication) {
        this.communication = communication;
    }

    public BigDecimal getGift() {
        return gift;
    }

    public void setGift(BigDecimal gift) {
        this.gift = gift;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getExpand1() {
        return expand1;
    }

    public void setExpand1(BigDecimal expand1) {
        this.expand1 = expand1;
    }

    public BigDecimal getExpand2() {
        return expand2;
    }

    public void setExpand2(BigDecimal expand2) {
        this.expand2 = expand2;
    }

    public BigDecimal getExpand3() {
        return expand3;
    }

    public void setExpand3(BigDecimal expand3) {
        this.expand3 = expand3;
    }

    public BigDecimal getExpand4() {
        return expand4;
    }

    public void setExpand4(BigDecimal expand4) {
        this.expand4 = expand4;
    }

    public BigDecimal getExpand5() {
        return expand5;
    }

    public void setExpand5(BigDecimal expand5) {
        this.expand5 = expand5;
    }

}
