package com.sosgps.wzt.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.RoleForm;
import com.sosgps.wzt.system.service.ModuleManagerService;
import com.sosgps.wzt.system.service.RefModuleRoleService;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.tree.TreeNode;
import com.sosgps.wzt.util.CharTools;

/**
 * 角色管理
 * 
 * @author 
 * 
 */
public class RoleAction extends DispatchWebActionSupport {
	private ModuleManagerService moduleManagerService = (ModuleManagerService) SpringHelper
			.getBean("moduleManagerService");
	private RoleService roleService = (RoleService) SpringHelper
			.getBean("roleService");
	private RefModuleRoleService refModuleRoleService = (RefModuleRoleService) SpringHelper
			.getBean("refModuleRoleService");

	// TOptLog tOptLog = new TOptLog();

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String empCode = "";

		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			empCode = userInfo.getEmpCode();
		}
		RoleForm roleForm = (RoleForm) form;
		// moduleService = (ModuleService)SpringHelper.getBean("moduleService");

		// List moduleList = moduleService.queryModuleList("1");
		// List list = this.moduleService.queryAllModuleList();
		// List list = null;
		TreeNode moduleTreeNode = null;
		// 超级管理员
		if (empCode.equals(Constants.EMP_ROOT)) {
			// 所以权限
			moduleTreeNode = this.moduleManagerService.getRootTreeNode();
		} else {
			// 用户级别权限
			moduleTreeNode = this.moduleManagerService
					.getTreeNodeByID(new Long(2));
		}

		// roleForm.setModuleList(moduleList);
		// roleForm.setList(list);
		request.setAttribute("moduleTreeNode", moduleTreeNode);
		roleForm.setEmpCode(empCode);
		return mapping.findForward("init");
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
		boolean isSuccescc = this.doCreate(form, request);
		String goPage = "createSuccess";
		if (!isSuccescc) {
			goPage = "message";
		}
		return mapping.findForward(goPage);
	}

	public boolean doCreate(ActionForm form, HttpServletRequest request) {

		RoleForm roleForm = (RoleForm) form;
		String empCode = roleForm.getEmpCode();
		Map<?, ?> orderMap = roleForm.getOrder();
		TRole role = roleForm.getRole();
		TOptLog tOptLog = new TOptLog();
		// moduleService = (ModuleService)SpringHelper.getBean("moduleService");
		String[] moduleIds = roleForm.getModuleIds();
		Long[] LongModuleIds = new Long[moduleIds.length];
		for (int i = 0; i < moduleIds.length; i++) {
			LongModuleIds[i] = new Long(moduleIds[i]);
		}
		//TModule module = null;
		List<?> moduleList = moduleManagerService.queryModuleList(LongModuleIds);
		/*
		 * if (moduleIds != null) { for (int i = 0; i < moduleIds.length; i++) {
		 * module = moduleManagerService.retrieveModule(LongModuleIds[i]);
		 * moduleList.add(module); } }
		 */

		String createBy = "initName";
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		role.setCreateBy(createBy);// 登录 user 信息
		// role.setCreateBy("zw");
		role.setCreateDate(DateUtil.getDateTime());
		role.setUsageFlag("1");
		role.setEmpCode(empCode); // 增加企业id
		roleService.saveRole(role);
		refModuleRoleService = (RefModuleRoleService) SpringHelper
				.getBean("refModuleRoleService");

		// 日志信息
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
		try {
			refModuleRoleService.saveModuleRole(role, moduleList, orderMap);

			tOptLog.setOptDesc("创建 " + roleForm.getRole().getRoleName()
					+ " 角色成功");
			tOptLog.setResult(new Long(1));
			// 保存操作日志
			LogFactory.newLogInstance("optLogger").info(tOptLog);

		} catch (Exception ex) {
			tOptLog.setOptDesc("创建 " + roleForm.getRole().getRoleName()
					+ " 角色失败");
			tOptLog.setResult(new Long(0));
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				request.setAttribute("msg", "保存操作日志失败");
				return false;
			}
			request.setAttribute("msg", "增加角色失败");
			return false;
		}

		return true;
	}

	/**
	 * 删除角色
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
		String goPage = "displayList";
		for (int x = 0; x < sids.length; x++) {
			longIds[x] = Long.valueOf(sids[x]);
		}
		// 获取登录用户信息
		//String createBy = "initName";
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		/*if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}*/
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);

		boolean ret = roleService.deleteAll(longIds);
		// 删除成功
		if (ret) {
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc("删除角色成功");
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			// 删除失败
		} else {
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("删除角色失败");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "删除角色失败");
			goPage = "message";
		}
		return mapping.findForward(goPage);
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
		boolean isSuccess = this.doUpdate(form, request);
		if (!isSuccess) {
			goPage = "message";
			request.setAttribute("msg", "更新角色失败");
		}
		return mapping.findForward(goPage);
	}

	public boolean doUpdate(ActionForm form, HttpServletRequest request)
			throws Exception {
		RoleForm roleForm = (RoleForm) form;
		TRole role = roleForm.getRole();
		String empCode = roleForm.getEmpCode();
		String[] moduleIds = roleForm.getModuleIds();
		//TModule module = null;
		List<?> moduleList = null;// new ArrayList();
		TOptLog tOptLog = new TOptLog();
		Long[] mIds = new Long[moduleIds.length];
		if (moduleIds != null) {
			for (int i = 0; i < moduleIds.length; i++) {
				Long l = Long.valueOf(moduleIds[i]);
				mIds[i] = l;
				// module = this.moduleManagerService.retrieveModule(l);
				// moduleList.add(module);
			}
		}
		moduleList = moduleManagerService.queryModuleList(mIds);
		// 顺序
		Map order = roleForm.getOrder();
		// 更新角色同时更新角色权限关系对应
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		role.setEmpCode(empCode);
		role.setCreateBy(userInfo.getUserAccount());// 登录 user 信息
		role.setCreateDate(DateUtil.getDateTime());
		role.setUsageFlag("1");

		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
		try {
			roleService.updateRole(role);
			refModuleRoleService = (RefModuleRoleService) SpringHelper
					.getBean("refModuleRoleService");
			refModuleRoleService.updateModuleRole(role, moduleList, order);

			tOptLog.setOptDesc("更新 " + roleForm.getRole().getRoleName()
					+ " 角色成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			tOptLog.setOptDesc("更新 " + roleForm.getRole().getRoleName()
					+ " 角色失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return false;
		}
		return true;
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
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm roleForm = (RoleForm) form;
		String id = roleForm.getId();
		// roleService = (RoleService)SpringHelper.getBean("roleService");
		TRole role = roleService.retrieveRole(new Long(id));
		UserInfo userInfo=this.getCurrentUser(request);

		TreeNode moduleTreeNode = moduleManagerService
		.getTreeNodeByRole(role.getId());
		
		roleForm.setRole(role);
		roleForm.setEmpCode(role.getEmpCode());
		//roleForm.setModuleList(moduleList);
		request.setAttribute("moduleTreeNode",moduleTreeNode);
		
		//empRoot用户可以修改任何人，但empDefaultRole即emp默认创建的bossSuperUser不能修改自己，也不能被任何其他人修改
		if(userInfo.getEmpCode().equals(Constants.EMP_ROOT)){
			request.setAttribute("showModify","show");
		}else{
			request.setAttribute("showModify", role.getCreateBy().equalsIgnoreCase(
				Constants.EMP_DEFAULT_ROLE) ? "notShow" : "show");
		}
		return mapping.findForward("view");
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
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm roleForm = (RoleForm) form;
		String id = roleForm.getId();
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		// RoleService roleService = new RoleService();
		TRole role = roleService.retrieveRole(new Long(id));
		Map modulesIds = new HashMap();
		Map order = new HashMap();
		RefModuleRole refModuleRole = null;
		int i = 0;
		for (Iterator it = role.getRefModuleRoles().iterator(); it.hasNext(); i++) {
			refModuleRole = (RefModuleRole) it.next();
			modulesIds.put(refModuleRole.getTModule().getId(), refModuleRole
					.getTModule().getId());
			order.put(refModuleRole.getTModule().getId(), refModuleRole
					.getModuleOrder());

		}
		List moduleList = new ArrayList();

		String useFlag = "1";
		// 查询所有权限列表
		TreeNode moduleTreeNode = null;
		
		if(empCode.equalsIgnoreCase(Constants.EMP_ROOT)){
			moduleTreeNode = moduleManagerService
			.getRootTreeNode();
		}else{
			moduleTreeNode = moduleManagerService
			.getTreeNodeByRole(userInfo.getUser().getRole().getId());
			
		}


		checkTreeNode(moduleTreeNode,modulesIds);
		// moduleList = moduleManagerService.;
		request.setAttribute("moduleIdsMap", modulesIds);
		request.setAttribute("order", order);
		request.setAttribute("moduleTreeNode",moduleTreeNode);
		
		roleForm.setEmpCode(roleForm.getEmpCode());
		roleForm.setRole(role);
		//roleForm.setModuleList(moduleList);
		return mapping.findForward("modify");
	}
	private void checkTreeNode(TreeNode node,Map modulesIds ){
		if(modulesIds.containsKey(node.getId())){
			node.setIsSelected("true");
		}else{
			node.setIsSelected("false");
		}
		if(node.getChildrenSize()>0){
			for(int i=0;i<node.getChildrenSize();i++){
				checkTreeNode((TreeNode)node.getChildrenNodes().get(i),modulesIds);
			}
		}
	}
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ModuleForm moduleForm = (ModuleForm)form;
		// ModuleService moduleService = new ModuleService();

		return mapping.findForward("query");
	}

	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("displayList");
	}
	// sos角色列表
	public ActionForward listRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		//String start = request.getParameter("start");
		//String limit = request.getParameter("limit");
		
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		
		String start = "0";
		String limit = "1000";
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint/limitint+1;
		
		//System.out.println(start+";"+limit+";page = "+page);
		UserInfo user = this.getCurrentUser(request);
		String entCode = user.getEmpCode();
		//json
		StringBuffer jsonSb = new StringBuffer();
		String total = "";
		if(start!=null&&start.length()>0&&limit!=null&&limit.length()>0){
			Page<TRole> roleList = roleService.listRole(entCode, page, limitint, searchValue);
			if(roleList!=null && roleList.getResult()!=null){
				total = "{total:"+roleList.getTotalCount()+",data:[";
		    	Iterator i = roleList.getResult().iterator();
		    	while(i.hasNext()){
		    		TRole roleObj = (TRole)i.next();
		    		//过滤掉超级管理员角色（角色编码为super_administrator）
		    		if(roleObj.getRoleCode().equals("super_administrator"))
		    			continue;
	    			//过滤掉角色模板（角色编码为defaultEntAdminRole）
	    			if(roleObj.getRoleCode().equals("defaultEntAdminRole"))
		    				continue;
		    		//根据roleid查找模块
		    		ModuleManagerService moduleManagerService=(ModuleManagerService)SpringHelper.getBean("moduleManagerService");
		    		TRole tRole=user.getUser().getRole();
		    		Long roleID=tRole.getId();
		    		/*TreeNode treeNode=moduleManagerService.getTreeNodeByRole(tRole);
		    		StringBuffer ids = new StringBuffer();
		    		int len = treeNode.getChildrenSize();
		    		if(len>0){
		    			String tmpids = getModuleIds(treeNode.getChildrenNodes());
		    			if(tmpids!=null&&tmpids.length()>0){
		    				ids.append(","+tmpids);
		    			}
		    		}*/
		    		
		    		jsonSb.append("{");
		    		jsonSb.append("id:'"+roleObj.getId()+"',");//id
		    		jsonSb.append("roleName:'"+CharTools.javaScriptEscape(roleObj.getRoleName())+"',");//角色名称
		    		jsonSb.append("roleCode:'"+CharTools.javaScriptEscape(roleObj.getRoleCode())+"',");//角色编码
		    		jsonSb.append("roleDesc:'"+CharTools.javaScriptEscape(roleObj.getRoleDesc())+"'");//角色描述
		    		//jsonSb.append("ModuleIds:'"+CharTools.javaScriptEscape(ids.toString())+"'");//角色对应的权限
		    		
		    		jsonSb.append("},");
		    	}
		    	if(jsonSb.length()>0){
		    		jsonSb.deleteCharAt(jsonSb.length()-1);
		    	}
		    	jsonSb.append("]}");
			}
		}
		 System.out.println(total+jsonSb.toString());
		 response.setContentType("text/json; charset=utf-8");
		 response.getWriter().write(total+jsonSb.toString());
		return mapping.findForward(null);
	}
	// sos根据企业查询角色列表
	public ActionForward listRoleByEntCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//从request中获取参数
		//String start = request.getParameter("start");
		//String limit = request.getParameter("limit");
		
		String entCode = request.getParameter("entCode");//企业代码
		entCode = CharTools.killNullString(entCode);
		entCode = java.net.URLDecoder.decode(entCode, "utf-8");
		entCode = CharTools.sqlEscape(entCode);
		
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		
		String start = "0";
		String limit = "1000";
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint/limitint+1;
		
		//System.out.println(start+";"+limit+";page = "+page);
		UserInfo user = this.getCurrentUser(request);
		//json
		StringBuffer jsonSb = new StringBuffer();
		String total = "";
		if(start!=null&&start.length()>0&&limit!=null&&limit.length()>0){
			Page<TRole> roleList = roleService.listRole(entCode, page, limitint, searchValue);
			if(roleList!=null && roleList.getResult()!=null){
				total = "{total:"+roleList.getTotalCount()+",data:[";
				Iterator i = roleList.getResult().iterator();
				while(i.hasNext()){
					TRole roleObj = (TRole)i.next();
					//过滤掉超级管理员角色（角色编码为super_administrator）
					if(roleObj.getRoleCode().equals("super_administrator"))
						continue;
					//过滤掉角色模板（角色编码为defaultEntAdminRole）
					if(roleObj.getRoleCode().equals("defaultEntAdminRole"))
						continue;
					//根据roleid查找模块
					ModuleManagerService moduleManagerService=(ModuleManagerService)SpringHelper.getBean("moduleManagerService");
					TRole tRole=user.getUser().getRole();
					Long roleID=tRole.getId();
					/*TreeNode treeNode=moduleManagerService.getTreeNodeByRole(tRole);
		    		StringBuffer ids = new StringBuffer();
		    		int len = treeNode.getChildrenSize();
		    		if(len>0){
		    			String tmpids = getModuleIds(treeNode.getChildrenNodes());
		    			if(tmpids!=null&&tmpids.length()>0){
		    				ids.append(","+tmpids);
		    			}
		    		}*/
					
					jsonSb.append("{");
					jsonSb.append("id:'"+roleObj.getId()+"',");//id
					jsonSb.append("roleName:'"+CharTools.javaScriptEscape(roleObj.getRoleName())+"',");//角色名称
					jsonSb.append("roleCode:'"+CharTools.javaScriptEscape(roleObj.getRoleCode())+"',");//角色编码
					jsonSb.append("roleDesc:'"+CharTools.javaScriptEscape(roleObj.getRoleDesc())+"'");//角色描述
					//jsonSb.append("ModuleIds:'"+CharTools.javaScriptEscape(ids.toString())+"'");//角色对应的权限
					
					jsonSb.append("},");
				}
				if(jsonSb.length()>0){
					jsonSb.deleteCharAt(jsonSb.length()-1);
				}
				jsonSb.append("]}");
			}
		}
		System.out.println(total+jsonSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(total+jsonSb.toString());
		return mapping.findForward(null);
	}
	
	
	public ActionForward comboboxRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		//String start = request.getParameter("start");
		//String limit = request.getParameter("limit");
		
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		
		String start = "0";
		String limit = "1000";
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint/limitint+1;
		
		//System.out.println(start+";"+limit+";page = "+page);
		UserInfo user = this.getCurrentUser(request);
		String entCode = user.getEmpCode();
		//json
		StringBuffer jsonSb = new StringBuffer();
		//String total = "";
		if(start!=null&&start.length()>0&&limit!=null&&limit.length()>0){
			Page<TRole> roleList = roleService.listRole(entCode, page, limitint, searchValue);
			if(roleList!=null && roleList.getResult()!=null){
//				total = "{total:"+roleList.getTotalCount()+",data:[";
		    	Iterator i = roleList.getResult().iterator();
		    	jsonSb.append("[");
		    	while(i.hasNext()){
		    		TRole roleObj = (TRole)i.next();
		    		//过滤掉超级管理员角色（角色编码为super_administrator）
		    		if(roleObj.getRoleCode().equals("super_administrator"))
		    			continue;
	    			//过滤掉角色模板（角色编码为defaultEntAdminRole）
	    			if(roleObj.getRoleCode().equals("defaultEntAdminRole"))
		    				continue;
		    		jsonSb.append("[");
		    		jsonSb.append("'"+roleObj.getId()+"',");//id
		    		jsonSb.append("'"+roleObj.getRoleName()+"'");//角色名称
		    		jsonSb.append("],");
		    		//jsonSb.append("},");
		    	}
		    	if(jsonSb.length()>0){
		    		jsonSb.deleteCharAt(jsonSb.length()-1);
		    	}
		    	jsonSb.append("]");
			}
		}
		 //System.out.println(jsonSb.toString());
		 response.setContentType("text/json; charset=utf-8");
		 response.getWriter().write(jsonSb.toString());
		return mapping.findForward(null);
	}
	
	//取得moduleids
	public String getModuleIds(Set<RefModuleRole> modules){
		StringBuffer ids = new StringBuffer();
		Iterator<RefModuleRole> i = modules.iterator();
		while(i.hasNext()){
			
			RefModuleRole ref = i.next();
			System.out.println("ref.getTModule().getId():"+ref.getTModule().getId());
			ids.append(ref.getTModule().getId());
			ids.append(",");
		}
		return ids.toString();
		
	}
	// sos增加角色
	public ActionForward addRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		String roleName = request.getParameter("roleName");//角色名称
		String roleCode = request.getParameter("roleCode");//角色编码
		String roleDesc = request.getParameter("roleDesc");//角色说明
		String moduleIds= request.getParameter("moduleIds");//权限id，多个用","隔开
		
		
		roleName = CharTools.killNullString(roleName);
		roleCode = CharTools.killNullString(roleCode);
		roleDesc = CharTools.killNullString(roleDesc);
		
		
		roleName = java.net.URLDecoder.decode(roleName, "utf-8");
		roleCode = java.net.URLDecoder.decode(roleCode, "utf-8");
		roleDesc = java.net.URLDecoder.decode(roleDesc, "utf-8");
		
		roleName = CharTools.killNullString(roleName);
		roleCode = CharTools.killNullString(roleCode);
		roleDesc = CharTools.killNullString(roleDesc);
		moduleIds = CharTools.killNullString(moduleIds);

		List<String> moduleList = new ArrayList<String>();
		String[] ids = CharTools.split(moduleIds, ",");
		for (int i = 0; i < ids.length; i++) {
			moduleList.add(ids[i]);
		}
		
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// 查询角色编码是否存在
		TRole r = roleService.findByRoleCode(entCode, roleCode);// 角色编码不允许重复
		if(r!=null){
			response.getWriter().write("{result:\"3\"}");//已存在此角色编码
		}else{
			
			// 增加
			TRole role = new TRole();
			role.setCreateBy(createBy);
			role.setCreateDate(DateUtil.getDateTime());
			role.setEmpCode(entCode);
			role.setRoleName(roleName);
			role.setRoleCode(roleCode);
			role.setRoleDesc(roleDesc);
			role.setUsageFlag("1");
			
			roleService.saveRole(role);
			
			// 日志信息
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
			try {
				Map orderMap = null;// 该参数已废弃，接口未更新，传null即可
				refModuleRoleService.saveModuleRole(role, moduleList, orderMap );
	
				tOptLog.setOptDesc("创建 " + roleName
						+ " 角色成功");
				tOptLog.setResult(new Long(1));
				// 保存操作日志
				LogFactory.newLogInstance("optLogger").info(tOptLog);
				response.getWriter().write("{result:\"1\"}");//成功
			} catch (Exception ex) {
				tOptLog.setOptDesc("创建 " + roleName
						+ " 角色失败");
				tOptLog.setResult(new Long(0));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
				}
				response.getWriter().write("{result:\"2\"}");//失败
			}
		}
		return mapping.findForward(null);
	}
	// sos修改角色
	public ActionForward updateRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		String roleId = request.getParameter("roleId");//角色id
		String roleName = request.getParameter("roleName");//角色名称
		String roleCode = request.getParameter("roleCode");//角色编码
		String roleDesc = request.getParameter("roleDesc");//角色说明
		String moduleIds= request.getParameter("moduleIds");//权限id，多个用","隔开
		
		
		roleName = CharTools.killNullString(roleName);
		roleCode = CharTools.killNullString(roleCode);
		roleDesc = CharTools.killNullString(roleDesc);
		
		
		roleName = java.net.URLDecoder.decode(roleName, "utf-8");
		roleCode = java.net.URLDecoder.decode(roleCode, "utf-8");
		roleDesc = java.net.URLDecoder.decode(roleDesc, "utf-8");
		
		roleName = CharTools.killNullString(roleName);
		roleCode = CharTools.killNullString(roleCode);
		roleDesc = CharTools.killNullString(roleDesc);
		moduleIds = CharTools.killNullString(moduleIds);
		
		//System.out.println("moduleIds:"+moduleIds);
		
		List moduleList = new ArrayList();
		String[] ids = CharTools.split(moduleIds, ",");
		for (int i = 0; i < ids.length; i++) {
			moduleList.add(ids[i]);
		}
		
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		
		Long role_id = Long.parseLong(roleId);
		// 获取未更新前角色信息
		TRole _role = roleService.retrieveRole(role_id);
		_role.setRoleName(roleName);
		// 角色编码需要修改
		if(!_role.getRoleCode().equals(roleCode)){
			// 查询角色编码是否存在
			TRole r = roleService.findByRoleCode(entCode, roleCode);// 角色编码不允许重复
			if(r!=null){
				response.getWriter().write("{result:\"3\"}");//已存在此角色编码
				return mapping.findForward(null);
			}else{
				_role.setRoleCode(roleCode);
			}
		}
		_role.setRoleDesc(roleDesc);
		
		// 日志信息
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
		try {
			// 更新角色同时更新角色权限关系对应
			roleService.updateRole(_role);
			refModuleRoleService = (RefModuleRoleService) SpringHelper
					.getBean("refModuleRoleService");
			Map order = null;// 该参数已废弃，接口未更新，传null即可
			refModuleRoleService.updateModuleRole(_role, moduleList, order);
					tOptLog.setOptDesc("更新 " + roleName
					+ " 角色成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"1\"}");//成功
		} catch (Exception ex) {
			tOptLog.setOptDesc("更新 " + roleName
					+ " 角色失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"2\"}");//失败
		}
		return mapping.findForward(null);
	}
	// sos 删除角色
	public ActionForward deleteRoles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		String roleIds = request.getParameter("roleIds");//角色id，多个用","隔开
		StringTokenizer st = new StringTokenizer(roleIds, ",");
		Long[] longIds = new Long[st.countTokens()];
		for (int x = 0; st.hasMoreElements();x ++) {
			longIds[x] = Long.valueOf(st.nextToken());
		}
		
		
		UserInfo userInfo = this.getCurrentUser(request);
		// 获取登录用户信息
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// 日志信息
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
		
		
		boolean ret = roleService.deleteAll(longIds);
		if(ret){
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc("删除角色成功");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"1\"}");//成功
		}else{
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("删除角色失败");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "删除角色失败");
			response.getWriter().write("{result:\"2\"}");//失败
		}
		return mapping.findForward(null);
	}
	
	/**
	 * 根据角色id查找权限ids  sos
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getModuleidsByRoleId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("roleid");
//		TRole role = roleService.retrieveRole(new Long(id));
		//UserInfo userInfo=this.getCurrentUser(request);
		

		//		TreeNode moduleTreeNode = moduleManagerService.getTreeNodeByRole(role.getId());
		Long roleId = Long.parseLong(id);
		TRole role = roleService.queryRoleAndModulesById(roleId);
		Set<RefModuleRole> modules = role.getRefModuleRoles();
		
		StringBuffer ids = new StringBuffer();
		int len = modules.size();
		if(len>0){
			String tmpids = getModuleIds(modules);
			if(tmpids!=null&&tmpids.length()>0){
				ids.append(","+tmpids);
			}
		}
		System.out.println("{result:\""+ids.toString()+"\"}");
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{result:\""+ids.toString()+"\"}");
		response.getWriter().flush();
		return mapping.findForward(null);
		
		//empRoot用户可以修改任何人，但empDefaultRole即emp默认创建的bossSuperUser不能修改自己，也不能被任何其他人修改
		/*if(userInfo.getEmpCode().equals(Constants.EMP_ROOT)){
			request.setAttribute("showModify","show");
		}else{
			request.setAttribute("showModify", role.getCreateBy().equalsIgnoreCase(
				Constants.EMP_DEFAULT_ROLE) ? "notShow" : "show");
		}*/
		//return mapping.findForward("view");
	}
}
