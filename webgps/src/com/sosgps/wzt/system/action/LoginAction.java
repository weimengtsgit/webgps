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
import com.sosgps.wzt.system.form.LoginForm;
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
public class LoginAction extends Action {
	private final String key = "tstx!@#$%^";
	private TargetService targetService =(TargetService)SpringHelper.getBean("targetService");
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm = (LoginForm) form;
		// 解密
		String empCodeBase64 = loginForm.getEmpCode();
		String empCode = URLDecoder.decode(empCodeBase64, "UTF-8");
		empCode = new String(new BASE64Decoder().decodeBuffer(empCode));
		loginForm.setEmpCode(empCode);
		String userAccountBase64 = loginForm.getUserAccount();
		String userAccount = URLDecoder.decode(userAccountBase64, "UTF-8");
		userAccount = new String(new BASE64Decoder().decodeBuffer(userAccount));
		loginForm.setUserAccount(userAccount);
		String passwordBase64 = loginForm.getPassword();
		String password = URLDecoder.decode(passwordBase64, "UTF-8");
		password = new String(new BASE64Decoder().decodeBuffer(password));
		loginForm.setPassword(password);
		UserService userService = (UserService) SpringHelper
				.getBean("userService");
		ShortMessageService shortMessageService = (ShortMessageService) SpringHelper
				.getBean("shortMessageService");
	   
	    TempShortMessage tempShortMessage = null;
		HttpSession session = request.getSession();
		TLoginLog tLoginLog = new TLoginLog(); // 登录日志bean
		// 清除session
		this.clearSession(session);
		TUser user = new TUser();
		TEnt tent = new TEnt();// 企业信息

