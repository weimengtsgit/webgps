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

	// sos ���������б��ѯ
	public ActionForward listAlarmsByToday(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer jsonSb = new StringBuffer();
		// jsonSb.append("{total:");
		int total = 0;
		// ��request�л�ȡ����
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
		optLog.setOptDesc(userInfo.getUserAccount() + "���������б��ѯ");
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
					// λ������
					String posDesc = "";
					/*
					 * if(areaLocrecord.getLongitude() > 0 &&
					 * areaLocrecord.getLatitude() > 0){
					 * if(areaLocrecord.getLocDesc()==null
					 * ||areaLocrecord.getLocDesc().equals("")){ // ȡ��λ������
					 * posDesc = coordCvtApi.getAddress(areaLocrecord.getJmx(),
					 * areaLocrecord.getJmy()); }else{ posDesc =
					 * areaLocrecord.getLocDesc(); } }
					 */
					jsonSb.append("{");
					jsonSb.append("id:'" + showId + "',");// id
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(terminal.getTermName())
							+ "',");// ��������
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");// �����绰
					jsonSb.append("vType:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleType()) + "',");// �����ͺ�
					jsonSb.append("vNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// ���ƺ�
					jsonSb.append("alarmId:'" + areaLocrecord.getId() + "',");// ������¼��id
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(
									areaLocrecord.getSpeed(), "0") + "',");// �ٶ�
					jsonSb.append("direction:'" + areaLocrecord.getDirection()
							+ "',");// ����

					// jsonSb.append("time:'" +
					// DateUtility.dateTimeToStr(areaLocrecord.getAlarmTime())
					jsonSb.append("time:'"
							+ DateUtility.dateTimeToStr(areaLocrecord
									.getInputdate()) + "',");// ʱ��
					jsonSb.append("type:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getAlarmType()) + "',");// ����
					jsonSb.append("maxSpeed:'"
							+ CharTools.killNullLong2String(
									areaLocrecord.getSpeedLimit(), "0") + "',");// maxSpeed
					jsonSb.append("areaPoints:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getAreaPoints()) + "',");// ��������
					jsonSb.append("startTime:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getStartTime()) + "',");// ����ʼʱ��
					jsonSb.append("endTime:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getEndTime()) + "',");// �������ʱ��
					jsonSb.append("areaType:'"
							+ CharTools.javaScriptEscape(areaLocrecord
									.getAreaType()) + "',");// ���򱨾�����0������1������
					jsonSb.append("x:'" + areaLocrecord.getLongitude() + "',");// x����
					jsonSb.append("y:'" + areaLocrecord.getLatitude() + "',");// y����
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(areaLocrecord.getJmx())
							+ "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(areaLocrecord.getJmy())
							+ "',");// jmy
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// λ������
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

	// sos��ѯ������Ϣ
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
	// // ��request�л�ȡ����
	// String alarmId = request.getParameter("alarmId");
	// String type = request.getParameter("type");
	//
	// type = CharTools.javaScriptEscape(type);
	//
	// if (type.equals("1")) {// ����
	// TSpeedCase speedCase = (TSpeedCase) alarmStatService.queryAlarm(
	// alarmId, type);
	// jsonSb.append("id:'" + speedCase.getId() + "',");// id
	// jsonSb.append("maxSpeed:'" + speedCase.getMaxSpeed() + "'");// maxSpeed
	// } else if (type.equals("2")) {// ����
	// Object[] objects = (Object[]) alarmStatService.queryAlarm(alarmId,
	// type);
	// TArea area = (TArea) objects[0];
	// RefTermAreaalarm refTermAreaalarm = (RefTermAreaalarm) objects[1];
	// jsonSb.append("id:'" + area.getId() + "',");// id
	// jsonSb.append("xy:'" + area.getXy() + "',");// ����xy
	// jsonSb.append("type:'" + refTermAreaalarm.getAlarmType() + "'");//
	// type��0������1������
	// }
	// // System.out.println("{" + jsonSb.toString() + "}");
	// response.setContentType("text/json; charset=utf-8");
	// response.getWriter().write("{" + jsonSb.toString() + "}");
	// return mapping.findForward(null);
	// }

	// sos�������
	public ActionForward removeAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"3\"}");// ��������
			return mapping.findForward(null);
		}
		// ��request�л�ȡ����
		String showId = request.getParameter("id");// id

		boolean result = alarmStatService.removeAlarm(showId);
		response.setContentType("text/json; charset=utf-8");
		if (result)
			response.getWriter().write("{result:\"1\"}");// �ɹ�
		else
			response.getWriter().write("{result:\"2\"}");// ʧ��
		return mapping.findForward(null);
	}

	// sos���ٱ����б�
	public ActionForward listSpeedAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("������ȫ");// δ��¼
			return mapping.findForward(null);
		}
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

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
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = alarmStatService.listSpeedAlarms(entCode,
					userId, 1, 65535, startTime, endTime, searchValue,
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
			excelWorkBook.addHeader("����ʱ��", 20);
			excelWorkBook.addHeader("�ٶ�", 15);
			excelWorkBook.addHeader("�ٶȷ�ֵ", 15);
			excelWorkBook.addHeader("λ������", 50);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			for (int i = 0; i < list.getResult().size(); i++) {
				int col = 0;
				int row = i + 1;
				Object[] obj = list.getResult().get(i);
				TAreaLocrecord al = (TAreaLocrecord) obj[0];
				TTerminal terminal = (TTerminal) obj[1];

				// λ������
				String posDesc = "";
				if (al.getLongitude() > 0 && al.getLatitude() > 0) {
					if (al.getLocDesc() == null || al.getLocDesc().equals("")) {
						// ȡ��λ������
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
			// add by 2012-12-17 zss ���ٱ�����־��¼����
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog optLog = new TOptLog();
			optLog.setUserId(userInfo.getUserId());
			optLog.setUserName(userInfo.getUserAccount());
			optLog.setEmpCode(entCode);
			optLog.setOptTime(new Date());
			optLog.setAccessIp(userInfo.getIp());
			optLog.setOptDesc(userInfo.getUserAccount() + "���ٱ�����־��¼�����ɹ�");
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ���ٱ����ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
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

					// λ������
					String posDesc = "";
					if (al.getLongitude() > 0 && al.getLatitude() > 0) {
						if (al.getLocDesc() == null
								|| al.getLocDesc().equals("")) {
							// ȡ��λ������
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
									.getVehicleNumber()) + "',");// ���ƺ���
					jsonSb.append("alarmTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(al.getAlarmTime())) + "',");// ����ʱ��
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(al.getSpeed(), "0")
							+ "',");// �ٶ�
					jsonSb.append("speedLimit:'"
							+ CharTools.killNullLong2String(al.getSpeedLimit(),
									"0") + "',");// �ٶȷ�ֵ
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					// todo
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// λ������
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

	// sos���򱨾��б�
	public ActionForward listAreaAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("������ȫ");
			return mapping.findForward(null);
		}
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
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
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();

			Page<Object[]> list = alarmStatService.listAreaAlarms(entCode,
					userId, 1, 65535, startTime, endTime, searchValue,
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
			excelWorkBook.addHeader("����ʱ��", 20);
			excelWorkBook.addHeader("��ʼʱ��", 20);
			excelWorkBook.addHeader("����ʱ��", 20);
			excelWorkBook.addHeader("���򱨾�����", 20);
			excelWorkBook.addHeader("λ������", 50);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			for (int i = 0; i < list.getResult().size(); i++) {
				int col = 0;
				int row = i + 1;
				Object[] obj = list.getResult().get(i);
				TAreaLocrecord al = (TAreaLocrecord) obj[0];
				TTerminal terminal = (TTerminal) obj[1];

				// λ������
				String posDesc = "";
				if (al.getLongitude() > 0 && al.getLatitude() > 0) {
					if (al.getLocDesc() == null || al.getLocDesc().equals("")) {
						// ȡ��λ������
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
				String alarmType = CharTools.javaScriptEscape(al.getAreaType());// 0������1������
				excelWorkBook.addCell(col++, row, alarmType.equals("0") ? "������"
						: "������");
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(posDesc));
			}

			excelWorkBook.write();
			
			// add by 2012-12-18 zss �������򱨾�ͳ�Ƴɹ�
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
			tOptLog.setOptDesc(userInfo.getUserAccount() + "�������򱨾�ͳ�Ƴɹ�");
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ���򱨾�ͳ�Ƴɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
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

					// λ������
					String posDesc = "";
					if (al.getLongitude() > 0 && al.getLatitude() > 0) {
						if (al.getLocDesc() == null
								|| al.getLocDesc().equals("")) {
							// ȡ��λ������
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
									.getVehicleNumber()) + "',");// ���ƺ���
					jsonSb.append("alarmTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(al.getAlarmTime())) + "',");// ����ʱ��
					jsonSb.append("areaPoints:'"
							+ CharTools.javaScriptEscape(al.getAreaPoints())
							+ "',");// ����
					jsonSb.append("startTime:'"
							+ CharTools.javaScriptEscape(al.getStartTime())
							+ "',");// ��ʼʱ��
					jsonSb.append("endTime:'"
							+ CharTools.javaScriptEscape(al.getEndTime())
							+ "',");// ����ʱ��
					jsonSb.append("areaType:'"
							+ CharTools.javaScriptEscape(al.getAreaType())
							+ "',");// ���򱨾�����0������1������
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					// todo
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// λ������
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

	// sos���������б�
	public ActionForward listHoldAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * UserInfo userInfo = this.getCurrentUser(request); if (userInfo ==
		 * null) { response.setContentType("text/json; charset=utf-8");
		 * response.getWriter().write("{result:\"9\"}");// δ��¼ return
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
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

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
				response.getWriter().write("��Ȩ����");// δ��¼
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = alarmStatService.listHoldAlarms(entCode,
					userId, 1, 65535, startTime, endTime, searchValue,
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
			excelWorkBook.addHeader("����ʱ��", 20);
			excelWorkBook.addHeader("λ������", 50);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			for (int i = 0; i < list.getResult().size(); i++) {
				int col = 0;
				int row = i + 1;
				Object[] obj = list.getResult().get(i);
				TAreaLocrecord al = (TAreaLocrecord) obj[0];
				TTerminal terminal = (TTerminal) obj[1];

				// λ������
				String posDesc = "";
				if (al.getLongitude() > 0 && al.getLatitude() > 0) {
					if (al.getLocDesc() == null || al.getLocDesc().equals("")) {
						// ȡ��λ������
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

			// add by 2012-12-18 zss �������������ɹ�
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
			tOptLog.setOptDesc(userInfo.getUserAccount() + "�������������ɹ�");
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ���������ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
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

					// λ������
					String posDesc = "";
					if (al.getLongitude() > 0 && al.getLatitude() > 0) {
						if (al.getLocDesc() == null
								|| al.getLocDesc().equals("")) {
							// ȡ��λ������
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
									.getVehicleNumber()) + "',");// ���ƺ���
					jsonSb.append("alarmTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(al.getAlarmTime())) + "',");// ����ʱ��
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					// todo
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// λ������
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

	// sos�������б����б�
	public ActionForward listAllAlarmByToday(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserInfo userInfo = this.getCurrentUser(request);

		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		// String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss

		// Date startTime = DateUtility.strToDateTime(st);
		// Date endTime = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// ����
		java.util.Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date startTime = c.getTime();
		// ����
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date endTime = c.getTime();

		// �Ƿ񵼳�excel
		// String expExcel = request.getParameter("expExcel");// trueΪ����
		// expExcel = CharTools.javaScriptEscape(expExcel);
		// if(expExcel.equalsIgnoreCase("true")){
		// ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
		//
		// Page<Object[]> list = alarmStatService.listAllAlarms(entCode,
		// userId, 1, 65535, startTime, endTime, searchValue);
		// // header
		// excelWorkBook.addHeader("����", 15);
		// excelWorkBook.addHeader("��������", 20);
		// excelWorkBook.addHeader("����ʱ��", 20);
		// excelWorkBook.addHeader("λ������", 50);
		// CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		// for (int i = 0; i < list.getResult().size(); i++) {
		// int col = 0;
		// int row = i + 1;
		// Object[] obj = list.getResult().get(i);
		// TAreaLocrecord al = (TAreaLocrecord) obj[0];
		// TTerminal terminal = (TTerminal) obj[1];
		//
		// // λ������
		// String posDesc = "";
		// if(al.getLongitude() > 0 && al.getLatitude() > 0){
		// if(al.getLocDesc()==null ||al.getLocDesc().equals("")){
		// // ȡ��λ������
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

					// λ������
					String posDesc = "";
					/*
					 * if(al.getLongitude() > 0 && al.getLatitude() > 0){
					 * if(al.getLocDesc()==null ||al.getLocDesc().equals("")){
					 * // ȡ��λ������ posDesc = coordCvtApi.getAddress(al.getJmx(),
					 * al.getJmy()); }else{ posDesc = al.getLocDesc(); } }
					 */
					jsonSb.append("{");
					jsonSb.append("id:'" + al.getId() + "',");// id
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(terminal.getTermName())
							+ "',");// ��������
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");// �����绰
					jsonSb.append("vType:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleType()) + "',");// �����ͺ�
					jsonSb.append("vNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");// ���ƺ�
					jsonSb.append("alarmId:'" + al.getId() + "',");// ������¼��id
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(al.getSpeed(), "0")
							+ "',");// �ٶ�
					jsonSb.append("direction:'" + al.getDirection() + "',");// ����
					// jsonSb.append("time:'" +
					// DateUtility.dateTimeToStr(al.getAlarmTime())
					jsonSb.append("time:'"
							+ DateUtility.dateTimeToStr(al.getInputdate())
							+ "',");// ʱ��
					jsonSb.append("type:'"
							+ CharTools.javaScriptEscape(al.getAlarmType())
							+ "',");// ����
					jsonSb.append("maxSpeed:'"
							+ CharTools.killNullLong2String(al.getSpeedLimit(),
									"0") + "',");// maxSpeed
					jsonSb.append("areaPoints:'"
							+ CharTools.javaScriptEscape(al.getAreaPoints())
							+ "',");// ��������
					jsonSb.append("startTime:'"
							+ CharTools.javaScriptEscape(al.getStartTime())
							+ "',");// ����ʼʱ��
					jsonSb.append("endTime:'"
							+ CharTools.javaScriptEscape(al.getEndTime())
							+ "',");// �������ʱ��
					jsonSb.append("areaType:'"
							+ CharTools.javaScriptEscape(al.getAreaType())
							+ "',");// ���򱨾�����0������1������
					jsonSb.append("x:'" + al.getLongitude() + "',");// x����
					jsonSb.append("y:'" + al.getLatitude() + "',");// y����
					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(al.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(al.getJmy()) + "',");// jmy
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// λ������
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
		// add by 2012-12-17 zss ��ѯ���б�����¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_ALARM);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯ���б�����¼�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		return mapping.findForward(null);
	}

	// sos�������
	public ActionForward removeAllAlarm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"3\"}");// ��������
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
			optLog.setOptDesc(userInfo.getUserAccount() + "������б����ɹ�");
			response.getWriter().write("{result:\"1\"}");// �ɹ�
		} else {
			// add by 2012-12-17 zss
			optLog.setOptDesc(userInfo.getUserAccount() + "������б���ʧ��");
			response.getWriter().write("{result:\"2\"}");// ʧ��
		}
		LogFactory.newLogInstance("optLogger").info(optLog);
		return mapping.findForward(null);
	}
}
