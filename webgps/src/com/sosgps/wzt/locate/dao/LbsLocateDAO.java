package com.sosgps.wzt.locate.dao;

/**
 * @Title: LbsLocateDAO.java
 * @Description: LBS定位接口
 * @Copyright:
 * @Date: 2009-3-25 下午05:45:57
 * @Copyright (c) 2008 Company: www.mapabc.com
 * @author yang.lei
 * @version 1.0
 */
public interface LbsLocateDAO {
	public void insertLbsLocrecord(String simcard, String x, String y, String entCode, String lbsTime,
			String errorCode, String errorDesc, String locDesc, String lngX, String latY);
	
	public void insertLastLocrecordByLbsData(String simcard, String x, String y, String lbsTime);
	
	public boolean isExistRecordLastLoc(String simcard);
	
	public void deleteLastLocByDeviceId(String simcard);
}
