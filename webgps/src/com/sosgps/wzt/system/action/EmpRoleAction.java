package com.sosgps.wzt.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.RoleForm;
import com.sosgps.wzt.system.service.ModuleService;
import com.sosgps.wzt.system.service.RefModuleRoleService;
import com.sosgps.wzt.system.service.RoleService;

/**
 * <p>Title: LoginAction</p>
 * <p>Description: 企业级角色管理</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class EmpRoleAction extends RoleAction{
	
	private ModuleService moduleService = (ModuleService) SpringHelper
	.getBean("moduleService");
	private RoleService roleService = (RoleService) SpringHelper
	.getBean("roleService");
	private RefModuleRoleService refModuleRoleService;
	
	/**
	 * 企业管理员创建角色
	 * 策略：创建的角色权限不应大于企业管理员
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String empCode ="";
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			empCode = userInfo.getEmpCode();
		}
		RoleForm roleForm = (RoleForm) form;
		List list  = null;
		
		//用户级别权限
		list = this.moduleService.queryModuleByGradeList(new Long(2));
		//list = this.moduleService.queryModuleByEmpCode(empCode);
		
		//roleForm.setModuleList(moduleList);
		roleForm.setList(list);
		roleForm.setEmpCode(empCode);
		return mapping.findForward("init");
	}
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm roleForm = (RoleForm) form;
		String id = roleForm.getId();
		// roleService = (RoleService)SpringHelper.getBean("roleService");
		TRole role = roleService.retrieveRole(new Long(id));
		TModule module = null;
		List moduleList = new ArrayList();
		RefModuleRole refModuleRole = null;
		for (Iterator it = role.getRefModuleRoles().iterator(); it.hasNext();) {
			refModuleRole = (RefModuleRole) it.next();
		//	module = refModuleRole.getTModule();
			moduleList.add(module);
		}
		roleForm.setRole(role);
		roleForm.setList(moduleList);
		roleForm.setEmpCode(role.getEmpCode());
		roleForm.setModuleList(moduleList);
		return mapping.findForward("viewEmp");
	}
	/*
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm roleForm = (RoleForm) form;
		String id = roleForm.getId();
		
		// RoleService roleService = new RoleService();
		TRole role = roleService.retrieveRole(new Long(id));
		String empCode = role.getEmpCode();
		Map modulesIds = new HashMap();
		Map order = new HashMap();
		RefModuleRole refModuleRole = null;
		int i = 0;
		for (Iterator it = role.getRefModuleRoles().iterator(); it.hasNext(); i++) {
			refModuleRole = (RefModuleRole) it.next();
		//	modulesIds.put(refModuleRole.getTModule().getId(), refModuleRole
		//			.getTModule().getId());
		//	order.put(refModuleRole.getTModule().getId(), refModuleRole
		//			.getModuleOrder());

		}
		List moduleList = new ArrayList();

		String useFlag = "1";
		// 查询所有权限列表
		//moduleList = moduleService.queryModuleList(useFlag);
		
		//moduleList = this.moduleService.queryModuleByEmpCode(empCode);
		//用户级别权限
		moduleList = this.moduleService.queryModuleByGradeList(new Long(2));
		request.setAttribute("moduleIdsMap", modulesIds);
		request.setAttribute("order", order);
		roleForm.setEmpCode(roleForm.getEmpCode());
		roleForm.setRole(role);
		roleForm.setList(moduleList);
		//roleForm.setModuleList(moduleList);
		return mapping.findForward("modify");
	}

	/**
	 * 更加角色
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String goPage = "createSuccess";
		this.doUpdate(form, request);
		
		return mapping.findForward(goPage);
	}
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String goPage = "createSuccess";
		this.doCreate(form, request);
		
		return mapping.findForward(goPage);
	}
	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("displayList");
	}
}
