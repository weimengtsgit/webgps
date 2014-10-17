package com.sosgps.wzt.system.action;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.exception.WZTException;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefUserRole;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.orm.TempShortMessage;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.UserForm;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.system.service.ShortMessageService;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.util.CharTools;

/**
 * @Title:�û�����
 * @Description:�û������漰����ɾ���ġ��鹦�ܲ���
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: zhangwei
 * @version 1.0
 * @date: 2008-8-23 ����02:41:11
 */
public class UserAction extends DispatchWebActionSupport {
	private UserService userService = (UserService) SpringHelper
			.getBean("userService");
	private RoleService roleService = (RoleService) SpringHelper
			.getBean("roleService");

	public static Logger log = Logger.getLogger(UserAction.class);

	// TOptLog tOptLog = new TOptLog();

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String goPage = "init";

		return mapping.findForward(goPage);

	}

	public ActionForward initModifyPassword(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String goPage = "initModifyPassword";
		return mapping.findForward(goPage);

	}

	public ActionForward modifyPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isSucceed = false;
		UserForm userForm = (UserForm) form;
		TUser tUser = userForm.getUser();
		UserInfo userInfo = this.getCurrentUser(request);
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

		TUser currentUser = userInfo.getUser();

		ShortMessageService shortMessageService = (ShortMessageService) SpringHelper
				.getBean("shortMessageService");
		TempShortMessage tempShortMessage = shortMessageService.findByPhone(
				userInfo.getEmpCode(), currentUser.getUserContact(),
				shortMessageService.MODIFY_PASSWORD_TYPE);

		if (tempShortMessage == null
				|| !tempShortMessage.getDynamicPassword().equalsIgnoreCase(
						userForm.getVeriMessage())) {
			tOptLog.setOptDesc("�޸� '" + userForm.getUser().getUserAccount()
					+ "' �û�����,��֤�����");
			request.setAttribute("error", "��֤�����");
			tOptLog.setResult(new Long(0));
		} else if ((System.currentTimeMillis() - tempShortMessage
				.getCreateTime()) > Constants.MESSAGE_OUT_TIME_VALUE * 1000) {
			tOptLog.setOptDesc("�޸� '" + userForm.getUser().getUserAccount()
					+ "' �û�����,��֤�볬ʱ�����������" + Constants.MESSAGE_OUT_TIME_VALUE
					+ "�����޸ģ�");
			request.setAttribute("error", "��֤�����");
			tOptLog.setResult(new Long(0));
		} else if (userForm.getOldPassword().equals(
				currentUser.getUserPassword())) {
			currentUser.setUserPassword(tUser.getUserPassword());
			userService.updateUser(currentUser, null, null);
			tOptLog.setOptDesc("�޸� '" + userForm.getUser().getUserAccount()
					+ "' �û�����,�޸ĳɹ���");
			request.setAttribute("success", "�޸ĳɹ�");
			tOptLog.setResult(new Long(1));
			isSucceed = true;
		} else {
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("�޸� '" + userForm.getUser().getUserAccount()
					+ "' �û�����,ԭʼ�������");
			request.setAttribute("error", "ԭʼ�������");
		}

		try {
			LogFactory.newLogInstance("optLogger").info(tOptLog);

		} catch (Exception ex) {
			tOptLog.setOptDesc("�޸� '" + userForm.getUser().getUserAccount()
					+ "' �û�����ʧ�ܣ�");
			tOptLog.setResult(new Long(0));

			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				request.setAttribute("error", "���������־ʧ��");
			}
			request.setAttribute("error", "���������־ʧ��");
			isSucceed = false;
		}

		if (isSucceed) {
			shortMessageService.deleteTempDynamicPassword(
					userInfo.getEmpCode(), currentUser.getUserContact(),
					shortMessageService.MODIFY_PASSWORD_TYPE);
		}

		return mapping.findForward("initModifyPassword");

	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userForm = (UserForm) form;
		TUser tUser = userForm.getUser();
		TOptLog tOptLog = new TOptLog();
		UserInfo userInfo = this.getCurrentUser(request);
		// ��ȡ��¼�û���Ϣ
		String createBy = "initName";

		// �ж��Ƿ��Ѿ������û�
		boolean hasUser = false;
		List list = userService.hasAccount(userForm.getUser().getUserAccount());

		if (list != null && list.size() > 0) {
			hasUser = true;
		}
		// �û����� ��ʾ�û��ʺŴ���
		if (hasUser) {
			// ��¼��־
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptDesc("���� '" + userForm.getUser().getUserAccount()
					+ "' �û��ʺ��Ѵ��ڣ�");
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			request.setAttribute("msg", "�û��ʺ��Ѵ��ڣ�");
			return mapping.findForward("message");

			// ���򱣴��û���Ϣ����ɫ��ϵ
		} else {
			TUser user = userForm.getUser();
			roleService = (RoleService) SpringHelper.getBean("roleService");
			String roleId = userForm.getRoleId();
			String empCode = userForm.getEmpCode();
			String isAreaAlarm = userForm.getUser().getIsAreaAlarm();
			TRole role = roleService.retrieveRole(new Long(roleId));

			user.setUsageFlag("1");
			// ��ȡ��¼�û���Ϣ
			if (userInfo != null) {
				createBy = userInfo.getUserAccount();
			}
			user.setCreateBy(createBy);// ��¼ user ��Ϣ
			// user.setCreateBy("zw");
			user.setCreateDate(DateUtil.getDateTime());
			user.setRole(role);
			user.setUsageFlag("1");
			user.setEmpCode(empCode);// ������˾��ҵ����

			if (isAreaAlarm == null) {
				user.setIsAreaAlarm("0");
			}

			// ��־��¼
			tOptLog.setEmpCode(user.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

			try {
				userService.saveUser(user, role);
				tOptLog.setOptDesc("���� " + userForm.getUser().getUserAccount()
						+ " �û��ʺųɹ���");
				tOptLog.setResult(new Long(1));
				LogFactory.newLogInstance("optLogger").info(tOptLog);

			} catch (Exception ex) {
				tOptLog.setOptDesc("���� '" + userForm.getUser().getUserAccount()
						+ "' �û��ʺ�ʧ�ܣ�");
				tOptLog.setResult(new Long(0));

				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					request.setAttribute("msg", "���������־ʧ��");
					return mapping.findForward("message");
				}
				request.setAttribute("msg", "�����û�ʧ��");
				return mapping.findForward("message");
			}

			return mapping.findForward("createSuccess");
		}
	}

	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userForm = (UserForm) form;
		java.lang.Long id = userForm.getId();
		java.lang.String empCode = userForm.getEmpCode();
		TUser user = userService.retrieveUser(id);
		// List roleList = roleService.getAllRole();
		List roleList = this.roleService.getRoleByEmpCode(empCode);
		Long roleId = user.getRole().getId();
		userForm.setRoleList(roleList);
		userForm.setRoleId(roleId.toString());
		userForm.setUser(user);
		return mapping.findForward("modify");

	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userForm = (UserForm) form;

		String roleId = userForm.getRoleId();
		Long id = userForm.getId();
		TRole role = roleService.retrieveRole(new Long(roleId));
		TOptLog tOptLog = new TOptLog();

		// ��ȡδ����ǰ�û���Ϣ
		TUser _user = this.userService.retrieveUser(id);
		String userAccout = _user.getUserAccount();
		Iterator iterator = _user.getRefUserRoles().iterator();
		RefUserRole refUserRole = null;
		while (iterator.hasNext()) {
			refUserRole = (RefUserRole) iterator.next();
		}

		_user.setUserAccount(userForm.getUser().getUserAccount());
		_user.setUserPassword(userForm.getUser().getUserPassword());
		_user.setUserContact(userForm.getUser().getUserContact());
		_user.setLimitedIp(userForm.getUser().getLimitedIp());
		_user.setRole(role);
		_user.setIsAreaAlarm(userForm.getUser().getIsAreaAlarm() == null ? "0"
				: userForm.getUser().getIsAreaAlarm());
		userForm.setRole(role);
		userForm.setUser(_user);

		// ��ȡ��¼�û���Ϣ
		String createBy = "initName";
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}

		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

		try {
			userService.updateUser(_user, role, refUserRole);
			tOptLog.setOptDesc("������ '" + userAccout + "' ����Ϊ '"
					+ userForm.getUser().getUserAccount() + "' �ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			tOptLog.setOptDesc("������ '" + userAccout + "' ����Ϊ '"
					+ userForm.getUser().getUserAccount() + "' ʧ��");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "�����û�ʧ��");
			return mapping.findForward("message");
		}

		return mapping.findForward("createSuccess");
	}

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userForm = (UserForm) form;
		java.lang.Long userId = userForm.getId();
		TUser user = userService.retrieveUser(userId);
		userForm.setEmpCode(user.getEmpCode());
		userForm.setUser(user);
		return mapping.findForward("view");
	}

	/**
	 * ɾ���û�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] sids = request.getParameterValues("ids");
		Long[] longIds = new Long[sids.length];
		TOptLog tOptLog = new TOptLog();
		for (int x = 0; x < sids.length; x++) {
			longIds[x] = Long.valueOf(sids[x]);
		}
		// ��ȡ��¼�û���Ϣ
		String createBy = "initName";
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
		boolean ret = userService.deleteAll(longIds);
		// ɾ���ɹ�
		if (ret) {
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc("ɾ���û��ɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return mapping.findForward("displayList");
			// ɾ��ʧ��
		} else {
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("ɾ���û�ʧ��");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "ɾ���û�ʧ��");
			return mapping.findForward("message");
		}
	}

	public ActionForward changePWD(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// String m="changesuccess";
		// log.info("--enter changePWD");
		// try{
		// UserForm userForm = (UserForm)form;
		// String newpassword=request.getParameter("newPassword");
		// String oldpassword=userForm.getUser().getUserPassword();
		// String account=userForm.getUser().getUserAccount();
		// UserService userService = new UserService();
		// DimUser user = userService.retrieveUserByAccount(account);
		// log.info("--new password="+newpassword);
		// if(user.getUserPassword().trim().equals(oldpassword)){
		// user.setUserPassword(newpassword);
		// }
		// else m="nosuchuser";
		// userForm.setUser(user);
		// userService.updateUser(user);
		// }catch(Exception e){
		// m="unknown";
		// //throw e;
		// }
		// log.info("--exit changePWD m="+m);
		return mapping.findForward("");
	}

	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("displayList");
	}

	// sos�û��б�
	public ActionForward listUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		UserInfo user = this.getCurrentUser(request);

		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			/*
			 * StringBuffer jsonSb = new StringBuffer(); String json =
			 * "{total:20,data:["; json +="{DeviceId:'��1"+start+
			 * "',ParentCard:'13911111112',CrteDate:'13911111111',OperState:'��1'}"
			 * ; json += "]}";
			 * response.setContentType("text/json; charset=utf-8");
			 * response.getWriter().write(json);
			 */

			// service
			Page<TUser> userList = userService.listUserAdmin(user.getEmpCode(),
					page, limitint, searchValue);
			StringBuffer jsonSb = new StringBuffer();
			String total = "";
			if (userList != null && userList.getResult() != null) {
				total = "{total:" + userList.getTotalCount() + ",data:[";
				// jsonSb.append("{total:"+gridList.getCountTotal()+",data:[");
				Iterator i = userList.getResult().iterator();
				while (i.hasNext()) {
					TUser userObj = (TUser) i.next();
					jsonSb.append("{");
					jsonSb.append("id:'" + userObj.getId() + "',");// id
					jsonSb.append("userName:'"
							+ CharTools.javaScriptEscape(userObj.getUserName())
							+ "',");// ��������
					jsonSb.append("userAccount:'"
							+ CharTools
									.javaScriptEscape(userObj.getUserAccount())
							+ "',");// ��¼�ʺ�
					jsonSb.append("province:'"
							+ CharTools.javaScriptEscape(userObj.getProvince())
							+ "',");// ʡ
					jsonSb.append("city:'"
							+ CharTools.javaScriptEscape(userObj.getCity())
							+ "',");// ��
					jsonSb.append("createDate:'"
							+ CharTools.javaScriptEscape(userObj.getCreateDate())
							+ "',");// ����ʱ��
					jsonSb.append("password:'"
							+ CharTools.javaScriptEscape(userObj
									.getUserPassword()) + "',");// 
					jsonSb.append("controlPassword:'"
							+ CharTools.javaScriptEscape(userObj
									.getControlPassword()) + "'");// 
					
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
				jsonSb.append("]}");
				// System.out.println(jsonSb.toString());
			}
			System.out.println(total + jsonSb.toString());
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(total + jsonSb.toString());

		}

		return mapping.findForward(null);
	}

	/**
	 * sos �û��б�,���˵�¼�û����û��б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listUserWithOutLoginUser(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		UserInfo user = this.getCurrentUser(request);

		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			/*
			 * StringBuffer jsonSb = new StringBuffer(); String json =
			 * "{total:20,data:["; json +="{DeviceId:'��1"+start+
			 * "',ParentCard:'13911111112',CrteDate:'13911111111',OperState:'��1'}"
			 * ; json += "]}";
			 * response.setContentType("text/json; charset=utf-8");
			 * response.getWriter().write(json);
			 */

			// service
			Page<TUser> userList = userService.listUser(user.getEmpCode(),
					page, limitint, searchValue);
			StringBuffer jsonSb = new StringBuffer();
			String total = "";
			if (userList != null && userList.getResult() != null) {
				total = "{total:" + userList.getTotalCount() + ",data:[";
				// jsonSb.append("{total:"+gridList.getCountTotal()+",data:[");
				Iterator i = userList.getResult().iterator();
				while (i.hasNext()) {
					TUser userObj = (TUser) i.next();

					// System.out.println((userObj.getId()+"") +
					// (user.getUserId()+"")+(userObj.getId() ==
					// user.getUserId()));
					if ((userObj.getId() + "").equals(user.getUserId() + "")) {
						continue;
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + userObj.getId() + "',");// id
					jsonSb.append("userName:'"
							+ CharTools.javaScriptEscape(userObj.getUserName())
							+ "',");// ��������
					jsonSb.append("userAccount:'"
							+ CharTools
									.javaScriptEscape(userObj.getUserAccount())
							+ "',");// ��¼�ʺ�
					jsonSb.append("province:'"
							+ CharTools.javaScriptEscape(userObj.getProvince())
							+ "',");// ʡ
					jsonSb.append("city:'"
							+ CharTools.javaScriptEscape(userObj.getCity())
							+ "',");// ��
					jsonSb.append("createDate:'"
							+ CharTools.javaScriptEscape(userObj.getCreateDate())
							+ "',");// ����ʱ��
					jsonSb.append("password:'"
							+ CharTools.javaScriptEscape(userObj
									.getUserPassword()) + "'");// ����ʱ��

					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
				jsonSb.append("]}");
				// System.out.println(jsonSb.toString());
			}
			System.out.println(total + jsonSb.toString());
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(total + jsonSb.toString());

		}

		return mapping.findForward(null);
	}

	// sosͨ��id���û���Ϣ
	public ActionForward queryUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{}");
			return mapping.findForward(null);
		}
		// ��request�л�ȡ����
		String userId = request.getParameter("userId");// �û�id

		//��ѯ��ɫ
		List roles = userService.queryUserRolesById(userId);
		if (roles == null || roles.size() == 0) {
			response.getWriter().write("{}");
			return mapping.findForward(null);
		}
		//��ѯ�ɼ���
		List groups = userService.queryUserTgroupsById(userId);
		
		StringBuffer jsonSb = new StringBuffer();
		
		Long roleId = 0L;
		String rolename = "";
		TRole role = (TRole) roles.get(0);
		roleId = role.getId();
		rolename = role.getRoleName();
		
		String groupIds = "";
		Iterator i = groups.iterator();
		while (i.hasNext()) {
			TTermGroup group = (TTermGroup) i.next();
			groupIds += group.getId() + ",";
		}
		if (groupIds.length() > 0) {
			groupIds = groupIds.substring(0, groupIds.length() - 1);
		}
		
		jsonSb.append("{");
		jsonSb.append("roleId:'" + roleId + ',' + rolename + "',");// role
		// id
		//System.out.println("groupIds:"+groupIds);
		if(groupIds.length() > 0){
			jsonSb.append("groupIds:'," + groupIds + ",'");// group ids
		}else{
			jsonSb.append("groupIds:''");// group ids
		}
		
		jsonSb.append("}");
		//System.out.println("queryUser json:"+jsonSb.toString());
		response.getWriter().write(jsonSb.toString());
		return mapping.findForward(null);
	}

	// sos�����û�
	public ActionForward addUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ȡ����
		String userName = request.getParameter("userName");// �û���
		String userAccount = request.getParameter("userAccount");// ��¼�ʺ�
		String password = request.getParameter("password");// ����
		String province = request.getParameter("province");// ʡ
		String city = request.getParameter("city");// ��
		String groupIds = request.getParameter("groupIds");// ������id�������","����
		String roleId = request.getParameter("roleId");// ��ɫid
		String controlPassword = request.getParameter("controlPassword");// ���Ͷϵ�����

		userName = CharTools.killNullString(userName);
		userName = java.net.URLDecoder.decode(userName, "utf-8");
		userName = CharTools.killNullString(userName);
		userAccount = CharTools.killNullString(userAccount);
		userAccount = java.net.URLDecoder.decode(userAccount, "utf-8");
		userAccount = CharTools.killNullString(userAccount);
		password = CharTools.killNullString(password);
		password = java.net.URLDecoder.decode(password, "utf-8");
		password = CharTools.killNullString(password);
		province = CharTools.killNullString(province);
		province = java.net.URLDecoder.decode(province, "utf-8");
		province = CharTools.killNullString(province);
		
		city = CharTools.killNullString(city);
		city = java.net.URLDecoder.decode(city, "utf-8");
		city = CharTools.killNullString(city);
		groupIds = CharTools.killNullString(groupIds);
		Long role_id = Long.parseLong(roleId);
		controlPassword = CharTools.killNullString(controlPassword);
		controlPassword = java.net.URLDecoder.decode(controlPassword, "utf-8");
		controlPassword = CharTools.killNullString(controlPassword);
		//System.out.println("userName:"+userName+"userAccount:"+userAccount+"password:"+password+"province:"+province
			//	+"city:"+city+"groupIds:"+groupIds+"roleId:"+roleId);
		
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();// ��ҵ����

		// �ж��Ƿ��Ѿ������û�
		//boolean hasUser = false;
		List list = userService.hasAccount(userAccount,entCode);
		if (list != null && list.size() > 0) {
			//hasUser = true;
			response.getWriter().write("{result:\"3\"}");// �Ѵ��ڴ��û��ʺ�
			return mapping.findForward(null);
		}
		
		List listByUserName = userService.hasUserName(userName,entCode);
		if (listByUserName != null && listByUserName.size() > 0) {
			//hasUser = true;
			response.getWriter().write("{result:\"4\"}");// �Ѵ��ڴ��û���
			return mapping.findForward(null);
		}
			TRole role = roleService.retrieveRole(role_id);
			TUser user = new TUser();
			user.setUserAccount(userAccount);
			user.setUserPassword(password);
			user.setUserName(userName);
			user.setProvince(province);
			user.setCity(city);
			user.setUsageFlag("1");
			// ��ȡ��¼�û���Ϣ
			String createBy = "initName";
			if (userInfo != null) {
				createBy = userInfo.getUserAccount();
			}
			user.setCreateBy(createBy);// ��¼ user ��Ϣ
			user.setCreateDate(DateUtil.getDateTime());
			user.setRole(role);
			user.setControlPassword(controlPassword);
			user.setEmpCode(entCode);// ������˾��ҵ����

			// ��־��¼
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

			try {
				// �����û�
				userService.saveUser(user, role);
				// ���ù�����
				if (groupIds != null && groupIds.length() > 0) {
					TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
							.getBean("TerminalGroupManageServiceImpl");
					boolean re = terminalGroupManageService.userTermGroupSet(
							entCode, user.getId(), groupIds);
				}
				tOptLog.setOptDesc("���� " + userAccount + " �û��ʺųɹ���");
				tOptLog.setResult(new Long(1));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"1\"}");// �ɹ�
			} catch (Exception ex) {
				tOptLog.setOptDesc("���� " + userAccount + " �û��ʺ�ʧ�ܣ�");
				tOptLog.setResult(new Long(0));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"2\"}");// ʧ��
			}
		
		return mapping.findForward(null);
	}

	// sos�޸��û�
	public ActionForward updateUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ȡ����
		String userId = request.getParameter("userId");// �û�id
		String userName = request.getParameter("userName");// �û���
		String userAccount = request.getParameter("userAccount");// ��¼�ʺ�
		String password = request.getParameter("password");// ����
		String province = request.getParameter("province");// ʡ
		String city = request.getParameter("city");// ��
		String groupIds = request.getParameter("groupIds");// ������id�������","����
		String roleId = request.getParameter("roleId");// ��ɫid
		String controlPassword = request.getParameter("controlPassword");// ���Ͷϵ�����

		userName = CharTools.killNullString(userName);
		userAccount = CharTools.killNullString(userAccount);
		password = CharTools.killNullString(password);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		controlPassword = CharTools.killNullString(controlPassword);
		
		userName = java.net.URLDecoder.decode(userName, "utf-8");
		userAccount = java.net.URLDecoder.decode(userAccount, "utf-8");
		password = java.net.URLDecoder.decode(password, "utf-8");
		province = java.net.URLDecoder.decode(province, "utf-8");
		city = java.net.URLDecoder.decode(city, "utf-8");
		controlPassword = java.net.URLDecoder.decode(controlPassword, "utf-8"); 
		
		userName = CharTools.killNullString(userName);
		userAccount = CharTools.killNullString(userAccount);
		password = CharTools.killNullString(password);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		groupIds = CharTools.killNullString(groupIds);
		Long role_id = Long.parseLong(roleId);
		Long user_id = Long.parseLong(userId);
		controlPassword = CharTools.killNullString(controlPassword);
		
	
		
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		// ��ȡδ����ǰ�û���Ϣ
		TUser _user = this.userService.retrieveUser(user_id);
		String userAccout = _user.getUserAccount();
		String userName_ = _user.getUserName();
		
		List list = userService.hasAccount(userAccount,entCode);
		if (list != null && list.size() > 0 && !userAccout.equals(userAccount)) {
			//hasUser = true;
			response.getWriter().write("{result:\"3\"}");// �Ѵ��ڴ��û��ʺ�
			return mapping.findForward(null);
		}
		
		List listByUserName = userService.hasUserName(userName,entCode);
		if (listByUserName != null && listByUserName.size() > 0 && !userName_.equals(userName)) {
			//hasUser = true;
			response.getWriter().write("{result:\"4\"}");// �Ѵ��ڴ��û���
			return mapping.findForward(null);
		}
			Iterator iterator = _user.getRefUserRoles().iterator();
			RefUserRole refUserRole = null;
			while (iterator.hasNext()) {
				refUserRole = (RefUserRole) iterator.next();
			}
	
			TRole role = roleService.retrieveRole(role_id);
			_user.setUserName(userName);
			_user.setUserAccount(userAccount);
			_user.setUserPassword(password);
			_user.setProvince(province);
			_user.setCity(city);
			_user.setControlPassword(controlPassword);
	
			// _user.setUserContact(userForm.getUser().getUserContact());
			// _user.setLimitedIp(userForm.getUser().getLimitedIp());
			_user.setRole(role);
			// _user.setIsAreaAlarm(userForm.getUser().getIsAreaAlarm()==null?"0":userForm.getUser().getIsAreaAlarm());
	
			// ��ȡ��¼�û���Ϣ
			String createBy = "initName";
			if (userInfo != null) {
				createBy = userInfo.getUserAccount();
			}
	
			// ��־��¼
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
	
			try {
				// �޸��û�
				userService.updateUser(_user, role, refUserRole);
				// ���ù�����
				if (groupIds != null && groupIds.length() > 0) {
					TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
							.getBean("TerminalGroupManageServiceImpl");
					boolean re = terminalGroupManageService.userTermGroupSet(
							entCode, _user.getId(), groupIds);
				}
				tOptLog.setOptDesc("������ " + userAccout + " ����Ϊ " + userAccount
						+ " �ɹ�");
				tOptLog.setResult(new Long(1));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"1\"}");// �ɹ�
			} catch (Exception ex) {
				tOptLog.setOptDesc("������ " + userAccout + " ����Ϊ " + userAccount
						+ " ʧ��");
				tOptLog.setResult(new Long(0));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"2\"}");// ʧ��
			}
		
		return mapping.findForward(null);
	}

	// sosɾ���û�
	public ActionForward deleteUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ȡ����
		String userIds = request.getParameter("userIds");// �û�id�������","����

		StringTokenizer st = new StringTokenizer(userIds, ",");
		Long[] longIds = new Long[st.countTokens()];
		for (int x = 0; st.hasMoreElements(); x++) {
			longIds[x] = Long.valueOf(st.nextToken());
		}

		UserInfo userInfo = this.getCurrentUser(request);
		// ��ȡ��¼�û���Ϣ
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}

		// ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

		try {
			// ɾ���û��������ϵ
			TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
					.getBean("TerminalGroupManageServiceImpl");
			for (int i = 0; i < longIds.length; i++) {
				terminalGroupManageService.deleteUserTermGroup(longIds[i]);
			}
			// ɾ���û�
			userService.deleteUsers(longIds);
			tOptLog.setOptDesc("ɾ���û��ɹ�");
			tOptLog.setResult(new Long(1));
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				SysLogger.sysLogger.error("log out error", e);
			}
			response.getWriter().write("{result:\"1\"}");// �ɹ�
		} catch (Exception ex) {
			tOptLog.setOptDesc("ɾ���û�ʧ��");
			tOptLog.setResult(new Long(0));
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				SysLogger.sysLogger.error("log out error", e);
			}
			response.getWriter().write("{result:\"2\"}");// ʧ��
		}

		return mapping.findForward(null);
	}

	// sos�޸�����
	public ActionForward updatePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String userAccount = userInfo.getUserAccount();
		// ��request�л�ȡ����
		String password = request.getParameter("password");// old password
		String newPassword = request.getParameter("newPassword");// new password

		List users = userService.hasAccount(userAccount, entCode);
		if (users == null || users.size() == 0) {
			response.getWriter().write("{result:\"2\"}");// �ʺŲ�����
			return mapping.findForward(null);
		}
		TUser user = (TUser) users.get(0);
		if (!user.getUserPassword().equals(password)) {
			response.getWriter().write("{result:\"3\"}");// �����벻ƥ��
			return mapping.findForward(null);
		}
		
		user.setUserPassword(newPassword);
		userService.updateUser(user, null, null);
		

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�޸� " + userAccount + " �û�����,�޸ĳɹ���");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return null;
	}
}