		/* 修改获取源地址方式（加入网宿后需要调整） modify zw 2011-04-29 */
		String accessIp = request.getHeader("Cdn-Src-Ip");
		if (accessIp == null) {
			accessIp = request.getRemoteAddr();
		}
		// String accessIp = request.getRemoteAddr();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(new Long(-1));
		userInfo.setIp(accessIp);
		userInfo.setEmpCode(empCode);
		userInfo.setUserAccount(userAccount);
		// 增加cookie
		CookieHelper.setValue(request, response, 3600 * 24 * 31,
				"login_entCode", empCode);
		CookieHelper.setValue(request, response, 3600 * 24 * 31,
				"login_userName", userAccount);
		String validateCode = (String) session.getAttribute("ValidateCode");
		request.getSession().setAttribute("edition", loginForm.getEdition());
		request.getSession().setAttribute("cooperation",
				loginForm.getCooperation());
		String cooperation = loginForm.getCooperation();
		cooperation = CharTools.killNullString(cooperation, "");
		if (!validateCode.equals(loginForm.getValidateCode())) {
			String logIndesc = "验证码错误！";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);
			SysLogger.sysLogger.info(getLogInfo(userInfo, logIndesc));
			if (loginForm.getEdition().equals("en")) {
				request.setAttribute("error", "Security Code Error！");
			} else {
				request.setAttribute("error", logIndesc);
			}
			return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation);
		}
		/**
		 * @验证企业服务时间是否已到 update by zhaofeng 2011-6-7
		 */

		// 取得企业信息,判断企业是否存在：
		TEnt ent = null;
		List entInfoList = userService.queryEntInfo2(loginForm.getEmpCode());
		if (entInfoList == null || entInfoList.size() <= 0) {
			String logIndesc = "企业代码：" + loginForm.getEmpCode() + "不存在！";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);
			SysLogger.sysLogger.info(getLogInfo(userInfo,
					"I000: 登录操作-登录失败,企业代码不存在"));
			if (loginForm.getEdition().equals("en")) {
				request.setAttribute("error", "Enterprise Code not exist！");
			} else {
				request.setAttribute("error", logIndesc);
			}
			return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation);
			// request.setAttribute("error", logIndesc);
			// return mapping.findForward("error");
		} else {
			// update by zhaofeng 通过企业标志(0为无效，1为有效，2为服务已到期)判断企业服务有效日期
			ent = (TEnt) entInfoList.get(0);
			if (ent.getEntStatus().equals("2")) {
				String logIndesc = "企业：" + loginForm.getEmpCode() + "服务已到期！";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);
				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,企业服务已到期"));
				if (loginForm.getEdition().equals("en")) {
					request.setAttribute("error", logIndesc);
				} else {
					request.setAttribute("error", logIndesc);
				}

				return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN + "_"
						+ loginForm.getEdition() + cooperation);

			}/*
				 * else if(ent.getEntStatus().equals("2")) { String logIndesc =
				 * "企业代码：" + loginForm.getEmpCode() + "不存在！";
				 * doLoginLog(tLoginLog, userInfo, logIndesc, 0);
				 * SysLogger.sysLogger.info(getLogInfo(userInfo, "I000:
				 * 登录操作-登录失败,企业代码不存在"));
				 * if(loginForm.getEdition().equals("en")){
				 * request.setAttribute("error", "Enterprise Code not exist！");
				 * }else{ request.setAttribute("error", logIndesc); } return
				 * mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN+"_"+loginForm.getEdition()+cooperation); }
				 */

		}

		/*
		 * Configure configure = configService.getById(1L); Long isClose =
		 * configure.getParaClose(); //系统是否关闭 Long isParaLogin =
		 * configure.getParaLogin();//是否可以同一个ip多处登录
		 */

		String findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
				+ loginForm.getEdition() + cooperation;

		String logIndesc = null;

		List listUser = null;
		boolean isDynamicLogin = false;
		boolean isLoginSuccess = false;
		if (loginForm.getLoginType() != null
				&& loginForm.getLoginType().equals("2")) {// 动态密码登陆
			isDynamicLogin = true;
		}

		if (isDynamicLogin) {
			tempShortMessage = shortMessageService.findByPhone(loginForm
					.getEmpCode(), loginForm.getUserAccount(),
					shortMessageService.LOGIN_TYPE);

			if (tempShortMessage == null) {
				logIndesc = "企业代码：" + loginForm.getEmpCode() + " 用户: "
						+ loginForm.getUserAccount() + " 密码错误";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,密码错误"));
				request.setAttribute("error", "请获取动态密码");
			} else if (!tempShortMessage.getDynamicPassword().equalsIgnoreCase(
					loginForm.getPassword())) {

				logIndesc = "企业代码：" + loginForm.getEmpCode() + " 用户: "
						+ loginForm.getUserAccount() + " 密码错误";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,密码错误"));
				request.setAttribute("error", "登录失败,密码错误");

			} else if (System.currentTimeMillis()
					- tempShortMessage.getCreateTime() > Constants.MESSAGE_OUT_TIME_VALUE * 1000) {
				// 登录日志记录

				logIndesc = "企业代码：" + loginForm.getEmpCode() + " 用户: "
						+ loginForm.getUserAccount() + " 临时密码超过"
						+ Constants.MESSAGE_OUT_TIME_VALUE + "秒";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,临时密码超过"
								+ Constants.MESSAGE_OUT_TIME_VALUE + "秒"));
				request.setAttribute("error", "登录失败,临时密码超过");
			} else if (!tempShortMessage.getPhoneNumber().equalsIgnoreCase(
					loginForm.getUserAccount())) {
				logIndesc = "企业代码：" + loginForm.getEmpCode() + " 用户: "
						+ loginForm.getUserAccount() + " 用户不存在";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,用户不存在"));
				request.setAttribute("error", "登录失败,用户不存在");

			} else {
				listUser = userService.hasAccountByPhoneNum(loginForm
						.getUserAccount(), loginForm.getEmpCode());
				if (listUser == null || listUser.size() == 0) {
					logIndesc = "企业代码：" + loginForm.getEmpCode() + " 用户: "
							+ loginForm.getUserAccount() + " 用户不存在";
					doLoginLog(tLoginLog, userInfo, logIndesc, 0);

					SysLogger.sysLogger.info(getLogInfo(userInfo,
							"I000: 登录操作-登录失败,用户不存在"));
					request.setAttribute("error", "登录失败,用户不存在");
				}
			}
		} else {// 固定密码登陆
			listUser = userService.hasAccount(loginForm.getUserAccount(),
					loginForm.getEmpCode());
		}
		// 用户不为空
		if (listUser != null && listUser.size() != 0) {
			user = (TUser) listUser.get(0);
			if (isDynamicLogin
					|| user.getUserPassword().equals(loginForm.getPassword())) {

				userInfo = new UserInfo(user, tent);
				userInfo.setIp(accessIp);
				userInfo.setEnt(ent);
				userInfo.setEmpCodeBase64(empCodeBase64);
				userInfo.setUserAccountBase64(userAccountBase64);
				userInfo.setPasswordBase64(passwordBase64);

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
					findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
							+ loginForm.getEdition() + cooperation;
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
					List<Kpi> kpis = targetService.getKpi(CharTools.javaScriptEscape(ent.getEntCode()), 0);
					String templateType = "";
					if(kpis != null && kpis.size()>=1){
						Kpi kpi_ = kpis.get(0);
						templateType = kpi_.getValue();
					}
					userInfo.setTargetTemplateType(templateType);
					session.setAttribute("userInfo", userInfo);
					isLoginSuccess = true;// 登陆成功
					result = 1;
				}

				doLoginLog(tLoginLog, userInfo, logIndesc, result);

			} else {// 密码错误
				this.clearSession(session);

				// 登录日志记录
				logIndesc = "企业代码：" + loginForm.getEmpCode() + " 用户: "
						+ loginForm.getUserAccount() + " 登录失败,登录密码不正确";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: 登录操作-登录失败,密码错误"));
				if (loginForm.getEdition().equals("en")) {
					request
							.setAttribute("error",
									"Login Failed,Password Error");
				} else {
					request.setAttribute("error", "登录失败,密码错误");
				}
				findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
						+ loginForm.getEdition() + cooperation;
			}

		} else if (isDynamicLogin) {// 用户不存在,动态登陆
			findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation;
		} else {// 用户不存在，固态登陆
			// 登录日志记录

			logIndesc = "登录失败，用户: " + loginForm.getUserAccount() + "不存在！";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);

			SysLogger.sysLogger.info(getLogInfo(userInfo, "I000: 登录操作-登录失败,"
					+ loginForm.getUserAccount() + "用户不存在"));
			if (loginForm.getEdition().equals("en")) {
				request.setAttribute("error", "Login Failed,User not exist！");
			} else {
				request.setAttribute("error", logIndesc);
			}

			findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation;
		}

		try {
			LogFactory.newLogInstance("loginLogger").info(tLoginLog);
		} catch (Exception ex) {

			SysLogger.sysLogger.error("企业代码：" + loginForm.getEmpCode()
					+ " 用户: " + loginForm.getUserAccount() + "登录失败，写入日志错误", ex);
			request.setAttribute("error", "登录失败，写入日志错误");
			return mapping.findForward("error");
		}

		if (isDynamicLogin && isLoginSuccess) {// 登陆完成需要删除登陆密码
			shortMessageService.deleteTempDynamicPassword(loginForm
					.getEmpCode(), loginForm.getUserAccount(),
					shortMessageService.LOGIN_TYPE);
		}
		
		// 设置配置文件 add by weimeng 2012-09-17
		Properties properties = SystemInitListener.globalMessagesMap
				.get("globalMessages_" + empCode);
		if (properties == null) {
			properties = SystemInitListener.globalMessagesMap
					.get("globalMessages_default");
		}
		session.setAttribute("message", properties);

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
