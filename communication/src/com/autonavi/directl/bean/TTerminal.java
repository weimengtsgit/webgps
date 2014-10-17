package com.autonavi.directl.bean;

import java.util.Date;

@SuppressWarnings("serial")
public class TTerminal implements java.io.Serializable{

	private String deviceId;
	private String termName;
	private String imgUrl;
	private Long suiteId;
	private String entCode;
	private String simcard;
	private String oemCode;
	private String driverNumber;
	private String locateType;
	private Long carTypeId;
	private String isAllocate;
	private String subCompany;
	private Date crtdate;
	private Long usageFlag;
	private Date outdate;
	private String startTime;
	private String endTime;
	private Long getherInterval;
	private String vehicleNumber;
	private String vehicleType;
	private Long oilSpeedLimit;
	private Long speedAlarmLimit;
	private Long speedAlarmLast;
	private String holdAlarmFlag;
	private String province;
	private String city;
	private String termdesc;
	private Long week;
	private Long carTypeInfoId;
	private Date expirationTime;
	private Long expirationFlag;
	private String imsi;
	private String typeCode;
    private String protocolPwd;
    private String areaAlarmFlag;
    private String speedAlarmFlag;
    private Long createon;
    private Long lastupdateon;
    private Long states;
    private String termType;
	

	
    public String getTypeCode() {
        return typeCode;
    }

    
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    
    public String getProtocolPwd() {
        return protocolPwd;
    }

    
    public void setProtocolPwd(String protocolPwd) {
        this.protocolPwd = protocolPwd;
    }

    
    public String getAreaAlarmFlag() {
        return areaAlarmFlag;
    }

    
    public void setAreaAlarmFlag(String areaAlarmFlag) {
        this.areaAlarmFlag = areaAlarmFlag;
    }

    
    public String getSpeedAlarmFlag() {
        return speedAlarmFlag;
    }

    
    public void setSpeedAlarmFlag(String speedAlarmFlag) {
        this.speedAlarmFlag = speedAlarmFlag;
    }

    
    public Long getCreateon() {
        return createon;
    }

    
    public void setCreateon(Long createon) {
        this.createon = createon;
    }

    
    public Long getLastupdateon() {
        return lastupdateon;
    }

    
    public void setLastupdateon(Long lastupdateon) {
        this.lastupdateon = lastupdateon;
    }

    
    public Long getStates() {
        return states;
    }

    
    public void setStates(Long states) {
        this.states = states;
    }

    
    public String getTermType() {
        return termType;
    }

    
    public void setTermType(String termType) {
        this.termType = termType;
    }

    public TTerminal() {
	}

	public TTerminal(String deviceId) {
		this.deviceId = deviceId;
	}

	public TTerminal(String termName, String imgUrl, Long suiteId,
			String entCode, String simcard, String oemCode,
			String driverNumber, String locateType, Long carTypeId,
			String isAllocate, String subCompany, String imsi) {
		this.termName = termName;
		this.imgUrl = imgUrl;
		this.suiteId = suiteId;
		this.entCode = entCode;
		this.simcard = simcard;
		this.oemCode = oemCode;
		this.driverNumber = driverNumber;
		this.locateType = locateType;
		this.carTypeId = carTypeId;
		this.isAllocate = isAllocate;
		this.subCompany = subCompany;
		this.imsi = imsi;
	}

	// Property accessors

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTermName() {
		return this.termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getSuiteId() {
		return this.suiteId;
	}

	public void setSuiteId(Long suiteId) {
		this.suiteId = suiteId;
	}

	public String getEntCode() {
		return this.entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getSimcard() {
		return this.simcard;
	}

	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public String getOemCode() {
		return this.oemCode;
	}

	public void setOemCode(String oemCode) {
		this.oemCode = oemCode;
	}

	public String getDriverNumber() {
		return this.driverNumber;
	}

	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}

	public String getLocateType() {
		return this.locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public Long getCarTypeId() {
		return this.carTypeId;
	}

	public void setCarTypeId(Long carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getIsAllocate() {
		return this.isAllocate;
	}

	public void setIsAllocate(String isAllocate) {
		this.isAllocate = isAllocate;
	}

	public String getSubCompany() {
		return this.subCompany;
	}

	public void setSubCompany(String subCompany) {
		this.subCompany = subCompany;
	}

	public Date getCrtdate() {
		return crtdate;
	}

	public void setCrtdate(Date crtdate) {
		this.crtdate = crtdate;
	}

	public Long getUsageFlag() {
		return usageFlag;
	}

	public void setUsageFlag(Long usageFlag) {
		this.usageFlag = usageFlag;
	}

	public Date getOutdate() {
		return outdate;
	}

	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getGetherInterval() {
		return getherInterval;
	}

	public void setGetherInterval(Long getherInterval) {
		this.getherInterval = getherInterval;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Long getOilSpeedLimit() {
		return oilSpeedLimit;
	}

	public void setOilSpeedLimit(Long oilSpeedLimit) {
		this.oilSpeedLimit = oilSpeedLimit;
	}

	public Long getSpeedAlarmLimit() {
		return speedAlarmLimit;
	}

	public void setSpeedAlarmLimit(Long speedAlarmLimit) {
		this.speedAlarmLimit = speedAlarmLimit;
	}

	public Long getSpeedAlarmLast() {
		return speedAlarmLast;
	}

	public void setSpeedAlarmLast(Long speedAlarmLast) {
		this.speedAlarmLast = speedAlarmLast;
	}

	public String getHoldAlarmFlag() {
		return holdAlarmFlag;
	}

	public void setHoldAlarmFlag(String holdAlarmFlag) {
		this.holdAlarmFlag = holdAlarmFlag;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTermdesc() {
		return termdesc;
	}

	public void setTermdesc(String termdesc) {
		this.termdesc = termdesc;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Long getExpirationFlag() {
		return expirationFlag;
	}

	public void setExpirationFlag(Long expirationFlag) {
		this.expirationFlag = expirationFlag;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public Long getWeek() {
		return week;
	}

	public void setWeek(Long week) {
		this.week = week;
	}

	public Long getCarTypeInfoId() {
		return carTypeInfoId;
	}

	public void setCarTypeInfoId(Long carTypeInfoId) {
		this.carTypeInfoId = carTypeInfoId;
	}

}
