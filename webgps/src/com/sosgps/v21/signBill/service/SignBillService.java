package com.sosgps.v21.signBill.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.sosgps.wzt.system.common.UserInfo;

public interface SignBillService {
	public String getSignBillsByTime(HttpServletRequest request, UserInfo userInfo)throws Exception;
	public String getSignBillHisByTime(HttpServletRequest request, UserInfo userInfo)throws Exception;
	public String listSignBillDetails(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	public String listSignBillDetailsExpExcel(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	
	public String approved(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
	
	public String getGaugeByTargetType(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
			
}
