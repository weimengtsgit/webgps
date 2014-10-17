/**
 * <p>Title:Toten</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: WinChannel </p>
 * <p>Date:Jul 7, 2006</p>
 * @author bxz
 * @version 1.0
 */
package com.sosgps.wzt.system.action;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;

import sun.misc.BASE64Decoder;

import com.sosgps.v21.listener.SystemInitListener;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TLoginLog;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.orm.TempShortMessage;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.ShortMessageService;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.system.util.CheckOnlineUser;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.CookieHelper;
import com.sosgps.wzt.util.DESEncrypt;

/**
 * <p>
 * Title: LoginAction
 * </p>
 * <p>
 * Description: 登录
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author
 * @version 1.1
 */
public class LoginAloneAction extends Action {
	private final String key = "tstx!@#$%^";
	private TargetService targetService =(TargetService)SpringHelper.getBean("targetService");

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 解密
		String empCodeBase64 = request.getParameter("empCode");
		String empCode = URLDecoder.decode(empCodeBase64, "UTF-8");
		empCode = new String(new BASE64Decoder().decodeBuffer(empCode));

		String userAccountBase64 = request.getParameter("userAccount");
		String userAccount = URLDecoder.decode(userAccountBase64, "UTF-8");
		userAccount = new String(new BASE64Decoder().decodeBuffer(userAccount));

		String passwordBase64 = request.getParameter("password");
		String password = URLDecoder.decode(passwordBase64, "UTF-8");
		password = new String(new BASE64Decoder().decodeBuffer(password));

		String cooperation = request.getParameter("cooperation");
		cooperation = CharTools.javaScriptEscape(cooperation);
		//cooperation = URLDecoder.decode(cooperation, "UTF-8");
		//cooperation = new String(new BASE64Decoder().decodeBuffer(cooperation));
		
		HttpSession session = request.getSession();
		request.getSession().setAttribute("edition", "zh_cn");
		request.getSession().setAttribute("cooperation", cooperation);
		
		// add by liuhongxiao 2012-01-30 如果用户重复登录,直接跳转到主页
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

		// 设置配置文件 add by weimeng 2012-09-17
		Properties properties = SystemInitListener.globalMessagesMap
				.get("globalMessages_" + empCode);
		if (properties == null) {
			properties = SystemInitListener.globalMessagesMap
					.get("globalMessages_default");
		}
		session.setAttribute("message", properties);

		if (userInfo != null) {
			if (userInfo.getEmpCode().equals(empCode)
					&& userInfo.getUserAccount().equals(userAccount)) {
				return mapping.findForward("welcome");
			}
		}
		// ConfigService configService =
		// (ConfigService)SpringHelper.getBean("configService");
		UserService userService = (UserService) SpringHelper
				.getBean("userService");
		ShortMessageService shortMessageService = (ShortMessageService) SpringHelper
				.getBean("shortMessageService");

		TempShortMessage tempShortMessage = null;

		TLoginLog tLoginLog = new TLoginLog(); // 登录日志bean
		// 清除session
		this.clearSession(session);
		TUser user = new TUser();
		TEnt tent = new TEnt();// 企业信息
		String accessIp = request.getRemoteAddr();

		userInfo = new UserInfo();
		userInfo.setUserId(new Long(-1));
		userInfo.setIp(accessIp);
		userInfo.setEmpCode(empCode);
		userInfo.setUserAccount(userAccount);
		userInfo.setEmpCodeBase64(empCodeBase64);
		userInfo.setUserAccountBase64(userAccountBase64);
		userInfo.setPasswordBase64(passwordBase64);
		
		List<Kpi> kpis = targetService.getKpi(CharTools.javaScriptEscape(empCode), 0);
		String templateType = "";
		if(kpis != null && kpis.size()>=1){
			Kpi kpi_ = kpis.get(0);
			templateType = kpi_.getValue();
		}
		userInfo.setTargetTemplateType(templateType);
		
		// 增加cookie
		CookieHelper.setValue(request, response, 3600 * 24 * 31,
				"login_entCode", empCode);
		CookieHelper.setValue(request, response, 3600 * 24 * 31,
				"login_userName", userAccount);

