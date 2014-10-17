package com.sosgps.wzt.util;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.orm.TLocrecord;

/**
 * �����������cn.net.sosgps.entity.TLocrecord
 * 
 * @author liuhx
 * 
 */
public class PointBean {
	private Float latitude;
	private Float longitude;
	private Date gpstime;
	private boolean cover;// �жϸõ��Ƿ���������,true-��,false-��
	private Date coverGpstime;// ���һ���������ǵ�Ķ�λʱ��
	private List<TLocrecord> locrecordList;// �ϲ���Ķ�λ��¼

	/** ���¸���ҵ����Ҫ* */
	private String locDesc;
	private String jmx;
	private String jmy;
	private Float distance;
	private Long statlliteNum;
	private String locateType;
	private Float speed;
	
    public Float getSpeed() {
        return speed;
    }

    
    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Date getGpstime() {
		return gpstime;
	}

	public void setGpstime(Date gpstime) {
		this.gpstime = gpstime;
	}

	public boolean isCover() {
		return cover;
	}

	public void setCover(boolean cover) {
		this.cover = cover;
	}

	public Date getCoverGpstime() {
		return coverGpstime;
	}

	public void setCoverGpstime(Date coverGpstime) {
		this.coverGpstime = coverGpstime;
	}

	public List<TLocrecord> getLocrecordList() {
		return locrecordList;
	}

	public void setLocrecordList(List<TLocrecord> locrecordList) {
		this.locrecordList = locrecordList;
	}

	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}

	public String getJmx() {
		return jmx;
	}

	public void setJmx(String jmx) {
		this.jmx = jmx;
	}

	public String getJmy() {
		return jmy;
	}

	public void setJmy(String jmy) {
		this.jmy = jmy;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Long getStatlliteNum() {
		return statlliteNum;
	}

	public void setStatlliteNum(Long statlliteNum) {
		this.statlliteNum = statlliteNum;
	}

	public String getLocateType() {
		return locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}
}
