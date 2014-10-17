/**
 * 
 */
package com.sosgps.wzt.stat.action;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.sosgps.wzt.orm.TAreaLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.stat.service.AlarmStatService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

/**
 * @author
 * 
 */
public class AlarmStatAction extends DispatchWebActionSupport {
	AlarmStatService alarmStatService = (AlarmStatService) SpringHelper
			.getBean("AlarmStatServiceImpl");
	private UserService userService = (UserService) SpringHelper
			.getBean("userService");

	// sos 报警提醒列表查询
	public ActionForward listAlarmsByToday(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer jsonSb = new StringBuffer();
		// jsonSb.append("{total:");
		int total = 0;
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");

		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			jsonSb.append("[]");
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(
					"{total:" + total + ",data:" + jsonSb.toString() + "}");
			return mapping.findForward(null);
		}
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-17 zss
		TOptLog optLog = new TOptLog();

		optLog.setUserId(userId);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setEmpCode(entCode);
		optLog.setOptTime(new Date());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setOptDesc(userInfo.getUserAccount() + "报警提醒列表查询");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_ALARM);
		optLog.setResult(1L);
		LogFactory.newLogInstance("optLogger").info(optLog);

		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> alarmsList = alarmStatService.listAlarmsByToday(
					entCode, userId, page, limitint);
			if (alarmsList != null && alarmsList.getResult() != null) {
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				total = alarmsList.getTotalCount();
				jsonSb.append("[");
				Iterator i = alarmsList.getResult().iterator();
				while (i.hasNext()) {
					Object[] obj = (Object[]) i.next();
					Long showId = (Long) obj[0];
					TTerminal terminal = (TTerminal) obj[1];
					TAreaLocrecord areaLocrecord = (TAreaLocrecord) obj[2];
					// 位置描述
					String posDesc = "";
					/*
					 * if(areaLocrecord.getLongitude() > 0 &&
					 * areaLocrecord.getLatitude() > 0){
					 * if(areaLocrecord.getLocDesc()==null
					 * ||areaLocrecord.getLocDesc().equals("")){ // 取得位置描述
					 * posDesc = coordCvtApi.getAddress(areaLocrecord.getJmx(),
					 * areaLocrecord.getJmy()); }else{ posDesc =
					 * areaLocrecord.getLocDesc(); } }
					 */
					jsonSb.append("{");
					jsonSb.append("id:'" + showId + "',");// id
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(terminal.getTermName())
							+ "',");// 车主名称
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");// 车主电话
					jsonSb.append("vType:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleType()) + "',");// 车辆型号
					jsonSb.append("vNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// 车牌号
					jsonSb.append("alarmId:'" + areaLocrecord.getId() + "',");// 报警记录表id
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(
									areaLocrecord.getSpeed(), "0") + "',");// 速度
					jsonSb.append("direction:'" + areaLocrecord.getDirection()
							+ "',");// 方向

					// jsonSb.append("time:'" +
					// DateUtility.dateTimeToStr(areaLocrecord.getAlarmTime())
					jsonSb.append("time:'"
							+ DateUtility.dateTimeToStr(areaLocrecord
									.getInputdate()) + "',");// 时间
					jsonSb.append("type:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getAlarmType()) + "',");// 类型
					jsonSb.append("maxSpeed:'"
							+ CharTools.killNullLong2String(
									areaLocrecord.getSpeedLimit(), "0") + "',");// maxSpeed
					jsonSb.append("areaPoints:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getAreaPoints()) + "',");// 区域坐标
					jsonSb.append("startTime:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getStartTime()) + "',");// 区域开始时间
					jsonSb.append("endTime:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getEndTime()) + "',");// 区域结束时间
					jsonSb.append("areaType:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getAreaType()) + "',");// 区域报警类型0出区域1进区域
					jsonSb.append("x:'" + areaLocrecord.getLongitude() + "',");// x坐标
					jsonSb.append("y:'" + areaLocrecord.getLatitude() + "',");// y坐标
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(areaLocrecord.getJmx())
							+ "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(areaLocrecord.getJmy())
							+ "',");// jmy
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// 位置描述
					jsonSb.append("},");
				}
				if (jsonSb.length() > 1) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
				jsonSb.append("]");
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:" + jsonSb.toString()
		// +"}");
		response.getWriter().write(
				"{total:" + total + ",data:" + jsonSb.toString() + "}");
		return mapping.findForward(null);
	}

	// sos查询报警信息
	// public ActionForward queryAlarm(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// StringBuffer jsonSb = new StringBuffer();
	// UserInfo userInfo = this.getCurrentUser(request);
	// if (userInfo == null) {
	// response.setContentType("text/json; charset=utf-8");
	// response.getWriter().write("{}");
	// return mapping.findForward(null);
	// }
	// // 从request中获取参数
	// String alarmId = request.getParameter("alarmId");
	// String type = request.getParameter("type");
	//
	// type = CharTools.javaScriptEscape(type);
	//
	// if (type.equals("1")) {// 超速
	// TSpeedCase speedCase = (TSpeedCase) alarmStatService.queryAlarm(
	// alarmId, type);
	// jsonSb.append("id:'" + speedCase.getId() + "',");// id
	// jsonSb.append("maxSpeed:'" + speedCase.getMaxSpeed() + "'");// maxSpeed
	// } else if (type.equals("2")) {// 区域
	// Object[] objects = (Object[]) alarmStatService.queryAlarm(alarmId,
	// type);
	// TArea area = (TArea) objects[0];
	// RefTermAreaalarm refTermAreaalarm = (RefTermAreaalarm) objects[1];
	// jsonSb.append("id:'" + area.getId() + "',");// id
	// jsonSb.append("xy:'" + area.getXy() + "',");// 区域xy
	// jsonSb.append("type:'" + refTermAreaalarm.getAlarmType() + "'");//
	// type：0出区域1进区域
	// }
	// // System.out.println("{" + jsonSb.toString() + "}");
	// response.setContentType("text/json; charset=utf-8");
	// response.getWriter().write("{" + jsonSb.toString() + "}");
	// return mapping.findForward(null);
	// }

	// sos解除报警
	public ActionForward removeAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"3\"}");// 其他错误
			return mapping.findForward(null);
		}
		// 从request中获取参数
		String showId = request.getParameter("id");// id

		boolean result = alarmStatService.removeAlarm(showId);
		response.setContentType("text/json; charset=utf-8");
		if (result)
			response.getWriter().write("{result:\"1\"}");// 成功
		else
			response.getWriter().write("{result:\"2\"}");// 失败
		return mapping.findForward(null);
	}

	// sos超速报警列表
	public ActionForward listSpeedAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("参数不全");// 未登录
			return mapping.findForward(null);
		}
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));

			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = alarmStatService.listSpeedAlarms(entCode,
					userId, 1, 65535, startTime, endTime, searchValue,
					deviceIds);
			// 查询结果为空
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// 查询结果为空
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("报警时间", 20);
			excelWorkBook.addHeader("速度", 15);
			excelWorkBook.addHeader("速度阀值", 15);
			excelWorkBook.addHeader("位置描述", 50);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			for (int i = 0; i < list.getResult().size(); i++) {
				int col = 0;
				int row = i + 1;
				Object[] obj = list.getResult().get(i);
				TAreaLocrecord al = (TAreaLocrecord) obj[0];
				TTerminal terminal = (TTerminal) obj[1];

				// 位置描述
				String posDesc = "";
				if (al.getLongitude() > 0 && al.getLatitude() > 0) {
					if (al.getLocDesc() == null || al.getLocDesc().equals("")) {
						// 取得位置描述
						posDesc = coordCvtApi.getAddress(al.getJmx(),
								al.getJmy());
					} else {
						posDesc = al.getLocDesc();
					}
				}
				excelWorkBook
						.addCell(col++, row, CharTools
								.javaScriptEscape(terminal.getVehicleNumber()));
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateTimeToStr(al
								.getAlarmTime())));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullFloat2String(al.getSpeed(), "0"));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullLong2String(al.getSpeedLimit(), "0"));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(posDesc));
			}
			// add by 2012-12-17 zss 超速报警日志记录导出
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog optLog = new TOptLog();
			optLog.setUserId(userInfo.getUserId());
			optLog.setUserName(userInfo.getUserAccount());
			optLog.setEmpCode(entCode);
			optLog.setOptTime(new Date());
			optLog.setAccessIp(userInfo.getIp());
			optLog.setOptDesc(userInfo.getUserAccount() + "超速报警日志记录导出成功");
			optLog.setFunFType(LogConstants.LOG_STAT);
			optLog.setFunCType(LogConstants.LOG_STAT_SpeedExcel);
			optLog.setResult(1L);
			LogFactory.newLogInstance("optLogger").info(optLog);

			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);

		// excel
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_SPEEDALARM);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询超速报警成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = alarmStatService.listSpeedAlarms(entCode,
					userId, page, limitint, startTime, endTime, searchValue,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] obj = iterator.next();
					TAreaLocrecord al = (TAreaLocrecord) obj[0];
					TTerminal terminal = (TTerminal) obj[1];

					// 位置描述
					String posDesc = "";
					if (al.getLongitude() > 0 && al.getLatitude() > 0) {
						if (al.getLocDesc() == null
								|| al.getLocDesc().equals("")) {
							// 取得位置描述
							posDesc = coordCvtApi.getAddress(al.getJmx(),
									al.getJmy());
						} else {
							posDesc = al.getLocDesc();
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + al.getId() + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// 车牌号码
					jsonSb.append("alarmTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(al.getAlarmTime())) + "',");// 报警时间
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(al.getSpeed(), "0")
							+ "',");// 速度
					jsonSb.append("speedLimit:'"
							+ CharTools.killNullLong2String(al.getSpeedLimit(),
									"0") + "',");// 速度阀值
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					// todo
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// 位置描述
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

	// sos区域报警列表
	public ActionForward listAreaAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("参数不全");
			return mapping.findForward(null);
		}
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
		deviceIds = CharTools.splitAndAdd(deviceIds);

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");

			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));

			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();

			Page<Object[]> list = alarmStatService.listAreaAlarms(entCode,
					userId, 1, 65535, startTime, endTime, searchValue,
					deviceIds);
			// 查询结果为空
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// 查询结果为空
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("报警时间", 20);
			excelWorkBook.addHeader("开始时间", 20);
			excelWorkBook.addHeader("结束时间", 20);
			excelWorkBook.addHeader("区域报警类型", 20);
			excelWorkBook.addHeader("位置描述", 50);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			for (int i = 0; i < list.getResult().size(); i++) {
				int col = 0;
				int row = i + 1;
				Object[] obj = list.getResult().get(i);
				TAreaLocrecord al = (TAreaLocrecord) obj[0];
				TTerminal terminal = (TTerminal) obj[1];

				// 位置描述
				String posDesc = "";
				if (al.getLongitude() > 0 && al.getLatitude() > 0) {
					if (al.getLocDesc() == null || al.getLocDesc().equals("")) {
						// 取得位置描述
						posDesc = coordCvtApi.getAddress(al.getJmx(),
								al.getJmy());
					} else {
						posDesc = al.getLocDesc();
					}
				}
				excelWorkBook
						.addCell(col++, row, CharTools
								.javaScriptEscape(terminal.getVehicleNumber()));
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateTimeToStr(al
								.getAlarmTime())));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(al.getStartTime()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(al.getEndTime()));
				String alarmType = CharTools.javaScriptEscape(al.getAreaType());// 0出区域1进区域
				excelWorkBook.addCell(col++, row, alarmType.equals("0") ? "出区域"
						: "进区域");
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(posDesc));
			}

			excelWorkBook.write();
			
			// add by 2012-12-18 zss 导出区域报警统计成功
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(entCode);
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_AREAALARM);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "导出区域报警统计成功");
			LogFactory.newLogInstance("optLogger").info(tOptLog);

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
		tOptLog.setFunCType(LogConstants.LOG_STAT_AREAALARM);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询区域报警统计成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = alarmStatService.listAreaAlarms(entCode,
					userId, page, limitint, startTime, endTime, searchValue,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] obj = iterator.next();
					TAreaLocrecord al = (TAreaLocrecord) obj[0];
					TTerminal terminal = (TTerminal) obj[1];

					// 位置描述
					String posDesc = "";
					if (al.getLongitude() > 0 && al.getLatitude() > 0) {
						if (al.getLocDesc() == null
								|| al.getLocDesc().equals("")) {
							// 取得位置描述
							posDesc = coordCvtApi.getAddress(al.getJmx(),
									al.getJmy());
						} else {
							posDesc = al.getLocDesc();
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + al.getId() + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// 车牌号码
					jsonSb.append("alarmTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(al.getAlarmTime())) + "',");// 报警时间
					jsonSb.append("areaPoints:'"
							+ CharTools.javaScriptEscape(al.getAreaPoints())
							+ "',");// 区域
					jsonSb.append("startTime:'"
							+ CharTools.javaScriptEscape(al.getStartTime())
							+ "',");// 开始时间
					jsonSb.append("endTime:'"
							+ CharTools.javaScriptEscape(al.getEndTime())
							+ "',");// 结束时间
					jsonSb.append("areaType:'"
							+ CharTools.javaScriptEscape(al.getAreaType())
							+ "',");// 区域报警类型0出区域1进区域
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					// todo
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// 位置描述
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}

		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos主动报警列表
	public ActionForward listHoldAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.setContentType("text/json; charset=utf-8");
		 * response.getWriter().write("{result:\"9\"}");// 未登录 return
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
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");

			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(
					new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));

			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			TUser tUser = userService.findUserByLoginParam(entCode,
					userAccount, password);
			if (tUser == null) {
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = alarmStatService.listHoldAlarms(entCode,
					userId, 1, 65535, startTime, endTime, searchValue,
					deviceIds);
			// 查询结果为空
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// 查询结果为空
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("报警时间", 20);
			excelWorkBook.addHeader("位置描述", 50);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			for (int i = 0; i < list.getResult().size(); i++) {
				int col = 0;
				int row = i + 1;
				Object[] obj = list.getResult().get(i);
				TAreaLocrecord al = (TAreaLocrecord) obj[0];
				TTerminal terminal = (TTerminal) obj[1];

				// 位置描述
				String posDesc = "";
				if (al.getLongitude() > 0 && al.getLatitude() > 0) {
					if (al.getLocDesc() == null || al.getLocDesc().equals("")) {
						// 取得位置描述
						posDesc = coordCvtApi.getAddress(al.getJmx(),
								al.getJmy());
					} else {
						posDesc = al.getLocDesc();
					}
				}
				excelWorkBook
						.addCell(col++, row, CharTools
								.javaScriptEscape(terminal.getVehicleNumber()));
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape(DateUtility.dateTimeToStr(al
								.getAlarmTime())));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(posDesc));
			}

			// add by 2012-12-18 zss 主动报警导出成功
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_HOLDALARM);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "主动报警导出成功");
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);

		// excelLog
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_HOLDALARM);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询主动报警成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = alarmStatService.listHoldAlarms(entCode,
					userId, page, limitint, startTime, endTime, searchValue,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] obj = iterator.next();
					TAreaLocrecord al = (TAreaLocrecord) obj[0];
					TTerminal terminal = (TTerminal) obj[1];

					// 位置描述
					String posDesc = "";
					if (al.getLongitude() > 0 && al.getLatitude() > 0) {
						if (al.getLocDesc() == null
								|| al.getLocDesc().equals("")) {
							// 取得位置描述
							posDesc = coordCvtApi.getAddress(al.getJmx(),
									al.getJmy());
						} else {
							posDesc = al.getLocDesc();
						}
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + al.getId() + "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// 车牌号码
					jsonSb.append("alarmTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(al.getAlarmTime())) + "',");// 报警时间
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					// todo
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// 位置描述
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

	// sos当日所有报警列表
	public ActionForward listAllAlarmByToday(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserInfo userInfo = this.getCurrentUser(request);

		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		// HH:mm:ss
		// String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		// HH:mm:ss

		// Date startTime = DateUtility.strToDateTime(st);
		// Date endTime = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// 当日
		java.util.Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date startTime = c.getTime();
		// 明日
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date endTime = c.getTime();

		// 是否导出excel
		// String expExcel = request.getParameter("expExcel");// true为导出
		// expExcel = CharTools.javaScriptEscape(expExcel);
		// if(expExcel.equalsIgnoreCase("true")){
		// ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
		//
		// Page<Object[]> list = alarmStatService.listAllAlarms(entCode,
		// userId, 1, 65535, startTime, endTime, searchValue);
		// // header
		// excelWorkBook.addHeader("名称", 15);
		// excelWorkBook.addHeader("报警类型", 20);
		// excelWorkBook.addHeader("报警时间", 20);
		// excelWorkBook.addHeader("位置描述", 50);
		// CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		// for (int i = 0; i < list.getResult().size(); i++) {
		// int col = 0;
		// int row = i + 1;
		// Object[] obj = list.getResult().get(i);
		// TAreaLocrecord al = (TAreaLocrecord) obj[0];
		// TTerminal terminal = (TTerminal) obj[1];
		//
		// // 位置描述
		// String posDesc = "";
		// if(al.getLongitude() > 0 && al.getLatitude() > 0){
		// if(al.getLocDesc()==null ||al.getLocDesc().equals("")){
		// // 取得位置描述
		// posDesc = coordCvtApi.getAddress(al.getJmx(), al.getJmy());
		// }else{
		// posDesc = al.getLocDesc();
		// }
		// }
		// excelWorkBook.addCell(col++, row,
		// CharTools.javaScriptEscape(terminal.getVehicleNumber()));
		// excelWorkBook.addCell(col++, row, al.getAlarmType());//
		// excelWorkBook.addCell(col++, row,
		// CharTools.javaScriptEscape(DateUtility.dateTimeToStr(al.getAlarmTime())));
		// excelWorkBook.addCell(col++, row,
		// CharTools.javaScriptEscape(posDesc));
		// }
		// excelWorkBook.write();
		// return null;
		// }

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = alarmStatService.listAllAlarms(entCode,
					userId, page, limitint, startTime, endTime, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] obj = iterator.next();
					TAreaLocrecord al = (TAreaLocrecord) obj[0];
					TTerminal terminal = (TTerminal) obj[1];

					// 位置描述
					String posDesc = "";
					/*
					 * if(al.getLongitude() > 0 && al.getLatitude() > 0){
					 * if(al.getLocDesc()==null ||al.getLocDesc().equals("")){
					 * // 取得位置描述 posDesc = coordCvtApi.getAddress(al.getJmx(),
					 * al.getJmy()); }else{ posDesc = al.getLocDesc(); } }
					 */
					jsonSb.append("{");
					jsonSb.append("id:'" + al.getId() + "',");// id
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(terminal.getTermName())
							+ "',");// 车主名称
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");// 车主电话
					jsonSb.append("vType:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleType()) + "',");// 车辆型号
					jsonSb.append("vNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// 车牌号
					jsonSb.append("alarmId:'" + al.getId() + "',");// 报警记录表id
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(al.getSpeed(), "0")
							+ "',");// 速度
					jsonSb.append("direction:'" + al.getDirection() + "',");// 方向
					// jsonSb.append("time:'" +
					// DateUtility.dateTimeToStr(al.getAlarmTime())
					jsonSb.append("time:'"
							+ DateUtility.dateTimeToStr(al.getInputdate())
							+ "',");// 时间
					jsonSb.append("type:'"
							+ CharTools.javaScriptEscape(al.getAlarmType())
							+ "',");// 类型
					jsonSb.append("maxSpeed:'"
							+ CharTools.killNullLong2String(al.getSpeedLimit(),
									"0") + "',");// maxSpeed
					jsonSb.append("areaPoints:'"
							+ CharTools.javaScriptEscape(al.getAreaPoints())
							+ "',");// 区域坐标
					jsonSb.append("startTime:'"
							+ CharTools.javaScriptEscape(al.getStartTime())
							+ "',");// 区域开始时间
					jsonSb.append("endTime:'"
							+ CharTools.javaScriptEscape(al.getEndTime())
							+ "',");// 区域结束时间
					jsonSb.append("areaType:'"
							+ CharTools.javaScriptEscape(al.getAreaType())
							+ "',");// 区域报警类型0出区域1进区域
					jsonSb.append("x:'" + al.getLongitude() + "',");// x坐标
					jsonSb.append("y:'" + al.getLatitude() + "',");// y坐标
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// 位置描述
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
		// add by 2012-12-17 zss 查询所有报警记录
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_ALARM);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询所有报警记录成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		return mapping.findForward(null);
	}

	// sos解除报警
	public ActionForward removeAllAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"3\"}");// 其他错误
			return mapping.findForward(null);
		}
		String empCode = userInfo.getEmpCode();
		boolean result = alarmStatService.removeAllAlarm(empCode);
		response.setContentType("text/json; charset=utf-8");

		// add by 2012-12-17 zss
		Long userId = userInfo.getUserId();
		TOptLog optLog = new TOptLog();
		optLog.setUserId(userId);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setEmpCode(empCode);
		optLog.setOptTime(new Date());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_RemoveAllAlarm);
		optLog.setResult(1L);

		if (result) {
			// add by 2012-12-17 zss
			optLog.setOptDesc(userInfo.getUserAccount() + "清除所有报警成功");
			response.getWriter().write("{result:\"1\"}");// 成功
		} else {
			// add by 2012-12-17 zss
			optLog.setOptDesc(userInfo.getUserAccount() + "清除所有报警失败");
			response.getWriter().write("{result:\"2\"}");// 失败
		}
		LogFactory.newLogInstance("optLogger").info(optLog);
		return mapping.findForward(null);
	}
}
