package com.sosgps.wzt.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.sos.constant.GlobalConstant;
import org.sos.helper.SpringHelper;
import org.sos.taglibs.pagination.PageIterator;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.model.TEntValue;
import com.sosgps.wzt.system.service.EnterpManageService;

/**
 * @Title: EnterpManageAction.java
 * @Description: 企业管理
 * @Copyright:
 * @Date: 2009-4-1 下午04:47:45
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public class EnterpManageAction extends DispatchWebActionSupport {
	private static final Logger logger = Logger.getLogger(EnterpManageAction.class);

	private EnterpManageService enterpManageService = (EnterpManageService) SpringHelper
			.getBean("enterpManageService");

	/**
	 * 查询企业详细信息
	 */
	public ActionForward getEntValue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String entcode = request.getParameter("entCode");
		TEntValue entValue = enterpManageService.getTEntValue(entcode);
		request.setAttribute("EntDetailInfo", entValue);
		return mapping.findForward("entDetailInfo");
	}

	/**
	 * 删除企业---即修改使用状态
	 */
	public ActionForward deleteEntValues(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String[] entCodes = request.getParameterValues("userSelected");
		enterpManageService.deleteEnterprise(entCodes);
		return mapping.findForward("entList");
	}

	/**
	 * 修改企业信息
	 */
	public ActionForward modifyEntInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String entCode = request.getParameter("entCode");
		String entName = request.getParameter("entName");
		String centerX = request.getParameter("centerX");
		String centerY = request.getParameter("centerY");
		String maxUserNum = request.getParameter("maxUserNum");
		String usageFlag = request.getParameter("usageFlag");
		String mapZoom = request.getParameter("mapZoom");
		TEntValue value = new TEntValue();
		value.setEntCode(entCode);
		value.setEntName(entName);
		value.setCenterX(centerX);
		value.setCenterY(centerY);
		value.setMapZoom(Integer.parseInt(mapZoom));
		value.setMaxUserNum(maxUserNum);
		value.setUsageFlag(usageFlag);
		enterpManageService.modifyEntInfo(value);
		return mapping.findForward("entList");
	}

	public ActionForward getEntInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String currentPageStr = request.getParameter(GlobalConstant.CURRENT_PAGE);
		String pageSizeStr = request.getParameter(GlobalConstant.PAGE_SIZE);
		if (currentPageStr == null) {
			currentPageStr = GlobalConstant.CURRENT_PAGE_INITIALIZATION;
		}
		if (pageSizeStr == null) {
			pageSizeStr = GlobalConstant.NORMAL_PAGE_SIZE;
		}
		//System.out.println("CurrentPage=: " + currentPageStr);
		//System.out.println("PageSize=: " + pageSizeStr);
		PageIterator pageItor = enterpManageService.getEntInfoList(Integer.parseInt(currentPageStr), Integer
				.parseInt(pageSizeStr));
		request.setAttribute(GlobalConstant.PAGINATION, pageItor);
		request.setAttribute("EntInfoList", pageItor.getCollection());
		return mapping.findForward("showEntList");
	}

}
