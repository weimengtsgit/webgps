package com.sosgps.wzt.manage.terminal.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminal.dao.TerminalManageDao;
import com.sosgps.wzt.manage.terminal.service.TerminalManageService;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

/**
 * @Title:�ն˹���action��
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����01:35:23
 */
public class TerminalManageAction extends DispatchWebActionSupport {

	/**
	 * �ն����²����ն��б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void findTermByGroupId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String groupId = request.getParameter("groupId");
		String action = request.getParameter("action");// �¼�
		String isOndblclick = request.getParameter("isOndblclick");// �Ƿ�Ϊ���˫���¼�
		Long termGroupId = 0L;
		try {
			termGroupId = Long.parseLong(groupId);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + " termId="
					+ termGroupId, e);
		}
		TerminalManageService terminalManageService = (TerminalManageService) SpringHelper
				.getBean("TerminalManageServiceImpl");
		String re = terminalManageService.findByTermGroupId(termGroupId,
				action, isOndblclick);
		SysLogger.sysLogger.debug(LogConstants.LOG_SOURCE_MANAGE_TERMINAL + " "
				+ Constants.FIND_TERMINAL_FROM_GROUP + " response=" + re);
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.getWriter().write(re);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + " "
					+ Constants.ERROR_RESPONSE_WRITE, e);
		}
		// return null;
	}

	/**
	 * �ն����²��Ҹ����ն��б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void findSpecialTermByGroupId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String groupId = request.getParameter("groupId");
		String type = request.getParameter("type");// �ն�����
		String action = request.getParameter("action");// �¼�
		String isOndblclick = request.getParameter("isOndblclick");// �Ƿ�Ϊ���˫���¼�
		Long termGroupId = 0L;
		try {
			termGroupId = Long.parseLong(groupId);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + " termId="
					+ termGroupId, e);
		}
		TerminalManageService terminalManageService = (TerminalManageService) SpringHelper
				.getBean("TerminalManageServiceImpl");
		String re = terminalManageService.findSpecialByTermGroupId(termGroupId,
				type, action, isOndblclick);
		SysLogger.sysLogger.debug(LogConstants.LOG_SOURCE_MANAGE_TERMINAL + " "
				+ Constants.FIND_TERMINAL_FROM_GROUP + " response=" + re);
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.getWriter().write(re);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + " "
					+ Constants.ERROR_RESPONSE_WRITE, e);
		}
		// return null;
	}

	/**
	 * �����ն�������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void terminalInGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// ��ҵ����
		if (entCode == null || entCode.equals("")) {
			return;
		}
		String deviceIdss = request.getParameter("deviceIds");// �ն��������飬��,����
		String groupId = request.getParameter("groupId");// �ն���id
		TerminalManageService terminalManageService = (TerminalManageService) SpringHelper
				.getBean("TerminalManageServiceImpl");
		if (deviceIdss != null) {
			String[] deviceIds = CharTools.split(deviceIdss, ",");
			boolean re = terminalManageService.updateRefTermGroup(deviceIds,
					groupId);
			// д�����ݿ������־
			TOptLog tOptLog = new TOptLog();
			// ��ȡ��¼�û���Ϣ
			UserInfo userInfo = this.getCurrentUser(request);
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
			tOptLog.setFunCType(LogConstants.EDIT_TERMINAL_IN_GROUP);
			tOptLog.setOptDesc(Constants.EDIT_TERMINAL_IN_GROUP);
			if (re) {
				tOptLog.setResult(1L);
			} else {
				tOptLog.setResult(0L);
			}
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				SysLogger.sysLogger.error(Constants.ERROR_OPT_LOG_SAVE, e);
				request.setAttribute("msg", "���������־ʧ��");
			}
			response.setContentType("text/xml;charset=utf-8");
			try {
				if(re)
					response.getWriter().write("{result:\"1\"}");//�ɹ�
				else
					response.getWriter().write("{result:\"2\"}");//ʧ��
//				response.getWriter().write("<r>" + re + "</r>");
			} catch (Exception e) {
				SysLogger.sysLogger.error(
						LogConstants.LOG_SOURCE_MANAGE_TERMINAL + " "
								+ Constants.EDIT_TERMINAL_IN_GROUP + " "
								+ Constants.ERROR_RESPONSE_WRITE, e);
			}
		}

	}

	/**
	 * ����������ȡ��entCode
	 * 
	 * @param request
	 * @return
	 */
	public String help(HttpServletRequest request) {
		String entCode = null;// ��ҵ����
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj != null && obj instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) obj;
			entCode = userInfo.getEmpCode();
			// ��������Ա
			if (entCode.equalsIgnoreCase("emproot")) {
				// ��request�л�ò���
				// entCode = request.getParameter("entCode");// ��ҵ����
			}
		}
		return entCode;
	}
}
