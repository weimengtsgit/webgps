package com.autonavi.directl.bean;

import java.util.Date;

@SuppressWarnings("serial")
public class TEnt implements java.io.Serializable{

	// Fields
	private Long id;
	private String entCode;
	private String entName;
	private Date openTime;
	private Date endTime;
	private String entStatus;
	private String centerX;
	private String centerY;
	private Long mapZoom;
	private String logoUrl;
	private Date entCrtDate;
	private Long feeType;
	private String businessId;
	private String areaCode;
	private Long entType;
	private String otherInfo;
	private Long maxUserNum;
	private String smsAccount;
	private String smsPwd;
	private Long visitDistance;
	private Long doubleVisitTime;
	private Long visitPlaceDistance;
	private Long visitTjOrder;
    private Long smsTotal;
    private Long smsAvailable;
	private Long smsCount;
	private Long smsType;
	private Long visitTjStatus;
	private Long reportStatus;
    private Long hashCode;
    private Long carGreyInterval;
    private Long persionGreyInterval;
    private Long status;
    private String createby;
    private String lastupdateby;
    private Long createon;
    private Long lastupdateon;
    private String editionCode;
    private String version;
    private String mmsAccount;
    private String mmsPwd;
	
    public Long getSmsTotal() {
        return smsTotal;
    }

    public void setSmsTotal(Long smsTotal) {
        this.smsTotal = smsTotal;
    }

    public Long getSmsAvailable() {
        return smsAvailable;
    }

    public void setSmsAvailable(Long smsAvailable) {
        this.smsAvailable = smsAvailable;
    }

    public Long getCarGreyInterval() {
        return carGreyInterval;
    }

    public void setCarGreyInterval(Long carGreyInterval) {
        this.carGreyInterval = carGreyInterval;
    }

    public Long getPersionGreyInterval() {
        return persionGreyInterval;
    }
    
    public void setPersionGreyInterval(Long persionGreyInterval) {
        this.persionGreyInterval = persionGreyInterval;
    }
    
    public Long getStatus() {
        return status;
    }
    
    public void setStatus(Long status) {
        this.status = status;
    }
    
    public String getCreateby() {
        return createby;
    }
    
    public void setCreateby(String createby) {
        this.createby = createby;
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
    
    public Long getLastupdateon() {
        return lastupdateon;
    }
    
    public void setLastupdateon(Long lastupdateon) {
        this.lastupdateon = lastupdateon;
    }
    
    public String getEditionCode() {
        return editionCode;
    }
    
    public void setEditionCode(String editionCode) {
        this.editionCode = editionCode;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getMmsAccount() {
        return mmsAccount;
    }
    
    public void setMmsAccount(String mmsAccount) {
        this.mmsAccount = mmsAccount;
    }

    
    public String getMmsPwd() {
        return mmsPwd;
    }

    
    public void setMmsPwd(String mmsPwd) {
        this.mmsPwd = mmsPwd;
    }

    public Long getHashCode() {
        return hashCode;
    }
    
    public void setHashCode(Long hashCode) {
        this.hashCode = hashCode;
    }

    public Long getVisitPlaceDistance() {
		return visitPlaceDistance;
	}

	public void setVisitPlaceDistance(Long visitPlaceDistance) {
		this.visitPlaceDistance = visitPlaceDistance;
	}

	// Constructors

	/** default constructor */
	public TEnt() {
	}

	/** minimal constructor */
	public TEnt(String entName, Date openTime, Date endTime, String entStatus,
			Long feeType) {
		this.entName = entName;
		this.openTime = openTime;
		this.endTime = endTime;
		this.entStatus = entStatus;
		this.feeType = feeType;
	}

	/** full constructor */
	public TEnt(String entName, Date openTime, Date endTime, String entStatus,
			String centerX, String centerY, Long mapZoom, String logoUrl,
			Date entCrtDate, Long feeType) {
		this.entName = entName;
		this.openTime = openTime;
		this.endTime = endTime;
		this.entStatus = entStatus;
		this.centerX = centerX;
		this.centerY = centerY;
		this.mapZoom = mapZoom;
		this.logoUrl = logoUrl;
		this.entCrtDate = entCrtDate;
		this.feeType = feeType;
	}

	// Property accessors

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

	public Date getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getEntStatus() {
		return this.entStatus;
	}

	public void setEntStatus(String entStatus) {
		this.entStatus = entStatus;
	}

	public String getCenterX() {
		return this.centerX;
	}

	public void setCenterX(String centerX) {
		this.centerX = centerX;
	}

	public String getCenterY() {
		return this.centerY;
	}

	public void setCenterY(String centerY) {
		this.centerY = centerY;
	}

	public Long getMapZoom() {
		return this.mapZoom;
	}

	public void setMapZoom(Long mapZoom) {
		this.mapZoom = mapZoom;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Date getEntCrtDate() {
		return this.entCrtDate;
	}

	public void setEntCrtDate(Date entCrtDate) {
		this.entCrtDate = entCrtDate;
	}

	public Long getFeeType() {
		return this.feeType;
	}

	public void setFeeType(Long feeType) {
		this.feeType = feeType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Long getEntType() {
		return entType;
	}

	public void setEntType(Long entType) {
		this.entType = entType;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Long getMaxUserNum() {
		return maxUserNum;
	}

	public void setMaxUserNum(Long maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSmsAccount() {
		return smsAccount;
	}

	public void setSmsAccount(String smsAccount) {
		this.smsAccount = smsAccount;
	}

	public String getSmsPwd() {
		return smsPwd;
	}

	public void setSmsPwd(String smsPwd) {
		this.smsPwd = smsPwd;
	}

	public Long getSmsType() {
		return smsType;
	}

	public void setSmsType(Long smsType) {
		this.smsType = smsType;
	}

	public Long getVisitTjStatus() {
		return visitTjStatus;
	}

	public void setVisitTjStatus(Long visitTjStatus) {
		this.visitTjStatus = visitTjStatus;
	}

	public Long getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Long reportStatus) {
		this.reportStatus = reportStatus;
	}

	public Long getVisitDistance() {
		return visitDistance;
	}

	public void setVisitDistance(Long visitDistance) {
		this.visitDistance = visitDistance;
	}

	public Long getDoubleVisitTime() {
		return doubleVisitTime;
	}

	public void setDoubleVisitTime(Long doubleVisitTime) {
		this.doubleVisitTime = doubleVisitTime;
	}

	public Long getVisitTjOrder() {
		return visitTjOrder;
	}

	public void setVisitTjOrder(Long visitTjOrder) {
		this.visitTjOrder = visitTjOrder;
	}

	public Long getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Long smsCount) {
		this.smsCount = smsCount;
	}
}