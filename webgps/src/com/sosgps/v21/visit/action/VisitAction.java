package com.sosgps.v21.visit.action;

import java.net.URLDecoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sos.helper.SpringHelper;

import com.sosgps.v21.model.Visit;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.v21.util.VisitUtils;
import com.sosgps.v21.visit.service.VisitService;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

public class VisitAction extends DispatchAction {
	private static final Logger logger = LoggerFactory
			.getLogger(VisitAction.class);
	private VisitService visitService = (VisitService) SpringHelper
			.getBean("visitService");

	/**
	 * ����ǩ��ǩ����¼
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportVisit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			request.setAttribute("error", "�û�δ��¼!");
			return mapping.findForward("error");
		}
		// entCode
		String entCode = userInfo.getEmpCode();
		// ������ѯ�ݷü�¼�б�
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
		String startTimeStr = request.getParameter("startTime");
		startTimeStr = URLDecoder.decode(startTimeStr, "utf-8");
		String endTimeStr = request.getParameter("endTime");
		endTimeStr = URLDecoder.decode(endTimeStr, "utf-8");
		String poiName = request.getParameter("poiName");
		poiName = URLDecoder.decode(poiName, "utf-8");
		String deviceIdsStr = request.getParameter("deviceIds");// �ն�id,���","����
		deviceIdsStr = URLDecoder.decode(deviceIdsStr, "utf-8");
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		deviceIdsStr = CharTools.splitAndAdd(deviceIdsStr);
		if (!StringUtils.isEmpty(startTimeStr)) {
			Date startTime = DateUtils.strToDateTime(startTimeStr);
			paramMap.put("startTime", startTime.getTime());
		}
		if (!StringUtils.isEmpty(endTimeStr)) {
			Date endTime = DateUtils.strToDateTime(endTimeStr);
			paramMap.put("endTime", endTime.getTime());
		}
		if (!StringUtils.isEmpty(poiName)) {
			paramMap.put("poiName", poiName);
		}
		paramMap.put("deviceIds", deviceIdsStr);
		paramMap.put("entCode", entCode);
		paramMap.put("orderby", "createOn desc, groupName, deviceId, poiName");
		List<Visit> visits = visitService.queryVisitsByCondition(paramMap,
				entCode);
		Workbook excel = VisitUtils.convertVisitFromObjectToExcel(visits,
				XSSFWorkbook.class);

		// add by 2012-12-18 ����ǩ��ǩ����¼
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "����ԭʼǩ��ǩ����¼Excel");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);
		// ����
		response.setHeader("Content-Disposition",
				"attachment; filename=download.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}

	/**
	 * �������ڱ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportVisitStat(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			request.setAttribute("error", "�û�δ��¼!");
			return mapping.findForward("error");
		}
		// entCode
		String entCode = userInfo.getEmpCode();
		// ������ѯ�ݷü�¼�б�
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
		String startTimeStr = request.getParameter("startTime");
		startTimeStr = URLDecoder.decode(startTimeStr, "utf-8");
		String endTimeStr = request.getParameter("endTime");
		endTimeStr = URLDecoder.decode(endTimeStr, "utf-8");
		String poiName = request.getParameter("poiName");
		poiName = URLDecoder.decode(poiName, "utf-8");
		String deviceIdsStr = request.getParameter("deviceIds");// �ն�id,���","����
		deviceIdsStr = URLDecoder.decode(deviceIdsStr, "utf-8");
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		deviceIdsStr = CharTools.splitAndAdd(deviceIdsStr);
		if (!StringUtils.isEmpty(startTimeStr)) {
			Date startTime = DateUtils.strToDateTime(startTimeStr);
			paramMap.put("startTime", startTime.getTime());
		}
		if (!StringUtils.isEmpty(endTimeStr)) {
			Date endTime = DateUtils.strToDateTime(endTimeStr);
			paramMap.put("endTime", endTime.getTime());
		}
		if (!StringUtils.isEmpty(poiName)) {
			paramMap.put("poiName", poiName);
		}
		paramMap.put("deviceIds", deviceIdsStr);
		paramMap.put("entCode", entCode);
		paramMap.put("orderby", "createOn asc, groupName, deviceId, poiName");
		List<Visit> visits = visitService.queryVisitsByCondition(paramMap,
				entCode);
		Workbook excel = VisitUtils.convertVisitStatFromObjectToExcel(visits,
				XSSFWorkbook.class);

		// add by 2012-12-18 �����ݷÿ��ڱ�Excel
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "�����ݷÿ��ڱ�Excel");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitListExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// ����
		response.setHeader("Content-Disposition",
				"attachment; filename=download.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}

	/**
	 * �ͻ��ݷô������б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listVisitRanks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listVisitRanks(mapping, request, response,
				userInfo);
		response.setContentType("text/json; charset=utf-8");
		logger.info("[listVisitRanks] json = " + json);

		// add by 2012-12-18 ��ѯ�ͻ��ݷô������б�
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯ�ͻ��ݷô������б�");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitRanking);
		LogFactory.newLogInstance("optLogger").info(optLog);

		response.getWriter().write(json);
		return mapping.findForward(null);
	}

	/**
	 * �ͻ��ݷô������б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listVisitRanksExpExcel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listVisitRanksExpExcel(mapping, request,
				response, userInfo);
		logger.info("[listVisitRanksExpExcel] json = " + json);
		return mapping.findForward(null);
	}

	/**
	 * ȡ��ҵ��Ա��������ͼ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getVisitsByTime(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// δ��¼
			return mapping.findForward(null);
		}
		String json = visitService.getVisitsByTime(userInfo);
		response.getWriter().write(json);

		// add by 2012-12-18 ȡ��ҵ��Ա��������ͼ
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "ȡ��ҵ��Ա��������ͼ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitGraph);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// logger.info("[getVisitsByTime] json = " + json);
		return mapping.findForward(null);
	}

	/**
	 * ȡ��ҵ��Ա������ʷ����ͼ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getVisitHisByTime(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// δ��¼
			return mapping.findForward(null);
		}
		String json = visitService.getVisitHisByTime(userInfo);
		response.getWriter().write(json);

		// add by 2012-12-18ȡ��ҵ��Ա������ʷ����ͼ
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "ȡ��ҵ��Ա������ʷ����ͼ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitHistorical);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// logger.info("[getVisitHisByTime] json = " + json);
		return mapping.findForward(null);
	}

	/**
	 * ȡ�ÿͻ����ݷ�����ͼ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCusVisitsByTime(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// δ��¼
			return mapping.findForward(null);
		}
		String json = visitService.getCusVisitsByTime(userInfo);
		response.getWriter().write(json);
		logger.info("[getCusVisitsByTime] json = " + json);

		// add by 2012-12-18 ȡ�ÿͻ����ݷ�����ͼ
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "ȡ�ÿͻ����ݷ�����ͼ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitedGraph);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	/**
	 * ȡ�ÿͻ����ݷ���ʷ����ͼ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCusVisitHisByTime(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// δ��¼
			return mapping.findForward(null);
		}
		String json = visitService.getCusVisitHisByTime(userInfo);
		response.getWriter().write(json);
		logger.info("[getCusVisitHisByTime] json = " + json);

		// add by 2012-12-18 ȡ�ÿͻ����ݷ���ʷ����ͼ
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "ȡ�ÿͻ����ݷ���ʷ����ͼ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitedHistorical);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	public ActionForward listVisitReports(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listVisitDetails(mapping, request, response,
				userInfo);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(json);
		logger.info("[listSignBillDetails] json = " + json);

//		// add by 2012-12-18 ��ѯҵ��Ա������ϸ��¼
//		TOptLog optLog = new TOptLog();
//		optLog.setEmpCode(userInfo.getEmpCode());
//		optLog.setUserName(userInfo.getUserAccount());
//		optLog.setAccessIp(userInfo.getIp());
//		optLog.setUserId(userInfo.getUserId());
//		optLog.setOptTime(new Date());
//		optLog.setResult(1L);
//		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯҵ��Ա������ϸ��¼");
//		optLog.setFunFType(LogConstants.LOG_STAT);
//		optLog.setFunCType(LogConstants.LOG_STAT_VisitDetail);
//		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	/**
	 * ��ҳ-Ա�����ô�����Ǳ���
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
		String json = visitService.getGaugeByTargetType(mapping, request,
				response, userInfo);
		response.setContentType("text/json; charset=utf-8");
		// logger.info("[getGaugeByTargetType] json = "+json);
		response.getWriter().write(json);

		// add by 2012-12-18 Ա�����ô�����Ǳ���
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "Ա�����ô�����Ǳ���");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitDashboard);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	/**
	 * ��ҳ-Ա�����ô�����Ǳ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getGaugeByTargetTypeCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.getGaugeByTargetTypeCus(mapping, request,
				response, userInfo);
		response.setContentType("text/json; charset=utf-8");
		// logger.info("[getGaugeByTargetType] json = "+json);
		response.getWriter().write(json);

		// add by 2012-12-18 �ͻ����ݷ��Ǳ���
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "�ͻ����ݷ��Ǳ���");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitedDashboard);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	/**
	 * �ͻ����ݷ�ͳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listCustomVisitCountTj(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listCustomVisitCountTj(mapping, request,
				response, userInfo);
		if(json == null){
		    return null;
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(json);
		return mapping.findForward(null);
	}

	/**
	 * �ͻ����ݷ�ͳ��,�����������תҳ��,��ѯ��ϸ�ı��ݷÿͻ���Ϣͬ2.0
	 */
	public ActionForward listCustomVisitCountTjByCustom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listCustomVisitCountTjByCustom(mapping,
				request, response, userInfo);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(json);

		// add by 2012-12-18 //��ѯ��ϸ�ı��ݷÿͻ���Ϣ
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯ��ϸ�ı��ݷÿͻ���Ϣ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitedDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	public ActionForward listVisitCountTjSql(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listVisitCountTjSql(mapping, request,
				response, userInfo);
		if(json==null){
		    return null;
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(json);

		// add by 2012-12-18 //��ѯҵ��Ա����ͳ�Ʊ�
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯҵ��Ա����ͳ�Ʊ�v21");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitStatistics);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	public ActionForward listVisitCountTjByCustom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String json = visitService.listVisitCountTjByCustom(mapping, request,
				response, userInfo);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(json);
		// add by 2012-12-18 //��ת��ѯҵ��Ա������ϸ��
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯҵ��Ա������ϸ��");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitStatisticsDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}
}
