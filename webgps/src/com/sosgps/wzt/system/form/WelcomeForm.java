/**
 * <p>Title:Toten</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: WinChannel </p>
 * <p>Date:Jul 7, 2006</p>
 * @author bxz
 * @version 1.0
 */
package com.sosgps.wzt.system.form;

import java.util.List;


public class WelcomeForm extends BaseForm {
	private List topGroupList ;
	private String topGroupId;
	private List holidayList;
	private List comNoticeList;
	private List dealerNoticeList;
	/**
	 * @return Returns the topGroupId.
	 */
	public String getTopGroupId() {
		return topGroupId;
	}
	/**
	 * @param topGroupId The topGroupId to set.
	 */
	public void setTopGroupId(String topGroupId) {
		this.topGroupId = topGroupId;
	}
	/**
	 * @return Returns the topGroupList.
	 */
	public List getTopGroupList() {
		return topGroupList;
	}
	/**
	 * @param topGroupList The topGroupList to set.
	 */
	public void setTopGroupList(List topGroupList) {
		this.topGroupList = topGroupList;
	}
	/**
	 * @return Returns the comNoticeList.
	 */
	public List getComNoticeList() {
		return comNoticeList;
	}
	/**
	 * @param comNoticeList The comNoticeList to set.
	 */
	public void setComNoticeList(List comNoticeList) {
		this.comNoticeList = comNoticeList;
	}
	/**
	 * @return Returns the dealerNoticeList.
	 */
	public List getDealerNoticeList() {
		return dealerNoticeList;
	}
	/**
	 * @param dealerNoticeList The dealerNoticeList to set.
	 */
	public void setDealerNoticeList(List dealerNoticeList) {
		this.dealerNoticeList = dealerNoticeList;
	}
	/**
	 * @return Returns the holidayList.
	 */
	public List getHolidayList() {
		return holidayList;
	}
	/**
	 * @param holidayList The holidayList to set.
	 */
	public void setHolidayList(List holidayList) {
		this.holidayList = holidayList;
	}
}
