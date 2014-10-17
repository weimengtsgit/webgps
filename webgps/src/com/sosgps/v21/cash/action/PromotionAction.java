package com.sosgps.v21.cash.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.v21.cash.service.PromotionService;
import com.sosgps.wzt.system.common.UserInfo;


public class PromotionAction extends DispatchWebActionSupport {

    private PromotionService promotionService =(PromotionService)SpringHelper.getBean("promotionService");
	private static final Logger logger = LoggerFactory.getLogger(PromotionAction.class);

	//明细查询
	public ActionForward listPromotionDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        String json = promotionService.listPromotionDetails(mapping, request, response, userInfo);
		response.setContentType("text/json; charset=utf-8");
        response.getWriter().write(json);
        logger.info("[listPromotionDetails] json = "+json);//return "{totalp:" + totalp + ",data:[" + jsonSp.toString() + "]}";
		return mapping.findForward(null);
	}
	//add by 2012-12-12 上报导出
	public ActionForward listPromotionExpExcel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        promotionService.listPromotionExpExcel(mapping, request, response, userInfo);
        return mapping.findForward(null);
	}
	
	public ActionForward approved(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String json = promotionService.promotionApproved(mapping, request, response, userInfo);
		response.setContentType("text/json; charset=utf-8");
        logger.info("[approved-promotion] json = "+json);
        response.getWriter().write(json);
		return mapping.findForward(null);
	}
	
	
}
