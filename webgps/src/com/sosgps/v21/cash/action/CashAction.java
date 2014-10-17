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
import com.sosgps.v21.cash.service.CashService;
import com.sosgps.v21.cash.service.PromotionService;
import com.sosgps.wzt.system.common.UserInfo;

public class CashAction extends DispatchWebActionSupport {

    private CashService cashService =(CashService)SpringHelper.getBean("cashService");
	private static final Logger logger = LoggerFactory.getLogger(CashAction.class);

    /**
     * 取得回款额趋势图
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getCashsByTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = cashService.getCashsByTime(request, userInfo);
        response.getWriter().write(json);
        logger.info("[getCashsByTime] json = "+json);
        return mapping.findForward(null);
    }
    
    /**
     * 取得回款额历史趋势图
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getCashHisByTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = cashService.getCashHisByTime(request, userInfo);
        response.getWriter().write(json);
        logger.info("[getCashHisByTime] json = "+json);
        return mapping.findForward(null);
    }
    
	public ActionForward listCashDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        String json = cashService.listCashDetails(mapping, request, response, userInfo);
		response.setContentType("text/json; charset=utf-8");
        response.getWriter().write(json);
        logger.info("[listCashDetails] json = "+json);
		return mapping.findForward(null);
	}
	
	public ActionForward listCashDetailsExpExcel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        cashService.listCashDetailsExpExcel(mapping, request, response, userInfo);
        return mapping.findForward(null);
	}
	//add by 2012-12-12 上报导出
	public ActionForward listEnquiriesExpExcel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        cashService.listEnquiriesExpExcel(mapping, request, response, userInfo);
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
		String json = cashService.approved(mapping, request, response, userInfo);
		response.setContentType("text/json; charset=utf-8");
        logger.info("[approved-cash] json = "+json);
        response.getWriter().write(json);
		return mapping.findForward(null);
	}
	
	/**
	 * 首页-回款额仪表盘
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getGaugeByTargetType(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        String json = cashService.getGaugeByTargetType(mapping, request, response, userInfo);
		response.setContentType("text/json; charset=utf-8");
        logger.info("[getGaugeByTargetType-cash] json = "+json);
        response.getWriter().write(json);
		return mapping.findForward(null);
	}
}
