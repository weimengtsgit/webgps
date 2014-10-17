package com.sosgps.wzt.system.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.UserForm;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.system.service.UserService;

public class SysUserAction extends UserAction {

	private UserService userService = (UserService) SpringHelper
			.getBean("userService");;
	private RoleService roleService = (RoleService) SpringHelper
			.getBean("roleService");;
	public static Logger log = Logger.getLogger(UserAction.class);

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String goPage = "init";
		String empCode = request.getParameter("id");
		UserForm userForm = (UserForm) form;
		roleService = (RoleService) SpringHelper.getBean("roleService");
		List roleList = roleService.getRoleByEmpCode(empCode);
		userForm.setEmpCode(empCode);
		userForm.setRoleList(roleList);
		return mapping.findForward(goPage);
	}
	public ActionForward empList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String goPage = "empList";
		return mapping.findForward(goPage);
	}
}
