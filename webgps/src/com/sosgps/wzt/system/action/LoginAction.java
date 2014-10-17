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
 * Description: ��¼
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
		// ����
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
		TLoginLog tLoginLog = new TLoginLog(); // ��¼��־bean
		// ���session
		this.clearSession(session);
		TUser user = new TUser();
		TEnt tent = new TEnt();// ��ҵ��Ϣ

		/* �޸Ļ�ȡԴ��ַ��ʽ���������޺���Ҫ������ modify zw 2011-04-29 */
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
		// ����cookie
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
			String logIndesc = "��֤�����";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);
			SysLogger.sysLogger.info(getLogInfo(userInfo, logIndesc));
			if (loginForm.getEdition().equals("en")) {
				request.setAttribute("error", "Security Code Error��");
			} else {
				request.setAttribute("error", logIndesc);
			}
			return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation);
		}
		/**
		 * @��֤��ҵ����ʱ���Ƿ��ѵ� update by zhaofeng 2011-6-7
		 */

		// ȡ����ҵ��Ϣ,�ж���ҵ�Ƿ���ڣ�
		TEnt ent = null;
		List entInfoList = userService.queryEntInfo2(loginForm.getEmpCode());
		if (entInfoList == null || entInfoList.size() <= 0) {
			String logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + "�����ڣ�";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);
			SysLogger.sysLogger.info(getLogInfo(userInfo,
					"I000: ��¼����-��¼ʧ��,��ҵ���벻����"));
			if (loginForm.getEdition().equals("en")) {
				request.setAttribute("error", "Enterprise Code not exist��");
			} else {
				request.setAttribute("error", logIndesc);
			}
			return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation);
			// request.setAttribute("error", logIndesc);
			// return mapping.findForward("error");
		} else {
			// update by zhaofeng ͨ����ҵ��־(0Ϊ��Ч��1Ϊ��Ч��2Ϊ�����ѵ���)�ж���ҵ������Ч����
			ent = (TEnt) entInfoList.get(0);
			if (ent.getEntStatus().equals("2")) {
				String logIndesc = "��ҵ��" + loginForm.getEmpCode() + "�����ѵ��ڣ�";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);
				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,��ҵ�����ѵ���"));
				if (loginForm.getEdition().equals("en")) {
					request.setAttribute("error", logIndesc);
				} else {
					request.setAttribute("error", logIndesc);
				}

				return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN + "_"
						+ loginForm.getEdition() + cooperation);

			}/*
				 * else if(ent.getEntStatus().equals("2")) { String logIndesc =
				 * "��ҵ���룺" + loginForm.getEmpCode() + "�����ڣ�";
				 * doLoginLog(tLoginLog, userInfo, logIndesc, 0);
				 * SysLogger.sysLogger.info(getLogInfo(userInfo, "I000:
				 * ��¼����-��¼ʧ��,��ҵ���벻����"));
				 * if(loginForm.getEdition().equals("en")){
				 * request.setAttribute("error", "Enterprise Code not exist��");
				 * }else{ request.setAttribute("error", logIndesc); } return
				 * mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN+"_"+loginForm.getEdition()+cooperation); }
				 */

		}

		/*
		 * Configure configure = configService.getById(1L); Long isClose =
		 * configure.getParaClose(); //ϵͳ�Ƿ�ر� Long isParaLogin =
		 * configure.getParaLogin();//�Ƿ����ͬһ��ip�ദ��¼
		 */

		String findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
				+ loginForm.getEdition() + cooperation;

		String logIndesc = null;

		List listUser = null;
		boolean isDynamicLogin = false;
		boolean isLoginSuccess = false;
		if (loginForm.getLoginType() != null
				&& loginForm.getLoginType().equals("2")) {// ��̬�����½
			isDynamicLogin = true;
		}

		if (isDynamicLogin) {
			tempShortMessage = shortMessageService.findByPhone(loginForm
					.getEmpCode(), loginForm.getUserAccount(),
					shortMessageService.LOGIN_TYPE);

			if (tempShortMessage == null) {
				logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + " �û�: "
						+ loginForm.getUserAccount() + " �������";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,�������"));
				request.setAttribute("error", "���ȡ��̬����");
			} else if (!tempShortMessage.getDynamicPassword().equalsIgnoreCase(
					loginForm.getPassword())) {

				logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + " �û�: "
						+ loginForm.getUserAccount() + " �������";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,�������"));
				request.setAttribute("error", "��¼ʧ��,�������");

			} else if (System.currentTimeMillis()
					- tempShortMessage.getCreateTime() > Constants.MESSAGE_OUT_TIME_VALUE * 1000) {
				// ��¼��־��¼

				logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + " �û�: "
						+ loginForm.getUserAccount() + " ��ʱ���볬��"
						+ Constants.MESSAGE_OUT_TIME_VALUE + "��";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,��ʱ���볬��"
								+ Constants.MESSAGE_OUT_TIME_VALUE + "��"));
				request.setAttribute("error", "��¼ʧ��,��ʱ���볬��");
			} else if (!tempShortMessage.getPhoneNumber().equalsIgnoreCase(
					loginForm.getUserAccount())) {
				logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + " �û�: "
						+ loginForm.getUserAccount() + " �û�������";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,�û�������"));
				request.setAttribute("error", "��¼ʧ��,�û�������");

			} else {
				listUser = userService.hasAccountByPhoneNum(loginForm
						.getUserAccount(), loginForm.getEmpCode());
				if (listUser == null || listUser.size() == 0) {
					logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + " �û�: "
							+ loginForm.getUserAccount() + " �û�������";
					doLoginLog(tLoginLog, userInfo, logIndesc, 0);

					SysLogger.sysLogger.info(getLogInfo(userInfo,
							"I000: ��¼����-��¼ʧ��,�û�������"));
					request.setAttribute("error", "��¼ʧ��,�û�������");
				}
			}
		} else {// �̶������½
			listUser = userService.hasAccount(loginForm.getUserAccount(),
					loginForm.getEmpCode());
		}
		// �û���Ϊ��
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

				// ����Ƿ�����ͬ�����û������쳣���׳�
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
					logIndesc = "��ҵ���룺" + userInfo.getEmpCode() + " �û�: "
							+ userInfo.getUserAccount() + " ���û��Ѿ���½";
					checkFlag = false;
					result = 0;
				}
				if (checkFlag) {
					SysLogger.sysLogger.info(getLogInfo(userInfo,
							"I000: ��¼����-��¼�ɹ�"));
					logIndesc = "��ҵ���룺" + userInfo.getEmpCode() + " �û�: "
							+ userInfo.getUserAccount() + " ��¼�ɹ�";
					findForwardStr = "welcome";
					Constants.registerKeys("userInfo");

					// add by liuhongxiao 2012-01-30 �������½��Ϣ�ɼ�ϵͳ
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
					isLoginSuccess = true;// ��½�ɹ�
					result = 1;
				}

				doLoginLog(tLoginLog, userInfo, logIndesc, result);

			} else {// �������
				this.clearSession(session);

				// ��¼��־��¼
				logIndesc = "��ҵ���룺" + loginForm.getEmpCode() + " �û�: "
						+ loginForm.getUserAccount() + " ��¼ʧ��,��¼���벻��ȷ";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,�������"));
				if (loginForm.getEdition().equals("en")) {
					request
							.setAttribute("error",
									"Login Failed,Password Error");
				} else {
					request.setAttribute("error", "��¼ʧ��,�������");
				}
				findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
						+ loginForm.getEdition() + cooperation;
			}

		} else if (isDynamicLogin) {// �û�������,��̬��½
			findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation;
		} else {// �û������ڣ���̬��½
			// ��¼��־��¼

			logIndesc = "��¼ʧ�ܣ��û�: " + loginForm.getUserAccount() + "�����ڣ�";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);

			SysLogger.sysLogger.info(getLogInfo(userInfo, "I000: ��¼����-��¼ʧ��,"
					+ loginForm.getUserAccount() + "�û�������"));
			if (loginForm.getEdition().equals("en")) {
				request.setAttribute("error", "Login Failed,User not exist��");
			} else {
				request.setAttribute("error", logIndesc);
			}

			findForwardStr = Constants.GLOBAL_FORWARD_LOGIN + "_"
					+ loginForm.getEdition() + cooperation;
		}

		try {
			LogFactory.newLogInstance("loginLogger").info(tLoginLog);
		} catch (Exception ex) {

			SysLogger.sysLogger.error("��ҵ���룺" + loginForm.getEmpCode()
					+ " �û�: " + loginForm.getUserAccount() + "��¼ʧ�ܣ�д����־����", ex);
			request.setAttribute("error", "��¼ʧ�ܣ�д����־����");
			return mapping.findForward("error");
		}

		if (isDynamicLogin && isLoginSuccess) {// ��½�����Ҫɾ����½����
			shortMessageService.deleteTempDynamicPassword(loginForm
					.getEmpCode(), loginForm.getUserAccount(),
					shortMessageService.LOGIN_TYPE);
		}
		
		// ���������ļ� add by weimeng 2012-09-17
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
		// ��¼��־��¼
		tLoginLog.setUserId(userInfo.getUserId());
		tLoginLog.setUserName(userInfo.getUserAccount());
		tLoginLog.setAccessIp(userInfo.getIp());
		tLoginLog.setEmpCode(userInfo.getEmpCode());
		tLoginLog.setResult(new Long(result));
		tLoginLog.setLoginDesc(loginDesc);
	}

	private String getLogInfo(UserInfo userInfo, String loginDesc) {
		String logInfo = loginDesc + "-��ҵ���룺" + userInfo.getEmpCode()
				+ " -��¼�ʺţ�" + userInfo.getUserAccount() + " ��¼ʱ�䣺"
				+ DateUtil.getDateTime() + " ��¼��IP��" + userInfo.getIp();
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
