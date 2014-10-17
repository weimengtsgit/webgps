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
public class LoginAloneAction extends Action {
	private final String key = "tstx!@#$%^";
	private TargetService targetService =(TargetService)SpringHelper.getBean("targetService");

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ����
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
		
		// add by liuhongxiao 2012-01-30 ����û��ظ���¼,ֱ����ת����ҳ
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

		// ���������ļ� add by weimeng 2012-09-17
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

		TLoginLog tLoginLog = new TLoginLog(); // ��¼��־bean
		// ���session
		this.clearSession(session);
		TUser user = new TUser();
		TEnt tent = new TEnt();// ��ҵ��Ϣ
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
		
		// ����cookie
		CookieHelper.setValue(request, response, 3600 * 24 * 31,
				"login_entCode", empCode);
		CookieHelper.setValue(request, response, 3600 * 24 * 31,
				"login_userName", userAccount);

		// ȡ����ҵ��Ϣ,�ж���ҵ�Ƿ���ڣ�
		TEnt ent = null;
		List entInfoList = userService.queryEntInfo(empCode);
		if (entInfoList == null || entInfoList.size() <= 0) {
			String logIndesc = "��ҵ���룺" + empCode + "�����ڣ�";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);
			SysLogger.sysLogger.info(getLogInfo(userInfo,
					"I000: ��¼����-��¼ʧ��,��ҵ���벻����"));
			request.setAttribute("error", logIndesc);
			return mapping.findForward(Constants.GLOBAL_FORWARD_LOGIN);
			// request.setAttribute("error", logIndesc);
			// return mapping.findForward("error");
		} else {
			ent = (TEnt) entInfoList.get(0);
		}

		/*
		 * Configure configure = configService.getById(1L); Long isClose =
		 * configure.getParaClose(); //ϵͳ�Ƿ�ر� Long isParaLogin =
		 * configure.getParaLogin();//�Ƿ����ͬһ��ip�ദ��¼
		 */

		String findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;

		String logIndesc = null;

		List listUser = null;

		listUser = userService.hasAccount(userAccount, empCode);
		// �û���Ϊ��
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
					findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;
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

					session.setAttribute("userInfo", userInfo);
					result = 1;
				}

				doLoginLog(tLoginLog, userInfo, logIndesc, result);

			} else {// �������
				this.clearSession(session);

				// ��¼��־��¼
				logIndesc = "��ҵ���룺" + empCode + " �û�: " + userAccount
						+ " ��¼ʧ��,��¼���벻��ȷ";
				doLoginLog(tLoginLog, userInfo, logIndesc, 0);

				SysLogger.sysLogger.info(getLogInfo(userInfo,
						"I000: ��¼����-��¼ʧ��,�������"));
				request.setAttribute("error", "��¼ʧ��,�������");
				findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;
			}
		} else {// �û������ڣ���̬��½
			// ��¼��־��¼

			logIndesc = "��¼ʧ�ܣ��û�: " + userAccount + "�����ڣ�";
			doLoginLog(tLoginLog, userInfo, logIndesc, 0);

			SysLogger.sysLogger.info(getLogInfo(userInfo, "I000: ��¼����-��¼ʧ��,"
					+ userAccount + "�û�������"));

			request.setAttribute("error", logIndesc);

			findForwardStr = Constants.GLOBAL_FORWARD_LOGIN;
		}

		try {
			LogFactory.newLogInstance("loginLogger").info(tLoginLog);
		} catch (Exception ex) {

			SysLogger.sysLogger.error("��ҵ���룺" + empCode + " �û�: " + userAccount
					+ "��¼ʧ�ܣ�д����־����", ex);
			request.setAttribute("error", "��¼ʧ�ܣ�д����־����");
			return mapping.findForward("error");
		}

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
