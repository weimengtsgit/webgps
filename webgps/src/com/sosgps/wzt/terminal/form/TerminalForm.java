/**
 * 
 */
package com.sosgps.wzt.terminal.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

 
import com.sosgps.wzt.orm.TEntTermtype;;

/**
 * @author shiguang.zhou
 *
 */
public class TerminalForm extends ActionForm{

private static final long serialVersionUID = 1L;
	
	 
	 
	
	private String deviceId;

	private String typeCode;

	private String termName;

	private String imgUrl;

	private Long suiteId;

	private String entCode;

	private String simcard;
	
	private  long groupId;
	
	private String driverNumber;
	
	private String carTypeId;
	
	private String isAllocate;
	
	private String locateType;
	
	private String subCompany;
	
	
	
	

	/**
	 * @return the typeCode
	 */
	public String getTypeCode() {
		return this.typeCode;
	}

	/**
	 * @param typeCode the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return this.deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	 

	/**
	 * @return the termName
	 */
	public String getTermName() {
		return this.termName;
	}

	/**
	 * @param termName the termName to set
	 */
	public void setTermName(String termName) {
		this.termName = termName;
	}

	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return this.imgUrl;
	}

	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @return the suiteId
	 */
	public Long getSuiteId() {
		return this.suiteId;
	}

	/**
	 * @param suiteId the suiteId to set
	 */
	public void setSuiteId(Long suiteId) {
		this.suiteId = suiteId;
	}

	/**
	 * @return the entCode
	 */
	public String getEntCode() {
		return this.entCode;
	}

	/**
	 * @param entCode the entCode to set
	 */
	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	/**
	 * @return the simcard
	 */
	public String getSimcard() {
		return this.simcard;
	}

	/**
	 * @param simcard the simcard to set
	 */
	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public String getDriverNumber() {
		return this.driverNumber;
	}

	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}

	 

	public String getIsAllocate() {
		return this.isAllocate;
	}

	public void setIsAllocate(String isAllocate) {
		this.isAllocate = isAllocate;
	}

	public String getLocateType() {
		return this.locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCarTypeId() {
		return this.carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getSubCompany() {
		return this.subCompany;
	}

	public void setSubCompany(String subCompany) {
		this.subCompany = subCompany;
	}
	
	
}
