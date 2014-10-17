package com.sosgps.v21.cost.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.system.common.UserInfo;

public interface CostService {
	public String getCostsByTime(UserInfo userInfo);
	public String getCostHisByTime(HttpServletRequest request, UserInfo userInfo);
	public String getCostReportByTime(HttpServletRequest request, UserInfo userInfo);
	public String getCostHisReportByTime(HttpServletRequest request, UserInfo userInfo);
	public String listCostDetails(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	
	public String listCostDetailsExpExcel(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	
	public String approved(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
	
	public String getGaugeByTargetType(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
	//新华脉集团定制化费用明细
	public String listCostDetailsForXinHuaMai(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo)throws Exception;
	//新华脉集团定制化费用明细导出
	public String listCostDetailsExpExcelForXinHuaMai(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo)throws Exception;
			
}
