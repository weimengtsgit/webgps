package com.sosgps.wzt.stat.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import sun.misc.BASE64Decoder;
import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.orm.TVisitTj;
import com.sosgps.wzt.stat.report.DownLoadReportService;
import com.sosgps.wzt.stat.report.impl.DownLoadReportServiceImpl;
import com.sosgps.wzt.stat.service.TReportFilepathService;
import com.sosgps.wzt.stat.service.VisitStatService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

/**
 * @Title:�ݷ�ͳ��action
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����09:10:37
 * @modify by zhumingzhe 2010-09-15
 */
public class VisitStatAction extends DispatchWebActionSupport {
	private static final Logger logger = Logger
			.getLogger(VisitStatAction.class);
	private VisitStatService visitStatService = (VisitStatService) SpringHelper
			.getBean("VisitStatServiceImpl");
	private TReportFilepathService tReportFilepathService = (TReportFilepathService) SpringHelper
			.getBean("TReportFilepathImpl");
	private DownLoadReportService downLoadReportService = (DownLoadReportServiceImpl) SpringHelper
			.getBean("downLoadReportService");
	private UserService userService = (UserService) SpringHelper
			.getBean("userService");

	// sos �鿴ҵ��Ա���ڱ���ͳ��
	@SuppressWarnings("unchecked")
	public ActionForward listAttendanceReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "10" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String deviceIdsStr = request.getParameter("deviceIds");// �ն�id�����","����
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		response.setContentType("text/json; charset=utf-8");

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.killNullString(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
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

			// TOptLog tOptLog = new TOptLog();
			// tOptLog.setEmpCode(entCode);
			// tOptLog.setUserName(userAccount);
			// tOptLog.setUserId(userId);
			// tOptLog.setFunFType(LogConstants.LOG_STAT);
			// tOptLog.setFunCType(LogConstants.LOG_STAT_SALSEMAN);

			try {
				// ��ȡָ��ʱ�䷶Χ�ڱ���·������ (deviceIDs ��ʼʱ�䡢 ����ʱ�� ��ҵ����)
				List<String> pathes = tReportFilepathService.getFilepathes(
						deviceIds, entCode, startDate, endDate);
				if (pathes.size() == 0) {
					logger.info("listAttendanceReport query result is zero");
					response.getWriter().write("����δ����");// δ���ɱ���
					return mapping.findForward(null);
				}
				String filepath = downLoadReportService.generateDownloadReport(
						entCode, pathes, true);
				if (filepath == null) {

					// tOptLog.setResult(new Long(0));
					// tOptLog.setOptDesc(userAccount+" ����ҵ��Ա���ڱ���ʧ��");
					// LogFactory.newLogInstance("optLogger").info(tOptLog);

					response.getWriter().write("����ҵ��Ա���ڱ���ʧ��,�����ļ�Ϊnull");// ����ʧ��
					return mapping.findForward(null);
				}
				try {
					this.downloadLocal(response, filepath, entCode + ".zip");
				} catch (IOException ex) {
					response.getWriter().write("����ҵ��Ա���ڱ���ʧ��,���ɱ����ļ�ʧ��");// ����ʧ��
					return mapping.findForward(null);
				}
				
				// add by 2012-12-18 zss ������ϸ��־
				UserInfo userInfo = this.getCurrentUser(request);
				TOptLog tOptLog = new TOptLog();
				tOptLog.setEmpCode(userInfo.getEmpCode());
				tOptLog.setUserName(userInfo.getUserAccount());
				tOptLog.setUserId(userId);
				tOptLog.setAccessIp(userInfo.getIp());
				tOptLog.setOptTime(new Date());
				tOptLog.setFunFType(LogConstants.LOG_STAT);
				tOptLog.setFunCType(LogConstants.LOG_STAT_SALSEMAN);
				tOptLog.setResult(new Long(1));
				tOptLog.setOptDesc(userInfo.getUserAccount() + "����ָ��ʱ�䷶Χ����ϸ��־����ɹ�");
				LogFactory.newLogInstance("optLogger").info(tOptLog);

				// tOptLog.setResult(new Long(1));
				// tOptLog.setOptDesc(userAccount+"����ҵ��Ա���ڱ���ɹ����ļ����ƣ�"+entCode+".zip");
				// LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return mapping.findForward(null);
		}
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null || st == null || et == null
				|| deviceIdsStr == null) {
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
		tOptLog.setFunCType(LogConstants.LOG_STAT_SALSEMAN);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ��ϸ��־�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			int startint = Integer.parseInt(start);
			int pageSize = Integer.parseInt(limit);
			// int pageNo = startint / pageSize + 1;
			Page<Object[]> list = visitStatService.listAttendanceReport(
					deviceIds, entCode, userId, startint, pageSize, st, et,
					searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					Long week = ((BigDecimal) objects[0]).longValue();
					String startTime = (String) objects[1];
					String endTime = (String) objects[2];
					String vehicleNumber = (String) objects[3];
					String simcard = (String) objects[4];
					String groupName = (String) objects[5];
					String deviceId = (String) objects[6];
					String openTime = (String) objects[7];
					String closeTime = (String) objects[8];
					jsonSb.append("{");
					jsonSb.append("week:'"
							+ CharTools.killNullLong2String(week, "127") + "',");
					jsonSb.append("startTime:'"
							+ CharTools.killNullString(startTime, "08:00")
							+ "',");
					jsonSb.append("endTime:'"
							+ CharTools.killNullString(endTime, "18:00") + "',");
					jsonSb.append("deviceId:'"
							+ CharTools.javaScriptEscape(deviceId) + "',");
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ����
					jsonSb.append("groupName:'"
							+ CharTools.javaScriptEscape(groupName) + "',");// ����
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// �ֻ�����
					jsonSb.append("openTime:'"
							+ CharTools.javaScriptEscape(openTime) + "',");// ����ʱ��
					jsonSb.append("closeTime:'"
							+ CharTools.javaScriptEscape(closeTime) + "'");// �ػ�ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos �鿴ҵ��Ա���ڱ�����ϸͳ��
	@SuppressWarnings("static-access")
	public ActionForward listAttendanceReportDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}

		// add by 2012-12-18 �鿴ҵ��Ա���ڱ�����ϸͳ��
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "�鿴ҵ��Ա���ڱ�����ϸͳ��");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�id
		deviceId = CharTools.splitAndAdd(CharTools.javaScriptEscape(deviceId));
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "1" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		String week = request.getParameter("week");
		week = CharTools.killNullString(week, "127");
		long deviceWeek = Long.parseLong(week);
		int todayWeekNum = DateUtility
				.getDateWeekNum(DateUtility.strToDate(st));
		Double todayWeekBinary = Math.pow(2, todayWeekNum - 1);
		if ((deviceWeek & todayWeekBinary.intValue()) != todayWeekBinary) {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("deviceId:'',");// deviceId
			sb.append("vehicleNumber:'',");// ����
			sb.append("simcard:'',");// �ֻ�����
			sb.append("gpsTime:'',");// ʱ��
			sb.append("pd:'" + DateUtility.getDateWeekCnName(todayWeekNum - 1)
					+ "��Ϣ��',");// λ������
			sb.append("visitId:'',");// ��ע������id
			sb.append("visitName:'',");// ���ÿͻ�
			sb.append("imsi:''");// imsi
			sb.append("}");
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(
					"{total: 1,data:[" + sb.toString() + "]}");
			return mapping.findForward(null);
		}

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService
					.listAttendanceReportDetail(deviceId, pageNo, pageSize,
							startDate, endDate, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				double lastlngX = -1;
				double lastLatY = -1;
				String lastPosDesc = "";
				String locDesc = "";// λ������
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TLocrecord locrecord = (TLocrecord) objects[0];
					String vehicleNumber = (String) objects[1];
					String simcard = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + locrecord.getDeviceId() + "',");// deviceId
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ����
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// �ֻ�����
                    jsonSb.append("gpsTime:'"
                            + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(locrecord
                                    .getGpstime())) + "',");// ʱ��
                    jsonSb.append("inputTime:'"
                            + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(locrecord
                                    .getInputdate())) + "',");// ���ʱ��
                    // �����ȡλ��Ϊ��
					if (locrecord.getLocDesc() == null
							|| locrecord.getLocDesc().equals("")) {
						// ����γ�����겻Ϊ0
						if (locrecord.getLatitude() > 0
								&& locrecord.getLongitude() > 0) {
							double[] xs = { locrecord.getLongitude() };
							double[] ys = { locrecord.getLatitude() };
							if (lastlngX != locrecord.getLongitude()
									&& lastLatY != locrecord.getLatitude()) {
								try {
									com.sos.sosgps.api.DPoint[] dPoint = coordApizw
											.encryptConvert(xs, ys);
									String lngX = dPoint[0].getEncryptX();
									String latY = dPoint[0].getEncryptY();
									// ȡ��λ������
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}
							}
							lastlngX = locrecord.getLongitude();
							lastLatY = locrecord.getLatitude();
						}
						// ��γ������Ϊ0
						else {
							lastlngX = locrecord.getLongitude();
							lastLatY = locrecord.getLatitude();
							locDesc = "û���յ������ź�";// λ������
						}
					} else {
						locDesc = locrecord.getLocDesc();
					}
					lastPosDesc = locDesc;
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// λ������
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(
									locrecord.getPoiId(), "") + "',");// ��ע������id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(locrecord.getPoiName())
							+ "',");// ���ÿͻ�
					jsonSb.append("imsi:'"
							+ CharTools.javaScriptEscape(locrecord.getImsi())
							+ "'");// imsi
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			} else {
				StringBuffer sb = new StringBuffer();
				sb.append("{");
				sb.append("deviceId:'',");// deviceId
				sb.append("vehicleNumber:'',");// ����
				sb.append("simcard:'',");// �ֻ�����
				sb.append("gpsTime:'',");// ʱ��
				sb.append("inputTime:'',");// ʱ��
				sb.append("pd:'" + st + "~" + et + "����ʱ����û����Ч�Ķ�λ����',");// λ������
				sb.append("visitId:'',");// ��ע������id
				sb.append("visitName:'',");// ���ÿͻ�
				sb.append("imsi:''");// imsi
				sb.append("}");
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(
						"{total: 1,data:[" + sb.toString() + "]}");
				return mapping.findForward(null);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos �ݷô���ͳ�� --- ����
	@SuppressWarnings("static-access")
	public ActionForward listVisitCountTj(ActionMapping mapping,
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
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String deviceIdsStr = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
		int duration = CharTools.str2Integer(durationStr, 15);// Ĭ��15����

		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = visitStatService.listVisitCountTj(entCode,
					userId, 1, 65536, startDate, endDate, searchValue,
					deviceIds, duration);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�ݷô���", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				// String deviceId = (String) objects[0];
				String vehicleNumber = (String) objects[1];
				Long visitCount = (Long) objects[2];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullLong2String(visitCount, "0"));
			}
			String[] deviceIdss = CharTools.split(deviceIdsStr, ",");
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
			for (String deviceId : deviceIdss) {
				// һ��ҵ��Աһ��sheet
				Page<Object[]> list2 = visitStatService
						.listVisitCountTjByDeviceId(entCode, userId, 1, 65536,
								startDate, endDate, deviceId, duration);
				if (list2 != null && list2.getResult().size() > 0) {
					row = 0;
					// CoordCvtAPI coordCvtApi = new CoordCvtAPI();
					for (Object[] objects : list2.getResult()) {
						TVisitTj visitTj = (TVisitTj) objects[0];
						TTerminal terminal = (TTerminal) objects[1];
						String locDesc = visitTj.getLocDesc();
						if (locDesc == null) {
							// ����γ�����겻Ϊ0
							if (visitTj.getLatitude() > 0
									&& visitTj.getLongitude() > 0) {
								double[] xs = { visitTj.getLongitude() };
								double[] ys = { visitTj.getLatitude() };
								// DPoint[] dPoint =
								// coordCvtApi.encryptConvert(xs, ys);
								// String lngX = dPoint[0].getEncryptX();
								// String latY = dPoint[0].getEncryptY();
								// locDesc = coordCvtApi.getAddress(lngX, latY);
								try {
									com.sos.sosgps.api.DPoint[] dPoint = coordApizw
											.encryptConvert(xs, ys);
									String lngX = dPoint[0].getEncryptX();
									String latY = dPoint[0].getEncryptY();
									// ȡ��λ������
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}

							}// ��γ������Ϊ0
							else {
								locDesc = "û���յ������ź�";// λ������
							}
						}
						int col = 0;
						if (row == 0) {
							// ����sheet
							excelWorkBook.addWorkSheet(CharTools
									.javaScriptEscape(terminal
											.getVehicleNumber()));
							// ����header
							excelWorkBook.addHeader("���ÿͻ�", 15);
							// excelWorkBook.addHeader("�ݷ�ҵ��Ա", 15);
							// excelWorkBook.addHeader("�ֻ�����", 15);
							excelWorkBook.addHeader("����ʱ��", 20);
							excelWorkBook.addHeader("�뿪ʱ��", 20);
							excelWorkBook.addHeader("ͣ��ʱ��", 20);
							excelWorkBook.addHeader("λ������", 50);
							// excelWorkBook.addHeader("ͳ��ʱ��", 20);
						}
						row += 1;
						excelWorkBook.addCell(col++, row, CharTools
								.javaScriptEscape(visitTj.getPoiName()));
						// excelWorkBook.addCell(col++, row, CharTools
						// .javaScriptEscape(terminal.getVehicleNumber()));
						// excelWorkBook.addCell(col++, row, CharTools
						// .javaScriptEscape(terminal.getSimcard()));
						excelWorkBook
								.addCell(col++, row, CharTools
										.javaScriptEscape(DateUtility
												.dateTimeToStr(visitTj
														.getArriveTime())));
						excelWorkBook
								.addCell(col++, row, CharTools
										.javaScriptEscape(DateUtility
												.dateTimeToStr(visitTj
														.getLeaveTime())));
						int stayTime = DateUtility
								.betweenMinute(visitTj.getLeaveTime(),
										visitTj.getArriveTime());
						excelWorkBook.addCell(col++, row,
								String.valueOf(stayTime));
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(locDesc));
						// excelWorkBook.addCell(col++, row, CharTools
						// .killNullString(DateUtility.dateTimeToStr(visitTj
						// .getTjDate())));
					}
				}
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
			Page<Object[]> list = visitStatService.listVisitCountTj(entCode,
					userId, pageNo, pageSize, startDate, endDate, searchValue,
					deviceIds, duration);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					String vehicleNumber = (String) objects[1];
					Long visitCount = (Long) objects[2];
					jsonSb.append("{");
					jsonSb.append("id:'" + CharTools.javaScriptEscape(deviceId)
							+ "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ����
					jsonSb.append("visitCount:'"
							+ CharTools.killNullLong2String(visitCount, "0")
							+ "'");// �ݷô���
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

	// sos �鿴�ݷô�����ϸͳ�ƣ�ĳ��ҵ��Ա�ݷÿͻ���ϸ��
	@SuppressWarnings("static-access")
	public ActionForward listVisitCountTjByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-18 zss �鿴�ݷô�����ϸͳ�ƣ�ĳ��ҵ��Ա�ݷÿͻ���ϸ��
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "�鿴�ݷô�����ϸͳ�ƣ�ĳ��ҵ��Ա�ݷÿͻ���ϸ��");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitCustomerDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String deviceId = request.getParameter("deviceId");// �ն�deviceId
		String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
		int duration = CharTools.str2Integer(durationStr, 15);// Ĭ��15����

		deviceId = CharTools.javaScriptEscape(deviceId);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService.listVisitCountTjByDeviceId(
					entCode, userId, pageNo, pageSize, startDate, endDate,
					deviceId, duration);

			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TVisitTj visitTj = (TVisitTj) objects[0];
					// TTerminal terminal = (TTerminal) objects[1];
					String locDesc = visitTj.getLocDesc();
					if (locDesc == null) {
						// ����γ�����겻Ϊ0
						if (visitTj.getLatitude() > 0
								&& visitTj.getLongitude() > 0) {

							double[] xs = { visitTj.getLongitude() };
							double[] ys = { visitTj.getLatitude() };
							// DPoint[] dPoint = coordCvtApi
							// .encryptConvert(xs, ys);
							// String lngX = dPoint[0].getEncryptX();
							// String latY = dPoint[0].getEncryptY();
							// locDesc = coordCvtApi.getAddress(lngX, latY);
							try {
								com.sos.sosgps.api.DPoint[] dPoint = coordApizw
										.encryptConvert(xs, ys);
								String lngX = dPoint[0].getEncryptX();
								String latY = dPoint[0].getEncryptY();
								// ȡ��λ������
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listAttendanceReportDetail-encryptConvert error,"
												+ ex.getMessage());
							}

						}// ��γ������Ϊ0
						else {
							locDesc = "û���յ������ź�";// λ������
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + visitTj.getId() + "',");// id
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(visitTj.getPoiId(),
									"") + "',");// ���ÿͻ�����ע�㣩id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(visitTj.getPoiName())
							+ "',");// ���ÿͻ�����ע�㣩����
					// jsonSb.append("vehicleNumber:'"
					// + CharTools.javaScriptEscape(terminal
					// .getVehicleNumber()) + "',");// �ݷ�ҵ��Ա
					// jsonSb.append("simcard:'"
					// + CharTools.javaScriptEscape(terminal.getSimcard())
					// + "',");// �ֻ�����
					jsonSb.append("arriveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getArriveTime()))
							+ "',");// ����ʱ��
					jsonSb.append("leaveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getLeaveTime()))
							+ "',");// �뿪ʱ��
					int stayTime = DateUtility.betweenMinute(
							visitTj.getLeaveTime(), visitTj.getArriveTime());
					jsonSb.append("stayTime:'" + stayTime + "',");// ͣ��ʱ��,��λ����
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(locDesc)
							+ "',");// λ������
					jsonSb.append("tjDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateToStr(visitTj.getTjDate())) + "'");// ͳ��ʱ��
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

	// sos �ͻ��ݷô���ͳ��
	@SuppressWarnings("static-access")
	public ActionForward listCustomVisitCountTj(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserInfo userInfo = this.getCurrentUser(request);
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
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
		int duration = CharTools.str2Integer(durationStr, 15);// Ĭ��15����
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
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
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

			Page<Object[]> list = visitStatService.listCustomVisitCountTj(
					entCode, userId, 1, 65536, startDate, endDate, searchValue,
					deviceIds, duration);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("�ͻ�����", 15);
			excelWorkBook.addHeader("�ݷô���", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				// Long poiId = (Long) objects[0];
				String poiName = (String) objects[1];
				Long visitCount = (Long) objects[2];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poiName));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullLong2String(visitCount, "0"));
			}
			// TODO
			Page<Object[]> list2 = visitStatService.listCustomVisitTj(entCode,
					userId, 1, 65536, startDate, endDate, searchValue,
					deviceIds, duration);
			if (list2 != null && list2.getResult().size() > 0) {
				row = 0;
				// ����sheet
				excelWorkBook.addWorkSheet("��ϸ��Ϣ");
				// ����header
				excelWorkBook.addHeader("���ÿͻ�", 20);
				excelWorkBook.addHeader("�ݷ�ҵ��Ա", 15);
				excelWorkBook.addHeader("����ʱ��", 20);
				excelWorkBook.addHeader("�뿪ʱ��", 20);
				excelWorkBook.addHeader("ͣ��ʱ��", 20);
				excelWorkBook.addHeader("λ������", 50);
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list2.getResult()) {
					TVisitTj visitTj = (TVisitTj) objects[0];
					TTerminal terminal = (TTerminal) objects[1];
					String locDesc = visitTj.getLocDesc();
					if (locDesc == null) {
						// ����γ�����겻Ϊ0
						if (visitTj.getLatitude() > 0
								&& visitTj.getLongitude() > 0) {
							double[] xs = { visitTj.getLongitude() };
							double[] ys = { visitTj.getLatitude() };
							// DPoint[] dPoint = coordCvtApi
							// .encryptConvert(xs, ys);
							// String lngX = dPoint[0].getEncryptX();
							// String latY = dPoint[0].getEncryptY();
							// locDesc = coordCvtApi.getAddress(lngX, latY);
							try {
								com.sos.sosgps.api.DPoint[] dPoint = coordApizw
										.encryptConvert(xs, ys);
								String lngX = dPoint[0].getEncryptX();
								String latY = dPoint[0].getEncryptY();
								// ȡ��λ������
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listAttendanceReportDetail-encryptConvert error,"
												+ ex.getMessage());
							}
						}// ��γ������Ϊ0
						else {
							locDesc = "û���յ������ź�";// λ������
						}
					}
					int col = 0;
					row += 1;
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(visitTj.getPoiName()));
					excelWorkBook.addCell(col++, row, CharTools
							.javaScriptEscape(terminal.getVehicleNumber()));
					excelWorkBook.addCell(col++, row, CharTools
							.javaScriptEscape(DateUtility.dateTimeToStr(visitTj
									.getArriveTime())));
					excelWorkBook.addCell(col++, row, CharTools
							.javaScriptEscape(DateUtility.dateTimeToStr(visitTj
									.getLeaveTime())));
					int stayTime = DateUtility.betweenMinute(
							visitTj.getLeaveTime(), visitTj.getArriveTime());
					excelWorkBook.addCell(col++, row, String.valueOf(stayTime));
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(locDesc));
				}
			}
			excelWorkBook.write();
			return null;
		}

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// searchLog
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + " ��ѯ�ͻ����ݷ�ͳ��");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VISIT);
		LogFactory.newLogInstance("optLogger").info(optLog);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService.listCustomVisitCountLing(
					pageNo, pageSize, st, et, searchValue, deviceIds, duration);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					Long poiId = (Long.parseLong(objects[0].toString()));
					String poiName = (String) objects[1];
					Long visitCount;
					if (objects[2] == null) {
						visitCount = (long) 0;
					} else {
						visitCount = (Long.parseLong(objects[2].toString()));
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + poiId + "',");// id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(poiName) + "',");// �ͻ�����
					jsonSb.append("visitCount:'"
							+ CharTools.killNullLong2String(visitCount, "0")
							+ "'");// �ݷô���
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

	// sos �ͻ��ݷ�ͳ����ϸ��Ϣ��ĳ���ͻ����ݷ���ϸ��Ϣ��
	@SuppressWarnings("static-access")
	public ActionForward listCustomVisitCountTjByCustom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-18 zss �ͻ��ݷ�ͳ����ϸ��Ϣ��ĳ���ͻ����ݷ���ϸ��Ϣ��
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯĳ���ͻ����ݷ���ϸ��Ϣ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitedByDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String poiIdStr = request.getParameter("poiId");// �ͻ�id
		String deviceIds = request.getParameter("deviceIds");// ҵ��Աid
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);

		String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
		int duration = CharTools.str2Integer(durationStr, 15);// Ĭ��15����

		Long poiId = CharTools.str2Long(poiIdStr, -1L);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService
					.listCustomVisitCountTjByCustom(entCode, userId, pageNo,
							pageSize, startDate, endDate, poiId, deviceIds,
							duration);

			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TVisitTj visitTj = (TVisitTj) objects[0];
					TTerminal terminal = (TTerminal) objects[1];
					String locDesc = visitTj.getLocDesc();

					if (locDesc == null) {
						// ����γ�����겻Ϊ0
						if (visitTj.getLatitude() > 0
								&& visitTj.getLongitude() > 0) {

							double[] xs = { visitTj.getLongitude() };
							double[] ys = { visitTj.getLatitude() };
							// DPoint[] dPoint = coordCvtApi
							// .encryptConvert(xs, ys);
							// String lngX = dPoint[0].getEncryptX();
							// String latY = dPoint[0].getEncryptY();
							// locDesc = coordCvtApi.getAddress(lngX, latY);

							try {
								com.sos.sosgps.api.DPoint[] dPoint = coordApizw
										.encryptConvert(xs, ys);
								String lngX = dPoint[0].getEncryptX();
								String latY = dPoint[0].getEncryptY();
								// ȡ��λ������
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listCustomVisitCountTjByCustom-encryptConvert error,"
												+ ex.getMessage());
							}

						}// ��γ������Ϊ0
						else {
							locDesc = "û���յ������ź�";// λ������
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + visitTj.getId() + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// ����
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");// �ֻ�����
					jsonSb.append("arriveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getArriveTime()))
							+ "',");// ����ʱ��
					jsonSb.append("leaveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getLeaveTime()))
							+ "',");// �뿪ʱ��
					int stayTime = DateUtility.betweenMinute(
							visitTj.getLeaveTime(), visitTj.getArriveTime());
					jsonSb.append("stayTime:'" + stayTime + "',");// ͣ��ʱ��,��λ����
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(locDesc)
							+ "',");// λ������
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(visitTj.getPoiId(),
									"") + "',");// ���ÿͻ�id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(visitTj.getPoiName())
							+ "',");// ���ÿͻ�����
					jsonSb.append("tjDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getTjDate())) + "'");// ͳ��ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:" + jsonSb.toString() +
		// "}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	public void downloadLocal(HttpServletResponse response, String filePath,
			String fileName) throws FileNotFoundException {
		logger.info("download file:" + filePath);
		// ���ر����ļ�
		fileName = fileName.toString(); // �ļ���Ĭ�ϱ�����
		// ��������
		InputStream inStream = new FileInputStream(filePath);// �ļ��Ĵ��·��
		// ��������ĸ�ʽ
		response.reset();
		response.setContentType("bin");
		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		// ѭ��ȡ�����е�����
		byte[] b = new byte[100];
		int len;
		try {
			while ((len = inStream.read(b)) > 0)
				response.getOutputStream().write(b, 0, len);
			inStream.close();
		} catch (IOException e) {
			logger.error("downloadLocal-error," + e);
			// e.printStackTrace();
		}
	}

	// ҵ��Ա����ͳ��(sql)
	@SuppressWarnings("static-access")
	public ActionForward listVisitCountTjSql(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
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
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		String deviceIdsStr = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
		int duration = CharTools.str2Integer(durationStr, 15);// Ĭ��15����
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIdsStr.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
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
			Page<Object[]> list = visitStatService.listVisitCountTjSql(entCode,
					userId, 0, 65536, st, et, searchValue, deviceIds, duration);

			// add by 2012-12-18 zss ����ҵ��Ա����ͳ�Ƴɹ�
			TOptLog optLog = new TOptLog();
			optLog.setEmpCode(entCode);
			optLog.setUserName(userInfo.getUserAccount());
			optLog.setAccessIp(userInfo.getIp());
			optLog.setUserId(userId);
			optLog.setOptTime(new Date());
			optLog.setResult(1L);
			optLog.setOptDesc(userInfo.getUserAccount() + "����ҵ��Ա����ͳ��wzt�ɹ�");
			optLog.setFunFType(LogConstants.LOG_STAT);
			optLog.setFunCType(LogConstants.LOG_STAT_CUSTOM);
			LogFactory.newLogInstance("optLogger").info(optLog);

			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�ݷô���", 15);
			excelWorkBook.addHeader("�ݷÿͻ���", 15);
			// excelWorkBook.addHeader("�ݷõص���", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				// String deviceId = (String) objects[0];
				String vehicleNumber = (String) objects[1];
				Long visitCount = ((BigDecimal) objects[2]).longValue();
				Long visitCusCount = ((BigDecimal) objects[3]).longValue();
				// Long visitPlaceCount = ((BigDecimal) objects[4]).longValue();
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullLong2String(visitCount, "0"));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullLong2String(visitCusCount, "0"));
				// excelWorkBook.addCell(col++, row,
				// CharTools.killNullLong2String(visitPlaceCount, "0"));
			}
			String[] deviceIdss = CharTools.split(deviceIdsStr, ",");
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
			for (String deviceId : deviceIdss) {
				// һ��ҵ��Աһ��sheet
				Page<Object[]> list2 = visitStatService
						.listVisitCountTjByDeviceId(entCode, userId, 1, 65536,
								startDate, endDate, deviceId, duration);
				if (list2 != null && list2.getResult().size() > 0) {
					row = 0;
					for (Object[] objects : list2.getResult()) {
						TVisitTj visitTj = (TVisitTj) objects[0];
						TTerminal terminal = (TTerminal) objects[1];
						String locDesc = visitTj.getLocDesc();
						if (locDesc == null) {
							// ����γ�����겻Ϊ0
							if (visitTj.getLatitude() > 0
									&& visitTj.getLongitude() > 0) {
								double[] xs = { visitTj.getLongitude() };
								double[] ys = { visitTj.getLatitude() };
								try {
									com.sos.sosgps.api.DPoint[] dPoint = coordApizw
											.encryptConvert(xs, ys);
									String lngX = dPoint[0].getEncryptX();
									String latY = dPoint[0].getEncryptY();
									// ȡ��λ������
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}
							}// ��γ������Ϊ0
							else {
								locDesc = "û���յ������ź�";// λ������
							}
						}
						int col = 0;
						if (row == 0) {
							// ����sheet
							excelWorkBook.addWorkSheet(CharTools
									.javaScriptEscape(terminal
											.getVehicleNumber()));
							// ����header
							excelWorkBook.addHeader("���ÿͻ�", 15);
							excelWorkBook.addHeader("����ʱ��", 20);
							excelWorkBook.addHeader("�뿪ʱ��", 20);
							excelWorkBook.addHeader("ͣ��ʱ��", 20);
							excelWorkBook.addHeader("λ������", 50);
						}
						row += 1;
						excelWorkBook.addCell(col++, row, CharTools
								.javaScriptEscape(visitTj.getPoiName()));
						excelWorkBook
								.addCell(col++, row, CharTools
										.javaScriptEscape(DateUtility
												.dateTimeToStr(visitTj
														.getArriveTime())));
						excelWorkBook
								.addCell(col++, row, CharTools
										.javaScriptEscape(DateUtility
												.dateTimeToStr(visitTj
														.getLeaveTime())));
						int stayTime = DateUtility
								.betweenMinute(visitTj.getLeaveTime(),
										visitTj.getArriveTime());
						excelWorkBook.addCell(col++, row,
								String.valueOf(stayTime));
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(locDesc));
					}
				}
			}
			excelWorkBook.write();
			return null;
		}
		// UserInfo userInfo = this.getCurrentUser(request);
		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// searchLog
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯҵ��Ա����ͳ�Ƴɹ�");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_CUSTOM);
		LogFactory.newLogInstance("optLogger").info(optLog);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		// int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService.listVisitCountTjSql(entCode,
					userId, startint, pageSize, st, et, searchValue, deviceIds,
					duration);
			if (list != null && list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String deviceId = (String) objects[0];
					String vehicleNumber = (String) objects[1];
					Long visitCount = ((BigDecimal) objects[2]).longValue();
					Long visitCusCount = ((BigDecimal) objects[3]).longValue();
					// Long visitPlaceCount = ((BigDecimal)
					// objects[4]).longValue();
					jsonSb.append("{");
					jsonSb.append("deviceId:'"
							+ CharTools.javaScriptEscape(deviceId) + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ����
					jsonSb.append("visitCount:'"
							+ CharTools.killNullLong2String(visitCount, "0")
							+ "',");// �ݷô���
					jsonSb.append("visitCusCount:'"
							+ CharTools.killNullLong2String(visitCusCount, "0")
							+ "',");// �ݷÿͻ���
					jsonSb.append("visitPlaceCount:'"
							+ CharTools.killNullLong2String(visitCusCount, "0")
							+ "'");// �ݷõص���
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

	/**
	 * ������λ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public ActionForward listVehicleGPS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.getWriter().write("{result:\"9\"}");// δ��¼ return
		 * mapping.findForward(null); }
		 */

		// ��request�л�ȡ����
		// String deviceIds = request.getParameter("deviceIds");// �ն�id
		// deviceIds =
		// CharTools.splitAndAdd(CharTools.javaScriptEscape(deviceIds));
		// �ؼ��ֲ�ѯ add by zhaofeng
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		String deviceIdsStr = request.getParameter("deviceIds");// �ն�id�����","����
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "1" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss

		/*
		 * String week = request.getParameter("week");
		 * 
		 * week = CharTools.javaScriptEscape(week, "127"); long deviceWeek =
		 * Long.parseLong(week); int todayWeekNum =
		 * DateUtility.getDateWeekNum(DateUtility.strToDate(st)); Double
		 * todayWeekBinary = Math.pow(2,todayWeekNum-1); if((deviceWeek &
		 * todayWeekBinary.intValue()) != todayWeekBinary){ StringBuffer sb =
		 * new StringBuffer(); sb.append("{"); sb.append("deviceId:'',");//
		 * deviceId sb.append("vehicleNumber:'',");// ����
		 * sb.append("simcard:'',");// �ֻ����� sb.append("gpsTime:'',");// ʱ��
		 * sb.append
		 * ("pd:'"+DateUtility.getDateWeekCnName(todayWeekNum-1)+"��Ϣ��',");//
		 * λ������ sb.append("visitId:'',");// ��ע������id
		 * sb.append("visitName:'',");// ���ÿͻ� sb.append("longitude:'',");// ����
		 * sb.append("latitude:'',");// γ�� sb.append("speed:'',");// �ٶ�
		 * sb.append("direction:'',");// ���� sb.append("distance:'',");// ���
		 * sb.append("accStatus:'',");// ACC ״̬ sb.append("temperature:''");//�¶�
		 * sb.append("}"); response.setContentType("text/json; charset=utf-8");
		 * response.getWriter().write("{total: 1,data:["+sb.toString()+"]}");
		 * return mapping.findForward(null); }
		 */

		// Date startDate = DateUtility.strToDateTime(st);
		// Date endDate = DateUtility.strToDateTime(et);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		// expExcel = "true";
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
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

			Page<Object[]> list = visitStatService.listVehicleGPS(searchValue,
					deviceIds, 1, 65536, st, et);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			// excelWorkBook.addHeader("ID", 10);
			excelWorkBook.addHeader("���ƺ�", 15);// �ն�����
			excelWorkBook.addHeader("�ֻ�����", 15);// �ֻ�sim����
			excelWorkBook.addHeader("γ��", 15);// ά��
			excelWorkBook.addHeader("����", 15);// ����
			excelWorkBook.addHeader("�ٶ�", 10);// ��ʻ�ٶ�
			excelWorkBook.addHeader("����", 10);// ��ʻ����
			excelWorkBook.addHeader("���", 10);// ��ʻ���
			excelWorkBook.addHeader("��ʻ״̬", 10);// acc״̬
			excelWorkBook.addHeader("λ������", 30);// λ������
			excelWorkBook.addHeader("���ÿͻ�", 30);// �ݷõ�����
			excelWorkBook.addHeader("ʱ��", 10);// �����ϱ�ʱ��
			excelWorkBook.addHeader("�¶�", 10);// �¶�
			int row = 0;
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
			float longitudeLast = -1;
			float latitudeLast = -1;
			String locDescLast = "";
			for (Object[] objects : list.getResult()) {
				TLocrecord locrecord = (TLocrecord) objects[0];
				String vehicleNumber = (String) objects[1];
				String simcard = (String) objects[2];
				int col = 0;
				row += 1;
				// excelWorkBook.addCell(col++, row, String.valueOf(row));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getLatitude(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getLongitude(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getSpeed(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getDirection(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getDistance(), "0"));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(locrecord.getAccStatus()));
				String locDesc = locrecord.getLocDesc();
				if (locDesc == null || locDesc.equals("")) {
					if (locrecord.getLongitude() == longitudeLast
							&& locrecord.getLatitude() == latitudeLast) {
						locDesc = locDescLast;
					} else {
						double[] xs = { locrecord.getLongitude() };
						double[] ys = { locrecord.getLatitude() };
						try {
							com.sos.sosgps.api.DPoint[] dPoint = coordApizw
									.encryptConvert(xs, ys);
							String lngX = dPoint[0].getEncryptX();
							String latY = dPoint[0].getEncryptY();
							// ȡ��λ������
							locDesc = coordCvtApi.getAddress(lngX, latY);
						} catch (Exception ex) {
							this.logger
									.error("listAttendanceReportDetail-encryptConvert error,"
											+ ex.getMessage());
						}
					}
					locDescLast = locDesc;
					longitudeLast = locrecord.getLongitude();
					latitudeLast = locrecord.getLatitude();
				}
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(locDesc));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(locrecord.getPoiName()));
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateTimeToStr(locrecord
								.getGpstime())));
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullFloat2String(
								locrecord.getTemperature(), "0"));
			}
			// add by 2012-12-18 zss ������λ��Ϣ
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_CAR);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "������λ��Ϣ�ɹ�");
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
		tOptLog.setFunCType(LogConstants.LOG_STAT_CAR);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ��λ��Ϣ�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService.listVehicleGPS(searchValue,
					deviceIds, pageNo, pageSize, st, et);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				double lastlngX = -1;
				double lastLatY = -1;
				String lastPosDesc = "";
				String locDesc = "";// λ������
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TLocrecord locrecord = (TLocrecord) objects[0];
					String vehicleNumber = (String) objects[1];
					String simcard = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + locrecord.getDeviceId() + "',");// deviceId
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ����
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// �ֻ�����
					jsonSb.append("gpsTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(locrecord.getGpstime()))
							+ "',");// ʱ��
					// �����ȡλ��Ϊ��
					if (locrecord.getLocDesc() == null
							|| locrecord.getLocDesc().equals("")) {
						// ����γ�����겻Ϊ0
						if (locrecord.getLatitude() > 0
								&& locrecord.getLongitude() > 0) {
							double[] xs = { locrecord.getLongitude() };
							double[] ys = { locrecord.getLatitude() };
							if (lastlngX != locrecord.getLongitude()
									&& lastLatY != locrecord.getLatitude()) {
								try {
									com.sos.sosgps.api.DPoint[] dPoint = coordApizw
											.encryptConvert(xs, ys);
									String lngX = dPoint[0].getEncryptX();
									String latY = dPoint[0].getEncryptY();
									// ȡ��λ������
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}
							}
							lastlngX = locrecord.getLongitude();
							lastLatY = locrecord.getLatitude();
						}
						// ��γ������Ϊ0
						else {
							locDesc = "û���յ������ź�";// λ������
						}
					} else {
						locDesc = locrecord.getLocDesc();
					}
					lastPosDesc = locDesc;
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// λ������
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(
									locrecord.getPoiId(), "") + "',");// ��ע������id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(locrecord.getPoiName())
							+ "',");// ���ÿͻ�
					jsonSb.append("longitude:'" + locrecord.getLongitude()
							+ "',");// ����
					jsonSb.append("latitude:'" + locrecord.getLatitude() + "',");// γ��
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(
									locrecord.getSpeed(), "0") + "',");// �ٶ�
					jsonSb.append("direction:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDirection(), "0") + "',");// ����
					jsonSb.append("distance:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDistance(), "0") + "',");// ���
					jsonSb.append("accStatus:'"
							+ CharTools.javaScriptEscape(locrecord
									.getAccStatus()) + "',");// ACC ״̬
					// jsonSb.append("varExt1:'" +
					// CharTools.javaScriptEscape(locrecord.getVarExt1()) +
					// "',");// ��״̬
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(
									locrecord.getTemperature(), "0") + "'");// �¶�
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

	public ActionForward listVisitCountTjByDeviceId2(ActionMapping mapping,
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
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "15" : request
				.getParameter("limit");

		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String deviceId = request.getParameter("deviceId");// �ն�deviceId
		String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
		int duration = CharTools.str2Integer(durationStr, 15);// Ĭ��15����
		if (st == null || et == null || deviceId.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		deviceId = CharTools.killNullString(deviceId);
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;

		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService.listVisitCountTjByDeviceId2(
					entCode, userId, startint, pageSize, st, et, deviceId,
					duration);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					String poiName = (String) objects[0];
					String locDesc = (String) objects[1];
					String poiDatas = (String) objects[2];
					Long poicount = ((BigDecimal) objects[3]).longValue();
					if (locDesc == null) {
						String[] poiDatasArr = poiDatas.split(",");
						double longitude = 0;
						double latitude = 0;
						if (poiDatasArr.length == 2) {
							String longitudeStr = poiDatasArr[0];
							String latitudeStr = poiDatasArr[1];
							longitude = Double.parseDouble(longitudeStr);
							latitude = Double.parseDouble(latitudeStr);
						} else {
							continue;
						}
						// ����γ�����겻Ϊ0
						if (latitude > 0 && longitude > 0) {
							double[] xs = { longitude };
							double[] ys = { latitude };
							try {
								com.sos.sosgps.api.DPoint[] dPoint = coordApizw
										.encryptConvert(xs, ys);
								String lngX = dPoint[0].getEncryptX();
								String latY = dPoint[0].getEncryptY();
								// ȡ��λ������
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listAttendanceReportDetail-encryptConvert error,"
												+ ex.getMessage());
							}
						}// ��γ������Ϊ0
						else {
							locDesc = "û���յ������ź�";// λ������
						}
					}
					jsonSb.append("{");
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poiName) + "',");// ���ÿͻ�����ע�㣩����
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(locDesc)
							+ "',");// ����ʱ��
					jsonSb.append("poi_count:'"
							+ CharTools.killNullLong2String(poicount, "0")
							+ "'");// ����ʱ��
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

	public ActionForward listVehicleGPS1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceIdsStr = request.getParameter("deviceIds");// �ն�id�����","����
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "1" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		// expExcel = "true";
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
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

			Page<Object[]> list = visitStatService.listVehicleGPS(deviceIds, 1,
					65536, st, et);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			// excelWorkBook.addHeader("ID", 10);
			excelWorkBook.addHeader("���ƺ�", 15);// �ն�����
			excelWorkBook.addHeader("�ֻ�����", 15);// �ֻ�sim����
			excelWorkBook.addHeader("γ��", 15);// ά��
			excelWorkBook.addHeader("����", 15);// ����
			excelWorkBook.addHeader("��״̬", 10);// ��״̬
			excelWorkBook.addHeader("�ٶ�", 10);// ��ʻ�ٶ�
			excelWorkBook.addHeader("����", 10);// ��ʻ����
			excelWorkBook.addHeader("���", 10);// ��ʻ���
			excelWorkBook.addHeader("��ʻ״̬", 10);// acc״̬
			excelWorkBook.addHeader("λ������", 30);// λ������
			excelWorkBook.addHeader("���ÿͻ�", 30);// �ݷõ�����
			excelWorkBook.addHeader("ʱ��", 10);// �����ϱ�ʱ��
			excelWorkBook.addHeader("�¶�", 10);// �¶�
			int row = 0;
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
			float longitudeLast = -1;
			float latitudeLast = -1;
			String locDescLast = "";
			for (Object[] objects : list.getResult()) {
				TLocrecord locrecord = (TLocrecord) objects[0];
				String vehicleNumber = (String) objects[1];
				String simcard = (String) objects[2];
				String varExt1 = CharTools.javaScriptEscape(locrecord
						.getVarExt1());
				if (varExt1.equals("1")) {
					varExt1 = "����";
				} else if (varExt1.equals("0")) {
					varExt1 = "����";
				} else {
					varExt1 = "";
				}
				int col = 0;
				row += 1;
				// excelWorkBook.addCell(col++, row, String.valueOf(row));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getLatitude(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getLongitude(), "0"));
				excelWorkBook.addCell(col++, row, varExt1);
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getSpeed(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getDirection(), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullFloat2String(locrecord.getDistance(), "0"));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(locrecord.getAccStatus()));
				String locDesc = locrecord.getLocDesc();
				if (locDesc == null || locDesc.equals("")) {
					if (locrecord.getLongitude() == longitudeLast
							&& locrecord.getLatitude() == latitudeLast) {
						locDesc = locDescLast;
					} else {
						double[] xs = { locrecord.getLongitude() };
						double[] ys = { locrecord.getLatitude() };
						try {
							com.sos.sosgps.api.DPoint[] dPoint = coordApizw
									.encryptConvert(xs, ys);
							String lngX = dPoint[0].getEncryptX();
							String latY = dPoint[0].getEncryptY();
							// ȡ��λ������
							locDesc = coordCvtApi.getAddress(lngX, latY);
						} catch (Exception ex) {
							this.logger
									.error("listAttendanceReportDetail-encryptConvert error,"
											+ ex.getMessage());
						}
					}
					locDescLast = locDesc;
					longitudeLast = locrecord.getLongitude();
					latitudeLast = locrecord.getLatitude();
				}
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(locDesc));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(locrecord.getPoiName()));
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateTimeToStr(locrecord
								.getGpstime())));
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullFloat2String(
								locrecord.getTemperature(), "0"));
			}
			// add by 2012-12-18 zss �����Ŵ���ϢExcel
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_SENSOR);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "�����Ŵ���ϢExcel�ɹ�");
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
		tOptLog.setFunCType(LogConstants.LOG_STAT_SENSOR);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ�Ŵ���Ϣ�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = visitStatService.listVehicleGPS(deviceIds,
					pageNo, pageSize, st, et);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				double lastlngX = -1;
				double lastLatY = -1;
				String lastPosDesc = "";
				String locDesc = "";// λ������
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TLocrecord locrecord = (TLocrecord) objects[0];
					String vehicleNumber = (String) objects[1];
					String simcard = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + locrecord.getDeviceId() + "',");// deviceId
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// ����
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// �ֻ�����
					jsonSb.append("gpsTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(locrecord.getGpstime()))
							+ "',");// ʱ��
					// �����ȡλ��Ϊ��
					if (locrecord.getLocDesc() == null
							|| locrecord.getLocDesc().equals("")) {
						// ����γ�����겻Ϊ0
						if (locrecord.getLatitude() > 0
								&& locrecord.getLongitude() > 0) {
							double[] xs = { locrecord.getLongitude() };
							double[] ys = { locrecord.getLatitude() };
							if (lastlngX != locrecord.getLongitude()
									&& lastLatY != locrecord.getLatitude()) {
								try {
									com.sos.sosgps.api.DPoint[] dPoint = coordApizw
											.encryptConvert(xs, ys);
									String lngX = dPoint[0].getEncryptX();
									String latY = dPoint[0].getEncryptY();
									// ȡ��λ������
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}
							}
							lastlngX = locrecord.getLongitude();
							lastLatY = locrecord.getLatitude();
						}
						// ��γ������Ϊ0
						else {
							locDesc = "û���յ������ź�";// λ������
						}
					} else {
						locDesc = locrecord.getLocDesc();
					}
					lastPosDesc = locDesc;
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// λ������
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(
									locrecord.getPoiId(), "") + "',");// ��ע������id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(locrecord.getPoiName())
							+ "',");// ���ÿͻ�
					jsonSb.append("longitude:'" + locrecord.getLongitude()
							+ "',");// ����
					jsonSb.append("latitude:'" + locrecord.getLatitude() + "',");// γ��
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(
									locrecord.getSpeed(), "0") + "',");// �ٶ�
					jsonSb.append("direction:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDirection(), "0") + "',");// ����
					jsonSb.append("distance:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDistance(), "0") + "',");// ���
					jsonSb.append("accStatus:'"
							+ CharTools.javaScriptEscape(locrecord
									.getAccStatus()) + "',");// ACC ״̬
					jsonSb.append("varExt1:'"
							+ CharTools.javaScriptEscape(locrecord.getVarExt1())
							+ "',");// ��״̬
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(
									locrecord.getTemperature(), "0") + "'");// �¶�
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
