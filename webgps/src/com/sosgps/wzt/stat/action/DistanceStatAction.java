package com.sosgps.wzt.stat.action;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import sun.misc.BASE64Decoder;

import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDistanceDay;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.stat.service.DistanceStatService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class DistanceStatAction extends DispatchWebActionSupport {
	private DistanceStatService distanceStatService = (DistanceStatService) SpringHelper
			.getBean("DistanceStatServiceImpl");
	private UserService userService = (UserService) SpringHelper
			.getBean("userService");

	private static final Logger logger = Logger
			.getLogger(DistanceStatAction.class);

	// sos�Զ���ͳ�����
	public ActionForward listDistanceStatByCustom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.getWriter().write("{result:\"9\"}");// δ��¼ return
		 * mapping.findForward(null); } String entCode = userInfo.getEmpCode();
		 * Long userId = userInfo.getUserId();
		 */

		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDate(st);
		Date endDate = DateUtility.strToDate(et);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");

			entCode = URLDecoder.decode(entCode, "UTF-8");
			userAccount = URLDecoder.decode(userAccount, "UTF-8");
			password = URLDecoder.decode(password, "UTF-8");

			// entCode = CharTools.javaScriptEscape(entCode);
			// userAccount = CharTools.javaScriptEscape(userAccount);
			// password = CharTools.javaScriptEscape(password);

			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));

			password = new String(new BASE64Decoder().decodeBuffer(password));
			logger.info("[listDistanceStatByCustom] entCode = " + entCode
					+ ";userAccount = " + userAccount + ";password = "
					+ password);
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				logger.info("[listDistanceStatByCustom] ��Ȩ����");
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = distanceStatService.listDistanceStatByCustom(
					entCode, userId, 1, 65536, startDate, endDate, searchValue,
					deviceIds);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�����", 15);
			excelWorkBook.addHeader("ͳ��ʱ��", 35);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String deviceId = (String) objects[0];
				Float distance = (Float) objects[1];
				String vehicleNumber = (String) objects[2];
				Date tjDate = (Date) objects[3];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, distance == null ? "0"
						: distance.toString());
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateToStr(tjDate)));
			}

			// add by 2012-12-18 zss �������ͳ������
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userId);
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_DISTANCE);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + " �������ͳ������");
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);
		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// searchLog
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userId);
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_DISTANCE);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ���ͳ�Ƴɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService.listDistanceStatByCustom(
					entCode, userId, pageNo, pageSize, startDate, endDate,
					searchValue, deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					Float distance = (Float) objects[1];
					String vehicleNumber = (String) objects[2];
					Date tjDate = (Date) objects[3];
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ���ƺ�
					java.text.DecimalFormat myformat = new java.text.DecimalFormat(
							"0.00");
					// System.out.println("distance:" +
					// myformat.format(distance)+",");
					jsonSb.append("distance:" + myformat.format(distance) + ",");// ����һ������룬��λkm

					// jsonSb.append("distance:'" + distance + "',");// �����
					jsonSb.append("tjdate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateToStr(tjDate)) + "'");// ͳ��ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos��ͳ�����
	public ActionForward listDistanceStatByDay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("theDate");// ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����

		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date theDate = DateUtility.strToDate(st);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = distanceStatService.listDistanceStatByDay(
					entCode, userId, 1, 65536, theDate, searchValue, deviceIds);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�����", 15);
			excelWorkBook.addHeader("ͳ��ʱ��", 35);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String deviceId = (String) objects[0];
				Float distance = (Float) objects[1];
				String vehicleNumber = (String) objects[2];
				Date tjDate = (Date) objects[3];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, distance == null ? "0"
						: distance.toString());
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateToStr(tjDate)));
			}
			excelWorkBook.write();
			return null;
		}

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService.listDistanceStatByDay(
					entCode, userId, pageNo, pageSize, theDate, searchValue,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					Float distance = (Float) objects[1];
					String vehicleNumber = (String) objects[2];
					Date tjDate = (Date) objects[3];
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ���ƺ�
					jsonSb.append("distance:'" + distance + "',");// �����
					jsonSb.append("tjdate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateToStr(tjDate)) + "'");// ͳ��ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos��ͳ�����
	public ActionForward listDistanceStatByMonth(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("theMonth");// ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����

		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date theMonth = DateUtility.strToDate(st);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = distanceStatService
					.listDistanceStatByMonth(entCode, userId, 1, 65536,
							theMonth, searchValue, deviceIds);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�����", 15);
			excelWorkBook.addHeader("ͳ��ʱ��", 35);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String deviceId = (String) objects[0];
				Float distance = (Float) objects[1];
				String vehicleNumber = (String) objects[2];
				Date tjDate = (Date) objects[3];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, distance == null ? "0"
						: distance.toString());
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateToStr(tjDate)));
			}
			excelWorkBook.write();
			return null;
		}

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService.listDistanceStatByMonth(
					entCode, userId, pageNo, pageSize, theMonth, searchValue,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					Float distance = (Float) objects[1];
					String vehicleNumber = (String) objects[2];
					Date tjDate = (Date) objects[3];
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ���ƺ�
					jsonSb.append("distance:'" + distance + "',");// �����
					jsonSb.append("tjdate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateToStr(tjDate)) + "'");// ͳ��ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos��ͳ�����
	public ActionForward listDistanceStatByYear(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("theYear");// ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����

		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date theYear = DateUtility.strToDate(st);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = distanceStatService.listDistanceStatByYear(
					entCode, userId, 1, 65536, theYear, searchValue, deviceIds);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�����", 15);
			excelWorkBook.addHeader("ͳ��ʱ��", 35);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String deviceId = (String) objects[0];
				Float distance = (Float) objects[1];
				String vehicleNumber = (String) objects[2];
				Date tjDate = (Date) objects[3];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, distance == null ? "0"
						: distance.toString());
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateToStr(tjDate)));
			}
			excelWorkBook.write();
			return null;
		}

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService.listDistanceStatByYear(
					entCode, userId, pageNo, pageSize, theYear, searchValue,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					Float distance = (Float) objects[1];
					String vehicleNumber = (String) objects[2];
					Date tjDate = (Date) objects[3];
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ���ƺ�
					jsonSb.append("distance:'" + distance + "',");// �����
					jsonSb.append("tjdate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateToStr(tjDate)) + "'");// ͳ��ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	public ActionForward listTotalDistanceStat(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.getWriter().write("{result:\"9\"}");// δ��¼ return
		 * mapping.findForward(null); } String entCode = userInfo.getEmpCode();
		 * Long userId = userInfo.getUserId();
		 */
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDate(st);
		Date endDate = DateUtility.strToDate(et);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");

			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");

			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);

			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = distanceStatService.listTotalDistanceStat(
					entCode, userId, 1, 65536, searchValue, deviceIds);

			logger.info("[DistanceStatAction] listTotalDistanceStat " + list);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�����", 15);
			int row = 0;
			logger.info("[DistanceStatAction] listTotalDistanceStat "
					+ list.getResult());
			for (Object[] objects : list.getResult()) {
				String deviceId = (String) objects[0];
				Float distance = (Float) objects[1];
				String vehicleNumber = (String) objects[2];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, distance == null ? "0"
						: distance.toString());
			}
			// add by 2012-12-18 zss ���������ͳ��
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_TOTALDISTANCE);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "���������ͳ�Ʊ�ɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);
		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// searchLog
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(entCode);
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userId);
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_TOTALDISTANCE);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ�����ͳ�Ƴɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService.listTotalDistanceStat(
					entCode, userId, pageNo, pageSize, searchValue, deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					Float distance = (Float) objects[1];
					String vehicleNumber = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ���ƺ�
					java.text.DecimalFormat myformat = new java.text.DecimalFormat(
							"0.00");
					// System.out.println("distance:" +
					// myformat.format(distance)+",");
					jsonSb.append("distance:" + myformat.format(distance) + "");// ����һ������룬��λkm
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// ��ʱ��β�ѯ���
	public ActionForward listTimeDistanceStatByCustom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);

		// Date startDate = DateUtility.strToDate(st);
		// Date endDate = DateUtility.strToDate(et);
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");

			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");

			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);

			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = distanceStatService
					.listTimeDistanceStatByCustom(0, 65536, st, et, deviceIds,
							searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("��ʼʱ��", 35);
			excelWorkBook.addHeader("����ʱ��", 35);
			excelWorkBook.addHeader("�����", 35);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String vehicleNumber = (String) objects[0];
				String max_gpstime = (String) objects[2];
				String min_gpstime = (String) objects[3];
				BigDecimal sub_distance = (BigDecimal) objects[4];
				Float sub_distance_ = 0f;
				if (sub_distance != null) {
					sub_distance_ = sub_distance.floatValue();
				}
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(min_gpstime));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(max_gpstime));
				java.text.DecimalFormat myformat = new java.text.DecimalFormat(
						"0.00");
				excelWorkBook.addCell(col++, row,
						myformat.format(sub_distance_));
			}
			UserInfo userInfo = this.getCurrentUser(request);
			// searchLog
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_HOURDISTANCE);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "������ʱ���ͳ��Excel�ɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);

		// searchLog
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_HOURDISTANCE);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ��ʱ���ͳ�Ƴɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService
					.listTimeDistanceStatByCustom(startint, pageSize, st, et,
							deviceIds, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String vehicleNumber = (String) objects[0];
					String deviceId = (String) objects[1];
					String max_gpstime = (String) objects[2];
					String min_gpstime = (String) objects[3];
					BigDecimal sub_distance = (BigDecimal) objects[4];
					Float sub_distance_ = 0f;
					if (sub_distance != null) {
						sub_distance_ = sub_distance.floatValue();
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ���ƺ�
					java.text.DecimalFormat myformat = new java.text.DecimalFormat(
							"0.00");
					jsonSb.append("distance:" + myformat.format(sub_distance_)
							+ ",");
					jsonSb.append("starttime:'"
							+ CharTools.javaScriptEscape(min_gpstime) + "',");
					jsonSb.append("endtime:'"
							+ CharTools.javaScriptEscape(max_gpstime) + "'");
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	public ActionForward listAttendanceRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		st = st.replace("-", "");
		et = et.replace("-", "");
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);

		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");

			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");

			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);

			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = distanceStatService.listAttendanceRecord(0,
					65536, st, et, deviceIds);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("��������", 15);
			excelWorkBook.addHeader("ǩ��ʱ��", 20);
			excelWorkBook.addHeader("ǩ��λ��", 40);
			excelWorkBook.addHeader("ǩ��ʱ��", 20);
			excelWorkBook.addHeader("ǩ��λ��", 40);
			// excelWorkBook.addHeader("����ʱ��", 20);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String termName = (String) objects[0];
				Number attendanceDate = (Number) objects[1];
				String signinTime = (String) objects[2];
				String signinDesc = (String) objects[3];
				String signoffTime = (String) objects[4];
				String signoffDesc = (String) objects[5];
				String groupName = (String) objects[8];

				String newAttendanceDate = attendanceDate.toString().substring(
						0, 4)
						+ "-"
						+ attendanceDate.toString().substring(4, 6)
						+ "-"
						+ attendanceDate.toString().substring(6, 8);

				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(groupName));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(termName));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(newAttendanceDate));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(signinTime));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(signinDesc));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(signoffTime));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(signoffDesc));
				// excelWorkBook.addCell(col++, row,
				// CharTools.javaScriptEscape(createTime));
			}
			//add by 2012-12-18 zss �������ڼ�¼��
			UserInfo userInfo = this.getCurrentUser(request);

			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_ATTENDANCE_RECORD);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "�������ڼ�¼��ɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			
			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);

		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_ATTENDANCE_RECORD);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ���ڼ�¼�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = distanceStatService.listAttendanceRecord(
					startint, pageSize, st, et, deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String termName = (String) objects[0];
					Number attendanceDate = (Number) objects[1];
					String signinTime = (String) objects[2];
					String signinDesc = (String) objects[3];
					String signoffTime = (String) objects[4];
					String signoffDesc = (String) objects[5];
					String deviceId = (String) objects[6];
					String createTime = (String) objects[7];
					String groupName = (String) objects[8];

					String newAttendanceDate = attendanceDate.toString()
							.substring(0, 4)
							+ "-"
							+ attendanceDate.toString().substring(4, 6)
							+ "-"
							+ attendanceDate.toString().substring(6, 8);

					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("groupName:'"
							+ CharTools.javaScriptEscape(groupName) + "',");
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(termName) + "',");
					jsonSb.append("attendanceDate:'"
							+ CharTools.javaScriptEscape(newAttendanceDate)
							+ "',");
					jsonSb.append("signinTime:'"
							+ CharTools.javaScriptEscape(signinTime) + "',");
					jsonSb.append("signinDesc:'"
							+ CharTools.javaScriptEscape(signinDesc) + "',");
					jsonSb.append("signoffTime:'"
							+ CharTools.javaScriptEscape(signoffTime) + "',");
					jsonSb.append("signoffDesc:'"
							+ CharTools.javaScriptEscape(signoffDesc) + "'");

					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}
}
