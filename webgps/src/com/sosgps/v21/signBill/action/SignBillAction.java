package com.sosgps.v21.signBill.action;

import java.util.Date;

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
import com.sosgps.v21.signBill.service.SignBillService;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;

public class SignBillAction extends DispatchWebActionSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(SignBillAction.class);
	private SignBillService signBillService = (SignBillService) SpringHelper
			.getBean("signBillService");

	/**
	 * 首页-签单报表:取得签单额趋势图
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getSignBillsByTime(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

		// add by 2012-12-18 签单报表:取得签单额趋势图
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "签单报表:取得签单额趋势图");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SigningGraph);
		LogFactory.newLogInstance("optLogger").info(optLog);

		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// 未登录
			return mapping.findForward(null);
		}
		String json = signBillService.getSignBillsByTime(request, userInfo);
		response.getWriter().write(json);
		logger.info("[getSignBillsByTime] json = " + json);
		return mapping.findForward(null);
	}

	/**
	 * 首页-签单报表:取得签单额历史趋势图
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getSignBillHisByTime(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

		// add by 2012-12-18 签单报表:取得签单额历史趋势图
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "签单报表:取得签单额历史趋势图");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SigningHistoryGraph);
		LogFactory.newLogInstance("optLogger").info(optLog);

		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// 未登录
			return mapping.findForward(null);
		}
		String json = signBillService.getSignBillHisByTime(request, userInfo);
		response.getWriter().write(json);
		logger.info("[getSignBillHisByTime] json = " + json);
		return mapping.findForward(null);
	}

	/**
	 * 签单额明细报表查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listSignBillDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

		// add by 2012-12-18 签单报表:签单额明细报表查询
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "签单报表:签单额明细报表查询");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SigningDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		String json = signBillService.listSignBillDetails(mapping, request,
				response, userInfo);
		response.setContentType("text/json; charset=utf-8");
		logger.info("[listSignBillDetails] json = " + json);
		response.getWriter().write(json);
		return mapping.findForward(null);
	}

	/**
	 * 签单额明细报表导出
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listSignBillDetailsExpExcel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

		// add by 2012-12-18 签单报表:签单额明细报表导出
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "签单报表:签单额明细报表导出");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SigningExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		signBillService.listSignBillDetailsExpExcel(mapping, request, response,
				userInfo);
		return mapping.findForward(null);
	}

	/**
	 * 签单审核通过
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward approved(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);

		// add by 2012-12-18 签单报表:签单审核通过
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "签单报表:签单审核通过");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SigningAudit);
		LogFactory.newLogInstance("optLogger").info(optLog);

		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String json = signBillService.approved(mapping, request, response,
				userInfo);
		response.setContentType("text/json; charset=utf-8");
		logger.info("[approved-signBill] json = " + json);
		response.getWriter().write(json);
		return mapping.findForward(null);
	}

	/**
	 * 首页-签单额仪表盘
	 * 
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

		// add by 2012-12-18 签单报表:签单额仪表盘
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "签单报表:签单额仪表盘");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SigningDashboard);
		LogFactory.newLogInstance("optLogger").info(optLog);

		String json = signBillService.getGaugeByTargetType(mapping, request,
				response, userInfo);
		response.setContentType("text/json; charset=utf-8");
		logger.info("[getGaugeByTargetType-signBill] json = " + json);
		response.getWriter().write(json);
		return mapping.findForward(null);
	}
}
