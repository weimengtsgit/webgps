package com.sosgps.v21.travelcost.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sos.helper.SpringHelper;

import com.sosgps.v21.model.TravelCost;
import com.sosgps.v21.travelcost.service.TravelCostService;
import com.sosgps.v21.util.TravelCostUtils;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

/**
 * ������Ϣ����
 * 
 * @author Administrator
 * 
 */
public class TravelCostAction extends DispatchAction {
	private static final Logger logger = LoggerFactory
			.getLogger(TravelCostAction.class);
	private TravelCostService travelCostService = (TravelCostService) SpringHelper
			.getBean("travelCostService");

	/**
	 * ����������Ϣ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public ActionForward exportTravelCostInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String entCode = userInfo.getEmpCode();
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		// ��ҳ
		// ��ʼ����ʱ�䣬��������ʱ��
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		// ��ȡ���״̬��0��δ��ˣ�1���
		String reviewStates = request.getParameter("reviewStates");// ���״̬
		if (reviewStates == null || "".equals(reviewStates)
				|| "-1".equals(reviewStates)) {
			reviewStates = "0,1";
		}
		// ��ҳ����
		List<TravelCost> travelCosts = travelCostService.listTravelCost(
				entCode, st, et, reviewStates, deviceIds);
		Workbook excel = null;
		// ����

		if (travelCosts != null && travelCosts.size() > 0) {
			response.setHeader("Content-Disposition",
					"attachment; filename=download.xlsx");
			excel = TravelCostUtils.convertTravelCostFromObjectToExcel(
					travelCosts, XSSFWorkbook.class);
			ServletOutputStream os = response.getOutputStream();
			excel.write(os);
			os.close();
		} else {
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.write();
		}
		// add by 2012-12-18 ����������Ϣ���� ��־��¼
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "����������Ϣ����");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_TravelListExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		return mapping.findForward(null);
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public ActionForward listTravelReports(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		String entCode = userInfo.getEmpCode();
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		// ��ҳ
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// ��ʼ����ʱ�䣬��������ʱ��
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		// ��ȡ���״̬��0��δ��ˣ�1���
		String reviewStates = request.getParameter("reviewStates");// ���״̬
		if (reviewStates == null || "".equals(reviewStates)
				|| "-1".equals(reviewStates)) {
			reviewStates = "0,1";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// ��ҳ����
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		String json = "";
		String totalJson = "";
		int total = 0;
		int count = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			count = travelCostService.getTravelCostCount(entCode, st, et,
					reviewStates, deviceIds);
			if (count > 0) {
				List<TravelCost> list = travelCostService.listTravelDetails(
						entCode, startint, limitint, st, et, reviewStates,
						deviceIds);
				if (list != null && list.size() > 0) {
					List<TravelCost> travelCosts = travelCostService
							.listTravelCost(entCode, st, et, reviewStates,
									deviceIds);
					json = TravelCostUtils.convertTravelCostFromObjectToJson(
							list, travelCosts);
				}
			}
		}

		System.out.println(json);

		// add by 2012-12-18 ��ѯ������ϸ ��־��¼
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯ������ϸ ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_TravelList);
		LogFactory.newLogInstance("optLogger").info(optLog);

		response.setContentType("text/json; charset=utf-8");

		if ("".equals(json)) {
			response.getWriter().write("{ }");
		} else {
			response.getWriter().write(
					"{total:" + total + ",data:[" + json + "]}");
		}

		return mapping.findForward(null);
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward verifyTravelCost(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String ids = request.getParameter("ids");
		ids = CharTools.javaScriptEscape(ids);
		ids = CharTools.splitAndAdd(ids);
		String flag = request.getParameter("flag");
		// flag :0Ϊȡ����� 1 Ϊ��ˣ�
		String result = travelCostService.verifyTravelCost(ids, entCode, flag);
		response.getWriter().write(result);
		
		// add by 2012-12-18 	��˲��� ��־��¼
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "������� "+result);
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_TravelVerify);
		LogFactory.newLogInstance("optLogger").info(optLog);
		
		return mapping.findForward(null);
	}

}
