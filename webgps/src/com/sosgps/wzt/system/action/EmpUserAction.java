package com.sosgps.wzt.system.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.UserForm;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.system.service.UserService;
/**
 * @Title:��ҵ�û�����
 * @Description:�û������漰����ɾ���ġ��鹦�ܲ���
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: zhangwei
 * @version 1.0
 * @date: 2008-8-23 ����02:41:11
 */
public class EmpUserAction extends UserAction {

	private UserService userService = (UserService) SpringHelper
			.getBean("userService");;
	private RoleService roleService = (RoleService) SpringHelper
			.getBean("roleService");;
	public static Logger log = Logger.getLogger(UserAction.class);

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String goPage = "init";
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = request.getParameter("empCode");
		if (empCode == null) {
			if (userInfo != null) {
				empCode = userInfo.getEmpCode();// ��¼�û�������ҵ
			}
		}
		UserForm userForm = (UserForm) form;
		roleService = (RoleService) SpringHelper.getBean("roleService");
		//List roleList = roleService.getAllRole();
		List roleList = roleService.getRoleByEmpCode(empCode);
		userForm.setEmpCode(empCode);
		userForm.setRoleList(roleList);

		return mapping.findForward(goPage);
	}

}
