package com.sosgps.v21.cash.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.sosgps.wzt.system.common.UserInfo;

public interface CashService {
	public String getCashHisByTime(HttpServletRequest request, UserInfo userInfo)throws Exception;
	public String getCashsByTime(HttpServletRequest request, UserInfo userInfo)throws Exception;
	public String listCashDetails(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
	
	public String listCashDetailsExpExcel(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	//add 2012-12-12 上报导出
	public String listEnquiriesExpExcel(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	
	public String approved(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;

	public String getGaugeByTargetType(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
	
}
