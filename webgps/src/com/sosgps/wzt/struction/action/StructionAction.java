package com.sosgps.wzt.struction.action;

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

import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.struction.service.StructionService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.util.CharTools;

/**
 * <p>
 * Title:StructionAction.java
 * </p>
 */
public class StructionAction extends DispatchWebActionSupport {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(StructionAction.class);

	private StructionService structionService = (StructionService) SpringHelper
			.getBean("StructionServiceImpl");
	private UserService userService = (UserService) SpringHelper
			.getBean("userService");

	/**
	 * ������ٱ���ָ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward stopSpeedAlarmStruction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�deviceId

		deviceId = CharTools.javaScriptEscape(deviceId);

		structionService.stopSpeedAlarm(deviceId);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_LiftSpeeding);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("������ٱ����ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return null;
	}

	/**
	 * ����پ�ָ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward stopHoldAlarmStruction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�deviceId

		deviceId = CharTools.javaScriptEscape(deviceId);

		structionService.stopHoldAlarm(deviceId);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_LiftPanic);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("����پ��ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return null;
	}

	/**
	 * ���Ͷϵ�ָ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward turnOffOilPowerStruction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�deviceId
		String userAccount = request.getParameter("userAccount");// ��¼�ʺ�
		String password = request.getParameter("password");// ����
		String controlPassword = request.getParameter("controlPassword");// ���Ͷϵ�����

		deviceId = CharTools.javaScriptEscape(deviceId);
		userAccount = CharTools.killNullString(userAccount);
		userAccount = java.net.URLDecoder.decode(userAccount, "utf-8");
		userAccount = CharTools.killNullString(userAccount);
		password = CharTools.killNullString(password);
		controlPassword = CharTools.killNullString(controlPassword);

		// �ж��ʺš������Ƿ�ƥ��
		UserService userService = (UserService) SpringHelper
				.getBean("userService");
		List users = userService.hasAccount(userAccount, entCode);
		if (users == null || users.size() == 0) {
			response.getWriter().write("{result:\"2\"}");// �ʺŲ�����
			return mapping.findForward(null);
		}
		TUser user = (TUser) users.get(0);
		if (!user.getUserPassword().equals(password)) {
			response.getWriter().write("{result:\"3\"}");// ���벻ƥ��
			return mapping.findForward(null);
		}
		if (!user.getControlPassword().equals(controlPassword)) {
			response.getWriter().write("{result:\"4\"}");// ���Ͷϵ����벻ƥ��
			return mapping.findForward(null);
		}
		// TODO
		// ��ǰ�ٶȲ��ܴ��������ٶ�

		structionService.turnOffOilPower(deviceId);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_OffOilPower);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("���Ͷϵ�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return null;
	}

	/**
	 * �ָ��͵�ָ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward turnOnOilPowerStruction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�deviceId
		// String userAccount = request.getParameter("userAccount");// ��¼�ʺ�
		// String password = request.getParameter("password");// ����
		// String controlPassword = request.getParameter("controlPassword");//
		// ���Ͷϵ�����

		deviceId = CharTools.killNullString(deviceId);
		// userAccount = CharTools.javaScriptEscape(userAccount);
		// password = CharTools.javaScriptEscape(password);
		// controlPassword = CharTools.javaScriptEscape(controlPassword);

		// �ж��ʺš������Ƿ�ƥ��
		// UserService userService = (UserService) SpringHelper
		// .getBean("userService");
		// List users = userService.hasAccount(userAccount, entCode);
		// if (users == null || users.size() == 0) {
		// response.getWriter().write("{result:\"2\"}");// �ʺŲ�����
		// return mapping.findForward(null);
		// }
		// TUser user = (TUser) users.get(0);
		// if (!user.getUserPassword().equals(password)) {
		// response.getWriter().write("{result:\"3\"}");// ���벻ƥ��
		// return mapping.findForward(null);
		// }
		// if (!user.getControlPassword().equals(controlPassword)) {
		// response.getWriter().write("{result:\"4\"}");// ���Ͷϵ����벻ƥ��
		// return mapping.findForward(null);
		// }

		structionService.turnOnOilPower(deviceId);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_RestoreOilPower);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�ָ����Ͷϵ�ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return null;
	}

	/**
	 * �������ָ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward stopAreaAlarmStruction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�deviceId
		deviceId = CharTools.javaScriptEscape(deviceId);
		structionService.stopHoldAlarm(deviceId);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_LiftArea);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�������ɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return null;
	}

	/**
	 * ָ����Ϣͳ���б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listStructionsRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String typeStr = request.getParameter("type");
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
			Page<Object[]> list = structionService.listStructionsRecord(0,
					65536, st, et, deviceIds, typeStr);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("�ն����к�", 15);
			excelWorkBook.addHeader("ָ������", 20);
			excelWorkBook.addHeader("����״̬", 40);
			excelWorkBook.addHeader("ָ�����", 20);
			excelWorkBook.addHeader("ָ���������", 40);
			excelWorkBook.addHeader("ʱ��", 40);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String termName = (String) objects[0];// �ն�����
				String deviceId = (String) objects[1];// �ն����к�
				String instruction = (String) objects[2];// ָ������
				String state = (String) objects[3];// ����״̬
				String type1 = (String) objects[4];// ָ�����
				String param = (String) objects[5];// ָ������
				String createTime = (String) objects[6];// ָ��ʱ��
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(termName));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(deviceId));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(instruction));
				if (state.equals("0")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("���ͳɹ�"));
				} else if (state.equals("1")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("������"));
				} else {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("����ʧ��"));
				}
				if (type1.equals("0")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("����Ƶ��ָ��"));
				} else if (type1.equals("2")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("���Ͷϵ�ָ��"));
				} else if (type1.equals("3")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("��������ָ��"));
				} else if (type1.equals("7")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("���Э��ָ��"));
				} else {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("����ָ��"));
				}
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(param));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(createTime));
			}
			//add by 2012-12-18 zss ����ָ����Ϣ��
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_STRUCTIONS_RECORD);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount() + "����ָ����Ϣ��ɹ�");
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
		tOptLog.setFunCType(LogConstants.LOG_STAT_STRUCTIONS_RECORD);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "��ѯָ����Ϣ�ɹ�");
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
			Page<Object[]> list = structionService.listStructionsRecord(
					startint, pageSize, st, et, deviceIds, typeStr);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String termName = (String) objects[0];// �ն�����
					String deviceId = (String) objects[1];// �ն����к�
					String instruction = (String) objects[2];// ָ������
					String state = (String) objects[3];// ����״̬
					String type1 = (String) objects[4];// ָ�����
					String param = (String) objects[5];// ָ������
					String createTime = (String) objects[6];// ָ��ʱ��
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(termName) + "',");
					jsonSb.append("deviceId:'"
							+ CharTools.javaScriptEscape(deviceId) + "',");
					jsonSb.append("instruction:'"
							+ CharTools.javaScriptEscape(instruction) + "',");
					jsonSb.append("state:'" + CharTools.javaScriptEscape(state)
							+ "',");
					jsonSb.append("type1:'" + CharTools.javaScriptEscape(type1)
							+ "',");
					jsonSb.append("param:'" + CharTools.javaScriptEscape(param)
							+ "',");
					jsonSb.append("createTime:'"
							+ CharTools.javaScriptEscape(createTime) + "'");

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
