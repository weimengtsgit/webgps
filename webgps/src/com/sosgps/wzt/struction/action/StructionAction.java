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
	 * 解除超速报警指令
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端deviceId

		deviceId = CharTools.javaScriptEscape(deviceId);

		structionService.stopSpeedAlarm(deviceId);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_LiftSpeeding);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("解除超速报警成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	/**
	 * 解除劫警指令
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端deviceId

		deviceId = CharTools.javaScriptEscape(deviceId);

		structionService.stopHoldAlarm(deviceId);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_LiftPanic);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("解除劫警成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	/**
	 * 断油断电指令
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端deviceId
		String userAccount = request.getParameter("userAccount");// 登录帐号
		String password = request.getParameter("password");// 密码
		String controlPassword = request.getParameter("controlPassword");// 断油断电密码

		deviceId = CharTools.javaScriptEscape(deviceId);
		userAccount = CharTools.killNullString(userAccount);
		userAccount = java.net.URLDecoder.decode(userAccount, "utf-8");
		userAccount = CharTools.killNullString(userAccount);
		password = CharTools.killNullString(password);
		controlPassword = CharTools.killNullString(controlPassword);

		// 判断帐号、密码是否匹配
		UserService userService = (UserService) SpringHelper
				.getBean("userService");
		List users = userService.hasAccount(userAccount, entCode);
		if (users == null || users.size() == 0) {
			response.getWriter().write("{result:\"2\"}");// 帐号不存在
			return mapping.findForward(null);
		}
		TUser user = (TUser) users.get(0);
		if (!user.getUserPassword().equals(password)) {
			response.getWriter().write("{result:\"3\"}");// 密码不匹配
			return mapping.findForward(null);
		}
		if (!user.getControlPassword().equals(controlPassword)) {
			response.getWriter().write("{result:\"4\"}");// 断油断电密码不匹配
			return mapping.findForward(null);
		}
		// TODO
		// 当前速度不能大于设置速度

		structionService.turnOffOilPower(deviceId);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_OffOilPower);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("断油断电成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	/**
	 * 恢复油电指令
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端deviceId
		// String userAccount = request.getParameter("userAccount");// 登录帐号
		// String password = request.getParameter("password");// 密码
		// String controlPassword = request.getParameter("controlPassword");//
		// 断油断电密码

		deviceId = CharTools.killNullString(deviceId);
		// userAccount = CharTools.javaScriptEscape(userAccount);
		// password = CharTools.javaScriptEscape(password);
		// controlPassword = CharTools.javaScriptEscape(controlPassword);

		// 判断帐号、密码是否匹配
		// UserService userService = (UserService) SpringHelper
		// .getBean("userService");
		// List users = userService.hasAccount(userAccount, entCode);
		// if (users == null || users.size() == 0) {
		// response.getWriter().write("{result:\"2\"}");// 帐号不存在
		// return mapping.findForward(null);
		// }
		// TUser user = (TUser) users.get(0);
		// if (!user.getUserPassword().equals(password)) {
		// response.getWriter().write("{result:\"3\"}");// 密码不匹配
		// return mapping.findForward(null);
		// }
		// if (!user.getControlPassword().equals(controlPassword)) {
		// response.getWriter().write("{result:\"4\"}");// 断油断电密码不匹配
		// return mapping.findForward(null);
		// }

		structionService.turnOnOilPower(deviceId);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_RestoreOilPower);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("恢复断油断电成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	/**
	 * 解除区域指令
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端deviceId
		deviceId = CharTools.javaScriptEscape(deviceId);
		structionService.stopHoldAlarm(deviceId);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_LiftArea);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("解除区域成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	/**
	 * 指令信息统计列表
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
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String typeStr = request.getParameter("type");
		st = st.replace("-", "");
		et = et.replace("-", "");
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);

		if (st == null || et == null || deviceIds.equals("")) {
			response.getWriter().write("参数不全");// 未登录
			return mapping.findForward(null);
		}
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
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			Long userId = tUser.getId();
			Page<Object[]> list = structionService.listStructionsRecord(0,
					65536, st, et, deviceIds, typeStr);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("终端序列号", 15);
			excelWorkBook.addHeader("指令内容", 20);
			excelWorkBook.addHeader("发送状态", 40);
			excelWorkBook.addHeader("指令类别", 20);
			excelWorkBook.addHeader("指令类别内容", 40);
			excelWorkBook.addHeader("时间", 40);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String termName = (String) objects[0];// 终端名称
				String deviceId = (String) objects[1];// 终端序列号
				String instruction = (String) objects[2];// 指令内容
				String state = (String) objects[3];// 发送状态
				String type1 = (String) objects[4];// 指令类别
				String param = (String) objects[5];// 指令内容
				String createTime = (String) objects[6];// 指令时间
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
							CharTools.javaScriptEscape("发送成功"));
				} else if (state.equals("1")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("待处理"));
				} else {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("发送失败"));
				}
				if (type1.equals("0")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("设置频率指令"));
				} else if (type1.equals("2")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("断油断电指令"));
				} else if (type1.equals("3")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("超速设置指令"));
				} else if (type1.equals("7")) {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("解除协警指令"));
				} else {
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape("其他指令"));
				}
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(param));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(createTime));
			}
			//add by 2012-12-18 zss 导出指令信息表
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
			tOptLog.setOptDesc(userInfo.getUserAccount() + "导出指令信息表成功");
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
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询指令信息成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
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
					String termName = (String) objects[0];// 终端名称
					String deviceId = (String) objects[1];// 终端序列号
					String instruction = (String) objects[2];// 指令内容
					String state = (String) objects[3];// 发送状态
					String type1 = (String) objects[4];// 指令类别
					String param = (String) objects[5];// 指令内容
					String createTime = (String) objects[6];// 指令时间
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
