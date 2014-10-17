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
 * @Title:拜访统计action
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午09:10:37
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

	// sos 查看业务员考勤报表统计
	@SuppressWarnings("unchecked")
	public ActionForward listAttendanceReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从request中获取参数
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "10" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss
		String deviceIdsStr = request.getParameter("deviceIds");// 终端id，多个","隔开
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		response.setContentType("text/json; charset=utf-8");

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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

				response.getWriter().write("无权访问");// 未登录
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
				// 获取指定时间范围内报表路径集合 (deviceIDs 开始时间、 结束时间 企业代码)
				List<String> pathes = tReportFilepathService.getFilepathes(
						deviceIds, entCode, startDate, endDate);
				if (pathes.size() == 0) {
					logger.info("listAttendanceReport query result is zero");
					response.getWriter().write("报表未生成");// 未生成报表
					return mapping.findForward(null);
				}
				String filepath = downLoadReportService.generateDownloadReport(
						entCode, pathes, true);
				if (filepath == null) {

					// tOptLog.setResult(new Long(0));
					// tOptLog.setOptDesc(userAccount+" 导出业务员考勤报表失败");
					// LogFactory.newLogInstance("optLogger").info(tOptLog);

					response.getWriter().write("导出业务员考勤报表失败,报表文件为null");// 导出失败
					return mapping.findForward(null);
				}
				try {
					this.downloadLocal(response, filepath, entCode + ".zip");
				} catch (IOException ex) {
					response.getWriter().write("导出业务员考勤报表失败,生成报表文件失败");// 导出失败
					return mapping.findForward(null);
				}
				
				// add by 2012-12-18 zss 导出详细日志
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
				tOptLog.setOptDesc(userInfo.getUserAccount() + "导出指定时间范围内详细日志报表成功");
				LogFactory.newLogInstance("optLogger").info(tOptLog);

				// tOptLog.setResult(new Long(1));
				// tOptLog.setOptDesc(userAccount+"导出业务员考勤报表成功，文件名称："+entCode+".zip");
				// LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return mapping.findForward(null);
		}
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null || st == null || et == null
				|| deviceIdsStr == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询详细日志成功");
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
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// 名称
					jsonSb.append("groupName:'"
							+ CharTools.javaScriptEscape(groupName) + "',");// 部门
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// 手机号码
					jsonSb.append("openTime:'"
							+ CharTools.javaScriptEscape(openTime) + "',");// 开机时间
					jsonSb.append("closeTime:'"
							+ CharTools.javaScriptEscape(closeTime) + "'");// 关机时间
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

	// sos 查看业务员考勤报表明细统计
	@SuppressWarnings("static-access")
	public ActionForward listAttendanceReportDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}

		// add by 2012-12-18 查看业务员考勤报表明细统计
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCode());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查看业务员考勤报表明细统计");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_AttendanceDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端id
		deviceId = CharTools.splitAndAdd(CharTools.javaScriptEscape(deviceId));
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "1" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
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
			sb.append("vehicleNumber:'',");// 名称
			sb.append("simcard:'',");// 手机号码
			sb.append("gpsTime:'',");// 时间
			sb.append("pd:'" + DateUtility.getDateWeekCnName(todayWeekNum - 1)
					+ "休息日',");// 位置描述
			sb.append("visitId:'',");// 标注点名称id
			sb.append("visitName:'',");// 到访客户
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
				String locDesc = "";// 位置描述
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TLocrecord locrecord = (TLocrecord) objects[0];
					String vehicleNumber = (String) objects[1];
					String simcard = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + locrecord.getDeviceId() + "',");// deviceId
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// 名称
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// 手机号码
                    jsonSb.append("gpsTime:'"
                            + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(locrecord
                                    .getGpstime())) + "',");// 时间
                    jsonSb.append("inputTime:'"
                            + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(locrecord
                                    .getInputdate())) + "',");// 入库时间
                    // 如果获取位置为空
					if (locrecord.getLocDesc() == null
							|| locrecord.getLocDesc().equals("")) {
						// 当经纬度坐标不为0
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
									// 取得位置描述
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
						// 经纬度坐标为0
						else {
							lastlngX = locrecord.getLongitude();
							lastLatY = locrecord.getLatitude();
							locDesc = "没有收到卫星信号";// 位置描述
						}
					} else {
						locDesc = locrecord.getLocDesc();
					}
					lastPosDesc = locDesc;
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// 位置描述
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(
									locrecord.getPoiId(), "") + "',");// 标注点名称id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(locrecord.getPoiName())
							+ "',");// 到访客户
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
				sb.append("vehicleNumber:'',");// 名称
				sb.append("simcard:'',");// 手机号码
				sb.append("gpsTime:'',");// 时间
				sb.append("inputTime:'',");// 时间
				sb.append("pd:'" + st + "~" + et + "工作时间内没有有效的定位数据',");// 位置描述
				sb.append("visitId:'',");// 标注点名称id
				sb.append("visitName:'',");// 到访客户
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

	// sos 拜访次数统计 --- 弃用
	@SuppressWarnings("static-access")
	public ActionForward listVisitCountTj(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss
		String deviceIdsStr = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟

		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = visitStatService.listVisitCountTj(entCode,
					userId, 1, 65536, startDate, endDate, searchValue,
					deviceIds, duration);
			// 查询结果为空
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// 查询结果为空
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("拜访次数", 15);
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
				// 一个业务员一个sheet
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
							// 当经纬度坐标不为0
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
									// 取得位置描述
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}

							}// 经纬度坐标为0
							else {
								locDesc = "没有收到卫星信号";// 位置描述
							}
						}
						int col = 0;
						if (row == 0) {
							// 增加sheet
							excelWorkBook.addWorkSheet(CharTools
									.javaScriptEscape(terminal
											.getVehicleNumber()));
							// 增加header
							excelWorkBook.addHeader("到访客户", 15);
							// excelWorkBook.addHeader("拜访业务员", 15);
							// excelWorkBook.addHeader("手机号码", 15);
							excelWorkBook.addHeader("到达时间", 20);
							excelWorkBook.addHeader("离开时间", 20);
							excelWorkBook.addHeader("停留时间", 20);
							excelWorkBook.addHeader("位置描述", 50);
							// excelWorkBook.addHeader("统计时间", 20);
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
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// 名称
					jsonSb.append("visitCount:'"
							+ CharTools.killNullLong2String(visitCount, "0")
							+ "'");// 拜访次数
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

	// sos 查看拜访次数详细统计（某个业务员拜访客户详细）
	@SuppressWarnings("static-access")
	public ActionForward listVisitCountTjByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-18 zss 查看拜访次数详细统计（某个业务员拜访客户详细）
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查看拜访次数详细统计（某个业务员拜访客户详细）");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitCustomerDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss
		String deviceId = request.getParameter("deviceId");// 终端deviceId
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟

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
						// 当经纬度坐标不为0
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
								// 取得位置描述
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listAttendanceReportDetail-encryptConvert error,"
												+ ex.getMessage());
							}

						}// 经纬度坐标为0
						else {
							locDesc = "没有收到卫星信号";// 位置描述
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + visitTj.getId() + "',");// id
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(visitTj.getPoiId(),
									"") + "',");// 到访客户（标注点）id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(visitTj.getPoiName())
							+ "',");// 到访客户（标注点）名称
					// jsonSb.append("vehicleNumber:'"
					// + CharTools.javaScriptEscape(terminal
					// .getVehicleNumber()) + "',");// 拜访业务员
					// jsonSb.append("simcard:'"
					// + CharTools.javaScriptEscape(terminal.getSimcard())
					// + "',");// 手机号码
					jsonSb.append("arriveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getArriveTime()))
							+ "',");// 到达时间
					jsonSb.append("leaveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getLeaveTime()))
							+ "',");// 离开时间
					int stayTime = DateUtility.betweenMinute(
							visitTj.getLeaveTime(), visitTj.getArriveTime());
					jsonSb.append("stayTime:'" + stayTime + "',");// 停留时间,单位分钟
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(locDesc)
							+ "',");// 位置描述
					jsonSb.append("tjDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateToStr(visitTj.getTjDate())) + "'");// 统计时间
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

	// sos 客户拜访次数统计
	@SuppressWarnings("static-access")
	public ActionForward listCustomVisitCountTj(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserInfo userInfo = this.getCurrentUser(request);
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.getWriter().write("{result:\"9\"}");// 未登录 return
		 * mapping.findForward(null); } String entCode = userInfo.getEmpCode();
		 * Long userId = userInfo.getUserId();
		 */
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("参数不全");// 未登录
			return mapping.findForward(null);
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();

			Page<Object[]> list = visitStatService.listCustomVisitCountTj(
					entCode, userId, 1, 65536, startDate, endDate, searchValue,
					deviceIds, duration);
			// 查询结果为空
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// 查询结果为空
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("客户名称", 15);
			excelWorkBook.addHeader("拜访次数", 15);
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
				// 增加sheet
				excelWorkBook.addWorkSheet("详细信息");
				// 增加header
				excelWorkBook.addHeader("到访客户", 20);
				excelWorkBook.addHeader("拜访业务员", 15);
				excelWorkBook.addHeader("到达时间", 20);
				excelWorkBook.addHeader("离开时间", 20);
				excelWorkBook.addHeader("停留时间", 20);
				excelWorkBook.addHeader("位置描述", 50);
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list2.getResult()) {
					TVisitTj visitTj = (TVisitTj) objects[0];
					TTerminal terminal = (TTerminal) objects[1];
					String locDesc = visitTj.getLocDesc();
					if (locDesc == null) {
						// 当经纬度坐标不为0
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
								// 取得位置描述
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listAttendanceReportDetail-encryptConvert error,"
												+ ex.getMessage());
							}
						}// 经纬度坐标为0
						else {
							locDesc = "没有收到卫星信号";// 位置描述
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
			response.getWriter().write("{result:\"9\"}");// 未登录
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
		optLog.setOptDesc(userInfo.getUserAccount() + " 查询客户被拜访统计");
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
							+ CharTools.javaScriptEscape(poiName) + "',");// 客户名称
					jsonSb.append("visitCount:'"
							+ CharTools.killNullLong2String(visitCount, "0")
							+ "'");// 拜访次数
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

	// sos 客户拜访统计详细信息（某个客户被拜访详细信息）
	@SuppressWarnings("static-access")
	public ActionForward listCustomVisitCountTjByCustom(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-18 zss 客户拜访统计详细信息（某个客户被拜访详细信息）
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查询某个客户被拜访详细信息");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitedByDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss
		String poiIdStr = request.getParameter("poiId");// 客户id
		String deviceIds = request.getParameter("deviceIds");// 业务员id
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);

		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟

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
						// 当经纬度坐标不为0
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
								// 取得位置描述
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listCustomVisitCountTjByCustom-encryptConvert error,"
												+ ex.getMessage());
							}

						}// 经纬度坐标为0
						else {
							locDesc = "没有收到卫星信号";// 位置描述
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + visitTj.getId() + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// 名称
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");// 手机号码
					jsonSb.append("arriveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getArriveTime()))
							+ "',");// 到达时间
					jsonSb.append("leaveTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getLeaveTime()))
							+ "',");// 离开时间
					int stayTime = DateUtility.betweenMinute(
							visitTj.getLeaveTime(), visitTj.getArriveTime());
					jsonSb.append("stayTime:'" + stayTime + "',");// 停留时间,单位分钟
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(locDesc)
							+ "',");// 位置描述
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(visitTj.getPoiId(),
									"") + "',");// 到访客户id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(visitTj.getPoiName())
							+ "',");// 到访客户名称
					jsonSb.append("tjDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(visitTj.getTjDate())) + "'");// 统计时间
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
		// 下载本地文件
		fileName = fileName.toString(); // 文件的默认保存名
		// 读到流中
		InputStream inStream = new FileInputStream(filePath);// 文件的存放路径
		// 设置输出的格式
		response.reset();
		response.setContentType("bin");
		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		// 循环取出流中的数据
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

	// 业务员出访统计(sql)
	@SuppressWarnings("static-access")
	public ActionForward listVisitCountTjSql(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.getWriter().write("{result:\"9\"}");// 未登录 return
		 * mapping.findForward(null); } String entCode = userInfo.getEmpCode();
		 * Long userId = userInfo.getUserId();
		 */
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String deviceIdsStr = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIdsStr.equals("")) {
			response.getWriter().write("参数不全");// 未登录
			return mapping.findForward(null);
		}
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = visitStatService.listVisitCountTjSql(entCode,
					userId, 0, 65536, st, et, searchValue, deviceIds, duration);

			// add by 2012-12-18 zss 导出业务员出访统计成功
			TOptLog optLog = new TOptLog();
			optLog.setEmpCode(entCode);
			optLog.setUserName(userInfo.getUserAccount());
			optLog.setAccessIp(userInfo.getIp());
			optLog.setUserId(userId);
			optLog.setOptTime(new Date());
			optLog.setResult(1L);
			optLog.setOptDesc(userInfo.getUserAccount() + "导出业务员出访统计wzt成功");
			optLog.setFunFType(LogConstants.LOG_STAT);
			optLog.setFunCType(LogConstants.LOG_STAT_CUSTOM);
			LogFactory.newLogInstance("optLogger").info(optLog);

			// 查询结果为空
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// 查询结果为空
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("拜访次数", 15);
			excelWorkBook.addHeader("拜访客户数", 15);
			// excelWorkBook.addHeader("拜访地点数", 15);
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
				// 一个业务员一个sheet
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
							// 当经纬度坐标不为0
							if (visitTj.getLatitude() > 0
									&& visitTj.getLongitude() > 0) {
								double[] xs = { visitTj.getLongitude() };
								double[] ys = { visitTj.getLatitude() };
								try {
									com.sos.sosgps.api.DPoint[] dPoint = coordApizw
											.encryptConvert(xs, ys);
									String lngX = dPoint[0].getEncryptX();
									String latY = dPoint[0].getEncryptY();
									// 取得位置描述
									locDesc = coordCvtApi
											.getAddress(lngX, latY);
								} catch (Exception ex) {
									this.logger
											.error("listAttendanceReportDetail-encryptConvert error,"
													+ ex.getMessage());
								}
							}// 经纬度坐标为0
							else {
								locDesc = "没有收到卫星信号";// 位置描述
							}
						}
						int col = 0;
						if (row == 0) {
							// 增加sheet
							excelWorkBook.addWorkSheet(CharTools
									.javaScriptEscape(terminal
											.getVehicleNumber()));
							// 增加header
							excelWorkBook.addHeader("到访客户", 15);
							excelWorkBook.addHeader("到达时间", 20);
							excelWorkBook.addHeader("离开时间", 20);
							excelWorkBook.addHeader("停留时间", 20);
							excelWorkBook.addHeader("位置描述", 50);
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
			response.getWriter().write("{result:\"9\"}");// 未登录
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
		optLog.setOptDesc(userInfo.getUserAccount() + "查询业务员出访统计成功");
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
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// 名称
					jsonSb.append("visitCount:'"
							+ CharTools.killNullLong2String(visitCount, "0")
							+ "',");// 拜访次数
					jsonSb.append("visitCusCount:'"
							+ CharTools.killNullLong2String(visitCusCount, "0")
							+ "',");// 拜访客户数
					jsonSb.append("visitPlaceCount:'"
							+ CharTools.killNullLong2String(visitCusCount, "0")
							+ "'");// 拜访地点数
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
	 * 车辆定位信息
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
		 * null) { response.getWriter().write("{result:\"9\"}");// 未登录 return
		 * mapping.findForward(null); }
		 */

		// 从request中获取参数
		// String deviceIds = request.getParameter("deviceIds");// 终端id
		// deviceIds =
		// CharTools.splitAndAdd(CharTools.javaScriptEscape(deviceIds));
		// 关键字查询 add by zhaofeng
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		String deviceIdsStr = request.getParameter("deviceIds");// 终端id，多个","隔开
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "1" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
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
		 * deviceId sb.append("vehicleNumber:'',");// 名称
		 * sb.append("simcard:'',");// 手机号码 sb.append("gpsTime:'',");// 时间
		 * sb.append
		 * ("pd:'"+DateUtility.getDateWeekCnName(todayWeekNum-1)+"休息日',");//
		 * 位置描述 sb.append("visitId:'',");// 标注点名称id
		 * sb.append("visitName:'',");// 到访客户 sb.append("longitude:'',");// 经度
		 * sb.append("latitude:'',");// 纬度 sb.append("speed:'',");// 速度
		 * sb.append("direction:'',");// 方向 sb.append("distance:'',");// 里程
		 * sb.append("accStatus:'',");// ACC 状态 sb.append("temperature:''");//温度
		 * sb.append("}"); response.setContentType("text/json; charset=utf-8");
		 * response.getWriter().write("{total: 1,data:["+sb.toString()+"]}");
		 * return mapping.findForward(null); }
		 */

		// Date startDate = DateUtility.strToDateTime(st);
		// Date endDate = DateUtility.strToDateTime(et);

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();

			Page<Object[]> list = visitStatService.listVehicleGPS(searchValue,
					deviceIds, 1, 65536, st, et);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			// excelWorkBook.addHeader("ID", 10);
			excelWorkBook.addHeader("车牌号", 15);// 终端名称
			excelWorkBook.addHeader("手机号码", 15);// 手机sim卡号
			excelWorkBook.addHeader("纬度", 15);// 维度
			excelWorkBook.addHeader("经度", 15);// 经度
			excelWorkBook.addHeader("速度", 10);// 行驶速度
			excelWorkBook.addHeader("方向", 10);// 行驶方向
			excelWorkBook.addHeader("里程", 10);// 行驶里程
			excelWorkBook.addHeader("行驶状态", 10);// acc状态
			excelWorkBook.addHeader("位置描述", 30);// 位置描述
			excelWorkBook.addHeader("到访客户", 30);// 拜访点名称
			excelWorkBook.addHeader("时间", 10);// 数据上报时间
			excelWorkBook.addHeader("温度", 10);// 温度
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
							// 取得位置描述
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
			// add by 2012-12-18 zss 导出定位信息
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
			tOptLog.setOptDesc(userInfo.getUserAccount() + "导出定位信息成功");
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询定位信息成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
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
				String locDesc = "";// 位置描述
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TLocrecord locrecord = (TLocrecord) objects[0];
					String vehicleNumber = (String) objects[1];
					String simcard = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + locrecord.getDeviceId() + "',");// deviceId
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// 名称
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// 手机号码
					jsonSb.append("gpsTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(locrecord.getGpstime()))
							+ "',");// 时间
					// 如果获取位置为空
					if (locrecord.getLocDesc() == null
							|| locrecord.getLocDesc().equals("")) {
						// 当经纬度坐标不为0
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
									// 取得位置描述
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
						// 经纬度坐标为0
						else {
							locDesc = "没有收到卫星信号";// 位置描述
						}
					} else {
						locDesc = locrecord.getLocDesc();
					}
					lastPosDesc = locDesc;
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// 位置描述
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(
									locrecord.getPoiId(), "") + "',");// 标注点名称id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(locrecord.getPoiName())
							+ "',");// 到访客户
					jsonSb.append("longitude:'" + locrecord.getLongitude()
							+ "',");// 经度
					jsonSb.append("latitude:'" + locrecord.getLatitude() + "',");// 纬度
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(
									locrecord.getSpeed(), "0") + "',");// 速度
					jsonSb.append("direction:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDirection(), "0") + "',");// 方向
					jsonSb.append("distance:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDistance(), "0") + "',");// 里程
					jsonSb.append("accStatus:'"
							+ CharTools.javaScriptEscape(locrecord
									.getAccStatus()) + "',");// ACC 状态
					// jsonSb.append("varExt1:'" +
					// CharTools.javaScriptEscape(locrecord.getVarExt1()) +
					// "',");// 门状态
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(
									locrecord.getTemperature(), "0") + "'");// 温度
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "15" : request
				.getParameter("limit");

		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss
		String deviceId = request.getParameter("deviceId");// 终端deviceId
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		if (st == null || et == null || deviceId.equals("")) {
			response.getWriter().write("参数不全");// 未登录
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
						// 当经纬度坐标不为0
						if (latitude > 0 && longitude > 0) {
							double[] xs = { longitude };
							double[] ys = { latitude };
							try {
								com.sos.sosgps.api.DPoint[] dPoint = coordApizw
										.encryptConvert(xs, ys);
								String lngX = dPoint[0].getEncryptX();
								String latY = dPoint[0].getEncryptY();
								// 取得位置描述
								locDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								this.logger
										.error("listAttendanceReportDetail-encryptConvert error,"
												+ ex.getMessage());
							}
						}// 经纬度坐标为0
						else {
							locDesc = "没有收到卫星信号";// 位置描述
						}
					}
					jsonSb.append("{");
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poiName) + "',");// 到访客户（标注点）名称
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(locDesc)
							+ "',");// 到达时间
					jsonSb.append("poi_count:'"
							+ CharTools.killNullLong2String(poicount, "0")
							+ "'");// 到达时间
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
		String deviceIdsStr = request.getParameter("deviceIds");// 终端id，多个","隔开
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String start = request.getParameter("start") == null ? "0" : request
				.getParameter("start");
		String limit = request.getParameter("limit") == null ? "1" : request
				.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();

			Page<Object[]> list = visitStatService.listVehicleGPS(deviceIds, 1,
					65536, st, et);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			// excelWorkBook.addHeader("ID", 10);
			excelWorkBook.addHeader("车牌号", 15);// 终端名称
			excelWorkBook.addHeader("手机号码", 15);// 手机sim卡号
			excelWorkBook.addHeader("纬度", 15);// 维度
			excelWorkBook.addHeader("经度", 15);// 经度
			excelWorkBook.addHeader("门状态", 10);// 门状态
			excelWorkBook.addHeader("速度", 10);// 行驶速度
			excelWorkBook.addHeader("方向", 10);// 行驶方向
			excelWorkBook.addHeader("里程", 10);// 行驶里程
			excelWorkBook.addHeader("行驶状态", 10);// acc状态
			excelWorkBook.addHeader("位置描述", 30);// 位置描述
			excelWorkBook.addHeader("到访客户", 30);// 拜访点名称
			excelWorkBook.addHeader("时间", 10);// 数据上报时间
			excelWorkBook.addHeader("温度", 10);// 温度
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
					varExt1 = "开门";
				} else if (varExt1.equals("0")) {
					varExt1 = "关门";
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
							// 取得位置描述
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
			// add by 2012-12-18 zss 导出门磁信息Excel
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
			tOptLog.setOptDesc(userInfo.getUserAccount() + "导出门磁信息Excel成功");
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询门磁信息成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
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
				String locDesc = "";// 位置描述
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				for (Object[] objects : list.getResult()) {
					TLocrecord locrecord = (TLocrecord) objects[0];
					String vehicleNumber = (String) objects[1];
					String simcard = (String) objects[2];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + locrecord.getDeviceId() + "',");// deviceId
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// 名称
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// 手机号码
					jsonSb.append("gpsTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(locrecord.getGpstime()))
							+ "',");// 时间
					// 如果获取位置为空
					if (locrecord.getLocDesc() == null
							|| locrecord.getLocDesc().equals("")) {
						// 当经纬度坐标不为0
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
									// 取得位置描述
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
						// 经纬度坐标为0
						else {
							locDesc = "没有收到卫星信号";// 位置描述
						}
					} else {
						locDesc = locrecord.getLocDesc();
					}
					lastPosDesc = locDesc;
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// 位置描述
					jsonSb.append("visitId:'"
							+ CharTools.killNullLong2String(
									locrecord.getPoiId(), "") + "',");// 标注点名称id
					jsonSb.append("visitName:'"
							+ CharTools.javaScriptEscape(locrecord.getPoiName())
							+ "',");// 到访客户
					jsonSb.append("longitude:'" + locrecord.getLongitude()
							+ "',");// 经度
					jsonSb.append("latitude:'" + locrecord.getLatitude() + "',");// 纬度
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(
									locrecord.getSpeed(), "0") + "',");// 速度
					jsonSb.append("direction:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDirection(), "0") + "',");// 方向
					jsonSb.append("distance:'"
							+ CharTools.killNullFloat2String(
									locrecord.getDistance(), "0") + "',");// 里程
					jsonSb.append("accStatus:'"
							+ CharTools.javaScriptEscape(locrecord
									.getAccStatus()) + "',");// ACC 状态
					jsonSb.append("varExt1:'"
							+ CharTools.javaScriptEscape(locrecord.getVarExt1())
							+ "',");// 门状态
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(
									locrecord.getTemperature(), "0") + "'");// 温度
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
