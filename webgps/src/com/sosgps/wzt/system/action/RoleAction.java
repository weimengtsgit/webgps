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
 * ��ɫ����
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
		// ��������Ա
		if (empCode.equals(Constants.EMP_ROOT)) {
			// ����Ȩ��
			moduleTreeNode = this.moduleManagerService.getRootTreeNode();
		} else {
			// �û�����Ȩ��
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
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		role.setCreateBy(createBy);// ��¼ user ��Ϣ
		// role.setCreateBy("zw");
		role.setCreateDate(DateUtil.getDateTime());
		role.setUsageFlag("1");
		role.setEmpCode(empCode); // ������ҵid
		roleService.saveRole(role);
		refModuleRoleService = (RefModuleRoleService) SpringHelper
				.getBean("refModuleRoleService");

		// ��־��Ϣ
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
		try {
			refModuleRoleService.saveModuleRole(role, moduleList, orderMap);

			tOptLog.setOptDesc("���� " + roleForm.getRole().getRoleName()
					+ " ��ɫ�ɹ�");
			tOptLog.setResult(new Long(1));
			// ���������־
			LogFactory.newLogInstance("optLogger").info(tOptLog);

		} catch (Exception ex) {
			tOptLog.setOptDesc("���� " + roleForm.getRole().getRoleName()
					+ " ��ɫʧ��");
			tOptLog.setResult(new Long(0));
			try {
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			} catch (Exception e) {
				request.setAttribute("msg", "���������־ʧ��");
				return false;
			}
			request.setAttribute("msg", "���ӽ�ɫʧ��");
			return false;
		}

		return true;
	}

	/**
	 * ɾ����ɫ
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
		// ��ȡ��¼�û���Ϣ
		//String createBy = "initName";
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		/*if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}*/
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);

		boolean ret = roleService.deleteAll(longIds);
		// ɾ���ɹ�
		if (ret) {
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc("ɾ����ɫ�ɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			// ɾ��ʧ��
		} else {
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("ɾ����ɫʧ��");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "ɾ����ɫʧ��");
			goPage = "message";
		}
		return mapping.findForward(goPage);
	}

	/**
	 * ���ӽ�ɫ
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
			request.setAttribute("msg", "���½�ɫʧ��");
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
		// ˳��
		Map order = roleForm.getOrder();
		// ���½�ɫͬʱ���½�ɫȨ�޹�ϵ��Ӧ
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		role.setEmpCode(empCode);
		role.setCreateBy(userInfo.getUserAccount());// ��¼ user ��Ϣ
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

			tOptLog.setOptDesc("���� " + roleForm.getRole().getRoleName()
					+ " ��ɫ�ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			tOptLog.setOptDesc("���� " + roleForm.getRole().getRoleName()
					+ " ��ɫʧ��");
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
		
		//empRoot�û������޸��κ��ˣ���empDefaultRole��empĬ�ϴ�����bossSuperUser�����޸��Լ���Ҳ���ܱ��κ��������޸�
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
		// ��ѯ����Ȩ���б�
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
	// sos��ɫ�б�
	public ActionForward listRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��request�л�ȡ����
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
		    		//���˵���������Ա��ɫ����ɫ����Ϊsuper_administrator��
		    		if(roleObj.getRoleCode().equals("super_administrator"))
		    			continue;
	    			//���˵���ɫģ�壨��ɫ����ΪdefaultEntAdminRole��
	    			if(roleObj.getRoleCode().equals("defaultEntAdminRole"))
		    				continue;
		    		//����roleid����ģ��
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
		    		jsonSb.append("roleName:'"+CharTools.javaScriptEscape(roleObj.getRoleName())+"',");//��ɫ����
		    		jsonSb.append("roleCode:'"+CharTools.javaScriptEscape(roleObj.getRoleCode())+"',");//��ɫ����
		    		jsonSb.append("roleDesc:'"+CharTools.javaScriptEscape(roleObj.getRoleDesc())+"'");//��ɫ����
		    		//jsonSb.append("ModuleIds:'"+CharTools.javaScriptEscape(ids.toString())+"'");//��ɫ��Ӧ��Ȩ��
		    		
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
	// sos������ҵ��ѯ��ɫ�б�
	public ActionForward listRoleByEntCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//��request�л�ȡ����
		//String start = request.getParameter("start");
		//String limit = request.getParameter("limit");
		
		String entCode = request.getParameter("entCode");//��ҵ����
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
					//���˵���������Ա��ɫ����ɫ����Ϊsuper_administrator��
					if(roleObj.getRoleCode().equals("super_administrator"))
						continue;
					//���˵���ɫģ�壨��ɫ����ΪdefaultEntAdminRole��
					if(roleObj.getRoleCode().equals("defaultEntAdminRole"))
						continue;
					//����roleid����ģ��
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
					jsonSb.append("roleName:'"+CharTools.javaScriptEscape(roleObj.getRoleName())+"',");//��ɫ����
					jsonSb.append("roleCode:'"+CharTools.javaScriptEscape(roleObj.getRoleCode())+"',");//��ɫ����
					jsonSb.append("roleDesc:'"+CharTools.javaScriptEscape(roleObj.getRoleDesc())+"'");//��ɫ����
					//jsonSb.append("ModuleIds:'"+CharTools.javaScriptEscape(ids.toString())+"'");//��ɫ��Ӧ��Ȩ��
					
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
		//��request�л�ȡ����
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
		    		//���˵���������Ա��ɫ����ɫ����Ϊsuper_administrator��
		    		if(roleObj.getRoleCode().equals("super_administrator"))
		    			continue;
	    			//���˵���ɫģ�壨��ɫ����ΪdefaultEntAdminRole��
	    			if(roleObj.getRoleCode().equals("defaultEntAdminRole"))
		    				continue;
		    		jsonSb.append("[");
		    		jsonSb.append("'"+roleObj.getId()+"',");//id
		    		jsonSb.append("'"+roleObj.getRoleName()+"'");//��ɫ����
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
	
	//ȡ��moduleids
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
	// sos���ӽ�ɫ
	public ActionForward addRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��request�л�ȡ����
		String roleName = request.getParameter("roleName");//��ɫ����
		String roleCode = request.getParameter("roleCode");//��ɫ����
		String roleDesc = request.getParameter("roleDesc");//��ɫ˵��
		String moduleIds= request.getParameter("moduleIds");//Ȩ��id�������","����
		
		
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
		
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// ��ѯ��ɫ�����Ƿ����
		TRole r = roleService.findByRoleCode(entCode, roleCode);// ��ɫ���벻�����ظ�
		if(r!=null){
			response.getWriter().write("{result:\"3\"}");//�Ѵ��ڴ˽�ɫ����
		}else{
			
			// ����
			TRole role = new TRole();
			role.setCreateBy(createBy);
			role.setCreateDate(DateUtil.getDateTime());
			role.setEmpCode(entCode);
			role.setRoleName(roleName);
			role.setRoleCode(roleCode);
			role.setRoleDesc(roleDesc);
			role.setUsageFlag("1");
			
			roleService.saveRole(role);
			
			// ��־��Ϣ
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
			try {
				Map orderMap = null;// �ò����ѷ������ӿ�δ���£���null����
				refModuleRoleService.saveModuleRole(role, moduleList, orderMap );
	
				tOptLog.setOptDesc("���� " + roleName
						+ " ��ɫ�ɹ�");
				tOptLog.setResult(new Long(1));
				// ���������־
				LogFactory.newLogInstance("optLogger").info(tOptLog);
				response.getWriter().write("{result:\"1\"}");//�ɹ�
			} catch (Exception ex) {
				tOptLog.setOptDesc("���� " + roleName
						+ " ��ɫʧ��");
				tOptLog.setResult(new Long(0));
				try {
					LogFactory.newLogInstance("optLogger").info(tOptLog);
				} catch (Exception e) {
				}
				response.getWriter().write("{result:\"2\"}");//ʧ��
			}
		}
		return mapping.findForward(null);
	}
	// sos�޸Ľ�ɫ
	public ActionForward updateRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��request�л�ȡ����
		String roleId = request.getParameter("roleId");//��ɫid
		String roleName = request.getParameter("roleName");//��ɫ����
		String roleCode = request.getParameter("roleCode");//��ɫ����
		String roleDesc = request.getParameter("roleDesc");//��ɫ˵��
		String moduleIds= request.getParameter("moduleIds");//Ȩ��id�������","����
		
		
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
		
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		
		Long role_id = Long.parseLong(roleId);
		// ��ȡδ����ǰ��ɫ��Ϣ
		TRole _role = roleService.retrieveRole(role_id);
		_role.setRoleName(roleName);
		// ��ɫ������Ҫ�޸�
		if(!_role.getRoleCode().equals(roleCode)){
			// ��ѯ��ɫ�����Ƿ����
			TRole r = roleService.findByRoleCode(entCode, roleCode);// ��ɫ���벻�����ظ�
			if(r!=null){
				response.getWriter().write("{result:\"3\"}");//�Ѵ��ڴ˽�ɫ����
				return mapping.findForward(null);
			}else{
				_role.setRoleCode(roleCode);
			}
		}
		_role.setRoleDesc(roleDesc);
		
		// ��־��Ϣ
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_ROLE_CODE);
		try {
			// ���½�ɫͬʱ���½�ɫȨ�޹�ϵ��Ӧ
			roleService.updateRole(_role);
			refModuleRoleService = (RefModuleRoleService) SpringHelper
					.getBean("refModuleRoleService");
			Map order = null;// �ò����ѷ������ӿ�δ���£���null����
			refModuleRoleService.updateModuleRole(_role, moduleList, order);
					tOptLog.setOptDesc("���� " + roleName
					+ " ��ɫ�ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"1\"}");//�ɹ�
		} catch (Exception ex) {
			tOptLog.setOptDesc("���� " + roleName
					+ " ��ɫʧ��");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"2\"}");//ʧ��
		}
		return mapping.findForward(null);
	}
	// sos ɾ����ɫ
	public ActionForward deleteRoles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��request�л�ȡ����
		String roleIds = request.getParameter("roleIds");//��ɫid�������","����
		StringTokenizer st = new StringTokenizer(roleIds, ",");
		Long[] longIds = new Long[st.countTokens()];
		for (int x = 0; st.hasMoreElements();x ++) {
			longIds[x] = Long.valueOf(st.nextToken());
		}
		
		
		UserInfo userInfo = this.getCurrentUser(request);
		// ��ȡ��¼�û���Ϣ
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// ��־��Ϣ
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
			tOptLog.setOptDesc("ɾ����ɫ�ɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"1\"}");//�ɹ�
		}else{
			tOptLog.setResult(new Long(0));
			tOptLog.setOptDesc("ɾ����ɫʧ��");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "ɾ����ɫʧ��");
			response.getWriter().write("{result:\"2\"}");//ʧ��
		}
		return mapping.findForward(null);
	}
	
	/**
	 * ���ݽ�ɫid����Ȩ��ids  sos
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
		
		//empRoot�û������޸��κ��ˣ���empDefaultRole��empĬ�ϴ�����bossSuperUser�����޸��Լ���Ҳ���ܱ��κ��������޸�
		/*if(userInfo.getEmpCode().equals(Constants.EMP_ROOT)){
			request.setAttribute("showModify","show");
		}else{
			request.setAttribute("showModify", role.getCreateBy().equalsIgnoreCase(
				Constants.EMP_DEFAULT_ROLE) ? "notShow" : "show");
		}*/
		//return mapping.findForward("view");
	}
}
