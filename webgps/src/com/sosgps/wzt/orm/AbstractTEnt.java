package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractTEnt generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractTEnt implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private Long visitPlaceDistance;
	private Long smsType;
	private Long visitTjStatus;
	private Long reportStatus;
	private Long hashCode;//��ҵ�Ĺ�ϣ���� add by wangzhen
	private Long carGreyInterval;
	private Long persionGreyInterval;
    private String smsAccount;
    private String smsPwd;
    private Set refEntCallednumbers = new HashSet(0);
    private Set TTermGroups = new HashSet(0);
    private Set Kpis = new HashSet(0);
    private String mmsAccount;
    private String mmsPwd;
    private String version;
    
	
    public String getVersion() {
        return version;
    }

    
    public void setVersion(String version) {
        this.version = version;
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

	// Constructors

	/** default constructor */
	public AbstractTEnt() {
	}

	/** minimal constructor */
	public AbstractTEnt(String entName, Date openTime, Date endTime,
			String entStatus, Long feeType) {
		this.entName = entName;
		this.openTime = openTime;
		this.endTime = endTime;
		this.entStatus = entStatus;
		this.feeType = feeType;
	}

	/** full constructor */
	public AbstractTEnt(String entName, Date openTime, Date endTime,
			String entStatus, String centerX, String centerY, Long mapZoom,
			String logoUrl, Date entCrtDate, Long feeType, Set TTermGroups, Set Kpis) {
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
		this.TTermGroups = TTermGroups;
		this.Kpis = Kpis;
	}

	// Property accessors

	
	public String getEntCode() {
		return this.entCode;
	}

	public Set getKpis() {
		return Kpis;
	}

	public void setKpis(Set Kpis) {
		Kpis = Kpis;
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

	public Set getTTermGroups() {
		return this.TTermGroups;
	}

	public void setTTermGroups(Set TTermGroups) {
		this.TTermGroups = TTermGroups;
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

	public Set getRefEntCallednumbers() {
		return refEntCallednumbers;
	}

	public void setRefEntCallednumbers(Set refEntCallednumbers) {
		this.refEntCallednumbers = refEntCallednumbers;
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
}