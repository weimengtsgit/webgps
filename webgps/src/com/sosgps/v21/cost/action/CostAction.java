package com.sosgps.v21.cost.action;

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

import com.sosgps.v21.cost.service.CostService;
import com.sosgps.wzt.system.common.UserInfo;

public class CostAction extends DispatchWebActionSupport {

    private CostService costService = (CostService) SpringHelper.getBean("costService");

    private static final Logger logger = LoggerFactory.getLogger(CostAction.class);

    /**
     * 取得回款额趋势图
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getCostsByTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = costService.getCostsByTime(userInfo);
        response.getWriter().write(json);
        //logger.info("[getCostsByTime] json = "+json);
        return mapping.findForward(null);
    }

    /**
     * 取得回款额历史趋势图
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getCostHisByTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = costService.getCostHisByTime(request, userInfo);
        response.getWriter().write(json);
        //logger.info("[getCostHisByTime] json = "+json);
        return mapping.findForward(null);
    }

    public ActionForward getCostHisReportByTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = costService.getCostHisReportByTime(request, userInfo);
        response.getWriter().write(json);
        //logger.info("[getCostHisByTime] json = "+json);
        return mapping.findForward(null);
    }

    public ActionForward getCostReportByTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = costService.getCostReportByTime(request, userInfo);
        response.getWriter().write(json);
        //logger.info("[getCostsByTime] json = "+json);
        return mapping.findForward(null);
    }

    public ActionForward listCostDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        String json = costService.listCostDetails(mapping, request, response, userInfo);
        response.setContentType("text/json; charset=utf-8");
        response.getWriter().write(json);
        //logger.info("[listCostDetails] json = "+json);
        return mapping.findForward(null);
    }

    public ActionForward listCostDetailsExpExcel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        costService.listCostDetailsExpExcel(mapping, request, response, userInfo);
        return mapping.findForward(null);
    }

    public ActionForward approved(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo userInfo = this.getCurrentUser(request);
        if (userInfo == null) {
            response.getWriter().write("{result:\"9\"}");// 未登录
            return mapping.findForward(null);
        }
        String json = costService.approved(mapping, request, response, userInfo);
        response.setContentType("text/json; charset=utf-8");
        logger.info("[approved] json = " + json);
        response.getWriter().write(json);
        return mapping.findForward(null);
    }

    /**
     * 首页-回款额仪表盘
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getGaugeByTargetType(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        String json = costService.getGaugeByTargetType(mapping, request, response, userInfo);
        response.setContentType("text/json; charset=utf-8");
        //logger.info("[getGaugeByTargetType] json = "+json);
        response.getWriter().write(json);
        return mapping.findForward(null);
    }

    /**
     * 新华脉集团定制化费用明细
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward listCostDetailsForXinHuaMai(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        String json = costService.listCostDetailsForXinHuaMai(mapping, request, response, userInfo);
        response.setContentType("text/json; charset=utf-8");
        response.getWriter().write(json);
        //logger.info("[listCostDetails] json = "+json);
        return mapping.findForward(null);
    }

    /**
     * 新华脉集团定制化费用明细导出
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward listCostDetailsExpExcelForXinHuaMai(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        costService.listCostDetailsExpExcelForXinHuaMai(mapping, request, response, userInfo);
        return mapping.findForward(null);
    }
}
