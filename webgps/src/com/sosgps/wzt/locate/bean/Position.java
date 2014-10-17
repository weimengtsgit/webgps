package com.sosgps.wzt.locate.bean;

import java.io.Serializable;

/**
 * @Title:λ��ʵ����
 * @Description:�����ֻ������Լ���x��y���꣬�����롢�����������������š�ʱ��
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-11 ����09:54:45
 */
public class Position implements Serializable {
	private String simcard;// �ֻ�����
	private double coordX;// x����
	private double coordY;// y����
	private int errorCode;// ������
	private String errorDesc;// ��������
	private String cityCode;// ��������
	private String dateTime;// ʱ��

	public String getSimcard() {
		return simcard;
	}

	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cityCode == null) ? 0 : cityCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(coordX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(coordY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + errorCode;
		result = prime * result
				+ ((errorDesc == null) ? 0 : errorDesc.hashCode());
		result = prime * result + ((simcard == null) ? 0 : simcard.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Position other = (Position) obj;
		if (cityCode == null) {
			if (other.cityCode != null)
				return false;
		} else if (!cityCode.equals(other.cityCode))
			return false;
		if (Double.doubleToLongBits(coordX) != Double
				.doubleToLongBits(other.coordX))
			return false;
		if (Double.doubleToLongBits(coordY) != Double
				.doubleToLongBits(other.coordY))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (errorCode != other.errorCode)
			return false;
		if (errorDesc == null) {
			if (other.errorDesc != null)
				return false;
		} else if (!errorDesc.equals(other.errorDesc))
			return false;
		if (simcard == null) {
			if (other.simcard != null)
				return false;
		} else if (!simcard.equals(other.simcard))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer re = new StringBuffer();
		re.append("simcard=" + simcard);
		re.append(" coordX=" + coordX);
		re.append(" coordY=" + coordY);
		re.append(" errorCode=" + errorCode);
		if (errorDesc != null)
			re.append(" errorDesc=" + errorDesc);
		re.append(" cityCode=" + cityCode);
		re.append(" dateTime=" + dateTime);
		return re.toString();
	}

}