		// 取得企业信息,判断企业是否存在：
		TEnt ent = null;
		List entInfoList = userService.queryEntInfo(empCode);
		if (entInfoList == null || entInfoList.size() <= 0) {
			String logIndesc = "企业代码：" + empCode + "不存在！";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);
			SysLogger.sysLogger.info(getLogInfo(userInfo,
					"I000: 登录操作-登录失败,企业代码不存在"));
			request.setAttribute("error", logIndesc);
			return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN);
			// request.setAttribute("error", logIndesc);
			// return mapping.findForward("error");
		} else {
			ent = (TEnt) entInfoList.get(0);
		}

		/*
		 * Configure configure = configService.getById(1L); Long isClose =
		 * configure.getParaClose(); //系统是否关闭 Long isParaLogin =
		 * configure.getParaLogin();//是否可以同一个ip多处登录
		 */

		String findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;

		String logIndesc = null;

		List listUser = null;

		listUser = userService.hasAccount(userAccount, empCode);
		// 用户不为空
		if (listUser != null && listUser.size() != 0) {
			user = (TUser) listUser.get(0);
			if (user.getUserPassword().equals(password)) {
				userInfo = new UserInfo(user, tent);
				userInfo.setIp(accessIp);
				userInfo.setEnt(ent);
				session.setAttribute("message", properties);
				userInfo.setEmpCodeBase64(empCodeBase64);
				userInfo.setUserAccountBase64(userAccountBase64);
				userInfo.setPasswordBase64(passwordBase64);
				userInfo.setTargetTemplateType(templateType);
				
				// 检测是否有相同在线用户，有异常则抛出
				boolean checkFlag = true;
				int result = 1;
				try {
					CheckOnlineUser.checkUser(session, userInfo, accessIp);
					checkFlag = true;
				} catch (com.sosgps.wzt.cs.server.ex.UserInfoException e) {
					// e.printStackTrace();
					request.setAttribute("error", e.getMessage());
					SysLogger.sysLogger.info(getLogInfo(userInfo, e
							.getMessage()));
					findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;
					logIndesc = "企业代码：" + userInfo.getEmpCode() + " 用户: "
							+ userInfo.getUserAccount() + " 此用户已经登陆";
					checkFlag = false;
					result = 0;
				}
				if (checkFlag) {
					SysLogger.sysLogger.info(getLogInfo(userInfo,
							"I000: 登录操作-登录成功"));
					logIndesc = "企业代码：" + userInfo.getEmpCode() + " 用户: "
							+ userInfo.getUserAccount() + " 登录成功";
					findForwardStr = "welcome";
					Constants.registerKeys("userInfo");

					// add by liuhongxiao 2012-01-30 用于免登陆信息采集系统
					ServletContext context = super.getServlet()
							.getServletContext();
					String infoAcquisitionUrl = context
							.getInitParameter("infoAcquisitionUrl");
					DESEncrypt crypt = new DESEncrypt(key);
					String sso = URLEncoder.encode(
							crypt.encrypt(userInfo.getEmpCode() + "&"
									+ userInfo.getUserAccount() + "&"
									+ password), "utf-8");
					userInfo.setInfoAcquisitionUrl(infoAcquisitionUrl
							+ "welcome/index.action?sso=" + sso);

					session.setAttribute("userInfo", userInfo);
					result = 1;
				}

				doLoginLog(tLoginLog, userInfo, logIndesc, result);

			} else {// 密码错误
				this.clearSession(session);

				// 登录日志记录
				logIndesc = "企业代码：" + empCode + " 用户: " + userAccount
						+ " 登录失败,登录密码不正确";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,密码错误"));
				request.setAttribute("error", "登录失败,密码错误");
				findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;
			}
		} else {// 用户不存在，固态登陆
			// 登录日志记录

			logIndesc = "登录失败，用户: " + userAccount + "不存在！";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);

			SysLogger.sysLogger.info(getLogInfo(userInfo, "I000: 登录操作-登录失败,"
					+ userAccount + "用户不存在"));

			request.setAttribute("error", logIndesc);

			findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;
		}

		try {
			LogFactory.newLogInstance("loginLogger").info(tLoginLog);
		} catch (Exception ex) {

			SysLogger.sysLogger.error("企业代码：" + empCode + " 用户: " + userAccount
					+ "登录失败，写入日志错误", ex);
			request.setAttribute("error", "登录失败，写入日志错误");
			return mapping.findForward("error");
		}

		return mapping.findForward(findForwardStr);

	}

	private void doLoginLog(TLoginLog tLoginLog, UserInfo userInfo,
			String loginDesc, int result) {
		// 登录日志记录
		tLoginLog.setUserId(userInfo.getUserId());
		tLoginLog.setUserName(userInfo.getUserAccount());
		tLoginLog.setAccessIp(userInfo.getIp());
		tLoginLog.setEmpCode(userInfo.getEmpCode());
		tLoginLog.setResult(new Long(result));
		tLoginLog.setLoginDesc(loginDesc);
	}

	private String getLogInfo(UserInfo userInfo, String loginDesc) {
		String logInfo = loginDesc + "-企业代码：" + userInfo.getEmpCode()
				+ " -登录帐号：" + userInfo.getUserAccount() + " 登录时间："
				+ DateUtil.getDateTime() + " 登录者IP：" + userInfo.getIp();
		return logInfo;
	}

	protected void clearSession(HttpSession session) {
		Set sessionAttributes = Constants.SESSION_KEYS_SET;
		Iterator iter = sessionAttributes.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			session.removeAttribute(key);
		}

	}

}
