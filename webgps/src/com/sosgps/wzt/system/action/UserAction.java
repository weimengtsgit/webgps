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
 * @Title:用户管理
 * @Description:用户管理，涉及增、删、改、查功能操作
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: zhangwei
 * @version 1.0
 * @date: 2008-8-23 下午02:41:11
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
			tOptLog.setOptDesc("修改 '" + userForm.getUser().getUserAccount()
					+ "' 用户密码,验证码错误！");
			request.setAttribute("error", "验证码错误");
			tOptLog.setResult(new Long(0));
		} else if ((System.currentTimeMillis() - tempShortMessage
				.getCreateTime()) > Constants.MESSAGE_OUT_TIME_VALUE * 1000) {
			tOptLog.setOptDesc("修改 '" + userForm.getUser().getUserAccount()
					+ "' 用户密码,验证码超时，请于申请后" + Constants.MESSAGE_OUT_TIME_VALUE
					+ "秒内修改！");
			request.setAttribute("error", "验证码错误");
			tOptLog.setResult(new Long(0));
		} else if (userForm.getOldPassword().equals(
				currentUser.getUserPassword())) {
			currentUser.setUserPassword(tUser.getUserPassword());
			userService.updateUser(currentUser, null, null);
			tOptLog.setOptDesc("修改 '" + userForm.getUser().getUserAccount()
					+ "' 用户密码,修改成功！");
			request.setAttribute("success", "修改成功");
			tOptLog.setResult(new Long(1));
			isSucceed = true;
		} else {
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("修改 '" + userForm.getUser().getUserAccount()
					+ "' 用户密码,原始密码错误！");
			request.setAttribute("error", "原始密码错误");
		}

		try {
			LogFactory.newLogInstance("optLogger").info(tOptLog);

		} catch (Exception ex) {
			tOptLog.setOptDesc("修改 '" + userForm.getUser().getUserAccount()
					+ "' 用户密码失败！");
			tOptLog.setResult(new Long(0));

			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				request.setAttribute("error", "保存操作日志失败");
			}
			request.setAttribute("error", "保存操作日志失败");
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
		// 获取登录用户信息
		String createBy = "initName";

		// 判断是否已经存在用户
		boolean hasUser = false;
		List list = userService.hasAccount(userForm.getUser().getUserAccount());

		if (list != null && list.size() > 0) {
			hasUser = true;
		}
		// 用户存在 提示用户帐号存在
		if (hasUser) {
			// 记录日志
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptDesc("创建 '" + userForm.getUser().getUserAccount()
					+ "' 用户帐号已存在！");
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			request.setAttribute("msg", "用户帐号已存在！");
			return mapping.findForward("message");

			// 否则保存用户信息及角色关系
		} else {
			TUser user = userForm.getUser();
			roleService = (RoleService) SpringHelper.getBean("roleService");
			String roleId = userForm.getRoleId();
			String empCode = userForm.getEmpCode();
			String isAreaAlarm = userForm.getUser().getIsAreaAlarm();
			TRole role = roleService.retrieveRole(new Long(roleId));

			user.setUsageFlag("1");
			// 获取登录用户信息
			if (userInfo != null) {
				createBy = userInfo.getUserAccount();
			}
			user.setCreateBy(createBy);// 登录 user 信息
			// user.setCreateBy("zw");
			user.setCreateDate(DateUtil.getDateTime());
			user.setRole(role);
			user.setUsageFlag("1");
			user.setEmpCode(empCode);// 所属公司企业代码

			if (isAreaAlarm == null) {
				user.setIsAreaAlarm("0");
			}

			// 日志记录
			tOptLog.setEmpCode(user.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

			try {
				userService.saveUser(user, role);
				tOptLog.setOptDesc("创建 " + userForm.getUser().getUserAccount()
						+ " 用户帐号成功！");
				tOptLog.setResult(new Long(1));
				LogFactory.newLogInstance("optLogger").info(tOptLog);

			} catch (Exception ex) {
				tOptLog.setOptDesc("创建 '" + userForm.getUser().getUserAccount()
						+ "' 用户帐号失败！");
				tOptLog.setResult(new Long(0));

				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					request.setAttribute("msg", "保存操作日志失败");
					return mapping.findForward("message");
				}
				request.setAttribute("msg", "新增用户失败");
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

		// 获取未更新前用户信息
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

		// 获取登录用户信息
		String createBy = "initName";
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}

		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

		try {
			userService.updateUser(_user, role, refUserRole);
			tOptLog.setOptDesc("将更新 '" + userAccout + "' 更新为 '"
					+ userForm.getUser().getUserAccount() + "' 成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			tOptLog.setOptDesc("将更新 '" + userAccout + "' 更新为 '"
					+ userForm.getUser().getUserAccount() + "' 失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "更新用户失败");
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
	 * 删除用户
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
		// 获取登录用户信息
		String createBy = "initName";
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
		boolean ret = userService.deleteAll(longIds);
		// 删除成功
		if (ret) {
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc("删除用户成功");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return mapping.findForward("displayList");
			// 删除失败
		} else {
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("删除用户失败");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "删除用户失败");
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

	// sos用户列表
	public ActionForward listUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 从request中获取参数
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
			 * "{total:20,data:["; json +="{DeviceId:'中1"+start+
			 * "',ParentCard:'13911111112',CrteDate:'13911111111',OperState:'中1'}"
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
							+ "',");// 主管名称
					jsonSb.append("userAccount:'"
							+ CharTools
									.javaScriptEscape(userObj.getUserAccount())
							+ "',");// 登录帐号
					jsonSb.append("province:'"
							+ CharTools.javaScriptEscape(userObj.getProvince())
							+ "',");// 省
					jsonSb.append("city:'"
							+ CharTools.javaScriptEscape(userObj.getCity())
							+ "',");// 市
					jsonSb.append("createDate:'"
							+ CharTools.javaScriptEscape(userObj.getCreateDate())
							+ "',");// 创建时间
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
	 * sos 用户列表,除了登录用户的用户列表
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
		// 从request中获取参数
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
			 * "{total:20,data:["; json +="{DeviceId:'中1"+start+
			 * "',ParentCard:'13911111112',CrteDate:'13911111111',OperState:'中1'}"
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
							+ "',");// 主管名称
					jsonSb.append("userAccount:'"
							+ CharTools
									.javaScriptEscape(userObj.getUserAccount())
							+ "',");// 登录帐号
					jsonSb.append("province:'"
							+ CharTools.javaScriptEscape(userObj.getProvince())
							+ "',");// 省
					jsonSb.append("city:'"
							+ CharTools.javaScriptEscape(userObj.getCity())
							+ "',");// 市
					jsonSb.append("createDate:'"
							+ CharTools.javaScriptEscape(userObj.getCreateDate())
							+ "',");// 创建时间
					jsonSb.append("password:'"
							+ CharTools.javaScriptEscape(userObj
									.getUserPassword()) + "'");// 创建时间

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

	// sos通过id查用户信息
	public ActionForward queryUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{}");
			return mapping.findForward(null);
		}
		// 从request中获取参数
		String userId = request.getParameter("userId");// 用户id

		//查询角色
		List roles = userService.queryUserRolesById(userId);
		if (roles == null || roles.size() == 0) {
			response.getWriter().write("{}");
			return mapping.findForward(null);
		}
		//查询可见组
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

	// sos增加用户
	public ActionForward addUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 从request中获取参数
		String userName = request.getParameter("userName");// 用户名
		String userAccount = request.getParameter("userAccount");// 登录帐号
		String password = request.getParameter("password");// 密码
		String province = request.getParameter("province");// 省
		String city = request.getParameter("city");// 市
		String groupIds = request.getParameter("groupIds");// 管理组id，多个用","隔开
		String roleId = request.getParameter("roleId");// 角色id
		String controlPassword = request.getParameter("controlPassword");// 断油断电密码

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
		String entCode = userInfo.getEmpCode();// 企业代码

		// 判断是否已经存在用户
		//boolean hasUser = false;
		List list = userService.hasAccount(userAccount,entCode);
		if (list != null && list.size() > 0) {
			//hasUser = true;
			response.getWriter().write("{result:\"3\"}");// 已存在此用户帐号
			return mapping.findForward(null);
		}
		
		List listByUserName = userService.hasUserName(userName,entCode);
		if (listByUserName != null && listByUserName.size() > 0) {
			//hasUser = true;
			response.getWriter().write("{result:\"4\"}");// 已存在此用户名
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
			// 获取登录用户信息
			String createBy = "initName";
			if (userInfo != null) {
				createBy = userInfo.getUserAccount();
			}
			user.setCreateBy(createBy);// 登录 user 信息
			user.setCreateDate(DateUtil.getDateTime());
			user.setRole(role);
			user.setControlPassword(controlPassword);
			user.setEmpCode(entCode);// 所属公司企业代码

			// 日志记录
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

			try {
				// 新增用户
				userService.saveUser(user, role);
				// 设置管理组
				if (groupIds != null && groupIds.length() > 0) {
					TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
							.getBean("TerminalGroupManageServiceImpl");
					boolean re = terminalGroupManageService.userTermGroupSet(
							entCode, user.getId(), groupIds);
				}
				tOptLog.setOptDesc("创建 " + userAccount + " 用户帐号成功！");
				tOptLog.setResult(new Long(1));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"1\"}");// 成功
			} catch (Exception ex) {
				tOptLog.setOptDesc("创建 " + userAccount + " 用户帐号失败！");
				tOptLog.setResult(new Long(0));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"2\"}");// 失败
			}
		
		return mapping.findForward(null);
	}

	// sos修改用户
	public ActionForward updateUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 从request中获取参数
		String userId = request.getParameter("userId");// 用户id
		String userName = request.getParameter("userName");// 用户名
		String userAccount = request.getParameter("userAccount");// 登录帐号
		String password = request.getParameter("password");// 密码
		String province = request.getParameter("province");// 省
		String city = request.getParameter("city");// 市
		String groupIds = request.getParameter("groupIds");// 管理组id，多个用","隔开
		String roleId = request.getParameter("roleId");// 角色id
		String controlPassword = request.getParameter("controlPassword");// 断油断电密码

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
		// 获取未更新前用户信息
		TUser _user = this.userService.retrieveUser(user_id);
		String userAccout = _user.getUserAccount();
		String userName_ = _user.getUserName();
		
		List list = userService.hasAccount(userAccount,entCode);
		if (list != null && list.size() > 0 && !userAccout.equals(userAccount)) {
			//hasUser = true;
			response.getWriter().write("{result:\"3\"}");// 已存在此用户帐号
			return mapping.findForward(null);
		}
		
		List listByUserName = userService.hasUserName(userName,entCode);
		if (listByUserName != null && listByUserName.size() > 0 && !userName_.equals(userName)) {
			//hasUser = true;
			response.getWriter().write("{result:\"4\"}");// 已存在此用户名
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
	
			// 获取登录用户信息
			String createBy = "initName";
			if (userInfo != null) {
				createBy = userInfo.getUserAccount();
			}
	
			// 日志记录
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
	
			try {
				// 修改用户
				userService.updateUser(_user, role, refUserRole);
				// 设置管理组
				if (groupIds != null && groupIds.length() > 0) {
					TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
							.getBean("TerminalGroupManageServiceImpl");
					boolean re = terminalGroupManageService.userTermGroupSet(
							entCode, _user.getId(), groupIds);
				}
				tOptLog.setOptDesc("将更新 " + userAccout + " 更新为 " + userAccount
						+ " 成功");
				tOptLog.setResult(new Long(1));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"1\"}");// 成功
			} catch (Exception ex) {
				tOptLog.setOptDesc("将更新 " + userAccout + " 更新为 " + userAccount
						+ " 失败");
				tOptLog.setResult(new Long(0));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
					SysLogger.sysLogger.error("log out error", e);
				}
				response.getWriter().write("{result:\"2\"}");// 失败
			}
		
		return mapping.findForward(null);
	}

	// sos删除用户
	public ActionForward deleteUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 从request中获取参数
		String userIds = request.getParameter("userIds");// 用户id，多个用","隔开

		StringTokenizer st = new StringTokenizer(userIds, ",");
		Long[] longIds = new Long[st.countTokens()];
		for (int x = 0; st.hasMoreElements(); x++) {
			longIds[x] = Long.valueOf(st.nextToken());
		}

		UserInfo userInfo = this.getCurrentUser(request);
		// 获取登录用户信息
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}

		// 日志记录
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);

		try {
			// 删除用户管理组关系
			TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
					.getBean("TerminalGroupManageServiceImpl");
			for (int i = 0; i < longIds.length; i++) {
				terminalGroupManageService.deleteUserTermGroup(longIds[i]);
			}
			// 删除用户
			userService.deleteUsers(longIds);
			tOptLog.setOptDesc("删除用户成功");
			tOptLog.setResult(new Long(1));
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				SysLogger.sysLogger.error("log out error", e);
			}
			response.getWriter().write("{result:\"1\"}");// 成功
		} catch (Exception ex) {
			tOptLog.setOptDesc("删除用户失败");
			tOptLog.setResult(new Long(0));
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				SysLogger.sysLogger.error("log out error", e);
			}
			response.getWriter().write("{result:\"2\"}");// 失败
		}

		return mapping.findForward(null);
	}

	// sos修改密码
	public ActionForward updatePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String userAccount = userInfo.getUserAccount();
		// 从request中获取参数
		String password = request.getParameter("password");// old password
		String newPassword = request.getParameter("newPassword");// new password

		List users = userService.hasAccount(userAccount, entCode);
		if (users == null || users.size() == 0) {
			response.getWriter().write("{result:\"2\"}");// 帐号不存在
			return mapping.findForward(null);
		}
		TUser user = (TUser) users.get(0);
		if (!user.getUserPassword().equals(password)) {
			response.getWriter().write("{result:\"3\"}");// 老密码不匹配
			return mapping.findForward(null);
		}
		
		user.setUserPassword(newPassword);
		userService.updateUser(user, null, null);
		

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_USER_CODE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("修改 " + userAccount + " 用户密码,修改成功！");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}
}
