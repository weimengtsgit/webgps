package com.sosgps.v21.employeeAttend.action;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.sosgps.v21.employeeAttend.service.EmployeeAttendService;
import com.sosgps.v21.model.EmployeeAttend;
import com.sosgps.v21.model.TravelCost;
import com.sosgps.v21.travelcost.service.TravelCostService;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.v21.util.EmployeeAttendUtils;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class EmployeeAttendAction extends DispatchAction {
	private EmployeeAttendService employeeAttendService = (EmployeeAttendService) SpringHelper
			.getBean("employeeAttendService");
	private TravelCostService travelCostService = (TravelCostService) SpringHelper
			.getBean("travelCostService");
	private static final Logger logger = LoggerFactory
			.getLogger(EmployeeAttendAction.class);

	/**
	 * 导出人员考勤汇总报表下载
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportEmpAttendInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			request.setAttribute("error", "用户未登录!");
			return mapping.findForward("error");
		}

		String entCode = userInfo.getEmpCode();
		String deviceIds = request.getParameter("deviceIds");// 终端id,多个","隔开
		deviceIds = URLDecoder.decode(deviceIds, "utf-8");
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);// 如：1,2,3
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		// add by 2012-12-18 导出人员考勤汇总报表下载
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "导出人员考勤汇总报表下载");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceStaffExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
		    if(month.length() == 1){
		        month = "0"+month;
		    }
			attendanceDate = year + month;
		}
		List<EmployeeAttend> empAttes = null;// 根据条件显示数据
		List<EmployeeAttend> empAtteInfo = null;// 展示数据
		try {
			// 查询所给的年月下的员工考勤数据
			empAttes = employeeAttendService.getEmployeeAttendByCondition(
					entCode, deviceIds, attendanceDate);
			if (empAttes == null) {
				return mapping.findForward(null);
			} else {
				empAtteInfo = EmployeeAttendUtils.assemEmployeeAttendInfo(
						empAttes, year, month, entCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward(null);
		}

		// 下载该文件
		Workbook excel = null;
		response.setHeader("Content-Disposition",
				"attachment; filename=download.xlsx");
		if (empAtteInfo != null && empAtteInfo.size() > 0) {
			excel = EmployeeAttendUtils.convertEmployeeAttendFromObjectToExcel(
					empAtteInfo, year, month, XSSFWorkbook.class);
			ServletOutputStream os = response.getOutputStream();
			excel.write(os);
			os.close();
		}
		return mapping.findForward(null);
	}

	/**
	 * 重构导出人员考勤汇总报表下载
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportEmpAttendInfo1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String entCode = userInfo.getEmpCode();

		// add by 2012-12-18 重构导出人员考勤汇总报表
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "重构导出人员考勤汇总报表");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceReconstructedExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
            if(month.length() == 1){
                month = "0"+month;
            }
			attendanceDate = year + month;
		}
		List<EmployeeAttend> empAttes = null;// 根据条件查询数据
		HashMap<String, ArrayList<Integer>> hm = null;
		HashMap<String, EmployeeAttend> hmEmploy = new HashMap<String, EmployeeAttend>();
		try {
			// 查询所给的年月下的员工考勤数据
			empAttes = employeeAttendService.getEmployeeAttendByCondition(
					entCode, deviceIds, attendanceDate);
			if (empAttes == null) {
				return mapping.findForward(null);
			} else {
				for (EmployeeAttend emp : empAttes) {
					String deviceId = emp.getDeviceId();
					hmEmploy.put(deviceId, emp);
				}
				hm = EmployeeAttendUtils.assemEmployeeAttendInfo1(empAttes,
						year, month, entCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward(null);
		}
		if (hm != null && hm.size() > 0) {
			try {
				ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
				excelWorkBook = EmployeeAttendUtils
						.convertEmployeeAttendFromObjectToExcel1(excelWorkBook,
								hm, hmEmploy, year, month);
				excelWorkBook.write();
			} catch (Exception e) {
				return null;
			}
		} else {
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.write();
		}
		return null;
	}

	/**
	 * 展示人员考勤数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listEmpAttend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String entCode = userInfo.getEmpCode();

		// add by 2012-12-18 展示人员考勤数据 日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "展示人员考勤数据");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceShowStaff);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
            if(month.length() == 1){
                month = "0"+month;
            }
			attendanceDate = year + month;
		}
		List<EmployeeAttend> empAttes = null;// 根据条件查询数据
		List<EmployeeAttend> empAtteInfo = null;// 展示数据
		try {
			// 查询所给的年月下的员工考勤数据
			empAttes = employeeAttendService.getEmployeeAttendByCondition(
					entCode, deviceIds, attendanceDate);
			if (empAttes == null) {
				return mapping.findForward(null);
			} else {
				empAtteInfo = EmployeeAttendUtils.assemEmployeeAttendInfo(
						empAttes, year, month, entCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward(null);
		}
		String json = "";
		int total = 0;
		if (empAtteInfo != null && empAtteInfo.size() > 0) {
			total = empAtteInfo.size();
			json = EmployeeAttendUtils.convertEmployeeAttendFromObjectToJson(
					empAtteInfo, year, month);
		} else {
			total = 0;
		}
		System.out.println(json);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{total:" + total + ",data:[" + json + "]}");
		return mapping.findForward(null);
	}

	/**
	 * 展示公司级的考勤数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listCompanyEmpAttend(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String entCode = userInfo.getEmpCode();

		// add by 2012-12-18 展示公司级的考勤数据 日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "展示公司级的考勤数据");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceShowCompany);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
            if(month.length() == 1){
                month = "0"+month;
            }
			attendanceDate = year + month;
		}
		String attendanceStartDate = attendanceDate + "01";
		String attendanceEndDate = attendanceDate + "31";
		Integer atteStartDateI = Integer.valueOf(attendanceStartDate);
		Integer atteEndDateI = Integer.valueOf(attendanceEndDate);

		SimpleDateFormat form1 = new SimpleDateFormat("yyyyMM");
		Date d = form1.parse(attendanceDate);
		String utcStartDate = DateUtils.getStartTimeOfMonth(d) + "";
		String utcEndDate = DateUtils.getEndTimeOfMonth(d) + "";

		// String utcStartDate =
		// EmployeeAttendUtils.convertDate(attendanceStartDate,"start");
		// String utcEndDate = EmployeeAttendUtils.convertDate(attendanceEndDate
		// ,"end");
		Integer attendanceDateI = Integer.valueOf(attendanceDate);
		List<Object[]> list = null;
		String[] deviceIds_ = deviceIds.split(",");
		int dCount = deviceIds_.length;

		try {
			list = employeeAttendService.getCompanyAttendData(entCode,
					deviceIds, attendanceDateI, utcStartDate, utcEndDate,
					atteStartDateI, atteEndDateI);
			if (list == null && list.size() < 0) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String json = "";
		if (list != null && list.size() > 0) {
			json = EmployeeAttendUtils.convertCompanyAttendFromObjectToJSON(
					list, dCount);
		}
		System.out.println(json);
		response.getWriter().write("{total:5,data:[" + json + "]}");
		return null;
	}

	/**
	 * 导出公司级考勤数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportCompanyEmpAttend(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String entCode = userInfo.getEmpCode();

		// add by 2012-12-18 导出公司级考勤数据 日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "导出公司级考勤数据");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
            if(month.length() == 1){
                month = "0"+month;
            }
			attendanceDate = year + month;
		}
		String attendanceStartDate = attendanceDate + "01";
		String attendanceEndDate = attendanceDate + "31";
		Integer atteStartDateI = Integer.valueOf(attendanceStartDate);
		Integer atteEndDateI = Integer.valueOf(attendanceEndDate);

		SimpleDateFormat form1 = new SimpleDateFormat("yyyyMM");
		Date d = form1.parse(attendanceDate);
		String utcStartDate = DateUtils.getStartTimeOfMonth(d) + "";
		String utcEndDate = DateUtils.getEndTimeOfMonth(d) + "";

		// String utcStartDate =
		// EmployeeAttendUtils.convertDate(attendanceDate,"start");
		// String utcEndDate =
		// EmployeeAttendUtils.convertDate(attendanceDate,"end");
		Integer attendanceDateI = Integer.valueOf(attendanceDate);
		List<Object[]> list = null;
		String[] deviceIds_ = deviceIds.split(",");
		int dCount = deviceIds_.length;

		try {
			list = employeeAttendService.getCompanyAttendData(entCode,
					deviceIds, attendanceDateI, utcStartDate, utcEndDate,
					atteStartDateI, atteEndDateI);
			if (list == null && list.size() < 0) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 下载该文件
		/*
		 * Workbook excel = null; response.setHeader("Content-Disposition",
		 * "attachment; filename=download.xlsx"); if(list != null && list.size()
		 * > 0 ) { excel =
		 * EmployeeAttendUtils.convertCompanyAttendFromObjectToExcel1(response,
		 * list,XSSFWorkbook.class,year,month, dCount); ServletOutputStream os =
		 * response.getOutputStream(); excel.write(os); os.close(); }
		 */
		Integer yeari = Integer.valueOf(year);
		Integer monthi = Integer.valueOf(month);

		try {
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("", 30);
			Integer mountCount = DateUtils.getMonthCountByStr(
					year + "" + month, "yyyyMM");
			for (int i = 1; i <= mountCount; i++) {
				excelWorkBook.addHeader(i + "", 15);
			}
			int col = 0;
			excelWorkBook.addCell(col++, 1, "");
			for (int i = 1; i <= mountCount; i++) {
				String week = EmployeeAttendUtils.week(yeari, monthi, i);
				excelWorkBook.addCell(col++, 1, week);
			}

			excelWorkBook.addCell(0, 2, "出勤人数");
			excelWorkBook.addCell(0, 3, "缺勤人数");
			excelWorkBook.addCell(0, 4, "脱岗人数");
			excelWorkBook.addCell(0, 5, "迟到早退人数");
			excelWorkBook.addCell(0, 6, "请假人数");
			excelWorkBook.addCell(0, 7, "出差人数");
			col = 1;
			for (Object[] obj : list) {
				int attend0 = (BigDecimal) obj[0] == null ? 0
						: ((BigDecimal) obj[0]).intValue();
				int attend1 = (BigDecimal) obj[1] == null ? 0
						: ((BigDecimal) obj[1]).intValue();
				int attend3 = (BigDecimal) obj[2] == null ? 0
						: ((BigDecimal) obj[2]).intValue();
				int attend4 = (BigDecimal) obj[3] == null ? 0
						: ((BigDecimal) obj[3]).intValue();
				int attend5 = (BigDecimal) obj[4] == null ? 0
						: ((BigDecimal) obj[4]).intValue();
				int attend2 = (dCount - attend0 - attend1 - attend3 - attend4 - attend5);
				String arriveTime = (String) obj[5];
				Date arriveTimeDate = DateUtility.strToDate(arriveTime,
						"yyyyMMdd");
				Date dd = new Date();
				if (arriveTimeDate.after(dd)) {
					attend2 = 0;
				}
				excelWorkBook.addCell(col, 2, attend0 + "");
				excelWorkBook.addCell(col, 3, attend2 + "");
				excelWorkBook.addCell(col, 4, attend1 + "");
				excelWorkBook.addCell(col, 5, attend5 + "");
				excelWorkBook.addCell(col, 6, attend4 + "");
				excelWorkBook.addCell(col, 7, attend3 + "");
				col++;
			}

			excelWorkBook.write();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 考勤明细
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward attentdDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEnt().getEntCode();
		String ymd = request.getParameter("ymd");
		String deviceId = request.getParameter("deviceId");
		String stateNum = request.getParameter("stateNum");

		// add by 2012-12-18 考勤明细 日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查询考勤明细");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_Attendance);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// Long empAtteId = null;
		// if(deviceId != null && !"".equals(deviceId)) {
		// empAtteId = Long.valueOf(deviceId);
		// }
		EmployeeAttend emp = null;
		// if(empAtteId != null) {
		// emp = employeeAttendService.getEmployeeAttendById(empAtteId);
		// }
		if (ymd == null || deviceId == null || stateNum == null) {
			return null;
		}
		String[] dateArray = ymd.split(",");
		String year = dateArray[0];
		String month = dateArray[1];
        Integer monthInt = Integer.valueOf(month);
        String monthStr = "";
        if (monthInt < 10) {
            monthStr = "0" + monthInt;
        } else {
            monthStr = "" + monthInt;
        }
		String day = dateArray[2];
		Integer dayInt = Integer.valueOf(day);
		String dayStr = "";
		if (dayInt < 10) {
			dayStr = "0" + dayInt;
		} else {
			dayStr = "" + dayInt;
		}
		Integer attendDate = Integer.valueOf(year + monthStr + dayStr);
		Integer states = Integer.valueOf(stateNum);
		try {
			emp = employeeAttendService
					.getEmployeeAttendByDeviceIdAndDateAndStates(entCode,
							deviceId, attendDate, states);
		} catch (Exception e) {
			logger.info("状态明细参数：年月日" + ymd + ",deviceId:" + deviceId + "考勤状态："
					+ stateNum);
		}

		if (emp == null) {
			logger.info("员工信息存在！状态明细参数：年月日" + ymd + ",deviceId:" + deviceId
					+ "考勤状态：" + stateNum);
			return null;
		}
		String json = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (emp != null) {
			String signIntime = "";
			if (emp.getSignInTime() != null) {
				signIntime = df.format(emp.getSignInTime());
			}
			String signOuttime = "";
			if (emp.getSignOffTime() != null) {
				signOuttime = df.format(emp.getSignOffTime());
			}
			String signIndesc = "";
			String signoffdesc = "";
			if (emp.getSignInDesc() != null) {
				signIndesc = emp.getSignInDesc();
			}
			if (emp.getSignOffDesc() != null) {
				signoffdesc = emp.getSignOffDesc();
			}
			String vacateDays = "";
			String vacateCause = "";
			if (emp.getVacateCause() != null) {
				if (0 == emp.getVacateCause()) {
					vacateCause = "事假";
				}
				if (1 == emp.getVacateCause()) {
					vacateCause = "病假";
				}
				if (2 == emp.getVacateCause()) {
					vacateCause = "调休";
				}
				if (3 == emp.getVacateCause()) {
					vacateCause = "带薪假";
				}
				if (4 == emp.getVacateCause()) {
					vacateCause = "婚假";
				}
				if (5 == emp.getVacateCause()) {
					vacateCause = "产假";
				}
				if (6 == emp.getVacateCause()) {
					vacateCause = "丧假";
				}
				if (7 == emp.getVacateCause()) {
					vacateCause = "工伤";
				}
			}
			if (emp.getVacateDay() != null) {
				vacateDays = emp.getVacateDay().toString();
			}
			// 正常出勤
			if ("0".equals(stateNum)) {
				json = "{ v1: '" + signIntime + "', v2:'" + signOuttime
						+ "', v3:'" + signIndesc + "', v4:'" + signoffdesc
						+ "'}";
			}
			if ("1".equals(stateNum)) {
				json = "{ v1: '" + signIntime + "', v2:'" + signOuttime
						+ "', v3:'" + signIndesc + "', v4:'" + signoffdesc
						+ "'}";
			}
			if ("3".equals(stateNum)) {
				if (emp.getTravelCostId() != null) {
					TravelCost travelCost = travelCostService
							.getTravelCostById(emp.getTravelCostId());
					String signIntimeT = "";
					String signInDescT = "";
					String signOfftimeT = "出差未结束";
					String signOffdescT = "出差未结束";
					if (travelCost != null) {
						if (travelCost.getStartTravelTime() != null) {
							signIntimeT = df.format(travelCost
									.getStartTravelTime());
						}
						if (travelCost.getStartTravelDesc() != null) {
							signInDescT = travelCost.getStartTravelDesc();
						}
						if (travelCost.getEndTravelTime() != null) {
							signOfftimeT = df.format(travelCost
									.getEndTravelTime());
						}
						if (travelCost.getEndTravelDesc() != null) {
							signOffdescT = travelCost.getEndTravelDesc();
						}
					}
					json = "{ v1: '" + signIntimeT + "', v2:'" + signInDescT
							+ "',v3:'" + signOfftimeT + "',v4:'" + signOffdescT
							+ "'}";
				} else {
					json = "{ v1: '无记录', v2:'无记录',v3:'无记录',v4:'无记录'}";
				}
			}

			if ("4".equals(stateNum)) {
				json = "{ v1: '" + vacateCause + "', v2:'" + vacateDays + "'}";
			}
			if ("5".equals(stateNum)) {
				json = "{ v1: '" + signIntime + "', v2:'" + signOuttime
						+ "', v3:'" + signIndesc + "', v4:'" + signoffdesc
						+ "'}";
			}
		}
		response.getWriter().write(json);
		return null;
	}

	// 人员考勤报表修改 weimeng
	public ActionForward listEmpAttend1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String entCode = userInfo.getEmpCode();

		// add by 2012-12-18   人员考勤报表修改 
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查询人员考勤报表");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceModifyl);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
            if(month.length() == 1){
                month = "0"+month;
            }
			attendanceDate = year + month;
		}
		List<EmployeeAttend> empAttes = null;// 根据条件查询数据
		HashMap<String, ArrayList<Integer>> hm = null;
		HashMap<String, EmployeeAttend> hmEmploy = new HashMap<String, EmployeeAttend>();
		// List<EmployeeAttend> empAtteInfo = null;//展示数据
		try {
			// 查询所给的年月下的员工考勤数据
			empAttes = employeeAttendService.getEmployeeAttendByCondition(
					entCode, deviceIds, attendanceDate);
			if (empAttes == null) {
				return mapping.findForward(null);
			} else {
				for (EmployeeAttend emp : empAttes) {
					String deviceId = emp.getDeviceId();
					hmEmploy.put(deviceId, emp);
				}
				hm = EmployeeAttendUtils.assemEmployeeAttendInfo1(empAttes,
						year, month, entCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward(null);
		}
		String json = "";
		int total = 0;
		if (hm != null && hm.size() > 0) {
			total = hm.size();
			json = EmployeeAttendUtils.convertEmployeeAttendFromObjectToJson1(
					hm, hmEmploy, year, month);
		} else {
			total = 0;
		}
		System.out.println(json);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{total:" + total + ",data:[" + json + "]}");
		return mapping.findForward(null);
	}

	public ActionForward exportCompanyEmpAttend1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		response.setContentType("text/json; charset=UTF-8");
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String year = request.getParameter("year");
		String month = request.getParameter("month");

		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String entCode = userInfo.getEmpCode();
		// 考勤日期
		String attendanceDate = "";
		if (year == null && month == null) {
			return null;
		} else {
            if(month.length() == 1){
                month = "0"+month;
            }
			attendanceDate = year + month;
		}
		String attendanceStartDate = attendanceDate + "01";
		String attendanceEndDate = attendanceDate + "31";
		Integer atteStartDateI = Integer.valueOf(attendanceStartDate);
		Integer atteEndDateI = Integer.valueOf(attendanceEndDate);

		SimpleDateFormat form1 = new SimpleDateFormat("yyyyMM");
		Date d = form1.parse(attendanceDate);
		String utcStartDate = DateUtils.getStartTimeOfMonth(d) + "";
		String utcEndDate = DateUtils.getEndTimeOfMonth(d) + "";

		// String utcStartDate =
		// EmployeeAttendUtils.convertDate(attendanceDate,"start");
		// String utcEndDate =
		// EmployeeAttendUtils.convertDate(attendanceDate,"end");
		Integer attendanceDateI = Integer.valueOf(attendanceDate);
		List<Object[]> list = null;
		String[] deviceIds_ = deviceIds.split(",");
		int dCount = deviceIds_.length;

		try {
			list = employeeAttendService.getCompanyAttendData(entCode,
					deviceIds, attendanceDateI, utcStartDate, utcEndDate,
					atteStartDateI, atteEndDateI);
			if (list == null && list.size() < 0) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 下载该文件
		/*
		 * Workbook excel = null; response.setHeader("Content-Disposition",
		 * "attachment; filename=download.xlsx"); if(list != null && list.size()
		 * > 0 ) { excel =
		 * EmployeeAttendUtils.convertCompanyAttendFromObjectToExcel1(response,
		 * list,XSSFWorkbook.class,year,month, dCount); ServletOutputStream os =
		 * response.getOutputStream(); excel.write(os); os.close(); }
		 */
		Integer yeari = Integer.valueOf(year);
		Integer monthi = Integer.valueOf(month);

		try {
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("", 30);
			Integer mountCount = DateUtils.getMonthCountByStr(
					year + "" + month, "yyyyMM");
			for (int i = 1; i <= mountCount; i++) {
				excelWorkBook.addHeader(i + "", 15);
			}
			int col = 0;
			excelWorkBook.addCell(col++, 1, "");
			for (int i = 1; i <= mountCount; i++) {
				String week = EmployeeAttendUtils.week(yeari, monthi, i);
				excelWorkBook.addCell(col++, 1, week);
			}

			excelWorkBook.addCell(0, 2, "出勤人数");
			excelWorkBook.addCell(0, 3, "缺勤人数");
			excelWorkBook.addCell(0, 4, "脱岗人数");
			excelWorkBook.addCell(0, 5, "迟到早退人数");
			excelWorkBook.addCell(0, 6, "请假人数");
			excelWorkBook.addCell(0, 7, "出差人数");
			col = 1;
			for (Object[] obj : list) {
				int attend0 = (BigDecimal) obj[0] == null ? 0
						: ((BigDecimal) obj[0]).intValue();
				int attend1 = (BigDecimal) obj[1] == null ? 0
						: ((BigDecimal) obj[1]).intValue();
				int attend3 = (BigDecimal) obj[2] == null ? 0
						: ((BigDecimal) obj[2]).intValue();
				int attend4 = (BigDecimal) obj[3] == null ? 0
						: ((BigDecimal) obj[3]).intValue();
				int attend5 = (BigDecimal) obj[4] == null ? 0
						: ((BigDecimal) obj[4]).intValue();
				int attend2 = (dCount - attend0 - attend1 - attend3 - attend4 - attend5);
				String arriveTime = (String) obj[5];
				Date arriveTimeDate = DateUtility.strToDate(arriveTime,
						"yyyyMMdd");
				Date dd = new Date();
				if (arriveTimeDate.after(dd)) {
					attend2 = 0;
				}
				excelWorkBook.addCell(col, 2, attend0 + "");
				excelWorkBook.addCell(col, 3, attend2 + "");
				excelWorkBook.addCell(col, 4, attend1 + "");
				excelWorkBook.addCell(col, 5, attend5 + "");
				excelWorkBook.addCell(col, 6, attend4 + "");
				excelWorkBook.addCell(col, 7, attend3 + "");
				col++;
			}

			excelWorkBook.write();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}
