package com.sosgps.wzt.system.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.RoleForm;
import com.sosgps.wzt.system.form.UserForm;
import com.sosgps.wzt.system.service.ModuleManagerService;
import com.sosgps.wzt.system.service.ModuleService;
import com.sosgps.wzt.system.service.RefModuleRoleService;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.tree.TreeNode;

/**
 * <p>
 * Title: LoginAction
 * </p>
 * <p>
 * Description: 系统级角色管理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: 图盟科技
 * </p>
 * 
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class SysRoleAction extends RoleAction {

	//private ModuleService moduleService = (ModuleService) SpringHelper
	//		.getBean("moduleService");
	private RoleService roleService = (RoleService) SpringHelper
			.getBean("roleService");
	private RefModuleRoleService refModuleRoleService;
	private ModuleManagerService moduleManagerService = (ModuleManagerService) SpringHelper
			.getBean("moduleManagerService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String goPage = "init";
		String empCode = request.getParameter("id");// 获得选择企业代码
		RoleForm roleForm = (RoleForm) form;
		List list = null;
		// 取得所有级别权限集合
		//list = this.moduleService.queryAllModuleList();
		// list = this.moduleService.queryModuleByGradeList(new Long(2));
		TRole role=this.getCurrentUser(request).getUser().getRole();
		TreeNode moduleTreeNode=moduleManagerService.getTreeNodeByRole(role.getId());
		roleForm.setList(list);
		roleForm.setEmpCode(empCode);
		request.setAttribute("moduleTreeNode",moduleTreeNode);
		return mapping.findForward(goPage);
	}

	public ActionForward empList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String goPage = "empList";
		return mapping.findForward(goPage);
	}
}
